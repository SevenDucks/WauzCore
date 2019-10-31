package eu.wauz.wauzcore.skills;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.commands.WauzDebugger;

public class SkillTheHangedMan implements WauzPlayerSkill {
	
	public static String SKILL_NAME = "The Hanged Man XII";

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
		return "Steal";
	}

	@Override
	public int getCooldownSeconds() {
		return 30;
	}

	@Override
	public int getManaCost() {
		return 5;
	}

	@Override
	public boolean executeSkill(final Player player, ItemStack weapon) {
		Entity target = SkillUtils.getTargetInLine(player, 3);
		
		if(target != null) {
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 0.75f);
			SkillParticle particle = new SkillParticle(Particle.SQUID_INK);
			ParticleSpawner.spawnParticleCircle(target.getLocation(), particle, 1, 6);

			Random random = new Random();
			switch (random.nextInt(3) + 1) {
			case 1:
				long money = PlayerConfigurator.getCharacterCoins(player);
				long added = (int) ((random.nextInt(21) + 10) * PlayerPassiveSkillConfigurator.getTradingFloat(player));
				PlayerConfigurator.setCharacterCoins(player, money + added);
				player.sendMessage(ChatColor.LIGHT_PURPLE + "You stole " + added + " COINS from the enemy!");
				break;
			case 2:
				double earnedxp = ((double) (random.nextInt(5) + 3)) / 100.0; 
				int tier = player.getLevel();
				int displayexp = WauzRewards.level(player, tier, earnedxp, target.getLocation());
				player.sendMessage(ChatColor.LIGHT_PURPLE + "You stole " + displayexp + " EXP from the enemy!");
				break;
			case 3:
				int saturation = random.nextInt(4) + 4;
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
