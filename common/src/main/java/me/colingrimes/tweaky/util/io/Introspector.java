package me.colingrimes.tweaky.util.io;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Utility to retrieve and load tweaks. Taken and adapted from the Midnight library.
 * @see <a href=https://github.com/ColinGrime/Midnight/blob/master/src/main/java/me/colingrimes/midnight/util/io/Introspector.java>Midnight</a>
 */
public final class Introspector {

	private static final String IGNORE_PACKAGE = "/hidden/";

	@Nonnull
	public static List<Tweak> getTweaks(@Nonnull Tweaky plugin) {
		List<Class<?>> classes = getClasses(plugin.getClass().getClassLoader(), plugin.getClass().getPackage().getName() + ".tweak.implementation");
		return instantiateTweaks(classes, plugin);
	}

	/**
	 * Gets all classes in the given package, one level deep.
	 *
	 * @param classLoader the class loader that is getting the classes
	 * @param packageName the fully qualified package name
	 * @return the list of classes
	 */
	@Nonnull
	private static List<Class<?>> getClasses(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return walkFileSystem(classLoader, packageName);
	}

	/**
	 * Instantiates the tweak classes.
	 *
	 * @param classes the classes to instantiate
	 * @param plugin the tweaky plugin
	 * @return the instantiated classes
	 */
	@Nonnull
	private static List<Tweak> instantiateTweaks(@Nonnull List<Class<?>> classes, @Nonnull Tweaky plugin) {
		List<Tweak> instances = new ArrayList<>();
		for (Class<?> clazz : classes) {
			try {
				instances.add((Tweak) clazz.getConstructor(Tweaky.class).newInstance(plugin));
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
					 NoSuchMethodException e) {
				throw new RuntimeException("[Tweaky] Introspector has failed to instantiate a class:", e);
			}
		}
		return instances;
	}

	/**
	 * Walks the file system starting from a package, retrieving either classes.
	 * This method differentiates between JAR files and directories and adjusts its behavior accordingly.
	 *
	 * @param classLoader the class loader used to locate and retrieve classes
	 * @param packageName the fully qualified package name
	 * @return a list of classes
	 */
	@Nonnull
	private static List<Class<?>> walkFileSystem(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		String packagePath = packageName.replace('.', '/');
		URI uri = getUri(classLoader, packagePath);
		if (uri == null) {
			return new ArrayList<>();
		}

		// Only support JAR files.
		if (!uri.getScheme().equals("jar")) {
			throw new IllegalArgumentException("[Tweaky] Unsupported URI scheme: " + uri.getScheme());
		}

		// If the URI is a JAR, walk the JAR.
		try (FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>())) {
			return walkFileSystem(classLoader, fileSystem.getPath(packagePath));
		} catch (IOException e) {
			throw new RuntimeException("[Tweaky] Introspector has failed to walk the JAR file system:", e);
		}
	}

	/**
	 * Walks the file system from a starting path, retrieving classes.
	 *
	 * @param classLoader  the class loader used to locate and retrieve classes
	 * @param startingPath the starting path from which to begin the search
	 * @return a list of classes
	 */
	@Nonnull
	private static List<Class<?>> walkFileSystem(@Nonnull ClassLoader classLoader, @Nonnull Path startingPath) {
		try {
			ClassFileVisitor fileVisitor = new ClassFileVisitor(classLoader);
			Files.walkFileTree(startingPath, Set.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, fileVisitor);
			return fileVisitor.getList();
		} catch (IOException e) {
			throw new RuntimeException("[Tweaky] Introspector has failed to walk the file system:", e);
		}
	}

	/**
	 * Retrieves the URI of a resource based on its path, using the provided class loader.
	 * This method is useful for distinguishing resources located inside JAR files from those in directories.
	 *
	 * @param classLoader the class loader used to locate the resource
	 * @param path        the path of the resource to be located
	 * @return the URI of the resource
	 */
	@Nullable
	private static URI getUri(@Nonnull ClassLoader classLoader, @Nonnull String path) {
		URL resourceUrl = classLoader.getResource(path);
		if (resourceUrl == null) {
			return null;
		}

		try {
			return resourceUrl.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("[Tweaky] Invalid URI syntax for resource path: " + path, e);
		}
	}


	private static class ClassFileVisitor extends SimpleFileVisitor<Path> {

		private final List<Class<?>> list = new ArrayList<>();
		private ClassLoader classLoader;

		public ClassFileVisitor(@Nullable ClassLoader classLoader) {
			this.classLoader = classLoader;
		}

		/**
		 * Gets the list of items found by the visitor.
		 *
		 * @return the list of items
		 */
		@Nonnull
		public List<Class<?>> getList() {
			return list;
		}

		@Nonnull
		@Override
		public FileVisitResult visitFile(@Nonnull Path file, @Nullable BasicFileAttributes attrs) {
			if (!file.toString().endsWith(".class") || file.toString().contains("$") || file.toString().contains(IGNORE_PACKAGE)) {
				return FileVisitResult.CONTINUE;
			}

			addClass(toQualifiedName(file));
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Adds the given class to the list of classes.
		 * Uses the ClassLoader of the plugin to load the class to avoid incorrect dependency warnings.
		 *
		 * @param className the fully qualified class name
		 */
		private void addClass(@Nonnull String className) {
			if (classLoader == null) {
				classLoader = getClass().getClassLoader();
				Logger.warn("No class loader provided, attempting to use system class loader.");
			}

			try {
				getList().add(Class.forName(className, false, classLoader));
			} catch (ClassNotFoundException | NoClassDefFoundError e) {
				Logger.severe("Class '" + className + "' could not be found/loaded:", e);
			}
		}

		/**
		 * Converts a file system path to a fully qualified Java class or package name.
		 * <p>
		 * This method processes the path, removes any file extension related to class files,
		 * and formats it to match Java's naming convention for classes and packages.
		 * </p>
		 *
		 * @param path the file system path representing a class or a package
		 * @return the fully qualified Java class or package name
		 */
		@Nonnull
		private String toQualifiedName(@Nonnull Path path) {
			String name = path.toString();
			name = name.replace(".class", "");
			name = name.replace("\\", ".").replace("/", ".");
			return name;
		}
	}

	private Introspector() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
