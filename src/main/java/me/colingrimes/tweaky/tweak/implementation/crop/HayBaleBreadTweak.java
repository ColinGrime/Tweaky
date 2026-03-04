package me.colingrimes.tweaky.tweak.implementation.crop;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class HayBaleBreadTweak extends DefaultTweak {

	public HayBaleBreadTweak(@Nonnull Tweaky plugin) {
		super(plugin, "hay_bale_bread");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.breakable()
				.rightClick()
				.item(Tag.ITEMS_HOES)
				.block(Material.HAY_BLOCK);
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Players.use(event.getPlayer(), event.getHand(), event.getBlock().getBlockData().getSoundGroup().getBreakSound(), event.getLocation());
		Items.drop(new ItemStack(Material.BREAD, 3), event.getLocation());
		event.getBlock().setType(Material.AIR);
		event.setCancelled(true);
	}
}
