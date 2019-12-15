package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.system.ChatFormatter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_14_R1.ChatMessage;
import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_14_R1.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import net.minecraft.server.v1_14_R1.WorldServer;

/**
 * Collection of general net.minecraft.server specific methods.
 * Used to only have a single place, when in need of changing version numbers.
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
		((CraftPlayer) player).getHandle().playerConnection.a(
				new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
	}

	/**
	 * Sends a packet to update the action bar of a player.
	 * 
	 * @param player The player whose action bar should be updated.
	 * @param message The text for that should show in the action bar.
	 */
	public static void nmsActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(
				new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.GAME_INFO));
	}
	
	/**
	 * Sends a packet with a clickable message, that executes a command, to a player.
	 * 
	 * @param player The player that should receive the message.
	 * @param command The command that should be executed on click.
	 * @param message The message that should be shown.
	 * @param border If the message has borders like this: ----------
	 */
	public static void nmsChatCommand(Player player, String command, String message, boolean border) {
		if(border) {
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		}
		
		IChatBaseComponent comp = ChatSerializer
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + ChatFormatter.ICON_PARAGRAPH + "bClick Here\","
						+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Run Command\"},"
						+ "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + command + "\"}}]}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
        
        if(border) {
        	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
        }
	}
	
	/**
	 * Sends a packet with a clickable message, that opens an url, to a player.
	 * 
	 * @param player The player that should receive the message.
	 * @param url The url that should be opened on click.
	 * @param message The message that should be shown.
	 * @param border If the message has borders like this: ----------
	 */
	public static void nmsChatHyperlink(Player player, String url, String message, boolean border) {
		if(border) {
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		}
		
		IChatBaseComponent comp = ChatSerializer
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + ChatFormatter.ICON_PARAGRAPH + "bClick Here\","
						+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Open URL\"},"
						+ "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"}}]}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
        
        if(border) {
        	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
        }
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
	 * Creates a hologram entity.
	 * 
	 * @param location The spawn location of the hologram.
	 * @param display The text of the hologram.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity nmsCustomEntityHologram(Location location, String display) {
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		
		return new Hologram(worldServer, x, y, z, display).getBukkitEntity();
	}
	
	/**
	 * A hologram entity based on an armor stand.
	 * 
	 * @author Wauzmons
	 */
	private static class Hologram extends EntityArmorStand {
		
		/**
		 * Creates a hologram entity.
		 * 
		 * @param worldServer The world server to create the entity on.
		 * @param x The x coordinate of the spawn location.
		 * @param y The y coordinate of the spawn location.
		 * @param z The z coordinate of the spawn location.
		 * @param display The text of the hologram.
		 */
		public Hologram(WorldServer worldServer, double x, double y, double z, String display) {
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
	
	/**
	 * Creates a totem entity.
	 * 
	 * @param player The owner of the totem.
	 * @param headItemStack The head of the totem.
	 * 
	 * @return The created entity.
	 */
	public static org.bukkit.entity.Entity nmsCustomEntityTotem(Player player, ItemStack headItemStack) {
		Location location = player.getLocation();
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		String display = ChatColor.GREEN + player.getDisplayName() + "'s Totem";
		
		return new Totem(worldServer, x, y, z, display, headItemStack).getBukkitEntity();
	}
	
	/**
	 * A totem entity based on an armor stand.
	 * 
	 * @author Wauzmons
	 */
	private static class Totem extends EntityArmorStand {
		
		/**
		 * Creates a totem entity.
		 * 
		 * @param worldServer The world server to create the entity on.
		 * @param x The x coordinate of the spawn location.
		 * @param y The y coordinate of the spawn location.
		 * @param z The z coordinate of the spawn location.
		 * @param display The text of the totem.
		 * @param headItemStack The head of the totem.
		 */
		public Totem(WorldServer worldServer, double x, double y, double z, String display, ItemStack headItemStack) {
			super(worldServer, x, y, z);
			
			this.collides = false;
			this.persist = false;
			this.canPickUpLoot = false;
			
			this.setInvisible(false);
			this.setInvulnerable(true);
			this.setSmall(true);
			this.setCustomName(new ChatMessage(display));
			this.setCustomNameVisible(true);
			
			this.setArms(false);
			this.setBasePlate(false);
			
			ArmorStand armorStand = (ArmorStand) this.getBukkitEntity();
			armorStand.setHelmet(headItemStack);

			worldServer.addEntity(this);
		}
		
	}

}
