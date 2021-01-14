package eu.wauz.wauzcore.building;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * A block structure in the shape of a circle.
 * 
 * @author Wauzmons
 */
public class ShapeCircle {
	
	/**
	 * The center location of the circle.
	 */
	private final Location center;
	
	/**
	 * The radius of the circle.
	 */
	private final int radius;
    
    /**
     * If the circle should be hollow.
     */
    private final boolean hollow;
	
	/**
     * Constructs a block structure in the shape of a circle.
     * 
     * @param center The center location of the circle.
     * @param radius The radius of the circle.
     * @param hollow If the circle should be hollow.
     */
    public ShapeCircle(Location center, int radius, boolean hollow) {
        this.center = center;
        this.radius = radius;
        this.hollow = hollow;
    }
    
    /**
     * @return The center location of the circle.
     */
    public Location getCenter() {
    	return center;
    }
    
    /**
     * @return The radius of the circle.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * @return If the circle should be hollow.
     */
	public boolean isHollow() {
		return hollow;
	}

	public List<Block> create(Material material) {
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
