package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * This inventory lets a player choose their equipped tabard / banner.
 * 
 * @author Wauzmons
 * 
 * @see TabardBuilder
 */
public class TabardMenu implements WauzInventory {
	
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
		WauzInventoryHolder holder = new WauzInventoryHolder(new TabardMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Tabard!");
		
		{
			ItemStack tabd = getTabardByName(player, "No Tabard");
			menu.setItem(0, tabd);
		}
		
		{
			ItemStack tabd = getTabardByName(player, "Guild Tabard");
			menu.setItem(1, tabd);
		}
		
		{
			ItemStack tabd = getTabardByName(player, "Republic Wauzland");
			menu.setItem(2, tabd);
		}
		
		{
			ItemStack tabd = getTabardByName(player, "Eternal Empire");
			menu.setItem(3, tabd);
		}
		
		{
			ItemStack tabd = getTabardByName(player, "Dark Legion");
			menu.setItem(4, tabd);
		}
		
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
		
		String tabardDisplay = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
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
			String tabardDisplay = ChatColor.stripColor(bannerItemMeta.getDisplayName());
			bannerItemMeta.setDisplayName(ChatColor.RESET + "Cosmetic Item [" + tabardDisplay + "]");
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
			bannerMeta.setDisplayName(ChatColor.WHITE + "Guild Tabard");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.GREEN + "Guild: " + playerGuild.getGuildName());
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bannerMeta.setLore(lores);
			bannerItemStack.setItemMeta(bannerMeta);
			return bannerItemStack;
			
		case "Republic Wauzland":
			bannerItemStack = new ItemStack(Material.WHITE_BANNER);
			bannerMeta = (BannerMeta) bannerItemStack.getItemMeta();
			bannerMeta.setDisplayName(ChatColor.WHITE + "Republic Wauzland");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bannerMeta.setLore(lores);
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
			bannerMeta.setDisplayName(ChatColor.WHITE + "Eternal Empire");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bannerMeta.setLore(lores);
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
			bannerMeta.setDisplayName(ChatColor.WHITE + "Dark Legion");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bannerMeta.setLore(lores);
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
			bannerItemMeta.setDisplayName(ChatColor.WHITE + "No Tabard");
			lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Unequips Current Tabard");
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bannerItemMeta.setLore(lores);
			bannerItemStack.setItemMeta(bannerItemMeta);
			return bannerItemStack;
		}
	}

}
