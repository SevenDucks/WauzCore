package eu.wauz.wauzcore.system.nms;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/**
 * A human entity used to trigger map rendering.
 * 
 * @author Wauzmons
 */
public class NmsEntityMockPlayer extends Player {
	
	/**
	 * The world server the entity is located in.
	 */
	private ServerLevel worldServer;
	
	/**
	 * Creates a mock player in the given world.
	 * 
	 * @param worldServer The world server the entity is located in.
	 */
	public NmsEntityMockPlayer(ServerLevel worldServer) {
		super(worldServer.getMinecraftWorld(), new BlockPos(0, 64, 0), 64, new GameProfile(UUID.randomUUID(), ""));
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
	 * @param mapItem The map item.
	 * @param mapData The world map that should be rendered.
	 * @param size The size of the world map.
	 */
	public void updateMap(MapItem mapItem, MapItemSavedData mapData, int size) {
		for (int x = mapData.x - size / 2; x <= mapData.x + size / 2; x += 24) {
            for (int z = mapData.z - size / 2; z <= mapData.z + size / 2; z += 24) {
                this.teleportTo(x, 64, z);
                mapItem.update(worldServer, this, mapData);
            }
        }
	}

}
