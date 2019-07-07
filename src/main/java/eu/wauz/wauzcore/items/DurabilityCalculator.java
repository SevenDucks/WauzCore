package eu.wauz.wauzcore.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class DurabilityCalculator {
	
	public static void takeDamage(Player player, ItemStack itemStack, boolean armor) {
		org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) itemStack.getItemMeta();
		int durability = damageable.getDamage() + 1;
		int maxDurability = itemStack.getType().getMaxDurability();
		damageable.setDamage(durability);
		itemStack.setItemMeta((ItemMeta) damageable);
		
		String displayName = ItemUtils.hasDisplayName(itemStack)
				? itemStack.getItemMeta().getDisplayName()
				: itemStack.getType().toString();
		
		if(durability >= maxDurability) {
			itemStack.setAmount(0);
			if(armor) {
				player.getEquipment().setLeggings(null);
				player.getEquipment().setBoots(null);
			}
			player.sendMessage(ChatColor.RED + "Your " + displayName + ChatColor.RED + " just broke!");
		}
		else if(durability + 10 == maxDurability) {
			player.sendMessage(ChatColor.YELLOW + "Your " + displayName + ChatColor.YELLOW + " is about to break!");
		}
		WauzDebugger.log(player, "Durability: " + durability + " / " + maxDurability + " for " + displayName);
	}

}
