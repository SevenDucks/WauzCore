package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.ConfigurationUtils;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;

public class PlayerPassiveSkillConfigurator extends ConfigurationUtils {
	
// Passive Skills

	public static int getTotalStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.points.total", true);
	}
	
	public static int getSpentStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.points.spent", true);
	}
	
	public static int getUnusedStatpoints(Player player) {
		return getTotalStatpoints(player) - getSpentStatpoints(player);
	}
	
	public static void increaseSpentSkillpoints(Player player) {
		playerConfigSet(player, "stats.points.spent", getSpentStatpoints(player) + 1, true);
	}
	
// Passive Skill - Health
	
	public static int getHealthStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.healthpts", true);
	}
 	
	public static int getHealth(Player player) {
		return playerConfigGetInt(player, "stats.health", true);
	}
	
	public static void increaseHealth(Player player) {
		increaseSpentSkillpoints(player);
		playerConfigSet(player, "stats.healthpts", getHealthStatpoints(player) + 1, true);
		
		int health = getHealth(player) + 5;			
		playerConfigSet(player, "stats.health", health, true);
		WauzPlayerDataPool.getPlayer(player).setMaxHealth(health);
		DamageCalculator.setHealth(player, health);
	}
	
// Passive Skill - Trading
	
	public static int getTradingStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.tradingpts", true);
	}
	
	public static int getTrading(Player player) {
		return playerConfigGetInt(player, "stats.trading", true);
	}
	
	public static float getTradingFloat(Player player) {
		return (float) ((float) playerConfigGetInt(player, "stats.trading", true) / 100);
	}
	
	public static void increaseTrading(Player player) {
		increaseSpentSkillpoints(player);
		playerConfigSet(player, "stats.tradingpts", getTradingStatpoints(player) + 1, true);
		
		playerConfigSet(player, "stats.trading", getTrading(player) + 10, true);
	}
	
// Passive Skill - Luck
	
	public static int getLuckStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.luckpts", true);
	}
	
	public static int getLuck(Player player) {
		return playerConfigGetInt(player, "stats.luck", true);
	}
	
	public static void increaseLuck(Player player) {
		increaseSpentSkillpoints(player);
		playerConfigSet(player, "stats.luckpts", getLuckStatpoints(player) + 1, true);
		
		playerConfigSet(player, "stats.luck", getLuck(player) + 10, true);
	}
	
// Passive Skill - Mana
	
	public static int getManaStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.manapts", true);
	}
	
	public static int getMana(Player player) {
		return playerConfigGetInt(player, "stats.mana", true);
	}
	
	public static void increaseMana(Player player) {
		increaseSpentSkillpoints(player);
		int statpoints = getManaStatpoints(player) + 1;
		playerConfigSet(player, "stats.manapts", statpoints, true);
				
		if(statpoints <= 40) {
			int mana = getMana(player) + 1;			
			playerConfigSet(player, "stats.mana", mana, true);
			WauzPlayerDataPool.getPlayer(player).setMaxMana(mana);
			ManaCalculator.updateManaItem(player);
		}
	}
	
// Passive Skill - Strength
	
	public static int getStrengthStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.strengthpts", true);
	}
	
	public static int getStrength(Player player) {
		return playerConfigGetInt(player, "stats.strength", true);
	}
	
	public static float getStrengthFloat(Player player) {
		return (float) ((float) playerConfigGetInt(player, "stats.strength", true) / 100);
	}
	
	public static void increaseStrength(Player player) {
		increaseSpentSkillpoints(player);
		int statpoints = getStrengthStatpoints(player) + 1;
		playerConfigSet(player, "stats.strengthpts", statpoints, true);
		
		if(statpoints <= 40) {
			playerConfigSet(player, "stats.strength", getStrength(player) + 5, true);
		}
	}

// Passive Skill - Agility
	
	public static int getAgilityStatpoints(Player player) {
		return playerConfigGetInt(player, "stats.agilitypts", true);
	}
	
	public static int getAgility(Player player) {
		return playerConfigGetInt(player, "stats.agility", true);
	}
	
	public static void increaseAgility(Player player) {
		increaseSpentSkillpoints(player);
		int statpoints = getAgilityStatpoints(player) + 1;
		playerConfigSet(player, "stats.agilitypts", statpoints, true);
		
		if(statpoints <= 40) {
			playerConfigSet(player, "stats.agility", getAgility(player) + 1, true);
		}
	}
	
// Crafting Skill
	
	public static Integer getCraftingSkill(Player player) {
		return playerConfigGetInt(player, "skills.crafting", true);
	}
	
	public static void increaseCraftingSkill(Player player) {
		try {
			Integer characterCraftingSkill = getCraftingSkill(player);
			playerConfigSet(player, "skills.crafting", characterCraftingSkill + 1, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Weapon Skill - Sword
	
	public static Integer getSwordSkill(Player player) {
		return playerConfigGetInt(player, "skills.sword", true);
	}
	
	public static Integer getSwordSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.swordmax", true);
	}
	
	public static void increaseSwordSkill(Player player) {
		try {
			Integer characterSwordSkill = getSwordSkill(player);
			Integer characterSwordSkillMax = getSwordSkillMax(player);
			if(characterSwordSkill < characterSwordSkillMax) {
				playerConfigSet(player, "skills.sword", characterSwordSkill + 1, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Weapon Skill - Axe
	
	public static Integer getAxeSkill(Player player) {
		return playerConfigGetInt(player, "skills.axe", true);
	}
	
	public static Integer getAxeSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.axemax", true);
	}
	
	public static void increaseAxeSkill(Player player) {
		try {
			Integer characterAxeSkill = getAxeSkill(player);
			Integer characterAxeSkillMax = getAxeSkillMax(player);
			if(characterAxeSkill < characterAxeSkillMax) {
				playerConfigSet(player, "skills.axe", characterAxeSkill + 1, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Weapon Skill Staff
	
	public static Integer getStaffSkill(Player player) {
		return playerConfigGetInt(player, "skills.staff", true);
	}
	
	public static Integer getStaffSkillMax(Player player) {
		return playerConfigGetInt(player, "skills.staffmax", true);
	}
	
	public static void increaseStaffSkill(Player player) {
		try {
			Integer characterStaffSkill = getStaffSkill(player);
			Integer characterStaffSkillMax = getStaffSkillMax(player);
			if(characterStaffSkill < characterStaffSkillMax) {
				playerConfigSet(player, "skills.staff", characterStaffSkill + 1, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
