package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildDemoteMember;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildKick;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildLeave;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildPromoteLeader;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildPromoteOfficer;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the main menu, that is used for viewing your guild and interacting with guild buildings.
 * 
 * @author Wauzmons
 *
 * @see GuildApplicationMenu
 */
public class GuildOverviewMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * If the player isn't already in a guild an overview of random guilds and an instruction to create your own are shown.
	 * If you are in a guild, there will be a display showing the guild's name, leader, member count, description and commands.
	 * There is always the option to select your own tabard, or create a custom one, for your guild, if you are a guild officer.
	 * There is also a list of upgradable buildings, shown for your guild, including the enterable guildhall.
	 * Officers also have the option to view applications to the guild.
	 * A list of guild members, including last online time and character stats, can also be used to pro- or demote members.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerGuild#getGuilds()
	 * @see WauzPlayerGuild#getGuildName()
	 * @see WauzPlayerGuild#getAdminUuidString()
	 * @see WauzPlayerGuild#getMemberUuidStrings()
	 * @see WauzPlayerGuild#getMemberAmount()
	 * @see WauzPlayerGuild#getApplicationCount()
	 * @see WauzPlayerGuild#getWrappedGuildDescription()
	 * @see WauzPlayerGuild#getGuildTabard()
	 * 
	 * @see PlayerConfigurator#getLastPlayed(OfflinePlayer)
	 * @see PlayerConfigurator#getRaceString(OfflinePlayer, int)
	 * @see PlayerConfigurator#getWorldString(OfflinePlayer, int)
	 * @see PlayerConfigurator#getLevelString(OfflinePlayer, int)
	 * @see PlayerConfigurator#getSurvivalScore(OfflinePlayer)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GuildOverviewMenu());
		Inventory menu = Bukkit.createInventory(holder, 54, ChatColor.BLACK + "" + ChatColor.BOLD + "Guild Overview");
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		
		if(playerGuild != null) {
			ItemStack guildItemStack = HeadUtils.getGuildItem();
			ItemMeta guildItemMeta = guildItemStack.getItemMeta();
			guildItemMeta.setDisplayName(ChatColor.GREEN + playerGuild.getGuildName());
			List<String> guildLores = new ArrayList<String>();
			guildLores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
					+ Bukkit.getOfflinePlayer(UUID.fromString(playerGuild.getAdminUuidString())).getName());
			guildLores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
					+ playerGuild.getMemberAmount() + " / " + playerGuild.getMaxMemberAmount());
			guildLores.add("");
			for(String lore : playerGuild.getWrappedGuildDescription())
				guildLores.add(ChatColor.GRAY + lore);
			guildLores.add("");
			guildLores.add(ChatColor.DARK_PURPLE + "Commands:");
			guildLores.add(ChatColor.GREEN + "/" + ChatColor.WHITE + "gld [text] " + ChatColor.GRAY + "Send Message in Guild Chat");
			guildLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "motd [text] " + ChatColor.GRAY + "Set the Guild Message of the Day");
			guildLores.add("");
			guildLores.add(ChatColor.DARK_GRAY + "UUID: " + playerGuild.getGuildUuidString());
			guildItemMeta.setLore(guildLores);
			guildItemStack.setItemMeta(guildItemMeta);
			menu.setItem(0, guildItemStack);
			
			ItemStack bannerItemStack = playerGuild.getGuildTabard();
			ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
			bannerItemMeta.setDisplayName(ChatColor.YELLOW + "Select (Guild) Tabard");
			List<String> bannerLores = new ArrayList<String>();
			if(((BannerMeta) bannerItemMeta).getPatterns().size() > 1)
				bannerLores.add("");
			bannerLores.add(ChatColor.GRAY + "Right Click to Edit (Officers Only)");
			bannerItemMeta.setLore(bannerLores);
			bannerItemStack.setItemMeta(bannerItemMeta);
			menu.setItem(1, bannerItemStack);
			
			ItemStack buildingGuildhallItemStack = HeadUtils.getGuildHallItem();
			ItemMeta buildingGuildhallItemMeta = buildingGuildhallItemStack.getItemMeta();
			buildingGuildhallItemMeta.setDisplayName(ChatColor.YELLOW + "Building: Guildhall");
			List<String> buildingGuildhallLores = new ArrayList<String>();
			buildingGuildhallLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + playerGuild.getUpgradeAdditionalSlots());
			buildingGuildhallLores.add("");
			buildingGuildhallLores.add(ChatColor.GRAY + "Increases Maximum Members by 1 per Level.");
			buildingGuildhallLores.add(ChatColor.GRAY + "Right Click to enter Guildhall");
			buildingGuildhallLores.add("");
			buildingGuildhallLores.add(ChatColor.WHITE + "Maximum Members: " + ChatColor.GOLD
					+ (5 + playerGuild.getUpgradeAdditionalSlots())
					+ ChatColor.GRAY + " (Max: 21)");
			buildingGuildhallItemMeta.setLore(buildingGuildhallLores);
			buildingGuildhallItemStack.setItemMeta(buildingGuildhallItemMeta);
			menu.setItem(2, buildingGuildhallItemStack);
			
			MenuUtils.setComingSoon(menu, "Building: ???", 3);
			MenuUtils.setComingSoon(menu, "Building: ???", 4);
			MenuUtils.setComingSoon(menu, "Building: ???", 5);
			MenuUtils.setComingSoon(menu, "Building: ???", 6);
			
			ItemStack applicationsItemStack = playerGuild.getGuildTabard();
			ItemMeta applicationsItemMeta = applicationsItemStack.getItemMeta();
			applicationsItemMeta.setDisplayName(ChatColor.YELLOW + "View Applications");
			List<String> applicationsLores = new ArrayList<String>();
			if(((BannerMeta) applicationsItemMeta).getPatterns().size() > 1)
				applicationsLores.add("");
			applicationsLores.add(ChatColor.GRAY + "Click to view "
				+ ChatColor.LIGHT_PURPLE + playerGuild.getApplicationCount()
				+ ChatColor.GRAY + " Applicants (Officers Only)");
			applicationsItemMeta.setLore(applicationsLores);
			applicationsItemStack.setItemMeta(applicationsItemMeta);
			menu.setItem(7, applicationsItemStack);
			
			ItemStack leaveItemStack = new ItemStack(Material.BARRIER);
			ItemMeta leaveItemMeta = leaveItemStack.getItemMeta();
			leaveItemMeta.setDisplayName(ChatColor.RED + "Leave Guild");
			leaveItemStack.setItemMeta(leaveItemMeta);
			menu.setItem(8, leaveItemStack);
			
			List<OfflinePlayer> members = playerGuild.getMemberUuidStrings().stream()
					.map(uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)))
					.filter(offlinePlayer -> offlinePlayer != null)
					.collect(Collectors.toList());
			
			ItemStack freeSlotItemStack = HeadUtils.getConfirmItem();
			ItemMeta freeSlotItemMeta = freeSlotItemStack.getItemMeta();
			freeSlotItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Free Slot");
			freeSlotItemStack.setItemMeta(freeSlotItemMeta);
			
			ItemStack lockedSlotItemStack = HeadUtils.getDeclineItem();
			ItemMeta lockedSlotItemMeta = lockedSlotItemStack.getItemMeta();
			lockedSlotItemMeta.setDisplayName(ChatColor.DARK_RED + "Locked Slot");
			lockedSlotItemStack.setItemMeta(lockedSlotItemMeta);
			
			int memberNumber = 0;
			for(int slot = 19; slot < 44; slot++) {
				if(StringUtils.equalsAny("" + slot, "26", "27", "35", "36")) {
					continue;
				}
				else if(memberNumber + 1 > playerGuild.getMaxMemberAmount()) {
					menu.setItem(slot, lockedSlotItemStack);
					memberNumber++;
					continue;
				}
				else if(memberNumber + 1 > playerGuild.getMemberAmount()) {
					menu.setItem(slot, freeSlotItemStack);
					memberNumber++;
					continue;
				}
				
				OfflinePlayer member = members.get(memberNumber);
				boolean isGuildLeader = playerGuild.isGuildAdmin(member);
				boolean isGuildOfficer = playerGuild.isGuildOfficer(member);
				String name = member.getName();
				if(isGuildLeader) {
					name = ChatColor.GOLD + name + " [Leader]";
				}
				else if(isGuildOfficer) {
					name = ChatColor.YELLOW + name + " [Officer]";
				}
				else {
					name = ChatColor.GREEN + name + " [Member]";
				}
				
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				sm.setDisplayName(name);
				sm.setOwningPlayer(member);
				List<String> slores = new ArrayList<String>();
				if(!player.getUniqueId().equals(member.getUniqueId()) && !isGuildLeader) {
					if(playerGuild.isGuildAdmin(player)) {
						if(isGuildOfficer) {
							slores.add(ChatColor.GRAY + "Left Click to promote to Leader");
							slores.add(ChatColor.GRAY + "Right Click to demote to Member");
						}
						else {
							slores.add(ChatColor.GRAY + "Left Click to promote to Officer");
							slores.add(ChatColor.GRAY + "Right Click to Kick");
						}
						slores.add("");
					}
					else if(playerGuild.isGuildOfficer(player) && !isGuildOfficer) {
						slores.add(ChatColor.GRAY + "Right Click to Kick");
						slores.add("");
					}
				}
				slores.add(ChatColor.GRAY + "Last Online: " + (member.isOnline()
						? ChatColor.GREEN + "Now"
						: ChatColor.BLUE + PlayerConfigurator.getLastPlayed(member) + " ago"));
				slores.add("");
				slores.add(ChatColor.DARK_PURPLE + "MMORPG Characters: ");
				for(int character = 1; character <= 3; character++) {
					if(PlayerConfigurator.doesCharacterExist(member, character)) {
						slores.add(ChatColor.WHITE 
								+ PlayerConfigurator.getRaceString(member, character) + ", "
								+ PlayerConfigurator.getWorldString(member, character) + ", "
								+ PlayerConfigurator.getLevelString(member, character));
					}
					else {
						slores.add(ChatColor.GRAY + "Empty");
					}
				}
				slores.add("");
				slores.add(ChatColor.DARK_PURPLE + "Survival Score: ");
				slores.add(ChatColor.WHITE + Formatters.INT.format(PlayerConfigurator.getSurvivalScore(member)));
				sm.setLore(slores);
				skull.setItemMeta(sm);
				menu.setItem(slot, skull);
				memberNumber++;
			}
		}
		else {
			List<WauzPlayerGuild> playerGuilds = WauzPlayerGuild.getGuilds();
			int inventorySize = playerGuilds.size() + 2;
			if(inventorySize <= 9) inventorySize = 9;
			else if(inventorySize <= 18) inventorySize = 18;
			else if(inventorySize <= 27) inventorySize = 27;
			else if(inventorySize <= 36) inventorySize = 36;
			else if(inventorySize <= 45) inventorySize = 45;
			else inventorySize = 54;
			
			menu = Bukkit.createInventory(holder, inventorySize, ChatColor.BLACK + "" + ChatColor.BOLD + "Guild List");
			
			ItemStack createItemStack = HeadUtils.getGuildItem();
			ItemMeta createItemMeta = createItemStack.getItemMeta();
			createItemMeta.setDisplayName(ChatColor.BLUE + "Create New Guild");
			List<String> createLores = new ArrayList<String>();
			createLores.add(ChatColor.GRAY + "Type this Command in Chat to create a Guild!");
			createLores.add(ChatColor.GRAY + "This will cost 300 Tokens! You have: "
					+ Formatters.INT.format(PlayerConfigurator.getTokens(player)) + " Tokens.");
			createLores.add("");
			createLores.add(ChatColor.BLUE + "/" + ChatColor.WHITE + "guild [guildName]");
			createLores.add("");
			createLores.add(ChatColor.GRAY + "You can't change the name later, so pick wisely!");
			createLores.add(ChatColor.GRAY + "Example: /guild Burning Crusade");
			createItemMeta.setLore(createLores);
			createItemStack.setItemMeta(createItemMeta);
			menu.setItem(0, createItemStack);
			
			ItemStack bannerItemStack = new ItemStack(Material.ORANGE_BANNER);
			ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
			bannerItemMeta.setDisplayName(ChatColor.YELLOW + "Select (Guild) Tabard");
			bannerItemStack.setItemMeta(bannerItemMeta);
			menu.setItem(1, bannerItemStack);
			
			int guildNumber = 2;
			for(WauzPlayerGuild listedGuild : playerGuilds) {
				if(guildNumber >= inventorySize) {
					break;
				}
				
				ItemStack guildItemStack = listedGuild.getGuildTabard();
				ItemMeta guildItemMeta = guildItemStack.getItemMeta();
				guildItemMeta.setDisplayName(ChatColor.GREEN + listedGuild.getGuildName());
				List<String> guildLores = new ArrayList<String>();
				if(((BannerMeta) guildItemMeta).getPatterns().size() > 1)
					guildLores.add("");
				guildLores.add(ChatColor.GRAY + "Click to apply or type this in Chat!");
				guildLores.add(ChatColor.GREEN + "/" + ChatColor.WHITE + "apply " + listedGuild.getGuildName());
				guildLores.add("");
				guildLores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
						+ Bukkit.getOfflinePlayer(UUID.fromString(listedGuild.getAdminUuidString())).getName());
				guildLores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
						+ listedGuild.getMemberAmount() + " / " + listedGuild.getMaxMemberAmount());
				guildLores.add("");
				for(String lore : listedGuild.getWrappedGuildDescription())
					guildLores.add(ChatColor.GRAY + lore);
				guildLores.add("");
				guildLores.add(ChatColor.DARK_GRAY + "UUID: " + listedGuild.getGuildUuidString());
				guildItemMeta.setLore(guildLores);
				guildItemStack.setItemMeta(guildItemMeta);
				menu.setItem(guildNumber, guildItemStack);
				guildNumber++;
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding guild interaction.
	 * If the tabard is left clicked, the selection will be shown, or it can be edited if it was a right click.
	 * A click on the application item opens the list of guild applications for officers.
	 * A right click on the guild hall teleports the player inside.
	 * A click on the barrier lets the player leave the guild.
	 * A click on a guild banner in the guild list, sents a guild application to them.
	 * Clicking on player heads in the member list can open a pro- or demote dialog, based on permissions.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see TabardMenu#open(Player)
	 * @see TabardBuilder#open(Player)
	 * @see GuildApplicationMenu#open(Player)
	 * @see InstanceManager#enterGuild(Player)
	 * @see WauzPlayerEventGuildLeave
	 * @see WauzPlayerGuild#applyForGuild(Player, String)
	 * @see WauzPlayerEventGuildDemoteMember
	 * @see WauzPlayerEventGuildKick
	 * @see WauzPlayerEventGuildPromoteOfficer
	 * @see WauzPlayerEventGuildPromoteLeader
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(ItemUtils.isSpecificItem(clicked, "Select (Guild) Tabard")) {
			boolean isRightClick = event.getClick().toString().contains("RIGHT");
			if(isRightClick && ItemUtils.hasLore(clicked) && ItemUtils.doesLoreContain(clicked, "Right Click")) {
				TabardBuilder.open(player);
			}
			else {
				TabardMenu.open(player);
			}
		}
		else if(ItemUtils.isSpecificItem(clicked, "View Applications")) {
			GuildApplicationMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Building: Guildhall")) {
			if(event.getClick().toString().contains("RIGHT")) {
				InstanceManager.enterGuild(player);
			}
		}
		else if(clicked.getType().equals(Material.BARRIER)) {
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.setWauzPlayerEventName("Leave Guild");
			playerData.setWauzPlayerEvent(new WauzPlayerEventGuildLeave());
			WauzDialog.open(player);
		}
		else if(clicked.getType().toString().endsWith("_BANNER") && ItemUtils.hasDisplayName(clicked)) {
			WauzPlayerGuild.applyForGuild(player, ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
			player.closeInventory();
		}
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta skullMeta = (SkullMeta) clicked.getItemMeta();
			OfflinePlayer member = skullMeta.getOwningPlayer();
			if(member == null || !ItemUtils.hasLore(clicked)) {
				return;
			}
			if(event.getClick().toString().contains("RIGHT")) {
				if(ItemUtils.doesLoreContain(clicked, "Right Click to demote to Member")) {
					WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
					playerData.setWauzPlayerEventName("Demote");
					playerData.setWauzPlayerEvent(new WauzPlayerEventGuildDemoteMember(member));
					WauzDialog.open(player, clicked);
				}
				else if(ItemUtils.doesLoreContain(clicked, "Right Click to Kick")) {
					WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
					playerData.setWauzPlayerEventName("Kick");
					playerData.setWauzPlayerEvent(new WauzPlayerEventGuildKick(member));
					WauzDialog.open(player, clicked);
				}
			}
			else {
				if(ItemUtils.doesLoreContain(clicked, "Left Click to promote to Officer")) {
					WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
					playerData.setWauzPlayerEventName("Promote");
					playerData.setWauzPlayerEvent(new WauzPlayerEventGuildPromoteOfficer(member));
					WauzDialog.open(player, clicked);
				}
				else if(ItemUtils.doesLoreContain(clicked, "Left Click to promote to Leader")) {
					WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
					playerData.setWauzPlayerEventName("Promote");
					playerData.setWauzPlayerEvent(new WauzPlayerEventGuildPromoteLeader(member));
					WauzDialog.open(player, clicked);
				}
			}
		}
	}
	
	/**
	 * Checks if the given player is a guild officer or higher.
	 * 
	 * @param player The player to check.
	 * @param playerGuild The guild of the player.
	 * 
	 * @return If the player is a guild officer.
	 */
	public static boolean validateOfficerAccess(Player player, WauzPlayerGuild playerGuild) {
		if(playerGuild == null) {
			player.sendMessage(ChatColor.RED + "You are not in a guild!");
			player.closeInventory();
			return false;
		}
		if(!playerGuild.isGuildOfficer(player)) {
			player.sendMessage(ChatColor.RED + "You are no guild-officer!");
			player.closeInventory();
			return false;
		}
		return true;
	}

}
