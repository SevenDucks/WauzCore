package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerConfigurationUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;

/**
 * Configurator to fetch or modify passive skill data from the Player.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerSkillConfigurator extends PlayerConfigurationUtils {
	
// Quick Slot Skills
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The id of the quick slot.
	 * 
	 * @return The name of the skill in the quick slot.
	 */
	public static String getQuickSlotSkill(Player player, int slot) {
		return playerConfigGetString(player, "skills.active." + slot, true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param slot The id of the quick slot.
	 * @param skill The name of the new skill in the quick slot.
	 */
	public static void setQuickSlotSkill(Player player, int slot, String skill) {
		playerConfigSet(player, "skills.active." + slot, skill, true);
		WauzPlayerDataPool.getPlayer(player).refreshSelectedCastables();
	}
	
// Passive Skills

	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of total statpoints.
	 */
	public static int getTotalStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.points.total", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of spent statpoints.
	 */
	public static int getSpentStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.points.spent", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of unused statpoints.
	 */
	public static int getUnusedStatpoints(Player player) {
		return getTotalStatpoints(player) - getSpentStatpoints(player);
	}
	
	/**
	 * Increases the spent statpoints by 1.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseSpentStatpoints(Player player) {
		playerConfigSet(player, "stats.points.spent", getSpentStatpoints(player) + 1, true);
	}
	
// Passive Skill - Health
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of statpoints spent in health.
	 */
	public static int getHealthStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.healthpts", true);
	}
 	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of maximum health.
	 */
	public static int getHealth(Player player) {
		return playerConfigGetInt(player, "stats.health", true);
	}
	
	/**
	 * Spents 1 statpoint and increases health by 5.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseHealth(Player player) {
		increaseSpentStatpoints(player);
		playerConfigSet(player, "stats.healthpts", getHealthStatpoints(player) + 1, true);
		
		int health = getHealth(player) + 5;			
		playerConfigSet(player, "stats.health", health, true);
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		playerData.setMaxHealth(health);
		DamageCalculator.setHealth(player, playerData.getMaxHealth());
	}
	
// Passive Skill - Trading
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of statpoints spent in trading.
	 */
	public static int getTradingStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.tradingpts", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of trading percent points.
	 */
	public static int getTrading(Player player) {
		return playerConfigGetInt(player, "stats.trading", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of trading as percentage.
	 */
	public static float getTradingFloat(Player player) {
		return (float) ((float) playerConfigGetInt(player, "stats.trading", true) / 100);
	}
	
	/**
	 * Spents 1 statpoint and increases trading by 10%.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseTrading(Player player) {
		increaseSpentStatpoints(player);
		playerConfigSet(player, "stats.tradingpts", getTradingStatpoints(player) + 1, true);
		
		playerConfigSet(player, "stats.trading", getTrading(player) + 10, true);
	}
	
// Passive Skill - Luck
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of statpoints spent in luck.
	 */
	public static int getLuckStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.luckpts", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of luck percent points.
	 */
	public static int getLuck(Player player) {
		return playerConfigGetInt(player, "stats.luck", true);
	}
	
	/**
	 * Spents 1 statpoint and increases luck by 10%.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseLuck(Player player) {
		increaseSpentStatpoints(player);
		playerConfigSet(player, "stats.luckpts", getLuckStatpoints(player) + 1, true);
		
		playerConfigSet(player, "stats.luck", getLuck(player) + 10, true);
	}
	
// Passive Skill - Mana
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of statpoints spent in mana.
	 */
	public static int getManaStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.manapts", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of maximum mana.
	 */
	public static int getMana(Player player) {
		return playerConfigGetInt(player, "stats.mana", true);
	}
	
	/**
	 * Spents 1 statpoint and increases mana by 1.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseMana(Player player) {
		increaseSpentStatpoints(player);
		int statpoints = getManaStatpoints(player) + 1;
		playerConfigSet(player, "stats.manapts", statpoints, true);
				
		if(statpoints <= 40) {
			int mana = getMana(player) + 1;			
			playerConfigSet(player, "stats.mana", mana, true);
			WauzPlayerDataPool.getPlayer(player).setMaxMana(mana);
		}
	}
	
// Passive Skill - Strength
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of statpoints spent in strength.
	 */
	public static int getStrengthStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.strengthpts", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of strength percent points.
	 */
	public static int getStrength(Player player) {
		return playerConfigGetInt(player, "stats.strength", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of strength as percentage.
	 */
	public static float getStrengthFloat(Player player) {
		return (float) ((float) playerConfigGetInt(player, "stats.strength", true) / 100);
	}
	
	/**
	 * Spents 1 statpoint and increases strength by 5%.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseStrength(Player player) {
		increaseSpentStatpoints(player);
		int statpoints = getStrengthStatpoints(player) + 1;
		playerConfigSet(player, "stats.strengthpts", statpoints, true);
		
		if(statpoints <= 40) {
			playerConfigSet(player, "stats.strength", getStrength(player) + 5, true);
		}
	}

// Passive Skill - Agility
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of statpoints spent in agility.
	 */
	public static int getAgilityStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.agilitypts", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The amount of agility percent points.
	 */
	public static int getAgility(Player player) {
		return playerConfigGetInt(player, "stats.agility", true);
	}
	
	/**
	 * Spents 1 statpoint and increases agility by 1%.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseAgility(Player player) {
		increaseSpentStatpoints(player);
		int statpoints = getAgilityStatpoints(player) + 1;
		playerConfigSet(player, "stats.agilitypts", statpoints, true);
		
		if(statpoints <= 40) {
			playerConfigSet(player, "stats.agility", getAgility(player) + 1, true);
		}
	}
	
// Passive Skill - Mastery
	
	/**
	 * @param player The player that owns the config file.
	 * @param mastery The number of the mastery.
	 * 
	 * @return The amount of statpoints spent in that mastery.
	 */
	public static int getMasteryStatpoints(Player player, int mastery) {
		return playerConfigGetInt(player, "masteries." + mastery, true);
	}
	
	/**
	 * Spents 1 statpoint and increases the given mastery by 1.
	 * 
	 * @param player The player that owns the config file.
	 * @param mastery The number of the mastery.
	 */
	public static void increaseMastery(Player player, int mastery) {
		increaseSpentStatpoints(player);
		int statpoints = getMasteryStatpoints(player, mastery) + 1;
		playerConfigSet(player, "masteries." + mastery, statpoints, true);
		WauzPlayerDataPool.getPlayer(player).refreshUnlockedCastables();
	}
	
// Crafting Skill
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The level of the crafting skill.
	 */
	public static Integer getCraftingSkill(Player player) {
		return playerConfigGetInt(player, "skills.crafting", true);
	}
	
	/**
	 * Increases the level of the crafting skill by 1.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseCraftingSkill(Player player) {
		Integer characterCraftingSkill = getCraftingSkill(player);
		playerConfigSet(player, "skills.crafting", characterCraftingSkill + 1, true);
	}
	
// Taming Skill
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The level of the taming skill.
	 */
	public static Integer getTamingSkill(Player player) {
		return playerConfigGetInt(player, "skills.taming", true);
	}
	
	/**
	 * Increases the level of the taming skill by a given amount.
	 * 
	 * @param player The player that owns the config file.
	 * @param exp The amount of experience to add to the skill.
	 */
	public static void increaseTamingSkill(Player player, int exp) {
		Integer characterTamingSkill = getTamingSkill(player);
		playerConfigSet(player, "skills.taming", characterTamingSkill + exp, true);
	}
	
// Weapon Skill - Sword
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The current level of the sword skill.
	 */
	public static Integer getSwordSkill(Player player) {
		return playerConfigGetInt(player, "skills.sword", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The maximum level of the sword skill.
	 */
	public static Integer getSwordSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.swordmax", true);
	}
	
	/**
	 * Increases the level of the sword skill by 1, if posssible.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseSwordSkill(Player player) {
		Integer characterSwordSkill = getSwordSkill(player);
		Integer characterSwordSkillMax = getSwordSkillMax(player);
		if(characterSwordSkill < characterSwordSkillMax) {
			playerConfigSet(player, "skills.sword", characterSwordSkill + 1, true);
		}
	}
	
// Weapon Skill - Axe
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The current level of the axe skill.
	 */
	public static Integer getAxeSkill(Player player) {
		return playerConfigGetInt(player, "skills.axe", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The maximum level of the axe skill.
	 */
	public static Integer getAxeSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.axemax", true);
	}
	
	/**
	 * Increases the level of the axe skill by 1, if posssible.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseAxeSkill(Player player) {
		Integer characterAxeSkill = getAxeSkill(player);
		Integer characterAxeSkillMax = getAxeSkillMax(player);
		if(characterAxeSkill < characterAxeSkillMax) {
			playerConfigSet(player, "skills.axe", characterAxeSkill + 1, true);
		}
	}
	
// Weapon Skill Staff
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The current level of the staff skill.
	 */
	public static Integer getStaffSkill(Player player) {
		return playerConfigGetInt(player, "skills.staff", true);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * 
	 * @return The maximum level of the staff skill.
	 */
	public static Integer getStaffSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.staffmax", true);
	}
	
	/**
	 * Increases the level of the staff skill by 1, if posssible.
	 * 
	 * @param player The player that owns the config file.
	 */
	public static void increaseStaffSkill(Player player) {
		Integer characterStaffSkill = getStaffSkill(player);
		Integer characterStaffSkillMax = getStaffSkillMax(player);
		if(characterStaffSkill < characterStaffSkillMax) {
			playerConfigSet(player, "skills.staff", characterStaffSkill + 1, true);
		}
	}

}
