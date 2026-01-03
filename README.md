<div align="center">
    <img src="images/Tweaky.png" alt="Tweaks" width="300" height="300" />
</div>

<div align="center">
    <h3>Tweaky</h3>
    <p>Adds toggleable vanilla tweaks and other quality-of-life improvements.</p>
</div>

## Tweaks
* `anvil-color` - Items can be colored in the anvil.
* `anvil-repair` - Right-clicking an anvil with an iron block will repair it slightly.
* `armor-swap` - Shift over an armor stand to quick swap armor.
* `bee-capture` - Right-click a bee with a beehive to capture it.
* `break-deepslate` - Deepslate breaks instantly when mined with a netherite pickaxe, eff 5, and haste 2.
* `break-ender-chest` - Protect your ender chests by picking between different break behaviors.
* `break-glass` - Glass breaks instantly when broken by a Netherite Pickaxe (configurable).
* `break-leaves` - Breaking leaves with a hoe will break an extra block on each side. Netherite hoes = 3x3x3.
* `break-plants` - Breaking grass/flowers with a hoe will 3x3 break them. Netherite hoes = 5x5.
* `breeding-indicator` - Adds an indicator above mobs to show when they can breed again.
* `cauldron-concrete` - Throw concrete powder into a cauldron to turn it into concrete.
* `cauldron-mud` - Throw dirt into a cauldron to turn it into mud.
* `coordinates` - Allows players to view their coordinates in the Action Bar.
* `crops-bone-meal` - Allows players to bone meal any crop.
* `crops-harvest` - Right-click crops with a hoe to harvest them.
* `crops-protection` - Prevents the trampling of crops.
* `death-notify` - Notifies the dying player where they died.
* `doors-double` - Allows the opening of double doors with 1 click.
* `doors-iron` - Allows you to open iron doors with your hand.
* `drops-filter` - Allows you to filter out useless item drops.
* `drops-magnet` - Breaking blocks nearby will cause their item drops to go towards the player a little.
* `enchanting-lapis` - Lapis stays in the lapis slot in enchanting tables when you close it.
* `entity-dye` - Allows the dying of named mobs with dyes.
* `entity-equip` - If a player throws an item close to an entity, they will auto equip it.
* `entity-ignite` - Right-clicking an entity with a flint & steel or fire charge sets it on fire.
* `entity-silence` - Name an entity "silent" to make it silent or "unsilent" to undo it.
* `fortune-silk-swap` - Allows you to swap between fortune and silk touch on the same tool.
* `happy-ghast-placement` - Blocks can now be placed on the top of happy ghast's heads.
* `happy-ghast-speed` - Allows happy ghasts to move at a configurable speed while they are being ridden.
* `hay-bale-bread` - Right-clicking a hay bale with a hoe automatically crafts and drops bread.
* `horse-statistics` - Allows you to view the statistics (health-speed-jump) of horses by sneak-right-click.
* `inventory-crafting` - Right-clicking a crafting table in your inventory opens it.
* `inventory-ender-chest` - Right-clicking a silk touch ender chest in your inventory opens it.
* `item-frame-click-through` - Right-click item frame on chest to open it. Shift for normal behavior.
* `item-frame-invisible` - Shift-right-click an item frame with shears to toggle its visibility on/off.
* `ladder-placement` - Place ladders up/down depending on your direction by right-clicking existing ladders.
* `ladder-teleportation` - Allows you to instantly climb up/down ladders (different modes are available).
* `night-vision` - Toggle night vision with the /nv command.
* `pet-protection` - Prevents you from attacking your own pet.
* `portal-protection` - Nether portals can no longer be destroyed by explosions.
* `recipe-unlock-all` - Players who join the server will have all the recipes unlocked.
* `revert-farmland` - Sneak-right-click on farmland with a hoe to revert it back to dirt.
* `revert-path` - Sneak-right-click on dirt paths with a shovel to revert it back to dirt.
* `revert-stripped` - Sneak-right-click on a stripped log or stem to revert it back to its previous variant.
* `rotten-flesh-to-leather` - Allows you to smelt rotten flesh into leather.
* `torch-throw` - Allows you to throw torches on the ground to place them.
* `snowballs-add-snow-layer` - Throwing snowballs on existing snow layers will add snow layers.
* `snowballs-break-plants` - Throwing snowballs on plants/flowers will break them.
* `snowballs-damage` - Allows snowballs to inflict a configurable amount of damage.
* `snowballs-extinguish-entities` - Throwing snowballs on entities will extinguish them.
* `snowballs-extinguish-fire` - Throwing snowballs on fire will extinguish it.
* `snowballs-form-ice` - Throwing snowballs on water will freeze it.
* `snowballs-form-snow` - Throwing snowballs on any block will form snow.
* `snowballs-knockback` - Allows you to change the knockback value of snowballs.
* `sponge-ignite` - Setting a sponge on fire dries it out instantly.
* `vehicle-pickup` - Allows vehicles to be picked up by sneak-right-click.
* `villager-follow` - (PAPER ONLY) Villagers will follow you if you are holding an emerald.
* `water-bottle-convert-lava` - Splashing a water bottle into lava converts it into obsidian.
* `water-bottle-craft` - Allows you to convert a water bucket into splash water bottles.
* `weapon-swing-through-grass` - Allows weapons to swing through grass without destroying them.
* `xp-fill` - Allows the filling of empty bottles into XP bottles by right-clicking on an enchanting table.

## Configurations:
* [`config.yml`](https://github.com/ColinGrime/Tweaky/blob/master/src/main/resources/config.yml) - Allows you to change the various tweaks associated with this plugin.
* [`menus.yml`](https://github.com/ColinGrime/Tweaky/blob/master/src/main/resources/menus.yml) - Allows you to change the Tweak Menu names, lores, and usage messages.
* [`messages.yml`](https://github.com/ColinGrime/Tweaky/blob/master/src/main/resources/messages.yml) - Allows you to change the various messages associated with this plugin.

## Player Commands:
* `/tweaks` - Allows players to view all the activated tweaks on the server.
* `/coordinates` - Allows players to toggle the 'coordinates' tweak. Requires it to be enabled.

## Admin Commands:
All Admin commands require the `tweaky.admin` permission node.
* `/tweaky list` - Lists all enabled/disabled tweaks.
* `/tweaky toggle <tweak_id>` - Toggles the specific tweak.
* `/tweaky reload` - Reloads all configuration files.
