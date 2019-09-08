package eu.wauz.wauzcore.system;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.RegionConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzRegion {
	
	private static List<WauzRegion> masterRegions = new ArrayList<>();
	
	private static final String FLAG_BUILD = "build";
	
	private static final String FLAG_PROTECTED = "protected";
	
	private static final String FLAG_PVP = "pvp";
	
	public static void init() {
		for(String masterRegionKey : RegionConfigurator.getMasterRegionKeys()) {
			masterRegions.add(new WauzRegion(null, masterRegionKey));
		}
	}
	
	public static void regionCheck(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		WauzRegion currentRegion = playerData.getRegion();
		WauzRegion newRegion = null;
		
		if(currentRegion == null)
			newRegion = getNewRegion(player.getLocation());
		else
			newRegion = getNewRegion(player.getLocation(), currentRegion, false);
		
		playerData.setRegion(newRegion);
		if(currentRegion != newRegion)
			if(newRegion != null)
				newRegion.enter(player);
			else
				WauzNoteBlockPlayer.play(player);
	}
	
	public static boolean disallowBuild(Block block) {
		if(WauzMode.isInstanceOfType(block.getWorld().getName(), "Survival"))
			return false;
		WauzRegion region = getNewRegion(block.getLocation());
		return region == null || !region.getFlags().contains(FLAG_BUILD);
	}
	
	public static boolean disallowBlockChange(Block block) {
		WauzRegion region = getNewRegion(block.getLocation());
		return region != null && region.getFlags().contains(FLAG_PROTECTED);
	}
	
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
	
	public static boolean isCombatZone(Location location) {
		WauzRegion region = getNewRegion(location);
		return region != null && region.getFlags().contains(FLAG_PVP);
	}
	
	public static WauzRegion getNewRegion(Location location) {
		WauzRegion result = null;
		for(WauzRegion masterRegion : masterRegions) {
			if(insideRegion(location, masterRegion))
				return getNewRegion(location, masterRegion, true);
		}
		return result;
	}
	
	private static WauzRegion getNewRegion(Location location, WauzRegion region, boolean knownToBeInRegion) {
		if(!knownToBeInRegion) {
			boolean inRegion = insideRegion(location, region);
			if(!inRegion)
				return region.getParent() != null
					? getNewRegion(location, region.getParent(), false)
					: getNewRegion(location);
		}
		for(WauzRegion subRegion : region.getChildren()) {
			if(insideRegion(location, subRegion))
				return getNewRegion(location, subRegion, true);
		}
		return region;
	}
	
	private static boolean insideRegion(Location location, WauzRegion region) {
		String type = region.getType();

		if(type.equals("world"))
			return insideWorldRegion(location, region.getWorld());
		else if(type.equals("polygon"))
			return insidePolygonRegion(location, region.getPolygon());
		else if(type.equals("ellipse"))
			return insideEllipseRegion(location, region.getEllipse());
		
		return false;
	}
	
	private static boolean insideWorldRegion(Location location, String world) {
		return location.getWorld().getName().equals(world);
	}
	
	private static boolean insidePolygonRegion(Location location, Polygon polygon) {
		return polygon.contains(location.getX() * 10, location.getZ() * 10);
	}
	
	private static boolean insideEllipseRegion(Location location, Ellipse2D ellipse) {
		return ellipse.contains(location.getBlock().getX(), location.getBlock().getZ());
	}
	
	private WauzRegion parent;
				
	private List<WauzRegion> children = new ArrayList<>();
	
	private String regionKey;
				
	private String type;
	
	private String world;
	
	private Polygon polygon;
	
	private Ellipse2D ellipse;
	
	private String title;
	
	private String subtitle;
	
	private String music;
	
	private byte temperature;
	
	private List<String> flags;
	
	public WauzRegion(WauzRegion parent, String regionKey) {
		this.regionKey = regionKey;
		this.parent = parent;
		
		title = RegionConfigurator.getTitle(regionKey);
		subtitle = RegionConfigurator.getSubtitle(regionKey);
		music = RegionConfigurator.getMusic(regionKey);
		temperature = RegionConfigurator.getTemperature(regionKey);
		flags = RegionConfigurator.getFlags(regionKey);
		
		type = RegionConfigurator.getType(regionKey);
		
		if(type.equals("world"))
			world = RegionConfigurator.getTypeWorld(regionKey);
		else if(type.equals("polygon"))
			polygon = RegionConfigurator.getTypePolygon(regionKey);
		else if(type.equals("ellipse"))
			ellipse = RegionConfigurator.getTypeEllipse(regionKey);
		
		for(String subRegionKey : RegionConfigurator.getSubRegionKeys(regionKey)) {
			children.add(new WauzRegion(this, regionKey + ".areas." + subRegionKey));
		}
	}
	
	public void enter(Player player) {
		WauzDebugger.log(player, "Entered Region: " + this);
		if(StringUtils.isNotBlank(title))
			player.sendTitle(ChatColor.GREEN + title, subtitle != null ? subtitle : "", 10, 70, 20);
		
		if(music != null)
			WauzNoteBlockPlayer.play(player, music);
		else
			WauzNoteBlockPlayer.play(player);
	}

	public WauzRegion getParent() {
		return parent;
	}

	public void setParent(WauzRegion parent) {
		this.parent = parent;
	}

	public List<WauzRegion> getChildren() {
		return children;
	}

	public void setChildren(List<WauzRegion> children) {
		this.children = children;
	}

	public String getRegionKey() {
		return regionKey;
	}

	public void setRegionKey(String regionKey) {
		this.regionKey = regionKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
	
	public Ellipse2D getEllipse() {
		return ellipse;
	}

	public void setEllipse(Ellipse2D ellipse) {
		this.ellipse = ellipse;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public byte getTemperature() {
		return temperature;
	}

	public void setTemperature(byte temperature) {
		this.temperature = temperature;
	}

	public List<String> getFlags() {
		return flags;
	}

	public void setFlags(List<String> flags) {
		this.flags = flags;
	}

	@Override
	public String toString() {
		return "(Region: " + regionKey.replace(".areas.", ":") + ")";
	}

}
