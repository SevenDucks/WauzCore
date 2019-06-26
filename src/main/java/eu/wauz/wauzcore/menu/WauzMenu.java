package eu.wauz.wauzcore.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import net.md_5.bungee.api.ChatColor;

public class WauzMenu implements WauzInventory {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");

	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Wauzland Main Menu");
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;

		MenuUtils.setCurrencyDisplay(menu, player, 0);
		
		{
			ItemStack port = HeadUtils.getPortsItem();
			ItemMeta im = port.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Travelling");
			List<String> lores = new ArrayList<String>();
			WauzRegion region = playerData.getRegion();
			lores.add(ChatColor.DARK_PURPLE + "Region: " + ChatColor.YELLOW
					+ (region != null ?  region.getTitle() : "(None)"));
			lores.add("");
			lores.add(ChatColor.GRAY + "Teleport yourself to other Locations");
			lores.add(ChatColor.GRAY + "or view the Lore of visited Regions.");
			im.setLore(lores);
			port.setItemMeta(im);
			menu.setItem(1, port);
		}
		
		{
			ItemStack glds = HeadUtils.getGuildItem();
			ItemMeta im = glds.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Guild");
			List<String> lores = new ArrayList<String>();
			WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
			lores.add(ChatColor.DARK_PURPLE + "Your Guild: " + ChatColor.YELLOW
					+ (guild != null ?  guild.getGuildName() : "(None)"));
			lores.add("");
			lores.add(ChatColor.GRAY + "Join a Guild to get Bonuses, Tabards");
			lores.add(ChatColor.GRAY + "and Guildhall Access for all your Chars!");
			im.setLore(lores);
			glds.setItemMeta(im);
			menu.setItem(2, glds);
		}
		
		{
			ItemStack grps = HeadUtils.getGroupItem();
			ItemMeta im = grps.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Group");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Listed Groups: " + ChatColor.YELLOW
					+ WauzPlayerGroupPool.getGroups().size());
			lores.add("");
			lores.add(ChatColor.GRAY + "Group up with other Players");
			lores.add(ChatColor.GRAY + "and enter their Instances together!");
			im.setLore(lores);
			grps.setItemMeta(im);
			menu.setItem(3, grps);
		}
		
		{
			ItemStack ditm = HeadUtils.getItemsItem();
			ItemMeta im = ditm.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Dungeon Items");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Collected Items: " + ChatColor.YELLOW
				+ PlayerConfigurator.getCollectedDungeonItemsString(player) + " / 4");
			lores.add("");
			lores.add(ChatColor.GRAY + "Equip Items with special Properties,");
			lores.add(ChatColor.GRAY + "which can be found in lvl 20 Dungeons.");
			im.setLore(lores);
			ditm.setItemMeta(im);
			menu.setItem(4, ditm);
		}
		
		{
			ItemStack qlog = HeadUtils.getQuestItem();
			ItemMeta im = qlog.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Questlog");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Completed Quests: " + ChatColor.YELLOW
				+ formatter.format(PlayerConfigurator.getCharacterCompletedQuests(player)));
			lores.add("");
			lores.add(ChatColor.GRAY + "View or Cancel your running Quests.");
			lores.add(ChatColor.GRAY + "Use the Questfinder to locate Questgivers.");
			im.setLore(lores);
			qlog.setItemMeta(im);
			menu.setItem(5, qlog);
		}
		
		{
			ItemStack craf = HeadUtils.getCraftItem();
			ItemMeta im = craf.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Crafting");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Crafting Level: " + ChatColor.YELLOW
				+ PlayerPassiveSkillConfigurator.getCraftingSkill(player) + " / " + WauzCore.MAX_CRAFTING_SKILL);
			lores.add("");
			lores.add(ChatColor.GRAY + "Make new Items out of Materials.");
			lores.add(ChatColor.GRAY + "Craft Items to learn new Recipes.");
			im.setLore(lores);
			craf.setItemMeta(im);
			menu.setItem(6, craf);
		}
		
		{
			ItemStack pets = HeadUtils.getTamesItem();
			ItemMeta im = pets.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Pets");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Used Pet Slots: " + ChatColor.YELLOW
					+ PlayerConfigurator.getCharacterUsedPetSlots(player) + " / 5");
				lores.add("");
			lores.add(ChatColor.GRAY + "View and Summon your tamed Pets.");
			lores.add(ChatColor.GRAY + "Breed them to get stronger Offsprings.");
			im.setLore(lores);
			pets.setItemMeta(im);
			menu.setItem(7, pets);
		}
		
		{
			ItemStack pass = HeadUtils.getSkillItem();
			ItemMeta im = pass.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Skills");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Spent Skillpoints: " + ChatColor.YELLOW
					+ PlayerPassiveSkillConfigurator.getSpentStatpoints(player) + " / "
					+ PlayerPassiveSkillConfigurator.getTotalStatpoints(player));
			lores.add("");
			lores.add(ChatColor.GRAY + "Spend Points to improve your Stats.");
			lores.add(ChatColor.GRAY + "You gain 2 Points per Level-Up!");
			im.setLore(lores);
			if(PlayerPassiveSkillConfigurator.getUnusedStatpoints(player) > 0) {
				WauzDebugger.log(player, "Detected Unused Skillpoints");
				im.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			pass.setItemMeta(im);
			menu.setItem(8, pass);
		}
		
		player.openInventory(menu);
	}

	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Travelling"))
			TravellingMenu.open(player);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Guild"))
			GuildOverviewMenu.open(player);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Group"))
			GroupMenu.open(player);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Dungeon Items"))
			DungeonItemMenu.open(player);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Questlog"))
			QuestBuilder.open(player);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Crafting"))
			CraftingMenu.open(player);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Pets"))
			PetOverviewMenu.open(player, -1);
		
		else if(HeadUtils.isHeadMenuItem(clicked, "Skills"))
			SkillMenu.open(player);
	}

}
