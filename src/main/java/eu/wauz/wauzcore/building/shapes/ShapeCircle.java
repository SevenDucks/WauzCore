package eu.wauz.wauzcore.building.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * A block structure blueprint in the shape of a circle.
 * 
 * @author Wauzmons
 */
public class ShapeCircle extends WauzShape {
	
    /**
     * If the circle should be hollow.
     */
    private final boolean hollow;
    
    /**
     * Empty default constructor.
     */
    public ShapeCircle() {
    	super();
    	this.hollow = false;
    }
	
	/**
     * Constructs a block structure blueprint in the shape of a circle.
     * 
     * @param radius The radius of the circle.
     * @param height The height of the circle.
     * @param hollow If the circle should be hollow.
     */
    public ShapeCircle(int radius, int height, boolean hollow) {
    	super(radius, height);
        this.hollow = hollow;
    }
    
    /**
     * @return If the circle should be hollow.
     */
	public boolean isHollow() {
		return hollow;
	}

	/**
	 * Creates the shape as ingame block structure.
	 * 
	 * @param center The center location of the circle.
	 * @param material The block material.
	 * 
	 * @return The affected blocks.
	 */
	public List<Block> create(Location center, Material material) {
		List<Block> blocks = new ArrayList<>();
		World world = center.getWorld();
		Vector vector = new BlockVector(center.getX(), center.getY(), center.getZ());
		for(int x = -radius; x <= radius; x++) {
			for(int z = -radius; z <= radius; z++) {
				Vector position = vector.clone().add(new Vector(x, 0, z));
				double distance = vector.distance(position);
				if(distance <= radius + 0.5) {
					if(isHollow() && distance <= radius - 0.5) {
						continue;
					}
					Block block = world.getBlockAt(position.getBlockX(), position.getBlockY(), position.getBlockZ());
					block.setType(material);
    				blocks.add(block);
				}
			}
		}
		return blocks;
	}

}
