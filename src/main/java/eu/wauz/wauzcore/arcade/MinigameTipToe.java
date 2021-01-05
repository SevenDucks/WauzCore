package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import eu.wauz.wauzcore.building.PathGenerator;
import eu.wauz.wauzcore.system.annotations.Minigame;

/**
 * A race minigame, where you have to find a hidden path among fake tiles.
 * 
 * @author Wauzmons
 */
@Minigame
public class MinigameTipToe implements ArcadeMinigame {
	
	/**
	 * A map of fake tiles, indexed by their blocks.
	 */
	private Map<Block, List<Block>> blockFakeTileMap = new HashMap<>();
	
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
		return "Tip-Toe";
	}

	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Avoid Fake Tiles and");
		description.add(ChatColor.WHITE + "find the Hidden Paths");
		description.add(ChatColor.WHITE + "to reach the Finish Line!");
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
		int width = 5;
		int length = 13;
		World world = ArcadeLobby.getWorld();
		Location cornerLocation = new Location(world, 731.5, 87, 493.5);
		PathGenerator generator = new PathGenerator(width, length);
		generator.run();
		int[][] grid = generator.getPathMatrix();
		boolean isAlternate = true;
		for(int x = 0; x < width; x++) {
			for(int z = 0; z < length; z++) {
				Location tileCornerLocation = cornerLocation.clone().add(z * 3, 0, x * 3);
				boolean isFake = grid[x][z] == 0;
				isAlternate = !isAlternate;
				createTile(tileCornerLocation, isFake, isAlternate);
			}
		}
		Location spawnLocation = new Location(world, 776.5, 88, 500.5, 90, 0);
		ArcadeUtils.placeTeam(players, spawnLocation, 1, 6);
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
		blockFakeTileMap.clear();
		finishedPlayers.clear();
		maxWinningPlayers = 1;
		return winners;
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
		for(double x = -0.15; x <= 0.15; x += 0.15) {
			for(double z = -0.15; z <= 0.15; z += 0.15) {
				Block blockBelow = location.clone().add(x, 0, z).getBlock();
				List<Block> tileBlocks = blockFakeTileMap.get(blockBelow);
				if(tileBlocks != null) {
					makeTileFall(tileBlocks);
					player.teleport(player.getLocation().clone().subtract(0, 0.1, 0));
					return;
				}
			}
		}
		if(location.getX() <= 728 && location.getY() >= 87) {
			finishedPlayers.add(player);
			player.teleport(new Location(ArcadeLobby.getWorld(), 750.5, 96, 500.5, 90, 0));
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
	 * Creates a 3x3 block tile.
	 * 
	 * @param cornerLocation The location of the tile block with the lowest coordinates.
	 * @param isFake If the tile should fall when touched.
	 * @param isAlternate If the tile should have an alternative color.
	 */
	public void createTile(Location cornerLocation, boolean isFake, boolean isAlternate) {
		List<Block> blocks = new ArrayList<>();
		Material material = isAlternate ? Material.YELLOW_CONCRETE : Material.ORANGE_CONCRETE;
		for(int x = 0; x < 3; x++) {
			for(int z = 0; z < 3; z++) {
				Location blockLocation = cornerLocation.clone().add(x, 0, z);
				Block block = blockLocation.getBlock();
				block.setType(material);
				blocks.add(block);
			}
		}
		if(isFake) {
			for(Block block : blocks) {
				blockFakeTileMap.put(block, blocks);
			}
		}
	}
	
	/**
	 * Makes the given tile fall down and removes it from the map.
	 * 
	 * @param tileBlocks The blocks forming the tile.
	 */
	public void makeTileFall(List<Block> tileBlocks) {
		for(Block block : tileBlocks) {
			blockFakeTileMap.remove(block);
			block.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
			block.setType(Material.AIR);
		}
	}

}
