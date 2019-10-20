package eu.wauz.wauzcore.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

/**
 * An event that lets a player demote one of their guild members.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventGuildDemoteMember implements WauzPlayerEvent {
	
	/**
	 * The member that should get demoted.
	 */
	private OfflinePlayer member;
	
	/**
	 * Creates an event to demote the given member.
	 * 
	 * @param member The member that should get demoted.
	 */
	public WauzPlayerEventGuildDemoteMember(OfflinePlayer member) {
		this.member = member;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player (not the member that gets demoted) for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzPlayerGuild#demoteToMember(Player, OfflinePlayer)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild != null) {
				playerGuild.demoteToMember(player, member);
				GuildOverviewMenu.open(player);
			}
			else {
				player.closeInventory();
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while demoting the Player!");
			player.closeInventory();
			return false;
		}
	}

}
