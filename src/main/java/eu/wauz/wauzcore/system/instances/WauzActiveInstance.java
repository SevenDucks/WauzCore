package eu.wauz.wauzcore.system.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.mobs.MobSpawn;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.mobs.citizens.WauzInstanceCitizen;

/**
 * An instance data to save session scoped instance information.
 * 
 * @author Wauzmons
 */
public class WauzActiveInstance extends WauzBaseInstance {
	
	/**
	 * The world of the instance.
	 */
	private World world;
	
	/**
	 * The location to spawn players.
	 */
	private Location spawnLocation;
	
	/**
	 * The citizens that have been spawned in the instance.
	 */
	private List<WauzCitizen> activeCitizens = new ArrayList<>();
	
	/**
	 * A map of the amount of times a player has died in the instance so far, indexed by player.
	 */
	private Map<Player, Integer> playerDeathMap = new HashMap<>();
	
	/**
	 * A map of the statuses of the instance's keys, indexed by key id.
	 */
	private Map<String, WauzInstanceKeyStatus> keyStatusMap = new HashMap<>();

	public WauzActiveInstance(World world, String name) {
		this.world = world;
		setInstanceName(name);
	}
	
	/**
	 * Constructs an active instance data, based on an instance template.
	 * 
	 * @param world The world of the instance.
	 * @param template The template, the instance was created from.
	 * 
	 * @see MobSpawn#spawn(World)
	 * @see WauzInstanceCitizen#spawn(World)
	 */
	public WauzActiveInstance(World world, WauzInstance template) {
		this.world = world;
		setInstanceName(template.getInstanceName());
		setType(template.getType());
		List<Float> coords = template.getSpawnCoords();
		if(coords.size() >= 5) {
			this.spawnLocation = new Location(world, coords.get(0), coords.get(1), coords.get(2));
		}
		else if(coords.size() >= 3) {
			this.spawnLocation = new Location(world, coords.get(0), coords.get(1), coords.get(2), coords.get(3), coords.get(4));
		}
		else {
			this.spawnLocation = new Location(world, 0.5, 5, 0.5);
		}
		setSpawnCoords(coords);
		setMaxPlayers(template.getMaxPlayers());
		setMaxDeaths(template.getMaxDeaths());
		setDisplayTitle(template.getDisplayTitle());
		setDisplaySubtitle(template.getDisplaySubtitle());
		setSoundtrackName(template.getSoundtrackName());
		setKeyIds(template.getKeyIds());
		if(template.getMobArena() != null) {
			setMobArena(new InstanceMobArena(template.getMobArena(), world));
		}
		
		for(MobSpawn mob : template.getMobs()) {
			mob.spawn(world);
		}
		for(WauzInstanceCitizen citizen : template.getCitizens()) {
			activeCitizens.add(citizen.spawn(world));
		}
	}
	
	/**
	 * @return The world of the instance.
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * @return The location to spawn players.
	 */
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	/**
	 * Removes all citizens that have been spawned in the instance.
	 * 
	 * @see WauzCitizen#removeFromChunkMap(WauzCitizen, org.bukkit.Chunk)
	 */
	public void clearActiveCitizens() {
		for(WauzCitizen citizen : activeCitizens) {
			WauzCitizen.removeFromChunkMap(citizen, citizen.getLocation().getChunk());
		}
	}

	/**
	 * @param player The player to get the death count for.
	 * 
	 * @return The amount of times a player has died in the instance so far.
	 */
	public int getPlayerDeaths(Player player) {
		Integer count = playerDeathMap.get(player);
		return count != null ? count : 0;
	}
	
	/**
	 * Increases the amount of times a player has died in the instance by one.
	 * 
	 * @param player The player to increase the deaths for.
	 */
	public void addPlayerDeath(Player player) {
		int count = getPlayerDeaths(player);
		playerDeathMap.put(player, count + 1);
	}
	
	/**
	 * @param keyId The id of the key to get the status for.
	 * 
	 * @return The status of the key.
	 */
	public WauzInstanceKeyStatus getKeyStatus(String keyId) {
		WauzInstanceKeyStatus keyStatus = keyStatusMap.get(keyId);
		return keyStatus != null ? keyStatus : WauzInstanceKeyStatus.UNOBTAINED;
	}
	
	/**
	 * Updates the status of a key.
	 * 
	 * @param keyId The id of the key to update the status for.
	 * @param status The new key status.
	 */
	public void setKeyStatus(String keyId, WauzInstanceKeyStatus status) {
		keyStatusMap.put(keyId, status);
	}

}
