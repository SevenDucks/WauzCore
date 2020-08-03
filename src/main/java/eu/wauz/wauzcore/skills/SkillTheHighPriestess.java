package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A skill, that can be executed by a player.
 * "Area Heal" heals all players in a radius of 6 blocks, by 20% of their maximum health.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheHighPriestess implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The High Priestess II";

	/**
	 * @return The id of the skill.
	 */
	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	/**
	 * @return The type of the skill shown in the default description.
	 */
	@Override
	public WauzPlayerSkillType getSkillDescriptionType() {
		return WauzPlayerSkillType.AOE;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Area Heal";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 21;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 7;
	}
	
	/**
	 * @return If rage should be used for this skill instead of mana.
	 */
	@Override
	public boolean isPhysical() {
		return false;
	}

	/**
	 * Executes the skill for the given player.
	 * 
	 * @param player The player who executes the skill.
	 * @param weapon The weapon that player uses for it.
	 * 
	 * @return If the skill hit something.
	 */
	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		List<Player> targets = SkillUtils.getPlayersInRadius(player.getLocation(), 6);
		for(Player target : targets) {
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1, 0.8f);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			if(playerData == null) {
				continue;
			}
			
			ParticleSpawner.spawnParticleHelix(target.getLocation(), new SkillParticle(Particle.HEART), 1, 3.5);
			DamageCalculator.heal(new EntityRegainHealthEvent(target, playerData.getMaxHealth() / 5, RegainReason.MAGIC));
		}
		return true;
	}

}
