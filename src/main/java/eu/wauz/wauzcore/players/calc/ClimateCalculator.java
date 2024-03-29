package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionStats;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used to calculate the temperature of players, based on the region.
 * Also adds negative effects, if the player cannot handle the current temperature.
 * 
 * @author Wauzmons
 * 
 * @see WauzRegion#getTemperature()
 * @see WauzPlayerDataSectionStats#getHeat()
 */
public class ClimateCalculator {
	
	/**
	 * Updates the temperature of the given player.
	 * The temperature in- or decreases, based on the region temperature.
	 * If it is night a value of 3 is subtracted from the base value.
	 * The temperature displayed to the player is randomized,
	 * by a value of plus/minus 2, to simulate temperature fluctuation.
	 * After that the timers for heat/cold resistances are decreased.<br><br>
	 * 
	 * If the temperature is above 8 and the player has no heat resistance,
	 * the player receives 2 damage and gets a hunger III effect.
	 * If the temperature is below 2 and the player has no cold resistance,
	 * the player receives 2 damage and gets a slowness III effect.
	 * For these cases a warning will be shown on screen.
	 * 
	 * @param player The player whose temperature should be updated.
	 * 
	 * @see WauzRegion#getTemperature()
	 * @see WauzPlayerDataSectionStats#setHeat(byte)
	 * @see WauzPlayerDataSectionStats#setHeatRandomizer(byte)
	 * @see WauzPlayerDataSectionStats#getEffects()
	 */
	public static void updateTemperature(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		byte playerTemperature = playerData.getStats().getHeat();
		WauzRegion region = playerData.getSelections().getRegion();
		byte regionTemperature = region != null ? region.getTemperature() : 6;
		regionTemperature -= player.getWorld().getTime() < 12000 ? 0 : 3;
		
		if(playerTemperature > regionTemperature) {
			playerTemperature--;
			playerData.getStats().setHeat(playerTemperature);
		}
		else if(playerTemperature < regionTemperature) {
			playerTemperature++;
			playerData.getStats().setHeat(playerTemperature);
		}
		playerData.getStats().setHeatRandomizer((byte) Chance.negativePositive(2));
		
		WauzDebugger.log(player, "Temperature of " + playerTemperature + " Heat Unit(s)");
		WauzPlayerActionBar.update(player);
		if(!player.getGameMode().equals(GameMode.CREATIVE) && !WauzMode.inHub(player)) {
			if(playerTemperature > 8 && !playerData.getStats().getEffects().hasEffect(WauzPlayerEffectType.HEAT_RESISTANCE)) {
				DamageCalculator.setHealth(player, playerData.getStats().getHealth() - 2);
				PotionEffect effect = new PotionEffect(PotionEffectType.HUNGER, 100, 2);
				player.addPotionEffect(effect);
				Components.title(player, ChatColor.DARK_RED + "Critical Heat!", "Find a colder place asap!");
			}
			else if(playerTemperature < 2 && !playerData.getStats().getEffects().hasEffect(WauzPlayerEffectType.COLD_RESISTANCE)) {
				DamageCalculator.setHealth(player, playerData.getStats().getHealth() - 2);
				PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 100, 2);
				player.addPotionEffect(effect);
				Components.title(player, ChatColor.DARK_BLUE + "Critical Cold!", "Find a warmer place asap!");
			}
		}
	}
	
}
