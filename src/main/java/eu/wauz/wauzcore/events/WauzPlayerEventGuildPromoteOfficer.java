package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;

/**
 * An event that lets a player promote one of their guild members to an officer.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventGuildPromoteOfficer implements WauzPlayerEvent {
	
	/**
	 * The member that should get promoted.
	 */
	private OfflinePlayer member;
	
	/**
	 * Creates an event to promote the given member to an officer.
	 * 
	 * @param member The member that should get promoted.
	 */
	public WauzPlayerEventGuildPromoteOfficer(OfflinePlayer member) {
		this.member = member;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player (not the member that gets promoted) for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzPlayerGuild#promoteToOfficer(Player, OfflinePlayer)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild != null) {
				playerGuild.promoteToOfficer(player, member);
				GuildOverviewMenu.open(player);
			}
			else {
				player.closeInventory();
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while promoting the Player!");
			player.closeInventory();
			return false;
		}
	}

}
