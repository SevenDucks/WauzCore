package eu.wauz.wauzcore.system.nms;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.ItemWorldMap;
import net.minecraft.server.v1_16_R3.WorldMap;
import net.minecraft.server.v1_16_R3.WorldServer;

/**
 * A human entity used to trigger map rendering.
 * 
 * @author Wauzmons
 */
public class NmsEntityMockPlayer extends EntityHuman {
	
	/**
	 * The world server the entity is located in.
	 */
	private WorldServer worldServer;
	
	/**
	 * Creates a mock player in the given world.
	 * 
	 * @param worldServer The world server the entity is located in.
	 */
	public NmsEntityMockPlayer(WorldServer worldServer) {
		super(worldServer.getMinecraftWorld(), new BlockPosition(0, 64, 0), 64, new GameProfile(UUID.randomUUID(), ""));
		this.worldServer = worldServer;
	}
	
	/**
	 * If the player is in creative mode. Always false.
	 */
	@Override
	public boolean isCreative() {
		return false;
	}
	
	/**
	 * If the player is in spectator mode. Always false.
	 */
	@Override
	public boolean isSpectator() {
		return false;
	}
	
	/**
	 * Uses the entity to render the given map.
	 * 
	 * @param itemWorldMap The map item.
	 * @param worldMap The world map that should be rendered.
	 * @param size The size of the world map.
	 */
	public void updateMap(ItemWorldMap itemWorldMap, WorldMap worldMap, int size) {
		for (int x = worldMap.centerX - size / 2; x <= worldMap.centerX + size / 2; x += 24) {
            for (int z = worldMap.centerZ - size / 2; z <= worldMap.centerZ + size / 2; z += 24) {
                setLocation(x, 64, z, 0, 0);
                itemWorldMap.a(worldServer, this, worldMap);
            }
        }
	}

}
