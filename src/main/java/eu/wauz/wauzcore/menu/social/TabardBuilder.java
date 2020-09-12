package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.GuildConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * This inventory lets a player create a tabard / banner for their guild.
 * 
 * @author Wauzmons
 * 
 * @see TabardMenu
 */
public class TabardBuilder implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "tabardbuilder";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		TabardBuilder.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Used for the initial opening, where a new tabard builder is initialized and the real editing menu is loaded.
	 * Only openable if  the player is a guild officer or higher.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see GuildOverviewMenu#validateOfficerAccess(Player, WauzPlayerGuild)
	 * @see TabardBuilder#TabardBuilder(ItemStack, String)
	 * @see TabardBuilder#open(Player, TabardBuilder)
	 */
	public static void open(Player player) {
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		if(!GuildOverviewMenu.validateOfficerAccess(player, playerGuild)) {
			return;
		}
		
		ItemStack bannerItemStack = new ItemStack(Material.WHITE_BANNER);
		ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
		bannerItemMeta.setDisplayName(ChatColor.GREEN + playerGuild.getGuildName() + " Tabard");
		bannerItemStack.setItemMeta(bannerItemMeta);
		open(player, new TabardBuilder(bannerItemStack, playerGuild.getGuildUuidString()));
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows the tabard that is being edited, aswell as options to change the base color or add a new layer.
	 * The tabard can also be saved or discarded from this menu.
	 * 
	 * @param player The player that should view the inventory.
	 * @param tabardBuilder
	 * 
	 * @see TabardBuilder#getTabard()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, TabardBuilder tabardBuilder) {
		WauzInventoryHolder holder = new WauzInventoryHolder(tabardBuilder);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Tabard Builder");
		
		tabardBuilder.page = "overview";
		
		ItemStack saveItemStack = GenericIconHeads.getConfirmItem();
		ItemMeta saveItemMeta = saveItemStack.getItemMeta();
		saveItemMeta.setDisplayName(ChatColor.GREEN + "Save Guild Tabard");
		saveItemStack.setItemMeta(saveItemMeta);
		menu.setItem(0, saveItemStack);
		
		if(tabardBuilder.getLayers() < 10) {
			ItemStack layerItemStack = new ItemStack(Material.DRIED_KELP);
			ItemMeta layerItemMeta = layerItemStack.getItemMeta();
			layerItemMeta.setDisplayName(ChatColor.GOLD + "Add New Layer");
			layerItemStack.setItemMeta(layerItemMeta);
			menu.setItem(3, layerItemStack);
		}
		
		menu.setItem(4, tabardBuilder.getTabard());
		
		if(tabardBuilder.getLayers() < 10) {
			ItemStack colorItemStack = new ItemStack(Material.CYAN_DYE);
			ItemMeta colorItemMeta = colorItemStack.getItemMeta();
			colorItemMeta.setDisplayName(ChatColor.AQUA + "Change Base Color");
			colorItemStack.setItemMeta(colorItemMeta);
			menu.setItem(5, colorItemStack);
		}
		
		ItemStack closeItemStack = GenericIconHeads.getDeclineItem();
		ItemMeta closeItemMeta = closeItemStack.getItemMeta();
		closeItemMeta.setDisplayName(ChatColor.RED + "Close Tabard Builder");
		closeItemStack.setItemMeta(closeItemMeta);
		menu.setItem(8, closeItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The tabard / banner item stack that is getting edited.
	 */
	private ItemStack tabard;
	
	/**
	 * The uuid of the guild that owns the tabard.
	 */
	private String guildUuidString;
	
	/**
	 * The name of the current editing page view.
	 */
	private String page;
	
	/**
	 * The amount of layers the tabard / banner has.
	 */
	private int layers = 0;
	
	/**
	 * Creates a new tabard builder for the given guild.
	 * 
	 * @param tabard The initial tabard / banner item stack that is getting edited.
	 * @param guildUuidString The uuid of the guild that owns the tabard.
	 */
	public TabardBuilder(ItemStack tabard, String guildUuidString) {
		this.tabard = tabard;
		this.guildUuidString = guildUuidString;
	}

	/**
	 * @return The tabard / banner item stack that is getting edited.
	 */
	public ItemStack getTabard() {
		return tabard;
	}

	/**
	 * @param tabard The new tabard / banner item stack that is getting edited.
	 */
	public void setTabard(ItemStack tabard) {
		this.tabard = tabard;
	}

	/**
	 * @return The uuid of the guild that owns the tabard.
	 */
	public String getGuildUuidString() {
		return guildUuidString;
	}

	/**
	 * @param guildUuidString The new uuid of the guild that owns the tabard.
	 */
	public void setGuildUuidString(String guildUuidString) {
		this.guildUuidString = guildUuidString;
	}

	/**
	 * @return The amount of layers the tabard / banner has.
	 */
	public int getLayers() {
		return layers;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and opens the selected view or executes the editing action.
	 * Possible actions are determined through the view name.
	 * From the overview a new layer can be added, the base color can be changed, or the tabard can be saved or declined.
	 * From the base color selection, the new color can be directly loaded into the tabard.
	 * From the layer color selection, the layer pattern selection can be viewed with the selected color.
	 * From the layer pattern selection, the colored layer can be directly added to the tabard.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see TabardBuilder#open(Player, TabardBuilder)
	 * @see TabardBuilder#openColorSelection(Player)
	 * @see TabardBuilder#openPatternSelection(Player, DyeColor)
	 * @see GuildConfigurator#setGuildTabard(String, ItemStack)
	 * @see GuildOverviewMenu#open(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		
		else if(page.equals("overview")) {
			if(HeadUtils.isHeadMenuItem(clicked, "Save Guild Tabard")) {
				WauzPlayerGuild playerGuild = WauzPlayerGuild.getGuild(guildUuidString);
				if(playerGuild != null) {
					playerGuild.setGuildTabard(player, tabard);
					GuildConfigurator.setGuildTabard(guildUuidString, tabard);
					GuildOverviewMenu.open(player);
				}
				else {
					player.sendMessage(ChatColor.RED + "An Error occurred while saving the tabard!");
				}
				return;
			}
			if(HeadUtils.isHeadMenuItem(clicked, "Close Tabard Builder")) {
				GuildOverviewMenu.open(player);
				return;
			}
			if(ItemUtils.isSpecificItem(clicked, "Add New Layer")) {
				page = "layer-color";
				openColorSelection(player);
				return;
			}
			if(ItemUtils.isSpecificItem(clicked, "Change Base Color")) {
				page = "base-color";
				openColorSelection(player);
				return;
			}
		}
		
		else if(page.equalsIgnoreCase("base-color")) {
			if(clicked.getType().toString().endsWith("_BANNER")) {
				tabard.setType(clicked.getType());
				open(player, this);
				return;
			}
		}
		
		else if(page.equalsIgnoreCase("layer-color")) {
			if(clicked.getType().toString().endsWith("_BANNER")) {
				DyeColor color = DyeColor.valueOf(StringUtils.substringBefore(clicked.getType().toString(), "_BANNER"));
				openPatternSelection(player, color);
				return;
			}
		}
		
		else if(page.equalsIgnoreCase("layer-pattern")) {
			if(clicked.getType().toString().endsWith("_BANNER")) {
				BannerMeta bm = (BannerMeta) tabard.getItemMeta();
				List<Pattern> patterns = bm.getPatterns() != null ? bm.getPatterns() : new ArrayList<>();
				patterns.addAll(((BannerMeta) clicked.getItemMeta()).getPatterns());
				bm.setPatterns(patterns);
				tabard.setItemMeta(bm);
				layers++;
				open(player, this);
				return;
			}
		}
	}
	
	/**
	 * Shows a view of all selectable base or layer colors for the tabard / banner.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public void openColorSelection(Player player) {
		String colorType = page.contains("layer") ? "Layer" : "Base";
		WauzInventoryHolder holder = new WauzInventoryHolder(this);
		Inventory menu = Bukkit.createInventory(holder, 18, ChatColor.BLACK + "" + ChatColor.BOLD + "Select Tabard " + colorType + " Color");
		
		menu.setItem(0, new ItemStack(Material.WHITE_BANNER));
		menu.setItem(1, new ItemStack(Material.ORANGE_BANNER));
		menu.setItem(2, new ItemStack(Material.MAGENTA_BANNER));
		menu.setItem(3, new ItemStack(Material.LIGHT_BLUE_BANNER));
		menu.setItem(4, new ItemStack(Material.YELLOW_BANNER));
		menu.setItem(5, new ItemStack(Material.LIME_BANNER));
		menu.setItem(6, new ItemStack(Material.PINK_BANNER));
		menu.setItem(7, new ItemStack(Material.GRAY_BANNER));
		menu.setItem(8, new ItemStack(Material.LIGHT_GRAY_BANNER));
		
		menu.setItem(10, new ItemStack(Material.CYAN_BANNER));
		menu.setItem(11, new ItemStack(Material.PURPLE_BANNER));
		menu.setItem(12, new ItemStack(Material.BLUE_BANNER));
		menu.setItem(13, new ItemStack(Material.BROWN_BANNER));
		menu.setItem(14, new ItemStack(Material.GREEN_BANNER));
		menu.setItem(15, new ItemStack(Material.RED_BANNER));
		menu.setItem(16, new ItemStack(Material.BLACK_BANNER));
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Shows a view of all selectable layer patterns in the given dye color.
	 * 
	 * @param player The player that should view the inventory.
	 * @param color The color to show the patterns in.
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public void openPatternSelection(Player player, DyeColor color) {
		WauzInventoryHolder holder = new WauzInventoryHolder(this);
		Inventory menu = Bukkit.createInventory(holder, 45, ChatColor.BLACK + "" + ChatColor.BOLD + "Select Tabard Layer Pattern");
		
		page = "layer-pattern";
		
		for(int iterator = 0; iterator < PatternType.values().length; iterator++) {
			ItemStack tabd = new ItemStack(color == DyeColor.WHITE ? Material.BLACK_BANNER : Material.WHITE_BANNER);
			BannerMeta bm = (BannerMeta) tabd.getItemMeta();
			bm.setDisplayName(ChatColor.WHITE + "Republic Wauzland");
			bm.setPatterns(Collections.singletonList(new Pattern(color, PatternType.values()[iterator])));
			tabd.setItemMeta(bm);
			menu.setItem(iterator, tabd);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

}
