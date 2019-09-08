package eu.wauz.wauzcore.skills.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import net.md_5.bungee.api.ChatColor;

public class SkillUtils {
	
	public static boolean isValidAttackTarget(Entity entity) {
		boolean isNoArmorStand = !entity.getType().equals(EntityType.ARMOR_STAND);
		boolean hasLevel = entity.getCustomName() != null && entity.getCustomName().contains("" + ChatColor.AQUA);
		return isNoArmorStand && hasLevel;
	}
	
	public static Entity getTargetInLine(Player player, int range) {
		Entity target = null;
		for(Block block : player.getLineOfSight(null, range)) {
			Collection<Entity> nearbyEntites = block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1);
			for(Entity entity : nearbyEntites) {
				if(isValidAttackTarget(entity)) {
					target = entity;
					break;
				}
			}
		}
		return target;
	}
	
	public static List<Entity> getTargetsInLine(Player player, int range) {
		List<Entity> targets = new ArrayList<>();
		for(Block block : player.getLineOfSight(null, range)) {
			Collection<Entity> nearbyEntites = block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1);
			for(Entity entity : nearbyEntites) {
				if(isValidAttackTarget(entity)) {
					targets.add(entity);
				}
			}
		}
		return targets;
	}
	
	public static List<Entity> getTargetsInRadius(Location location, double radius) {
		return getTargetsInRadius(location, radius, new ArrayList<Entity>());
	}
	
	public static List<Entity> getTargetsInRadius(Location location, double radius, List<Entity> excludes) {
		List<Entity> targets = new ArrayList<>();
		for(Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
			if(isValidAttackTarget(entity) && !excludes.contains(entity)) {
				targets.add(entity);
			}
		}
		return targets;
	}
	
	public static List<Player> getPlayersInRadius(Location location, int radius) {
		List<Player> players = new ArrayList<>();
		for(Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
			if(entity instanceof Player) {
				players.add((Player) entity);
			}
		}
		return players;
	}
	
	public static void addPotionEffect(List<Entity> entities, PotionEffectType potionEffectType, int duration, int amplifier) {
		for(Entity entity : entities) {
			addPotionEffect(entity, potionEffectType, duration, amplifier);
		}
	}
	
	public static void addPotionEffect(Entity entity, PotionEffectType potionEffectType,  int duration, int amplifier) {
		if(entity instanceof LivingEntity) {
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration * 20, amplifier);
			((LivingEntity) entity).addPotionEffect(potionEffect, true);
		}
	}
	
	public static void callPlayerMagicDamageEvent(Player player, List<Entity> entities, double damageMultiplier) {
		for(Entity entity : entities)
			callPlayerMagicDamageEvent(player, entity, damageMultiplier);
	}
	
	public static void callPlayerMagicDamageEvent(Player player, Entity entity, double damageMultiplier) {	
		if(entity instanceof Damageable && player.getWorld().equals(entity.getWorld())) {
			Damageable damagable = (Damageable) entity;
			damagable.setMetadata("wzMagic", new FixedMetadataValue(WauzCore.getInstance(), damageMultiplier));
			damagable.damage(10, player);
		}
	}
	
	public static void callPlayerFixedDamageEvent(Player player, List<Entity> entities, double damage) {
		for(Entity entity : entities)
			callPlayerFixedDamageEvent(player, entity, damage);
	}
	
	public static void callPlayerFixedDamageEvent(Player player, Entity entity, double damage) {
		if(entity instanceof Damageable && player.getWorld().equals(entity.getWorld())) {
			Damageable damageable = (Damageable) entity;
			damageable.setMetadata("wzFixedDmg", new FixedMetadataValue(WauzCore.getInstance(), true));
			damageable.damage(damage, player);
		}
	}
	
	public static void callPlayerDamageOverTimeEvent(Player player, Entity entity, Color color, int damage, int ticks, int interval) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        public void run() {
	        	try {
	        		if(player != null && player.isValid() && entity != null && entity.isValid()) {
	        			ParticleSpawner.spawnParticleHelix(entity.getLocation(), new SkillParticle(color), 0.5, 2.5);
	        			if(damage > 0)
	        				callPlayerFixedDamageEvent(player, entity, damage);
	        			if(ticks - 1 > 0)
	        				callPlayerDamageOverTimeEvent(player, entity, color, damage, ticks - 1, interval);
	        		}
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
	        
		}, interval);
	}
	
	public static void spawnTotem(Player owner, Material material, TotemRunnable runnable, int ticks, int interval) {
		Entity totem = WauzNmsClient.nmsCustomEntityTotem(owner, new ItemStack(material));
		callTotemEvent(totem, runnable, ticks, interval);
	}
	
	private static void callTotemEvent(Entity totem, TotemRunnable runnable, int ticks, int interval) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        public void run() {
	        	try {
	        		if(totem != null && totem.isValid()) {
	        			runnable.run(totem);
	        			if(ticks - 1 > 0)
	        				callTotemEvent(totem, runnable, ticks - 1, interval);
	        			else
	        				totem.remove();
	        		}
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
	        
		}, interval);
	}
	
	public static interface TotemRunnable {
		
		public abstract void run(Entity totem);
		
	}
	
	public static void createExplosion(Location location, float power) {
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power, false, false);
	}
	
	public static void throwEntity(Entity entity, Entity target) {
		Vector vector = getVectorForPoints(entity.getLocation(), target.getLocation());
		entity.teleport(entity.getLocation().add(0, 0.5, 0));
		entity.setVelocity(vector);	
	}
	
	public static void throwEntity(Entity entity, Location location) {
		Vector vector = getVectorForPoints(entity.getLocation(), location);
		entity.teleport(entity.getLocation().add(0, 0.5, 0));
		entity.setVelocity(vector);
	}
	
	public static void throwEntity(Entity entity, double x, double y, double z) {
		Vector vector = getVectorForPoints(entity.getLocation(), new Location(entity.getWorld(), x, y, z));
		entity.teleport(entity.getLocation().add(0, 0.5, 0));
		entity.setVelocity(vector);
	}
	
	public static void throwEntitiesIntoAir(List<Entity> entities, double force) {
		for(Entity entity : entities)
			throwEntityIntoAir(entity, force);
	}
	
	public static void throwEntityIntoAir(Entity entity, double force) {
		Vector vector = new Vector(0, force, 0);
		entity.setVelocity(vector);
	}
	
	public static void throwBackEntity(Entity entity, Location fromLocation, double intensity) {
		Vector vector = getThrowbackVector(entity.getLocation(), fromLocation, intensity);
		entity.setVelocity(vector);
	}
	
    public static Vector getVectorForPoints(Location l1, Location l2) {
		double g = -0.08;
		double d = l2.distance(l1);
		double t = d;
		double vX = (1.0+0.07*t) * (l2.getX() - l1.getX())/t;
		double vY = (1.0+0.03*t) * (l2.getY() - l1.getY())/t - 0.5*g*t;
		double vZ = (1.0+0.07*t) * (l2.getZ() - l1.getZ())/t;
		return new Vector(vX, vY, vZ);
    }
    
    public static Vector getThrowbackVector(Location l1, Location l2, double intensity) {
		double d = l2.distance(l1);
		double t = d;
		double vX = (intensity / t) * (l1.getX() - l2.getX());
		double vZ = (intensity / t) * (l1.getZ() - l2.getZ());
		return new Vector(vX, intensity / 3.0, vZ);
    }
	
//	public static final Vector rotateVectorAroundAxisX(Vector v, double angle) {
//		double y, z, cos, sin;
//		cos = Math.cos(angle);
//		sin = Math.sin(angle);
//		y = v.getY() * cos - v.getZ() * sin;
//		z = v.getY() * sin + v.getZ() * cos;
//		return v.setY(y).setZ(z);
//	}
//	
//	public static final Vector rotateVectorAroundAxisY(Vector v, double angle) {
//		double x, z, cos, sin;
//		cos = Math.cos(angle);
//		sin = Math.sin(angle);
//		x = v.getX() * cos + v.getZ() * sin;
//		z = v.getX() * -sin + v.getZ() * cos;
//		return v.setX(x).setZ(z);
//	}
//	
//	public static final Vector rotateVectorAroundAxisZ(Vector v, double angle) {
//		double x, y, cos, sin;
//		cos = Math.cos(angle);
//		sin = Math.sin(angle);
//		x = v.getX() * cos - v.getY() * sin;
//		y = v.getX() * sin + v.getY() * cos;
//		return v.setX(x).setY(y);
//	}

}
