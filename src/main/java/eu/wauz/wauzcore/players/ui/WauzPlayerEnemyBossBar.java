package eu.wauz.wauzcore.players.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.mobs.MenacingModifier;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * An UI class to show the player name and health of the targeted enemy.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEnemyBossBar extends WauzPlayerBossBar {
	
	/**
	 * The entity, this bar belongs to.
	 */
	private Damageable damageable;
	
	/**
	 * The particles circling the entity, this bar belongs to.
	 */
	private SkillParticle particle = null;
	
	/**
	 * Creates a boss bar for the given entity.
	 * Also schedules a task to check if the entity and assigned players are still valid. 
	 * 
	 * @param entity The entity, this bar belongs to.
	 * @param modifiers Menacing modifiers shown in the bar.
	 * @param maxHealth Max health shown in the bar.
	 * @param raidBoss If the entity is a raid boss.
	 * 
	 * @see WauzPlayerBossBar#doPlayerChecks()
	 */
	public WauzPlayerEnemyBossBar(Entity entity, List<MenacingModifier> modifiers, double maxHealth, boolean raidBoss) {
		if(!(entity instanceof Damageable)) {
			return;
		}
		
		this.damageable = (Damageable) entity;
		this.uuid = damageable.getUniqueId().toString();
		this.modifiers = modifiers.size() > 0 ? ChatColor.GOLD + StringUtils.join(modifiers, " ") + " " : "";
		this.maxHealth = maxHealth;
		
		BarColor barColor;
		if(raidBoss) {
			barColor = BarColor.PINK;
		}
		else if(modifiers.size() > 0) {
			barColor = BarColor.YELLOW;
			particle = new SkillParticle(Color.ORANGE);
		}
		else {
			barColor = BarColor.RED;
		}
		
		bossBar = Bukkit.createBossBar(getTitle((int) Math.ceil(getHealth())), barColor, BarStyle.SEGMENTED_6);
		bossBar.setProgress(1);
		bossBars.put(uuid, this);
		doPlayerChecks();
	}
	
	/**
	 * @param entity An entity with a boss bar.
	 * 
	 * @return The boss bar of this entity, if existing.
	 */
	public static WauzPlayerBossBar getBossBar(Entity entity) {
		return bossBars.get(entity.getUniqueId().toString());
	}
	
	/**
	 * Schedules a task to check if the object and assigned players are still valid.
	 * If not, they get removed, else the task is scheduled again for the next second.
	 * 
	 * @see WauzPlayerBossBar#destroy()
	 */
	@Override
	protected void doPlayerChecks() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	try {
	        		if(damageable == null || !damageable.isValid()) {
	        			destroy();
	        			return;
	        		}
	        		for(Player player : bossBar.getPlayers()) {
	        			if(!inDistance(player, damageable.getLocation())) {
	        				bossBar.removePlayer(player);
	        				bossBarPlayerLinks.remove(player);
	        			}
	        		}
	        		if(particle != null) {
	        			ParticleSpawner.spawnParticleCircle(damageable.getLocation(), particle, 1, 8);
	        		}
	        		if(MobMetadataUtils.hasMenacingModifier(damageable, MenacingModifier.RAVENOUS)) {
	        			SkillUtils.addPotionEffect(damageable, PotionEffectType.SPEED, 2, 4);
	        		}
	        		doPlayerChecks();
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        		destroy();
	        	}
	        }
	        
		}, 20);
	}
	
	/**
	 * @return The name of the object, this bar belongs to.
	 */
	@Override
	protected String getName() {
		return damageable.getName();
	}
	
	/**
	 * @return The health of the object, this bar belongs to.
	 */
	@Override
	protected double getHealth() {
		return damageable.getHealth();
	}

}
