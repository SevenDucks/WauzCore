package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.nms.NmsEntityChickoon;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A collection of methods for using the glider weapon.
 * 
 * @author Wauzmons
 */
@Item
public class CustomWeaponGlider implements CustomWeapon {
	
	/**
	 * A particle, used to represent chicken feathers.
	 */
	private static SkillParticle particle = new SkillParticle(Color.WHITE);
	
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
	@Override
	public void use(PlayerInteractEvent event) {
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
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.FEATHER);
	}
	
	/**
	 * Determines if the custom weapon can have a skillgem slot.
	 * 
	 * @return If the custom weapon can have a skillgem slot.
	 */
	@Override
	public boolean canHaveSkillSlot() {
		return false;
	}

	/**
	 * Gets the lores to show on an instance of the custom weapon.
	 * 
	 * @param hasSkillSlot If the weapon has a skillgem slot.
	 */
	@Override
	public List<String> getCustomWeaponLores(boolean hasSkillSlot) {
		List<String> lores = new ArrayList<>();
		lores.add("");
		lores.add(ChatColor.GRAY + "Use while Sneaking to fly into the Air");
		lores.add(ChatColor.GRAY + "Right Click to throw Chickens");
		return lores;
	}
	
	/**
	 * Shoots a chicken in the player's eye direction.
	 * The chicken explodes and spawns more chickens and AAAAAAAHHH!!
	 * Makes the glider loose 4 durability.
	 * 
	 * @param player The player who is shooting the chicken.
	 * 
	 * @see Cooldown#playerProjectileShoot(Player)
	 * @see NmsEntityChickoon
	 * @see CustomWeaponGlider#spawnTemporaryChicken(Location, int)
	 * @see SkillUtils#createExplosion(Location, float)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void shoot(Player player) {
		Location origin = player.getLocation();
		if(!Cooldown.playerProjectileShoot(player)) {
			return;
		}
		
		playChickenSounds(origin);
		ParticleSpawner.spawnParticleHelix(origin, particle, 1, 2);
		
		ItemStack gliderItemStack = player.getEquipment().getItemInMainHand();
		int damage = (int) (EquipmentUtils.getBaseAtk(gliderItemStack) * 1.5);
		DurabilityCalculator.damageItem(player, gliderItemStack, 4, false);
		
		Entity chicken = origin.getWorld().spawnEntity(origin.clone().add(0, 0.5, 0), EntityType.CHICKEN);
		WauzNmsClient.nmsEntityPersistence(chicken, false);
		chicken.setInvulnerable(true);
		chicken.setVelocity(origin.getDirection().multiply(1.25));
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	if(chicken != null && chicken.isValid()) {
        			SkillUtils.createExplosion(chicken.getLocation(), 5);
        			for(Entity target : SkillUtils.getTargetsInRadius(origin, 4)) {
        				Entity tempChicken = spawnTemporaryChicken(target.getLocation(), 30);
        				target.addPassenger(tempChicken);
        				SkillUtils.callPlayerFixedDamageEvent(player, target, damage);
        			}
        			chicken.remove();
        		}
	        }
	        
		}, 20);
	}

	/**
	 * Throws the player into the air and triggers gliding.
	 * Damages all entities inside a 2.5 block radius particle shape.
	 * Makes the glider loose 12 durability.
	 * 
	 * @param player The player who starts to fly.
	 * 
	 * @see Cooldown#playerProjectileShoot(Player)
	 * @see SkillUtils#throwEntityIntoAir(Entity, double)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void fly(Player player) {
		Location origin = player.getLocation();
		if(!Cooldown.playerProjectileShoot(player)) {
			return;
		}
		if(WauzMode.isInstance(player.getWorld().getName())) {
			player.sendMessage(ChatColor.RED + "This skill does not work in instances!");
			return;
		}
		ItemStack gliderItemStack = player.getEquipment().getItemInMainHand();
		if(EquipmentUtils.getCurrentDurability(gliderItemStack) <= 12) {
			player.sendMessage(ChatColor.RED + "Glider durability too low!");
			return;
		}
		
		playChickenSounds(origin);
		ParticleSpawner.spawnParticleCircle(origin, particle, 1.5, 8);
		ParticleSpawner.spawnParticleCircle(origin, particle, 2.5, 18);
		SkillUtils.throwEntityIntoAir(player, 2.5);
		
		int damage = EquipmentUtils.getBaseAtk(gliderItemStack);
		List<Entity> targets = SkillUtils.getTargetsInRadius(origin, 2.5);
		for(Entity entity : targets) {
			SkillUtils.callPlayerFixedDamageEvent(player, entity, damage);
		}
		DurabilityCalculator.damageItem(player, gliderItemStack, 12, false);
	}
	
	/**
	 * Plays three chicken sounds in different pitches at the same time.
	 * 
	 * @param origin The location where the sounds should originate from.
	 */
	private static void playChickenSounds(Location origin) {
		origin.getWorld().playSound(origin, Sound.ENTITY_CHICKEN_HURT, 1, 0.75f);
		origin.getWorld().playSound(origin, Sound.ENTITY_CHICKEN_HURT, 1, 1.00f);
		origin.getWorld().playSound(origin, Sound.ENTITY_CHICKEN_HURT, 1, 1.25f);
	}

	/**
	 * Adds a chicken to the player's head, if it is not already there.
	 * Reduces the falling speed by 50% and lets the player slowly glide to their eye direction.
	 * 
	 * @param event The move event.
	 * 
	 * @see NmsEntityChickoon
	 */
	public static void glide(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		
		boolean hasChicken = false;
		for(Entity passanger : player.getPassengers()) {
			if(passanger instanceof Chicken) {
				hasChicken = true;
			}
		}
		if(!hasChicken) {
			Entity chicken = NmsEntityChickoon.create(player.getLocation());
			WauzDebugger.log(player, "Added Head-Chicken: " + player.addPassenger(chicken));
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
	
	/**
	 * Spawns a temporary chicken, which will be removed after a given amount of ticks.
	 * 
	 * @param location The location where the chicken should spawn.
	 * @param duration The time in ticks till despawn.
	 * 
	 * @return The temporary chicken entity.
	 * 
	 * @see NmsEntityChickoon
	 */
	private static Entity spawnTemporaryChicken(Location location, int duration) {
		Entity chicken = NmsEntityChickoon.create(location);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	if(chicken != null && chicken.isValid()) {
        			chicken.remove();
        		}
	        }
	        
		}, duration);
		return chicken;
	}
	
}
