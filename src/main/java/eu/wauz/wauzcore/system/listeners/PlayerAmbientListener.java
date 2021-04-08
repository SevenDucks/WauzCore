package eu.wauz.wauzcore.system.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.ItemStack;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.events.PetObtainEvent;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.oneblock.OneBlock;
import eu.wauz.wauzcore.oneblock.OnePlotManager;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.nms.WauzNmsMinimap;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to interactions between the environment and the player.
 * 
 * @author Wauzmons
 */
public class PlayerAmbientListener implements Listener {
	
	/**
	 * Prevents that the spawn is loaded if a new world is initialized.
	 * This prevents lag on entering instances.
	 * 
	 * @param event The world init event.
	 */
	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		event.getWorld().setKeepSpawnInMemory(false);
	}

	/**
	 * Reloads most custom UI if the player changes their current world.
	 * This assures that the correct scoreboard, minimap etc. are shown.
	 * Also checks for a new music track and instance title.
	 * 
	 * @param event The change world event.
	 */
	@EventHandler
	public void onWorldEnter(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		WauzPlayerBossBar.clearBar(player);
		WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
		WauzNmsMinimap.init(player);
		WauzNoteBlockPlayer.play(player);
		
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		if(instance != null) {
			player.sendTitle(ChatColor.RED + instance.getDisplayTitle(), instance.getDisplaySubtitle(), 10, 70, 20);
		}
		else {
			if(WauzMode.inOneBlock(player)) {
				OnePlotManager.setUpBorder(player);
			}
			WauzRegion.regionCheck(player);
		}
	}
	
	/**
	 * Checks if a player is allowed to teleport to another location.
	 * 
	 * @param event The teleport event.
	 * 
	 * @see WauzTeleporter#commandTeleport(PlayerTeleportEvent)
	 */
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(event.getCause().equals(TeleportCause.COMMAND)) {
			WauzTeleporter.commandTeleport(event);
		}
	}
	
	/**
	 * Prevents players to break blocks in certain regions.
	 * 
	 * @param event The break event.
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(OneBlock.isOneBlock(block)) {
			event.setCancelled(true);
			OneBlock.breakOneBlock(player, block);
		}
		else if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(block)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents players to place blocks in certain regions.
	 * 
	 * @param event The place event.
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(OneBlock.isOneBlock(block)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build in the OneBlock spawn zone!");
		}
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(block)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents players to empty bukkets in certain regions.
	 * 
	 * @param event The bukket empty event.
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBukketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(block)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents players to fill bukkets in certain regions.
	 * 
	 * @param event The bukket fill event.
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBukketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(block)) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents players from breaking frames and paintings in certain regions.
	 * 
	 * @param event The hanging break event.
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onHangingBreak(HangingBreakEvent event) {
		Block block = event.getEntity().getLocation().getBlock();
		boolean hasDebugPermission = false;
		if(event instanceof HangingBreakByEntityEvent) {
			HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent) event;
			hasDebugPermission = entityEvent.getRemover().hasPermission(WauzPermission.DEBUG_BUILDING.toString());
		}
		if(!hasDebugPermission && WauzRegion.disallowBuild(block)) {
			event.setCancelled(true);
		}
	}

	/**
	 * Checks if an OP player just created a sign that is bound to an event.
	 * 
	 * @param event The sign change event.
	 * 
	 * @see WauzSigns#create(SignChangeEvent)
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if(event.getPlayer().isOp() && WauzMode.isMMORPG(event.getPlayer())) {
			WauzSigns.create(event);
		}
	}
	
	/**
	 * Prevents the player from gaining natural experience in MMORPG mode.
	 * 
	 * @param event The exp change event.
	 */
	@EventHandler
	public void OnExpGain(PlayerExpChangeEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setAmount(0);
		}
	}
	
	/**
	 * Rewards tokens if a Survival player exceeded the maximum level.
	 * 
	 * @param event The level change event.
	 * 
	 * @see WauzRewards#earnSurvivalToken(Player)
	 */
	@EventHandler
	public void onLevelUp(PlayerLevelChangeEvent event) {
		Player player = event.getPlayer();
		if(WauzMode.isSurvival(player) && !WauzMode.inOneBlock(player) && event.getNewLevel() > WauzCore.MAX_PLAYER_LEVEL_SURVIVAL) {
			WauzRewards.earnSurvivalToken(player);
		}
	}
	
	/**
	 * Grants breeding experience when a player obtains a pet.
	 * 
	 * @param event The pet obtain event.
	 */
	@EventHandler
	public void onPetObtain(PetObtainEvent event) {
		Player player = event.getPlayer();
		WauzPet pet = event.getPet();
		int exp = pet.getRarity().getMultiplier();
		PlayerSkillConfigurator.increaseTamingSkill(player, exp);
		player.sendMessage(ChatColor.GREEN + "You earned " + exp + " breeding exp by obtaining " + pet.getKey() + "!");
		AchievementTracker.addProgress(player, WauzAchievementType.COLLECT_PETS, 1);
	}
	
	/**
	 * Prevents MMORPG players from learning normal Minecraft recipes.
	 * 
	 * @param event The recipe discover event.
	 */
	@EventHandler
	public void onRecipeDiscover(PlayerRecipeDiscoverEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents MMORPG players from receiving normal Minecraft advancements.
	 * 
	 * @param event The advancement event.
	 */
	@EventHandler
	public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents players from loosing saturation in the hub.
	 * 
	 * @param event The food level change event.
	 */
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if(WauzMode.inHub(event.getEntity()) || WauzMode.isArcade(event.getEntity())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents entities from dropping unallowed items for the player.
	 * 
	 * @param event the item drop event.
	 */
	@EventHandler
	public void onItemDrop(EntityDropItemEvent event) {
		if(WauzMode.isMMORPG(event.getEntity())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Adds a name label to every item that was dropped on the ground.
	 * Also prevents items from spawning in the one block of the one-block gamemode.
	 * 
	 * @param event The spawn event.
	 * 
	 * @see ItemUtils#hasDisplayName(ItemStack)
	 */
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		Item item = event.getEntity();
		ItemStack itemStack = item.getItemStack();
		if(ItemUtils.hasDisplayName(itemStack)) {
			item.setCustomName(Components.displayName(itemStack.getItemMeta()));
			item.setCustomNameVisible(true);
		}
		Location location = event.getLocation();
		if(OneBlock.isOneBlock(location.getBlock())) {
			item.teleport(location.add(0, 1.25, 0));
		}
		item.setItemStack(WauzNmsClient.nmsSerialize(itemStack));
	}
	
	/**
	 * Prevents unallowed wither spawns.
	 * 
	 * @param event The spawn event.
	 */
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
	    if(WauzMode.inOneBlock(entity) && entity.getType().equals(EntityType.WITHER)) {
	        event.setCancelled(true);
	    }
	}

}
