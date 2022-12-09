package eu.wauz.wauzcore.players.ui.scoreboard;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * A scoreboard to show the server ip, aswell as their rank and tokens to a player.
 * 
 * @author Wauzmons
 */
public class HubScoreboard extends BaseScoreboard {

	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	public HubScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Delseyria" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Reborn" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "The Minecraft MMORPG");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add(" ");
		WauzRank rank = WauzRank.getRank(player);
		String rankTitle = rank.getRankPrefix();
		rankTitle = StringUtils.isNotBlank(rankTitle) ? rankTitle : (rank.getRankColor() + rank.getRankName());
		rowStrings.add("Rank: " + rankTitle);
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player)));
	}

}
