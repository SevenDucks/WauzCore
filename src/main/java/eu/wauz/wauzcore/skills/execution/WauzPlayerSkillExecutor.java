package eu.wauz.wauzcore.skills.execution;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerSkillExecutor {
	
	public static Map<String, WauzPlayerSkill> playerSkillMap = new HashMap<>();
	
	public static void tryToUseSkill(Player player, ItemStack itemStack) {
		String skillId = ItemUtils.getSocketedSkill(itemStack);
		if(StringUtils.isNotBlank(skillId)) {
			int requiredLevel = ItemUtils.getLevelRequirement(itemStack);
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
		if(playerData == null || skill == null)
			return false;
		
		boolean skillReady = playerData.isSkillReady(player, skillId);
		int manaCost = player.hasPermission("wauz.debug.magic") ? 0 : skill.getManaCost();
		
		if(skillReady && ManaCalculator.useMana(player, manaCost)) {
			playerData.updateSkillCooldown(player, skillId);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			
			boolean success = skill.executeSkill(player);
			String message = "[" + ChatColor.LIGHT_PURPLE + "Skill" + ChatColor.RESET + "] " + ChatColor.GRAY;
			message += player.getName() + " casted (" + skill.getSkillId() + ")";
			message += success ? " Hit!" : " Miss!";
			
			for(Player playerInRadius : SkillUtils.getPlayersInRadius(player.getLocation(), 24)) {
				playerInRadius.sendMessage(message);
			}
			
			if(itemStack != null) {
				Damageable damageable = (Damageable) itemStack.getItemMeta();
				int durability = damageable.getDamage() + 1;
				int maxDurability = itemStack.getType().getMaxDurability();
				damageable.setDamage(durability);
				itemStack.setItemMeta((ItemMeta) damageable);
				
				if(durability >= maxDurability)
					itemStack.setAmount(itemStack.getAmount() - 1);
			}
		}
		return true;
	}

}
