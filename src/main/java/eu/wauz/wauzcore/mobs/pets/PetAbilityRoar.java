package eu.wauz.wauzcore.mobs.pets;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A pet ability to help the pet's owner, used every few seconds.
 * Effect: 7s Damage Boost
 * 
 * @author Wauzmons
 * 
 * @see WauzPetAbilities
 */
public class PetAbilityRoar implements PetAbility {
	
	/**
	 * The particles used to display the ability's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.RED);

	/**
	 * @return The name of the ability.
	 */
	@Override
	public String getAbilityName() {
		return "Roar";
	}

	/**
	 * @return The description of the ability.
	 */
	@Override
	public String getAbilityDescription() {
		return "7s Damage Boost";
	}

	/**
	 * @return The needed breeding level for the ability.
	 */
	@Override
	public int getAbilityLevel() {
		return 4;
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
		SkillUtils.addPotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 7, 0);
	}

}
