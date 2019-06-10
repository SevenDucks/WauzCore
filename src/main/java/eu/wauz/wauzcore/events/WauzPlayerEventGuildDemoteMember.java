package eu.wauz.wauzcore.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventGuildDemoteMember implements WauzPlayerEvent {
	
	private OfflinePlayer member;
	
	public WauzPlayerEventGuildDemoteMember(OfflinePlayer member) {
		this.member = member;
	}

	@Override
	public boolean execute(Player player) {
		try {
			WauzPlayerGuild pg = PlayerConfigurator.getGuild(player);
			if(pg != null) {
				pg.demoteToMember(player, member);
				GuildOverviewMenu.open(player);
			}
			else {
				player.closeInventory();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while demoting the Player!");
			player.closeInventory();
			return false;
		}
	}

}
