package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the abilities menu, that is used for viewing passive skills and spending statpoints.
 * 
 * @author Wauzmons
 *
 * @see PlayerPassiveSkillConfigurator
 */
public class SkillMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Lists all passive skills, to spent points in, aswell as weapon skill stats.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getHealth(Player)
	 * @see PlayerPassiveSkillConfigurator#getTrading(Player)
	 * @see PlayerPassiveSkillConfigurator#getLuck(Player)
	 * @see PlayerPassiveSkillConfigurator#getMana(Player)
	 * @see PlayerPassiveSkillConfigurator#getStrength(Player)
	 * @see PlayerPassiveSkillConfigurator#getAgility(Player)
	 * 
	 * @see PlayerPassiveSkillConfigurator#getStaffSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getAxeSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getSwordSkill(Player)
	 * 
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new SkillMenu());
		int pts = PlayerPassiveSkillConfigurator.getUnusedStatpoints(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Passive Skills "
				+ ChatColor.DARK_RED + ChatColor.BOLD + pts + " Points");
		
		int spent;
		
		spent = PlayerPassiveSkillConfigurator.getHealthStatpoints(player);
		ItemStack skillHealthItemStack = HeadUtils.getSkillHealthItem(spent);			
		ItemMeta skillHealthItemMeta = skillHealthItemStack.getItemMeta();
		skillHealthItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Health");
		List<String> skillHealthLores = new ArrayList<String>();
		skillHealthLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillHealthLores.add("");
		skillHealthLores.add(ChatColor.GRAY + "Increases Maximum Hitpoints by 5.");
		skillHealthLores.add(ChatColor.GRAY + "Also instantly refills your Hitpoints.");
		skillHealthLores.add("");
		skillHealthLores.add(ChatColor.WHITE + "Maximum HP: " + ChatColor.RED
				+ PlayerPassiveSkillConfigurator.getHealth(player));
		skillHealthItemMeta.setLore(skillHealthLores);
		skillHealthItemStack.setItemMeta(skillHealthItemMeta);
		menu.setItem(0, skillHealthItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getTradingStatpoints(player);
		ItemStack skillTradingItemStack = HeadUtils.getSkillTradingItem(spent);	
		ItemMeta skillTradingItemMeta = skillTradingItemStack.getItemMeta();
		skillTradingItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Trading");
		List<String> skillTradingLores = new ArrayList<String>();
		skillTradingLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillTradingLores.add("");
		skillTradingLores.add(ChatColor.GRAY + "Increases Sell Income by 10%.");
		skillTradingLores.add(ChatColor.GRAY + "Applies for all ways of gaining Coins.");
		skillTradingLores.add("");
		skillTradingLores.add(ChatColor.WHITE + "Coin Multiplier: " + ChatColor.GOLD
				+ PlayerPassiveSkillConfigurator.getTrading(player) + "%");
		skillTradingItemMeta.setLore(skillTradingLores);
		skillTradingItemStack.setItemMeta(skillTradingItemMeta);
		menu.setItem(1, skillTradingItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getLuckStatpoints(player);
		ItemStack skillLuckItemStack = HeadUtils.getSkillLuckItem(spent);	
		ItemMeta skillLuckItemMeta = skillLuckItemStack.getItemMeta();
		skillLuckItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Luck");
		List<String> skillLuckLores = new ArrayList<String>();
		skillLuckLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillLuckLores.add("");
		skillLuckLores.add(ChatColor.GRAY + "Increases Rate on every 3rd Identify by 10%.");
		skillLuckLores.add(ChatColor.GRAY + "Enhanced Equipment has special Effects.");
		skillLuckLores.add("");
		skillLuckLores.add(ChatColor.WHITE + "Enhance-Rate: " + ChatColor.YELLOW
				+ PlayerPassiveSkillConfigurator.getLuck(player) + "%");
		skillLuckItemMeta.setLore(skillLuckLores);
		skillLuckItemStack.setItemMeta(skillLuckItemMeta);
		menu.setItem(2, skillLuckItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getManaStatpoints(player);
		ItemStack skillMagicItemStack = HeadUtils.getSkillMagicItem(spent);	
		ItemMeta skillMagicItemMeta = skillMagicItemStack.getItemMeta();
		skillMagicItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Magic");
		List<String> skillMagicLores = new ArrayList<String>();
		skillMagicLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillMagicLores.add("");
		skillMagicLores.add(ChatColor.GRAY + "Increases Maximum Mana Points by 1.");
		skillMagicLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Staff Fighting.");
		skillMagicLores.add("");
		skillMagicLores.add(ChatColor.WHITE + "Maximum MP: " + ChatColor.LIGHT_PURPLE
				+ PlayerPassiveSkillConfigurator.getMana(player)
				+ ChatColor.GRAY + " (Max: 50)");
		skillMagicItemMeta.setLore(skillMagicLores);
		skillMagicItemStack.setItemMeta(skillMagicItemMeta);
		menu.setItem(3, skillMagicItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getStrengthStatpoints(player);
		ItemStack skillStrengthItemStack = HeadUtils.getSkillStrengthItem(spent);	
		ItemMeta skillStrengthItemMeta = skillStrengthItemStack.getItemMeta();
		skillStrengthItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Strength");
		List<String> skillStrengthLores = new ArrayList<String>();
		skillStrengthLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillStrengthLores.add("");
		skillStrengthLores.add(ChatColor.GRAY + "Increases Defense from Equip by 5%.");
		skillStrengthLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Axe Combat.");
		skillStrengthLores.add("");
		skillStrengthLores.add(ChatColor.WHITE + "Defense Multiplier: " + ChatColor.BLUE
				+ PlayerPassiveSkillConfigurator.getStrength(player) + "%"
				+ ChatColor.GRAY + " (Max: 300%)");
		skillStrengthItemMeta.setLore(skillStrengthLores);
		skillStrengthItemStack.setItemMeta(skillStrengthItemMeta);
		menu.setItem(4, skillStrengthItemStack);
		
		spent = PlayerPassiveSkillConfigurator.getAgilityStatpoints(player);
		ItemStack skillAgilityItemStack = HeadUtils.getSkillAgilityItem(spent);	
		ItemMeta skillAgilityItemMeta = skillAgilityItemStack.getItemMeta();
		skillAgilityItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Agility");
		List<String> skillAgilityLores = new ArrayList<String>();
		skillAgilityLores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
		skillAgilityLores.add("");
		skillAgilityLores.add(ChatColor.GRAY + "Increases Evasion/Crit-Chance by 1%.");
		skillAgilityLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Sword Art.");
		skillAgilityLores.add("");
		skillAgilityLores.add(ChatColor.WHITE + "Evasion/Crit-Chance: " + ChatColor.AQUA
				+ PlayerPassiveSkillConfigurator.getAgility(player) + "%"
				+ ChatColor.GRAY + " (Max: 40%)");
		skillAgilityItemMeta.setLore(skillAgilityLores);
		skillAgilityItemStack.setItemMeta(skillAgilityItemMeta);
		menu.setItem(5, skillAgilityItemStack);
		
		ItemStack skillWeaponStaffItemStack = new ItemStack(Material.IRON_HOE, 1);
		ItemMeta skillWeaponStaffItemMeta = skillWeaponStaffItemStack.getItemMeta();
		skillWeaponStaffItemMeta.setDisplayName(ChatColor.DARK_RED + "Staff Fighting");
		List<String> skillWeaponStaffLores = new ArrayList<String>();
		skillWeaponStaffLores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
				+ Formatters.DEC.format((float) ((float) PlayerPassiveSkillConfigurator.getStaffSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerPassiveSkillConfigurator.getStaffSkillMax(player) / 1000) + "%)");
		skillWeaponStaffLores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerPassiveSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1) + " from Magic");
		skillWeaponStaffLores.add("");
		skillWeaponStaffLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponStaffLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponStaffLores.add(ChatColor.GRAY + "the damage dealt with them.");
		skillWeaponStaffItemMeta.setLore(skillWeaponStaffLores);
		skillWeaponStaffItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponStaffItemStack.setItemMeta(skillWeaponStaffItemMeta);
		menu.setItem(6, skillWeaponStaffItemStack);
		
		ItemStack skillWeaponAxeItemStack = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta skillWeaponAxeItemMeta = skillWeaponAxeItemStack.getItemMeta();
		skillWeaponAxeItemMeta.setDisplayName(ChatColor.DARK_RED + "Axe Combat");
		List<String> skillWeaponAxeLores = new ArrayList<String>();
		skillWeaponAxeLores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
				+ Formatters.DEC.format((float) ((float) PlayerPassiveSkillConfigurator.getAxeSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerPassiveSkillConfigurator.getAxeSkillMax(player) / 1000) + "%)");
		skillWeaponAxeLores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerPassiveSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1) + " from Strength");
		skillWeaponAxeLores.add("");
		skillWeaponAxeLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponAxeLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponAxeLores.add(ChatColor.GRAY + "the damage dealt with them.");
		skillWeaponAxeItemMeta.setLore(skillWeaponAxeLores);
		skillWeaponAxeItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponAxeItemStack.setItemMeta(skillWeaponAxeItemMeta);
		menu.setItem(7, skillWeaponAxeItemStack);
		
		ItemStack skillWeaponSwordItemStack = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta skillWeaponSwordItemMeta = skillWeaponSwordItemStack.getItemMeta();
		skillWeaponSwordItemMeta.setDisplayName(ChatColor.DARK_RED + "Sword Art");
		List<String> skillWeaponSwordLores = new ArrayList<String>();
		skillWeaponSwordLores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
			+ Formatters.DEC.format((float) ((float) PlayerPassiveSkillConfigurator.getSwordSkill(player) / 1000)) + "%"
			+ ChatColor.GRAY + " (Max: "
			+ (int) (PlayerPassiveSkillConfigurator.getSwordSkillMax(player) / 1000) + "%)");
		skillWeaponSwordLores.add(ChatColor.GRAY + "Multiplied by "
			+ ((float) PlayerPassiveSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1) + " from Agility");
		skillWeaponSwordLores.add("");
		skillWeaponSwordLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponSwordLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponSwordLores.add(ChatColor.GRAY + "the damage dealt with them.");
		skillWeaponSwordItemMeta.setLore(skillWeaponSwordLores);
		skillWeaponSwordItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponSwordItemStack.setItemMeta(skillWeaponSwordItemMeta);
		menu.setItem(8, skillWeaponSwordItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and adds a point to the selected skill, if any statpoints are left.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getTotalStatpoints(Player)
	 * @see PlayerPassiveSkillConfigurator#getSpentStatpoints(Player)
	 * 
	 * @see PlayerPassiveSkillConfigurator#increaseHealth(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseTrading(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseLuck(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseMana(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseStrength(Player)
	 * @see PlayerPassiveSkillConfigurator#increaseAgility(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !clicked.getType().equals(Material.PLAYER_HEAD))
			return;
		
		Integer total = PlayerPassiveSkillConfigurator.getTotalStatpoints(player);
		Integer spent = PlayerPassiveSkillConfigurator.getSpentStatpoints(player);
		Integer pts = total - spent;

		try {
			if(pts < 1) {
				player.sendMessage(ChatColor.RED + "You don't have Skillpoints left!");
				player.closeInventory();
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Health")) {
				PlayerPassiveSkillConfigurator.increaseHealth(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Trading")) {
				PlayerPassiveSkillConfigurator.increaseTrading(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Luck")) {
				PlayerPassiveSkillConfigurator.increaseLuck(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Magic")) {
				PlayerPassiveSkillConfigurator.increaseMana(player);
				SkillMenu.open(player);
				return;
			}	
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Strength")) {
				PlayerPassiveSkillConfigurator.increaseStrength(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Agility")) {
				PlayerPassiveSkillConfigurator.increaseAgility(player);
				SkillMenu.open(player);
				return;
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
