package eu.wauz.wauzcore.system.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.ChatFormatter;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_13_R2.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.WorldServer;

public class WauzNmsClient {
	
	public static void nmsRepsawn(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.a(
				new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
	}

	public static void nmsActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(
				new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.GAME_INFO));
	}
	
	public static void nmsChatCommand(Player player, String command, String message, boolean border) {
		if(border)
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		
		IChatBaseComponent comp = ChatSerializer
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + ChatFormatter.ICON_PGRPH + "bClick Here\","
						+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Run Command\"},"
						+ "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + command + "\"}}]}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
        
        if(border)
        	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
	}
	
	public static void nmsChatHyperlink(Player player, String url, String message, boolean border) {
		if(border)	
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		
		IChatBaseComponent comp = ChatSerializer
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + ChatFormatter.ICON_PGRPH + "bClick Here\","
						+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Open URL\"},"
						+ "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"}}]}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(comp);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
        
        if(border)
        	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
	}
	
	public static void nmsEntityPersistence(org.bukkit.entity.Entity entity, boolean persistent) {
		((CraftEntity) entity).getHandle().persist = persistent;
	}
	
	public static org.bukkit.entity.Entity nmsCustomEntityHologram(Location location, String display) {
		return new Hologram(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), display).getBukkitEntity();
	}
	
	private static class Hologram extends EntityArmorStand {
		
		public Hologram(WorldServer worldServer, double x, double y, double z, String display) {
			super(worldServer, x, y, z);
			
			this.collides = false;
			this.persist = false;
			
			this.setInvisible(true);
			this.setInvulnerable(true);
			this.setCustomName(new ChatMessage(display));
			this.setCustomNameVisible(true);

			worldServer.addEntity(this);
		}
		
	}

//	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
//		Field field;
//		Object result = null;
//
//		try {
//			field = clazz.getDeclaredField(fieldName);
//			field.setAccessible(true);
//			result = field.get(object);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}

}
