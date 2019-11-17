package eu.wauz.wauzcore.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.system.commands.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class DurabilityCalculator {
	
	public static void takeDamage(Player player, ItemStack itemStack, boolean armor) {
		takeDamage(player, itemStack, 1, armor);
	}
	
	public static void takeDamage(Player player, ItemStack itemStack, int damage, boolean armor) {
		int maxDurability = ItemUtils.getMaximumDurability(itemStack);
		if(maxDurability == 0 || itemStack.getItemMeta().isUnbreakable()) {
			return;
		}
		int oldDurability = ItemUtils.getCurrentDurability(itemStack);
		int newDurability = oldDurability - damage;
		
		String displayName = ItemUtils.hasDisplayName(itemStack)
				? itemStack.getItemMeta().getDisplayName()
				: itemStack.getType().toString();
		
		if(newDurability <= 0) {
			itemStack.setAmount(0);
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
		
		int materialDurability = itemStack.getType().getMaxDurability();
		int materialDamage = maxDurability - newDurability;
		materialDamage = (int) ((double) (materialDamage * materialDurability) / (double) maxDurability);
		setDamage(itemStack, materialDamage < materialDurability ? materialDamage : materialDurability - 1);
		
		ItemUtils.setDurability(itemStack, newDurability);
		WauzDebugger.log(player, "Durability: " + newDurability + " / " + maxDurability + " for " + displayName);
	}
	
	public static void setDamage(ItemStack itemStack, int damage) {
		Damageable damageable = (Damageable) itemStack.getItemMeta();
		damageable.setDamage(damage);
		itemStack.setItemMeta((ItemMeta) damageable);
	}

}
