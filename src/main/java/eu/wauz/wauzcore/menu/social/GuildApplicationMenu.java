package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the guild menu, that is used for viewing and managing guild applications.
 * 
 * @author Wauzmons
 *
 * @see GuildOverviewMenu
 */
public class GuildApplicationMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "applications";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		GuildApplicationMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows a list of guild applicants, only viewable by guild officers or higher.
	 * Lists up to 27 applicants (3 inventory rows), including last online time and character stats.
	 * The officer can decide which application to accept by left or right clicking them.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see GuildOverviewMenu#validateOfficerAccess(Player, WauzPlayerGuild)
	 * @see WauzPlayerGuild#getApplicantUuidStrings()
	 * @see PlayerConfigurator#getLastPlayed(OfflinePlayer)
	 * @see PlayerConfigurator#getRaceString(OfflinePlayer, int)
	 * @see PlayerConfigurator#getWorldString(OfflinePlayer, int)
	 * @see PlayerConfigurator#getLevelString(OfflinePlayer, int)
	 * @see PlayerConfigurator#getSurvivalScore(OfflinePlayer)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		if(!GuildOverviewMenu.validateOfficerAccess(player, playerGuild)) {
			return;
		}
		
		WauzInventoryHolder holder = new WauzInventoryHolder(new GuildApplicationMenu());
		Inventory menu = Bukkit.createInventory(holder, 27, ChatColor.BLACK + "" + ChatColor.BOLD + "Guild Applications");
		
		List<OfflinePlayer> applicants = playerGuild.getApplicantUuidStrings().stream()
				.map(uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)))
				.filter(offlinePlayer -> offlinePlayer != null)
				.collect(Collectors.toList());
		
		for(int slot = 0; slot < 27 && slot < applicants.size(); slot++) {
			OfflinePlayer applicant = applicants.get(slot);
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta sm = (SkullMeta) skull.getItemMeta();
			sm.setDisplayName(ChatColor.LIGHT_PURPLE + applicant.getName() + " [Applicant]");
			sm.setOwningPlayer(applicant);
			List<String> slores = new ArrayList<String>();
			slores.add(ChatColor.GRAY + "Left Click to Accept, "
					+ "Free Slots: " + (playerGuild.getMaxMemberAmount() - playerGuild.getMemberAmount()));
			slores.add(ChatColor.GRAY + "Right Click to Reject");
			slores.add("");
			slores.add(ChatColor.GRAY + "Last Online: " + (applicant.isOnline()
					? ChatColor.GREEN + "Now"
					: ChatColor.BLUE + PlayerConfigurator.getLastPlayed(player) + " ago"));
			slores.add("");
			slores.add(ChatColor.DARK_PURPLE + "MMORPG Characters: ");
			for(int character = 1; character <= 3; character++) {
				if(PlayerConfigurator.doesCharacterExist(applicant, character)) {
					slores.add(ChatColor.WHITE 
							+ PlayerConfigurator.getRaceString(applicant, character) + ", "
							+ PlayerConfigurator.getWorldString(applicant, character) + ", "
							+ PlayerConfigurator.getLevelString(applicant, character));
				}
				else {
					slores.add(ChatColor.GRAY + "Empty");
				}
			}
			slores.add("");
			slores.add(ChatColor.DARK_PURPLE + "Survival Score: ");
			slores.add(ChatColor.WHITE + Formatters.INT.format(PlayerConfigurator.getSurvivalScore(applicant)));
			sm.setLore(slores);
			skull.setItemMeta(sm);
			menu.setItem(slot, skull);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and accepts or rejects the clicked application.
	 * The application has to be bo�nd to a valid player and the guild must have free slots, to accept them.
	 * A right click rejects the application, while a left click will accept the player into the guild.
	 * The menu is updated after each processed application.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerConfigurator#getGuild(OfflinePlayer)
	 * @see WauzPlayerGuild#removeApplicant(String)
	 * @see WauzPlayerGuild#addPlayer(OfflinePlayer)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
		
		if(clicked == null || guild == null) {
			return;
		}
		
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta sm = (SkullMeta) clicked.getItemMeta();
			OfflinePlayer applicant = sm.getOwningPlayer();
			if(applicant == null) {
				return;
			}
			
			if(event.getClick().toString().contains("RIGHT")) {
				if(applicant.isOnline()) {
					applicant.getPlayer().sendMessage(org.bukkit.ChatColor.RED + "Your application for " + guild.getGuildName() + " was rejected.");
				}
				guild.removeApplicant(applicant.getUniqueId().toString());
			}
			else if(!guild.isFull()) {
				guild.removeApplicant(applicant.getUniqueId().toString());
				guild.addPlayer(applicant);
			}
			open(player);
		}
	}

}
