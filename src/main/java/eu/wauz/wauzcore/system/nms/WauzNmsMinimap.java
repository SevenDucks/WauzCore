package eu.wauz.wauzcore.system.nms;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R2.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_13_R2.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.map.MinecraftFont;

import com.mojang.authlib.GameProfile;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.ItemWorldMap;
import net.minecraft.server.v1_13_R2.WorldMap;
import net.minecraft.server.v1_13_R2.WorldServer;

public class WauzNmsMinimap {
	
    public static void init(Player player) {
    	MapView mapView = null;
    	ItemStack mapItem = player.getEquipment().getItemInOffHand();
		if(mapItem == null || !mapItem.getType().equals(Material.FILLED_MAP))
			return;
		
		MapMeta mm = (MapMeta) mapItem.getItemMeta();
		
    	try {
    		mapView = mm.getMapView();
    	}
    	catch(Exception e) {
    		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
                public void run() {
                	init(player);
                }
    		}, 5);
    		return;
    	}
    	
        for (MapRenderer renderer : mapView.getRenderers()) {
        	mapView.removeRenderer(renderer);
        }
        
        mapView.setWorld(player.getWorld());
        mapView.setCenterX(player.getLocation().getBlockX());
		mapView.setCenterZ(player.getLocation().getBlockZ());
        mapView.setScale(Scale.CLOSE);
        
        net.minecraft.server.v1_13_R2.ItemStack craftItemStack = CraftItemStack.asNMSCopy(mapItem);
		ItemWorldMap itemWorldMap = (ItemWorldMap) craftItemStack.getItem();
		
		WorldServer worldServer = ((CraftWorld) mapView.getWorld()).getHandle();
		WorldMap worldMap = ItemWorldMap.getSavedMap(craftItemStack, worldServer.getMinecraftWorld());
		
		mapView.addRenderer(new CraftMapRenderer((CraftMapView) mapView, worldMap) {
			
			boolean firstRender = true;
			
			private int iterator = 0;
			
			@Override
			public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
				if (iterator > 20) {
	                iterator = 0;
	                
	    			for (int i = 0; i < worldMap.colors.length; i ++) {
	                    worldMap.colors[i] = 0;
	                }
	                
	    			Location playerLocation = player.getLocation();
	    			Location mapLocation = new Location(player.getWorld(), mapView.getCenterX(), playerLocation.getY(), mapView.getCenterZ());
	    			if(firstRender || playerLocation.distance(mapLocation) > 12) {
	    				firstRender = false;
	    				
	    				mapView.setWorld(player.getWorld());
	    				mapView.setCenterX(player.getLocation().getBlockX());
	    				mapView.setCenterZ(player.getLocation().getBlockZ());
	    				
	    				MockPlayer mockPlayer = new MockPlayer(worldServer);
	    				mockPlayer.updateMap(itemWorldMap, worldMap, 128 << worldMap.scale);
	    				super.render(mapView, mapCanvas, player);
	    				
	    				for(int x = 0; x <= 128; x++)
	    					for(int z = 0; z <= 9; z++)
	    						mapCanvas.setPixel(x, z, (byte) 0);
	    				
	    				WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
	    				if(pd != null && pd.getRegion() != null)
	    					mapCanvas.drawText(1, 1, MinecraftFont.Font, pd.getRegion().getTitle());
	    				else if(WauzMode.isInstance(mapView.getWorld().getName()))
	    					mapCanvas.drawText(1, 1, MinecraftFont.Font, InstanceConfigurator.getInstanceWorldName(mapView.getWorld()));
	    				else
	    					mapCanvas.drawText(1, 1, MinecraftFont.Font, "Unknown Region");
	    				
	    				MapCursorCollection mapCursorCollection = new MapCursorCollection();
	    				mapCursorCollection.addCursor(new MapCursor((byte) 0, (byte) 0, (byte) 0, MapCursor.Type.WHITE_CROSS, true));
	    				mapCanvas.setCursors(mapCursorCollection);
	    			}
	            }
	            iterator ++;
			}
			
        });
    }
	
	private static class MockPlayer extends EntityHuman {
		
		private WorldServer worldServer;
		
		public MockPlayer(WorldServer worldServer) {
			super(worldServer.getMinecraftWorld(), new GameProfile(UUID.randomUUID(), ""));
			this.worldServer = worldServer;
		}
		
		@Override
		public boolean u() {
			return false;
		}
		
		@Override
		public boolean isSpectator() {
			return false;
		}
		
		public void updateMap(ItemWorldMap itemWorldMap, WorldMap worldMap, int size) {
			for (int x = worldMap.centerX - size / 2; x <= worldMap.centerX + size / 2; x += 24) {
	            for (int z = worldMap.centerZ - size / 2; z <= worldMap.centerZ + size / 2; z += 24) {
	                locX = x;
	                locZ = z;
	                itemWorldMap.a(worldServer, this, worldMap);
	            }
	        }
		}
		
	}

}
