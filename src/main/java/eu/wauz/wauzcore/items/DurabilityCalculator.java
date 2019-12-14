package eu.wauz.wauzcore.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class DurabilityCalculator {
	
	public static void damageItem(Player player, ItemStack itemToDamage, boolean armor) {
		damageItem(player, itemToDamage, 1, armor);
	}
	
	public static void damageItem(Player player, ItemStack itemToDamage, int damage, boolean armor) {
		int maxDurability = EquipmentUtils.getMaximumDurability(itemToDamage);
		if(maxDurability == 0 || itemToDamage.getItemMeta().isUnbreakable()) {
			return;
		}
		int oldDurability = EquipmentUtils.getCurrentDurability(itemToDamage);
		int newDurability = oldDurability - damage;
		
		String displayName = ItemUtils.hasDisplayName(itemToDamage)
				? itemToDamage.getItemMeta().getDisplayName()
				: itemToDamage.getType().toString();
		
		if(newDurability <= 0) {
			itemToDamage.setAmount(0);
			if(armor) {
				player.getEquipment().setLeggings(null);
				player.getEquipment().setBoots(null);
			}
			player.sendMessage(ChatColor.RED + "Your " + displayName + ChatColor.RED + " just broke!");
			return;
		}
		else if(oldDurability > 12 && newDurability - 12 <= 0) {
			player.sendMessage(ChatColor.YELLOW + "Your " + displayName + ChatColor.YELLOW + " is about to break!");
		}
		
		int materialDurability = itemToDamage.getType().getMaxDurability();
		int materialDamage = maxDurability - newDurability;
		materialDamage = (int) ((double) (materialDamage * materialDurability) / (double) maxDurability);
		setDamage(itemToDamage, materialDamage < materialDurability ? materialDamage : materialDurability - 1);
		
		EquipmentUtils.setDurability(itemToDamage, newDurability);
		WauzDebugger.log(player, "Durability: " + newDurability + " / " + maxDurability + " for " + displayName);
	}
	
	public static void repairItem(Player player, ItemStack itemToRepair) {
		setDamage(itemToRepair, 0);
		
		if(WauzMode.isMMORPG(player)) {
			int maxDurability = EquipmentUtils.getMaximumDurability(itemToRepair);
			if(maxDurability != 0) {
				EquipmentUtils.setDurability(itemToRepair, maxDurability);
			}
		}
	}
	
	public static void setDamage(ItemStack itemStack, int damage) {
		if(itemStack.getItemMeta() instanceof Damageable) {
			Damageable damageable = (Damageable) itemStack.getItemMeta();
			damageable.setDamage(damage);
			itemStack.setItemMeta((ItemMeta) damageable);
		}
	}

}
