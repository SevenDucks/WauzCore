package eu.wauz.wauzcore.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class SkillMenu implements WauzInventory {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###.000");
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new SkillMenu());
		int pts = PlayerConfigurator.getCharacterUnusedStatpoints(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Passive Skills "
				+ ChatColor.DARK_RED + ChatColor.BOLD + pts + " Points");
		
		int spent;
		
		spent = PlayerConfigurator.getCharacterHealthStatpoints(player);
		{
			ItemStack item = HeadUtils.getSkillHealthItem(spent);			
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Health");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
			lores.add("");
			lores.add(ChatColor.GRAY + "Increases Maximum Hitpoints by 5.");
			lores.add(ChatColor.GRAY + "Also instantly refills your Hitpoints.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Maximum HP: " + ChatColor.RED
					+ PlayerConfigurator.getCharacterHealth(player));
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(0, item);
		}
		
		spent = PlayerConfigurator.getCharacterTradingStatpoints(player);
		{
			ItemStack item = HeadUtils.getSkillTradingItem(spent);	
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Trading");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
			lores.add("");
			lores.add(ChatColor.GRAY + "Increases Sell Income by 10%.");
			lores.add(ChatColor.GRAY + "Applies for all ways of gaining Coins.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Coin Multiplier: " + ChatColor.GOLD
					+ PlayerConfigurator.getCharacterTrading(player) + "%");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(1, item);
		}
		
		spent = PlayerConfigurator.getCharacterLuckStatpoints(player);
		{
			ItemStack item = HeadUtils.getSkillLuckItem(spent);	
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Luck");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
			lores.add("");
			lores.add(ChatColor.GRAY + "Increases Rate on every 3rd Identify by 10%.");
			lores.add(ChatColor.GRAY + "Enhanced Equipment has special Effects.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Enhance-Rate: " + ChatColor.YELLOW
					+ PlayerConfigurator.getCharacterLuck(player) + "%");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(2, item);
		}
		
		spent = PlayerConfigurator.getCharacterManaStatpoints(player);
		{
			ItemStack item = HeadUtils.getSkillMagicItem(spent);	
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Magic");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
			lores.add("");
			lores.add(ChatColor.GRAY + "Increases Maximum Mana Points by 1.");
			lores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Staff Fighting.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Maximum MP: " + ChatColor.LIGHT_PURPLE
					+ PlayerConfigurator.getCharacterMana(player)
					+ ChatColor.GRAY + " (Max: 50)");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(3, item);
		}
		
		spent = PlayerConfigurator.getCharacterStrengthStatpoints(player);
		{
			ItemStack item = HeadUtils.getSkillStrengthItem(spent);	
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Strength");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
			lores.add("");
			lores.add(ChatColor.GRAY + "Increases Defense from Equip by 5%.");
			lores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Axe Combat.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Defense Multiplier: " + ChatColor.BLUE
					+ PlayerConfigurator.getCharacterStrength(player) + "%"
					+ ChatColor.GRAY + " (Max: 300%)");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(4, item);
		}
		
		spent = PlayerConfigurator.getCharacterAgilityStatpoints(player);
		{
			ItemStack item = HeadUtils.getSkillAgilityItem(spent);	
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Agility");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Points: " + ChatColor.GREEN + spent);
			lores.add("");
			lores.add(ChatColor.GRAY + "Increases Evasion/Crit-Chance by 1%.");
			lores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Sword Art.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Evasion/Crit-Chance: " + ChatColor.AQUA
					+ PlayerConfigurator.getCharacterAgility(player) + "%"
					+ ChatColor.GRAY + " (Max: 40%)");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(5, item);
		}
		
		{
			ItemStack item = new ItemStack(Material.IRON_HOE, 1);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_RED + "Staff Fighting");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
					+ formatter.format((float) ((float) PlayerConfigurator.getCharacterStaffSkill(player) / 1000)) + "%"
					+ ChatColor.GRAY + " (Max: "
					+ (int) (PlayerConfigurator.getCharacterStaffSkillMax(player) / 1000) + "%)");
			lores.add(ChatColor.GRAY + "Multiplied by "
					+ ((float) PlayerConfigurator.getCharacterManaStatpoints(player) * 5 / 100 + 1) + " from Magic");
			lores.add("");
			lores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
			lores.add(ChatColor.GRAY + "will increase your skill and thus");
			lores.add(ChatColor.GRAY + "the damage dealt with them.");
			im.setLore(lores);
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(im);
			menu.setItem(6, item);
		}
		
		{
			ItemStack item = new ItemStack(Material.IRON_AXE, 1);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_RED + "Axe Combat");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
					+ formatter.format((float) ((float) PlayerConfigurator.getCharacterAxeSkill(player) / 1000)) + "%"
					+ ChatColor.GRAY + " (Max: "
					+ (int) (PlayerConfigurator.getCharacterAxeSkillMax(player) / 1000) + "%)");
			lores.add(ChatColor.GRAY + "Multiplied by "
					+ ((float) PlayerConfigurator.getCharacterStrengthStatpoints(player) * 5 / 100 + 1) + " from Strength");
			lores.add("");
			lores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
			lores.add(ChatColor.GRAY + "will increase your skill and thus");
			lores.add(ChatColor.GRAY + "the damage dealt with them.");
			im.setLore(lores);
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(im);
			menu.setItem(7, item);
		}
		
		{
			ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.DARK_RED + "Sword Art");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Atk: " + ChatColor.RED
				+ formatter.format((float) ((float) PlayerConfigurator.getCharacterSwordSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerConfigurator.getCharacterSwordSkillMax(player) / 1000) + "%)");
			lores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerConfigurator.getCharacterAgilityStatpoints(player) * 5 / 100 + 1) + " from Agility");
			lores.add("");
			lores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
			lores.add(ChatColor.GRAY + "will increase your skill and thus");
			lores.add(ChatColor.GRAY + "the damage dealt with them.");
			im.setLore(lores);
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(im);
			menu.setItem(8, item);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !clicked.getType().equals(Material.PLAYER_HEAD))
			return;
		
		Integer total = PlayerConfigurator.getCharacterTotalStatpoints(player);
		Integer spent = PlayerConfigurator.getCharacterSpentStatpoints(player);
		Integer pts = total - spent;

		try {
			if(pts < 1) {
				player.sendMessage(ChatColor.RED + "You don't have Skillpoints left!");
				player.closeInventory();
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Health")) {
				PlayerConfigurator.increaseCharacterHealth(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Trading")) {
				PlayerConfigurator.increaseCharacterTrading(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Luck")) {
				PlayerConfigurator.increaseCharacterLuck(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Magic")) {
				PlayerConfigurator.increaseCharacterMana(player);
				SkillMenu.open(player);
				return;
			}	
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Strength")) {
				PlayerConfigurator.increaseCharacterStrength(player);
				SkillMenu.open(player);
				return;
			}
			
			else if(HeadUtils.isHeadMenuItem(clicked, "Agility")) {
				PlayerConfigurator.increaseCharacterAgility(player);
				SkillMenu.open(player);
				return;
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
