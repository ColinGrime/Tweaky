package me.colingrimes.tweaky.tweak.event;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.io.Logger;
import me.colingrimes.tweaky.util.bukkit.Events;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.EventExecutor;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class TweakEvent {

	/**
	 * Registers all listeners on the tweak.
	 *
	 * @param plugin the plugin
	 * @param tweak the tweak to register events
	 */
	public static void register(@Nonnull Tweaky plugin, @Nonnull Tweak tweak) {
		if (!tweak.isEnabled()) {
			return;
		}

		Set<Method> methods = new HashSet<>();
		methods.addAll(Arrays.asList(tweak.getClass().getMethods()));
		methods.addAll(Arrays.asList(tweak.getClass().getDeclaredMethods()));

		for (Method method : methods) {
			EventHandler handler = method.getAnnotation(EventHandler.class);
			TweakHandler tweakHandler = method.getAnnotation(TweakHandler.class);
			if (handler == null && tweakHandler == null) {
				continue;
			}

			Class<?>[] params = method.getParameterTypes();
			if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) {
				continue;
			}

			Class<? extends Event> eventClass = params[0].asSubclass(Event.class);
			method.setAccessible(true);

			EventExecutor executor = (__, event) -> {
				if (!eventClass.isAssignableFrom(event.getClass())) {
					return;
				}

				// Additional filtering for tweak event handlers.
				if (tweakHandler != null && ignoreEvent(tweak, event)) {
					return;
				}

				try {
					method.invoke(tweak, event);
				} catch (Exception e) {
					Logger.severe("TweakEvents has failed to invoke method:", e);
				}
			};

			Bukkit.getPluginManager().registerEvent(
					eventClass,
					tweak,
					handler != null ? handler.priority() : tweakHandler.priority(),
					executor,
					plugin,
					handler != null ? handler.ignoreCancelled() : tweakHandler.ignoreCancelled()
			);
		}
	}

	/**
	 * Returns true if the event should be ignored.
	 * This uses the {@link TweakProperties} to ignore irrelevant events.
	 *
	 * @param tweak the tweak
	 * @param event the event to check
	 * @return true if the event should be ignored
	 */
	private static boolean ignoreEvent(@Nonnull Tweak tweak, @Nonnull Event event) {
		TweakProperties properties = tweak.getProperties();
		return !tweak.hasPermission(Events.getPlayer(event)) || properties.getGuard().reject(event);
	}
}
