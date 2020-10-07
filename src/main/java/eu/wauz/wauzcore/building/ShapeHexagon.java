package eu.wauz.wauzcore.building;

import java.awt.Polygon;
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
	 * The radius of the hexagon.
	 */
	private final int radius;

	/**
	 * The center location of the hexagon.
	 */
    private final Location center;

    /**
     * The calculated polygon object.
     */
    private final Polygon polygon;

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

    private Polygon createHexagon() {
        Polygon polygon = new Polygon();
        for (int point = 0; point < 6; point++) {
            int xval = (int) (center.getBlockX() + radius * Math.cos(point * 2 * Math.PI / 6D));
            int yval = (int) (center.getBlockZ() + radius * Math.sin(point * 2 * Math.PI / 6D));
            polygon.addPoint(xval, yval);
        }
        return polygon;
    }

    /**
     * @return The radius of the hexagon.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return The center location of the hexagon.
     */
    public Location getCenter() {
        return center;
    }

    /**
     * @return The calculated polygon object.
     */
    public Polygon getPolygon() {
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
    			if(polygon.intersects(block.getX() - 0.5, block.getZ() - 0.5, 1, 1)) {
    				block.setType(material);
    				blocks.add(block);
    			}
    		}
    	}
    	return blocks;
    }

}
