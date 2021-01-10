package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerMoveEvent;

import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.annotations.Minigame;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * A race minigame, where you have to find a hidden path among fake doors.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameDoorDash implements ArcadeMinigame {
	
	/**
	 * A list of blocks that break when hit.
	 */
	private List<Block> breakingBlocks = new ArrayList<>();
	
	/**
	 * A list of blocks that don't break when hit.
	 */
	private List<Block> fakeBlocks = new ArrayList<>();
	
	/**
	 * The players who have crossed the finish line.
	 */
	private List<Player> finishedPlayers = new ArrayList<>();
	
	/**
	 * The amount of players who can win the game.
	 */
	private int maxWinningPlayers = 1;

	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "Door-Dash";
	}
	
	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Find the Real Doors and");
		description.add(ChatColor.WHITE + "Hit them to Break your Way");
		description.add(ChatColor.WHITE + "to the Finish Line!");
		description.add("   ");
		description.add(ChatColor.BLUE + "Qualified Players: " + ChatColor.GOLD + finishedPlayers.size() + " / " + maxWinningPlayers);
		return description;
	}
	
	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	@Override
	public void startGame(List<Player> players) {
		maxWinningPlayers = players.size() / 2;
		createDoors(489, 1, false);
		createDoors(475, 1, true);
		createDoors(465, 1, false);
		createDoors(447, 1, true);
		Location spawnLocation = new Location(ArcadeLobby.getWorld(), 1000.5, 88, 500.5, 180, 0);
		ArcadeUtils.placeTeam(players, spawnLocation, 10, 1);
		ArcadeUtils.runStartTimer(10, 120);
	}
	
	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>(finishedPlayers);
		breakingBlocks.clear();
		fakeBlocks.clear();
		finishedPlayers.clear();
		maxWinningPlayers = 1;
		return winners;
	}
	
	/**
	 * Handles the given animation event, that occured in the minigame.
	 * 
	 * @param event The animation event.
	 */
	@Override
	public void handleAnimationEvent(PlayerAnimationEvent event) {
		if(event.getAnimationType() != PlayerAnimationType.ARM_SWING) {
			return;
		}
		Player player = event.getPlayer();
		Block block = player.getTargetBlock(3);
		if(block == null) {
			return;
		}
		else if(breakingBlocks.contains(block)) {
			breakingBlocks.remove(block);
			block.setType(Material.AIR);
			block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
		}
		else if(fakeBlocks.contains(block)) {
			if(Cooldown.playerEntityInteraction(player)) {
				SkillUtils.throwBackEntity(event.getPlayer(), block.getLocation(), 1);
			}
			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_DROWNED_SHOOT, 1, 1);
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
		Location location = player.getLocation();
		if(location.getZ() <= 427 && location.getY() >= 78) {
			finishedPlayers.add(player);
			player.teleport(new Location(ArcadeLobby.getWorld(), 1000.5, 101, 465.5, 180, 0));
			player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
			for(Player playing : ArcadeLobby.getPlayingPlayers()) {
				playing.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.BLUE + " has been qualified!");
			}
			if(finishedPlayers.size() >= maxWinningPlayers) {
				ArcadeLobby.endGame();
			}
		}
		else if(location.getY() <= 72) {
			player.teleport(player.getBedSpawnLocation());
		}
	}
	
	/**
	 * Creates a row of 5 doors at the given z coordinate.
	 * 
	 * @param z The z coordinate to create the doors at.
	 * @param realDoors How many doors should be real.
	 * @param isThin If the doors should be 1 instead of 3 blocks high.
	 */
	public void createDoors(int z, int realDoors, boolean isThin) {
		World world = ArcadeLobby.getWorld();
		List<Boolean> doorStatuses = new ArrayList<>();
		for(int door = 0; door < 5; door++) {
			doorStatuses.add(door < realDoors);
		}
		Collections.shuffle(doorStatuses);
		int yBase = isThin ? 93 : 90;
		for(int door = 0; door < 5; door++) {
			boolean isReal = doorStatuses.get(door);
			int xBase = 989 + door * 5;
			for(int x = 0; x < 3; x++) {
				for(int y = 0; y < (isThin ? 1 : 3); y++) {
					Block block = world.getBlockAt(xBase + x, yBase - y, z);
					block.setType(Material.MAGENTA_CONCRETE);
					(isReal ? breakingBlocks : fakeBlocks).add(block);
				}
			}
		}
	}

}
