package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.WauzIdentifier;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class WauzRuneRemover {

	public boolean clearAllSockets(Player player, ItemStack equipmentItemStack) {
		boolean valid = false;
		if(!ItemUtils.hasLore(equipmentItemStack)) {
			return false;
		}
		
		int skipLines = 0;
		int atkManus = ItemUtils.getRuneAtkBoost(equipmentItemStack);
		int defManus = ItemUtils.getRuneDefBoost(equipmentItemStack);
		
		ItemMeta itemMeta = equipmentItemStack.getItemMeta();
		List<String> newLores = new ArrayList<>();
		for(String lore : itemMeta.getLore()) {
			if(skipLines > 0) {
				skipLines--;
				continue;
			}
			else if(lore.contains("Attack") && atkManus > 0) {
				String[] val = lore.split(" ");
				Integer attack = Integer.parseInt(val[1]);
				int newValue = attack - atkManus;
				lore = lore.replace(
						ChatColor.RED + " " + attack,
						ChatColor.RED + " " + newValue);
				WauzDebugger.log(player, "Manus: " + atkManus);
			}
			else if(lore.contains("Defense") && defManus > 0) {
				String[] val = lore.split(" ");
				Integer defense = Integer.parseInt(val[1]);
				int newValue = defense - defManus;
				lore = lore.replace(
						ChatColor.BLUE + " " + defense,
						ChatColor.BLUE + " " + newValue);	
				WauzDebugger.log(player, "Manus: " + defManus);
			}
			else if(lore.contains("Rune (")) {
				WauzDebugger.log(player, "Cleared Rune Slot");
				lore = WauzIdentifier.EMPTY_RUNE_SLOT;
				valid = true;
			}
			else if(lore.contains("Skillgem (")) {
				WauzDebugger.log(player, "Cleared Skill Slot");
				lore = WauzIdentifier.EMPTY_SKILL_SLOT;
				valid = true;
				skipLines = 2;
			}
			newLores.add(lore);
		}
		
		if(valid) {
			itemMeta.setLore(newLores);
			equipmentItemStack.setItemMeta(itemMeta);
		}
		return valid;
	}
	
}
