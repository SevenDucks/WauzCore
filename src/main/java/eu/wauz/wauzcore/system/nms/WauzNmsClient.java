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
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + ChatFormatter.ICON_PARAGRAPH + "bClick Here\","
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
				.a("{\"text\":\"" + message + " \",\"extra\":[{\"text\":\"" + ChatFormatter.ICON_PARAGRAPH + "bClick Here\","
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
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		
		return new Hologram(worldServer, x, y, z, display).getBukkitEntity();
	}
	
	private static class Hologram extends EntityArmorStand {
		
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
	
	public static org.bukkit.entity.Entity nmsCustomEntityTotem(Player player, ItemStack headItemStack) {
		Location location = player.getLocation();
		WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		String display = ChatColor.GREEN + player.getDisplayName() + "'s Totem";
		
		return new Totem(worldServer, x, y, z, display, headItemStack).getBukkitEntity();
	}
	
	private static class Totem extends EntityArmorStand {
		
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
