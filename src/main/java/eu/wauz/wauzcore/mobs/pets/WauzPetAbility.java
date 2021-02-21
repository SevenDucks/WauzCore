package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A pet ability to help the pet's owner, used every few seconds.
 * 
 * @author Wauzmons
 * 
 * @see WauzPetAbilities
 */
public interface WauzPetAbility {
	
	/**
	 * @return The name of the ability.
	 */
	public String getAbilityName();
	
	/**
	 * @return The description of the ability.
	 */
	public String getAbilityDescription();
	
	/**
	 * @return The needed breeding level for the ability.
	 */
	public int getAbilityLevel();
	
	/**
	 * Executes this ability for the given player and pet.
	 * 
	 * @param player The player bound to this runnable.
	 * @param pet The pet bound to this runnable.
	 */
	public void use(Player player, Entity pet);
	
	/**
	 * Executes the particle effect of this ability.
	 * 
	 * @param player The player bound to this runnable.
	 * @param pet The pet bound to this runnable.
	 * @param particle The particles used to display the ability's effect.
	 */
	public default void spawnParticles(Player player, Entity pet, SkillParticle particle) {
		Location originLocation = player.getLocation().clone().add(0, 1, 0);
		Location targetLocation = pet.getLocation().clone().add(0, 1, 0);
		ParticleSpawner.spawnParticleLine(originLocation, targetLocation, particle, 1);
	}

}
