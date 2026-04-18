package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.implementation.Menus;
import me.colingrimes.tweaky.config.implementation.Messages;
import me.colingrimes.tweaky.config.implementation.Settings;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.event.TweakEvent;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;

import javax.annotation.Nonnull;

public abstract class DefaultTweak implements Tweak {

	protected final Tweaky plugin;
	protected final Settings settings;
	protected final Menus menus;
	protected final Messages msg;
	protected final String id;
	protected final TweakProperties properties;

	public DefaultTweak(@Nonnull Tweaky plugin, @Nonnull String id) {
		this.plugin = plugin;
		this.settings = plugin.getSettings();
		this.menus = plugin.getMenus();
		this.msg = plugin.getMessages();
		this.id = id;
		this.properties = new TweakProperties();
		this.configureProperties(properties);
		TweakEvent.register(plugin, this);
	}

	/**
	 * Configures the various properties of the tweak.
	 *
	 * @param properties the tweak properties to configure
	 */
	protected void configureProperties(@Nonnull TweakProperties properties) {}

	@Nonnull
	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isEnabled() {
		return settings.getTweak(id).get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.getTweak(id).get();
	}

	@Nonnull
	@Override
	public TweakProperties getProperties() {
		return properties;
	}
}
