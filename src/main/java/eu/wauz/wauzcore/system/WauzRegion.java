package eu.wauz.wauzcore.system;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.RegionConfigurator;
import eu.wauz.wauzcore.oneblock.OneBlock;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A region, generated from a region config file.
 * 
 * @author Wauzmons
 */
public class WauzRegion {
	
	/**
	 * A list of all top level regions.
	 */
	private static List<WauzRegion> masterRegions = new ArrayList<>();
	
	/**
	 * The number of all constructed regions.
	 */
	private static int regionCount;
	
	/**
	 * The flag to allow building in a region.
	 */
	private static final String FLAG_BUILD = "build";
	
	/**
	 * The flag to prevent changes in a region.
	 */
	private static final String FLAG_PROTECTED = "protected";
	
	/**
	 * The flag to enable pvp in a region.
	 */
	private static final String FLAG_PVP = "pvp";
	
	/**
	 * Initializes all master regions and their sub regions.
	 */
	public static void init() {
		for(String masterRegionKey : RegionConfigurator.getMasterRegionKeys()) {
			masterRegions.add(new WauzRegion(null, masterRegionKey));
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + regionCount + " Regions!");
	}
	
	/**
	 * Checks if a player region is still valid and determines the new region, if not.
	 * Also handles region entering and changes the music, if the region has changed.
	 * 
	 * @param player The player whose region should be updated.
	 * 
	 * @see WauzRegion#getNewRegion(Location)
	 * @see WauzRegion#getNewRegion(Location, WauzRegion, boolean)
	 * @see WauzRegion#enter(Player)
	 * @see WauzNoteBlockPlayer#play(Player)
	 */
	public static void regionCheck(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		WauzRegion currentRegion = playerData.getSelections().getRegion();
		WauzRegion newRegion = null;
		
		if(currentRegion == null) {
			newRegion = getNewRegion(player.getLocation());
		}
		else {
			newRegion = getNewRegion(player.getLocation(), currentRegion, false);
		}
		
		playerData.getSelections().setRegion(newRegion);
		if(currentRegion != newRegion) {
			if(newRegion != null) {
				newRegion.enter(player);
			}
			else {
				WauzNoteBlockPlayer.play(player);
			}
		}
	}
	
	/**
	 * @param block The affected block.
	 * 
	 * @return If the block cannot be broken / built on.
	 */
	public static boolean disallowBuild(Block block) {
		if(WauzMode.isInstanceOfType(block.getWorld().getName(), "Survival")) {
			return false;
		}
		WauzRegion region = getNewRegion(block.getLocation());
		return region == null || !region.getFlags().contains(FLAG_BUILD);
	}
	
	/**
	 * @param block The affected block.
	 * 
	 * @return If the block cannot be changed.
	 */
	public static boolean disallowBlockChange(Block block) {
		WauzRegion region = getNewRegion(block.getLocation());
		return (region != null && region.getFlags().contains(FLAG_PROTECTED)) || OneBlock.isOneBlock(block);
	}
	
	/**
	 * @param attacker The entity that is attacking.
	 * @param attacked The entity that gets attacked.
	 * 
	 * @return If the entities cannot hurt each other.
	 */
	public static boolean disallowPvP(Entity attacker, Entity attacked) {
		if(!(attacker instanceof Player) || !(attacked instanceof Player)) {
			return false;
		}
		if(attacker.equals(attacked)) {
			attacker.sendMessage(ChatColor.RED + "Don't hurt yourself! There are so many reasons to live!");
			return true;
		}
		if(!isCombatZone(attacker.getLocation()) || !isCombatZone(attacked.getLocation())) {
			attacker.sendMessage(ChatColor.RED + "Attack canceled! One of you is not in a PvP-Zone!");
			return true;
		}
		if(DamageCalculator.hasPvPProtection((Player) attacker)) {
			attacker.sendMessage(ChatColor.RED + "Attack canceled! You have PvP-Protection!");
			return true;
		}
		if(DamageCalculator.hasPvPProtection((Player) attacked)) {
			attacker.sendMessage(ChatColor.RED + "Attack canceled! This player has PvP-Protection!");
			return true;
		}
		return false;
	}
	
	/**
	 * @param location The affected location.
	 * 
	 * @return If players can hurt each other in this location.
	 */
	public static boolean isCombatZone(Location location) {
		WauzRegion region = getNewRegion(location);
		return region != null && region.getFlags().contains(FLAG_PVP);
	}
	
	/**
	 * Finds out the region of a location, if the current region is unknown.
	 * 
	 * @param location The location to check.
	 * 
	 * @return The recursively determined region of the location.
	 */
	public static WauzRegion getNewRegion(Location location) {
		WauzRegion result = null;
		for(WauzRegion masterRegion : masterRegions) {
			if(insideRegion(location, masterRegion)) {
				return getNewRegion(location, masterRegion, true);
			}
		}
		return result;
	}
	
	/**
	 * Finds out the region of a location, if the current region is known.
	 * 
	 * @param location The location to check.
	 * @param region The current region.
	 * @param knownToBeInRegion If it should not be checked if the current region is still valid.
	 * 
	 * @return The recursively determined region of the location.
	 */
	private static WauzRegion getNewRegion(Location location, WauzRegion region, boolean knownToBeInRegion) {
		if(!knownToBeInRegion) {
			boolean inRegion = insideRegion(location, region);
			if(!inRegion) {
				return region.getParent() != null
						? getNewRegion(location, region.getParent(), false)
						: getNewRegion(location);
			}
		}
		for(WauzRegion subRegion : region.getChildren()) {
			if(insideRegion(location, subRegion)) {
				return getNewRegion(location, subRegion, true);
			}
		}
		return region;
	}
	
	/**
	 * @param location The location to check.
	 * @param region The region to check.
	 * 
	 * @return If the location is inside the region.
	 */
	private static boolean insideRegion(Location location, WauzRegion region) {
		String type = region.getType();

		if(type.equals("world")) {
			return insideWorldRegion(location, region.getWorld());
		}
		else if(type.equals("polygon")) {
			return insidePolygonRegion(location, region.getPolygon());
		}
		else if(type.equals("ellipse")) {
			return insideEllipseRegion(location, region.getEllipse());
		}
		return false;
	}
	
	/**
	 * @param location The location to check.
	 * @param world The name of the world, that represents the region.
	 * 
	 * @return  If the location is inside the region.
	 */
	private static boolean insideWorldRegion(Location location, String world) {
		return location.getWorld().getName().equals(world);
	}
	
	/**
	 * @param location The location to check.
	 * @param polygon The polygon, that represents the region.
	 * 
	 * @return If the location is inside the region.
	 */
	private static boolean insidePolygonRegion(Location location, Polygon polygon) {
		return polygon.contains(location.getX() * 10, location.getZ() * 10);
	}
	
	/**
	 * @param location The location to check.
	 * @param ellipse The ellipse, that represents the region.
	 * 
	 * @return If the location is inside the region.
	 */
	private static boolean insideEllipseRegion(Location location, Ellipse2D ellipse) {
		return ellipse.contains(location.getBlock().getX(), location.getBlock().getZ());
	}
	
	/**
	 * The region, this region is inside.
	 */
	private WauzRegion parent;
				
	/**
	 * All regions inside this region.
	 */
	private List<WauzRegion> children = new ArrayList<>();
	
	/**
	 * The configuration key of this region.
	 */
	private String regionKey;
				
	/**
	 * The type of this region a text.
	 */
	private String type;
	
	/**
	 * The name of the world, that represents the region.
	 */
	private String world;
	
	/**
	 * The polygon, that represents the region.
	 */
	private Polygon polygon;
	
	/**
	 * The ellipse, that represents the region.
	 */
	private Ellipse2D ellipse;
	
	/**
	 * The title that is shown when entering the region.
	 */
	private String title;
	
	/**
	 * The subtitle that is shown when entering the region.
	 */
	private String subtitle;
	
	/**
	 * The music that plays to players inside the region.
	 */
	private String music;
	
	/**
	 * The base temperature of the region.
	 */
	private byte temperature;
	
	/**
	 * The flags of this region.
	 */
	private List<String> flags;
	
	/**
	 * Constructs a region, based on the region file in the /WauzCore folder.
	 * 
	 * @param parent The region, this region is inside.
	 * @param regionKey The configuration key of this region.
	 */
	public WauzRegion(WauzRegion parent, String regionKey) {
		this.regionKey = regionKey;
		this.parent = parent;
		
		title = RegionConfigurator.getTitle(regionKey);
		subtitle = RegionConfigurator.getSubtitle(regionKey);
		music = RegionConfigurator.getMusic(regionKey);
		temperature = RegionConfigurator.getTemperature(regionKey);
		flags = RegionConfigurator.getFlags(regionKey);
		
		type = RegionConfigurator.getType(regionKey);
		
		if(type.equals("world")) {
			world = RegionConfigurator.getTypeWorld(regionKey);
		}
		else if(type.equals("polygon")) {
			polygon = RegionConfigurator.getTypePolygon(regionKey);
		}
		else if(type.equals("ellipse")) {
			ellipse = RegionConfigurator.getTypeEllipse(regionKey);
		}
		
		for(String subRegionKey : RegionConfigurator.getSubRegionKeys(regionKey)) {
			children.add(new WauzRegion(this, regionKey + ".areas." + subRegionKey));
		}
		
		regionCount++;
	}
	
	/**
	 * Called when a player enters the region.
	 * Shows the title and switches music, if possible.
	 * 
	 * @param player The player that entered the region.
	 */
	public void enter(Player player) {
		WauzDebugger.log(player, "Entered Region: " + this);
		if(StringUtils.isNotBlank(title)) {
			player.sendTitle(ChatColor.GREEN + title, subtitle != null ? subtitle : "", 10, 70, 20);
			AchievementTracker.checkForAchievement(player, WauzAchievementType.EXPLORE_REGIONS, title);
		}
		
		if(music != null) {
			WauzNoteBlockPlayer.play(player, music);
		}
		else {
			WauzNoteBlockPlayer.play(player);
		}
	}

	/**
	 * @return The region, this region is inside.
	 */
	public WauzRegion getParent() {
		return parent;
	}

	/**
	 * @return All regions inside this region.
	 */
	public List<WauzRegion> getChildren() {
		return children;
	}

	/**
	 * @return The configuration key of this region.
	 */
	public String getRegionKey() {
		return regionKey;
	}

	/**
	 * @return The type of this region a text.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return The name of the world, that represents the region.
	 */
	public String getWorld() {
		return world;
	}

	/**
	 * @return The polygon, that represents the region.
	 */
	public Polygon getPolygon() {
		return polygon;
	}
	
	/**
	 * @return The ellipse, that represents the region.
	 */
	public Ellipse2D getEllipse() {
		return ellipse;
	}

	/**
	 * @return The title that is shown when entering the region.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return The subtitle that is shown when entering the region.
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * @return The base temperature of the region.
	 */
	public byte getTemperature() {
		return temperature;
	}

	/**
	 * @return The flags of this region.
	 */
	public List<String> getFlags() {
		return flags;
	}

	/**
	 * @return The region key as a readable string.
	 */
	@Override
	public String toString() {
		return "(Region: " + regionKey.replace(".areas.", ":") + ")";
	}

}
