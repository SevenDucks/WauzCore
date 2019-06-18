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
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

public class TabardMenu implements WauzInventory {
	
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

	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
		if(!ItemUtils.isBanner(clicked))
			if(!ItemUtils.isMaterial(clicked, Material.BARRIER) || !ItemUtils.isSpecificItem(clicked, "No Tabard"))
				return;
		
		String tabardDisplay = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
		PlayerConfigurator.setCharacterTabard(player, tabardDisplay);
		equipSelectedTabard(player);
		player.sendMessage(ChatColor.GREEN + "Equipped Tabard: " + tabardDisplay);
		player.closeInventory();
	}
	
	public static void equipSelectedTabard(Player player) {
		String tabardName = PlayerConfigurator.getCharacterTabard(player);
		ItemStack tabd = getTabardByName(player, tabardName);
		if(tabd == null || tabd.getType().equals(Material.BARRIER)) {
			tabd = null;
		}
		else {
			ItemMeta im = tabd.getItemMeta();
			String tabardDisplay = ChatColor.stripColor(im.getDisplayName());
			im.setDisplayName(ChatColor.RESET + "Cosmetic Item [" + tabardDisplay + "]");
			tabd.setItemMeta(im);
		}
		player.getEquipment().setHelmet(tabd);
	}
	
	private static ItemStack getTabardByName(Player player, String tabardName) {
		ItemStack tabd;
		BannerMeta bm;
		List<String> lores;
		List<Pattern> patterns;
		
		switch (tabardName) {
		case "Guild Tabard":
			WauzPlayerGuild pg = PlayerConfigurator.getGuild(player);
			if(pg == null)
				return null;
			tabd = pg.getGuildTabard();
			bm = (BannerMeta) tabd.getItemMeta();
			bm.setDisplayName(ChatColor.WHITE + "Guild Tabard");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.GREEN + "Guild: " + pg.getGuildName());
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bm.setLore(lores);
			tabd.setItemMeta(bm);
			return tabd;
			
		case "Republic Wauzland":
			tabd = new ItemStack(Material.WHITE_BANNER);
			bm = (BannerMeta) tabd.getItemMeta();
			bm.setDisplayName(ChatColor.WHITE + "Republic Wauzland");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bm.setLore(lores);
			patterns = new ArrayList<Pattern>();
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM));
			patterns.add(new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
			bm.setPatterns(patterns);
			tabd.setItemMeta(bm);
			return tabd;
			
		case "Eternal Empire":
			tabd = new ItemStack(Material.YELLOW_BANNER);
			bm = (BannerMeta) tabd.getItemMeta();
			bm.setDisplayName(ChatColor.WHITE + "Eternal Empire");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bm.setLore(lores);
			patterns = new ArrayList<Pattern>();
			patterns.add(new Pattern(DyeColor.YELLOW, PatternType.HALF_HORIZONTAL));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP));
			patterns.add(new Pattern(DyeColor.YELLOW, PatternType.RHOMBUS_MIDDLE));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.BORDER));
			patterns.add(new Pattern(DyeColor.RED, PatternType.GRADIENT_UP));
			bm.setPatterns(patterns);
			tabd.setItemMeta(bm);		
			return tabd;
			
		case "Dark Legion":
			tabd = new ItemStack(Material.BLUE_BANNER);
			bm = (BannerMeta) tabd.getItemMeta();
			bm.setDisplayName(ChatColor.WHITE + "Dark Legion");
			lores = new ArrayList<String>();
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			bm.setLore(lores);
			patterns = new ArrayList<Pattern>();
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_SMALL));
			patterns.add(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP));
			patterns.add(new Pattern(DyeColor.LIGHT_BLUE, PatternType.RHOMBUS_MIDDLE));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.FLOWER));
			patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM));
			bm.setPatterns(patterns);
			tabd.setItemMeta(bm);
			return tabd;
			
		default:
			tabd = new ItemStack(Material.BARRIER);
			ItemMeta im = tabd.getItemMeta();
			im.setDisplayName(ChatColor.WHITE + "No Tabard");
			lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Unequips Current Tabard");
			lores.add("");
			lores.add(ChatColor.YELLOW + "Click to select!");
			im.setLore(lores);
			tabd.setItemMeta(im);
			return tabd;
		}
	}

}
