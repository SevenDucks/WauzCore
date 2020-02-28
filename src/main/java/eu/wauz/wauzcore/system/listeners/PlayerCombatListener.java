package eu.wauz.wauzcore.system.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemDamageEvent;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.weapons.CustomWeaponBow;
import eu.wauz.wauzcore.items.weapons.CustomWeaponGlider;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.util.DeprecatedUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to player combat.
 * 
 * @author Wauzmons
 */
public class PlayerCombatListener implements Listener {
	
	/**
	 * Handles damage calculation in MMORPG mode.
	 * In attack debug mode, the damage output is multiplied by 100.
	 * Cancels out damage in hub or defense debug mode, damage to pets or damage from cosmetic explosions.
	 * Prevents damaging players in non PvP areas and fall damage when gliding.
	 * 
	 * Also updates the boss bar if one exists.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#attack(EntityDamageByEntityEvent)
	 * @see DamageCalculator#reflect(EntityDamageByEntityEvent)
	 * @see DamageCalculator#defend(EntityDamageEvent)
	 * @see DeprecatedUtils#removeDamageModifiers(EntityDamageEvent)
	 * @see WauzPlayerBossBar#updateBossBar(double)
	 * @see WauzDebugger#toggleAttackDebugMode(Player)
	 * @see WauzDebugger#toggleDefenseDebugMode(Player)
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(WauzMode.inHub(event.getEntity())
				|| PetOverviewMenu.getOwner(event.getEntity()) != null
				|| event.getEntity().hasPermission(WauzPermission.DEBUG_DEFENSE.toString())
				|| event.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
			event.setCancelled(true);
			return;
		}
		WauzPlayerBossBar playerBossBar = WauzPlayerBossBar.getBossBar(event.getEntity());
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
			
			if(WauzRegion.disallowPvP(entityEvent.getDamager(), event.getEntity())) {
				event.setCancelled(true);
				return;
			}
			if(entityEvent.getDamager() instanceof Arrow && CustomWeaponBow.cancelArrowImpact(entityEvent)) {
				event.setCancelled(true);
				return;
			}
			if(entityEvent.getDamager().hasPermission(WauzPermission.DEBUG_ATTACK.toString())) {
				event.setDamage(event.getDamage() * 100);
			}
			if(!WauzMode.isMMORPG(event.getEntity())) {
				return;
			}
			if(entityEvent.getDamager() instanceof Player) {
				DamageCalculator.attack(entityEvent);
				if(event.isCancelled()) {
					return;
				}
				if(playerBossBar != null) {
					playerBossBar.addPlayer((Player) entityEvent.getDamager(), entityEvent.getDamage());
				}
			}
			if(event.getEntity() instanceof Player) {
				DamageCalculator.reflect(entityEvent);
			}
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
	 * Handles healing calculation in MMORPG mode.
	 * Also updates the boss bar if one exists.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#heal(EntityRegainHealthEvent)
	 * @see WauzPlayerBossBar#updateBossBar(double)
	 */
	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		if(event.getEntity() instanceof Player && WauzMode.isMMORPG(event.getEntity())) {
			DamageCalculator.heal(event);
		}
		WauzPlayerBossBar playerBossBar = WauzPlayerBossBar.getBossBar(event.getEntity());
		if(playerBossBar != null) {
			playerBossBar.updateBossBar(- event.getAmount());
		}
	}

	/**
	 * Handles the killing of entities in MMORPG mode.
	 * This handler focuses on effects like life leech caused by the player,
	 * not on the actual death or loot drops of the entity.
	 * 
	 * @param event
	 * 
	 * @see DamageCalculator#kill(EntityDeathEvent)
	 * @see MythicMobsListener#onMythicDeath(MythicMobDeathEvent) "Real" death event handler
	 */
	@EventHandler
	public void onKill(EntityDeathEvent event) {
		if(event.getEntity().getKiller() != null && WauzMode.isMMORPG(event.getEntity().getKiller())) {
			DamageCalculator.kill(event);
		}
	}
	
	/**
	 * Prevents item damage in MMORPG mode.
	 * The equipment there has its own durability system.
	 * 
	 * @param event
	 * 
	 * @see DurabilityCalculator
	 */
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {
		if(WauzMode.isMMORPG(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

}
