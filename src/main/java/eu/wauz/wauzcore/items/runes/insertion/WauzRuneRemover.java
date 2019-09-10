package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.system.commands.WauzDebugger;

public class WauzRuneRemover {

	public boolean clearAllSockets(Player player, ItemStack equipmentItemStack) {
		boolean valid = false;
		if(!ItemUtils.hasLore(equipmentItemStack)) {
			return false;
		}
		
		int skipLines = 0;
		int atkManus = ItemUtils.getRuneAtkBoost(equipmentItemStack);
		if(atkManus > 0) {
			int baseAttack = ItemUtils.getBaseAtk(equipmentItemStack);
			ItemUtils.setBaseAtk(equipmentItemStack, baseAttack - atkManus);
		}
		int defManus = ItemUtils.getRuneDefBoost(equipmentItemStack);
		if(defManus > 0) {
			int baseDefense = ItemUtils.getBaseDef(equipmentItemStack);
			ItemUtils.setBaseDef(equipmentItemStack, baseDefense - defManus);
		}
		int durManus = ItemUtils.getRuneDurBoost(equipmentItemStack);
		if(durManus > 0) {
			int baseDurability = ItemUtils.getMaximumDurability(equipmentItemStack);
			ItemUtils.setMaximumDurability(equipmentItemStack, baseDurability - durManus);
		}
		
		ItemMeta itemMeta = equipmentItemStack.getItemMeta();
		List<String> newLores = new ArrayList<>();
		for(String lore : itemMeta.getLore()) {
			if(skipLines > 0) {
				skipLines--;
				continue;
			}
			else if(lore.contains("Rune (")) {
				WauzDebugger.log(player, "Cleared Rune Slot");
				lore = WauzEquipmentIdentifier.EMPTY_RUNE_SLOT;
				valid = true;
			}
			else if(lore.contains("Skillgem (")) {
				WauzDebugger.log(player, "Cleared Skill Slot");
				lore = WauzEquipmentIdentifier.EMPTY_SKILL_SLOT;
				valid = true;
				skipLines = 2;
			}
			newLores.add(lore);
		}
		
		if(valid) {
			itemMeta.setLore(newLores);
			equipmentItemStack.setItemMeta(itemMeta);
			player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 0);
		}
		return valid;
	}
	
}
