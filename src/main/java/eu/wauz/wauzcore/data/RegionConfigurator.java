package eu.wauz.wauzcore.data;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.wauz.wauzcore.data.api.ConfigurationUtils;

public class RegionConfigurator extends ConfigurationUtils {
	
// Keys
	
	public static String getServerRegionKey() {
		return mainConfigGetString("Server", "key");
	}
	
	public static List<String> getMasterRegionKeys() {
		return new ArrayList<>(mainConfigGetKeys("Regions", null));
	}
	
	public static List<String> getSubRegionKeys(String masterKey) {
		return new ArrayList<>(mainConfigGetKeys("Regions", masterKey + ".areas"));
	}
	
// Types and Coordinates
	
	public static String getType(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".type");
	}
	
	public static String getTypeWorld(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".world");
	}
	
	public static String getMusic(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".music");
	}
	
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
	
	public static Ellipse2D getTypeEllipse(String regionKey) {
		String[] ellipseProperties = mainConfigGetString("Regions", regionKey + ".radius").split(";");
		float radius = Float.parseFloat(ellipseProperties[2]) + 1;
		float x = Float.parseFloat(ellipseProperties[0]) - radius;
		float y = Float.parseFloat(ellipseProperties[1]) - radius;
		
		return new Ellipse2D.Float(x, y, radius * 2, radius * 2);
	}
	
// Properties
	
	public static String getTitle(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".title");
	}
	
	public static String getSubtitle(String regionKey) {
		return mainConfigGetString("Regions", regionKey + ".subtitle");
	}
	
	public static List<String> getFlags(String regionKey) {
		return Arrays.asList(mainConfigGetString("Regions", regionKey + ".flags").split(","));
	}
	
	public static byte getTemperature(String regionKey) {
		return (byte) mainConfigGetInt("Regions", regionKey + ".temperature");
	}
	
// Stations
	
	public static String getStationCoordinateString(String stationId) {
		return mainConfigGetString("Stations", stationId);
	}

}
