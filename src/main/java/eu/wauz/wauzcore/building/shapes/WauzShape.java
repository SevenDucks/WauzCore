package eu.wauz.wauzcore.building.shapes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import eu.wauz.wauzcore.system.nms.NmsChunkUpdate;

/**
 * A block structure blueprint in an abstract shape.
 * 
 * @author Wauzmons
 */
public abstract class WauzShape {
	
	/**
	 * The radius of the shape.
	 */
	protected final int radius;
	
	/**
	 * The height of the shape.
	 */
	protected final int height;
	
	/**
     * Constructs a block structure blueprint in an abstract shape.
     * 
     * @param radius The radius of the hexagon.
     * @param height The height of the hexagon.
     */
    public WauzShape(int radius, int height) {
        this.radius = radius;
        this.height = height;
    }
    
    /**
     * Changes the material of the blocks within a selection in this shape.
     * 
     * @param center The center location of the selection.
     * @param material The material to set.
     * 
     * @return The affected blocks.
     */
    public List<Block> setMaterial(Location center, Material material) {
    	List<Block> blocks = select(center);
    	for(Block block : blocks) {
    		block.setType(material);
    	}
    	return blocks;
    }
    
    /**
     * Changes the material of the blocks within a selection in this shape.
     * 
     * @param center The center location of the selection.
     * @param biome The biome to set.
     * 
     * @return The affected blocks.
     */
    public List<Block> setBiome(Location center, Biome biome) {
    	Set<Chunk> chunks = new HashSet<>();
    	List<Block> blocks = select(center);
    	for(Block block : blocks) {
    		block.setBiome(biome);
    		chunks.add(block.getChunk());
    	}
    	for(Chunk chunk : chunks) {
    		NmsChunkUpdate.init(chunk);
    	}
    	return blocks;
    }
    
    /**
     * @return The radius of the circle.
     */
    public int getRadius() {
        return radius;
    }
    
    /**
     * @return The height of the circle.
     */
    public int getHeight() {
    	return height;
    }
    
    /**
     * @return The height of the circle as string.
     */
    public String getHeightString() {
    	return height >= 0 ? String.valueOf(height) : "Max";
    }
    
    /**
     * Creates a selection in this shape.
     * 
     * @param center The center location of the selection.
     * 
     * @return The selected blocks.
     */
    protected abstract List<Block> select(Location center);
    
    /**
     * Gets a vertical stack of blocks, based on the shape's height.
     * 
     * @param baseBlock The block at the base of the stack.
     * 
     * @return The stack of blocks.
     */
    protected List<Block> getVerticalBlockStack(Block baseBlock) {
    	int maxHeight = height;
    	if(height < 0) {
    		maxHeight = baseBlock.getWorld().getMaxHeight();
    		
    		Location location = baseBlock.getLocation();
			location.setY(0);
    		baseBlock = location.getBlock();
    	}
    	
    	List<Block> blocks = new ArrayList<>();
    	for(int y = 0; y < maxHeight; y++) {
			Block block = y == 0 ? baseBlock : baseBlock.getRelative(BlockFace.UP, y);
			if(block.getY() >= block.getWorld().getMaxHeight()) {
				break;
			}
			blocks.add(block);
		}
    	return blocks;
    }

}
