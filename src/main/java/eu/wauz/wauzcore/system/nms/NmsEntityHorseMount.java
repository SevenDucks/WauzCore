package eu.wauz.wauzcore.system.nms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Horse.Color;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;

/**
 * A horse mount entity based on a normal horse.
 * 
 * @author Wauzmons
 */
public class NmsEntityHorseMount extends Horse {
	
	/**
	 * Creates a horse mount entity.
	 * 
	 * @param location The spawn location of the horse.
	 * @param color the color of the horse.
	 * @param name The display name of the horse.
	 * 
	 * @return The created horse entity.
	 */
	public static org.bukkit.entity.Horse create(Location location, Color color, String name) {
		ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
		return (org.bukkit.entity.Horse) new NmsEntityHorseMount(worldServer, location, color, ChatColor.GREEN + name).getBukkitEntity();
	}
	
	/**
	 * Creates a horse mount entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param location The spawn location of the entity.
	 * @param color the color of the entity.
	 * @param name The display name of the entity.
	 */
	private NmsEntityHorseMount(ServerLevel worldServer, Location location, Color color, String name) {
		super(EntityType.HORSE, worldServer);
		
		this.collides = false;
		this.persist = false;
		this.age = 1;
		this.ageLocked = true;
		
		this.teleportTo(location.getX(), location.getY(), location.getZ());
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setCanPickUpLoot(false);
		this.setCustomName(MutableComponent.create(new LiteralContents(name)));
		this.setCustomNameVisible(true);
		
		org.bukkit.entity.Horse horse = (org.bukkit.entity.Horse) this.getBukkitEntity();
		horse.setJumpStrength(0.69);
		horse.setColor(color);
		
		worldServer.addFreshEntity(this);
	}

}
