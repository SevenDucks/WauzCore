package eu.wauz.wauzcore.players.ui.bossbar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.mobs.MenacingModifier;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * An UI class to show name and health of the targeted enemy to the player.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEnemyBossBar extends WauzPlayerBossBar {
	
	/**
	 * The entity, this bar belongs to.
	 */
	private LivingEntity entity;
	
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
	 * @param raidBoss If the entity is a raid boss.
	 * 
	 * @see WauzPlayerBossBar#doPlayerChecks()
	 */
	private WauzPlayerEnemyBossBar(LivingEntity entity, List<MenacingModifier> modifiers, boolean raidBoss) {
		this.entity = (LivingEntity) entity;
		this.uuid = entity.getUniqueId().toString();
		this.modifiers = modifiers.size() > 0 ? ChatColor.GOLD + StringUtils.join(modifiers, " ") + " " : "";
		
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
		
		int health = (int) Math.ceil(getHealth());
		int maxHealth = (int) Math.ceil(getMaxHealth());
		bossBar = Bukkit.createBossBar(getTitle(health, maxHealth), barColor, BarStyle.SEGMENTED_6);
		bossBars.put(uuid, this);
		doPlayerChecks();
	}
	
	/**
	 * Finds or creates a boss bar for the given entity.
	 * 
	 * @param entity An entity with a boss bar.
	 * 
	 * @return The boss bar of this entity or null, if no bar is possible.
	 */
	public static WauzPlayerBossBar getBossBar(Entity entity) {
		WauzPlayerBossBar bossBar = bossBars.get(entity.getUniqueId().toString());
		return bossBar != null ? bossBar : createBossBar(entity, new ArrayList<>(), false);
		
	}
	
	/**
	 * Creates a boss bar for the given entity.
	 * 
	 * @param entity The entity, this bar belongs to.
	 * @param modifiers Menacing modifiers shown in the bar.
	 * @param raidBoss If the entity is a raid boss.
	 * 
	 * @return The boss bar of this entity or null, if no bar is possible.
	 */
	public static WauzPlayerBossBar createBossBar(Entity entity, List<MenacingModifier> modifiers, boolean raidBoss) {
		if(entity instanceof LivingEntity) {
			return new WauzPlayerEnemyBossBar((LivingEntity) entity, modifiers, raidBoss);
		}
		return null;
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
	        		if(entity == null || !entity.isValid()) {
	        			destroy();
	        			return;
	        		}
	        		for(Player player : bossBar.getPlayers()) {
	        			if(!inDistance(player, entity.getLocation())) {
	        				bossBar.removePlayer(player);
	        				bossBarPlayerLinks.remove(player);
	        			}
	        		}
	        		if(particle != null) {
	        			ParticleSpawner.spawnParticleCircle(entity.getLocation(), particle, 1, 8);
	        		}
	        		if(MobMetadataUtils.hasMenacingModifier(entity, MenacingModifier.RAVENOUS)) {
	        			SkillUtils.addPotionEffect(entity, PotionEffectType.SPEED, 2, 4);
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
		return entity.getName();
	}
	
	/**
	 * @return The health of the object, this bar belongs to.
	 */
	@Override
	protected double getHealth() {
		return entity.getHealth() + entity.getAbsorptionAmount();
	}
	
	/**
	 * @return The maximum health of the object, this bar belongs to.
	 */
	@Override
	protected double getMaxHealth() {
		return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + entity.getAbsorptionAmount();
	}

}
