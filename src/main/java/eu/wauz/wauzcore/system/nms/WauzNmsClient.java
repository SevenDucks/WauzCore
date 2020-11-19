package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R2.ItemStack;
import net.minecraft.server.v1_16_R2.MojangsonParser;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R2.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_16_R2.WorldBorder;

/**
 * Collection of general net.minecraft.server specific methods.
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
	
	/**
	 * Sends a packet to create a world border for the given player.
	 * 
	 * @param player The player to create the border for.
	 * @param location The location of the border.
	 * @param radius The radius of the border.
	 */
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
	
	/**
	 * Serializes the nbt data of an item stack, so it equals the default save format.
	 * 
	 * @param itemStack The item stack to serialize.
	 * 
	 * @return The serialized item stack.
	 */
	public static org.bukkit.inventory.ItemStack nmsSerialize(org.bukkit.inventory.ItemStack itemStack) {
		try {
			return nmsItemFromString(nmsStringFromItem(itemStack));
		}
		catch (Exception e) {
			e.printStackTrace();
			return itemStack;
		}
	}
	
	/**
	 * Converts a string into an item stack.
	 * 
	 * @param dataString The string to convert.
	 * 
	 * @return The created item stack.
	 * 
	 * @throws Exception Error while converting.
	 */
	public static org.bukkit.inventory.ItemStack nmsItemFromString(String dataString) throws Exception {
		NBTTagCompound compound = MojangsonParser.parse(dataString);
		return CraftItemStack.asBukkitCopy(ItemStack.a(compound));
	}
	
	/**
	 * Conberts an item stack into a string.
	 * 
	 * @param itemStack The item to convert.
	 * 
	 * @return The created string.
	 * 
	 * @throws Exception Error while converting.
	 */
	public static String nmsStringFromItem(org.bukkit.inventory.ItemStack itemStack) throws Exception {
		return CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).asString();
	}
	
}
