package eu.wauz.wauzcore.mobs.towers;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;

/**
 * A defense tower to help players to defend themselves from enemies.
 * Effect: Slows Down nearby Enemies.
 * 
 * @author Wauzmons
 */
public class TowerFreezingPulse implements DefenseTower {
	
	/**
	 * The particles used to display the tower's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.BLUE);
	
	/**
	 * @return The name of the tower.
	 */
	@Override
	public String getTowerName() {
		return "Freezing Pulse";
	}

	/**
	 * @return The item stack to use as the tower's head.
	 */
	@Override
	public ItemStack getHeadItemStack() {
		return new ItemStack(Material.PACKED_ICE);
	}

	/**
	 * @return The item stack to use as the tower's body.
	 */
	@Override
	public ItemStack getBodyItemStack() {
		ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		leatherArmorMeta.setColor(Color.BLUE);
		itemStack.setItemMeta(leatherArmorMeta);
		return itemStack;
	}
	
	/**
	 * Executes this runnable for the given tower entity.
	 * 
	 * @param tower The tower entity bound to this runnable.
	 */
	@Override
	public void run(Entity tower) {
		Location towerLocation = tower.getLocation();
		ParticleSpawner.spawnParticleCircle(towerLocation.clone().add(0, 0.5, 0), particle, 1.5, 6);
		List<Entity> targets = SkillUtils.getTargetsInRadius(towerLocation, 5);
		
		for(Entity target : targets) {
			ParticleSpawner.spawnParticleLine(towerLocation, target.getLocation(), particle, 1);
			SkillUtils.addPotionEffect(targets, PotionEffectType.SLOW, 3, 1);
		}
	}

	/**
	 * @return The server ticks between each execution of the tower's effect.
	 */
	@Override
	public int getInterval() {
		return 60;
	}

}
