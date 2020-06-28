package eu.wauz.wauzcore.system.economy;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.menu.ShopMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * A collection of actions, executable from a shop.
 * 
 * @author Wauzmons
 *
 * @see WauzShop
 * @see ShopMenu
 */
public class WauzShopActions {
	
	/**
	 * Tries to sell the given item for the player.
	 * The item stack will either be sold for a random value per item or with bonus on attack / defense.
	 * Some items have a fixed sell value in lore, while bought items are worth nothing.
	 * TODO: Also refactor this, when implementing equipment builder.
	 * 
	 * @param player The player who wants to sell the item.
	 * @param itemToSell The item that should be sold.
	 * @param fromShop If the selling was triggered from a shop menu.
	 * 
	 * @return If the selling was successful.
	 * 
	 * @see PlayerConfigurator#setCharacterCoins(Player, long)
	 * @see PlayerPassiveSkillConfigurator#getTradingFloat(Player)
	 * @see AchievementTracker#addProgress(Player, WauzAchievementType, double)
	 */
	public static boolean sell(Player player, ItemStack itemToSell, Boolean fromShop) {
		if(itemToSell.equals(null) || itemToSell.getType().equals(Material.AIR)) {
			return false;
		}
		
		int price = (int) (Math.random() * 3 + 1) * itemToSell.getAmount();
		long money = PlayerConfigurator.getCharacterCoins(player);
		
		if(itemToSell.hasItemMeta() && itemToSell.getItemMeta().hasLore()) {
			List<String> lores = itemToSell.getItemMeta().getLore();
			
			for(String s : lores) {
				if(s.contains("Attack")) {
					String[] parts = s.split(" ");
					price = (int) (((Integer.parseInt(parts[1]) / 1.5) * (Math.random() + 2)) * itemToSell.getAmount());
				}
				else if(s.contains("Defense")) {
					String[] parts = s.split(" ");
					price = (int) (((Integer.parseInt(parts[1]) * 3.0) * (Math.random() + 2)) * itemToSell.getAmount());
				}
				
				if(s.contains("Bought")) {
					price = 0;
				}
				else if(s.contains("Sell Value")) {
					String[] parts = s.split(" ");
					price = Integer.parseInt(parts[2]) * itemToSell.getAmount();
				}
			}
		}
		
		long priceOld = price;
		price = (int) ((float) price * (float) PlayerPassiveSkillConfigurator.getTradingFloat(player));
		
		WauzDebugger.log(player, "Item-Price: " + price + " (" + priceOld + ")");
		
		PlayerConfigurator.setCharacterCoins(player, money + price);
		AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, price);
		itemToSell.setAmount(0);
		
		WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
		player.sendMessage(ChatColor.GREEN + "Your item was sold for " + price + " Coins!");
		if(fromShop) {
			MenuUtils.setCurrencyDisplay(player.getOpenInventory().getTopInventory(), player, 0);
		}
		return true;
	}
	
	/**
	 * Tries to repair the given item for the player.
	 * Cannot repair items with no or max durability value.
	 * If the item is repaired in a shop menu, it will have a cost, equal to the lost durability.
	 * 
	 * @param player The player who wants to repair the item.
	 * @param itemToRepair The item that should be repaired.
	 * @param fromShop If the repairing was triggered from a shop menu.
	 * 
	 * @return If the repairing was successful.
	 * 
	 * @see EquipmentUtils#getCurrentDurability(ItemStack)
	 * @see EquipmentUtils#getMaximumDurability(ItemStack)
	 * @see PlayerConfigurator#setCharacterCoins(Player, long)
	 * @see DurabilityCalculator#repairItem(Player, ItemStack)
	 */
	public static boolean repair(Player player, ItemStack itemToRepair, Boolean fromShop) {
		if(itemToRepair.equals(null) || itemToRepair.getType().equals(Material.AIR)) {
			if(fromShop) {
				player.sendMessage(ChatColor.RED + "You can't repair air!");
				player.closeInventory();
			}
			return false;
		}
		
		int durability = EquipmentUtils.getCurrentDurability(itemToRepair);
		if(durability == 0) {
			player.sendMessage(ChatColor.RED + "This Item can't be repaired!");
			return false;
		}
		
		int maxDurability = EquipmentUtils.getMaximumDurability(itemToRepair);
		if(durability == maxDurability) {
			player.sendMessage(ChatColor.RED + "This Item already has full durability!");
			return false;
		}
		
		if(fromShop) {
			int price = maxDurability - durability;
			long money = PlayerConfigurator.getCharacterCoins(player);
			
			if(price < money) {
				PlayerConfigurator.setCharacterCoins(player, money - price);
				
				DurabilityCalculator.repairItem(player, itemToRepair);
				player.sendMessage(ChatColor.GREEN + "Your item was repaired for " + price + " Coins!");
				MenuUtils.setCurrencyDisplay(player.getOpenInventory().getTopInventory(), player, 0);
				return true;
			}
			else {
				player.sendMessage(ChatColor.RED + "You don't have enough money to rapair this item!");
				return false;
			}
		}
		else {
			DurabilityCalculator.repairItem(player, itemToRepair);
			player.sendMessage(ChatColor.GREEN + "Your item was repaired for one Scroll!");
			return true;
		}
	}

}
