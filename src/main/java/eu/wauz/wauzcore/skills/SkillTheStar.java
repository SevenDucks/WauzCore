package eu.wauz.wauzcore.skills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.annotations.Skillgem;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

/**
 * A skill, that can be executed by a player.
 * "Multipunch" hits an enemy within 3 blocks and causes 450% damage over the course of 3 automated attacks.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillTheStar implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Star XVII";

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
		return "Multipunch";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "OraOra";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 8;
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
		final Entity target = SkillUtils.getTargetInLine(player, 3);
		
		if(target != null) {
			for(int iterator = 0; iterator != 12; iterator++) {
				final int finalIterator = iterator;
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
					
		            public void run() {
		            	try {
		            		if(target == null || !target.isValid())
		            			return;
		            		
		            		Location location = target.getLocation();
		            		location.setY(location.getY() + 1);
		            		
		            		location.getWorld().playSound(location, Sound.ENTITY_VILLAGER_AMBIENT, 1, 0.8f);
		            		location.getWorld().playSound(location, Sound.ENTITY_VILLAGER_AMBIENT, 1, 0.5f);
		            		new SkillParticle(Particle.SWEEP_ATTACK).spawn(location, 2);
		            		
		            		if(finalIterator == 2 || finalIterator == 6 || finalIterator == 10)
		            			SkillUtils.callPlayerMagicDamageEvent(player, target, 1.5);
		            	}
		            	catch (NullPointerException e) {
		            		WauzDebugger.catchException(getClass(), e);
		            	}
		            }
		            
				}, iterator * 4);
			}
			return true;
		}
		return false;
	}

}
