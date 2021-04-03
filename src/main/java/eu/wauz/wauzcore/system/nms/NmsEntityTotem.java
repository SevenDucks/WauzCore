package eu.wauz.wauzcore.system.nms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.WorldServer;

/**
 * A totem entity based on an armor stand.
 * 
 * @author Wauzmons
 */
public class NmsEntityTotem extends EntityArmorStand {
	
	/**
	 * Creates a totem entity.
	 * 
	 * @param player The owner of the totem.
	 * @param headItemStack The head of the totem.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity create(Player player, ItemStack headItemStack) {
		Location location = player.getLocation();
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		String display = ChatColor.GREEN + player.getDisplayName() + "'s Totem";
		
		return new NmsEntityTotem(worldServer, x, y, z, display, headItemStack, null).getBukkitEntity();
	}
	
	/**
	 * Creates a tower (big totem) entity.
	 * 
	 * @param player The owner of the tower.
	 * @param headItemStack The head of the tower.
	 * @param bodyItemStack The body of the tower.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity create(Player player, ItemStack headItemStack, ItemStack bodyItemStack) {
		Location location = player.getLocation();
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		String display = ChatColor.GREEN + player.getDisplayName() + "'s Tower";
		
		return new NmsEntityTotem(worldServer, x, y, z, display, headItemStack, bodyItemStack).getBukkitEntity();
	}
	
	/**
	 * Creates a totem entity.
	 * 
	 * @param worldServer The world server to create the entity on.
	 * @param x The x coordinate of the spawn location.
	 * @param y The y coordinate of the spawn location.
	 * @param z The z coordinate of the spawn location.
	 * @param display The text of the totem.
	 * @param headItemStack The head of the totem.
	 * @param bodyItemStack The body of the totem.
	 */
	private NmsEntityTotem(WorldServer worldServer, double x, double y, double z, String display, ItemStack headItemStack, ItemStack bodyItemStack) {
		super(worldServer, x, y, z);
		
		this.collides = false;
		this.persist = false;
		this.canPickUpLoot = false;
		
		this.setInvisible(false);
		this.setInvulnerable(true);
		this.setSmall(bodyItemStack == null);
		this.setCustomName(new ChatMessage(display));
		this.setCustomNameVisible(true);
		
		this.setArms(false);
		this.setBasePlate(false);
		
		ArmorStand armorStand = (ArmorStand) this.getBukkitEntity();
		armorStand.getEquipment().setHelmet(headItemStack);
		armorStand.getEquipment().setChestplate(bodyItemStack);

		worldServer.addEntity(this);
	}

}
