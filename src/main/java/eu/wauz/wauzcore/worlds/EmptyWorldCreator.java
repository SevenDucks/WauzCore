package eu.wauz.wauzcore.worlds;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

/**
 * Represents various types of options that may be used to create an empty world.
 * 
 * @author Wauzmons
 */
public class EmptyWorldCreator extends WorldCreator {

	/**
	 * Creates an empty world creator for the given world name.
	 * 
	 * @param name The name of the world that will be created.
	 */
	public EmptyWorldCreator(String name) {
		super(name);
		generator(new ChunkGenerator() {
			
			@Override
			public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
				for(int x = 0; x < 16; x++) {
					for(int y = 0; y < 256; y++) {
						for(int z = 0; z < 16; z++) {
							biome.setBiome(x, y, z, Biome.FOREST);
						}
					}
				}
				return createChunkData(world);
			}
			
		});
	}

}
