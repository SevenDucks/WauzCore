package eu.wauz.wauzcore.skills.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;
import net.md_5.bungee.api.ChatColor;

/**
 * This class is used to register, find and execute skills.
 * 
 * @author User
 *
 * @see WauzPlayerSkill
 */
public class WauzPlayerSkillExecutor {
	
	
	/**
	 * A map of all registered skills.
	 */
	private static Map<String, WauzPlayerSkill> playerSkillMap = new HashMap<>();
	
	/**
	 * Gets a skill for given id from the map.
	 * 
	 * @param skillId The id of the skill.
	 * 
	 * @return The skill or null, if not found.
	 */
	public static WauzPlayerSkill getSkill(String skillId) {
		return playerSkillMap.get(skillId);
	}
	
	/**
	 * @return A list of all skills
	 */
	public static List<WauzPlayerSkill> getAllSkills() {
		return new ArrayList<>(playerSkillMap.values());
	}
	
	/**
	 * @return A list of all skill ids.
	 */
	public static List<String> getAllSkillIds() {
		return new ArrayList<>(playerSkillMap.keySet());
	}
	
	/**
	 * @return The count of different skills.
	 */
	public static int getSkillTypesCount() {
		return playerSkillMap.size();
	}
	
	/**
	 * Registers a skill.
	 * 
	 * @param skill The command to skill.
	 */
	public static void registerSkill(WauzPlayerSkill skill) {
		playerSkillMap.put(skill.getSkillId(), skill);
	}
	
	/**
	 * Tries to use a skill for a player.
	 * Only possible if a skillgem is socketed and the level matches.
	 * The global player skill cooldown must also be ready.
	 * 
	 * @param player The player that should use the skill.
	 * @param itemStack The item that skill is bound to.
	 * 
	 * @see Cooldown#playerSkillUse(Player)
	 * @see WauzPlayerSkillExecutor#execute(Player, ItemStack, String)
	 */
	public static void tryToUseSkill(Player player, ItemStack itemStack) {
		String skillId = EquipmentUtils.getSocketedSkill(itemStack);
		if(StringUtils.isNotBlank(skillId)) {
			int requiredLevel = EquipmentUtils.getLevelRequirement(itemStack);
			WauzDebugger.log(player, "Required Level: " + requiredLevel);
			if(player.getLevel() < requiredLevel) {
				player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
				return;
			}
			else if(Cooldown.playerSkillUse(player)) {
				execute(player, itemStack, skillId);
			}
		}
	}
	
	/**
	 * Uses a skill for a player.
	 * The player must have enough mana and the skill specific cooldown must be ready.
	 * These criteria can be bypassed in the magic debug mode.
	 * All players in a radius of 24 blocks will receive a message on skill use.
	 * 
	 * @param player The player that should use the skill.
	 * @param itemStack The item that skill is bound to.
	 * @param skillId The id of the skill to use.
	 * 
	 * @return If the execution was successful.
	 * 
	 * @see WauzPlayerData#isSkillReady(Player, String)
	 * @see ManaCalculator#useMana(Player, int)
	 * @see WauzPlayerSkill#executeSkill(Player, ItemStack)
	 * @see WauzDebugger#toggleMagicDebugMode(Player)
	 */
	public static boolean execute(Player player, ItemStack itemStack, String skillId) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		WauzPlayerSkill skill = playerSkillMap.get(skillId);
		if(playerData == null || skill == null) {
			return false;
		}
		
		boolean skillReady = playerData.isSkillReady(player, skillId);
		int manaCost = player.hasPermission("wauz.debug.magic") ? 0 : skill.getManaCost();
		
		if(skillReady && ManaCalculator.useMana(player, manaCost)) {
			playerData.updateSkillCooldown(player, skillId);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			
			boolean success = skill.executeSkill(player, itemStack);
			String message = "[" + ChatColor.LIGHT_PURPLE + "Skill" + ChatColor.RESET + "] " + ChatColor.GRAY;
			message += player.getName() + " casted (" + skill.getSkillId() + ")";
			message += success ? " Hit!" : " Miss!";
			
			for(Player playerInRadius : SkillUtils.getPlayersInRadius(player.getLocation(), 24)) {
				playerInRadius.sendMessage(message);
			}
			
			if(itemStack != null) {
				DurabilityCalculator.damageItem(player, itemStack, false);
			}
		}
		return true;
	}

}
