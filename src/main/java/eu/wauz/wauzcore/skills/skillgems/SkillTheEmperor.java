package eu.wauz.wauzcore.skills.skillgems;

import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Skillgem;

/**
 * A skill, that can be executed by a player.
 * "Gate of Babylon" shoots 10 weapons on random enemies in a 8 block radius,
 * each dealing up to 400% damage.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillTheEmperor implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Emperor IV";

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
		return "Gate of Babylon";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "GoBbln";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 35;
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
		Location location = null;
		for(Block block : player.getLineOfSight(null, 12)) {
			location = block.getLocation();
			if(!block.getType().equals(Material.AIR)) {
				break;
			}
		}
		
		int amount = 10;
		int radius = 8;
		
		Random random = new Random();
		List<Entity> targets = SkillUtils.getTargetsInRadius(location, radius);
		boolean hasTargets = !targets.isEmpty();
		
		double increment = (2 * Math.PI) / amount;
        for(int iterator = 0; iterator < amount; iterator++)
        {
            double angle = iterator * increment;
            double x = location.getX() + (radius * Math.cos(angle));
            double z = location.getZ() + (radius * Math.sin(angle));
            Location attackLocation = new Location(location.getWorld(), x, location.getY() + 8, z);
            
            SkillParticle beamParticle = new SkillParticle(Color.ORANGE);
            SkillParticle gateParticle = new SkillParticle(Color.YELLOW);
            ParticleSpawner.spawnParticleSphere(attackLocation, gateParticle, 0.8);
            
            if(hasTargets) {
            	Entity target = targets.get(random.nextInt(targets.size()));
            	player.getWorld().playSound(attackLocation, Sound.BLOCK_BELL_USE, 1, 2);
            	ParticleSpawner.spawnParticleLine(attackLocation, target.getLocation(), beamParticle, 1);
            	SkillUtils.callPlayerMagicDamageEvent(player, target, 4);
            }
        }
		
		return hasTargets;
	}

}
