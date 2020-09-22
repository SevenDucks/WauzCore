package eu.wauz.wauzcore.players.ui.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * A scoreboard to show the name and status of their arcade lobby to a player.
 * 
 * @author Wauzmons
 */
public class ArcadeScoreboard extends BaseScoreboard {

	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	public ArcadeScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Arcade" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
		rowStrings.add("");
		if(ArcadeLobby.isWaiting(player)) {
			rowStrings.add("Waiting for a Game to Start...");
			rowStrings.add("Currently Waiting: " + ChatColor.YELLOW + ArcadeLobby.getWaitingCount());
			rowStrings.add(ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "start " + ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "hub");
			rowStrings.add(" ");
		}
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player)));
		rowStrings.add("  ");
	}

}
