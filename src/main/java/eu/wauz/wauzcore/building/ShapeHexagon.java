package eu.wauz.wauzcore.building;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * A block structure in the shape of a hexagon.
 * 
 * @author Wauzmons
 */
public class ShapeHexagon {
	
	/**
	 * The center location of the hexagon.
	 */
	private final Location center;
	
	/**
	 * The radius of the hexagon.
	 */
	private final int radius;

    /**
     * The calculated polygon object.
     */
    private final Path2D.Double polygon;

    /**
     * Constructs a block structure in the shape of a hexagon.
     * 
     * @param center The center location of the hexagon.
     * @param radius The radius of the hexagon.
     */
    public ShapeHexagon(Location center, int radius) {
        this.center = center;
        this.radius = radius;
        this.polygon = createHexagon();
    }

    private Path2D.Double createHexagon() {
    	Path2D.Double polygon = null;
        for (int point = 0; point < 6; point++) {
        	double xval = (double) center.getBlockX() + (double) radius * Math.cos(point * 2 * Math.PI / 6D);
        	double yval = (double) center.getBlockZ() + (double) radius * Math.sin(point * 2 * Math.PI / 6D);
        	if(polygon == null) {
        		polygon = new Path2D.Double();
        		polygon.moveTo(xval, yval);
        	}
        	else {
        		polygon.lineTo(xval, yval);
        	}
        }
        polygon.closePath();
        return polygon;
    }
    
    /**
     * @return The center location of the hexagon.
     */
    public Location getCenter() {
    	return center;
    }

    /**
     * @return The radius of the hexagon.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return The calculated polygon object.
     */
    public Path2D.Double getPolygon() {
        return polygon;
    }
    
    /**
     * Creates the shape as ingame block structure.
     * 
     * @param material The block material.
     * 
     * @return The affected blocks.
     */
    public List<Block> create(Material material) {
    	List<Block> blocks = new ArrayList<>();
    	for(int x = -radius; x <= radius; x++) {
    		for(int z = -radius; z <= radius; z++) {
    			Block block = center.clone().add(x, 0, z).getBlock();
    			if(polygon.intersects(block.getX(), block.getZ(), 1, 1)) {
    				block.setType(material);
    				blocks.add(block);
    			}
    		}
    	}
    	return blocks;
    }

}
