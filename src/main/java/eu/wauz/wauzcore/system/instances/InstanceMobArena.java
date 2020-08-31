package eu.wauz.wauzcore.system.instances;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.mobs.MobSpawn;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;

/**
 * An arena that spawns waves of mobs in an instance world.
 * 
 * @author Wauzmons
 */
public class InstanceMobArena {
	
	/**
	 * The instance world of the arena.
	 */
	private World world;
	
	/**
	 * The current wave number.
	 */
	private int currentWave;
	
	/**
	 * The maximum wave number.
	 */
	private int maximumWave;
	
	/**
	 * The list of lists of mobs that spawn in arena waves.
	 */
	private List<List<MobSpawn>> waves = new ArrayList<>();
	
	/**
	 * How many mobs of the current wave are left.
	 */
	private int mobsLeft = 0;
	
	/**
	 * Creates an arena template, based on the instance file in the /WauzCore/InstanceData folder.
	 * 
	 * @param instanceName The name of the instance, the arena is located in.
	 */
	public InstanceMobArena(String instanceName) {
		currentWave = 0;
		maximumWave = InstanceConfigurator.getWaveCount(instanceName);
		for(int wave = 1; wave <= maximumWave; wave++) {
			List<MobSpawn> waveMobs = new ArrayList<>();
			for(String mobString : InstanceConfigurator.getWaveMobSpawns(instanceName, wave)) {
				waveMobs.add(new MobSpawn(mobString));
			}
			waves.add(waveMobs);
		}
	}
	
	/**
	 * Constructs an arena, that is an exact copy of another arena.
	 * 
	 * @param arena The arena to clone.
	 * @param world The instance world for the cloned arena.
	 */
	public InstanceMobArena(InstanceMobArena arena, World world) {
		currentWave = arena.getCurrentWave();
		maximumWave = arena.getMaximumWave();
		waves = arena.getWaves();
		this.world = world;
	}
	
	/**
	 * Lets a player try to start a new wave manually.
	 * Only works when a set of 5 waves is completed and there are still waves left.
	 * 
	 * @param player The player who tries to start the wave.
	 * 
	 * @see InstanceMobArena#startNewWave()
	 */
	public void tryToManuallyStartNewWave(Player player) {
		if(mobsLeft > 0 || currentWave % 5 != 0) {
			player.sendMessage(ChatColor.RED + "There are still waves ongoing!");
			return;
		}
		if(currentWave >= maximumWave) {
			player.sendMessage(ChatColor.RED + "The arena has already been completed!");
		}
		else {
			startNewWave();
		}
	}
	
	/**
	 * Starts a new wave, if requirements are met.
	 * Broadcasts a message when all waves have been completed.
	 * 
	 * @see InstanceMobArena#startNewWave()
	 * @see InstanceMobArena#broadcastMessage(String, Sound)
	 */
	public void checkIfNewWaveShouldStart() {
		if(mobsLeft > 0 || currentWave <= 0) {
			return;
		}
		
		if(currentWave >= maximumWave) {
			broadcastMessage(ChatColor.GOLD + "Arena Completed!", Sound.ENTITY_ENDER_DRAGON_DEATH);
			handOutMedals();
		}
		else if(currentWave % 5 == 0) {
			handOutMedals();
		}
		else {
			startNewWave();
		}
	}
	
	/**
	 * Increases the wave counter and spawns the mobs for the new wave.
	 * Also broadcasts a message to show the current wave number.
	 * 
	 * @see MobSpawn#spawn(World)
	 * @see InstanceMobArena#broadcastMessage(String, Sound)
	 * @see WauzPlayerScoreboard#scheduleScoreboardRefresh(Player)
	 */
	public void startNewWave() {
		currentWave++;
		mobsLeft = 0;
		broadcastMessage(ChatColor.RED + "Wave " + currentWave, Sound.ENTITY_WITHER_SPAWN);
		for(MobSpawn mob : waves.get(currentWave - 1)) {
			mob.spawn(world);
		}
		for(Player player : world.getPlayers()) {
			WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
		}
	}
	
	/**
	 * Broadcasts a title message with sound effect to all players in the arena.
	 * 
	 * @param message The message to display.
	 * @param sound The sound effect to play.
	 */
	public void broadcastMessage(String message, Sound sound) {
		for(Player player : world.getPlayers()) {
			player.sendTitle(message, "", 10, 70, 20);
			player.getWorld().playSound(player.getLocation(), sound, 1, 1);
		}
	}
	
	/**
	 * Give every player in the arena a medal for completing the current wave.
	 * 
	 * @see PlayerConfigurator#setCharacterMedals(Player, long)
	 */
	public void handOutMedals() {
		for(Player player : world.getPlayers()) {
			long medals = PlayerCollectionConfigurator.getCharacterMedals(player) + 1;
			PlayerCollectionConfigurator.setCharacterMedals(player, medals);
			player.sendMessage(ChatColor.YELLOW + "You earned a medal for completing " + currentWave + " waves!");
		}
	}
	
	/**
	 * If the given entity is part of a mob arena, the arena's mob count gets increased.
	 * 
	 * @param entity The entity that could have appeared in an arena.
	 */
	public static void tryToIncreaseMobCount(Entity entity) {
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(entity.getWorld());
		if(instance != null && instance.getType() == WauzInstanceType.ARENA) {
			InstanceMobArena arena = instance.getMobArena();
			arena.increaseMobCount();
		}
	}
	
	/**
	 * Increases how many mobs are left in the current wave.
	 */
	public void increaseMobCount() {
		mobsLeft++;
	}
	
	/**
	 * If the given entity is part of a mob arena, the arena's mob count gets decreased.
	 * 
	 * @param entity The entity that could have disappeared from an arena.
	 */
	public static void tryToDecreaseMobCount(Entity entity) {
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(entity.getWorld());
		if(instance != null && instance.getType() == WauzInstanceType.ARENA) {
			InstanceMobArena arena = instance.getMobArena();
			arena.decreaseMobCount();
		}
	}
	
	/**
	 * Decreases how many mobs are left in the current wave.
	 * 
	 * @see InstanceMobArena#checkIfNewWaveShouldStart()
	 */
	public void decreaseMobCount() {
		mobsLeft--;
		checkIfNewWaveShouldStart();
	}
	
	/**
	 * @return The current wave number.
	 */
	public int getCurrentWave() {
		return currentWave;
	}

	/**
	 * @param currentWave The new current wave number.
	 */
	public void setCurrentWave(int currentWave) {
		this.currentWave = currentWave;
	}

	/**
	 * @return The maximum wave number.
	 */
	public int getMaximumWave() {
		return maximumWave;
	}

	/**
	 * @param maximumWave The new maximum wave number.
	 */
	public void setMaximumWave(int maximumWave) {
		this.maximumWave = maximumWave;
	}

	/**
	 * @return The list of lists of mobs that spawn in arena waves.
	 */
	public List<List<MobSpawn>> getWaves() {
		return waves;
	}

	/**
	 * @param waves The new list of lists of mobs that spawn in arena waves.
	 */
	public void setWaves(List<List<MobSpawn>> waves) {
		this.waves = waves;
	}

	/**
	 * @return How many mobs of the current wave are left.
	 */
	public int getMobsLeft() {
		return mobsLeft;
	}

}
