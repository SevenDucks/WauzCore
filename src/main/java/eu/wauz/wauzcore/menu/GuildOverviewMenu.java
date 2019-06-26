package eu.wauz.wauzcore.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.InstanceManager;
import net.md_5.bungee.api.ChatColor;

public class GuildOverviewMenu implements WauzInventory {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");

	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new GuildOverviewMenu());
		Inventory menu = Bukkit.createInventory(holder, 54, ChatColor.BLACK + "" + ChatColor.BOLD + "Guild Overview");
		WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
		
		if(guild != null) {
			{
				ItemStack gld = HeadUtils.getGuildItem();
				ItemMeta im = gld.getItemMeta();
				im.setDisplayName(ChatColor.GREEN + guild.getGuildName());
				List<String> lores = new ArrayList<String>();
				lores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
						+ Bukkit.getOfflinePlayer(UUID.fromString(guild.getAdminUuidString())).getName());
				lores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
						+ guild.getMemberAmount() + " / " + guild.getMaxMemberAmount());
				lores.add("");
				for(String lore : guild.getWrappedGuildDescription())
					lores.add(ChatColor.GRAY + lore);
				lores.add("");
				lores.add(ChatColor.DARK_PURPLE + "Commands:");
				lores.add(ChatColor.GREEN + "/" + ChatColor.WHITE + "gld [text] " + ChatColor.GRAY + "Send Message in Guild Chat");
				lores.add(ChatColor.YELLOW + "/" + ChatColor.WHITE + "motd [text] " + ChatColor.GRAY + "Set the Guild Message of the Day");
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "UUID: " + guild.getGuildUuidString());
				im.setLore(lores);
				gld.setItemMeta(im);
				menu.setItem(0, gld);
			}
			
			{
				ItemStack banner = guild.getGuildTabard();
				ItemMeta im = banner.getItemMeta();
				im.setDisplayName(ChatColor.YELLOW + "Select (Guild) Tabard");
				List<String> lores = new ArrayList<String>();
				if(((BannerMeta) im).getPatterns().size() > 1)
					lores.add("");
				lores.add(ChatColor.GRAY + "Right Click to Edit (Officers Only)");
				im.setLore(lores);
				banner.setItemMeta(im);
				menu.setItem(1, banner);
			}
			
			{
				ItemStack glhl = HeadUtils.getGuildHallItem();
				ItemMeta im = glhl.getItemMeta();
				im.setDisplayName(ChatColor.YELLOW + "Building: Guildhall");
				List<String> lores = new ArrayList<String>();
				lores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + guild.getUpgradeAdditionalSlots());
				lores.add("");
				lores.add(ChatColor.GRAY + "Increases Maximum Members by 1 per Level.");
				lores.add(ChatColor.GRAY + "Right Click to enter Guildhall");
				lores.add("");
				lores.add(ChatColor.WHITE + "Maximum Members: " + ChatColor.GOLD
						+ (5 + guild.getUpgradeAdditionalSlots())
						+ ChatColor.GRAY + " (Max: 21)");
				im.setLore(lores);
				glhl.setItemMeta(im);
				menu.setItem(2, glhl);
			}
			
			MenuUtils.setComingSoon(menu, "Building: ???", 3);
			MenuUtils.setComingSoon(menu, "Building: ???", 4);
			MenuUtils.setComingSoon(menu, "Building: ???", 5);
			MenuUtils.setComingSoon(menu, "Building: ???", 6);
			
			{
				ItemStack aplc = guild.getGuildTabard();
				ItemMeta aim = aplc.getItemMeta();
				aim.setDisplayName(ChatColor.YELLOW + "View Applications");
				List<String> lores = new ArrayList<String>();
				if(((BannerMeta) aim).getPatterns().size() > 1)
					lores.add("");
				lores.add(ChatColor.GRAY + "Click to view "
					+ ChatColor.LIGHT_PURPLE + guild.getApplicationCount()
					+ ChatColor.GRAY + " Applicants (Officers Only)");
				aim.setLore(lores);
				aplc.setItemMeta(aim);
				menu.setItem(7, aplc);
			}
			
			{
				ItemStack quit = new ItemStack(Material.BARRIER);
				ItemMeta qim = quit.getItemMeta();
				qim.setDisplayName(ChatColor.RED + "Leave Guild");
				quit.setItemMeta(qim);
				menu.setItem(8, quit);
			}
			
			List<OfflinePlayer> members = guild.getMemberUuidStrings().stream()
					.map(uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)))
					.filter(offlinePlayer -> offlinePlayer != null)
					.collect(Collectors.toList());
			
			ItemStack freeSlot = HeadUtils.getConfirmItem();
			ItemMeta fim = freeSlot.getItemMeta();
			fim.setDisplayName(ChatColor.DARK_GREEN + "Free Slot");
			freeSlot.setItemMeta(fim);
			
			ItemStack lockedSlot = HeadUtils.getDeclineItem();
			ItemMeta lim = lockedSlot.getItemMeta();
			lim.setDisplayName(ChatColor.DARK_RED + "Locked Slot");
			lockedSlot.setItemMeta(lim);
			
			int memberNumber = 0;
			for(int slot = 19; slot < 44; slot++) {
				if(StringUtils.equalsAny("" + slot, "26", "27", "35", "36")) {
					continue;
				}
				else if(memberNumber + 1 > guild.getMaxMemberAmount()) {
					menu.setItem(slot, lockedSlot);
					memberNumber++;
					continue;
				}
				else if(memberNumber + 1 > guild.getMemberAmount()) {
					menu.setItem(slot, freeSlot);
					memberNumber++;
					continue;
				}
				
				OfflinePlayer member = members.get(memberNumber);
				boolean isGuildLeader = guild.isGuildAdmin(member);
				boolean isGuildOfficer = guild.isGuildOfficer(member);
				String name = member.getName();
				if(isGuildLeader)
					name = ChatColor.GOLD + name + " [Leader]";
				else if(isGuildOfficer)
					name = ChatColor.YELLOW + name + " [Officer]";
				else
					name = ChatColor.GREEN + name + " [Member]";
				
				ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				sm.setDisplayName(name);
				sm.setOwningPlayer(member);
				List<String> slores = new ArrayList<String>();
				if(!player.getUniqueId().equals(member.getUniqueId()) && !isGuildLeader) {
					if(guild.isGuildAdmin(player)) {
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
					else if(guild.isGuildOfficer(player) && !isGuildOfficer) {
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
				slores.add(ChatColor.WHITE + formatter.format(PlayerConfigurator.getSurvivalScore(member)));
				sm.setLore(slores);
				skull.setItemMeta(sm);
				menu.setItem(slot, skull);
				memberNumber++;
			}
		}
		else {
			List<WauzPlayerGuild> guilds = WauzPlayerGuild.getGuilds();
			int inventorySize = guilds.size() + 2;
			if(inventorySize <= 9) inventorySize = 9;
			else if(inventorySize <= 18) inventorySize = 18;
			else if(inventorySize <= 27) inventorySize = 27;
			else if(inventorySize <= 36) inventorySize = 36;
			else if(inventorySize <= 45) inventorySize = 45;
			else inventorySize = 54;
			
			menu = Bukkit.createInventory(holder, inventorySize, ChatColor.BLACK + "" + ChatColor.BOLD + "Guild List");
			
			{
				ItemStack create = HeadUtils.getGuildItem();
				ItemMeta cim = create.getItemMeta();
				cim.setDisplayName(ChatColor.BLUE + "Create New Guild");
				List<String> lores = new ArrayList<String>();
				lores.add(ChatColor.GRAY + "Type this Command in Chat to create a Guild!");
				lores.add(ChatColor.GRAY + "This will cost 300 Tokens! You have: "
						+ new DecimalFormat("#,###").format(PlayerConfigurator.getTokens(player)) + " Tokens.");
				lores.add("");
				lores.add(ChatColor.BLUE + "/" + ChatColor.WHITE + "guild [guildName]");
				lores.add("");
				lores.add(ChatColor.GRAY + "You can't change the name later, so pick wisely!");
				lores.add(ChatColor.GRAY + "Example: /guild Burning Crusade");
				cim.setLore(lores);
				create.setItemMeta(cim);
				menu.setItem(0, create);
			}
			
			{
				ItemStack banner = new ItemStack(Material.ORANGE_BANNER);
				ItemMeta bim = banner.getItemMeta();
				bim.setDisplayName(ChatColor.YELLOW + "Select (Guild) Tabard");
				banner.setItemMeta(bim);
				menu.setItem(1, banner);
			}
			
			int guildNumber = 2;
			for(WauzPlayerGuild pg : guilds) {
				if(guildNumber >= inventorySize)
					break;
				
				ItemStack gld = pg.getGuildTabard();
				ItemMeta gim = gld.getItemMeta();
				gim.setDisplayName(ChatColor.GREEN + pg.getGuildName());
				List<String> lores = new ArrayList<String>();
				if(((BannerMeta) gim).getPatterns().size() > 1)
					lores.add("");
				lores.add(ChatColor.GRAY + "Click to apply or type this in Chat!");
				lores.add(ChatColor.GREEN + "/" + ChatColor.WHITE + "apply " + pg.getGuildName());
				lores.add("");
				lores.add(ChatColor.DARK_PURPLE + "Leader: " + ChatColor.YELLOW
						+ Bukkit.getOfflinePlayer(UUID.fromString(pg.getAdminUuidString())).getName());
				lores.add(ChatColor.DARK_PURPLE + "Players: " + ChatColor.YELLOW
						+ pg.getMemberAmount() + " / " + pg.getMaxMemberAmount());
				lores.add("");
				for(String lore : pg.getWrappedGuildDescription())
					lores.add(ChatColor.GRAY + lore);
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "UUID: " + pg.getGuildUuidString());
				gim.setLore(lores);
				gld.setItemMeta(gim);
				menu.setItem(guildNumber, gld);
				guildNumber++;
			}
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
		else if(ItemUtils.isSpecificItem(clicked, "Select (Guild) Tabard")) {
			boolean isRightClick = event.getClick().toString().contains("RIGHT");
			if(isRightClick && ItemUtils.hasLore(clicked) && ItemUtils.doesLoreContain(clicked, "Right Click"))
				TabardBuilder.open(player);
			else
				TabardMenu.open(player);
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
			if(member == null || !ItemUtils.hasLore(clicked))
				return;
			
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
