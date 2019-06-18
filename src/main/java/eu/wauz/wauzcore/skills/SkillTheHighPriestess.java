package eu.wauz.wauzcore.skills;

import java.util.List;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillMechanics;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillParticle;

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
	public boolean executeSkill(final Player player) {
		List<Player> targets = WauzPlayerSkillMechanics.getPlayersInRadius(player.getLocation(), 6);
		for(Player target : targets) {
			WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
			if(pd == null) continue;
			
			WauzPlayerSkillMechanics.spawnParticleHelix(target.getLocation(), new WauzPlayerSkillParticle(Particle.HEART), 1, 3.5);
			
			EntityRegainHealthEvent event = new EntityRegainHealthEvent(target, pd.getMaxHealth() / 5, RegainReason.MAGIC);
			DamageCalculator.heal(event);
		}
		return true;
	}

}
