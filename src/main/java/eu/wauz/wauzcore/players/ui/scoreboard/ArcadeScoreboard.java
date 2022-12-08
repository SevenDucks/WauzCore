package eu.wauz.wauzcore.players.ui.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.arcade.ArcadeMinigame;
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
	 * @param player The player who should receive the scoreboard.
	 */
	public ArcadeScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "DropGuys" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
		ArcadeMinigame minigame = ArcadeLobby.getMinigame();
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add("Currently Waiting: " + ChatColor.YELLOW + ArcadeLobby.getWaitingCount());
		rowStrings.add("Currently Playing: " + ChatColor.GREEN + ArcadeLobby.getPlayingCount());
		if(minigame == null) {
			rowStrings.add("Type " + ChatColor.RED + "/" + ChatColor.WHITE + "start to Start Game");
			if(!StringUtils.equals(ArcadeLobby.getRemainingTime(), "0:00")) {
				rowStrings.add("Auto Start in: " + ChatColor.RED + ArcadeLobby.getRemainingTime());
			}
			rowStrings.add(" ");
			rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player)));
			rowStrings.add("Go to the " + ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "hub to Spend");
		}
		else {
			rowStrings.add("Game Ends in: " + ChatColor.RED + ArcadeLobby.getRemainingTime());
			rowStrings.add(" ");
			rowStrings.add("Minigame: " + ChatColor.GOLD + minigame.getName());
			rowStrings.addAll(minigame.getDescription());
		}
	}

}
