package eu.wauz.wauzcore.building.shapes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
    	List<Block> blocks = select(center);
    	for(Block block : blocks) {
    		block.setBiome(biome);
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
    	List<Block> blocks = new ArrayList<>();
    	for(int y = 1; y <= height; y++) {
			Block block = y == 0 ? baseBlock : baseBlock.getRelative(BlockFace.UP, y);
			if(block.getY() >= block.getWorld().getMaxHeight()) {
				break;
			}
			blocks.add(block);
		}
    	return blocks;
    }

}
