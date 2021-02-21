package eu.wauz.wauzcore.arcade;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.arcade.template.MinigameTemplateBreakingFloor;
import eu.wauz.wauzcore.building.ShapeHexagon;
import eu.wauz.wauzcore.system.annotations.Minigame;

/**
 * A survival minigame, where you have to stay alive while the floor breaks.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameHexAGone extends MinigameTemplateBreakingFloor {
	
	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "Hex-A-Gone";
	}

	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	@Override
	public void startGame(List<Player> players) {
		maxLosingPlayers = players.size() - (players.size() / 2);
		World world = ArcadeLobby.getWorld();
		Location floorLocation = new Location(world, 750.5, 85, 750.5);
		breakingBlocks.clear();
		breakingBlocks.addAll(new ShapeHexagon(floorLocation, 12).create(Material.CYAN_CONCRETE));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation.subtract(0, 10, 0), 12).create(Material.GREEN_CONCRETE));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation.subtract(0, 10, 0), 12).create(Material.YELLOW_CONCRETE));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation.subtract(0, 10, 0), 12).create(Material.ORANGE_CONCRETE));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation.subtract(0, 10, 0), 12).create(Material.RED_CONCRETE));
		Location spawnLocation = new Location(world, 750.5, 88, 750.5, 0, 0);
		ArcadeUtils.placeTeam(players, spawnLocation, 6, 6);
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.BARRIER);
		}
		ArcadeUtils.runStartTimer(10, 180);
	}
	
	/**
	 * Handles the start event, that gets fired when the start countdown ends.
	 */
	@Override
	public void handleStartEvent() {
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
		}
	}
	
	/**
	 * Handles the given quit event, that occured in the minigame.
	 * 
	 * @param player The player who quit.
	 */
	@Override
	public void handleQuitEvent(Player player) {
		eliminatedPlayers.add(player);
		if(eliminatedPlayers.size() >= maxLosingPlayers) {
			ArcadeLobby.endGame();
		}
	}
	
	/**
	 * Handles the given move event, that occured in the minigame.
	 * 
	 * @param event The move event.
	 */
	@Override
	public void handleMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation().clone().subtract(0, 1, 0);
		for(double x = -0.3; x <= 0.3; x += 0.3) {
			for(double z = -0.3; z <= 0.3; z += 0.3) {
				makeBlockBreak(location.clone().add(x, 0, z).getBlock());
			}
		}
		if(location.getY() <= 32) {
			eliminatedPlayers.add(player);
			player.teleport(new Location(ArcadeLobby.getWorld(), 750.5, 96, 750.5, 0, 0));
			player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1);
			for(Player playing : ArcadeLobby.getPlayingPlayers()) {
				playing.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.RED + " has been eliminated!");
			}
			if(eliminatedPlayers.size() >= maxLosingPlayers) {
				ArcadeLobby.endGame();
			}
		}
	}
	
	/**
	 * Makes the given block break and removes it from the list.
	 * 
	 * @param block The block to break.
	 */
	public void makeBlockBreak(Block block) {
		if(breakingBlocks.contains(block)) {
			breakingBlocks.remove(block);
			block.setType(Material.BLACK_CONCRETE);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					block.setType(Material.AIR);
					block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
				}
				
			}, 20);
		}
	}

}
