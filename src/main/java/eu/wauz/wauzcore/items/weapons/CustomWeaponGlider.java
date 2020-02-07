package eu.wauz.wauzcore.items.weapons;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;

/**
 * A collection of methods for using the glider weapon.
 * 
 * @author Wauzmons
 */
public class CustomWeaponGlider {
	
	/**
	 * Cancels the event of a feather interaction.
	 * Throws the player into the air, if they are sneaking.
	 * Otherwhise shoots a chicken, if it was a right click.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see CustomWeaponGlider#fly(Player)
	 * @see CustomWeaponGlider#shoot(Player)
	 */
	public static void use(PlayerInteractEvent event) {
		event.setCancelled(true);
		final Player player = event.getPlayer();
		if(player.isSneaking()) {
			fly(player);
		}
		else if(event.getAction().toString().contains("RIGHT")) {
			shoot(player);
		}
	}
	
	/**
	 * Shoots a chicken in the player's eye direction.
	 * The chicken explodes and spawns more chickens and AAAAAAAHHH!!
	 * Makes the glider loose 6 durability.
	 * 
	 * @param player The player who is shooting the chicken.
	 * 
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void shoot(Player player) {
//		ItemStack gliderItemStack = player.getEquipment().getItemInMainHand();
//		int damage = EquipmentUtils.getBaseAtk(gliderItemStack);
//		DurabilityCalculator.damageItem(player, gliderItemStack, 6, false);
	}
	
	/**
	 * Throws the player into the air and triggers gliding.
	 * Damages all entities inside a 2.5 block radius particle shape.
	 * Makes the glider loose 12 durability.
	 * 
	 * @param player The player who starts to fly.
	 * 
	 * @see SkillUtils#throwEntityIntoAir(Entity, double)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void fly(Player player) {
		Location origin = player.getLocation();
		
		player.getWorld().playSound(origin, Sound.ENTITY_CHICKEN_DEATH, 1, 0.75f);
		player.getWorld().playEffect(origin, Effect.ENDERDRAGON_GROWL, 0);
		SkillUtils.throwEntityIntoAir(player, 2.5);
		
		ItemStack gliderItemStack = player.getEquipment().getItemInMainHand();
		int damage = EquipmentUtils.getBaseAtk(gliderItemStack);
		List<Entity> targets = SkillUtils.getTargetsInRadius(origin, 2.5);
		for(Entity entity : targets) {
			SkillUtils.callPlayerFixedDamageEvent(player, entity, damage);
		}
		DurabilityCalculator.damageItem(player, gliderItemStack, 12, false);
	}

	/**
	 * Adds a chicken to the player's head, if it is not already there.
	 * Reduces the falling speed by 50% and lets the player slowly glide to their eye direction.
	 * 
	 * @param event The move event.
	 */
	public static void glide(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		boolean hasChick = false;
		for(Entity passanger : player.getPassengers()) {
			if(passanger instanceof Chicken) {
				hasChick = true;
			}
		}
		if(!hasChick) {
			Entity chick = WauzNmsClient.nmsCustomEntityChickoon(player.getLocation());
			WauzDebugger.log(player, "Added Head-Chick: " + player.addPassenger(chick));
		}
		if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
			Vector dir = player.getLocation().getDirection().multiply(0.2);
			player.setVelocity(new Vector(dir.getX(), player.getVelocity().getY() * 0.5, dir.getZ()));
		}
	}
	
	/**
	 * Removes all chickens from the player's head.
	 * 
	 * @param event The move event.
	 */
	public static void dechick(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		for(Entity passenger : player.getPassengers()) {
			if(passenger instanceof Chicken) {
				passenger.remove();
			}
		}
	}
	
	/**
	 * Cancels fall damage when holding a feather (glider) and makes it loose 2 durability.
	 * Also cancels suffocation damage for vehicle rides.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void cancelFallDamage(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		if(event.getCause().equals(DamageCause.FALL)) {
			ItemStack gliderItemStack = player.getEquipment().getItemInMainHand();
			if(gliderItemStack.getType().equals(Material.FEATHER)) {
				DurabilityCalculator.damageItem(player, gliderItemStack, 2, false);
				event.setDamage(0);
			}
		}
		if(event.getCause().equals(DamageCause.SUFFOCATION)) {
			if(player.isInsideVehicle()) {
				event.setDamage(0);
			}
		}
	}
	
}
