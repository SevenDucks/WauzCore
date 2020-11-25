package eu.wauz.wauzcore.players.ui.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.oneblock.OneBlockProgression;
import eu.wauz.wauzcore.oneblock.OnePhase;
import eu.wauz.wauzcore.oneblock.OnePhaseLevel;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * A scoreboard to show the server ip, season, commands, aswell as their one-block progress and tokens to a player.
 * 
 * @author Wauzmons
 */
public class OneBlockScoreboard extends BaseScoreboard {
	
	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	public OneBlockScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "OneBlock" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
		rowStrings.add("");
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "OneBlock Season " + WauzDateUtils.getSurvivalSeason());
		rowStrings.add("" + ChatColor.WHITE + ChatColor.BOLD + "IP: play.wauz.eu");
		rowStrings.add(" ");
		OneBlockProgression progression = OneBlockProgression.getPlayerOneBlock(player);
		OnePhase phase = progression.getPhase();
		OnePhaseLevel level = progression.getLevel();
		int currentBlock = progression.getBlockNo();
		int maximumBlock = level.getBlockAmount();
		int totalBlocks = progression.getTotalBlocks();
		rowStrings.add("Phase: " + ChatColor.GREEN + phase.getPhaseName() + " " + level.getLevelName());
		rowStrings.add("Block Progress: " + ChatColor.YELLOW + currentBlock + " / " + maximumBlock);
		rowStrings.add("  ");
		rowStrings.add("Total Blocks: " + ChatColor.AQUA + Formatters.INT.format(totalBlocks));
		rowStrings.add("Earn a free Token");
		rowStrings.add("for every 500 Blocks");
		rowStrings.add("   ");
		rowStrings.add("Tokens: " + ChatColor.GOLD + Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player)));
		rowStrings.add("Use an Ender Chest to Spend");
		rowStrings.add("    ");
		rowStrings.add(ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "hub " + ChatColor.LIGHT_PURPLE + "/" + ChatColor.WHITE + "spawn");
	}

}
