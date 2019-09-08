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
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class GroupMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group Overview");
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData.isInGroup()) {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
			
			ItemStack groupItemStack = HeadUtils.getGroupItem();
			setGroupItemMeta(groupItemStack, playerGroup, true);
			menu.setItem(0, groupItemStack);
			
			int playerNumber = 1;
			for(Player member : playerGroup.getPlayers()) {
				boolean isGroupLeader = playerGroup.isGroupAdmin(member);
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
			
			if(playerGroup.isGroupAdmin(player)) {
				ItemStack promoteItemStack = new ItemStack(Material.GOLDEN_HELMET);
				ItemMeta promoteItemMeta = promoteItemStack.getItemMeta();
				promoteItemMeta.setDisplayName(ChatColor.RED + "Change Leader");
				promoteItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				promoteItemStack.setItemMeta(promoteItemMeta);
				menu.setItem(6, promoteItemStack);
				
				ItemStack kickItemStack = new ItemStack(Material.IRON_BOOTS);
				ItemMeta kickItemMeta = kickItemStack.getItemMeta();
				kickItemMeta.setDisplayName(ChatColor.RED + "Kick Member");
				kickItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				kickItemStack.setItemMeta(kickItemMeta);
				menu.setItem(7, kickItemStack);
			}
			
			ItemStack leaveItemStack = new ItemStack(Material.BARRIER);
			ItemMeta leaveItemMeta = leaveItemStack.getItemMeta();
			leaveItemMeta.setDisplayName(ChatColor.RED + "Leave Group");
			leaveItemStack.setItemMeta(leaveItemMeta);
			menu.setItem(8, leaveItemStack);
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
			
			ItemStack createItemStack = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
			ItemMeta createItemMeta = createItemStack.getItemMeta();
			createItemMeta.setDisplayName(ChatColor.BLUE + "Create Open Group");
			createItemStack.setItemMeta(createItemMeta);
			menu.setItem(0, createItemStack);
			
			ItemStack createWithPasswordItemStack = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
			ItemMeta createWithPasswordItemMeta = createWithPasswordItemStack.getItemMeta();
			createWithPasswordItemMeta.setDisplayName(ChatColor.YELLOW + "Create Password Group");
			createWithPasswordItemStack.setItemMeta(createWithPasswordItemMeta);
			menu.setItem(1, createWithPasswordItemStack);
			
			String wauzMode = WauzMode.getMode(player.getWorld().getName()).toString();
			
			int groupNumber = 2;
			for(WauzPlayerGroup playerGroup : groups) {
				if(groupNumber >= inventorySize)
					break;
				
				ItemStack groupItemStack = new ItemStack(playerGroup.isFull() || !playerGroup.getWauzMode().equals(wauzMode) ? Material.BLACK_CONCRETE :
						(playerGroup.isPasswordProtected() ? Material.YELLOW_CONCRETE : Material.LIME_CONCRETE));
				setGroupItemMeta(groupItemStack, playerGroup, false);
				menu.setItem(groupNumber, groupItemStack);
				groupNumber++;
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public static void setGroupItemMeta(ItemStack groupItemStack, WauzPlayerGroup playerGroup, boolean insideGroup) {
		ItemMeta groupItemMeta = groupItemStack.getItemMeta();
		groupItemMeta.setDisplayName(ChatColor.BLUE + "Group");
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.DARK_PURPLE + "Mode: " + ChatColor.YELLOW
				+ playerGroup.getWauzMode());
		lores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
				+ Bukkit.getPlayer(UUID.fromString(playerGroup.getAdminUuidString())).getName());
		lores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
				+ playerGroup.getPlayerAmount() + " / 5");
		lores.add("");
		for(String lore : playerGroup.getWrappedGroupDescription())
			lores.add(ChatColor.GRAY + lore);
		lores.add("");
		if(insideGroup) {
			lores.add(playerGroup.isPasswordProtected() ? ChatColor.RED + "Password: "
					+ playerGroup.getGroupPassword() : ChatColor.GREEN + "No Password");
			lores.add("");
			lores.add(ChatColor.DARK_PURPLE + "Commands:");
			lores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "group " + ChatColor.GRAY + "Open the Group Menu");
			lores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "grp [text] " + ChatColor.GRAY + "Send Message in Group Chat");
			lores.add(ChatColor.GOLD + "/" + ChatColor.WHITE + "desc [text] " + ChatColor.GRAY + "Set the Group Description");
		}
		else {
			lores.add(playerGroup.isPasswordProtected() ? ChatColor.RED
					+ "Password Protected" : ChatColor.GREEN + "No Password");
		}
		lores.add("");
		lores.add(ChatColor.DARK_GRAY + "UUID: " + playerGroup.getGroupUuidString());
		groupItemMeta.setLore(lores);
		groupItemStack.setItemMeta(groupItemMeta);
	}
	
	public static void passwordInput(Player player, String groupUuidString, String passwordString) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group Password: "
				+ ChatColor.DARK_RED + passwordString);
		
		int slot = 0;
		while(slot < 9) {
			ItemStack passwordNumberItemStack = new ItemStack(Material.GRAY_CONCRETE);
			ItemMeta passwordNumberItemMeta = passwordNumberItemStack.getItemMeta();
			passwordNumberItemMeta.setDisplayName(ChatColor.RESET + "" + (slot + 1));
			if(groupUuidString != null) {
				List<String> lores = new ArrayList<String>();
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "UUID: " + groupUuidString);
				passwordNumberItemMeta.setLore(lores);
			}
			passwordNumberItemStack.setItemMeta(passwordNumberItemMeta);
			menu.setItem(slot, passwordNumberItemStack);
			slot++;
		}
		
		player.openInventory(menu);
	}
	
	public static void playerSelection(Player player, boolean promote) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group: "
				+ (promote ? "Promote" : "Kick"));
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
		
		int playerNumber = 1;
		for(Player member : playerGroup.getPlayers()) {
			if(playerGroup.isGroupAdmin(member))
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
				WauzPlayerGroup playerGroup = new WauzPlayerGroup(player);
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.setGroupUuidString(playerGroup.getGroupUuidString());
				WauzPlayerGroupPool.regGroup(playerGroup);
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
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(groupUuidString);
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerGroup.addPlayer(player);
			playerData.setGroupUuidString(groupUuidString);
			open(player);
		}
		
		// Password Input
		else if(clicked.getType().equals(Material.GRAY_CONCRETE)) {
			String passwordString = player.getOpenInventory().getTitle().split(" ")[2].replace("" + ChatColor.DARK_RED, "");
			passwordString += clicked.getItemMeta().getDisplayName().replace("" + ChatColor.RESET, "");
			
			if(ItemUtils.hasLore(clicked)) {
				String groupUuidString = ItemUtils.getStringFromLore(clicked, "UUID", 1);
				if(passwordString.length() == 5) {
					WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(groupUuidString);
					if(passwordString.equals(playerGroup.getGroupPassword())) {
						WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
						playerGroup.addPlayer(player);
						playerData.setGroupUuidString(groupUuidString);
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
					WauzPlayerGroup playerGroup = new WauzPlayerGroup(player);
					WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
					playerData.setGroupUuidString(playerGroup.getGroupUuidString());
					playerGroup.setGroupPassword(passwordString);
					WauzPlayerGroupPool.regGroup(playerGroup);
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
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(target);
				WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
				if(playerGroup != null) {
					playerGroup.setAdminUuidString(target.getUniqueId().toString());
					for(Player member : playerGroup.getPlayers()) {
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
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(target);
				WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
				if(playerGroup != null) {
					playerGroup.removePlayer(target);
					playerData.setGroupUuidString(null);
					target.sendMessage(ChatColor.RED + player.getName() + " kicked you out of the group!");
					for(Player member : playerGroup.getPlayers()) {
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
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString());
			if(playerGroup != null) {
				playerGroup.removePlayer(player);
				playerData.setGroupUuidString(null);
				open(player);
			}
			else {
				player.closeInventory();
			}
		}
	}

}
