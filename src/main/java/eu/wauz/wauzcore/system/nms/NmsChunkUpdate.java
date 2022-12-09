package eu.wauz.wauzcore.system.nms;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;

import net.minecraft.server.level.ServerLevel;

/**
 * Chunk update using net.minecraft.server classes.
 * 
 * @author Wauzmons
 */
public class NmsChunkUpdate {
	
	/**
	 * Sends a packet to update a chunk for all players in the world.
	 * 
	 * @param chunk The chunk to update.
	 */
	public static void init(Chunk chunk) {
		ServerLevel worldServer = ((CraftWorld) chunk.getWorld()).getHandle();
//		LevelChunk levelChunk = worldServer.getChunkAt(chunk.getX(), chunk.getZ());
		
//		levelChunk.runPostLoad();
		
//		ClientboundLevelChunk packet = new ClientboundLevelChunkPacket(levelChunk);
//		for(Player player : chunk.getWorld().getPlayers()) {
//			((CraftPlayer) player).getHandle().connection.send(packet);
//		}
	}

}
