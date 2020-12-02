package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.RageCalculator;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.PetAbility;

/**
 * A pet ability to help the pet's owner, used every few seconds.
 * Effect: + 10 Rage Gain
 * 
 * @author Wauzmons
 * 
 * @see WauzPetAbilities
 */
@PetAbility
public class PetAbilityHatred implements WauzPetAbility {
	
	/**
	 * The particles used to display the ability's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.ORANGE);

	/**
	 * @return The name of the ability.
	 */
	@Override
	public String getAbilityName() {
		return "Hatred";
	}

	/**
	 * @return The description of the ability.
	 */
	@Override
	public String getAbilityDescription() {
		return "+ 10 Rage Gain";
	}

	/**
	 * @return The needed breeding level for the ability.
	 */
	@Override
	public int getAbilityLevel() {
		return 6;
	}

	/**
	 * Executes this ability for the given player and pet.
	 * 
	 * @param player The player bound to this runnable.
	 * @param pet The pet bound to this runnable.
	 */
	@Override
	public void use(Player player, Entity pet) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		Location originLocation = player.getLocation().clone().add(0, 1, 0);
		Location targetLocation = pet.getLocation().clone().add(0, 1, 0);
		ParticleSpawner.spawnParticleLine(originLocation, targetLocation, particle, 1);
		RageCalculator.generateRage(player, 10);
	}

}
