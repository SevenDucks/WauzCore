package eu.wauz.wauzcore.skills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;

public class SkillTheStar implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Star XVII";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "Melee";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Multipunch";
	}

	@Override
	public int getCooldownSeconds() {
		return 8;
	}

	@Override
	public int getManaCost() {
		return 4;
	}

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
