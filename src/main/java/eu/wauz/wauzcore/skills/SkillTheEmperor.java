package eu.wauz.wauzcore.skills;

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

import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillTheEmperor implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Emperor IV";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "AoE";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Gate of Babylon";
	}

	@Override
	public int getCooldownSeconds() {
		return 35;
	}

	@Override
	public int getManaCost() {
		return 8;
	}

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
            SkillUtils.spawnParticleSphere(attackLocation, gateParticle, 0.8);
            
            if(hasTargets) {
            	Entity target = targets.get(random.nextInt(targets.size()));
            	player.getWorld().playSound(attackLocation, Sound.BLOCK_BELL_USE, 1, 2);
            	SkillUtils.spawnParticleLine(attackLocation, target.getLocation(), beamParticle, 1);
            	SkillUtils.callPlayerMagicDamageEvent(player, target, 4);
            }
        }
		
		return hasTargets;
	}

}
