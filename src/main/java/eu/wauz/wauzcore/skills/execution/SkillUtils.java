package eu.wauz.wauzcore.skills.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.nms.NmsEntityTotem;

/**
 * An util class for working with skill and weapon meachanics.
 * 
 * @author Wauzmons
 */
public class SkillUtils {
	
	/**
	 * Determines if given entity should be targetable.
	 * This is defined by the aqua color for the levels in their name.
	 * Armor stands are never classified as attack targets.
	 * 
	 * @param entity The entity to target.
	 * 
	 * @return If the entity is targetable.
	 */
	public static boolean isValidAttackTarget(Entity entity) {
		boolean isNoArmorStand = !entity.getType().equals(EntityType.ARMOR_STAND);
		boolean hasLevel = entity.getCustomName() != null && entity.getCustomName().contains("" + ChatColor.AQUA);
		return isNoArmorStand && hasLevel;
	}
	
	/**
	 * Determines the first target in the line of sight of the player.
	 * 
	 * @param player The player that is targeting.
	 * @param range The range of the line in blocks.
	 * 
	 * @return The target, or null if no valid target was found.
	 */
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
	
	/**
	 * Determines all targets in the line of sight of the player.
	 * 
	 * @param player The player that is targeting.
	 * @param range The range of the line in blocks.
	 * 
	 * @return The list of targets. Can be empty.
	 */
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
	
	/**
	 * Determines all targets in the radius of the given location.
	 * 
	 * @param location The center to check around for targets.
	 * @param radius The radius from the center in blocks.
	 * 
	 * @return The list of targets. Can be empty.
	 */
	public static List<Entity> getTargetsInRadius(Location location, double radius) {
		return getTargetsInRadius(location, radius, new ArrayList<Entity>());
	}
	
	/**
	 * Determines all targets in the radius of the given location.
	 * 
	 * @param location The center to check around for targets.
	 * @param radius The radius from the center in blocks.
	 * @param excludes Entities that should be ignored.
	 * 
	 * @return The list of targets. Can be empty.
	 */
	public static List<Entity> getTargetsInRadius(Location location, double radius, List<Entity> excludes) {
		List<Entity> targets = new ArrayList<>();
		for(Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
			if(isValidAttackTarget(entity) && !excludes.contains(entity)) {
				targets.add(entity);
			}
		}
		return targets;
	}
	
	/**
	 * Determines all players in the radius of the given location.
	 * 
	 * @param location The center to check around for players.
	 * @param radius The radius from the center in blocks.
	 * 
	 * @return The list of players. Can be empty.
	 */
	public static List<Player> getPlayersInRadius(Location location, int radius) {
		List<Player> players = new ArrayList<>();
		for(Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
			if(entity instanceof Player) {
				players.add((Player) entity);
			}
		}
		return players;
	}
	
	/**
	 * Adds the given potion effect to multiple entities.
	 * 
	 * @param entities The entities to receive the potion effect.
	 * @param potionEffectType The type of the effect.
	 * @param duration The duration of the effect in seconds.
	 * @param amplifier The strength of the effect. (Starts at 0)
	 */
	public static void addPotionEffect(List<Entity> entities, PotionEffectType potionEffectType, int duration, int amplifier) {
		for(Entity entity : entities) {
			addPotionEffect(entity, potionEffectType, duration, amplifier);
		}
	}
	
	/**
	 * Adds the given potion effect to an entity.
	 * 
	 * @param entity The entity to receive the potion effect.
	 * @param potionEffectType The type of the effect.
	 * @param duration The duration of the effect in seconds.
	 * @param amplifier The strength of the effect. (Starts at 0)
	 */
	public static void addPotionEffect(Entity entity, PotionEffectType potionEffectType,  int duration, int amplifier) {
		if(entity instanceof LivingEntity) {
			PotionEffect potionEffect = new PotionEffect(potionEffectType, duration * 20, amplifier);
			((LivingEntity) entity).addPotionEffect(potionEffect);
		}
	}
	
	/**
	 * Calls a damage event with a damage multiplier, for magic attacks, hitting multiple enemies.
	 * 
	 * @param player The player who casted the attack.
	 * @param entities The entities that got hit by the attack.
	 * @param damageMultiplier The magic multiplier of the damage.
	 * 
	 * @see SkillUtils#callPlayerMagicDamageEvent(Player, Entity, double)
	 */
	public static void callPlayerMagicDamageEvent(Player player, List<Entity> entities, double damageMultiplier) {
		for(Entity entity : entities) {
			callPlayerMagicDamageEvent(player, entity, damageMultiplier);
		}
	}
	
	/**
	 * Calls a damage event with a damage multiplier, for magic attacks, hitting a single enemy.
	 * 
	 * @param player The player who casted the attack.
	 * @param entity The entity that got hit by the attack.
	 * @param damageMultiplier The magic multiplier of the damage.
	 * 
	 * @see MobMetadataUtils#setMagicDamageMultiplier(Entity, double)
	 */
	public static void callPlayerMagicDamageEvent(Player player, Entity entity, double damageMultiplier) {	
		if(entity instanceof Damageable && player.getWorld().equals(entity.getWorld())) {
			Damageable damagable = (Damageable) entity;
			MobMetadataUtils.setMagicDamageMultiplier(damagable, damageMultiplier);
			damagable.damage(10, player);
		}
	}
	
	/**
	 * Calls a damage event, that will not receive attack bonuses, hittting multiple enemies.
	 * 
	 * @param player The player who casted the attack.
	 * @param entity The entity that got hit by the attack.
	 * @param damage The fixed damage value.
	 * 
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 */
	public static void callPlayerFixedDamageEvent(Player player, List<Entity> entities, double damage) {
		for(Entity entity : entities) {
			callPlayerFixedDamageEvent(player, entity, damage);
		}
	}
	
	/**
	 * Calls a damage event, that will not receive attack bonuses, hittting a single enemy.
	 * 
	 * @param player The player who casted the attack.
	 * @param entity The entity that got hit by the attack.
	 * @param damage The fixed damage value.
	 * 
	 * @see MobMetadataUtils#setFixedDamage(Entity, boolean)
	 */
	public static void callPlayerFixedDamageEvent(Player player, Entity entity, double damage) {
		if(entity instanceof Damageable && player.getWorld().equals(entity.getWorld())) {
			Damageable damageable = (Damageable) entity;
			MobMetadataUtils.setFixedDamage(damageable, true);
			damageable.damage(damage, player);
		}
	}
	
	/**
	 * Calls a damage event, that deals damage based on a specified interval.
	 * 
	 * @param player The player who casted the attack.
	 * @param entity The entity that got hit by the attack.
	 * @param color The color of the damage particles.
	 * @param damage The fixed damage value.
	 * @param ticks How often the damage should be dealt.
	 * @param interval The server ticks between each damage.
	 */
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
	
	/**
	 * Spawns a totem to execute a runnable based on a specified interval.
	 * 
	 * @param owner The player who summoned the totem.
	 * @param material The material of the totem head.
	 * @param runnable The runnable that should be executed.
	 * @param ticks How often the runnable should be executed.
	 * @param interval The server ticks between each execution.
	 */
	public static void spawnTotem(Player owner, Material material, TotemRunnable runnable, int ticks, int interval) {
		Entity totem = NmsEntityTotem.create(owner, new ItemStack(material));
		callTotemEvent(totem, runnable, ticks, interval);
	}
	
	/**
	 * Spawns a tower (big totem) to execute a runnable based on a specified interval.
	 * 
	 * @param owner The player who summoned the tower.
	 * @param headItemStack The head of thr tower.
	 * @param bodyItemStack The body of the tower.
	 * @param runnable The runnable that should be executed.
	 * @param ticks How often the runnable should be executed.
	 * @param interval The server ticks between each execution.
	 */
	public static void spawnTower(Player owner, ItemStack headItemStack, ItemStack bodyItemStack, TotemRunnable runnable, int ticks, int interval) {
		Entity tower = NmsEntityTotem.create(owner, headItemStack, bodyItemStack);
		callTotemEvent(tower, runnable, ticks, interval);
	}
	
	/**
	 * Calls a the totem runnable for an existing totem.
	 * 
	 * @param totem The summoned totem.
	 * @param runnable The runnable that should be executed.
	 * @param ticks How often the runnable should be executed.
	 * @param interval The server ticks between each execution.
	 */
	private static void callTotemEvent(Entity totem, TotemRunnable runnable, int ticks, int interval) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        public void run() {
	        	try {
	        		if(totem != null && totem.isValid()) {
	        			runnable.run(totem);
	        			if(ticks - 1 > 0) {
	        				callTotemEvent(totem, runnable, ticks - 1, interval);
	        			}
	        			else {
	        				totem.remove();
	        			}
	        		}
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
	        
		}, interval);
	}
	
	/**
	 * A runnable that needs a totem entity to be executed.
	 * 
	 * @author Wauzmons
	 */
	public static interface TotemRunnable {
		
		/**
		 * Executes this runnable for the given totem.
		 * 
		 * @param totem The totem bound to this runnable.
		 */
		public abstract void run(Entity totem);
		
	}
	
	/**
	 * Creates an audiovisual explosion, that can not destroy blocks.
	 * 
	 * @param location The location of the explosion.
	 * @param power The power of the explosion.
	 */
	public static void createExplosion(Location location, float power) {
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power, false, false);
	}
	
	/**
	 * Lets the entity rotate 360 degrees over the course of 0.5 seconds.
	 * 
	 * @param entity The entity to rotate.
	 */
	public static void rotateEntity(Entity entity) {
		new BukkitRunnable() {
			
			private int runs = 0;
			
			public void run() {
				if(runs++ >= 10 || !entity.isValid()) {
					this.cancel();
				}
				Location location = entity.getLocation();
				float yaw = location.getYaw() + 36;
				yaw = yaw >= 360 ? yaw - 360 : yaw;
				location.setYaw(yaw);
				entity.teleport(location);
			}
			
		}.runTaskTimer(WauzCore.getInstance(), 0, 1);
	}
	
	/**
	 * Throws an entity to another entity.
	 * 
	 * @param entity The entity to throw.
	 * @param target The targeted entity.
	 */
	public static void throwEntity(Entity entity, Entity target) {
		Vector vector = getVectorForPoints(entity.getLocation(), target.getLocation());
		entity.teleport(entity.getLocation().add(0, 0.5, 0));
		entity.setVelocity(vector);	
	}
	
	/**
	 * Throws an entity to a location.
	 * 
	 * @param entity The entity to throw.
	 * @param location The targeted location.
	 */
	public static void throwEntity(Entity entity, Location location) {
		Vector vector = getVectorForPoints(entity.getLocation(), location);
		entity.teleport(entity.getLocation().add(0, 0.5, 0));
		entity.setVelocity(vector);
	}
	
	/**
	 * Throws an entity to given coordinates.
	 * 
	 * @param entity The entity to throw.
	 * @param x The targeted x coordinate.
	 * @param y The targeted y coordinate.
	 * @param z The targeted z coordinate.
	 */
	public static void throwEntity(Entity entity, double x, double y, double z) {
		Vector vector = getVectorForPoints(entity.getLocation(), new Location(entity.getWorld(), x, y, z));
		entity.teleport(entity.getLocation().add(0, 0.5, 0));
		entity.setVelocity(vector);
	}
	
	/**
	 * Throws multiple entities into the air.
	 * 
	 * @param entities The entities to throw.
	 * @param force The force to throw with.
	 */
	public static void throwEntitiesIntoAir(List<Entity> entities, double force) {
		for(Entity entity : entities) {
			throwEntityIntoAir(entity, force);
		}
	}
	
	/**
	 * Throws an entity into the air.
	 * 
	 * @param entity The entity to throw.
	 * @param force The force to throw with.
	 */
	public static void throwEntityIntoAir(Entity entity, double force) {
		Vector vector = new Vector(0, force, 0);
		entity.setVelocity(vector);
	}
	
	/**
	 * Throws an entity back from a location.
	 * 
	 * @param entity The entity to throw.
	 * @param fromLocation The location to throw back from.
	 * @param force The force to throw with.
	 */
	public static void throwBackEntity(Entity entity, Location fromLocation, double force) {
		Vector vector = getThrowbackVector(entity.getLocation(), fromLocation, force);
		entity.setVelocity(vector);
	}
	
	/**
	 * Generates a vector from one location to another.
	 * 
	 * @param originLocation The starting location.
	 * @param targetLocation The targeted location.
	 * 
	 * @return The vector between the locations.
	 */
    public static Vector getVectorForPoints(Location originLocation, Location targetLocation) {
		double g = -0.08;
		double d = targetLocation.distance(originLocation);
		double t = d;
		double vX = (1.0+0.07*t) * (targetLocation.getX() - originLocation.getX())/t;
		double vY = (1.0+0.03*t) * (targetLocation.getY() - originLocation.getY())/t - 0.5*g*t;
		double vZ = (1.0+0.07*t) * (targetLocation.getZ() - originLocation.getZ())/t;
		return new Vector(vX, vY, vZ);
    }
    
    /**
     * Generates a vector from one location away from another.
     * 
     * @param originLocation The starting location.
	 * @param throwbackLocation The throwback location.
     * @param force The force away from the throwback location.
     * 
     * @return The vector away from the throwback location.
     */
    public static Vector getThrowbackVector(Location originLocation, Location throwbackLocation, double force) {
		double d = throwbackLocation.distance(originLocation);
		double t = d;
		double vX = (force / t) * (originLocation.getX() - throwbackLocation.getX());
		double vZ = (force / t) * (originLocation.getZ() - throwbackLocation.getZ());
		return new Vector(vX, force / 3.0, vZ);
    }

}
