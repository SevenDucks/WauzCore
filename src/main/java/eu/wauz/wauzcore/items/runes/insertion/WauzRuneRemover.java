package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.identifiers.WauzEquipmentBuilder;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A helper class for removing runes and skillgems from items.
 * 
 * @author Wauzmons
 * 
 * @see WauzRuneInserter
 * @see WauzSkillgemInserter
 */
public class WauzRuneRemover {

	/**
	 * Removes all runes and skillgems from an equipment item stack and restores the empty slots.
	 * Returns false if the item had no slots in the first place.
	 * 
	 * @param player The player that is clearing the slots.
	 * @param equipmentItemStack The equipment that gets its slots cleared.
	 * 
	 * @return If the action was successful.
	 */
	public boolean clearAllSockets(Player player, ItemStack equipmentItemStack) {
		boolean valid = false;
		if(!ItemUtils.hasLore(equipmentItemStack)) {
			return false;
		}
		
		int skipLines = 0;
		int atkManus = EquipmentUtils.getRuneAtkBoost(equipmentItemStack);
		if(atkManus > 0) {
			int baseAttack = EquipmentUtils.getBaseAtk(equipmentItemStack);
			EquipmentUtils.setBaseAtk(equipmentItemStack, baseAttack - atkManus);
		}
		int defManus = EquipmentUtils.getRuneDefBoost(equipmentItemStack);
		if(defManus > 0) {
			int baseDefense = EquipmentUtils.getBaseDef(equipmentItemStack);
			EquipmentUtils.setBaseDef(equipmentItemStack, baseDefense - defManus);
		}
		int durManus = EquipmentUtils.getRuneDurBoost(equipmentItemStack);
		if(durManus > 0) {
			int baseDurability = EquipmentUtils.getMaximumDurability(equipmentItemStack);
			EquipmentUtils.setMaximumDurability(equipmentItemStack, baseDurability - durManus);
		}
		
		ItemMeta itemMeta = equipmentItemStack.getItemMeta();
		List<String> newLores = new ArrayList<>();
		for(String lore : Components.lore(itemMeta)) {
			if(skipLines > 0) {
				skipLines--;
				continue;
			}
			else if(lore.contains("Rune (")) {
				WauzDebugger.log(player, "Cleared Rune Slot");
				lore = WauzEquipmentBuilder.EMPTY_RUNE_SLOT;
				valid = true;
			}
			else if(lore.contains("Skillgem (")) {
				WauzDebugger.log(player, "Cleared Skill Slot");
				lore = WauzEquipmentBuilder.EMPTY_SKILL_SLOT;
				valid = true;
				skipLines = 2;
			}
			newLores.add(lore);
		}
		
		if(valid) {
			Components.lore(itemMeta, newLores);
			equipmentItemStack.setItemMeta(itemMeta);
			player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 0);
		}
		return valid;
	}
	
}
