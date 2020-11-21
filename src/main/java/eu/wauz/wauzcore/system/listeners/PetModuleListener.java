package eu.wauz.wauzcore.system.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.events.PetObtainEvent;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDespawnEvent;

/**
 * A listener to catch events, related to the standalone pet module.
 * 
 * @author Wauzmons
 */
public class PetModuleListener implements Listener {
	
	/**
	 * Loads and initializes all data for the standalone pet module.
	 */
	public PetModuleListener() {
		WauzPet.init();
		WauzCore.getInstance().getLogger().info("Loaded Standalone Pet Module!");
	}
	
	/**
	 * Handles the interaction between players and pet eggs.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzPetEgg#tryToSummon(PlayerInteractEvent)
	 */
	@EventHandler
	public void onInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(itemStack.getType().toString().endsWith("_SPAWN_EGG") && PetEggUtils.isEggItem(itemStack)) {
			WauzPetEgg.tryToSummon(event);
			event.setCancelled(true);
		}
		else if(ItemUtils.isSpecificItem(itemStack, "Scroll of Summoning")) {
			WauzPet newPet = WauzPet.getPet(ChatColor.stripColor(itemStack.getLore().get(0)));
			if(newPet == null) {
				player.sendMessage(ChatColor.RED + "Your scroll is invalid or outdated!");
				return;
			}
			ItemStack newPetItemStack = WauzPetEgg.getEggItem(player, newPet, System.currentTimeMillis());
			PetObtainEvent.call(player, newPet);
			player.getEquipment().setItemInMainHand(newPetItemStack);
			player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_HATCH, 1, 1);
			event.setCancelled(true);
		}
	}
	
	/**
	 * Handles possible interactions with pets.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzActivePet#handlePetInteraction(PlayerInteractEntityEvent)
	 */
	@EventHandler
	public void onEntityInteraction(PlayerInteractEntityEvent event) {
		WauzActivePet.handlePetInteraction(event);
	}
	
	/**
	 * Handles possible interactions with inventory pet items.
	 * 
	 * @param event The click event.
	 * 
	 * @see WauzPetEgg#tryToFeed(InventoryClickEvent)
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		WauzPetEgg.tryToFeed(event);
	}
	
	/**
	 * Removes a player from the owner map, in case their pet dies.
	 * 
	 * @param event The death event.
	 */
	@EventHandler
	public void onMythicDeath(MythicMobDeathEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = WauzActivePet.getOwner(entity);
		if(mobOwner != null) {
			WauzActivePet.removeOwner(mobId, mobOwner);
		}
	}
	
	/**
	 * Removes a player from the owner map, in case their pet despawns.
	 * 
	 * @param event The despawn event.
	 */
	@EventHandler
	public void onMythicDespawn(MythicMobDespawnEvent event) {
		Entity entity = event.getEntity();
		String mobId = entity.getUniqueId().toString();
		Player mobOwner = WauzActivePet.getOwner(entity);
		if(mobOwner != null) {
			WauzActivePet.removeOwner(mobId, mobOwner);
		}
	}
	
	/**
	 * Makes a player's mount despawn when standing up.
	 *
	 * @param event The dismount event.
	 */
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		Entity owner = WauzActivePet.getOwner(event.getDismounted());
		if(owner != null && owner.getUniqueId().equals(player.getUniqueId())) {
			WauzActivePet.tryToUnsummon(player, false);
		}
	}
	
	/**
	 * Unsummons the active pet after the player dies.
	 * 
	 * @param event The death event.
	 * 
	 * @see WauzActivePet#tryToUnsummon(Player, boolean)
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		WauzActivePet.tryToUnsummon(event.getEntity(), false);
	}
	
	/**
	 * Unsummons the active pet before logging out a player.
	 * 
	 * @param event The logout event.
	 * 
	 * @see WauzActivePet#tryToUnsummon(Player, boolean)
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		WauzActivePet.tryToUnsummon(event.getPlayer(), false);
	}
	
}
