package eu.wauz.wauzcore.system.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import eu.wauz.wauzcore.items.weapons.CustomWeaponBow;
import eu.wauz.wauzcore.items.weapons.CustomWeaponGlider;
import eu.wauz.wauzcore.items.weapons.CustomWeaponHook;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A listener to catch events, related to projectile and general entity movements.
 * 
 * @author Wauzmons
 */
public class ProjectileMovementListener implements Listener {
	
	/**
	 * Handles the mechanics of hooks in MMORPG mode.
	 * 
	 * @param event
	 * 
	 * @see CustomWeaponHook#use(ProjectileLaunchEvent)
	 */
	@EventHandler
	public void onHookLaunch(ProjectileLaunchEvent event) {
		if(WauzMode.isMMORPG(event.getEntity()) && event.getEntityType().equals(EntityType.FISHING_HOOK)) {
			CustomWeaponHook.use(event);
		}
	}

	/**
	 * Handles the instant despawn of arrows in MMORPG mode.
	 * 
	 * @param event
	 * 
	 * @see CustomWeaponBow
	 */
	@EventHandler
	public void onArrowHit(ProjectileHitEvent event) {
		if(WauzMode.isMMORPG(event.getEntity()) && event.getEntityType().equals(EntityType.ARROW)) {
			event.getEntity().remove();
		}
	}

	/**
	 * Handles glider mechanics in MMORPG mode, aswell as flying-permissions.
	 * Flying in every region, besides the hub, will be allowed in flying debug mode.
	 * 
	 * @param event
	 * 
	 * @see CustomWeaponGlider#glide(PlayerMoveEvent)
	 * @see WauzDebugger#toggleFlyingDebugMode(Player)
	 */
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(WauzMode.inHub(player)) {
			if(player.isOnGround()) {
				event.getPlayer().setAllowFlight(true);
			}
		}
		else {
			event.getPlayer().setAllowFlight(player.hasPermission(WauzPermission.DEBUG_FLYING.toString())
					|| player.getGameMode().equals(GameMode.CREATIVE)
					|| player.getGameMode().equals(GameMode.SPECTATOR));
		}
		
		if(WauzMode.isMMORPG(player)) {
			if (player.getEquipment().getItemInMainHand().getType().equals(Material.FEATHER)) {
				CustomWeaponGlider.glide(event);
			}
			else {
				CustomWeaponGlider.dechick(event);
			}
		}
	}

}
