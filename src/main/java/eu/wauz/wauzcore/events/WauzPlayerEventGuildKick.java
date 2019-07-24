package eu.wauz.wauzcore.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventGuildKick implements WauzPlayerEvent {
	
	private OfflinePlayer member;
	
	public WauzPlayerEventGuildKick(OfflinePlayer member) {
		this.member = member;
	}

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
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while kicking the Player!");
			player.closeInventory();
			return false;
		}
	}

}