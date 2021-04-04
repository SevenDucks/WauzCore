package eu.wauz.wauzcore.skills.skillgems;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillType;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.annotations.Skillgem;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A skill, that can be executed by a player.
 * "Steal" steals a random amount of either coins or saturation from an enemy within 3 blocks.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerSkill
 */
@Skillgem
public class SkillTheHangedMan implements WauzPlayerSkill {
	
	/**
	 * The static name of the skill.
	 */
	public static final String SKILL_NAME = "The Hanged Man XII";

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
		return "Steal";
	}
	
	/**
	 * @return The effect of the skill shown in quick slots.
	 */
	@Override
	public String getSkillQuickSlotEffect() {
		return "Steal";
	}

	/**
	 * @return The cooldown of the skill in seconds.
	 */
	@Override
	public int getCooldownSeconds() {
		return 30;
	}

	/**
	 * @return The mana cost of the skill.
	 */
	@Override
	public int getManaCost() {
		return 10;
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
		Entity target = SkillUtils.getTargetInLine(player, 3);
		
		if(target != null) {
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 0.75f);
			SkillParticle particle = new SkillParticle(Particle.SQUID_INK);
			ParticleSpawner.spawnParticleCircle(target.getLocation(), particle, 1, 6);

			switch (Chance.randomInt(2) + 1) {
			case 1:
				long money = PlayerCollectionConfigurator.getCharacterCoins(player);
				long added = (int) ((Chance.randomInt(21) + 10) * PlayerSkillConfigurator.getTradingFloat(player));
				PlayerCollectionConfigurator.setCharacterCoins(player, money + added);
				AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, added);
				player.sendMessage(ChatColor.LIGHT_PURPLE + "You stole " + added + " COINS from the enemy!");
				break;
			case 2:
				int saturation = Chance.randomInt(4) + 4;
				player.setFoodLevel(player.getFoodLevel() + saturation);
				player.setSaturation(5);
				player.sendMessage(ChatColor.LIGHT_PURPLE + "You stole " + saturation + " SATURATION from the enemy!");
				break;
			default:
				WauzDebugger.log("An error in the matrix occured.");
				break;
			}
			return true;
		}
		return false;
	}

}
