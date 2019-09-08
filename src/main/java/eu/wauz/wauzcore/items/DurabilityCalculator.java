package eu.wauz.wauzcore.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.system.commands.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class DurabilityCalculator {
	
	public static void takeDamage(Player player, ItemStack itemStack, boolean armor) {
		int maxDurability = ItemUtils.getMaximumDurability(itemStack);
		if(maxDurability == 0) {
			return;
		}
		int durability = ItemUtils.getCurrentDurability(itemStack) - 1;
		
		String displayName = ItemUtils.hasDisplayName(itemStack)
				? itemStack.getItemMeta().getDisplayName()
				: itemStack.getType().toString();
		
		if(durability == 0) {
			itemStack.setAmount(0);
			if(armor) {
				player.getEquipment().setLeggings(null);
				player.getEquipment().setBoots(null);
			}
			player.sendMessage(ChatColor.RED + "Your " + displayName + ChatColor.RED + " just broke!");
			return;
		}
		else if(durability - 10 == 0) {
			player.sendMessage(ChatColor.YELLOW + "Your " + displayName + ChatColor.YELLOW + " is about to break!");
		}
		
		int materialDurability = itemStack.getType().getMaxDurability();
		int damage = maxDurability - durability;
		damage = (int) ((double) (damage * materialDurability) / (double) maxDurability);
		setDamage(itemStack, damage < materialDurability ? damage : materialDurability - 1);
		
		ItemUtils.setDurability(itemStack, durability);
		WauzDebugger.log(player, "Durability: " + durability + " / " + maxDurability + " for " + displayName);
	}
	
	public static void setDamage(ItemStack itemStack, int damage) {
		Damageable damageable = (Damageable) itemStack.getItemMeta();
		damageable.setDamage(damage);
		itemStack.setItemMeta((ItemMeta) damageable);
	}

}
