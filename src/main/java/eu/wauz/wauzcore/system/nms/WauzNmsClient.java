package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_16_R2.WorldBorder;

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
		PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
		((CraftPlayer) player).getHandle().playerConnection.a(packet);
	}
	
	public static void nmsBorder(Player player, Location location, int radius) {
		WorldBorder worldBorder = new WorldBorder();
		worldBorder.setCenter(location.getBlockX(), location.getBlockZ());
		worldBorder.setSize(radius * 2);
		worldBorder.world = ((CraftWorld) location.getWorld()).getHandle();
		PacketPlayOutWorldBorder packet = new PacketPlayOutWorldBorder(worldBorder, EnumWorldBorderAction.INITIALIZE);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
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
