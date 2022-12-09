package eu.wauz.wauzcore.players.ui.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import eu.wauz.wauzcore.system.util.Components;

/**
 * The abstract base for all scoreboard classes.
 * 
 * @author Wauzmons
 */
public abstract class BaseScoreboard {
	
	/**
	 * The created scoreboard.
	 */
	private Scoreboard scoreboard;
	
	/**
	 * The strings that represent the rows of the scoreboard.
	 */
	protected List<String> rowStrings;
	
	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	public BaseScoreboard(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		scoreboard = scoreboardManager.getNewScoreboard();
		Objective objective = Components.objective(scoreboard, "row", Criteria.DUMMY, "-*-*-*-" + getTitleText() + "-*-*-*-");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		rowStrings = new ArrayList<>();
		fillScoreboard(player);
		
		for(int index = 0; index != rowStrings.size(); index++) {
			Score score = objective.getScore(rowStrings.get(index));
			score.setScore(rowStrings.size() - index);
		}
	}
	
	/**
	 * @return The text to show as scoreboard title.
	 */
	public abstract String getTitleText();
	
	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param player The player who should receive the scoreboard.
	 */
	public abstract void fillScoreboard(Player player);

	/**
	 * @return The created scoreboard.
	 */
	public Scoreboard getScoreboard() {
		return scoreboard;
	}
	
}
