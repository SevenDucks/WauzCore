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
	 * The name of the citizen to interact with.
	 */
	private String citizenName;
	
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
	 * @param citizenName The name of the citizen to interact with.
	 * @param messages The lines the citizen should speak.
	 */
	public WauzPlayerEventCitizenTalk(String citizenName, String displayName, List<String> messages) {
		this.citizenName = citizenName;
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
			printDialog(player, messages);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while interacting with " + citizenName + "!");
			player.closeInventory();
			return false;
		}
	}
	
	private void printDialog(Player player, List<String> remainingLines) {
		try {
    		if(player == null || !player.isValid() || remainingLines.isEmpty()) {
    			return;
    		}
    		
    		String msg = ChatColor.WHITE + "[" + ChatColor.YELLOW + displayName + ChatColor.WHITE + " (" +
					 ChatColor.AQUA  + "NPC" + ChatColor.WHITE + ")] " +
					 ChatColor.GRAY + remainingLines.get(0);
    		player.sendMessage(msg.replaceAll("player", player.getName()));
    		
    		remainingLines.remove(0);
			if(remainingLines.size() <= 0) {
				player.sendMessage(ChatColor.DARK_GRAY + "[End of Conversation]");
				return;
			}
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
				
				@Override
				public void run() {
					printDialog(player, remainingLines);
				}
				
			}, 30);
    	}
    	catch (NullPointerException e) {
    		WauzDebugger.catchException(getClass(), e);
    	}
	}

}
