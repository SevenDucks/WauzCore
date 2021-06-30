package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.worlds.instances.WauzActiveInstance;
import eu.wauz.wauzcore.worlds.instances.WauzActiveInstancePool;

/**
 * An util to add ability sub menus to the main menu.
 * 
 * @author Wauzmons
 *
 * @see WauzMenu
 */
public class AbilityMenuParts {
	
	/**
	 * Fills the given main menu with sub menu icons.<br>
	 * Row 1, Slot 1: The job menu + crafted item count display.<br>
	 * Row 1, Slot 2: The skill menu + spent skill points display.<br>
	 * Row 2, Slot 1: The travelling menu + current region display.<br>
	 * Row 2, Slot 2: CS - Paragon
	 * 
	 * @param player The player that should view the inventory.
	 * @param menu The main menu inventory.
	 * @param startIndex The first slot of the inventory to fill.
	 */
	public static void addMenuParts(Player player, Inventory menu, int startIndex) {
		ItemStack craftingItemStack = MenuIconHeads.getCraftItem();
		ItemMeta craftingItemMeta = craftingItemStack.getItemMeta();
		Components.displayName(craftingItemMeta, ChatColor.GOLD + "Jobs");
		List<String> craftingLores = new ArrayList<String>();
		craftingLores.add(ChatColor.DARK_PURPLE + "Crafted Items: " + ChatColor.YELLOW
				+ Formatters.INT.format(PlayerCollectionConfigurator.getCharacterAchievementProgress(player, WauzAchievementType.CRAFT_ITEMS)));
		craftingLores.add("");
		craftingLores.add(ChatColor.GRAY + "View your Job Skill Progression");
		craftingLores.add(ChatColor.GRAY + "and Craft Items out of Materials.");
		Components.lore(craftingItemMeta, craftingLores);
		craftingItemStack.setItemMeta(craftingItemMeta);
		menu.setItem(startIndex, craftingItemStack);
		
		ItemStack skillsItemStack = MenuIconHeads.getSkillItem();
		ItemMeta skillsItemMeta = skillsItemStack.getItemMeta();
		Components.displayName(skillsItemMeta, ChatColor.GOLD + "Skills");
		List<String> skillsLores = new ArrayList<String>();
		skillsLores.add(ChatColor.DARK_PURPLE + "Spent Skillpoints: " + ChatColor.YELLOW
				+ PlayerSkillConfigurator.getSpentStatpoints(player) + " / "
				+ PlayerSkillConfigurator.getTotalStatpoints(player));
		skillsLores.add("");
		skillsLores.add(ChatColor.GRAY + "Spend Points to improve your Stats.");
		skillsLores.add(ChatColor.GRAY + "You gain 2 Points per Level-Up!");
		Components.lore(skillsItemMeta, skillsLores);
		if(PlayerSkillConfigurator.getUnusedStatpoints(player) > 0) {
			WauzDebugger.log(player, "Detected Unused Skillpoints");
			skillsItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			skillsItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		skillsItemStack.setItemMeta(skillsItemMeta);
		menu.setItem(startIndex + 1, skillsItemStack);
		
		ItemStack travellingItemStack = MenuIconHeads.getPortsItem();
		ItemMeta travellingItemMeta = travellingItemStack.getItemMeta();
		Components.displayName(travellingItemMeta, ChatColor.GOLD + "Travelling");
		List<String> travellingLores = new ArrayList<String>();
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		if(instance != null) {
			travellingLores.add(ChatColor.DARK_PURPLE + "Region: " + ChatColor.YELLOW + instance.getInstanceName());
		}
		else {
			WauzRegion region = WauzPlayerDataPool.getPlayer(player).getSelections().getRegion();
			travellingLores.add(ChatColor.DARK_PURPLE + "Region: " + ChatColor.YELLOW
					+ (region != null ?  region.getTitle() : "(None)"));
		}
		travellingLores.add("");
		travellingLores.add(ChatColor.GRAY + "Teleport yourself to other Locations");
		travellingLores.add(ChatColor.GRAY + "or view the Lore of visited Regions.");
		Components.lore(travellingItemMeta, travellingLores);
		travellingItemStack.setItemMeta(travellingItemMeta);
		menu.setItem(startIndex + 9, travellingItemStack);
		
		MenuUtils.setComingSoon(menu, "Paragon", startIndex + 10);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * 
	 * @param player The placer who clicked a menu item.
	 * @param clicked The item that was clicled.
	 * 
	 * @return If an event was triggered.
	 * 
	 * @see CraftingMenu#open(Player)
	 * @see SkillMenu#open(Player)
	 * @see TravellingMenu#open(Player)
	 */
	public static boolean check(Player player, ItemStack clicked) {
		if(HeadUtils.isHeadMenuItem(clicked, "Jobs")) {
			JobMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Skills")) {
			SkillMenu.open(player);
			return true;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Travelling")) {
			TravellingMenu.open(player);
			return true;
		}
		return false;
	}

}
