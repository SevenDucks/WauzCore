package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.PetAbility;

/**
 * A pet ability to help the pet's owner, used every few seconds.
 * Effect: + 12.5% Health Gain
 * 
 * @author Wauzmons
 * 
 * @see WauzPetAbilities
 */
@PetAbility
public class PetAbilityMend implements WauzPetAbility {
	
	/**
	 * The particles used to display the ability's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.GREEN);

	/**
	 * @return The name of the ability.
	 */
	@Override
	public String getAbilityName() {
		return "Mend";
	}

	/**
	 * @return The description of the ability.
	 */
	@Override
	public String getAbilityDescription() {
		return "+ 12.5% Health Gain";
	}

	/**
	 * @return The needed breeding level for the ability.
	 */
	@Override
	public int getAbilityLevel() {
		return 2;
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
		DamageCalculator.heal(new EntityRegainHealthEvent(player, playerData.getMaxHealth() / 8, RegainReason.MAGIC));
	}

}
