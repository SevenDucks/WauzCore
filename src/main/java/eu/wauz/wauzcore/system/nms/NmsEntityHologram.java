package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;

/**
 * A hologram entity based on an armor stand.
 * 
 * @author Wauzmons
 */
public class NmsEntityHologram extends ArmorStand {
	
	/**
	 * Creates a hologram entity.
	 * 
	 * @param location The spawn location of the hologram.
	 * @param display The text of the hologram.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity create(Location location, String display) {
		ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
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
	private NmsEntityHologram(ServerLevel worldServer, double x, double y, double z, String display) {
		super(worldServer, x, y, z);
		
		this.collides = false;
		this.persist = false;
		
		this.setInvisible(true);
		this.setInvulnerable(true);
		this.setSmall(false);
		this.setCustomName(MutableComponent.create(new LiteralContents(display)));
		this.setCustomNameVisible(true);

		worldServer.addFreshEntity(this);
	}

}
