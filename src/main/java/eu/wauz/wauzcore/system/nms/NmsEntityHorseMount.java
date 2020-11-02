package eu.wauz.wauzcore.system.nms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Horse;

import net.minecraft.server.v1_16_R2.ChatMessage;
import net.minecraft.server.v1_16_R2.EntityHorse;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.HorseColor;
import net.minecraft.server.v1_16_R2.HorseStyle;
import net.minecraft.server.v1_16_R2.WorldServer;

/**
 * A horse mount entity based on a normal horse.
 * 
 * @author Wauzmons
 */
public class NmsEntityHorseMount extends EntityHorse {
	
	/**
	 * Creates a horse mount entity.
	 * 
	 * @param location The spawn location of the horse.
	 * @param color the color of the horse.
	 * @param name The display name of the horse.
	 * 
	 * @return The created horse entity.
	 */
	public static org.bukkit.entity.Horse create(Location location, Horse.Color color, String name) {
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		HorseColor horseColor = HorseColor.valueOf(color.toString());
		return (Horse) new NmsEntityHorseMount(worldServer, location, horseColor, ChatColor.GREEN + name).getBukkitEntity();
	}
	
	/**
	 * Creates a horse mount entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param location The spawn location of the entity.
	 * @param color the color of the entity.
	 * @param name The display name of the entity.
	 */
	private NmsEntityHorseMount(WorldServer worldServer, Location location, HorseColor color, String name) {
		super(EntityTypes.HORSE, worldServer);
		
		this.persist = false;
		this.canPickUpLoot = false;
		this.jumpPower = 2.0f;
		
		this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setAge(1, true);
		this.setCustomName(new ChatMessage(name));
		this.setCustomNameVisible(true);
		this.setVariant(color, HorseStyle.NONE);
		
		worldServer.addEntity(this);
	}

}
