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
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * A collection of methods for using the axe weapon.
 * 
 * @author Wauzmons
 */
@Item
public class CustomWeaponAxe implements CustomWeapon {

	/**
	 * Cancels the event of an axe interaction.
	 * Lets the player perform a slam attack, if they are sneaking.
	 * Otherwhise tries to use a socketed skillgem, if it was a right click.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see CustomWeaponAxe#slam(Player)
	 * @see WauzPlayerSkillExecutor#tryToUseSkill(Player, ItemStack)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if(player.isSneaking()) {
			slam(player);
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
		return Arrays.asList(Material.WOODEN_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
				Material.DIAMOND_AXE, Material.NETHERITE_AXE);
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
		lores.add(ChatColor.GRAY + "Use while Sneaking for a Stun Slam");
		if(hasSkillSlot) {
			lores.add(ChatColor.GRAY + "Right Click to activate Skillgem");
		}
		else {
			lores.add(ChatColor.GRAY + "Right Click Skill not Available");
		}
		return lores;
	}
	
	/**
	 * Lets the player perform a slam attack in front of them.
	 * Stuns and deals damage based on the axe's attack value.
	 * Makes the axe loose 2 durability.
	 * 
	 * @param player The player who performs the attack.
	 * 
	 * @see Cooldown#playerWeaponSkillUse(Player, String)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see ParticleSpawner#spawnParticleWave(Location, SkillParticle, double)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, int, boolean)
	 */
	public static void slam(Player player) {
		if(!Cooldown.playerWeaponSkillUse(player, "AXE_SLAM")) {
			return;
		}
		Location origin = player.getLocation();
		Location target = origin.clone().add(origin.getDirection().multiply(2));
		
		player.getWorld().playSound(origin, Sound.ENTITY_IRON_GOLEM_HURT, 1, 1.25f);
		SkillParticle particle = new SkillParticle(Color.WHITE);
		ParticleSpawner.spawnParticleWave(target, particle, 2.25);
		
		ItemStack axeItemStack = player.getEquipment().getItemInMainHand();
		int damage = EquipmentUtils.getBaseAtk(axeItemStack);
		List<Entity> targets = SkillUtils.getTargetsInLine(player, 3);
		for(Entity entity : targets) {
			SkillUtils.callPlayerFixedDamageEvent(player, entity, damage);
			SkillUtils.addPotionEffect(targets, PotionEffectType.SLOW, 2, 200);
			SkillUtils.addPotionEffect(targets, PotionEffectType.JUMP, 2, 200);
		}
		DurabilityCalculator.damageItem(player, axeItemStack, 2, false);
	}

}
