package eu.wauz.wauzcore.system.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzNoteBlockPlayer;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.nms.WauzNmsMinimap;
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
	 * @param event
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
	 * @param event
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
			WauzRegion.regionCheck(player);
		}
	}
	
	/**
	 * Prevents players to break blocks in certain regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(event.getBlock())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}
	
	/**
	 * Prevents players to place blocks in certain regions.
	 * 
	 * @param event
	 * 
	 * @see WauzRegion#disallowBuild(Block)
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission(WauzPermission.DEBUG_BUILDING.toString()) && WauzRegion.disallowBuild(event.getBlock())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You can't build here! Find another spot!");
		}
	}

	/**
	 * Checks if an OP player just created a sign that is bound to an event.
	 * 
	 * @param event
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
	 * Rewards tokens if a Survival player exceeded the maximum level.
	 * 
	 * @param event
	 * 
	 * @see WauzRewards#earnSurvivalToken(Player)
	 */
	@EventHandler
	public void onLevelUp(PlayerLevelChangeEvent event) {
		if(WauzMode.isSurvival(event.getPlayer()) && event.getNewLevel() > WauzCore.MAX_PLAYER_LEVEL_SURVIVAL) {
			WauzRewards.earnSurvivalToken(event.getPlayer());
		}
	}
	
	/**
	 * Prevents MMORPG players from learning normal Minecraft recipes.
	 * 
	 * @param event
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
	 * @param event
	 */
	public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Prevents players from loosing saturation in the hub.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if(WauzMode.inHub(event.getEntity())) {
			event.setCancelled(true);
		}
	}

}
