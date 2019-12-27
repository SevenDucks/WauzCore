package eu.wauz.wauzcore.data;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Regions.yml.
 * 
 * @author Wauzmons
 */
public class RegionConfigurator extends GlobalConfigurationUtils {
	
// Keys
	
	/**
	 * @return The key for the whole server.
	 */
	public static String getServerRegionKey() {
		return mainConfigGetString("Server", "key");
	}
	
	/**
	 * @return The keys of the master regions.
	 */
	public static List<String> getMasterRegionKeys() {
		return new ArrayList<>(mainConfigGetKeys("Regions", null));
	}
	
	/**
	 * @param masterKey The parent region key.
	 * 
	 * @return The children region keys.
	 */
	public static List<String> getSubRegionKeys(String masterKey) {
		return new ArrayList<>(mainConfigGetKeys("Regions", masterKey + ".areas"));
	}
	
// Types and Coordinates
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The type of the region.
	 */
	public static String getType(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".type");
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The world name of a world typed region.
	 */
	public static String getTypeWorld(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".world");
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The music file name of the region.
	 */
	public static String getMusic(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".music");
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The shape of a polygon typed region.
	 */
	public static Polygon getTypePolygon(String regionKey) {
		List<String> pointCoordinates = mainConfigGetStringList("Regions", regionKey + ".points");
		
		Polygon polygon = new Polygon();
		for(String coordinatePairString : pointCoordinates) {
			String[] coordinatePair = coordinatePairString.split(";");
			int x = Integer.parseInt(coordinatePair[0]) * 10;
			int y = Integer.parseInt(coordinatePair[1]) * 10;
			x += x >= 0 ? 5 : -5;
			y += y >= 0 ? 5 : -5;
			polygon.addPoint(x, y);
		}
		return polygon;
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The shape of a ellipse typed region.
	 */
	public static Ellipse2D getTypeEllipse(String regionKey) {
		String[] ellipseProperties = mainConfigGetString("Regions", regionKey + ".radius").split(";");
		float radius = Float.parseFloat(ellipseProperties[2]) + 1;
		float x = Float.parseFloat(ellipseProperties[0]) - radius;
		float y = Float.parseFloat(ellipseProperties[1]) - radius;
		
		return new Ellipse2D.Float(x, y, radius * 2, radius * 2);
	}
	
// Properties
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The display title of the region.
	 */
	public static String getTitle(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".title");
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The display subtitle of the region.
	 */
	public static String getSubtitle(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".subtitle");
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The flags of the region.
	 */
	public static List<String> getFlags(String regionKey) {
		return Arrays.asList(mainConfigGetString("Regions", regionKey + ".flags").split(","));
	}
	
	/**
	 * @param regionKey The key of the region.
	 * 
	 * @return The base temperature of the region.
	 */
	public static byte getTemperature(String regionKey) {
		return (byte) mainConfigGetInt("Regions", regionKey + ".temperature");
	}
	
// Stations
	
	/**
	 * @param stationId The name of the travelling station.
	 * 
	 * @return The coordinates of the station as string.
	 */
	public static String getStationCoordinateString(String stationId) {
		return mainConfigGetString("Stations", stationId);
	}

}
