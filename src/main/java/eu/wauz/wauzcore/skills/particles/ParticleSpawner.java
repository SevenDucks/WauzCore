package eu.wauz.wauzcore.skills.particles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;

public class ParticleSpawner {

	public static void spawnParticleLine(Location origin, Location target, SkillParticle particle, int amount) {
		spawnParticleLine(origin, target, particle, amount, 1);
	}

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

	public static void spawnParticleHelix(Location origin, SkillParticle particle, double radius, double height) {
		for(double y = 0; y <= height; y += 0.1) {
			double x = radius * Math.cos(y * 5);
			double z = radius * Math.sin(y * 5);
			
			Location location = new Location(origin.getWorld(), origin.getX() + x, origin.getY() + y, origin.getZ() + z);
			particle.spawn(location, 1);
		}
	}

	public static void spawnParticleWave(Location origin, SkillParticle particle, double length){
		new BukkitRunnable() {
			
			double t = Math.PI / 4;
			
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

	public static void spawnParticleSphere(Location origin, SkillParticle particle, double radius) {
		new BukkitRunnable() {
			
			double phi = 0;
			
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
