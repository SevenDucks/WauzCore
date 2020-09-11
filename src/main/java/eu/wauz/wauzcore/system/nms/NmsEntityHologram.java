package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

import net.minecraft.server.v1_16_R2.ChatMessage;
import net.minecraft.server.v1_16_R2.EntityArmorStand;
import net.minecraft.server.v1_16_R2.WorldServer;

/**
 * A hologram entity based on an armor stand.
 * 
 * @author Wauzmons
 */
public class NmsEntityHologram  extends EntityArmorStand {
	
	/**
	 * Creates a hologram entity.
	 * 
	 * @param location The spawn location of the hologram.
	 * @param display The text of the hologram.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity create(Location location, String display) {
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		
		return new NmsEntityHologram(worldServer, x, y, z, display).getBukkitEntity();
	}
	
	/**
	 * Creates a hologram entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param x The x coordinate of the spawn location.
	 * @param y The y coordinate of the spawn location.
	 * @param z The z coordinate of the spawn location.
	 * @param display The text of the hologram.
	 */
	private NmsEntityHologram(WorldServer worldServer, double x, double y, double z, String display) {
		super(worldServer, x, y, z);
		
		this.collides = false;
		this.persist = false;
		this.canPickUpLoot = false;
		
		this.setInvisible(true);
		this.setInvulnerable(true);
		this.setSmall(false);
		this.setCustomName(new ChatMessage(display));
		this.setCustomNameVisible(true);

		worldServer.addEntity(this);
	}

}
