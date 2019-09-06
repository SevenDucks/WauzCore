package eu.wauz.wauzcore.players.ui;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerBossBar {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");
	
	private static Map<String, WauzPlayerBossBar> bossBars = new HashMap<>();
	
	private static Map<Player, WauzPlayerBossBar> bossBarPlayerLinks = new HashMap<>();
	
	public static WauzPlayerBossBar getBossBar(Entity entity) {
		return bossBars.get(entity.getUniqueId().toString());
	}
	
	public static void clearBar(Player player) {
		WauzPlayerBossBar playerBossBar = bossBarPlayerLinks.get(player);
		if(playerBossBar != null) {
			playerBossBar.removePlayer(player);
			bossBarPlayerLinks.put(player, null);
		}
	}
	
	private Damageable damageable;
	
	private String damageableUuid;
	
	private String modifiers;
	
	private double maxHealth;
	
	private BossBar bossBar;
	
	private SkillParticle particle = null;
	
	public WauzPlayerBossBar(Entity entity, List<String> modifiers, double maxHealth, boolean raidBoss) {
		if(!(entity instanceof Damageable))
			return;
		
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
	
	public void addPlayer(Player player, double damage) {
		if(bossBar.getProgress() <= 0)
			return;
		
		if((int) Math.ceil(damageable.getHealth()) - damage <= 0)
			return;
		
		WauzPlayerBossBar playerBossBar = bossBarPlayerLinks.get(player);
		if(playerBossBar == null || !playerBossBar.equals(this)) {
			if(playerBossBar != null) {
				playerBossBar.removePlayer(player);
			}
			bossBar.addPlayer(player);
			bossBarPlayerLinks.put(player, this);
		}
	}
	
	public void removePlayer(Player player) {
		bossBar.removePlayer(player);
	}
	
	public void updateBossBar(double damage) {
		int health = (int) Math.ceil((damageable.getHealth() - damage));
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
	
	public String getTitle(int health) {
		String currentHealth = ChatColor.RED + formatter.format(health);
		String maximumHealth = formatter.format(maxHealth) + " " + ChatFormatter.ICON_HEART;
		String healthString = ChatColor.GRAY + "[ " + currentHealth + " / " + maximumHealth + ChatColor.GRAY + " ]";
		return modifiers + damageable.getName() + " " + healthString;
	}
	
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
	        			if(particle != null)
	        				ParticleSpawner.spawnParticleCircle(damageable.getLocation(), particle, 1, 8);
	        			if(damageable.hasMetadata("wzModRavenous"))
	        				SkillUtils.addPotionEffect(damageable, PotionEffectType.SPEED, 2, 4);
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
	
	private void destroy() {
		for(Player player : bossBar.getPlayers()) {
			bossBar.removePlayer(player);
			bossBarPlayerLinks.remove(player);
		}
		bossBars.remove(damageableUuid);
	}
	
}
