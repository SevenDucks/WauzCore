package eu.wauz.wauzcore.mobs.towers;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Tower;

/**
 * A defense tower to help players to defend themselves from enemies.
 * Effect: Restores Health of nearby Allies.
 * 
 * @author Wauzmons
 */
@Tower
public class TowerHealthRestorer implements DefenseTower {
	
	/**
	 * The particles used to display the tower's effect.
	 */
	private SkillParticle particle = new SkillParticle(Color.GREEN);
	
	/**
	 * @return The name of the tower.
	 */
	@Override
	public String getTowerName() {
		return "Health Restorer";
	}

	/**
	 * @return The item stack to use as the tower's head.
	 */
	@Override
	public ItemStack getHeadItemStack() {
		return new ItemStack(Material.MELON);
	}

	/**
	 * @return The item stack to use as the tower's body.
	 */
	@Override
	public ItemStack getBodyItemStack() {
		ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		leatherArmorMeta.setColor(Color.GREEN);
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
		List<Player> targets = SkillUtils.getPlayersInRadius(towerLocation, 7);
		
		if(!targets.isEmpty()) {
			Player target = targets.get(0);
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(target);
			if(playerData == null) {
				return;
			}
			ParticleSpawner.spawnParticleLine(towerLocation, target.getLocation(), particle, 1);
			DamageCalculator.heal(new EntityRegainHealthEvent(target, playerData.getMaxHealth() / 8, RegainReason.MAGIC));
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
