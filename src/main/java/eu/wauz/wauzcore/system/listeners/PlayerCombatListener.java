package eu.wauz.wauzcore.system.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import eu.wauz.wauzcore.system.CombatMapper;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to player combat.
 * 
 * @author Wauzmons
 */
public class PlayerCombatListener implements Listener {
	
	/**
	 * Handles general damage calculation.
	 * A combat mapper is used for mapping combat events to WauzCore functionalities.
	 * 
	 * @param event
	 * 
	 * @see CombatMapper#handleDamageEvent(EntityDamageEvent)
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		CombatMapper.handleDamageEvent(event);
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
