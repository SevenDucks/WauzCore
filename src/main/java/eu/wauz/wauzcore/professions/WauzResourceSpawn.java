package eu.wauz.wauzcore.professions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.particles.ParticleSpawner;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.system.util.Cooldown;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.LootBag;

/**
 * An instanced block of a gatherable resource.
 * 
 * @author Wauzmons
 * 
 * @see WauzResource
 */
public class WauzResourceSpawn {
	
	/**
	 * The instanced resource.
	 */
	private WauzResource resource;
	
	/**
	 * The location of the resource instance.
	 */
	private Location location;
	
	/**
	 * The particles to highlight the resource location.
	 */
	private SkillParticle particle;
	
	/**
	 * A map to cache player specific data of a resource spawn.
	 */
	private Map<Player, WauzResourceCache> playerResourceCacheMap = new HashMap<>();
	
	/**
	 * Instantiates a block of a gatherable resource.
	 * 
	 * @param resource The instanced resource.
	 * @param location The location of the resource instance.
	 */
	public WauzResourceSpawn(WauzResource resource, Location location) {
		this.resource = resource;
		this.location = location;
		
		switch (resource.getType()) {
		case CONTAINER:
			particle = new SkillParticle(Color.MAROON);
			break;
		case NODE:
			particle = new SkillParticle(Color.AQUA);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Highlights the resoure for a player, if it is ready to collect.
	 * 
	 * @param player The player to highlight the resource for.
	 */
	public void tryToHighlightResource(Player player) {
		if(!canCollectResource(player)) {
			return;
		}
		ParticleSpawner.spawnParticleCircle(player, location, particle, 0.75, 6);
	}
	
	/**
	 * Lets the player damage the resource, if it is ready to collect.
	 * 
	 * @param player The player damaging the resource.
	 */
	public void tryToDamageResource(Player player) {
		if(!canCollectResource(player) || !Cooldown.playerEntityInteraction(player)) {
			return;
		}
		WauzResourceNodeType type = resource.getNodeType();
		ItemStack toolItemStack = player.getEquipment().getItemInMainHand();
		if(!type.canGather(player, toolItemStack, resource.getNodeTier())) {
			return;
		}
		AbstractPassiveSkill skill = type.getSkill(player);
		double damage = EquipmentUtils.getBaseEfc(toolItemStack) * (1.00 + ((double) skill.getLevel() * 0.03));
		if(getPlayerResourceCache(player).reduceHealth(player, Math.floor(damage))) {
			collectResource(player);
			player.playSound(player.getLocation(), type.getBreakSound(), 1, 1);
			if(type.canGetExp(player, resource.getNodeTier())) {
				skill.grantExperience(player, 1);
				player.sendMessage(ChatColor.DARK_AQUA + "You earned 1 " + skill.getPassiveName() + " exp!");
			}
			else {
				player.sendMessage(ChatColor.YELLOW + "You can't earn exp from this low tier resource anymore!");
			}
		}
		else {
			player.playSound(player.getLocation(), type.getDamageSound(), 1, 1);
		}
		DurabilityCalculator.damageItem(player, toolItemStack, false);
	}
	
	/**
	 * Lets the player collect the resource, if it is ready.
	 * 
	 * @param player The player collecting the resource.
	 */
	public void tryToCollectResource(Player player) {
		if(canCollectResource(player)) {
			collectResource(player);
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		}
	}
	
	/**
	 * Lets the player collect the resource.
	 * 
	 * @param player The player collecting the resource.
	 */
	public void collectResource(Player player) {
		LootBag lootBag = resource.getDropTable().generate(new DropMetadata(null, BukkitAdapter.adapt(player)));
		lootBag.drop(BukkitAdapter.adapt(location.clone().add(0, 1, 0)));
		long cooldown = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(resource.getRespawnMins());
		getPlayerResourceCache(player).setCooldown(cooldown);
	}
	
	/**
	 * Checks if the given player can collect the resource.
	 * 
	 * @param player The player to check for.
	 * 
	 * @return If the resource is ready to collect. 
	 */
	public boolean canCollectResource(Player player) {
		return getPlayerResourceCache(player).getCooldown() < System.currentTimeMillis();
	}
	
	/**
	 * Gets the player specific cache of the resource spawn.
	 * 
	 * @param player The player to get the cache for.
	 * 
	 * @return The player specific cache.
	 */
	public WauzResourceCache getPlayerResourceCache(Player player) {
		WauzResourceCache cache = playerResourceCacheMap.get(player);
		if(cache == null) {
			cache = new WauzResourceCache(this);
			playerResourceCacheMap.put(player, cache);
		}
		return cache;
	}

	/**
	 * @return The instanced resource.
	 */
	public WauzResource getResource() {
		return resource;
	}

	/**
	 * @return The location of the resource instance.
	 */
	public Location getLocation() {
		return location;
	}

}
