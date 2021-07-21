package eu.wauz.wauzcore.system.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_17_R1.map.CraftMapView;
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

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.WauzMode;
import eu.wauz.wauzcore.worlds.instances.WauzActiveInstance;
import eu.wauz.wauzcore.worlds.instances.WauzActiveInstancePool;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

/**
 * Live minimap using net.minecraft.server classes.
 * Extracted from the general classes in the nms client.
 * 
 * @author Wauzmons
 * 
 * @see WauzNmsClient
 */
public class WauzNmsMinimap {
	
	/**
	 * Initializes the world map in the players inventory and adds a custom renderer.
	 * Renders the map every ~20 ticks if it is the first time or if the player has moved more than 8 blocks.
	 * Also draws the region name at the top and an x marker in the middle of the map.
	 * 
	 * @param player The player who holds the map.
	 */
    public static void init(Player player) {
    	MapView mapView = null;
    	ItemStack mapItemStack = player.getInventory().getItem(6);
		if(mapItemStack == null || !mapItemStack.getType().equals(Material.FILLED_MAP)) {
			return;
		}
		
		MapMeta mm = (MapMeta) mapItemStack.getItemMeta();
		
    	try {
    		mapView = mm.getMapView();
    	}
    	catch(Exception e) {
    		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
    			
                @Override
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
        
        net.minecraft.world.item.ItemStack craftItemStack = CraftItemStack.asNMSCopy(mapItemStack);
        MapItem mapItem = (MapItem) craftItemStack.getItem();
        ServerLevel worldServer = ((CraftWorld) mapView.getWorld()).getHandle();
        MapItemSavedData mapData = MapItem.getSavedData(craftItemStack, worldServer.getMinecraftWorld());
		
		mapView.addRenderer(new CraftMapRenderer((CraftMapView) mapView, mapData) {
			
			/**
			 * If the map is rendered for the first time
			 */
			boolean firstRender = true;
			
			/**
			 * If the iterator exceeds 20, the mao is rendered that tick.
			 */
			private int iterator = 0;
			
			/**
			 * Renders the map every ~10 ticks if it is the first time or if the player has moved more than 12 blocks.
			 * Also draws the region name at the top and an x marker in the middle of the map.
			 */
			@Override
			public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
				if (iterator > 10) {
	                iterator = 0;
	                
	    			for (int i = 0; i < mapData.colors.length; i ++) {
	                    mapData.colors[i] = 0;
	                }
	                
	    			Location playerLocation = player.getLocation();
	    			Location mapLocation = new Location(player.getWorld(), mapView.getCenterX(), playerLocation.getY(), mapView.getCenterZ());
	    			Material heldMaterial = player.getEquipment().getItemInMainHand().getType();
	    			if(firstRender || (playerLocation.distance(mapLocation) > 8 && heldMaterial.equals(Material.FILLED_MAP))) {
	    				firstRender = false;
	    				
	    				mapView.setWorld(player.getWorld());
	    				mapView.setCenterX(player.getLocation().getBlockX());
	    				mapView.setCenterZ(player.getLocation().getBlockZ());
	    				
	    				NmsEntityMockPlayer mockPlayer = new NmsEntityMockPlayer(worldServer);
	    				mockPlayer.updateMap(null, null, iterator);
	    				mockPlayer.updateMap(mapItem, mapData, 128 << mapData.scale);
	    				super.render(mapView, mapCanvas, player);
	    				
	    				for(int x = 0; x <= 128; x++) {
	    					for(int z = 0; z <= 9; z++) {
	    						mapCanvas.setPixel(x, z, (byte) 0);
	    					}
	    				}
	    				
	    				WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
	    				if(playerData != null && playerData.getSelections().getRegion() != null) {
	    					mapCanvas.drawText(1, 1, MinecraftFont.Font, playerData.getSelections().getRegion().getTitle());
	    				}
	    				else if(WauzMode.isInstance(mapView.getWorld().getName())) {
	    					WauzActiveInstance instance = WauzActiveInstancePool.getInstance(mapView.getWorld());
	    					mapCanvas.drawText(1, 1, MinecraftFont.Font, instance.getInstanceName());
	    				}
	    				else {
	    					mapCanvas.drawText(1, 1, MinecraftFont.Font, "Unknown Region");
	    				}
	    				
	    				MapCursorCollection mapCursorCollection = new MapCursorCollection();
	    				mapCursorCollection.addCursor(new MapCursor((byte) 0, (byte) 0, (byte) 0, MapCursor.Type.WHITE_CROSS, true));
	    				mapCanvas.setCursors(mapCursorCollection);
	    			}
	            }
	            iterator++;
			}
			
        });
    }

}
