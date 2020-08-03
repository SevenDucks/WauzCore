package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils.TotemRunnable;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;

/**
 * A skill, that can be executed by a player.
 * "Decoy Totem" is a mob, that taunts all enemies within 10 blocks,
 * forcing them to attack it, which lasts 10 seconds.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
public class SkillTheFool implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Fool 0";

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
		return WauzPlayerSkillType.SUMMON;
	}

	/**
	 * @return The effect of the skill shown in the default description.
	 */
	@Override
	public String getSkillDescriptionEffect() {
		return "Decoy Totem";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 45;
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
		return true;
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
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1.5f);
		SkillUtils.spawnTotem(player, Material.CARVED_PUMPKIN, new TotemRunnable() {
			
			private BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
			
			private SkillParticle particle = new SkillParticle(Particle.TOTEM);
			
			@Override
			public void run(Entity totem) {
				ParticleSpawner.spawnParticleCircle(totem.getLocation().clone().add(0, 0.5, 0), particle, 1.5, 6);
				List<Entity> targets = SkillUtils.getTargetsInRadius(totem.getLocation(), 10);
				
				for(Entity target : targets) {
					mythicMobs.taunt(target, (LivingEntity) totem);
				}
			}
			
		}, 20, 10);
		
		return true;
	}

}
