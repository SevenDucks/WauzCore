package eu.wauz.wauzcore.arcade.template;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.arcade.ArcadeMinigame;

/**
 * A template for breaking floor minigames.
 * 
 * @author Wauzmons
 */
public abstract class MinigameTemplateBreakingFloor implements ArcadeMinigame {
	
	/**
	 * The list of blocks that can break.
	 */
	protected List<Block> breakingBlocks = new ArrayList<>();
	
	/**
	 * The players who have been eliminated.
	 */
	protected List<Player> eliminatedPlayers = new ArrayList<>();
	
	/**
	 * The amount of players who can loose the game.
	 */
	protected int maxLosingPlayers = 1;
	
	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Floor Pieces break away");
		description.add(ChatColor.WHITE + "when you stand on them.");
		description.add(ChatColor.WHITE + "Keep moving to Survive!");
		description.add("   ");
		description.add(ChatColor.RED + "Eliminated Players: " + ChatColor.GOLD + eliminatedPlayers.size() + " / " + maxLosingPlayers);
		return description;
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

}
