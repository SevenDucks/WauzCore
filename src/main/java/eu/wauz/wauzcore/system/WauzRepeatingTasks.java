package eu.wauz.wauzcore.system;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizenCache;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizenSpawner;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.ClimateCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.players.calc.RageCalculator;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.players.ui.WauzPlayerNotifier;
import eu.wauz.wauzcore.players.ui.scoreboard.WauzPlayerScoreboard;
import eu.wauz.wauzcore.professions.WauzResource;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.instances.InstanceManager;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used to load different types of repeating tasks.
 * 
 * @author Wauzmons
 */
public class WauzRepeatingTasks {
	
	/**
	 * Schedules all predefined repeating tasks.
	 * 
	 * @param The plugin core.
	 */
	public static void schedule(WauzCore core) {
		BukkitScheduler scheduler = core.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(core, getEverySecondTask(), 200, 20);
		scheduler.scheduleSyncRepeatingTask(core, getEvery3SecondsTask(), 200, 60);
		scheduler.scheduleSyncRepeatingTask(core, getEvery5SecondsTask(), 200, 100);
		scheduler.scheduleSyncRepeatingTask(core, getEveryMinuteTask(), 200, 1200);
		scheduler.scheduleSyncRepeatingTask(core, getEvery3MinutesTask(), 200, 3600);
		scheduler.scheduleSyncRepeatingTask(core, getEvery5MinutesTask(), 200, 6000);
		scheduler.scheduleSyncRepeatingTask(core, getEvery15MinutesTask(), 200, 18000);
	}
	
	/**
	 * Gets the default task to execute every second.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEverySecondTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Player player : WauzCore.getRegisteredActivePlayers()) {
					if(WauzMode.isMMORPG(player)) {
						WauzResource.highlightResourcesNearPlayer(player);
					}
					WauzPlayerActionBar.update(player);
					WauzPlayerEffects.progressPlayerEffects(player);
				}
				WauzCitizenCache.updateCitizenLookDirections();
			}
			
		};
	}
	
	/**
	 * Gets the default task to execute every 3 seconds.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEvery3SecondsTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Player player : WauzCore.getRegisteredActivePlayers()) {
					WauzPlayerScoreboard.scheduleScoreboardRefresh(player);
					WauzRegion.regionCheck(player);
					WauzCitizenSpawner.showNpcsNearPlayer(player);
				}
			}
			
		};
	}
	
	/**
	 * Gets the default task to execute every 5 seconds.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEvery5SecondsTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Player player : WauzCore.getRegisteredActivePlayers()) {
					if(WauzMode.isMMORPG(player)) {
						ClimateCalculator.updateTemperature(player);
						ManaCalculator.regenerateMana(player);
						RageCalculator.degenerateRage(player);
					}
				}
			}
			
		};
	}
	
	/**
	 * Gets the default task to execute every minute.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEveryMinuteTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Player player : WauzCore.getRegisteredActivePlayers()) {
					if(WauzMode.isMMORPG(player)) {
						WauzPetAbilities.tryToUse(player);
					}
				}
			}
			
		};
	}
	
	/**
	 * Gets the default task to execute every 3 minutes.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEvery3MinutesTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Player player : WauzCore.getRegisteredActivePlayers()) {
					if(WauzMode.isMMORPG(player) && WauzPlayerDataPool.isCharacterSelected(player)) {
						AchievementTracker.addProgress(player, WauzAchievementType.PLAY_HOURS, 0.05);
					}
				}
			}
			
		};
	}
	
	/**
	 * Gets the default task to execute every 5 minutes.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEvery5MinutesTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(World world : Bukkit.getWorlds()) {
					if(world.getPlayerCount() == 0 && !world.getName().equals("WzInstance_Arcade")) {
						InstanceManager.closeInstance(world);
					}
				}
				for(Player player : WauzCore.getRegisteredActivePlayers()) {
					CharacterManager.saveCharacter(player);
				}
			}
			
		};
	}
	
	/**
	 * Gets the default task to execute every 15 minutes.
	 * 
	 * @return The task runnable.
	 */
	private static Runnable getEvery15MinutesTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					WauzPlayerNotifier.execute(player);
				}
				WauzCore.getDiscordBot().updateGeneralChannelServerStats();
				WauzCore.getDiscordBot().updateLoggingChannelServerStats();
			}
			
		};
	}

}
