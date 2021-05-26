package eu.wauz.wauzcore.system.util;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import eu.wauz.wauzcore.WauzCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

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
	 * The subchannel to send a raw message to all servers via BungeeCord.
	 */
	public static String BUNGEE_SUBCHANNEL_MESSAGERAW = "MessageRaw";
	
	/**
	 * The recipient to send messages to all servers via BungeeCord.
	 */
	public static String BUNGEE_RECIPIENT_ALL = "ALL";
	
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
	
	/**
	 * Tries to broadcast a message across all servers.
	 * 
	 * @param player The player to send the message.
	 * @param message The message to send.
	 */
	public static void broadcast(Player player, Component message) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeUTF(BUNGEE_SUBCHANNEL_MESSAGERAW);
		output.writeUTF(BUNGEE_RECIPIENT_ALL);
		output.writeUTF(GsonComponentSerializer.gson().serialize(message));
		player.sendPluginMessage(core, BUNGEE_CHANNEL, output.toByteArray());
	}

}
