package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import net.minecraft.server.v1_16_R3.EntityChicken;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.WorldServer;

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
		return new NmsEntityChickoon(worldServer, location).getBukkitEntity();
	}

	/**
	 * Creates a chickoon (indestructible chicken) entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param location The spawn location.
	 */
	private NmsEntityChickoon(WorldServer worldServer, Location location) {
		super(EntityTypes.CHICKEN, worldServer);
		
		this.collides = false;
		this.persist = false;
		this.canPickUpLoot = false;
		
		this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setAge(1, true);
		
		worldServer.addEntity(this);
	}
	
}
