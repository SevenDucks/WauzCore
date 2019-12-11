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

public class WauzPlayerSkillExecutor {
	
	private static Map<String, WauzPlayerSkill> playerSkillMap = new HashMap<>();
	
	public static WauzPlayerSkill getSkill(String skillId) {
		return playerSkillMap.get(skillId);
	}
	
	public static List<WauzPlayerSkill> getAllSkills() {
		return new ArrayList<>(playerSkillMap.values());
	}
	
	public static List<String> getAllSkillIds() {
		return new ArrayList<>(playerSkillMap.keySet());
	}
	
	public static int getSkillTypesCount() {
		return playerSkillMap.size();
	}
	
	public static void registerSkill(WauzPlayerSkill skill) {
		playerSkillMap.put(skill.getSkillId(), skill);
	}
	
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
				DurabilityCalculator.takeDamage(player, itemStack, false);
			}
		}
		return true;
	}

}
