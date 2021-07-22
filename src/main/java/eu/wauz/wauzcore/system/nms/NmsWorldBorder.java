package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;

/**
 * Per player world border using net.minecraft.server classes.
 * 
 * @author Wauzmons
 */
public class NmsWorldBorder {
	
	/**
	 * Sends a packet to create a world border for the given player.
	 * 
	 * @param player The player to create the border for.
	 * @param location The location of the border.
	 * @param radius The radius of the border.
	 */
	public static void init(Player player, Location location, int radius) {
		WorldBorder worldBorder = new WorldBorder();
		worldBorder.world = ((CraftWorld) location.getWorld()).getHandle();
		worldBorder.setCenter(location.getBlockX(), location.getBlockZ());
		worldBorder.setSize(radius * 2);
		ClientboundInitializeBorderPacket packet = new ClientboundInitializeBorderPacket(worldBorder);
		((CraftPlayer) player).getHandle().connection.send(packet);
	}
	
}
