package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

import net.minecraft.server.v1_14_R1.EntityChicken;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.WorldServer;

/**
 * A chickoon (indestructible chicken) entity based on a normal chicken.
 * 
 * @author Wauzmons
 */
public class NmsEntityChickoon extends EntityChicken {
	
	/**
	 * Creates a chickoon (indestructible chicken) entity.
	 * 
	 * @param location The spawn location of the chickoon.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity create(Location location) {
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		return new NmsEntityChickoon(worldServer, x, y, z).getBukkitEntity();
	}

	/**
	 * Creates a chickoon (indestructible chicken) entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param x The x coordinate of the spawn location.
	 * @param y The y coordinate of the spawn location.
	 * @param z The z coordinate of the spawn location.
	 */
	private NmsEntityChickoon(WorldServer worldServer, double x, double y, double z) {
		super(EntityTypes.CHICKEN, worldServer);
		
		this.locX = x;
		this.locY = y;
		this.locZ = z;
		
		this.collides = false;
		this.persist = false;
		this.canPickUpLoot = false;
		
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setAge(1, true);
		
		worldServer.addEntity(this);
	}
	
}
