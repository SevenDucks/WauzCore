package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.skills.execution.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils.TotemRunnable;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

public class SkillTheFool implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Fool 0";

	@Override
	public String getSkillId() {
		return SKILL_NAME;
	}
	
	@Override
	public String getSkillDescriptionType() {
		return "Summon";
	}

	@Override
	public String getSkillDescriptionEffect() {
		return "Decoy Totem";
	}

	@Override
	public int getCooldownSeconds() {
		return 45;
	}

	@Override
	public int getManaCost() {
		return 4;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		SkillUtils.spawnTotem(player, Material.CARVED_PUMPKIN, new TotemRunnable() {
			
			private BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
			
			private SkillParticle particle = new SkillParticle(Particle.TOTEM);
			
			@Override
			public void run(Entity totem) {
				SkillUtils.spawnParticleCircle(totem.getLocation().add(0, 0.5, 0), particle, 1.5, 6);
				List<Entity> targets = SkillUtils.getTargetsInRadius(totem.getLocation(), 10);
				
				for(Entity target : targets) {
					mythicMobs.taunt(target, (LivingEntity) totem);
				}
			}
			
		}, 20, 10);
		
		return true;
	}

}
