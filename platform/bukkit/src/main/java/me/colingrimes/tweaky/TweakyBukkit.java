package me.colingrimes.tweaky;

import me.colingrimes.tweaky.message.BukkitMessageService;
import me.colingrimes.tweaky.message.MessageService;
import me.colingrimes.tweaky.util.io.Logger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import javax.annotation.Nonnull;

public class TweakyBukkit extends Tweaky {

	private BukkitAudiences audiences;

	@Override
	public void onEnable() {
		if (paperCheck()) {
			Logger.warn("It looks like you are using the Bukkit version on a Paper server.");
			Logger.warn("It is recommended you switch to Tweaky-" + getDescription().getVersion() + "-paper.");
		}

		audiences = BukkitAudiences.create(this);
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		if (audiences != null) {
			audiences.close();
		}
	}

	/**
	 * Gets Adventure's audience provider.
	 *
	 * @return the audience provider
	 */
	@Nonnull
	public BukkitAudiences getAudiences() {
		return audiences;
	}

	@Nonnull
	@Override
	public MessageService getMessageService() {
		return new BukkitMessageService(this);
	}

	@Nonnull
	@Override
	public String getVersion() {
		return "Tweaky-Bukkit-" + getDescription().getVersion();
	}

	@Override
	public boolean isPaper() {
		return false;
	}

	private boolean paperCheck() {
		try {
			Class.forName("com.destroystokyo.paper.ParticleBuilder");
			return true;
		} catch (ClassNotFoundException ignored) {
			return false;
		}
	}
}
