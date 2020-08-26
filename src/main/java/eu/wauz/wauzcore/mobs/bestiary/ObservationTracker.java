package eu.wauz.wauzcore.mobs.bestiary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerBestiaryConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A class to track the progress of the rank representing the detail level of a bestiary entry.
 * 
 * @author Wauzmons
 */
public class ObservationTracker {
	
	/**
	 * Adds progress to the observation rank of the killed mob, if it has a valid bestiary entry.
	 * Also grants the fitting rewards when completed.
	 * 
	 * @param player The player who defeated the entity.
	 * @param entity The entity that was defeated.
	 */
	public static void tryToAddProgress(Player player, Entity entity) {
		if(!MobMetadataUtils.hasBestiaryEntry(entity)) {
			return;
		}
		String entry = MobMetadataUtils.getBestiaryEntry(entity);
		int oldProgress = PlayerBestiaryConfigurator.getBestiaryKills(player, entry);
		int newProgress = oldProgress + 1;
		boolean isBoss = MobMetadataUtils.isRaidBoss(entity);
		String mobName = entity.getCustomName() + ChatColor.YELLOW;
		ObservationRank oldObservationRank = ObservationRank.getObservationRank(oldProgress, isBoss);
		ObservationRank newObservationRank = ObservationRank.getObservationRank(newProgress, isBoss);
		PlayerBestiaryConfigurator.setBestiaryKills(player, entry, newProgress);
		
		if(newProgress == 1) {
			player.sendMessage(ChatColor.YELLOW + "Your defeated " + mobName + " for the first time!");
		}
		if(!Objects.equals(oldObservationRank, newObservationRank)) {
			String rankName = newObservationRank.getRankName() + ChatColor.YELLOW;
			player.sendMessage(ChatColor.YELLOW + "Your knowledge of " + mobName + " improved to rank \"" + rankName + "\"!");
			
			int reward = newObservationRank.getSouls();
			if(reward > 0) {
				long soulstones = PlayerConfigurator.getCharacterSoulstones(player);
				PlayerConfigurator.setCharacterSoulstones(player, soulstones + reward);
				player.sendMessage(ChatColor.YELLOW + "You received " + reward + " soulstones as reward!");
			}
			WauzNmsClient.nmsChatCommand(player, "menu bestiary", ChatColor.YELLOW + "To view your bestiary:", false);
		}
	}
	
	/**
	 * Generates a list of lores to display the observation rank progress of a mob for a given player.
	 * 
	 * @param player The player to get the observation rank progress for.
	 * @param entry The bestiary entry of the mob.
	 * 
	 * @return The list of progress lores.
	 */
	public static List<String> generateProgressLore(Player player, WauzBestiaryEntry entry) {
		int progress = PlayerBestiaryConfigurator.getBestiaryKills(player, entry.getEntryFullName());
		String progressString = Formatters.INT.format(progress);
		ObservationRank currentObservationRank = ObservationRank.getObservationRank(progress, entry.isBoss());
		ObservationRank nextObservationRank = currentObservationRank.getNextRank();
		
		List<String> lores = new ArrayList<>();
		lores.add(ChatColor.WHITE + "Knowledge Rank: " + currentObservationRank.getRankName());
		
		if(nextObservationRank != null) {
			int nextGoal = entry.isBoss() ? nextObservationRank.getBossKills() : nextObservationRank.getNormalKills();
			String nextGoalString = Formatters.INT.format(nextGoal);
			lores.add(ChatColor.WHITE + "Progress: " + progressString + " / " + nextGoalString + " Kills");
			lores.add(UnicodeUtils.createProgressBar(progress, nextGoal, 50, ChatColor.DARK_RED));
		}
		else {
			lores.add(ChatColor.WHITE + "Progress: " + progressString + " Kills");
		}
		return lores;
	}

}
