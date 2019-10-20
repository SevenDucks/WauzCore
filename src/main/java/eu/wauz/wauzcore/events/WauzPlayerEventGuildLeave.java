package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

/**
 * An event that lets a player leave their guild.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventGuildLeave implements WauzPlayerEvent {

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzPlayerGuild#removePlayer(org.bukkit.OfflinePlayer)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild != null) {
				PlayerConfigurator.setGuild(player, "none");
				playerGuild.removePlayer(player);
				GuildOverviewMenu.open(player);
			}
			else {
				player.closeInventory();
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while leaving the Guild!");
			player.closeInventory();
			return false;
		}
	}

}
