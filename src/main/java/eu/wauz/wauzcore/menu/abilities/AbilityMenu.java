package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the main menu, that is used to display ability mechanics.
 * 
 * @author Wauzmons
 *
 * @see WauzMenu
 */
public class AbilityMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "abilities";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		AbilityMenu.open(player);
	}

	/**
	 * Opens the menu for the given player.
	 * All menus for ability mechanics plus a short information are shown.
	 * Here is a quick summary:</br>
	 * Slot 1: The travelling menu + current region display.</br>
	 * Slot 2: The crafting menu + crafting level display.</br>
	 * Slot 4: Return to main menu...</br>
	 * Slot 7: The skill menu + spent skill points display.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerData#getRegion()
	 * @see PlayerSkillConfigurator#getCraftingSkill(Player)
	 * @see PlayerSkillConfigurator#getSpentStatpoints(Player)
	 * @see MenuUtils#setMainMenuOpener(Inventory, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new AbilityMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Ability Menu");
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		ItemStack travellingItemStack = MenuIconHeads.getPortsItem();
		ItemMeta travellingItemMeta = travellingItemStack.getItemMeta();
		travellingItemMeta.setDisplayName(ChatColor.GOLD + "Travelling");
		List<String> travellingLores = new ArrayList<String>();
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		if(instance != null) {
			travellingLores.add(ChatColor.DARK_PURPLE + "Region: " + ChatColor.YELLOW + instance.getInstanceName());
		}
		else {
			WauzRegion region = playerData.getRegion();
			travellingLores.add(ChatColor.DARK_PURPLE + "Region: " + ChatColor.YELLOW
					+ (region != null ?  region.getTitle() : "(None)"));
		}
		travellingLores.add("");
		travellingLores.add(ChatColor.GRAY + "Teleport yourself to other Locations");
		travellingLores.add(ChatColor.GRAY + "or view the Lore of visited Regions.");
		travellingItemMeta.setLore(travellingLores);
		travellingItemStack.setItemMeta(travellingItemMeta);
		menu.setItem(1, travellingItemStack);
		
		ItemStack craftingItemStack = MenuIconHeads.getCraftItem();
		ItemMeta craftingItemMeta = craftingItemStack.getItemMeta();
		craftingItemMeta.setDisplayName(ChatColor.GOLD + "Crafting");
		List<String> craftingLores = new ArrayList<String>();
		craftingLores.add(ChatColor.DARK_PURPLE + "Crafting Level: " + ChatColor.YELLOW
			+ PlayerSkillConfigurator.getCraftingSkill(player) + " / " + WauzCore.MAX_CRAFTING_SKILL);
		craftingLores.add("");
		craftingLores.add(ChatColor.GRAY + "Make new Items out of Materials.");
		craftingLores.add(ChatColor.GRAY + "Craft Items to learn new Recipes.");
		craftingItemMeta.setLore(craftingLores);
		craftingItemStack.setItemMeta(craftingItemMeta);
		menu.setItem(2, craftingItemStack);
		
		MenuUtils.setComingSoon(menu, "Disguises", 3);
		MenuUtils.setComingSoon(menu, "Subclasses", 5);
		
		ItemStack skillsItemStack = MenuIconHeads.getSkillItem();
		ItemMeta skillsItemMeta = skillsItemStack.getItemMeta();
		skillsItemMeta.setDisplayName(ChatColor.GOLD + "Passive Skills");
		List<String> skillsLores = new ArrayList<String>();
		skillsLores.add(ChatColor.DARK_PURPLE + "Spent Skillpoints: " + ChatColor.YELLOW
				+ PlayerSkillConfigurator.getSpentStatpoints(player) + " / "
				+ PlayerSkillConfigurator.getTotalStatpoints(player));
		skillsLores.add("");
		skillsLores.add(ChatColor.GRAY + "Spend Points to improve your Stats.");
		skillsLores.add(ChatColor.GRAY + "You gain 2 Points per Level-Up!");
		skillsItemMeta.setLore(skillsLores);
		if(PlayerSkillConfigurator.getUnusedStatpoints(player) > 0) {
			WauzDebugger.log(player, "Detected Unused Skillpoints");
			skillsItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			skillsItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		skillsItemStack.setItemMeta(skillsItemMeta);
		menu.setItem(6, skillsItemStack);
		
		MenuUtils.setComingSoon(menu, "Perk Tree", 7);
		
		MenuUtils.setMainMenuOpener(menu, 4);
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a sub menu was clicked, it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see TravellingMenu#open(Player)
	 * @see CraftingMenu#open(Player)
	 * @see SkillMenu#open(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Travelling")) {
			TravellingMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Crafting")) {
			CraftingMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Passive Skills")) {
			SkillMenu.open(player);
		}
	}

}
