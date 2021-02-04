package eu.wauz.wauzcore.professions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * An instanced block of a gatherable resource.
 * 
 * @author Wauzmons
 */
public class WauzResourceSpawn {
	
	/**
	 * The instanced resource.
	 */
	private WauzResource resource;
	
	/**
	 * The location of the resource instance.
	 */
	private Location location;
	
	/**
	 * The particles to highlight the resource location.
	 */
	private SkillParticle particle;
	
	/**
	 * A map to indicate when resources are ready to collect, indexed by player.
	 */
	private Map<Player, Long> playerCooldownMap = new HashMap<>();
	
	/**
	 * Instantiates a block of a gatherable resource.
	 * 
	 * @param resource The instanced resource.
	 * @param location The location of the resource instance.
	 */
	public WauzResourceSpawn(WauzResource resource, Location location) {
		this.resource = resource;
		this.location = location;
		
		switch (resource.getType()) {
		case CONTAINER:
			particle = new SkillParticle(Color.MAROON);
			break;
		case NODE:
			particle = new SkillParticle(Color.AQUA);
			break;
		}
	}
	
	/**
	 * Highlights the resoure for a player, if it is ready to collect.
	 * 
	 * @param player The player to highlight the resource for.
	 */
	public void tryToHighlightResource(Player player) {
		if(!canCollectResource(player)) {
			return;
		}
		ParticleSpawner.spawnParticleCircle(location.getBlock().getLocation(), particle, 0.75, 6);
	}
	
	/**
	 * Lets the player collect the resource, if it is ready.
	 * 
	 * @param player The player collecting the resource.
	 */
	public void tryToCollectResource(Player player) {
		if(!canCollectResource(player)) {
			return;
		}
		switch (resource.getType()) {
		case CONTAINER:
			break;
		case NODE:
			break;
		}
		Long cooldown = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(resource.getRespawnMins());
		playerCooldownMap.put(player, cooldown);
	}
	
	/**
	 * Checks if the given player can collect the resource.
	 * 
	 * @param player The player to check for.
	 * 
	 * @return If the resource is ready to collect. 
	 */
	public boolean canCollectResource(Player player) {
		Long cooldown = playerCooldownMap.get(player);
		return cooldown == null || cooldown < System.currentTimeMillis();
	}

}
