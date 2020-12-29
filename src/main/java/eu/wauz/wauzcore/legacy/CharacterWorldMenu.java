//package eu.wauz.wauzcore.legacy;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//
//import eu.wauz.wauzcore.menu.CharacterClassMenu;
//import eu.wauz.wauzcore.menu.CharacterSlotMenu;
//import eu.wauz.wauzcore.menu.util.MenuUtils;
//import eu.wauz.wauzcore.menu.util.WauzInventory;
//import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
//import eu.wauz.wauzcore.players.WauzPlayerData;
//import eu.wauz.wauzcore.players.WauzPlayerDataPool;
//
///**
// * An inventory that can be used as menu or for other custom interaction mechanics.
// * A character creation menu, that will let the player select their world.
// * 
// * @author Wauzmons
// * 
// * @see CharacterSlotMenu
// * @see CharacterClassMenu
// */
//public class CharacterWorldMenu implements WauzInventory {
//	
//	/**
//	 * @return The id of the inventory.
//	 */
//	@Override
//	public String getInventoryId() {
//		return "worlds";
//	}
//	
//	/**
//	 * Opens the menu for the given player.
//	 * Shows two hardcoded worlds to choose: "Dalyreos" and "Wauzland".
//	 * 
//	 * @param player The player that should view the inventory.
//	 * 
//	 * @see MenuUtils#setBorders(Inventory)
//	 */
//	public static void open(Player player) {
//		WauzInventoryHolder holder = new WauzInventoryHolder(new CharacterWorldMenu());
//		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your World!");
//		
//		ItemStack world2 = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
//		ItemMeta im2 = world2.getItemMeta();
//		im2.setDisplayName(ChatColor.AQUA + "Dalyreos");
//		List<String> lores2 = new ArrayList<String>();
//		lores2.add(ChatColor.WHITE + "Main Map (15Kx15K)");
//		im2.setLore(lores2);
//		world2.setItemMeta(im2);
//		menu.setItem(3, world2);
//		
//		ItemStack world1 = new ItemStack(Material.LIME_CONCRETE);
//		ItemMeta im1 = world1.getItemMeta();
//		im1.setDisplayName(ChatColor.GREEN + "Wauzland");
//		List<String> lores1 = new ArrayList<String>();
//		lores1.add(ChatColor.WHITE + "Alpha Test Map (2Kx2K)");
//		im1.setLore(lores1);
//		world1.setItemMeta(im1);
//		menu.setItem(5, world1);
//		
//		MenuUtils.setBorders(menu);
//		player.openInventory(menu);
//	}
//	
//	/**
//	 * Checks if an event in this inventory was triggered by a player click.
//	 * The default event will be automatically canceled.
//	 * If the clicked item is a world selection, it will be cached in the player data.
//	 * Next the class selection will be shown.
//	 * 
//	 * @param event The inventory click event.
//	 * 
//	 * @see WauzPlayerData#setSelectedCharacterWorld(String)
//	 * @see CharacterClassMenu#open(Player)
//	 */
//	@Override
//	public void selectMenuPoint(InventoryClickEvent event) {
//		event.setCancelled(true);
//		ItemStack clicked = event.getCurrentItem();
//		Player player = (Player) event.getWhoClicked();
//		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
//		
//		if(playerData == null) {
//			return;
//		}
//		if(clicked == null || !clicked.getType().toString().endsWith("CONCRETE")) {
//			return;
//		}
//		else if(clicked.getItemMeta().getDisplayName().contains("Wauzland")) {
//			playerData.setSelectedCharacterWorld("Wauzland");
//		}
//		else if(clicked.getItemMeta().getDisplayName().contains("Dalyreos")) {
//			playerData.setSelectedCharacterWorld("Dalyreos");
//		}
//		CharacterClassMenu.open(player);
//	}
//	
//}
