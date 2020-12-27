package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.building.ShapeHexagon;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.annotations.Minigame;

/**
 * A survival minigame, where you have to stay alive while the floor breaks.
 * 
 * @author Wauzmons
 * 
 * @see MinigameHexAGone
 */
@Minigame
public class MinigameThinIce implements ArcadeMinigame {
	
	/**
	 * The list of blocks that can break.
	 */
	private List<Block> breakingBlocks = new ArrayList<>();
	
	/**
	 * The players who have been eliminated.
	 */
	private List<Player> eliminatedPlayers = new ArrayList<>();
	
	/**
	 * The amount of players who can loose the game.
	 */
	private int maxLosingPlayers = 1;

	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "Thin-Ice";
	}

	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Ice Tiles melt away");
		description.add(ChatColor.WHITE + "when you stand on them.");
		description.add(ChatColor.WHITE + "Keep moving to Survive!");
		description.add("   ");
		description.add(ChatColor.RED + "Eliminated Players: " + ChatColor.GOLD + eliminatedPlayers.size() + " / " + maxLosingPlayers);
		return description;
	}

	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	@Override
	public void startGame(List<Player> players) {
		maxLosingPlayers = players.size() / 2;
		World world = ArcadeLobby.getWorld();
		Location floorLocation = new Location(world, 750.5, 85, 1000.5);
		breakingBlocks.addAll(new ShapeHexagon(floorLocation, 12).create(Material.BLUE_ICE));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation, 6).create(Material.AIR));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation.subtract(0, 1, 0), 12).create(Material.BLUE_ICE));
		breakingBlocks.addAll(new ShapeHexagon(floorLocation.subtract(0, 1, 0), 6).create(Material.BLUE_ICE));
		Location spawnLocation = new Location(world, 750.5, 88, 1000.5, 0, 0);
		ArcadeUtils.placeTeam(players, spawnLocation, 6, 6);
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.BARRIER);
		}
		ArcadeUtils.runStartTimer(10, 180);
	}

	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>(ArcadeLobby.getPlayingPlayers());
		winners.removeAll(eliminatedPlayers);
		breakingBlocks.clear();
		eliminatedPlayers.clear();
		maxLosingPlayers = 1;
		return winners;
	}
	
	/**
	 * Handles the start event, that gets fired when the start countdown ends.
	 */
	@Override
	public void handleStartEvent() {
		ItemStack snowballItemStack = new ItemStack(Material.SNOWBALL, 64);
		ItemMeta snowballItemMeta = snowballItemStack.getItemMeta();
		snowballItemMeta.setDisplayName(ChatColor.RED + "Snowball");
		snowballItemMeta.setUnbreakable(true);
		snowballItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
		snowballItemStack.setItemMeta(snowballItemMeta);
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			player.getInventory().addItem(snowballItemStack);
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
	 * Handles the given projectile hit event, that occured in the minigame.
	 * 
	 * @param event The projectile hit event.
	 */
	@Override
	public void handleProjectileHitEvent(ProjectileHitEvent event) {
		Entity entity = event.getHitEntity();
		if(entity instanceof Player) {
			SkillUtils.throwBackEntity(entity, event.getEntity().getLocation(), 0.75);
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
				makeBlockMelt(location.clone().add(x, 0, z).getBlock());
			}
		}
		if(location.getY() <= 32) {
			eliminatedPlayers.add(player);
			player.teleport(new Location(ArcadeLobby.getWorld(), 750.5, 70, 1000.5, 90, 0));
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
	 * A method that is called every second of the minigame.
	 */
	@Override
	public void handleTick() {
		for(Player player : ArcadeLobby.getPlayingPlayers()) {
			Location location = player.getLocation().clone().subtract(0, 1, 0);
			for(double x = -0.3; x <= 0.3; x += 0.3) {
				for(double z = -0.3; z <= 0.3; z += 0.3) {
					makeBlockMelt(location.clone().add(x, 0, z).getBlock());
				}
			}
		}
	}
	
	/**
	 * Makes the given block melt to the next stage and removes it temporarily from the list.
	 * 
	 * @param block The block to break.
	 */
	public void makeBlockMelt(Block block) {
		if(breakingBlocks.contains(block)) {
			breakingBlocks.remove(block);
			switch(block.getType()) {
			case BLUE_ICE:
				block.setType(Material.PACKED_ICE);
				makeBlockMeltable(block);
				break;
			case PACKED_ICE:
				block.setType(Material.ICE);
				makeBlockMeltable(block);
				break;
			default:
				block.setType(Material.AIR);
				break;
			}
			block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
		}
	}

	/**
	 * Makes the given block break and removes it from the list.
	 * 
	 * @param block The block to break.
	 */
	private void makeBlockMeltable(Block block) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				breakingBlocks.add(block);
			}
			
		}, 10);
	}

}
