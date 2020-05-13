package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPetsConfigurator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.system.WauzDebugger;
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
		boolean isSurvival = WauzMode.isSurvival(player);
		int goalExp = isSurvival ? 100 : getExpToLevel(player.getLevel() + 1);
		double currentExp = PlayerConfigurator.getCharacterExperience(player);
		player.setExp((float) (currentExp / goalExp));
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
	 * @see PlayerConfigurator#levelUpCharacter(Player)
	 * @see PlayerConfigurator#setCharacterExperience(Player, double)
	 */
	public static int grantExperience(Player player, int tier, double earnedxp, Location location, boolean shared) {
		try {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
			if(playerGroup != null && !shared) {
				for(Player member : playerGroup.getPlayers()) {
					grantExperience(member, tier, earnedxp / 4, null, true);
				}
			}
			
			if(player == null || player.getLevel() > tier || player.getLevel() >= WauzCore.MAX_PLAYER_LEVEL) {
				return 0;
			}
			
			double amplifiedxp = applyExperienceBonus(player, earnedxp);
			int displayexp = (int) (amplifiedxp * 100);
			
			if(location != null) {
				ValueIndicator.spawnExpIndicator(location, displayexp);
			}
			
			double currentxp = PlayerConfigurator.getCharacterExperience(player);
			currentxp = currentxp + amplifiedxp;
			WauzDebugger.log(player, "You earned " + amplifiedxp + " (" + earnedxp + ") experience!");
			
			int nextLevel = player.getLevel() + 1;
			if(currentxp >= getExpToLevel(nextLevel)) {
				player.setLevel(nextLevel);
				player.sendTitle(ChatColor.GOLD + "Level Up!", "You reached level " + nextLevel + "!", 10, 70, 20);
				PlayerConfigurator.levelUpCharacter(player);
				currentxp = 0;
			}
			PlayerConfigurator.setCharacterExperience(player, currentxp);
			updateExperienceBar(player);
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
	 * @see PlayerConfigurator#getCharacterPetIntelligence(Player, int)
	 */
	public static double applyExperienceBonus(Player player, double experience) {		
		ItemStack weaponItemStack = player.getEquipment().getItemInMainHand();
		double weaponBonus = ItemUtils.isNotAir(weaponItemStack) ? EquipmentUtils.getExperienceBonus(weaponItemStack) : 0;	
		ItemStack armorItemStack = player.getEquipment().getChestplate();
		double armorBonus = ItemUtils.isNotAir(armorItemStack) ? EquipmentUtils.getExperienceBonus(armorItemStack) : 0;
		
		double multiplier = 1 + (weaponBonus / 100.0) + (armorBonus / 100.0);
		
		int petSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
		if(petSlot >= 0) {
			multiplier += (float) PlayerPetsConfigurator.getCharacterPetIntelligence(player, petSlot) / 10f;
		}
		
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
