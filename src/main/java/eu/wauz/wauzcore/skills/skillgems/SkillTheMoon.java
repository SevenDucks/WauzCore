package eu.wauz.wauzcore.skills.skillgems;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Skillgem;

/**
 * A skill, that can be executed by a player.
 * "Lunaris Beam" fires a frost beam at an enemy within 15 blocks, which deals 200% damage and freezes it for 3 seconds,
 * making it unable to move in that timeframe.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillTheMoon implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Moon XVIII";

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
		return WauzPlayerSkillType.RANGED;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Lunaris Beam";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "LuBeam";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 12;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 4;
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
		Entity target = SkillUtils.getTargetInLine(player, 15);
		
		if(target != null) {
			Location originLocation = player.getLocation().clone().add(0, 1, 0);
			Location targetLocation = target.getLocation().clone().add(0, 1, 0);
			player.getWorld().playSound(originLocation, Sound.BLOCK_GLASS_BREAK, 1, 0.5f);
			SkillParticle particle = new SkillParticle(Particle.DRIP_WATER);
			ParticleSpawner.spawnParticleLine(originLocation, targetLocation, particle, 1, 0.25);
			ParticleSpawner.spawnParticleWave(target.getLocation(), particle, 2);
			SkillUtils.callPlayerMagicDamageEvent(player, target, 2);
			SkillUtils.addPotionEffect(target, PotionEffectType.SLOW, 3, 200);
			SkillUtils.addPotionEffect(target, PotionEffectType.JUMP, 3, 200);
			return true;
		}
		return false;
	}

}
