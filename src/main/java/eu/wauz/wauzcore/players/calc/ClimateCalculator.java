package eu.wauz.wauzcore.players.calc;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class ClimateCalculator {
	
	public static void temperature(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		byte playerTemperature = playerData.getHeat();
		WauzRegion region = playerData.getRegion();
		byte regionTemperature = region != null ? region.getTemperature() : 6;
		regionTemperature -= player.getWorld().getTime() < 12000 ? 0 : 3;
		
// Change Temperature
		
		if(playerTemperature > regionTemperature) {
			playerTemperature--;
			playerData.setHeat(playerTemperature);
		}
		else if(playerTemperature < regionTemperature) {
			playerTemperature++;
			playerData.setHeat(playerTemperature);
		}
		
		playerData.setHeatRandomizer((byte) Chance.negativePositive(2));
		
// Apply Temperature
		
		WauzDebugger.log(player, "Temperature of " + playerTemperature + " Heat Unit(s)");
		
		WauzPlayerActionBar.update(player);
		
		if(!player.getGameMode().equals(GameMode.CREATIVE) && !WauzMode.inHub(player)) {
			if(playerTemperature > 8 && playerData.getResistanceHeat() < 1) {
				DamageCalculator.setHealth(player, playerData.getHealth() - 2);
				PotionEffect effect = new PotionEffect(PotionEffectType.HUNGER, 100, 2);
				player.addPotionEffect(effect);
				player.sendTitle(ChatColor.RED + "Critical Heat!", "Find a colder place asap!", 10, 70, 20);
			}
			else if(playerTemperature < 2 && playerData.getResistanceCold() < 1) {
				DamageCalculator.setHealth(player, playerData.getHealth() - 2);
				PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 100, 2);
				player.addPotionEffect(effect);
				player.sendTitle(ChatColor.BLUE + "Critical Cold!", "Find a warmer place asap!", 10, 70, 20);
			}
		}
		
		playerData.decreaseTemperatureResistance();
	}
	
}
