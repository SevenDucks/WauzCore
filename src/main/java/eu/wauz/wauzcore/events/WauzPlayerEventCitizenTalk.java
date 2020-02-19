package eu.wauz.wauzcore.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

/**
 * An event that lets a player talk to a citizen.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventCitizenTalk implements WauzPlayerEvent {
	
	/**
	 * The name of the citizen, as shown in chat.
	 */
	private String displayName;
	
	/**
	 * The lines the citizen should speak.
	 */
	private List<String> messages;
	
	/**
	 * Creates an event to talk to the given citizen.
	 * 
	 * @param displayName How to display the sender of the messages.
	 * @param messages The lines the citizen should speak.
	 */
	public WauzPlayerEventCitizenTalk(String displayName, List<String> messages) {
		this.displayName = displayName;
		this.messages = messages;
	}

	/**
	 * Executes the event for the given player.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see WauzPlayerEventCitizenTalk#printDialog(Player, List)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			printDialog(player, messages, 0);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while interacting with " + displayName + "!");
			player.closeInventory();
			return false;
		}
	}
	
	/**
	 * Prints recursively all messages in chat.
	 * 
	 * @param player The player who should receive the messages.
	 * @param messages The list of all message lines.
	 * @param line The index of the current message line.
	 */
	private void printDialog(Player player, List<String> messages, int line) {
		try {
    		if(player == null || !player.isValid() || messages.isEmpty()) {
    			return;
    		}
    		
    		int nextLine = line + 1;
    		String msg = ChatColor.WHITE + "[" + ChatColor.YELLOW + displayName + ChatColor.WHITE + " (" +
					 ChatColor.AQUA  + "NPC" + ChatColor.WHITE + " " + nextLine + "/" + messages.size() + ")] " +
					 ChatColor.GRAY + messages.get(line);
    		player.sendMessage(msg.replaceAll("player", player.getName()));
    		
			if(messages.size() == nextLine) {
				return;
			}
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
				
				@Override
				public void run() {
					printDialog(player, messages, nextLine);
				}
				
			}, 50);
    	}
    	catch (NullPointerException e) {
    		WauzDebugger.catchException(getClass(), e);
    	}
	}

}
