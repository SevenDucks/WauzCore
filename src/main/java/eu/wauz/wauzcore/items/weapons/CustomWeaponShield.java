package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Cooldown;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;

/**
 * A collection of methods for using the shield weapon.
 * 
 * @author Wauzmons
 */
public class CustomWeaponShield implements CustomWeapon {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	/**
	 * A particle, used to create the taunt circle.
	 */
	private static SkillParticle tauntCircleParticle = new SkillParticle(Particle.TOTEM);
	
	/**
	 * A particle, used to mark taunted entities.
	 */
	private static SkillParticle tauntEntityParticle = new SkillParticle(Particle.VILLAGER_ANGRY);
	
	/**
	 * A list of valid dye colors for shield patterns.
	 */
	private static List<DyeColor> dyeColors = new ArrayList<>(Arrays.asList(
			DyeColor.RED, DyeColor.ORANGE, DyeColor.BLUE, DyeColor.LIGHT_BLUE, DyeColor.GREEN, DyeColor.LIME,
			DyeColor.PURPLE, DyeColor.PINK, DyeColor.MAGENTA, DyeColor.CYAN, DyeColor.YELLOW, DyeColor.BROWN));
	
	/**
	 * A list of valid pattern types for shield patterns.
	 */
	private static List<PatternType> patternTypes = new ArrayList<>(Arrays.asList(
			PatternType.CROSS, PatternType.GLOBE, PatternType.RHOMBUS_MIDDLE, PatternType.BRICKS,
			PatternType.CREEPER, PatternType.SKULL, PatternType.MOJANG, PatternType.FLOWER));
	
	/**
	 * A random instance, for rolling shield pattern types and dye colors.
	 */
	private static Random random = new Random();
	
	/**
	 * Handles the interaction with a shield.
	 * Cancels the event and taunts all enemies near the player, if they are sneaking.
	 * Otherwhise the event will proceed normally.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see CustomWeaponShield#taunt(Player)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.isSneaking()) {
			event.setCancelled(true);
			taunt(player);
		}
	}
	
	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.SHIELD);
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
		lores.add(ChatColor.GRAY + "Use while Sneaking to taunt nearby Enemies");
		lores.add(ChatColor.GRAY + "Right Click to block Attacks");
		return lores;
	}
	
	/**
	 * Creates a 10 block wide particle circle around the player
	 * and taunts every entity inside of it, forcing it to attack the player.
	 * Makes the shield loose 3 durability.
	 * 
	 * @param player The player that is taunting the entities.
	 * 
	 * @see Cooldown#playerWeaponSkillUse(Player, String)
	 * @see ParticleSpawner#spawnParticleCircle(Location, SkillParticle, double, int)
	 * @see DurabilityCalculator#damageItem(Player, org.bukkit.inventory.ItemStack, int, boolean)
	 */
	public static void taunt(Player player) {
		if(!Cooldown.playerWeaponSkillUse(player, "SHIELD_TAUNT")) {
			return;
		}
		Location origin = player.getLocation();
		
		player.getWorld().playSound(origin, Sound.ENTITY_CAT_HISS, 1, 0.75f);
		ParticleSpawner.spawnParticleCircle(origin.clone().add(0, 1, 0), tauntCircleParticle, 5, 32);
		
		List<Entity> targets = SkillUtils.getTargetsInRadius(origin, 5);
		for(Entity target : targets) {
			ParticleSpawner.spawnParticleCircle(target.getLocation().clone().add(0, 2, 0), tauntEntityParticle, 1, 5);
			mythicMobs.taunt(target, player);
		}
		DurabilityCalculator.damageItem(player, player.getEquipment().getItemInMainHand(), 3, false);
	}
	
	/**
	 * Adds a random pattern to the given shield item meta.
	 * It will receive either black or white as base and a colorful emblem as first pattern layer.
	 * 
	 * @param shieldItemMeta The item meta of a shield.
	 */
	public static void addPattern(ItemMeta shieldItemMeta) {
		BlockStateMeta blockStateMeta = (BlockStateMeta) shieldItemMeta;
        Banner banner = (Banner) blockStateMeta.getBlockState();
        banner.setBaseColor(Chance.oneIn(2) ? DyeColor.WHITE : DyeColor.BLACK);
        DyeColor dyeColor = dyeColors.get(random.nextInt(dyeColors.size()));
        PatternType patternType = patternTypes.get(random.nextInt(patternTypes.size()));
        banner.addPattern(new Pattern(dyeColor, patternType));
        banner.update();
        blockStateMeta.setBlockState(banner);
	}

}
