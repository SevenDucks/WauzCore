package eu.wauz.wauzcore.players.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

/**
 * All the active status effects on a player.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerEffect
 */
public class WauzPlayerEffects {
	
	/**
	 * The list of currently active status effects.
	 */
	private final List<WauzPlayerEffect> effects = new ArrayList<>();
	
	/**
	 * Adds a new temporary status effect to the player.
	 * Gets ignored when there is already a better effect of the same type and source active.
	 * 
	 * @param type The type of the effect.
	 * @param source The source of the effect.
	 * @param duration The remaining duration of the effect in seconds.
	 * 
	 * @return If the effect could be added.
	 */
	public boolean addEffect(WauzPlayerEffectType type, WauzPlayerEffectSource source, int duration) {
		return addEffect(type, source, duration, 0);
	}
	
	/**
	 * Adds a new temporary status effect to the player.
	 * Gets ignored when there is already a better effect of the same type and source active.
	 * 
	 * @param type The type of the effect.
	 * @param source The source of the effect.
	 * @param duration The remaining duration of the effect in seconds.
	 * @param power The strength of the effect.
	 * 
	 * @return If the effect could be added.
	 */
	public boolean addEffect(WauzPlayerEffectType type, WauzPlayerEffectSource source, int duration, int power) {
		for(WauzPlayerEffect existingEffect : getEffectList()) {
			if(!existingEffect.getType().equals(type) || !existingEffect.getSource().equals(source)) {
				continue;
			}
			if(existingEffect.getPower() > power || (existingEffect.getPower() == power && existingEffect.getDuration() >= duration)) {
				return false;
			}
			effects.remove(existingEffect);
		}
		effects.add(new WauzPlayerEffect(type, source, duration, power));
		effects.sort((e1, e2) -> Integer.compare(e1.getDuration(), e2.getDuration()));
		return true;
	}
	
	/**
	 * Checks if the player has an effect of the given type on them.
	 * 
	 * @param type The type of the effect.
	 * 
	 * @return If the player has the effect.
	 */
	public boolean hasEffect(WauzPlayerEffectType type) {
		for(WauzPlayerEffect effect : getEffectList()) {
			if(effect.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the combined strength of the all matching effects.
	 * 
	 * @param type The type of the effect.
	 * 
	 * @return The sum of the effect strengths.
	 */
	public int getEffectPowerSum(WauzPlayerEffectType type) {
		int power = 0;
		for(WauzPlayerEffect effect : getEffectList()) {
			if(effect.getType().equals(type)) {
				power += effect.getPower();
			}
		}
		return power;
	}
	
	/**
	 * Gets the combined strength of the all matching effects as decimal number.
	 * 
	 * @param type The type of the effect.
	 * 
	 * @return The sum of the effect strengths.
	 */
	public double getEffectPowerSumDecimal(WauzPlayerEffectType type) {
		double power = 0;
		for(WauzPlayerEffect effect : getEffectList()) {
			if(effect.getType().equals(type)) {
				power += effect.getPowerDecimal();
			}
		}
		return power;
	}
	
	/**
	 * @return A cloned list of currently active status effects.
	 */
	private List<WauzPlayerEffect> getEffectList() {
		return new ArrayList<>(effects);
	}
	
	/**
	 * Removes a single active status effect.
	 * 
	 * @param effect The effect to remove.
	 */
	public void removeEffect(WauzPlayerEffect effect) {
		effects.remove(effect);
	}
	
	/**
	 * Removes all currently active status effects.
	 */
	public void clearEffects() {
		effects.clear();
	}
	
	/**
	 * Returns the names of all effects in a scoreboard friendly format.
	 * 
	 * @return The names of all effects.
	 */
	@Override
	public String toString() {
		String effectString = "";
		for(WauzPlayerEffect effect : effects) {
			effectString += effect.toString();
		}
		return effectString;
	}
	
	/**
	 * Reduces the duration of all the player's effects by 1 second and removes expired ones.
	 * 
	 * @param player The player to progress the effects for.
	 */
	public static void progressPlayerEffects(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		WauzPlayerEffects effects = playerData.getStats().getEffects();
		for(WauzPlayerEffect effect : effects.getEffectList()) {
			if(effect.reduceDuration()) {
				effects.removeEffect(effect);
				if(effect.getInitialDuration() > 10) {
					player.sendMessage(ChatColor.YELLOW + "Effect expired: " + effect.toString());
				}
			}
			else if(effect.getDuration() == 15) {
				player.sendMessage(ChatColor.YELLOW + "Effect expires soon: " + effect.toString());
			}
		}
	}

}
