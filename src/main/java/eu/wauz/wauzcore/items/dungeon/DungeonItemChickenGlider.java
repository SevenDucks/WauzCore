package eu.wauz.wauzcore.items.dungeon;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.system.commands.WauzDebugger;

public class DungeonItemChickenGlider {
	
	public static void use(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		Location target = null;

		for(Block block : player.getLineOfSight(null, 15)) {
			if(block.getType().equals(Material.SPONGE)) {
				target = block.getLocation();
				Location signal = new Location(target.getWorld(), target.getX(), target.getY() + 1.5, target.getZ());
				target.getWorld().playEffect(signal, Effect.ENDER_SIGNAL, 0);
				break;
			}
		}
		
		if(target == null) {
			event.setCancelled(true);
			return;
		}
		
		if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().equals(target)) {
			player.getWorld().playEffect(target, Effect.ENDERDRAGON_GROWL, 0);
			player.setVelocity(new Vector(0, 3, 0));
		}
	}

	public static void glide(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		boolean hasChick = false;
		for(Entity passanger : player.getPassengers()) {
			if(passanger instanceof Chicken)
				hasChick = true;
		}
		if(!hasChick) {
			Location location = player.getLocation();
			Chicken chick = location.getWorld().spawn(location, Chicken.class);
			chick.setInvulnerable(true);
			chick.setAdult();
			WauzDebugger.log(player, "Added Head-Chick: " + player.addPassenger(chick));
		}
		
		if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
			Vector dir = player.getLocation().getDirection().multiply(0.2);
			player.setVelocity(new Vector(dir.getX(), player.getVelocity().getY() * 0.5, dir.getZ()));
		}
	}
	
	public static void dechick(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		for(Entity passenger : player.getPassengers()) {
			if(passenger instanceof Chicken)
				passenger.remove();
		}
	}
	
	public static void cancelFallDamage(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		if(event.getCause().equals(DamageCause.FALL))		
			if(player.getEquipment().getItemInMainHand().getType().equals(Material.FEATHER))
				event.setDamage(0);
		
		if(event.getCause().equals(DamageCause.SUFFOCATION))
			if(player.isInsideVehicle())
				event.setDamage(0);
	}
	
}
