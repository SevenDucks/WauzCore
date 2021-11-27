package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.mobs.pets.WauzPetStat;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used to calculate the experience gains of players.
 * Also handles level ups.
 * 
 * @author Wauzmons
 */
public class ExperienceCalculator {
	
	/**
	 * Updates the experience bar of a player.
	 * 
	 * @param player The player to update the bar.
	 */
	public static void updateExperienceBar(Player player) {
		int level = PlayerCollectionConfigurator.getCharacterLevel(player);
		double currentExp = PlayerCollectionConfigurator.getCharacterExperience(player);
		double goalExp;
		if(WauzMode.isSurvival(player)) {
			goalExp = 100;
		}
		else {
			if(level < 1) {
				level = 1;
				PlayerCollectionConfigurator.setCharacterLevel(player, level);
			}
			goalExp = getExpToLevel(level + 1);
		}
		player.setLevel(level);
		player.setExp(Math.min(1, Math.max(0, (float) (currentExp / goalExp))));
	}
	
	/**
	 * Gives experience points to the player and levels them up, if 100% is reached.
	 * Adds bonuses, based on level and passive stats.
	 * 
	 * @param player The player to receive the exp.
	 * @param tier The tier (max player level) for the exp reward.
	 * @param earnedxp The earned exp without modifiers.
	 * 
	 * @return The exp amount to display.
	 * 
	 * @see ExperienceCalculator#grantExperience(Player, int, double, Location, boolean)
	 */
	public static int grantExperience(Player player, int tier, double earnedxp) {
		return grantExperience(player, tier, earnedxp, null, false);
	}
	
	/**
	 * Gives experience points to the player and levels them up, if 100% is reached.
	 * Adds bonuses, based on level and passive stats.
	 * 
	 * @param player The player to receive the exp.
	 * @param tier The tier (max player level) for the exp reward.
	 * @param earnedxp The earned exp without modifiers.
	 * @param location The location for an exp indicator or null for none.
	 * 
	 * @return The exp amount to display.
	 * 
	 * @see ExperienceCalculator#grantExperience(Player, int, double, Location, boolean)
	 */
	public static int grantExperience(Player player, int tier, double earnedxp, Location location) {
		return grantExperience(player, tier, earnedxp, location, false);
	}
	
	/**
	 * Gives experience points to the player and levels them up, if 100% is reached.
	 * Adds bonuses, based on level and passive stats.
	 * 
	 * @param player The player to receive the exp.
	 * @param tier The tier (max player level) for the exp reward.
	 * @param earnedxp The earned exp without modifiers.
	 * @param location The location for an exp indicator or null for none.
	 * @param shared If the exp was already shared (25%) with group members.
	 * 
	 * @return The exp amount to display.
	 * 
	 * @see WauzCore#MAX_PLAYER_LEVEL
	 * @see ExperienceCalculator#applyExperienceBonus(Player, double)
	 * @see ValueIndicator#spawnExpIndicator(Location, int)
	 * @see PlayerCollectionConfigurator#levelUpCharacter(Player)
	 * @see PlayerCollectionConfigurator#setCharacterExperience(Player, double)
	 */
	public static int grantExperience(Player player, int tier, double earnedxp, Location location, boolean shared) {
		try {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
			if(playerGroup != null && !shared) {
				for(Player member : playerGroup.getPlayers()) {
					grantExperience(member, tier, earnedxp / 4, null, true);
				}
			}
			
			int level = PlayerCollectionConfigurator.getCharacterLevel(player);
			if(player == null || level > tier || level >= WauzCore.MAX_PLAYER_LEVEL) {
				return 0;
			}
			
			double amplifiedExp = applyExperienceBonus(player, earnedxp);
			int displayexp = (int) (amplifiedExp * 100);
			if(location != null) {
				ValueIndicator.spawnExpIndicator(location, displayexp);
			}
			
			double currentExp = PlayerCollectionConfigurator.getCharacterExperience(player);
			currentExp = currentExp + amplifiedExp;
			WauzDebugger.log(player, "You earned " + amplifiedExp + " (" + earnedxp + ") experience!");
			
			int nextLevel = level + 1;
			int neededExp = getExpToLevel(nextLevel);
			double leftoverExp = 0;
			if(currentExp >= neededExp) {
				player.setLevel(nextLevel);
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				Components.title(player, ChatColor.GOLD + "Level Up!", "You reached level " + nextLevel + "!");
				PlayerCollectionConfigurator.levelUpCharacter(player);
				leftoverExp = currentExp - neededExp;
				currentExp = 0;
			}
			PlayerCollectionConfigurator.setCharacterExperience(player, currentExp);
			updateExperienceBar(player);
			if(leftoverExp > 0) {
				grantExperience(player, tier, leftoverExp, null, true);
			}
			return displayexp;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Applies experience bonuses, based on the passive stats of the player.
	 * 
	 * @param player The player that will earn the exp.
	 * @param experience The exp without bonus.
	 * 
	 * @return The exp with added bonus.
	 * 
	 * @see EquipmentUtils#getExperienceBonus(ItemStack)
	 */
	public static double applyExperienceBonus(Player player, double experience) {		
		ItemStack weaponItemStack = player.getEquipment().getItemInMainHand();
		double weaponBonus = (ItemUtils.isNotAir(weaponItemStack) ? EquipmentUtils.getExperienceBonus(weaponItemStack) : 0) / 100.0;	
		ItemStack armorItemStack = player.getEquipment().getChestplate();
		double armorBonus = (ItemUtils.isNotAir(armorItemStack) ? EquipmentUtils.getExperienceBonus(armorItemStack) : 0) / 100.0;
		WauzPlayerEffects effects = WauzPlayerDataPool.getPlayer(player).getStats().getEffects();
		double effectBonus = effects.getEffectPowerSumDecimal(WauzPlayerEffectType.EXP_BOOST);
		int petInt = WauzActivePet.getPetStat(player, WauzPetStat.getPetStat("Intelligence"));
		double petBonus = (float) petInt / 200f;
		
		double multiplier = 1 + weaponBonus + armorBonus + effectBonus + petBonus;
		return experience * multiplier;
	}
	
	/**
	 * Gets the amount of exp needed to reach the next player level.
	 * Based on a fibonacci-like sequence.
	 * 
	 * @param level The next level to be reached.
	 * 
	 * @return The needed exp to the level.
	 */
	public static int getExpToLevel(int level) {
		int neededExp = 10;
		int previousExp = 0;
		for(int i = 0; i < level; i++) {
			int increaseExp = (int) (previousExp / (2 * Math.ceil((double) i / 10.0)));
			previousExp = neededExp;
			neededExp += increaseExp;
		}
		return neededExp;
	}

}
