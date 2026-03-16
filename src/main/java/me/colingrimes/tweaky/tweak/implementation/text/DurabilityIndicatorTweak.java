package me.colingrimes.tweaky.tweak.implementation.text;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import javax.annotation.Nonnull;

public class DurabilityIndicatorTweak extends DefaultTweak {

	public DurabilityIndicatorTweak(@Nonnull Tweaky plugin) {
		super(plugin, "durability_indicator");
	}

	@EventHandler
	public void onPlayerItemDamage(@Nonnull PlayerItemDamageEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (!hasPermission(player) || !settings.TWEAK_DURABILITY_INDICATOR_MATS.get().contains(item.getType()) || !(item.getItemMeta() instanceof Damageable damageable)) {
			return;
		}

		int damage = damageable.getDamage() + event.getDamage();
		int maxDamage = Items.getMaxDurability(item);
		int remaining = maxDamage - damage;

		if (remaining != plugin.getSettings().TWEAK_DURABILITY_INDICATOR_AMOUNT.get()) {
			return;
		}

		String name = Text.format(item.getType().name());
		player.sendTitle("", plugin.getMessages().TWEAK_DURABILITY_MESSAGE.toText().replace("{item}", name), 10, 40, 10);
		Scheduler.sync().runRepeating(() -> Players.sound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO), 0L, 3L, 9L);
	}
}
