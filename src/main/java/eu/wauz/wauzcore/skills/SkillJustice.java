package eu.wauz.wauzcore.skills;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

public class SkillJustice implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "Justice XI";

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
		return "Equivalent Exchange";
	}

	@Override
	public int getCooldownSeconds() {
		return 18;
	}

	@Override
	public int getManaCost() {
		return 6;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Entity target = SkillUtils.getTargetInLine(player, 3);
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(target != null && playerData != null) {
			Random random = new Random();
			
			Location playerLocation = player.getLocation();
			Location targetLocation = target.getLocation();
			if(!player.isInsideVehicle() && !target.isInsideVehicle()) {
				target.teleport(playerLocation);
				player.teleport(targetLocation);
			}
			
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_END_GATEWAY_SPAWN, 1, 0.5f);
			target.getWorld().playSound(target.getLocation(), Sound.BLOCK_END_GATEWAY_SPAWN, 1, 0.5f);
			SkillParticle whiteParticle = new SkillParticle(Color.WHITE);
			SkillParticle blackParticle = new SkillParticle(Color.BLACK);
			ParticleSpawner.spawnParticleHelix(targetLocation, whiteParticle, 0.75, 2);
			ParticleSpawner.spawnParticleHelix(playerLocation, blackParticle, 0.75, 2);
			
			double randomDamageMultiplier = (random.nextInt(651) + 100) / 100;
			SkillUtils.callPlayerMagicDamageEvent(player, target, randomDamageMultiplier);
			int randomHealingAmount = playerData.getMaxHealth() * ((random.nextInt(15) + 1) / 100) + 1;
			DamageCalculator.heal(new EntityRegainHealthEvent(player, randomHealingAmount, RegainReason.MAGIC));
			return true;
		}
		return false;
	}

}
