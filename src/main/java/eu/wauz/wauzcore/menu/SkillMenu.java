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

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import net.md_5.bungee.api.ChatColor;

public class SkillMenu implements WauzInventory {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###.000");
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new SkillMenu());
		int pts = PlayerPassiveSkillConfigurator.getUnusedStatpoints(player);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Passive Skills "
				+ ChatColor.DARK_RED + ChatColor.BOLD + pts + " Points");
		
		int spent;
		
		spent = PlayerPassiveSkillConfigurator.getHealthStatpoints(player);
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
					+ PlayerPassiveSkillConfigurator.getHealth(player));
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(0, item);
		}
		
		spent = PlayerPassiveSkillConfigurator.getTradingStatpoints(player);
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
					+ PlayerPassiveSkillConfigurator.getTrading(player) + "%");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(1, item);
		}
		
		spent = PlayerPassiveSkillConfigurator.getLuckStatpoints(player);
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
					+ PlayerPassiveSkillConfigurator.getLuck(player) + "%");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(2, item);
		}
		
		spent = PlayerPassiveSkillConfigurator.getManaStatpoints(player);
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
					+ PlayerPassiveSkillConfigurator.getMana(player)
					+ ChatColor.GRAY + " (Max: 50)");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(3, item);
		}
		
		spent = PlayerPassiveSkillConfigurator.getStrengthStatpoints(player);
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
					+ PlayerPassiveSkillConfigurator.getStrength(player) + "%"
					+ ChatColor.GRAY + " (Max: 300%)");
			im.setLore(lores);
			item.setItemMeta(im);
			menu.setItem(4, item);
		}
		
		spent = PlayerPassiveSkillConfigurator.getAgilityStatpoints(player);
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
					+ PlayerPassiveSkillConfigurator.getAgility(player) + "%"
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
					+ formatter.format((float) ((float) PlayerPassiveSkillConfigurator.getStaffSkill(player) / 1000)) + "%"
					+ ChatColor.GRAY + " (Max: "
					+ (int) (PlayerPassiveSkillConfigurator.getStaffSkillMax(player) / 1000) + "%)");
			lores.add(ChatColor.GRAY + "Multiplied by "
					+ ((float) PlayerPassiveSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1) + " from Magic");
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
					+ formatter.format((float) ((float) PlayerPassiveSkillConfigurator.getAxeSkill(player) / 1000)) + "%"
					+ ChatColor.GRAY + " (Max: "
					+ (int) (PlayerPassiveSkillConfigurator.getAxeSkillMax(player) / 1000) + "%)");
			lores.add(ChatColor.GRAY + "Multiplied by "
					+ ((float) PlayerPassiveSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1) + " from Strength");
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
				+ formatter.format((float) ((float) PlayerPassiveSkillConfigurator.getSwordSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerPassiveSkillConfigurator.getSwordSkillMax(player) / 1000) + "%)");
			lores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerPassiveSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1) + " from Agility");
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
