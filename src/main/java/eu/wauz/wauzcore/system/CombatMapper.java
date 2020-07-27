package eu.wauz.wauzcore.system;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import eu.wauz.wauzcore.items.weapons.CustomWeaponBow;
import eu.wauz.wauzcore.items.weapons.CustomWeaponGlider;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.system.util.DeprecatedUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used for mapping combat events to WauzCore functionalities.
 * 
 * @author Wauzmons
 */
public class CombatMapper {
	
	/**
	 * Handles general damage calculation.
	 * Defense boni, boss bars etc. are applied for players in MMORPG mode.
	 * 
	 * @param event The damage event.
	 * 
	 * @see CombatMapper#shouldCancelDamageEvent(EntityDamageEvent)
	 * @see CombatMapper#handleDamageByEntityEvent(EntityDamageByEntityEvent, WauzPlayerBossBar)
	 * @see CustomWeaponGlider#cancelFallDamage(EntityDamageEvent)
	 * @see DamageCalculator#defend(EntityDamageEvent)
	 * @see DeprecatedUtils#removeDamageModifiers(EntityDamageEvent)
	 * @see WauzPlayerBossBar#updateBossBar(double)
	 */
	public static void handleDamageEvent(EntityDamageEvent event) {
		if(shouldCancelDamageEvent(event)) {
			event.setCancelled(true);
			return;
		}
		
		WauzPlayerBossBar playerBossBar = WauzPlayerBossBar.getBossBar(event.getEntity());
		if(event instanceof EntityDamageByEntityEvent) {
			handleDamageByEntityEvent((EntityDamageByEntityEvent) event, playerBossBar);
		}
		if(event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			event.setCancelled(true);
			CustomWeaponGlider.cancelFallDamage(event);
			DamageCalculator.defend(event);
		}
		DeprecatedUtils.removeDamageModifiers(event);
		if(playerBossBar != null) {
			playerBossBar.updateBossBar(event.getDamage());
		}
	}
	
	/**
	 * Handles damage calculation of an entity attacking an other entity.
	 * Prevents pvp in non-pvp regions and harm from own projectiles.
	 * Dmage is multiplied by 100, if the damager is in attack debug mode.
	 * Attack boni, reflection, boss bars etc. are applied for players in MMORPG mode.
	 * 
	 * @param event The damage event.
	 * @param playerBossBar The boss bar that may be shown.
	 * 
	 * @see WauzRegion#disallowPvP(org.bukkit.entity.Entity, org.bukkit.entity.Entity)
	 * @see CustomWeaponBow#cancelArrowImpact(EntityDamageByEntityEvent)
	 * @see WauzDebugger#toggleAttackDebugMode(Player)
	 * @see DamageCalculator#attack(EntityDamageByEntityEvent)
	 * @see DamageCalculator#reflect(EntityDamageByEntityEvent)
	 * @see WauzPlayerBossBar#addPlayer(Player, double)
	 */
	private static void handleDamageByEntityEvent(EntityDamageByEntityEvent event, WauzPlayerBossBar playerBossBar) {
		if(WauzRegion.disallowPvP(event.getDamager(), event.getEntity())) {
			event.setCancelled(true);
			return;
		}
		if(event.getDamager() instanceof Arrow && CustomWeaponBow.cancelArrowImpact(event)) {
			event.setCancelled(true);
			return;
		}
		if(event.getDamager().hasPermission(WauzPermission.DEBUG_ATTACK.toString())) {
			event.setDamage(event.getDamage() * 100);
		}
		if(!WauzMode.isMMORPG(event.getEntity())) {
			return;
		}
		if(event.getDamager() instanceof Player) {
			DamageCalculator.attack(event);
			if(event.isCancelled()) {
				return;
			}
			if(playerBossBar != null) {
				playerBossBar.addPlayer((Player) event.getDamager(), event.getDamage());
			}
		}
		if(event.getEntity() instanceof Player) {
			DamageCalculator.reflect(event);
		}
	}
	
	/**
	 * Decides if the damage event should be cancelled.
	 * Cancels out damage in hub or defense debug mode, damage to pets or damage from cosmetic explosions.
	 * 
	 * @param event The damage event.
	 * 
	 * @return If the event should be cancelled.
	 * 
	 * @see WauzMode#inHub(org.bukkit.entity.Entity)
	 * @see PetOverviewMenu#getOwner(org.bukkit.entity.Entity)
	 * @see WauzDebugger#toggleDefenseDebugMode(Player)
	 */
	private static boolean shouldCancelDamageEvent(EntityDamageEvent event) {
		return WauzMode.inHub(event.getEntity())
				|| PetOverviewMenu.getOwner(event.getEntity()) != null
				|| event.getEntity().hasPermission(WauzPermission.DEBUG_DEFENSE.toString())
				|| event.getCause().equals(DamageCause.BLOCK_EXPLOSION);
	}

}