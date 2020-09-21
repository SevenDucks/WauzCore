package eu.wauz.wauzcore.system.economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.ShopMenu;
import eu.wauz.wauzcore.mobs.citizens.RelationTracker;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Chance;

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
	 * Tries to buy the given item for the player.
	 * Unlike ammo items (arrows), physical items, can only be bought when there is inventory space left.
	 * Items cannot be bought, if the player doesn't have enough money.
	 * 
	 * @param player The player who wants to buy the item.
	 * @param itemToBuy The item that should be bought.
	 * @param discount The discount on the price of the item.
	 * 
	 * @return If the buying was successful.
	 * 
	 * @see WauzCurrency#getCurrencyAmount(Player)
	 * @see ItemUtils#fitsInInventory(Inventory, ItemStack)
	 * @see ItemUtils#isAmmoItem(ItemStack)
	 * @see PlayerConfigurator#setArrowAmount(Player, String, int)
	 */
	public static boolean buy(Player player, WauzShopItem itemToBuy, WauzShopDiscount discount) {
		WauzCurrency currency = itemToBuy.getShopItemCurrency();
		long shopItemPrice = itemToBuy.getShopItemPrice();
		int currencyCost = (int) ((1.0 - discount.getTotalDiscount()) * (double) shopItemPrice);
		currencyCost = currencyCost < 1 ? 1 : currencyCost;
		long money = currency.getCurrencyAmount(player);
		
		if(money < currencyCost) {
			player.sendMessage(ChatColor.RED + "You don't have enough money!");
			player.closeInventory();
			return false;
		}
		
		ItemStack boughtItem = itemToBuy.getInstance(player, discount, true);
		if(ItemUtils.isAmmoItem(boughtItem)) {
			String displayName = ChatColor.stripColor(boughtItem.getItemMeta().getDisplayName());
			String arrowType = displayName.split(" ")[0].toLowerCase();
			int arrowAmount = PlayerConfigurator.getArrowAmount(player, arrowType) + boughtItem.getAmount();
			arrowAmount = arrowAmount > 999 ? 999 : arrowAmount;
			
			currency.setCurrencyAmount(player, money - currencyCost);
			PlayerConfigurator.setArrowAmount(player, arrowType, arrowAmount);
			String arrowString = "(" + arrowAmount + " / 999) " + displayName;
			player.sendMessage(ChatColor.GREEN + "Your now have " + arrowString + "s with you!");
		}
		else {
			if(!ItemUtils.fitsInInventory(player.getInventory(), boughtItem)) {
				player.sendMessage(ChatColor.RED + "Your inventory is full!");
				player.closeInventory();
				return false;
			}
			currency.setCurrencyAmount(player, money - currencyCost);
			player.getInventory().addItem(boughtItem);
			player.sendMessage(ChatColor.GREEN + "Your purchase was successful!");
			WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
		}
		if(StringUtils.isNotBlank(discount.getCitizenName()) && itemToBuy.getShopItemRelationExp() > 0) {
			RelationTracker.addProgress(player, discount.getCitizenName(), itemToBuy.getShopItemRelationExp());
		}
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
		return true;
	}
	
	/**
	 * Tries to sell the given item for the player.
	 * The item stack will be sold for a random value per item.
	 * Some items have a fixed sell value in lore, while bought items are worth nothing.
	 * 
	 * @param player The player who wants to sell the item.
	 * @param itemToSell The item that should be sold.
	 * @param sellSound If a sell sound should be played.
	 * 
	 * @return If the selling was successful.
	 * 
	 * @see PlayerCollectionConfigurator#setCharacterCoins(Player, long)
	 * @see ItemUtils#isBoughtItem(ItemStack)
	 * @see ItemUtils#getSellValue(ItemStack)
	 * @see PlayerSkillConfigurator#getTradingFloat(Player)
	 * @see AchievementTracker#addProgress(Player, WauzAchievementType, double)
	 */
	public static boolean sell(Player player, ItemStack itemToSell, boolean sellSound) {
		if(itemToSell.equals(null) || itemToSell.getType().equals(Material.AIR)) {
			return false;
		}
		
		int price;
		long money = PlayerCollectionConfigurator.getCharacterCoins(player);
		
		if(ItemUtils.isBoughtItem(itemToSell)) {
			price = 0;
		}
		else {
			price = ItemUtils.getSellValue(itemToSell);
			if(price == 0) {
				price = Chance.minMax(1, 3) * itemToSell.getAmount();
			}
		}
		
		long priceOld = price;
		price = (int) ((float) price * (float) PlayerSkillConfigurator.getTradingFloat(player));
		WauzDebugger.log(player, "Item-Price: " + price + " (" + priceOld + ")");
		String displayName = ItemUtils.hasDisplayName(itemToSell) ? itemToSell.getItemMeta().getDisplayName() : "Item";
		
		PlayerCollectionConfigurator.setCharacterCoins(player, money + price);
		AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, price);
		itemToSell.setAmount(0);
		
		if(sellSound) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
		}
		player.sendMessage(displayName + ChatColor.GREEN + " was sold for " + price + " Coins!");
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
	 * @see PlayerCollectionConfigurator#setCharacterCoins(Player, long)
	 * @see DurabilityCalculator#repairItem(Player, ItemStack)
	 */
	public static boolean repair(Player player, ItemStack itemToRepair, boolean fromShop) {
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
			long money = PlayerCollectionConfigurator.getCharacterCoins(player);
			if(money < price) {
				player.sendMessage(ChatColor.RED + "You don't have enough money to rapair this item!");
				return false;
			}
			PlayerCollectionConfigurator.setCharacterCoins(player, money - price);
			DurabilityCalculator.repairItem(player, itemToRepair);
			player.sendMessage(ChatColor.GREEN + "Your item was repaired for " + price + " Coins!");
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
			return true;
		}
		else {
			DurabilityCalculator.repairItem(player, itemToRepair);
			player.sendMessage(ChatColor.GREEN + "Your item was repaired for one Scroll!");
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
			return true;
		}
	}

}
