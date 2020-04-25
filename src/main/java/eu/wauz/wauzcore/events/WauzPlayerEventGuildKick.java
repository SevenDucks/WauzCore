package eu.wauz.wauzcore.events;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.social.GuildOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;

/**
 * An event that lets a player kick one of their guild members.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventGuildKick implements WauzPlayerEvent {
	
	/**
	 * The member that should get kicked.
	 */
	private OfflinePlayer member;
	
	/**
	 * Creates an event to demote the given member.
	 * 
	 * @param member The member that should get kicked.
	 */
	public WauzPlayerEventGuildKick(OfflinePlayer member) {
		this.member = member;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player (not the member that gets kicked) for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzPlayerGuild#kickMember(Player, OfflinePlayer)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild != null) {
				playerGuild.kickMember(player, member);
				GuildOverviewMenu.open(player);
			}
			else {
				player.closeInventory();
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while kicking the Player!");
			player.closeInventory();
			return false;
		}
	}

}
