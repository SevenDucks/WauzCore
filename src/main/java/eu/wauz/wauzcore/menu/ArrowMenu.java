package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.CustomWeaponBow;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Menu that can be opened with a bow, that lets the player select their arrows to shoot.
 * 
 * @author Wauzmons
 * 
 * @see CustomWeaponBow
 */
public class ArrowMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Shows all selectable arrow types, aswell as the count of arrows left.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerConfigurator#getSelectedArrows(Player)
	 * @see ArrowMenu#getArrowType(Player, String, String, Material)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new ArrowMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Arrows!");		
		
		String selectedArrowType = PlayerConfigurator.getSelectedArrows(player);
		
		menu.setItem(1, getArrowType(player, selectedArrowType, "normal", Material.ARROW));
		menu.setItem(2, getArrowType(player, selectedArrowType, "reinforced", Material.SPECTRAL_ARROW));
		menu.setItem(4, getArrowType(player, selectedArrowType, "fire", Material.TIPPED_ARROW));
		menu.setItem(5, getArrowType(player, selectedArrowType, "ice", Material.TIPPED_ARROW));
		menu.setItem(6, getArrowType(player, selectedArrowType, "shock", Material.TIPPED_ARROW));
		menu.setItem(7, getArrowType(player, selectedArrowType, "bomb", Material.TIPPED_ARROW));
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Generates an item stack, to show infos about a selectable arrow type.
	 * Includes arrow tip color, effect description and amount of arrows left.
	 * If it is the currently selected arrow, it will be highlighted with an enchantment glow.
	 * 
	 * @param player The player who owns the arrows.
	 * @param selectedArrowType The selected type arrow.
	 * @param arrowType The type of the arrow for this stack.
	 * @param material The arrow material. (Normal, Spectral, Tipped...)
	 * 
	 * @return The arrow type item stack.
	 */
	private static ItemStack getArrowType(Player player, String selectedArrowType, String arrowType, Material material) {
		ItemStack arrowItemStack = new ItemStack(material);
		ItemMeta arrowItemMeta = arrowItemStack.getItemMeta();
		arrowItemMeta.setDisplayName(ChatColor.WHITE + StringUtils.capitalize(arrowType) + " Arrows");
		List<String> lores = new ArrayList<String>();
		lores.add(getArrowDescription(arrowType));
		lores.add("");
		lores.add(ChatColor.GRAY + "Amount Left: " + PlayerConfigurator.getArrowAmount(player, arrowType) + " Arrows");
		arrowItemMeta.setLore(lores);
		if(selectedArrowType.equals(arrowType)) {
			arrowItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			arrowItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		PotionType arrowTipPotion = getArrowTipPotion(arrowType);
		if(arrowTipPotion != null) {
			((PotionMeta) arrowItemMeta).setBasePotionData(new PotionData(arrowTipPotion));
			arrowItemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		}
		arrowItemStack.setItemMeta(arrowItemMeta);
		return arrowItemStack;
	}
	
	/**
	 * Gets the colored description of the given arrow type.
	 * 
	 * @param arrowType The type of the arrow.
	 * 
	 * @return The colored description.
	 */
	private static String getArrowDescription(String arrowType) {
		switch (arrowType) {
		case "reinforced":
			return ChatColor.GREEN + "Deals doubled damage!";
		case "fire":
			return ChatColor.RED + "Ignites enemies!";
		case "ice":
			return ChatColor.BLUE + "Freezes enemies!";
		case "shock":
			return ChatColor.YELLOW + "Knockback on impact!";
		case "bomb":
			return ChatColor.DARK_PURPLE + "Explosion on impact!";
		default:
			return ChatColor.AQUA + "Never gets empty!";
		}
	}
	
	/**
	 * Gets the potion type to color the tip of the given arrow type in.
	 * 
	 * @param arrowType The type of the arrow.
	 * 
	 * @return The potion type for the arrow tip color.
	 */
	private static PotionType getArrowTipPotion(String arrowType) {
		switch (arrowType) {
		case "fire":
			return PotionType.STRENGTH;
		case "ice":
			return PotionType.WATER_BREATHING;
		case "shock":
			return PotionType.FIRE_RESISTANCE;
		case "bomb":
			return PotionType.SLOWNESS;
		default:
			return null;
		}
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If the clicked item is an arrow type, with more at least 1 arrow left, it will be selected.
	 * After that, the player will receive a message and the inventory will be closed.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see ItemUtils#getArrowCount(ItemStack)
	 * @see PlayerConfigurator#setSelectedArrowType(Player, String)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		int arrowCount = ItemUtils.getArrowCount(clicked);
		if(arrowCount < 1) {
			return;
		}
		
		String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
		PlayerConfigurator.setSelectedArrowType(player, displayName.split(" ")[0].toLowerCase());
		player.sendMessage(ChatColor.YELLOW + "You switched to " + displayName + "!");
		player.closeInventory();
	}

}
