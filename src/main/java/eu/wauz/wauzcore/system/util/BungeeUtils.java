package eu.wauz.wauzcore.system.util;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import eu.wauz.wauzcore.WauzCore;

/**
 * An util class to communicate with BungeeCord.
 * 
 * @author Wauzmons
 */
public class BungeeUtils {
	
	/**
	 * The channel to send messages to BungeeCord.
	 */
	public static String BUNGEE_CHANNEL = "BungeeCord";
	
	/**
	 * The subchannel to connect players via BungeeCord.
	 */
	public static String BUNGEE_SUBCHANNEL_CONNECT = "Connect";
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Tries to connect the player to the given server.
	 * 
	 * @param player The player to connect.
	 * @param server The server to connect to.
	 */
	public static void connect(Player player, String server) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF(BUNGEE_SUBCHANNEL_CONNECT);
		output.writeUTF(server);
		player.sendPluginMessage(core, BUNGEE_CHANNEL, output.toByteArray());
	}

}
