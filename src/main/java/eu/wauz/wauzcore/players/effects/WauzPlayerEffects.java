package eu.wauz.wauzcore.players.effects;

import java.util.ArrayList;
import java.util.List;

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
		effects.sort((e1, e2) -> Integer.compare(e2.getDuration(), e1.getDuration()));
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
	 * Reduces the duration of all effects by 1 second and removes expired ones.
	 */
	private void progressEffects() {
		for(WauzPlayerEffect effect : getEffectList()) {
			if(effect.reduceDuration()) {
				effects.remove(effect);
			}
		}
	}
	
	/**
	 * @return A cloned list of currently active status effects.
	 */
	private List<WauzPlayerEffect> getEffectList() {
		return new ArrayList<>(effects);
	}
	
	/**
	 * Removes all currently active status effects.
	 */
	public void clearEffects() {
		effects.clear();
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
		playerData.getStats().getEffects().progressEffects();
	}

}
