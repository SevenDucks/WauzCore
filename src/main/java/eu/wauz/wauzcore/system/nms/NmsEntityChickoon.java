package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;

/**
 * A chickoon (indestructible chicken) entity based on a normal chicken.
 * 
 * @author Wauzmons
 */
public class NmsEntityChickoon extends Chicken {
	
	/**
	 * Creates a chickoon (indestructible chicken) entity.
	 * 
	 * @param location The spawn location of the chickoon.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity create(Location location) {
		ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
		return new NmsEntityChickoon(worldServer, location).getBukkitEntity();
	}

	/**
	 * Creates a chickoon (indestructible chicken) entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param location The spawn location.
	 */
	private NmsEntityChickoon(ServerLevel worldServer, Location location) {
		super(EntityType.CHICKEN, worldServer);
		
		this.collides = false;
		this.persist = false;
		this.age = 1;
		this.ageLocked = true;
		
		this.teleportTo(location.getX(), location.getY(), location.getZ());
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setCanPickUpLoot(false);
		
		worldServer.addFreshEntity(this);
	}
	
}
