package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * A collection of methods for using the lance weapon.
 * 
 * @author Wauzmons
 */
@Item
public class CustomWeaponLance implements CustomWeapon {
	
	/**
	 * A particle, used to indicate the hit area of the lance.
	 */
	private static SkillParticle particle = new SkillParticle(Color.AQUA);
	
	/**
	 * Cancels the event of a lance interaction.
	 * Starts a spin attack around the player, if they are sneaking.
	 * Otherwhise attacks a line in front of the player, if it was a right click.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see CustomWeaponLance#spin(Player)
	 * @see CustomWeaponLance#thrust(Player)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		if(player.isSneaking()) {
			spin(player);
		}
		else if(event.getAction().toString().contains("RIGHT")) {
			thrust(player);
		}
	}
	
	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.TRIDENT);
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
		lores.add(ChatColor.GRAY + "Use while Sneaking to perform a Spin Attack");
		lores.add(ChatColor.GRAY + "Right Click to Thrust (Throwing Disabled)");
		return lores;
	}
	
	/**
	 * Creates a 3 block long particle line in front of the player
	 * and damages every entity near it.
	 * Makes the lance loose 2 durability.
	 * 
	 * @param player The player who performs the attack.
	 * 
	 * @see Cooldown#playerWeaponSkillUse(Player, String)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see ParticleSpawner#spawnParticleLine(Location, Location, SkillParticle, int, double)
	 * @see DurabilityCalculator#damageItem(Player, org.bukkit.inventory.ItemStack, int, boolean)
	 */
	public static void thrust(Player player) {
		if(!Cooldown.playerWeaponSkillUse(player, "LANCE_THRUST")) {
			return;
		}
		Location origin = player.getLocation();
		Location target = origin.clone().add(origin.getDirection().multiply(3.5)).add(0, 1, 0);
		
		player.getWorld().playSound(origin, Sound.ENTITY_DROWNED_SHOOT, 1, 0.75f);
		ParticleSpawner.spawnParticleLine(origin.clone().add(0, 1, 0), target, particle, 1, 0.5);
		
		ItemStack lanceItemStack = player.getEquipment().getItemInMainHand();
		int damage = EquipmentUtils.getBaseAtk(lanceItemStack);
		List<Entity> targets = SkillUtils.getTargetsInLine(player, 3);
		for(Entity entity : targets) {
			SkillUtils.callPlayerFixedDamageEvent(player, entity, damage);
		}
		DurabilityCalculator.damageItem(player, lanceItemStack, 2, false);
	}
	
	/**
	 * Creates an 8 block wide particle circle around the player
	 * and damages + throws back every entity inside of it.
	 * Makes the lance loose 4 durability.
	 * 
	 * @param player The player who performs the attack.
	 * 
	 * @see Cooldown#playerWeaponSkillUse(Player, String)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see SkillUtils#throwBackEntity(Entity, Location, double)
	 * @see SkillUtils#rotateEntity(Entity)
	 * @see ParticleSpawner#spawnParticleCircle(Location, SkillParticle, double, int)
	 * @see DurabilityCalculator#damageItem(Player, org.bukkit.inventory.ItemStack, int, boolean)
	 */
	public static void spin(Player player) {
		if(!Cooldown.playerWeaponSkillUse(player, "LANCE_SPIN")) {
			return;
		}
		Location origin = player.getLocation();
		
		player.getWorld().playSound(origin, Sound.ENTITY_DROWNED_SHOOT, 1, 0.75f);
		ParticleSpawner.spawnParticleCircle(origin.clone().add(0, 1, 0), particle, 4, 25);
		SkillUtils.rotateEntity(player);
		
		ItemStack lanceItemStack = player.getEquipment().getItemInMainHand();
		int damage = EquipmentUtils.getBaseAtk(lanceItemStack);
		List<Entity> targets = SkillUtils.getTargetsInRadius(player.getLocation(), 4);
		for(Entity entity : targets) {
			SkillUtils.callPlayerFixedDamageEvent(player, entity, damage);
			SkillUtils.throwBackEntity(entity, origin, 1);
		}
		DurabilityCalculator.damageItem(player, lanceItemStack, 4, false);
	}

}
