package eu.wauz.wauzcore.building.shapes;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * A block structure blueprint in the shape of a hexagon.
 * 
 * @author Wauzmons
 */
public class ShapeHexagon extends WauzShape {
	
    /**
     * Constructs a block structure blueprint in the shape of a hexagon.
     * 
     * @param radius The radius of the hexagon.
     * @param height The height of the hexagon.
     */
    public ShapeHexagon(int radius, int height) {
    	super(radius, height);
    }

    /**
     * Creates a selection in this shape.
     * 
     * @param center The center location of the selection.
     * 
     * @return The selected blocks.
     */
    @Override
    protected List<Block> select(Location center) {
    	Path2D.Double polygon = createHexagon(center);
    	List<Block> blocks = new ArrayList<>();
    	for(int x = -radius; x <= radius; x++) {
    		for(int z = -radius; z <= radius; z++) {
    			Block block = center.clone().add(x, 0, z).getBlock();
    			if(polygon.intersects(block.getX(), block.getZ(), 1, 1)) {
    				blocks.addAll(getVerticalBlockStack(block));
    			}
    		}
    	}
    	return blocks;
    }
    
    /**
     * Creates a hexagonal polygon.
     * 
     * @param center The center location of the hexagon.
     * 
     * @return The hexagon.
     */
    private Path2D.Double createHexagon(Location center) {
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

}
