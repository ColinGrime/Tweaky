package me.colingrimes.tweaky.tweak.implementation.misc;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import me.colingrimes.tweaky.util.misc.Random;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("UnstableApiUsage")
public class PlayerFeedTweak extends DefaultTweak {

	public PlayerFeedTweak(@Nonnull Tweaky plugin) {
		super(plugin, "player_feed");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(i -> i.hasData(DataComponentTypes.CONSUMABLE))
				.item(i -> i.hasData(DataComponentTypes.FOOD) && i.getData(DataComponentTypes.FOOD).nutrition() > 0)
				.entity(EntityType.PLAYER);
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Player target = (Player) event.getRightClicked();
		ItemStack item = player.getInventory().getItem(event.getHand());
		Consumable consumable = item.getData(DataComponentTypes.CONSUMABLE);
		FoodProperties food = item.getData(DataComponentTypes.FOOD);

		// Food cooldown for player giving the food.
		if (player.hasCooldown(item.getType())) {
			return;
		}

		// Target is full and cannot be fed.
		if (target.getFoodLevel() >= 20) {
			plugin.getMessages().TWEAK_FEED_FULL.replace("{player}", target.getName()).send(player);
			return;
		}

		player.setCooldown(item.getType(), (int) (consumable.consumeSeconds() * 20));

		// Manage food and saturation levels.
		target.setFoodLevel(Math.min(20, target.getFoodLevel() + food.nutrition()));
		target.setSaturation(Math.min(20, target.getSaturation() + food.saturation()));
		plugin.getMessages().TWEAK_FEED_NOTIFY
				.replace("{player}", player.getName())
				.replace("{item}", Text.format(item.getType().name()))
				.replace("{hunger}", food.nutrition())
				.send(target);

		// Play sound and particles.
		Sounds.play(target, Registry.SOUNDS.get(consumable.sound()));
		target.getWorld().spawnParticle(Particle.ITEM, target.getEyeLocation().subtract(0, 0.1, 0), 32, 0.2, 0.0, 0.2, 0, item);

		// Optionally apply potion effects to food.
		for (ConsumeEffect effect : consumable.consumeEffects()) {
			if (effect instanceof ConsumeEffect.ApplyStatusEffects applyEffects && Random.chance(applyEffects.probability() * 100)) {
				target.addPotionEffects(applyEffects.effects());
			}
		}

		// Annoying hacky solution to remove infinite eating bug.
		Items.remove(item);
		player.getInventory().setItem(event.getHand(), null);
		Scheduler.sync().runLater(() -> player.getInventory().setItem(event.getHand(), item), 2L);

		event.setCancelled(true);
	}
}
