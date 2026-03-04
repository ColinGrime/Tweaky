package me.colingrimes.tweaky.tweak.event;

import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark methods as being event handler methods.
 * <p>
 * This is a special type of {@link EventHandler} annotation for events
 * in tweaks to allow pre-filtering via the {@link TweakProperties} class.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TweakHandler {

	/**
	 * Define the priority of the event.
	 * <p>
	 * First priority to the last priority executed:
	 * <ol>
	 * <li>LOWEST
	 * <li>LOW
	 * <li>NORMAL
	 * <li>HIGH
	 * <li>HIGHEST
	 * <li>MONITOR
	 * </ol>
	 *
	 * @return the priority
	 */
	@Nonnull
	EventPriority priority() default EventPriority.NORMAL;

	/**
	 * Define if the handler ignores a cancelled event.
	 * <p>
	 * If ignoreCancelled is {@code true} and the event is cancelled, the method is
	 * not called. Otherwise, the method is always called.
	 *
	 * @return whether cancelled events should be ignored
	 */
	boolean ignoreCancelled() default false;
}
