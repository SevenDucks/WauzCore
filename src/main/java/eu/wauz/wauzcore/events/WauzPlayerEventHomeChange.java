package eu.wauz.wauzcore.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerEventHomeChange implements WauzPlayerEvent {
	
	private Entity innkeeper;
	
	private boolean fromCommand = false;
	
	private ItemStack scroll;
	
	public WauzPlayerEventHomeChange(Entity innkeeper, boolean fromCommand) {
		this.innkeeper = innkeeper;
		this.fromCommand = fromCommand;
	}
	
	public WauzPlayerEventHomeChange(Entity innkeeper, ItemStack scroll) {
		this.innkeeper = innkeeper;
		this.scroll = scroll;
	}

	@Override
	public boolean execute(Player player) {
		try {
			player.closeInventory();
			if(!WauzMode.isSurvival(player) && fromCommand) {
				player.sendMessage(ChatColor.RED + "You can't do that in this world!");
				return true;
			}
			WauzRegion region = WauzRegion.getNewRegion(innkeeper.getLocation());
			if(region == null) {
				player.sendMessage(ChatColor.RED + "Setting home failed: Your current region is unknown!");
				return true;
			}
			PlayerConfigurator.setCharacterHearthstone(player, innkeeper.getLocation(), region.getTitle());
			player.sendMessage(ChatColor.GREEN
					+ region.getTitle() + " is now your new home location!" + (WauzMode.isMMORPG(player)
					? " Speak to an Innkeeper in a different place to change your home." : ""));
			
			if(scroll != null)
				scroll.setAmount(scroll.getAmount() - 1);
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while changing your home!");
			player.closeInventory();
			return false;
		}
	}

}
