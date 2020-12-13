package eu.wauz.wauzcore.skills.skillgems;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.SkillUtils.TotemRunnable;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Skillgem;

/**
 * A skill, that can be executed by a player.
 * "Projectile Totem" is a mob, that deals 150% damage to up to 3 enemies within 10 blocks, which attacks 5 times.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillTheTower implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Tower XVI";

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
		return "Projectile Totem";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "ProjcT";
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
		return 12;
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
		int damage = (int) ((double) EquipmentUtils.getBaseAtk(weapon) * (double) 1.50);
		
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1.5f);
		SkillUtils.spawnTotem(player, Material.BLAST_FURNACE, new TotemRunnable() {
			
			private SkillParticle particle = new SkillParticle(Color.SILVER);
			
			@Override
			public void run(Entity totem) {
				ParticleSpawner.spawnParticleCircle(totem.getLocation().add(0, 0.5, 0), particle, 1.5, 6);
				List<Entity> targets = SkillUtils.getTargetsInRadius(totem.getLocation(), 10);
				int hitTargets = 0;
				
				for(Entity target : targets) {
					ParticleSpawner.spawnParticleLine(totem.getLocation(), target.getLocation(), particle, 1);
					SkillUtils.callPlayerFixedDamageEvent(player, target, damage);
					if(++hitTargets == 3) {
						return;
					}
				}
			}
			
		}, 5, 40);
		
		return true;
	}

}
