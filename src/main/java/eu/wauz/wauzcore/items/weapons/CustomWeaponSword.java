package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
 * A collection of methods for using the sword weapon.
 * 
 * @author Wauzmons
 */
public class CustomWeaponSword implements CustomWeapon {

	/**
	 * Cancels the event of a sword interaction.
	 * Lets the player perform a cut attack, if they are sneaking.
	 * Otherwhise tries to use a socketed skillgem, if it was a right click.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see CustomWeaponSword#cut(Player)
	 * @see WauzPlayerSkillExecutor#tryToUseSkill(Player, ItemStack)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if(player.isSneaking()) {
			cut(player);
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
		return Arrays.asList(Material.WOODEN_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
				Material.DIAMOND_SWORD);
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
		lores.add(ChatColor.GRAY + "Use while Sneaking for a Deadly Cut");
		if(hasSkillSlot) {
			lores.add(ChatColor.GRAY + "Right Click to activate Skillgem");
		}
		else {
			lores.add(ChatColor.GRAY + "Right Click Skill not Available");
		}
		return lores;
	}
	
	/**
	 * Lets the player perform a cut attack in front of them.
	 * Deals damage and lets targets bleed based on the sword's attack value.
	 * Makes the sword loose 2 durability.
	 * 
	 * @param player The player who performs the attack.
	 * 
	 * @see Cooldown#playerWeaponSkillUse(Player, String)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see SkillUtils#callPlayerDamageOverTimeEvent(Player, Entity, Color, int, int, int)
	 * @see ParticleSpawner#spawnParticleLine(Location, Location, SkillParticle, int, double)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void cut(Player player) {
		if(!Cooldown.playerWeaponSkillUse(player, "SWORD_CUT")) {
			return;
		}
		Location origin = player.getLocation();
		Location target1 = origin.clone().add(origin.getDirection().multiply(2));
		Location target2 = target1.clone().add(0, 3, 0);
		
		player.getWorld().playSound(origin, Sound.ENTITY_MAGMA_CUBE_DEATH, 1, 1.25f);
		SkillParticle particle = new SkillParticle(Particle.DAMAGE_INDICATOR);
		ParticleSpawner.spawnParticleLine(target1, target2, particle, 1, 0.25);
		
		ItemStack swordItemStack = player.getEquipment().getItemInMainHand();
		int damage = EquipmentUtils.getBaseAtk(swordItemStack);
		List<Entity> targets = SkillUtils.getTargetsInLine(player, 2);
		for(Entity entity : targets) {
			SkillUtils.callPlayerFixedDamageEvent(player, entity, damage);
			SkillUtils.callPlayerDamageOverTimeEvent(player, entity, Color.RED, (int) (damage * 1.35), 1, 40);
		}
		DurabilityCalculator.damageItem(player, swordItemStack, 2, false);
	}

}
