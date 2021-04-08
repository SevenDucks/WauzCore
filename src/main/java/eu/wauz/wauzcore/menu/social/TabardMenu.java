package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * This inventory lets a player choose their equipped tabard / banner.
 * 
 * @author Wauzmons
 * 
 * @see TabardBuilder
 */
@PublicMenu
public class TabardMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "tabards";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		TabardMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows a list of all selectable tabards / banners.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see TabardMenu#getTabardByName(Player, String)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Tabard!";
		Inventory menu = Components.inventory(new TabardMenu(), menuTitle, 9);
		
		menu.setItem(0, getTabardByName(player, "No Tabard"));
		menu.setItem(1, getTabardByName(player, "Guild Tabard"));
		menu.setItem(2, getTabardByName(player, "Republic Wauzland"));
		menu.setItem(3, getTabardByName(player, "Eternal Empire"));
		menu.setItem(4, getTabardByName(player, "Dark Legion"));
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and equips the tabard / banner, if a valid one was clicked.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerConfigurator#setCharacterTabard(Player, String)
	 * @see TabardMenu#equipSelectedTabard(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		
		if(!ItemUtils.isBanner(clicked)) {
			if(!ItemUtils.isMaterial(clicked, Material.BARRIER) || !ItemUtils.isSpecificItem(clicked, "No Tabard")) {
				return;
			}
		}
		
		String tabardDisplay = ChatColor.stripColor(Components.displayName(clicked.getItemMeta()));
		PlayerConfigurator.setCharacterTabard(player, tabardDisplay);
		equipSelectedTabard(player);
		player.sendMessage(ChatColor.GREEN + "Equipped Tabard: " + tabardDisplay);
		player.closeInventory();
	}
	
	/**
	 * Lets the player equip the tabard that has been set in their config.
	 * If it is null or a barrier, air will be equipped in the tabard slot.
	 * 
	 * @param player The player that should equip the tabard.
	 * 
	 * @see PlayerConfigurator#getCharacterTabard(Player)
	 * @see TabardMenu#getTabardByName(Player, String)
	 */
	public static void equipSelectedTabard(Player player) {
		String tabardName = PlayerConfigurator.getCharacterTabard(player);
		ItemStack bannerItemStack = getTabardByName(player, tabardName);
		if(bannerItemStack == null || bannerItemStack.getType().equals(Material.BARRIER)) {
			bannerItemStack = null;
		}
		else {
			ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
			String tabardDisplay = ChatColor.stripColor(Components.displayName(bannerItemMeta));
			Components.displayName(bannerItemMeta, ChatColor.RESET + "Cosmetic Item [" + tabardDisplay + "]");
			bannerItemStack.setItemMeta(bannerItemMeta);
		}
		player.getEquipment().setHelmet(bannerItemStack);
	}
	
	/**
	 * Creates a banner item stack for the given tabard name,
	 * that is either based on a preset, a custom guild tabard, or a barrier menu item, able to unequip the current tabard.
	 * 
	 * @param player The player who requested the tabard.
	 * @param tabardName The name of the requested tabard.
	 * 
	 * @return The requested tabard.
	 */
	private static ItemStack getTabardByName(Player player, String tabardName) {
		ItemStack bannerItemStack;
		BannerMeta bannerMeta;
		List<String> lores;
		List<Pattern> patterns;
		
		switch (tabardName) {
		case "Guild Tabard":
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild == null)
				return null;
			bannerItemStack = playerGuild.getGuildTabard();
			bannerMeta = (BannerMeta) bannerItemStack.getItemMeta();
			Components.displayName(bannerMeta, ChatColor.WHITE + "Guild Tabard");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.GREEN + "Guild: " + playerGuild.getGuildName());
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			Components.lore(bannerMeta, lores);
			bannerItemStack.setItemMeta(bannerMeta);
			return bannerItemStack;
			
		case "Republic Wauzland":
			bannerItemStack = new ItemStack(Material.WHITE_BANNER);
			bannerMeta = (BannerMeta) bannerItemStack.getItemMeta();
			Components.displayName(bannerMeta, ChatColor.WHITE + "Republic Wauzland");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			Components.lore(bannerMeta, lores);
			patterns = new ArrayList<Pattern>();
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM));
			patterns.add(new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
			bannerMeta.setPatterns(patterns);
			bannerItemStack.setItemMeta(bannerMeta);
			return bannerItemStack;
			
		case "Eternal Empire":
			bannerItemStack = new ItemStack(Material.YELLOW_BANNER);
			bannerMeta = (BannerMeta) bannerItemStack.getItemMeta();
			Components.displayName(bannerMeta, ChatColor.WHITE + "Eternal Empire");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			Components.lore(bannerMeta, lores);
			patterns = new ArrayList<Pattern>();
			patterns.add(new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP));
			patterns.add(new Pattern(DyeColor.YELLOW, PatternType.RHOMBUS_MIDDLE));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));
			patterns.add(new Pattern(DyeColor.RED, PatternType.GRADIENT_UP));
			bannerMeta.setPatterns(patterns);
			bannerItemStack.setItemMeta(bannerMeta);		
			return bannerItemStack;
			
		case "Dark Legion":
			bannerItemStack = new ItemStack(Material.BLUE_BANNER);
			bannerMeta = (BannerMeta) bannerItemStack.getItemMeta();
			Components.displayName(bannerMeta, ChatColor.WHITE + "Dark Legion");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			Components.lore(bannerMeta, lores);
			patterns = new ArrayList<Pattern>();
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_SMALL));
			patterns.add(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP));
			patterns.add(new Pattern(DyeColor.LIGHT_BLUE, PatternType.RHOMBUS_MIDDLE));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.FLOWER));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM));
			bannerMeta.setPatterns(patterns);
			bannerItemStack.setItemMeta(bannerMeta);
			return bannerItemStack;
			
		default:
			bannerItemStack = new ItemStack(Material.BARRIER);
			ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
			Components.displayName(bannerItemMeta, ChatColor.WHITE + "No Tabard");
			lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Unequips Current Tabard");
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			Components.lore(bannerItemMeta, lores);
			bannerItemStack.setItemMeta(bannerItemMeta);
			return bannerItemStack;
		}
	}

}
