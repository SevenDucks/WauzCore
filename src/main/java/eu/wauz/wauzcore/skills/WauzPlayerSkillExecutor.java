package eu.wauz.wauzcore.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionSkills;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.players.calc.RageCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * This class is used to register, find and execute skills.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class WauzPlayerSkillExecutor {
	
	/**
	 * A map of all registered skills.
	 */
	private static Map<String, WauzPlayerSkill> playerSkillMap = new HashMap<>();
	
	/**
	 * A list of all skill ids, that can be obtained as skillgem.
	 */
	private static List<String> skillgemIds = new ArrayList<>();
	
	/**
	 * Gets a skill for the given id from the map.
	 * 
	 * @param skillId The id of the skill.
	 * 
	 * @return The skill or null, if not found.
	 */
	public static WauzPlayerSkill getSkill(String skillId) {
		return playerSkillMap.get(skillId);
	}
	
	/**
	 * @return A list of all skills.
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
	 * @return A list of all skill ids, that can be obtained as skillgem.
	 */
	public static List<String> getAllSkillgemIds() {
		return new ArrayList<>(skillgemIds);
	}
	
	/**
	 * @return The count of different skills.
	 */
	public static int getSkillTypesCount() {
		return playerSkillMap.size();
	}
	
	/**
	 * Registers a skill, that can be obtained as skillgem.
	 * 
	 * @param skill The skill to register.
	 */
	public static void registerSkill(WauzPlayerSkill skill) {
		registerSkill(skill, true);
	}
	
	/**
	 * Registers a skill.
	 * 
	 * @param skill The skill to register.
	 * @param skillgem If the skill can be obtained as skillgem.
	 */
	public static void registerSkill(WauzPlayerSkill skill, boolean skillgem) {
		playerSkillMap.put(skill.getSkillId(), skill);
		if(skillgem) {
			skillgemIds.add(skill.getSkillId());
		}
	}
	
	/**
	 * Tries to use a skill for a player.
	 * Only possible if a valid skillgem is socketed in the given item.
	 * 
	 * @param player The player that should use the skill.
	 * @param itemStack The item that skill is bound to.
	 * 
	 * @see EquipmentUtils#getSocketedSkill(ItemStack)
	 * @see WauzPlayerSkillExecutor#tryToUseSkill(Player, ItemStack, WauzPlayerSkill)
	 */
	public static void tryToUseSkill(Player player, ItemStack itemStack) {
		String skillId = EquipmentUtils.getSocketedSkill(itemStack);
		if(StringUtils.isBlank(skillId)) {
			return;
		}
		WauzPlayerSkill skill = playerSkillMap.get(skillId);
		if(skill == null) {
			return;
		}
		tryToUseSkill(player, itemStack, skill);
	}
	
	/**
	 * Tries to use a skill for a player.
	 * Checks if the global skill cooldown is ready and if the level requirement matches.
	 * 
	 * @param player The player that should use the skill.
	 * @param itemStack The item that is used to cast the skill.
	 * @param skill The skill to use.
	 * 
	 * @see EquipmentUtils#getLevelRequirement(ItemStack)
	 * @see Cooldown#playerSkillUse(Player)
	 * @see WauzPlayerSkillExecutor#execute(Player, ItemStack, WauzPlayerSkill)
	 */
	public static void tryToUseSkill(Player player, ItemStack itemStack, WauzPlayerSkill skill) {
		int requiredLevel = EquipmentUtils.getLevelRequirement(itemStack);
		WauzDebugger.log(player, "Required Level: " + requiredLevel);
		if(PlayerCollectionConfigurator.getCharacterLevel(player) < requiredLevel) {
			player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
			return;
		}
		else if(Cooldown.playerSkillUse(player)) {
			execute(player, itemStack, skill);
		}
	}
	
	/**
	 * Uses a skill for a player.
	 * The player must have enough mana and the skill specific cooldown must be ready.
	 * These criteria can be bypassed in the magic debug mode.
	 * All players in a radius of 24 blocks will receive a message on skill use.
	 * 
	 * @param player The player that should use the skill.
	 * @param itemStack The item that is used to cast the skill.
	 * @param skill The skill to use.
	 * 
	 * @return If the execution was successful.
	 * 
	 * @see WauzPlayerDataSectionSkills#isSkillReady(String)
	 * @see ManaCalculator#useMana(Player, int)
	 * @see RageCalculator#useRage(Player, int)
	 * @see WauzPlayerSkill#executeSkill(Player, ItemStack)
	 * @see WauzDebugger#toggleMagicDebugMode(Player)
	 * @see WauzPlayerActionBar#update(Player)
	 */
	public static boolean execute(Player player, ItemStack itemStack, WauzPlayerSkill skill) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return false;
		}
		
		boolean skillReady = playerData.getSkills().isSkillReady(skill.getSkillId());
		int manaCost = player.hasPermission(WauzPermission.DEBUG_MAGIC.toString()) ? 0 : skill.getManaCost();
		
		if(skillReady && (skill.isPhysical() ? RageCalculator.useRage(player, manaCost) : ManaCalculator.useMana(player, manaCost))) {
			playerData.getSkills().updateSkillCooldown(skill.getSkillId());
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
		else {
			WauzPlayerActionBar.update(player);
		}
		return true;
	}
	
}
