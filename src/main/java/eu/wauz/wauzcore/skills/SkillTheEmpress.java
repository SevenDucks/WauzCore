package eu.wauz.wauzcore.skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A skill, that can be executed by a player.
 * "Lightning Chain" hits an enemy within 3 blocks and causes 250% damage.
 * Chains to up to 3 more enemies in a range of 7 blocks from the last target.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheEmpress implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Empress III";

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
		return WauzPlayerSkillType.MELEE;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Lightning Chain";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 20;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 8;
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
		Entity target = SkillUtils.getTargetInLine(player, 3);
		
		if(target != null) {
			lightningChain(player, player, target, 3, new ArrayList<Entity>());
			return true;
		}
		return false;
	}
	
	/**
	 * Help method to create the chaining lightning.
	 * 
	 * @param player The player who executes the skill.
	 * @param origin The origin of the lightning.
	 * @param target The target of the lightning.
	 * @param remainingChains How often the lightning can chain.
	 * @param excludes Entities that already have been hit.
	 */
	private void lightningChain(Player player, Entity origin, Entity target, int remainingChains, List<Entity> excludes) {
		Location originLocation = origin.getLocation();
		Location targetLocation = target.getLocation();
		originLocation.setY(originLocation.getY() + 1);
		targetLocation.setY(targetLocation.getY() + 1);
		
		player.getWorld().playSound(originLocation, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, (float) (0.8 + (remainingChains * 0.4)));
		ParticleSpawner.spawnParticleLine(originLocation, targetLocation, new SkillParticle(Particle.SPELL_WITCH), 3);
		SkillUtils.callPlayerMagicDamageEvent(player, target, 2.5);
		
		if(remainingChains > 0) {
			excludes.add(target);
			List<Entity> nextTargets = SkillUtils.getTargetsInRadius(target.getLocation(), 7, excludes);
			if(nextTargets.size() > 0)
				lightningChain(player, target, nextTargets.get(0), remainingChains - 1, excludes);
		}
	}

}
