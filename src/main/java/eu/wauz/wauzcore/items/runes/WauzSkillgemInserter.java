package eu.wauz.wauzcore.items.runes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import net.md_5.bungee.api.ChatColor;

public class WauzSkillgemInserter {
	
	public boolean insertSkillgem(Player player, ItemStack equipmentItemStack, ItemStack skillgemItemStack) {
		String skillName = skillgemItemStack.getItemMeta().getDisplayName();
		skillName = StringUtils.substringAfter(skillName, ": " + ChatColor.LIGHT_PURPLE);
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillName);
		
		if(ItemUtils.hasSkillgemSocket(equipmentItemStack)) {
			ItemMeta itemMeta = equipmentItemStack.getItemMeta();
			List<String> newLores = new ArrayList<>();
			for(String lore : itemMeta.getLore()) {
				if(lore.contains(ChatColor.DARK_RED + "Empty")) {
					newLores.add(ChatColor.WHITE + "Skillgem (" + ChatColor.LIGHT_PURPLE + skill.getSkillId() + ChatColor.WHITE + ")");
					newLores.add(ChatColor.WHITE + skill.getSkillDescription());
					lore = ChatColor.WHITE + skill.getSkillStats();
				}
				newLores.add(lore);
			}
			itemMeta.setLore(newLores);
			equipmentItemStack.setItemMeta(itemMeta);
			return true;
		}
		return false;
	}

}
