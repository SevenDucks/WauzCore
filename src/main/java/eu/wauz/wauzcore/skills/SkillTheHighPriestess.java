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
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.execution.SkillUtils;

public class SkillTheHighPriestess implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The High Priestess II";

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
		return "Area Heal";
	}

	@Override
	public int getCooldownSeconds() {
		return 21;
	}

	@Override
	public int getManaCost() {
		return 7;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		List<Player> targets = SkillUtils.getPlayersInRadius(player.getLocation(), 6);
		for(Player target : targets) {
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1, 0.8f);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			if(playerData == null) continue;
			
			ParticleSpawner.spawnParticleHelix(target.getLocation(), new SkillParticle(Particle.HEART), 1, 3.5);
			
			EntityRegainHealthEvent event = new EntityRegainHealthEvent(target, playerData.getMaxHealth() / 5, RegainReason.MAGIC);
			DamageCalculator.heal(event);
		}
		return true;
	}

}
