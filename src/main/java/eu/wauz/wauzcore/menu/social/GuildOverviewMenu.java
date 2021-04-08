package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Arrays;
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

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildDemoteMember;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildKick;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildLeave;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildPromoteLeader;
import eu.wauz.wauzcore.events.WauzPlayerEventGuildPromoteOfficer;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.heads.SkillIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzcore.system.instances.InstanceManager;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for viewing your guild and interacting with guild buildings.
 * 
 * @author Wauzmons
 *
 * @see GuildApplicationMenu
 */
@PublicMenu
public class GuildOverviewMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "guilds";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		GuildOverviewMenu.open(player);
	}
	
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
	 * @see GuildOverviewMenu#getGuildMemberItemStack(Player, WauzPlayerGuild, OfflinePlayer)
	 * @see GuildOverviewMenu#loadGuildBuildings(Inventory, WauzPlayerGuild)
	 * @see GuildOverviewMenu#loadGuildList(Player, WauzInventoryHolder)
	 */
	public static void open(Player player) {
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		if(playerGuild == null) {
			openGuildList(player);
			return;
		}
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Guild Overview";
		Inventory menu = Components.inventory(new GuildOverviewMenu(), menuTitle, 54);
		
		ItemStack guildItemStack = MenuIconHeads.getGuildItem();
		ItemMeta guildItemMeta = guildItemStack.getItemMeta();
		Components.displayName(guildItemMeta, ChatColor.GREEN + playerGuild.getGuildName());
		List<String> guildLores = new ArrayList<String>();
		guildLores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
				+ Bukkit.getOfflinePlayer(UUID.fromString(playerGuild.getAdminUuidString())).getName());
		guildLores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
				+ playerGuild.getMemberAmount() + " / " + playerGuild.getMaxMemberAmount());
		guildLores.add("");
		for(String lore : playerGuild.getWrappedGuildDescription()) {
			guildLores.add(ChatColor.GRAY + lore);
		}
		guildLores.add("");
		guildLores.add(ChatColor.DARK_PURPLE + "Commands:");
		guildLores.add(ChatColor.GREEN + "/" + ChatColor.WHITE + "gld [text] " + ChatColor.GRAY + "Send Message in Guild Chat");
		guildLores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "motd [text] " + ChatColor.GRAY + "Set the Guild Message of the Day");
		guildLores.add("");
		guildLores.add(ChatColor.DARK_GRAY + "UUID: " + playerGuild.getGuildUuidString());
		Components.lore(guildItemMeta, guildLores);
		guildItemStack.setItemMeta(guildItemMeta);
		menu.setItem(0, guildItemStack);
		
		ItemStack bannerItemStack = playerGuild.getGuildTabard();
		ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
		Components.displayName(bannerItemMeta, ChatColor.YELLOW + "Select (Guild) Tabard");
		List<String> bannerLores = new ArrayList<String>();
		if(((BannerMeta) bannerItemMeta).getPatterns().size() > 1)
			bannerLores.add("");
		bannerLores.add(ChatColor.GRAY + "Right Click to Edit (Officers Only)");
		Components.lore(bannerItemMeta, bannerLores);
		bannerItemStack.setItemMeta(bannerItemMeta);
		menu.setItem(1, bannerItemStack);
		
		loadGuildBuildings(menu, playerGuild);
		
		ItemStack applicationsItemStack = playerGuild.getGuildTabard();
		ItemMeta applicationsItemMeta = applicationsItemStack.getItemMeta();
		Components.displayName(applicationsItemMeta, ChatColor.YELLOW + "View Applications");
		List<String> applicationsLores = new ArrayList<String>();
		if(((BannerMeta) applicationsItemMeta).getPatterns().size() > 1)
			applicationsLores.add("");
		applicationsLores.add(ChatColor.GRAY + "Click to view "
			+ ChatColor.LIGHT_PURPLE + playerGuild.getApplicationCount()
			+ ChatColor.GRAY + " Applicants (Officers Only)");
		Components.lore(applicationsItemMeta, applicationsLores);
		applicationsItemStack.setItemMeta(applicationsItemMeta);
		menu.setItem(7, applicationsItemStack);
		
		ItemStack leaveItemStack = new ItemStack(Material.BARRIER);
		ItemMeta leaveItemMeta = leaveItemStack.getItemMeta();
		Components.displayName(leaveItemMeta, ChatColor.RED + "Leave Guild");
		leaveItemStack.setItemMeta(leaveItemMeta);
		menu.setItem(8, leaveItemStack);
		
		List<OfflinePlayer> members = playerGuild.getMemberUuidStrings().stream()
				.map(uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)))
				.filter(offlinePlayer -> offlinePlayer != null)
				.collect(Collectors.toList());
		
		ItemStack freeSlotItemStack = GenericIconHeads.getConfirmItem();
		ItemMeta freeSlotItemMeta = freeSlotItemStack.getItemMeta();
		Components.displayName(freeSlotItemMeta, ChatColor.DARK_GREEN + "Free Slot");
		freeSlotItemStack.setItemMeta(freeSlotItemMeta);
		
		ItemStack lockedSlotItemStack = GenericIconHeads.getDeclineItem();
		ItemMeta lockedSlotItemMeta = lockedSlotItemStack.getItemMeta();
		Components.displayName(lockedSlotItemMeta, ChatColor.DARK_RED + "Locked Slot");
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
			menu.setItem(slot, getGuildMemberItemStack(player, playerGuild, member));
			memberNumber++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Opens the guild list for the given player.
	 * Shows an overview of random guilds and an instruction on how to create your own.
	 * 
	 * @param player The player that should view the inventory.
	 */
	private static void openGuildList(Player player) {
		List<WauzPlayerGuild> playerGuilds = WauzPlayerGuild.getGuilds();
		int size = MenuUtils.roundInventorySize(playerGuilds.size() + 2);
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Guild List";
		Inventory menu = Components.inventory(new GuildOverviewMenu(), menuTitle, size);
		
		ItemStack createItemStack = MenuIconHeads.getGuildItem();
		ItemMeta createItemMeta = createItemStack.getItemMeta();
		Components.displayName(createItemMeta, ChatColor.BLUE + "Create New Guild");
		List<String> createLores = new ArrayList<String>();
		createLores.add(ChatColor.GRAY + "Type this Command in Chat to create a Guild!");
		createLores.add(ChatColor.GRAY + "This will cost 300 Tokens! You have: "
				+ Formatters.INT.format(PlayerCollectionConfigurator.getTokens(player)) + " Tokens.");
		createLores.add("");
		createLores.add(ChatColor.BLUE + "/" + ChatColor.WHITE + "guild [guildName]");
		createLores.add("");
		createLores.add(ChatColor.GRAY + "You can't change the name later, so pick wisely!");
		createLores.add(ChatColor.GRAY + "Example: /guild Burning Crusade");
		Components.lore(createItemMeta, createLores);
		createItemStack.setItemMeta(createItemMeta);
		menu.setItem(0, createItemStack);
		
		ItemStack bannerItemStack = new ItemStack(Material.ORANGE_BANNER);
		ItemMeta bannerItemMeta = bannerItemStack.getItemMeta();
		Components.displayName(bannerItemMeta, ChatColor.YELLOW + "Select (Guild) Tabard");
		bannerItemStack.setItemMeta(bannerItemMeta);
		menu.setItem(1, bannerItemStack);
		
		int guildNumber = 2;
		for(WauzPlayerGuild listedGuild : playerGuilds) {
			if(guildNumber >= size) {
				break;
			}
			ItemStack guildItemStack = listedGuild.getGuildTabard();
			ItemMeta guildItemMeta = guildItemStack.getItemMeta();
			Components.displayName(bannerItemMeta, ChatColor.GREEN + listedGuild.getGuildName());
			List<String> guildLores = new ArrayList<String>();
			if(((BannerMeta) guildItemMeta).getPatterns().size() > 1) {
				guildLores.add("");
			}
			guildLores.add(ChatColor.GRAY + "Click to apply or type this in Chat!");
			guildLores.add(ChatColor.GREEN + "/" + ChatColor.WHITE + "apply " + listedGuild.getGuildName());
			guildLores.add("");
			guildLores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
					+ Bukkit.getOfflinePlayer(UUID.fromString(listedGuild.getAdminUuidString())).getName());
			guildLores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
					+ listedGuild.getMemberAmount() + " / " + listedGuild.getMaxMemberAmount());
			guildLores.add("");
			for(String lore : listedGuild.getWrappedGuildDescription()) {
				guildLores.add(ChatColor.GRAY + lore);
			}
			guildLores.add("");
			guildLores.add(ChatColor.DARK_GRAY + "UUID: " + listedGuild.getGuildUuidString());
			Components.lore(guildItemMeta, guildLores);
			guildItemStack.setItemMeta(guildItemMeta);
			menu.setItem(guildNumber, guildItemStack);
			guildNumber++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Creates an item stack, showing information about a guild member.
	 * 
	 * @param player The player, viewing the guild member.
	 * @param playerGuild The guild, that is being viewed.
	 * @param member The member, that is being viewed.
	 * 
	 * @return The guild member item stack.
	 * 
	 * @see StatisticsFetcher#addCharacterLores(List)
	 */
	private static ItemStack getGuildMemberItemStack(Player player, WauzPlayerGuild playerGuild, OfflinePlayer member) {
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
		
		ItemStack skullItemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullItemMeta = (SkullMeta) skullItemStack.getItemMeta();
		Components.displayName(skullItemMeta, name);
		skullItemMeta.setOwningPlayer(member);
		List<String> skullLores = new ArrayList<String>();
		if(!player.getUniqueId().equals(member.getUniqueId()) && !isGuildLeader) {
			if(playerGuild.isGuildAdmin(player)) {
				if(isGuildOfficer) {
					skullLores.add(ChatColor.GRAY + "Left Click to promote to Leader");
					skullLores.add(ChatColor.GRAY + "Right Click to demote to Member");
				}
				else {
					skullLores.add(ChatColor.GRAY + "Left Click to promote to Officer");
					skullLores.add(ChatColor.GRAY + "Right Click to Kick");
				}
				skullLores.add("");
			}
			else if(playerGuild.isGuildOfficer(player) && !isGuildOfficer) {
				skullLores.add(ChatColor.GRAY + "Right Click to Kick");
				skullLores.add("");
			}
		}
		StatisticsFetcher statistics = new StatisticsFetcher(member);
		statistics.addCharacterLores(skullLores);
		Components.lore(skullItemMeta, skullLores);
		skullItemStack.setItemMeta(skullItemMeta);
		return skullItemStack;
	}

	/**
	 * Fills the guild menu with a list of its buildings.
	 * 
	 * @param menu The guild menu, that should be filled.
	 * @param playerGuild The player guild.
	 */
	private static void loadGuildBuildings(Inventory menu, WauzPlayerGuild playerGuild) {
		ItemStack buildingGuildhallItemStack = SkillIconHeads.getGuildHallItem();
		ItemMeta buildingGuildhallItemMeta = buildingGuildhallItemStack.getItemMeta();
		Components.displayName(buildingGuildhallItemMeta, ChatColor.YELLOW + "Building: Guildhall");
		List<String> buildingGuildhallLores = new ArrayList<String>();
		buildingGuildhallLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + playerGuild.getUpgradeAdditionalSlots());
		buildingGuildhallLores.add("");
		buildingGuildhallLores.add(ChatColor.GRAY + "Increases Maximum Members by 1 per Level.");
		buildingGuildhallLores.add(ChatColor.GRAY + "Right Click to enter Guildhall");
		buildingGuildhallLores.add("");
		buildingGuildhallLores.add(ChatColor.WHITE + "Maximum Members: " + ChatColor.GOLD
				+ (5 + playerGuild.getUpgradeAdditionalSlots())
				+ ChatColor.GRAY + " (Max: 21)");
		Components.lore(buildingGuildhallItemMeta, buildingGuildhallLores);
		buildingGuildhallItemStack.setItemMeta(buildingGuildhallItemMeta);
		menu.setItem(2, buildingGuildhallItemStack);
		
		MenuUtils.setComingSoon(menu, "Building: ???", 3);
		MenuUtils.setComingSoon(menu, "Building: ???", 4);
		MenuUtils.setComingSoon(menu, "Building: ???", 5);
		MenuUtils.setComingSoon(menu, "Building: ???", 6);
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
	 * @see GuildOverviewMenu#tryToChangePlayerRank(InventoryClickEvent)
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
			playerData.getSelections().setWauzPlayerEventName("Leave Guild");
			playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventGuildLeave());
			WauzDialog.open(player);
		}
		else if(clicked.getType().toString().endsWith("_BANNER") && ItemUtils.hasDisplayName(clicked)) {
			WauzPlayerGuild.applyForGuild(player, ChatColor.stripColor(Components.displayName(clicked.getItemMeta())));
			player.closeInventory();
		}
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			tryToChangePlayerRank(event);
		}
	}

	/**
	 * Called when a player head is clicked.
	 * Can open a pro- or demote dialog, based on permissions.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerEventGuildDemoteMember
	 * @see WauzPlayerEventGuildKick
	 * @see WauzPlayerEventGuildPromoteOfficer
	 * @see WauzPlayerEventGuildPromoteLeader
	 */
	private void tryToChangePlayerRank(InventoryClickEvent event) {
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		SkullMeta skullMeta = (SkullMeta) clicked.getItemMeta();
		OfflinePlayer member = skullMeta.getOwningPlayer();
		if(member == null || !ItemUtils.hasLore(clicked)) {
			return;
		}
		if(event.getClick().toString().contains("RIGHT")) {
			if(ItemUtils.doesLoreContain(clicked, "Right Click to demote to Member")) {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.getSelections().setWauzPlayerEventName("Demote");
				playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventGuildDemoteMember(member));
				WauzDialog.open(player, clicked);
			}
			else if(ItemUtils.doesLoreContain(clicked, "Right Click to Kick")) {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.getSelections().setWauzPlayerEventName("Kick");
				playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventGuildKick(member));
				WauzDialog.open(player, clicked);
			}
		}
		else {
			if(ItemUtils.doesLoreContain(clicked, "Left Click to promote to Officer")) {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.getSelections().setWauzPlayerEventName("Promote");
				playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventGuildPromoteOfficer(member));
				WauzDialog.open(player, clicked);
			}
			else if(ItemUtils.doesLoreContain(clicked, "Left Click to promote to Leader")) {
				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
				playerData.getSelections().setWauzPlayerEventName("Promote");
				playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventGuildPromoteLeader(member));
				WauzDialog.open(player, clicked);
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
