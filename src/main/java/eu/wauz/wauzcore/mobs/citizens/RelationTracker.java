package eu.wauz.wauzcore.mobs.citizens;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerRelationConfigurator;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A class to track the progress of relationships between a player and a citizen.
 * 
 * @author Wauzmons
 */
public class RelationTracker {
	
	/**
	 * Adds progress to a relation and grants the fitting rewards when completed.
	 * 
	 * @param player The player to receive relation experience.
	 * @param citizen The citizen to increase the relation for.
	 * @param relationExp The relation experience to add.
	 */
	public static void addProgress(Player player, String citizen, int relationExp) {
		int oldProgress = PlayerRelationConfigurator.getRelationProgress(player, citizen);
		int newProgress = oldProgress + relationExp;
		RelationLevel oldRelationLevel = RelationLevel.getRelationLevel(oldProgress);
		RelationLevel newRelationLevel = RelationLevel.getRelationLevel(newProgress);
		PlayerRelationConfigurator.setRelationProgress(player, citizen, newProgress);
		player.sendMessage(ChatColor.YELLOW + "You earned " + relationExp + " relation exp with " + citizen + "!");
		
		if(!Objects.equals(oldRelationLevel, newRelationLevel)) {
			int reward = newRelationLevel.getRelationTier();
			long soulstones = PlayerCollectionConfigurator.getCharacterSoulstones(player);
			PlayerCollectionConfigurator.setCharacterSoulstones(player, soulstones + reward);
			
			String relationName = newRelationLevel.getRelationName();
			player.sendMessage(ChatColor.YELLOW + "Your relation with " + citizen + " improved to \"" + relationName + "\"!");
			player.sendMessage(ChatColor.YELLOW + "You received " + reward + " soulstones as reward!");
		}
	}
	
	/**
	 * Generates a list of lores to display the relation progress for a given player and citizen.
	 * 
	 * @param player The player to get the relation progress for.
	 * @param citizen The citizen to measure the relation with.
	 * 
	 * @return The list of progress lores.
	 */
	public static List<String> generateProgressLore(Player player, String citizen) {
		int progress = PlayerRelationConfigurator.getRelationProgress(player, citizen);
		String progressString = Formatters.INT.format(progress);
		RelationLevel currentRelationLevel = RelationLevel.getRelationLevel(progress);
		RelationLevel nextRelationLevel = currentRelationLevel.getNextLevel();
		
		List<String> lores = new ArrayList<>();
		String currentRelationString = currentRelationLevel.getRelationName() + " (" + currentRelationLevel.getRelationTier() + ")";
		lores.add(ChatColor.GREEN + "Level: " + ChatColor.GOLD + currentRelationString);
		int pricePercentage = (int) (currentRelationLevel.getDiscountMultiplier() * 100);
		String discountString = "+" + (100 - pricePercentage) + "% Discount / Rewards";
		lores.add(ChatColor.YELLOW + "Bonus: " + discountString);
		
		lores.add("");
		if(nextRelationLevel != null) {
			lores.add(ChatColor.YELLOW + "Next: " + ChatColor.GOLD + nextRelationLevel.getRelationName());
			int nextGoal = nextRelationLevel.getNeededExp();
			String nextGoalString = Formatters.INT.format(nextGoal);
			lores.add(ChatColor.YELLOW + "Progress: " + progressString + " / " + nextGoalString + " Relation Exp");
			lores.add(UnicodeUtils.createProgressBar(progress, nextGoal, 50, ChatColor.LIGHT_PURPLE));
			lores.add(ChatColor.YELLOW + "Reward: " + nextRelationLevel.getRelationTier() + " Soulstones");
		}
		else {
			lores.add(ChatColor.YELLOW + "Next: " + ChatColor.GREEN + "COMPLETED");
			lores.add(ChatColor.YELLOW + "Progress: " + progressString + " Relation Exp");
		}
		return lores;
	}

}
