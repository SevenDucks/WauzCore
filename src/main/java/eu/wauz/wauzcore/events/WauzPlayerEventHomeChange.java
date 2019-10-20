package eu.wauz.wauzcore.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * An event for changing the home location of a player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventHomeChange implements WauzPlayerEvent {
	
	/**
	 * The "innkeeper" at the new home location.
	 */
	private Entity innkeeper;
	
	/**
	 * If the event was triggered by a command.
	 */
	private boolean fromCommand = false;
	
	/**
	 * The scroll that triggered the event.
	 */
	private ItemStack scroll;
	
	/**
	 * Creates an event to set the new home location of a player,
	 * to the position of an innkeeper.
	 * 
	 * @param innkeeper The "innkeeper" at the new home location.
	 * @param fromCommand If the event was triggered by a command.
	 */
	public WauzPlayerEventHomeChange(Entity innkeeper, boolean fromCommand) {
		this.innkeeper = innkeeper;
		this.fromCommand = fromCommand;
	}
	
	/**
	 * Creates an event to set the new home location of a player,
	 * to the position of an innkeeper.
	 * 
	 * @param innkeeper The "innkeeper" (in this case likely the player himself) at the new home location.
	 * @param scroll The scroll that triggered the event.
	 */
	public WauzPlayerEventHomeChange(Entity innkeeper, ItemStack scroll) {
		this.innkeeper = innkeeper;
		this.scroll = scroll;
	}
	
	/**
	 * Executes the event for the given player.
	 * If the player is not in Survival and executes this event per command it will fail.
	 * It will also fail if the region of the innkeeper is unknown.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PlayerConfigurator#setCharacterHearthstone(Player, org.bukkit.Location, String)
	 */
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
