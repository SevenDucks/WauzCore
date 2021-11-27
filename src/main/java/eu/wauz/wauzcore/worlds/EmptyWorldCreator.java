package eu.wauz.wauzcore.worlds;

import java.util.Collections;
import java.util.List;

import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

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
		biomeProvider(new BiomeProvider() {
			
			@Override
			public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
				return Collections.singletonList(Biome.FOREST);
			}
			
			@Override
			public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
				return Biome.FOREST;
			}
			
		});
		generator(new ChunkGenerator() {
			
			@Override
			public boolean shouldGenerateBedrock() {
				return false;
			}
			
			@Override
			public boolean shouldGenerateCaves() {
				return false;
			}
			
			@Override
			public boolean shouldGenerateDecorations() {
				return false;
			}
			
			@Override
			public boolean shouldGenerateMobs() {
				return false;
			}
			
			@Override
			public boolean shouldGenerateNoise() {
				return false;
			}
			
			@Override
			public boolean shouldGenerateStructures() {
				return false;
			}
			
			@Override
			public boolean shouldGenerateSurface() {
				return false;
			}
			
		});
	}

}
