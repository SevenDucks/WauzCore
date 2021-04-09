package eu.wauz.wauzcore.skills.particles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;

/**
 * A collection of methods to spawn particles in different shapes and animations.
 * 
 * @author Wauzmons
 *
 * @see SkillParticle
 */
public class ParticleSpawner {

	/**
	 * Creates a line of particles between two locations.
	 * 
	 * @param origin Where the particles should originate.
	 * @param target Where the particles should lead to.
	 * @param particle The type of particle to spawn.
	 * @param amount The amount of particles to spawn.
	 */
	public static void spawnParticleLine(Location origin, Location target, SkillParticle particle, int amount) {
		spawnParticleLine(origin, target, particle, amount, 1);
	}

	/**
	 * Creates a line of particles between two locations.
	 * 
	 * @param origin Where the particles should originate.
	 * @param target Where the particles should lead to.
	 * @param particle The type of particle to spawn.
	 * @param amount The amount of particles to spawn.
	 * @param spacing The spacing between particles.
	 */
	public static void spawnParticleLine(Location origin, Location target, SkillParticle particle, int amount, double spacing) {
		Vector originVector = origin.toVector();
		Vector targetVector = target.toVector();
		Vector addingVector = targetVector.clone().subtract(originVector).normalize().multiply(spacing);
		
		double distance = origin.distance(target);
		double length = 0;
		
		while(length < distance) {
			originVector.add(addingVector);
			particle.spawn(originVector.toLocation(origin.getWorld()), amount);
			length += spacing;
		}
	}

	/**
	 * Creates a circle of particles at a specific location.
	 * 
	 * @param origin Where the particles should originate.
	 * @param particle The type of particle to spawn.
	 * @param radius The radius of the circle.
	 * @param amount The amount of particles to spawn.
	 */
	public static void spawnParticleCircle(Location origin, SkillParticle particle, double radius, int amount) {
		World world = origin.getWorld();
	    double increment = (2 * Math.PI) / amount;
	    for(int iterator = 0; iterator < amount; iterator++)
	    {
	        double angle = iterator * increment;
	        double x = origin.getX() + (radius * Math.cos(angle));
	        double z = origin.getZ() + (radius * Math.sin(angle));
	        particle.spawn(new Location(world, x, origin.getY() + 0.5, z), 1);
	    }
	}
	
	/**
	 * Creates a circle of particles for a player at a specific location.
	 * 
	 * @param player The player who should see the particles.
	 * @param origin Where the particles should originate.
	 * @param particle The type of particle to spawn.
	 * @param radius The radius of the circle.
	 * @param amount The amount of particles to spawn.
	 */
	public static void spawnParticleCircle(Player player, Location origin, SkillParticle particle, double radius, int amount) {
		World world = origin.getWorld();
	    double increment = (2 * Math.PI) / amount;
	    for(int iterator = 0; iterator < amount; iterator++)
	    {
	        double angle = iterator * increment;
	        double x = origin.getX() + (radius * Math.cos(angle));
	        double z = origin.getZ() + (radius * Math.sin(angle));
	        particle.spawn(player, new Location(world, x, origin.getY() + 0.5, z), 1);
	    }
	}

	/**
	 * Creates a helix of particles at a specific location.
	 * 
	 * @param origin Where the particles should originate.
	 * @param particle The type of particle to spawn.
	 * @param radius The radius of the helix.
	 * @param height The amount of particles to spawn.
	 */
	public static void spawnParticleHelix(Location origin, SkillParticle particle, double radius, double height) {
		for(double y = 0; y <= height; y += 0.1) {
			double x = radius * Math.cos(y * 5);
			double z = radius * Math.sin(y * 5);
			
			Location location = new Location(origin.getWorld(), origin.getX() + x, origin.getY() + y, origin.getZ() + z);
			particle.spawn(location, 1);
		}
	}

	/**
	 * Creates an animated wave of particles at a specific location.
	 * 
	 * @param origin Where the particles should originate.
	 * @param particle The type of particle to spawn.
	 * @param length The length of the wave.
	 */
	public static void spawnParticleWave(Location origin, SkillParticle particle, double length){
		new BukkitRunnable() {
			
			private double t = Math.PI / 4;
			
			@Override
			public void run(){
				t = t + 0.1 * Math.PI;
				for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 8) {
					double x = t * Math.cos(theta);
					double y = 2 * Math.exp(- 0.1 * t) * Math.sin(t) + 1.5;
					double z = t * Math.sin(theta);
					origin.add(x, y, z);
					particle.spawn(origin, 1);
					origin.subtract(x, y, z);
				}
				if (t > length) {
					this.cancel();
				}
			}
			
		}.runTaskTimer(WauzCore.getInstance(), 0, 1);
	}

	/**
	 * Creates an animated sphere of particles at a specific location.
	 * 
	 * @param origin Where the particles should originate.
	 * @param particle The type of particle to spawn.
	 * @param radius The radius of the sphere.
	 */
	public static void spawnParticleSphere(Location origin, SkillParticle particle, double radius) {
		new BukkitRunnable() {
			
			private double phi = 0;
			
			@Override
			public void run() {
				phi += Math.PI / 10;
				for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 20) {
					double x = radius * Math.cos(theta) * Math.sin(phi);
					double y = radius * Math.cos(phi) + 1.5;
					double z = radius * Math.sin(theta) * Math.sin(phi);
					origin.add(x, y, z);
					particle.spawn(origin, 1);
					origin.subtract(x, y, z);
				}
				if(phi > 2 * Math.PI) {
					this.cancel();
				}
			}
		}.runTaskTimer(WauzCore.getInstance(), 0, 1);
	}

}
