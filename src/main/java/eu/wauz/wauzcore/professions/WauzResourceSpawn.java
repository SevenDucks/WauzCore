package eu.wauz.wauzcore.professions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.LootBag;

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
		ParticleSpawner.spawnParticleCircle(player, location, particle, 0.75, 4);
	}
	
	/**
	 * Lets the player collect the resource, if it is ready.
	 * 
	 * @param player The player collecting the resource.
	 * 
	 * @return If the collection was successful.
	 */
	public boolean tryToCollectResource(Player player) {
		if(!canCollectResource(player)) {
			return false;
		}
		LootBag lootBag = resource.getDropTable().generate(new DropMetadata(null, BukkitAdapter.adapt(player)));
		lootBag.drop(BukkitAdapter.adapt(location.clone().add(0, 1, 0)));
		Long cooldown = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(resource.getRespawnMins());
		playerCooldownMap.put(player, cooldown);
		return true;
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

	/**
	 * @return The instanced resource.
	 */
	public WauzResource getResource() {
		return resource;
	}

}
