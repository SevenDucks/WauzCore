package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class GroupMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group Overview");
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		
		if(pd.isInGroup()) {
			WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(pd.getGroupUuidString());
			
			ItemStack grps = HeadUtils.getGroupItem();
			ItemMeta gim = grps.getItemMeta();
			gim.setDisplayName(ChatColor.BLUE + "Group");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Mode: " + ChatColor.YELLOW
					+ pg.getWauzMode());
			lores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
					+ Bukkit.getPlayer(UUID.fromString(pg.getAdminUuidString())).getName());
			lores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
					+ pg.getPlayerAmount() + " / 5");
			lores.add("");
			for(String lore : pg.getWrappedGroupDescription())
				lores.add(ChatColor.GRAY + lore);
			lores.add("");
			lores.add(pg.isPasswordProtected() ? ChatColor.RED + "Password: "
					+ pg.getGroupPassword() : ChatColor.GREEN + "No Password");
			lores.add("");
			lores.add(ChatColor.DARK_PURPLE + "Commands:");
			lores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "group " + ChatColor.GRAY + "Open the Group Menu");
			lores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "grp [text] " + ChatColor.GRAY + "Send Message in Group Chat");
			lores.add(ChatColor.GOLD + "/" + ChatColor.WHITE + "desc [text] " + ChatColor.GRAY + "Set the Group Description");
			lores.add("");
			lores.add(ChatColor.DARK_GRAY + "UUID: " + pg.getGroupUuidString());
			gim.setLore(lores);
			grps.setItemMeta(gim);
			menu.setItem(0, grps);
			
			int playerNumber = 1;
			for(Player member : pg.getPlayers()) {
				boolean isGroupLeader = pg.isGroupAdmin(member);
				String name = isGroupLeader
					? ChatColor.GOLD + member.getName() + " [Leader]"
					: ChatColor.YELLOW + member.getName() + " [Member]";
				
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				sm.setDisplayName(name);
				sm.setOwningPlayer(member);
				List<String> slores = new ArrayList<String>();
				slores.add(ChatColor.GRAY + "Click to teleport!");
				sm.setLore(slores);
				skull.setItemMeta(sm);
				menu.setItem(playerNumber, skull);
				playerNumber++;
			}
			
			if(pg.isGroupAdmin(player)) {
				ItemStack promote = new ItemStack(Material.GOLDEN_HELMET);
				ItemMeta pim = promote.getItemMeta();
				pim.setDisplayName(ChatColor.RED + "Change Leader");
				pim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				promote.setItemMeta(pim);
				menu.setItem(6, promote);
				
				ItemStack kick = new ItemStack(Material.IRON_BOOTS);
				ItemMeta kim = kick.getItemMeta();
				kim.setDisplayName(ChatColor.RED + "Kick Member");
				kim.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				kick.setItemMeta(kim);
				menu.setItem(7, kick);
			}
			
			ItemStack quit = new ItemStack(Material.BARRIER);
			ItemMeta qim = quit.getItemMeta();
			qim.setDisplayName(ChatColor.RED + "Leave Group");
			quit.setItemMeta(qim);
			menu.setItem(8, quit);
		}
		else {
			List<WauzPlayerGroup> groups = WauzPlayerGroupPool.getGroups();
			int inventorySize = groups.size() + 2;
			if(inventorySize <= 9) inventorySize = 9;
			else if(inventorySize <= 18) inventorySize = 18;
			else if(inventorySize <= 27) inventorySize = 27;
			else if(inventorySize <= 36) inventorySize = 36;
			else if(inventorySize <= 45) inventorySize = 45;
			else inventorySize = 54;
			
			menu = Bukkit.createInventory(holder, inventorySize, ChatColor.BLACK + "" + ChatColor.BOLD + "Group List");
			
			ItemStack create = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
			ItemMeta cim = create.getItemMeta();
			cim.setDisplayName(ChatColor.BLUE + "Create Open Group");
			create.setItemMeta(cim);
			menu.setItem(0, create);
			
			ItemStack createPw = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
			ItemMeta cimPw = createPw.getItemMeta();
			cimPw.setDisplayName(ChatColor.YELLOW + "Create Password Group");
			createPw.setItemMeta(cimPw);
			menu.setItem(1, createPw);
			
			String wauzMode = WauzMode.getMode(player.getWorld().getName()).toString();
			
			int groupNumber = 2;
			for(WauzPlayerGroup pg : groups) {
				if(groupNumber >= inventorySize)
					break;
				
				ItemStack grp = new ItemStack(pg.isFull() || !pg.getWauzMode().equals(wauzMode) ? Material.BLACK_CONCRETE :
						(pg.isPasswordProtected() ? Material.YELLOW_CONCRETE : Material.LIME_CONCRETE));
				ItemMeta gim = grp.getItemMeta();
				gim.setDisplayName(ChatColor.BLUE + "Group");
				List<String> lores = new ArrayList<String>();
				lores.add(ChatColor.DARK_PURPLE + "Mode: " + ChatColor.YELLOW
						+ pg.getWauzMode());
				lores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
						+ Bukkit.getPlayer(UUID.fromString(pg.getAdminUuidString())).getName());
				lores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
						+ pg.getPlayerAmount() + " / 5");
				lores.add("");
				for(String lore : pg.getWrappedGroupDescription())
					lores.add(ChatColor.GRAY + lore);
				lores.add("");
				lores.add(pg.isPasswordProtected() ? ChatColor.RED + "Password Protected" : ChatColor.GREEN + "No Password");
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "UUID: " + pg.getGroupUuidString());
				gim.setLore(lores);
				grp.setItemMeta(gim);
				menu.setItem(groupNumber, grp);
				groupNumber++;
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public static void passwordInput(Player player, String groupUuidString, String passwordString) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group Password: "
				+ ChatColor.DARK_RED + passwordString);
		
		int slot = 0;
		while(slot < 9) {
			ItemStack number = new ItemStack(Material.GRAY_CONCRETE);
			ItemMeta im = number.getItemMeta();
			im.setDisplayName(ChatColor.RESET + "" + (slot + 1));
			if(groupUuidString != null) {
				List<String> lores = new ArrayList<String>();
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "UUID: " + groupUuidString);
				im.setLore(lores);
			}
			number.setItemMeta(im);
			menu.setItem(slot, number);
			slot++;
		}
		
		player.openInventory(menu);
	}
	
	public static void playerSelection(Player player, boolean promote) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group: "
				+ (promote ? "Promote" : "Kick"));
		
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(pd.getGroupUuidString());
		
		int playerNumber = 1;
		for(Player member : pg.getPlayers()) {
			if(pg.isGroupAdmin(member))
				continue;
			
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta sm = (SkullMeta) skull.getItemMeta();
			sm.setDisplayName(ChatColor.YELLOW + member.getName());
			sm.setOwningPlayer(member);
			skull.setItemMeta(sm);
			menu.setItem(playerNumber, skull);
			playerNumber += 2;
		}

		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
		// Create Group
		else if(clicked.getType().equals(Material.LIGHT_BLUE_CONCRETE)) {
			if(clicked.getItemMeta().getDisplayName().contains("Open")) {
				WauzPlayerGroup pg = new WauzPlayerGroup(player);
				WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
				pd.setGroupUuidString(pg.getGroupUuidString());
				WauzPlayerGroupPool.regGroup(pg);
				open(player);
			}
			else
				passwordInput(player, null, "_");
		}
		
		// Join Password Group
		else if(clicked.getType().equals(Material.YELLOW_CONCRETE)) {
			String groupUuidString = ItemUtils.getStringFromLore(clicked, "UUID", 1);
			WauzDebugger.log(player, "Group UUID: " + groupUuidString);
			passwordInput(player, groupUuidString, "_");
		}
		
		// Join Open Group
		else if(clicked.getType().equals(Material.LIME_CONCRETE)) {
			String groupUuidString = ItemUtils.getStringFromLore(clicked, "UUID", 1);
			WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(groupUuidString);
			WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
			pg.addPlayer(player);
			pd.setGroupUuidString(groupUuidString);
			open(player);
		}
		
		// Password Input
		else if(clicked.getType().equals(Material.GRAY_CONCRETE)) {
			String passwordString = player.getOpenInventory().getTitle().split(" ")[2].replace("" + ChatColor.DARK_RED, "");
			passwordString += clicked.getItemMeta().getDisplayName().replace("" + ChatColor.RESET, "");
			
			if(ItemUtils.hasLore(clicked)) {
				String groupUuidString = ItemUtils.getStringFromLore(clicked, "UUID", 1);
				if(passwordString.length() == 5) {
					WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(groupUuidString);
					if(passwordString.equals(pg.getGroupPassword())) {
						WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
						pg.addPlayer(player);
						pd.setGroupUuidString(groupUuidString);
						open(player);
					}
					else {
						player.sendMessage(ChatColor.RED + "Wrong Password!");
						player.closeInventory();
						return;
					}
				}
				else
					passwordInput(player, groupUuidString, passwordString);
			}
			
			else {
				WauzDebugger.log(player, "Password: " + passwordString + ", Length: " + passwordString.length());
				if(passwordString.length() == 5) {
					WauzPlayerGroup pg = new WauzPlayerGroup(player);
					WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
					pd.setGroupUuidString(pg.getGroupUuidString());
					pg.setGroupPassword(passwordString);
					WauzPlayerGroupPool.regGroup(pg);
					open(player);
				}
				else
					passwordInput(player, null, passwordString);
			}
		}
		
		// Player Teleport / Promote / Kick
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta sm = (SkullMeta) clicked.getItemMeta();
			if(sm.getOwningPlayer() == null)
				return;
			
			Player target = WauzCore.getOnlinePlayer((sm.getOwningPlayer().getName()));
			if(target == null || player.equals(target) || WauzPlayerDataPool.getPlayer(target) == null)
				return;
			
			String playerGroupUuid = WauzPlayerDataPool.getPlayer(player).getGroupUuidString();
			String targetGroupUuid = WauzPlayerDataPool.getPlayer(target).getGroupUuidString();
			if(!StringUtils.equals(playerGroupUuid, targetGroupUuid))
				return;
			
			String inventoryName = player.getOpenInventory().getTitle();
			
			if(inventoryName.contains("Promote")) {
				WauzPlayerData pd = WauzPlayerDataPool.getPlayer(target);
				WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(pd.getGroupUuidString());
				if(pg != null) {
					pg.setAdminUuidString(target.getUniqueId().toString());
					for(Player member : pg.getPlayers()) {
						member.sendMessage(ChatColor.GREEN + player.getName() + " promoted "
								+ target.getName() + " to the group-leader!");
					}
					open(player);
				}
				else {
					player.closeInventory();
				}
			}
			
			else if(inventoryName.contains("Kick")) {
				WauzPlayerData pd = WauzPlayerDataPool.getPlayer(target);
				WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(pd.getGroupUuidString());
				if(pg != null) {
					pg.removePlayer(target);
					pd.setGroupUuidString(null);
					target.sendMessage(ChatColor.RED + player.getName() + " kicked you out of the group!");
					for(Player member : pg.getPlayers()) {
						member.sendMessage(ChatColor.RED + player.getName() + " kicked "
								+ target.getName() + " out of the group!");
					}
					open(player);
				}
				else {
					player.closeInventory();
				}
			}
			
			else {
				WauzTeleporter.playerTeleportManual(player, target);
			}
		}
		
		// Group Options
		else if(clicked.getType().equals(Material.GOLDEN_HELMET)) {
			playerSelection(player, true);
		}
		
		else if(clicked.getType().equals(Material.IRON_BOOTS)) {
			playerSelection(player, false);
		}
		
		else if(clicked.getType().equals(Material.BARRIER)) {
			WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
			WauzPlayerGroup pg = WauzPlayerGroupPool.getGroup(pd.getGroupUuidString());
			if(pg != null) {
				pg.removePlayer(player);
				pd.setGroupUuidString(null);
				open(player);
			}
			else {
				player.closeInventory();
			}
		}
	}

}
