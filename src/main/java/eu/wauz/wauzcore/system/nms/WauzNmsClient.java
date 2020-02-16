package eu.wauz.wauzcore.system.nms;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.UnicodeUtils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;

/**
 * Collection of general net.minecraft.server specific methods.
 * Used to only have a single place, when in need of changing version numbers.
 * 
 * @author Wauzmons
 */
public class WauzNmsClient {
	
	/**
	 * Sends a packet to instantly respawn the given player.
	 * 
	 * @param player The player that should respawn.
	 */
	public static void nmsRepsawn(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.a(
				new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
	}

	/**
	 * Sends a packet to update the action bar of a player.
	 * 
	 * @param player The player whose action bar should be updated.
	 * @param message The text for that should show in the action bar.
	 */
	public static void nmsActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(
				new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.GAME_INFO));
	}
	
	/**
	 * Sends a packet with a clickable message, that executes a command, to a player.
	 * 
	 * @param player The player that should receive the message.
	 * @param command The command that should be executed on click.
	 * @param message The message that should be shown.
	 * @param border If the message has borders like this: ----------
	 */
	public static void nmsChatCommand(Player player, String command, String message, boolean border) {
		if(border) {
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		}
		
		IChatBaseComponent comp = ChatSerializer
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + UnicodeUtils.ICON_PARAGRAPH + "bClick Here\","
						+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Run Command\"},"
						+ "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + command + "\"}}]}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
        
        if(border) {
        	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
        }
	}
	
	/**
	 * Sends a packet with a clickable message, that opens an url, to a player.
	 * 
	 * @param player The player that should receive the message.
	 * @param url The url that should be opened on click.
	 * @param message The message that should be shown.
	 * @param border If the message has borders like this: ----------
	 */
	public static void nmsChatHyperlink(Player player, String url, String message, boolean border) {
		if(border) {
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		}
		
		IChatBaseComponent comp = ChatSerializer
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + UnicodeUtils.ICON_PARAGRAPH + "bClick Here\","
						+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Open URL\"},"
						+ "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"}}]}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
        
        if(border) {
        	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
        }
	}
	
	/**
	 * Changes the persistance of an entity.
	 * 
	 * @param entity The Bukkit entity.
	 * @param persistent If it should persist.
	 */
	public static void nmsEntityPersistence(org.bukkit.entity.Entity entity, boolean persistent) {
		((CraftEntity) entity).getHandle().persist = persistent;
	}
	
}
