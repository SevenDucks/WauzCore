package eu.wauz.wauzcore.players.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.mobs.MenacingModifier;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An UI class to show the player name and health of the targeted entity.
 * 
 * @author Wauzmons
 */
public class WauzPlayerBossBar {
	
	/**
	 * A map of all boss bars by entity uuid.
	 */
	private static Map<String, WauzPlayerBossBar> bossBars = new HashMap<>();
	
	/**
	 * A map of boss bars by the player they are shown to.
	 */
	private static Map<Player, WauzPlayerBossBar> bossBarPlayerLinks = new HashMap<>();
	
	/**
	 * @param entity An entity with a boss bar.
	 * 
	 * @return The boss bar of this entity, if existing.
	 */
	public static WauzPlayerBossBar getBossBar(Entity entity) {
		return bossBars.get(entity.getUniqueId().toString());
	}
	
	/**
	 * Removes the shown boss bar for the given player.
	 * 
	 * @param player The player who should no longer see a boss bar.
	 */
	public static void clearBar(Player player) {
		WauzPlayerBossBar playerBossBar = bossBarPlayerLinks.get(player);
		if(playerBossBar != null) {
			playerBossBar.removePlayer(player);
			bossBarPlayerLinks.put(player, null);
		}
	}
	
	/**
	 * The entity, this bar belongs to.
	 */
	private Damageable damageable;
	
	/**
	 * The id of the entity, this bar belongs to.
	 */
	private String damageableUuid;
	
	/**
	 * Menacing modifiers shown in the bar.
	 */
	private String modifiers;
	
	/**
	 * Max health shown in the bar.
	 */
	private double maxHealth;
	
	/**
	 * The Minecraft boss bar.
	 */
	private BossBar bossBar;
	
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
	public WauzPlayerBossBar(Entity entity, List<MenacingModifier> modifiers, double maxHealth, boolean raidBoss) {
		if(!(entity instanceof Damageable)) {
			return;
		}
		
		this.damageable = (Damageable) entity;
		this.damageableUuid = damageable.getUniqueId().toString();
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
		
		bossBar = Bukkit.createBossBar(getTitle((int) Math.ceil(damageable.getHealth())), barColor, BarStyle.SEGMENTED_6);
		bossBar.setProgress(1);
		
		bossBars.put(damageableUuid, this);
		doPlayerChecks();
	}
	
	/**
	 * Adds a player that should see this bar.
	 * If the entity will die from the damage, the bar will not be shown.
	 * 
	 * @param player The player that should see this bar.
	 * @param damage The damage the player dealt to see the bar.
	 */
	public void addPlayer(Player player, double damage) {
		if(bossBar.getProgress() <= 0) {
			return;
		}
		
		if((int) Math.ceil(damageable.getHealth()) - damage <= 0) {
			return;
		}
		
		WauzPlayerBossBar playerBossBar = bossBarPlayerLinks.get(player);
		if(playerBossBar == null || !playerBossBar.equals(this)) {
			if(playerBossBar != null) {
				playerBossBar.removePlayer(player);
			}
			bossBar.addPlayer(player);
			bossBarPlayerLinks.put(player, this);
		}
	}
	
	/**
	 * Removes a player that should no longer see this bar.
	 * 
	 * @param player The player that should no longer see this bar.
	 */
	public void removePlayer(Player player) {
		bossBar.removePlayer(player);
	}
	
	/**
	 * Updates the boss bar, because the entity got damaged.
	 * If the entity will die from the damage, the bar be destroyed.
	 * 
	 * @param damage The damage that was dealt to the entity.
	 */
	public void updateBossBar(double damage) {
		int health = (int) Math.ceil((damageable.getHealth() - damage));
		if(health > maxHealth) {
			health = (int) maxHealth;
		}
		if(health > 0) {
			for(Player player : bossBar.getPlayers()) {
				WauzDebugger.log(player, "BossBar: "
						+ health + " (" + damageable.getHealth() + " - "
						+ damage + ") / " + maxHealth);
			}
			bossBar.setTitle(getTitle(health));
			bossBar.setProgress(health / maxHealth);
		}
		else {
			destroy();
		}
	}
	
	/**
	 * Generates a new title for the boss bar.
	 * 
	 * @param health How much health is left.
	 * 
	 * @return The new title.
	 */
	public String getTitle(int health) {
		String currentHealth = ChatColor.RED + Formatters.INT.format(health);
		String maximumHealth = Formatters.INT.format(maxHealth) + " " + UnicodeUtils.ICON_HEART;
		String healthString = ChatColor.GRAY + "[ " + currentHealth + " / " + maximumHealth + ChatColor.GRAY + " ]";
		return modifiers + damageable.getName() + " " + healthString;
	}
	
	/**
	 * Schedules a task to check if the entity and assigned players are still valid.
	 * If not, they get removed, else the task is scheduled again for the next second.
	 * Also spawns the circling particles arount the entity.
	 * 
	 * @see ParticleSpawner#spawnParticleCircle(org.bukkit.Location, SkillParticle, double, int)
	 * @see WauzPlayerBossBar#destroy()
	 */
	private void doPlayerChecks() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	        public void run() {
	        	try {
	        		if(damageable != null && damageable.isValid()) {
	        			for(Player player : bossBar.getPlayers()) {
	        				if(player == null || !player.isValid() || player.getLocation().distance(damageable.getLocation()) > 32) {
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
	        		else {
	        			destroy();
	        		}
	        	}
	        	catch (Exception e) {
	        		e.printStackTrace();
	        		destroy();
	        	}
	        }
		}, 20);
	}
	
	/**
	 * Removes this boss bar from all maps and clears out all players.
	 */
	private void destroy() {
		for(Player player : bossBar.getPlayers()) {
			bossBar.removePlayer(player);
			bossBarPlayerLinks.remove(player);
		}
		bossBars.remove(damageableUuid);
	}
	
}
