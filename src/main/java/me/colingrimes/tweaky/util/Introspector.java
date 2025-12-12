package me.colingrimes.tweaky.util;

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
 * Utility to retrieve and load classes. Taken and adapted from the Midnight library.
 * @see <a href=https://github.com/ColinGrime/Midnight/blob/master/src/main/java/me/colingrimes/midnight/util/io/Introspector.java>Midnight</a>
 */
public final class Introspector {

	/**
	 * Gets all classes in the given package, one level deep.
	 *
	 * @param classLoader the class loader that is getting the classes
	 * @param packageName the fully qualified package name
	 * @return the list of classes
	 */
	@Nonnull
	public static List<Class<?>> getClasses(@Nonnull ClassLoader classLoader, @Nonnull String packageName) {
		return walkFileSystem(classLoader, packageName);
	}

	/**
	 * Instantiates the classes and converts them into the given class.
	 *
	 * @param classes the classes to instantiate
	 * @param type the class type to convert the classes to
	 * @param args optional args to pass into the constructor
	 * @return the instantiated classes
	 */
	@Nonnull
	public static <T> List<T> instantiateClasses(@Nonnull List<Class<?>> classes, @Nonnull Class<T> type, Object...args) {
		List<T> instances = new ArrayList<>();
		for (Class<?> clazz : classes) {
			try {
				if (args.length == 0) {
					instances.add(type.cast(clazz.getConstructor().newInstance()));
				} else {
					Class<?>[] parameters = Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
					instances.add(type.cast(clazz.getConstructor(parameters).newInstance(args)));
				}
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
					 NoSuchMethodException e) {
				Logger.severe("[Tweaky] Introspector has failed to instantiate a class:", e);
				throw new RuntimeException(e);
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

		// If the URI is a file, walk the directory.
		if (uri.getScheme().equals("file")) {
			Path startingPath = Paths.get(uri);
			return walkFileSystem(classLoader, packageName, startingPath);
		} else if (!uri.getScheme().equals("jar")) {
			throw new IllegalArgumentException("Unsupported URI scheme: " + uri.getScheme());
		}

		// If the URI is a JAR, walk the JAR.
		try (FileSystem fileSystem = FileSystems.newFileSystem(uri, new HashMap<>())) {
			Path startingPath = fileSystem.getPath(packagePath);
			return walkFileSystem(classLoader, packageName, startingPath);
		} catch (IOException e) {
			Logger.severe("[Tweaky] Introspector has failed to walk the JAR file system:", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Walks the file system from a starting path, retrieving classes.
	 *
	 * @param classLoader  the class loader used to locate and retrieve classes
	 * @param packageName  the fully qualified package name
	 * @param startingPath the starting path from which to begin the search
	 * @return a list of classes
	 */
	@Nonnull
	private static List<Class<?>> walkFileSystem(@Nonnull ClassLoader classLoader, @Nonnull String packageName, @Nonnull Path startingPath) {
		try {
			ClassFileVisitor fileVisitor = new ClassFileVisitor(startingPath, packageName, classLoader);
			Files.walkFileTree(startingPath, Set.of(FileVisitOption.FOLLOW_LINKS), 1, fileVisitor);
			return fileVisitor.getList();
		} catch (IOException e) {
			Logger.severe("[Tweaky] Introspector has failed to walk the file system:", e);
			throw new RuntimeException(e);
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
			throw new RuntimeException("Invalid URI syntax for resource path: " + path, e);
		}
	}


	private static class ClassFileVisitor extends SimpleFileVisitor<Path> {

		private final List<Class<?>> list = new ArrayList<>();
		private final Path startingPath;
		private final String packageName;
		private ClassLoader classLoader;

		public ClassFileVisitor(@Nonnull Path startingPath, @Nonnull String packageName, @Nullable ClassLoader classLoader) {
			String normalizedPath = startingPath.toString().replace("\\", "/");
			String normalizedPackage = packageName.replace(".", "/");
			if (!normalizedPath.endsWith(normalizedPackage)) {
				throw new IllegalArgumentException("Path " + startingPath + " does not end with " + packageName);
			}

			this.startingPath = startingPath;
			this.packageName = packageName;
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
			if (!file.toString().endsWith(".class") || file.toString().contains("$")) {
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
				getList().add(Class.forName(className, true, classLoader));
			} catch (ClassNotFoundException e) {
				Logger.severe("[Tweaky] Class '" + className + "' could not be found:", e);
				throw new RuntimeException(e);
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
			if (!path.normalize().toUri().toString().startsWith(startingPath.normalize().toUri().toString())) {
				throw new IllegalArgumentException("Path " + path + " is not a child of " + startingPath);
			}

			String name = path.toString();
			int startLength = startingPath.toString().length();
			if (startLength < name.length() && (name.charAt(startLength) == '/' || name.charAt(startLength) == '\\')) {
				startLength++;
			}

			name = name.substring(startLength);
			name = name.replace(".class", "");
			name = name.replace("\\", ".").replace("/", ".");
			return packageName + "." + name;
		}
	}

	private Introspector() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
