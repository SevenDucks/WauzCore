package eu.wauz.wauzcore.skills.skillgems;

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
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Skillgem;

/**
 * A skill, that can be executed by a player.
 * "Equivalent Exchange" swaps your location with an enemy and deals 100-750% damage to it
 * and heals you by 1-15% of your maximum health.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillJustice implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "Justice XI";

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
		return "Equivalent Exchange";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "EqExch";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 18;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 6;
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
			int randomHealingAmount = playerData.getStats().getMaxHealth() * ((random.nextInt(15) + 1) / 100) + 1;
			DamageCalculator.heal(new EntityRegainHealthEvent(player, randomHealingAmount, RegainReason.MAGIC));
			return true;
		}
		return false;
	}

}
