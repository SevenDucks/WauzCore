package eu.wauz.wauzcore.mobs.towers;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Tower;

/**
 * A defense tower to help players to defend themselves from enemies.
 * Effect: Knocks Back nearby Enemies.
 * 
 * @author Wauzmons
 */
@Tower
public class TowerKnockbackCannon implements DefenseTower {
	
	/**
	 * The particles used to display the tower's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.YELLOW);
	
	/**
	 * @return The name of the tower.
	 */
	@Override
	public String getTowerName() {
		return "Knockback Cannon";
	}

	/**
	 * @return The item stack to use as the tower's head.
	 */
	@Override
	public ItemStack getHeadItemStack() {
		return new ItemStack(Material.GLOWSTONE);
	}

	/**
	 * @return The item stack to use as the tower's body.
	 */
	@Override
	public ItemStack getBodyItemStack() {
		ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		leatherArmorMeta.setColor(Color.YELLOW);
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
		List<Entity> targets = SkillUtils.getTargetsInRadius(towerLocation, 10);
		
		if(!targets.isEmpty()) {
			Entity target = targets.get(0);
			ParticleSpawner.spawnParticleLine(towerLocation, target.getLocation(), particle, 1);
			SkillUtils.throwBackEntity(target, towerLocation, 1.4);
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
