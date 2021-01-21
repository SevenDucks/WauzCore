package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for teaming up with other players.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerGroup
 */
@PublicMenu
public class GroupMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "groups";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG, WauzMode.SURVIVAL);
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		GroupMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * If the player is not in a group, a list of groups, aswell as options to create their own, will be shown.
	 * The color of a group changes depending on status: full (black), protected (yellow), open (lime).
	 * If the player is in a group, the current group info, a list of members to teleport to, aswell as an option to leave are shown.
	 * As a group leader there are additional options to kick a member or promote them to the new leader.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerGroupPool#getGroups()
	 * @see WauzPlayerGroup#getPlayers()
	 * @see GroupMenu#setGroupItemMeta(ItemStack, WauzPlayerGroup, boolean)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group Overview");
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData.getSelections().isInGroup()) {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getSelections().getGroupUuidString());
			
			ItemStack groupItemStack = MenuIconHeads.getGroupItem();
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
			int inventorySize = MenuUtils.roundInventorySize(groups.size() + 2);
			
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
				if(groupNumber >= inventorySize) {
					break;
				}
				
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
	
	/**
	 * Adds information about a group to an item stack.
	 * The lore will contain the mode, leader, player amount, description and if it is password protected.
	 * If the viewer of the item is inside the group, available commands and the encoded password are shown additionally.
	 * 
	 * @param groupItemStack The item stack that will receive the group infos.
	 * @param playerGroup The group to get the infos from.
	 * @param insideGroup If the viewer of the item is inside the group.
	 * 
	 * @see WauzPlayerGroup#getWauzMode()
	 * @see WauzPlayerGroup#getAdminUuidString()
	 * @see WauzPlayerGroup#getPlayerAmount()
	 * @see WauzPlayerGroup#getWrappedGroupDescription()
	 * @see WauzPlayerGroup#isPasswordProtected()
	 */
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
		for(String lore : playerGroup.getWrappedGroupDescription()) {
			lores.add(ChatColor.GRAY + lore);
		}
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
	
	/**
	 * Opens an menu to input a password for a group creation or join.
	 * Called again for each character to add to the password.
	 * 
	 * @param player The player that is inputting a password character.
	 * @param groupUuidString The group that password is for.
	 * @param passwordString The incomplete password.
	 */
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
	
	/**
	 * Opens a view, showing all members of the player's group.
	 * Used to promote or kick members.
	 * 
	 * @param player The player that is promoting or kicking a member.
	 * @param promote If it is a promotion, else it is a kick.
	 * 
	 * @see WauzPlayerGroup#getPlayers()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void playerSelection(Player player, boolean promote) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GroupMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Group: "
				+ (promote ? "Promote" : "Kick"));
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getSelections().getGroupUuidString());
		
		int playerNumber = 1;
		for(Player member : playerGroup.getPlayers()) {
			if(playerGroup.isGroupAdmin(member)) {
				continue;
			}
			
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
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding group action, based on concrete menu item color.
	 * On light blue, a new group is created, optionally with password, if the item name doesn't contain "Open".
	 * On yellow, a group is joined, after successful password input.
	 * On lime, a group is joined directly.
	 * On gray, a password character is added or validated, if 5 characters are already reached.
	 * On a player head or option interaction, the corresponding handler is called.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerGroup
	 * @see WauzPlayerData#setGroupUuidString(String)
	 * @see WauzPlayerGroupPool#regGroup(WauzPlayerGroup)
	 * @see GroupMenu#passwordInput(Player, String, String)
	 * @see GroupMenu#handlePlayerInteractions(Player, ItemStack)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		
		// Create Group
		else if(clicked.getType().equals(Material.LIGHT_BLUE_CONCRETE)) {
			if(clicked.getItemMeta().getDisplayName().contains("Open")) {
				WauzPlayerGroup playerGroup = new WauzPlayerGroup(player);
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.getSelections().setGroupUuidString(playerGroup.getGroupUuidString());
				WauzPlayerGroupPool.regGroup(playerGroup);
				open(player);
			}
			else {
				passwordInput(player, null, "_");
			}
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
			playerData.getSelections().setGroupUuidString(groupUuidString);
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
						playerData.getSelections().setGroupUuidString(groupUuidString);
						open(player);
					}
					else {
						player.sendMessage(ChatColor.RED + "Wrong Password!");
						player.closeInventory();
						return;
					}
				}
				else {
					passwordInput(player, groupUuidString, passwordString);
				}
			}
			else {
				WauzDebugger.log(player, "Password: " + passwordString + ", Length: " + passwordString.length());
				if(passwordString.length() == 5) {
					WauzPlayerGroup playerGroup = new WauzPlayerGroup(player);
					WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
					playerData.getSelections().setGroupUuidString(playerGroup.getGroupUuidString());
					playerGroup.setGroupPassword(passwordString);
					WauzPlayerGroupPool.regGroup(playerGroup);
					open(player);
				}
				else {
					passwordInput(player, null, passwordString);
				}
			}
		}
		else {
			handlePlayerInteractions(player, clicked);
		}
	}
	
	/**
	 * Handles a player head or option interaction.
	 * On a player head, the player is, depending on context, promoted, kicked or teleported to.
	 * On a golden helmet, a player selection is shown, to selected a player to promote to the new leader.
	 * On an iron boot, a player selection is shown, to select a player to kick.
	 * On a barrier, the player will leave the group.
	 * 
	 * @param player The player who started the interaction.
	 * @param clicked The option to interact with.
	 * 
	 * @see GroupMenu#playerSelection(Player, boolean)
	 * @see GroupMenu#promotePlayer(Player, Player)
	 * @see GroupMenu#kickPlayer(Player, Player)
	 * @see WauzTeleporter#playerTeleportManual(Player, Player)
	 */
	public void handlePlayerInteractions(Player player, ItemStack clicked) {
		if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta sm = (SkullMeta) clicked.getItemMeta();
			if(sm.getOwningPlayer() == null) {
				return;
			}
			Player target = WauzCore.getOnlinePlayer((sm.getOwningPlayer().getName()));
			if(target == null || player.equals(target) || WauzPlayerDataPool.getPlayer(target) == null) {
				return;
			}
			String playerGroupUuid = WauzPlayerDataPool.getPlayer(player).getSelections().getGroupUuidString();
			String targetGroupUuid = WauzPlayerDataPool.getPlayer(target).getSelections().getGroupUuidString();
			if(!StringUtils.equals(playerGroupUuid, targetGroupUuid)) {
				return;
			}
			
			String inventoryName = player.getOpenInventory().getTitle();
			if(inventoryName.contains("Promote")) {
				promotePlayer(player, target);
			}
			else if(inventoryName.contains("Kick")) {
				kickPlayer(player, target);
			}
			else {
				WauzTeleporter.playerTeleportManual(player, target);
			}
		}
		else if(clicked.getType().equals(Material.GOLDEN_HELMET)) {
			playerSelection(player, true);
		}
		else if(clicked.getType().equals(Material.IRON_BOOTS)) {
			playerSelection(player, false);
		}
		else if(clicked.getType().equals(Material.BARRIER)) {
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getSelections().getGroupUuidString());
			if(playerGroup != null) {
				playerGroup.removePlayer(player);
				playerData.getSelections().setGroupUuidString(null);
				open(player);
			}
			else {
				player.closeInventory();
			}
		}
	}

	/**
	 * Promotes a player to the new group leader.
	 * 
	 * @param player The player performing the action.
	 * @param target The player to get promoted.
	 */
	private void promotePlayer(Player player, Player target) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(target);
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getSelections().getGroupUuidString());
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
	
	/**
	 * Kicks a player out of the group.
	 * 
	 * @param player The player performing the action.
	 * @param target The player to get kicked.
	 */
	private void kickPlayer(Player player, Player target) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(target);
		WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(playerData.getSelections().getGroupUuidString());
		if(playerGroup != null) {
			playerGroup.removePlayer(target);
			playerData.getSelections().setGroupUuidString(null);
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

}
