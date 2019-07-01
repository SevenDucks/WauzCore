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
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class ArrowMenu implements WauzInventory {
	
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

	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		int arrowCount = ItemUtils.getArrowCount(clicked);
		if(arrowCount < 1)
			return;
		
		String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
		PlayerConfigurator.setSelectedArrowType(player, displayName.split(" ")[0].toLowerCase());
		player.sendMessage(ChatColor.YELLOW + "You switched to " + displayName + "!");
		player.closeInventory();
	}

}
