package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * A collection of methods for using the staff weapon.
 * 
 * @author Wauzmons
 */
public class CustomWeaponStaff implements CustomWeapon {

	/**
	 * Cancels the event of a staff interaction.
	 * Lets the player perform a beam attack, if they are sneaking.
	 * Otherwhise tries to use a socketed skillgem, if it was a right click.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see CustomWeaponStaff#beam(Player)
	 * @see WauzPlayerSkillExecutor#tryToUseSkill(Player, ItemStack)
	 */ 
	@Override
	public void use(PlayerInteractEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if(player.isSneaking()) {
			beam(player);
		}
		else if(event.getAction().toString().contains("RIGHT")) {
			WauzPlayerSkillExecutor.tryToUseSkill(event.getPlayer(), itemStack);
		}
	}

	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.WOODEN_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.IRON_HOE,
				Material.DIAMOND_HOE);
	}
	
	/**
	 * Determines if the custom weapon can have a skillgem slot.
	 * 
	 * @return If the custom weapon can have a skillgem slot.
	 */
	@Override
	public boolean canHaveSkillSlot() {
		return true;
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
		lores.add(ChatColor.GRAY + "Use while Sneaking for an Energy Beam");
		if(hasSkillSlot) {
			lores.add(ChatColor.GRAY + "Right Click to activate Skillgem");
		}
		else {
			lores.add(ChatColor.GRAY + "Right Click Skill not Available");
		}
		return lores;
	}
	
	/**
	 * Creates a 9 block long beam, which hits the targeted entity.
	 * Deals damage based on the staff's attack value.
	 * Makes the staff loose 2 durability.
	 * 
	 * @param player The player who performs the attack.
	 * 
	 * @see Cooldown#playerWeaponSkillUse(Player, String)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see ParticleSpawner#spawnParticleLine(Location, Location, SkillParticle, int)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void beam(Player player) {
		if(!Cooldown.playerWeaponSkillUse(player, "STAFF_BEAM")) {
			return;
		}
		Entity target = SkillUtils.getTargetInLine(player, 9);
		Location originLocation = player.getLocation().clone().add(0, 1, 0);
		Location targetLocation;
		
		if(target != null) {
			targetLocation = target.getLocation().clone().add(0, 1, 0);
		}
		else {
			List<Block> blocksInSight = player.getLineOfSight(null, 9);
			targetLocation = blocksInSight.get(blocksInSight.size() - 1).getLocation();
		}
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1.25f);
		SkillParticle particle = new SkillParticle(Particle.DRIPPING_HONEY);
		ParticleSpawner.spawnParticleLine(originLocation, targetLocation, particle, 1, 0.25);
		
		ItemStack staffItemStack = player.getEquipment().getItemInMainHand();
		if(target != null) {
			ParticleSpawner.spawnParticleWave(target.getLocation(), particle, 2);
			int damage = EquipmentUtils.getBaseAtk(staffItemStack);
			SkillUtils.callPlayerFixedDamageEvent(player, target, damage);
		}
		DurabilityCalculator.damageItem(player, staffItemStack, 2, false);
	}

}
