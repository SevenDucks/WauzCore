package eu.wauz.wauzcore.players.ui.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * A scoreboard to show the server ip, season, commands, aswell as their score and tokens to a player.
 * 
 * @author Wauzmons
 */
public class SurvivalScoreboard extends BaseScoreboard {
	
	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	public SurvivalScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Survival" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "Survival Season " + WauzDateUtils.getSurvivalSeason());
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add(" ");
		rowStrings.add("Survival Score: " + ChatColor.AQUA + Formatters.INT.format(PlayerConfigurator.getSurvivalScore(player)));
		rowStrings.add("One point and a free Token");
		rowStrings.add("for each Level beyond 30");
		rowStrings.add("  ");
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player)));
		rowStrings.add("Use an Ender Chest to Spend");
		rowStrings.add("   ");
		rowStrings.add(ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "hub " + ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "spawn");
		rowStrings.add(ChatColor.RED + "/" + ChatColor.WHITE + "home " + ChatColor.RED + "/" + ChatColor.WHITE + "sethome");
		rowStrings.add(ChatColor.RED + "/" + ChatColor.WHITE + "group");
	}

}
