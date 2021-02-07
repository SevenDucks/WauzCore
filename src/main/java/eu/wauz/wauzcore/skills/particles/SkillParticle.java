package eu.wauz.wauzcore.skills.particles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

/**
 * A template for a particle, that can be spawned manually or by a particle spawner.
 * 
 * @author Wauzmons
 *
 * @see ParticleSpawner
 */
public class SkillParticle {
	
	/**
	 * The Minecraft particle type.
	 */
	Particle particle;
	
	/**
	 * The color for custom colored dust particles.
	 */
	Color color;
	
	/**
	 * Creates a particle based on a Minecraft particle.
	 * 
	 * @param particle The Minecraft particle type.
	 */
	public SkillParticle(Particle particle) {
		this.particle = particle;
	}
	
	/**
	 * Creates a particle based on a custom color.
	 * 
	 * @param color The color for custom colored dust particles.
	 */
	public SkillParticle(Color color) {
		this.color = color;
	}
	
	/**
	 * Spawns a specific amount of this particle at the given location.
	 * 
	 * @param location Where the particles should spawn.
	 * @param amount How many particles should spawn.
	 */
	public void spawn(Location location, int amount) {
		if(particle != null) {
			spawnParticles(location, particle, amount);
		}
		else {
			spawnParticles(location, color, amount);
		}
	}
	
	/**
	 * Spawns a specific amount of this particle for a player at the given location.
	 * 
	 * @param player The player who should see the particles.
	 * @param location Where the particles should spawn.
	 * @param amount How many particles should spawn.
	 */
	public void spawn(Player player, Location location, int amount) {
		if(particle != null) {
			spawnParticles(player, location, particle, amount);
		}
		else {
			spawnParticles(player, location, color, amount);
		}
	}
	
	/**
	 * Spawns a specific amount of a Minecraft particle at the given location.
	 * 
	 * @param location Where the particles should spawn.
	 * @param particle The Minecraft particle type.
	 * @param amount How many particles should spawn.
	 */
	public static void spawnParticles(Location location, Particle particle, int amount) {
		location.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, 0, 0, 0, 0.1);
	}
	
	/**
	 * Spawns a specific amount of a custom colored particle at the given location.
	 * 
	 * @param location Where the particles should spawn.
	 * @param color The color for custom colored dust particles.
	 * @param amount How many particles should spawn.
	 */
	public static void spawnParticles(Location location, Color color, int amount) {
		location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, 0, 0, 0, 0.1, new DustOptions(color, 3));
	}
	
	/**
	 * Spawns a specific amount of a Minecraft particle for a player at the given location.
	 * 
	 * @param player The player who should see the particles.
	 * @param location Where the particles should spawn.
	 * @param particle The Minecraft particle type.
	 * @param amount How many particles should spawn.
	 */
	public static void spawnParticles(Player player, Location location, Particle particle, int amount) {
		player.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), amount, 0, 0, 0, 0.1);
	}
	
	/**
	 * Spawns a specific amount of a custom colored particle for a player at the given location.
	 * 
	 * @param player The player who should see the particles.
	 * @param location Where the particles should spawn.
	 * @param color The color for custom colored dust particles.
	 * @param amount How many particles should spawn.
	 */
	public static void spawnParticles(Player player, Location location, Color color, int amount) {
		player.spawnParticle(Particle.REDSTONE, location, amount, 0, 0, 0, 0.1, new DustOptions(color, 3));
	}

}
