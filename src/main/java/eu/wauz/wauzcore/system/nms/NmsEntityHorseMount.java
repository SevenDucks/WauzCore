package eu.wauz.wauzcore.system.nms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Variant;

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
	public static org.bukkit.entity.Horse create(Location location, org.bukkit.entity.Horse.Color color, String name) {
		ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
		Variant horseColor = Variant.valueOf(color.toString());
		return (org.bukkit.entity.Horse) new NmsEntityHorseMount(worldServer, location, horseColor, ChatColor.GREEN + name).getBukkitEntity();
	}
	
	/**
	 * Creates a horse mount entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param location The spawn location of the entity.
	 * @param color the color of the entity.
	 * @param name The display name of the entity.
	 */
	private NmsEntityHorseMount(ServerLevel worldServer, Location location, Variant color, String name) {
		super(EntityType.HORSE, worldServer);
		
		this.persist = false;
//		this.jumpPower = 2.0f; TODO
		this.age = 1;
		this.ageLocked = true;
		
		this.teleportTo(location.getX(), location.getY(), location.getZ());
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setCanPickUpLoot(false);
		this.setCustomName(new TextComponent(name));
		this.setCustomNameVisible(true);
		this.setVariantAndMarkings(color, Markings.NONE);
		
		worldServer.addFreshEntity(this);
	}

}
