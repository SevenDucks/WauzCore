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

/**
 * Used to calculate the durability of equipment items.
 * 
 * @author Wauzmons
 */
public class DurabilityCalculator {
	
	/**
	 * Damages an item for exactly 1 point.
	 * Sends a warning message if the item broke or is about to break.
	 * 
	 * @param player The player who owns the item.
	 * @param itemToDamage The item to be damaged.
	 * @param armor If the item is armor.
	 */
	public static void damageItem(Player player, ItemStack itemToDamage, boolean armor) {
		damageItem(player, itemToDamage, 1, armor);
	}
	
	/**
	 * Damages an item for a given number of points.
	 * Sends a warning message if the item broke or is about to break.
	 * 
	 * @param player The player who owns the item.
	 * @param itemToDamage The item to be damaged.
	 * @param damage The damage, that should be done to the item.
	 * @param armor If the item is armor.
	 */
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
		
		EquipmentUtils.setCurrentDurability(itemToDamage, newDurability);
		WauzDebugger.log(player, "Durability: " + newDurability + " / " + maxDurability + " for " + displayName);
	}
	
	/**
	 * Completely restores the durability of an item.
	 * 
	 * @param player The player who owns the item.
	 * @param itemToRepair The item to be repaired.
	 */
	public static void repairItem(Player player, ItemStack itemToRepair) {
		setDamage(itemToRepair, 0);
		
		if(WauzMode.isMMORPG(player)) {
			int maxDurability = EquipmentUtils.getMaximumDurability(itemToRepair);
			if(maxDurability != 0) {
				EquipmentUtils.setCurrentDurability(itemToRepair, maxDurability);
			}
		}
	}
	
	/**
	 * Sets the vanilla Minecraft damage of an item.
	 * 
	 * @param itemStack The item to set the damage for.
	 * @param damage The new amount of damage.
	 */
	public static void setDamage(ItemStack itemStack, int damage) {
		if(itemStack.getItemMeta() instanceof Damageable) {
			Damageable damageable = (Damageable) itemStack.getItemMeta();
			damageable.setDamage(damage);
			itemStack.setItemMeta((ItemMeta) damageable);
		}
	}

}
