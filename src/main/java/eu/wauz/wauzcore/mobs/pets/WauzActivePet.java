package eu.wauz.wauzcore.mobs.pets;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.players.calc.SpeedCalculator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzModules;

/**
 * A cached pet, currently summoned by a player.
 * 
 * @author Wauzmons
 */
public class WauzActivePet {
	
	/**
	 * A map that contains all players with active pets, indexed by pet uuid.
	 */
	private static Map<String, Player> petOwnerMap = new HashMap<>();
	
	/**
	 * A map that contains all active pets, indexed by their owners.
	 */
	private static Map<Player, WauzActivePet> ownerPetMap = new HashMap<>();
	
	/**
	 * Gets the owner of a pet from the pet owner map.
	 * 
	 * @param entity The pet to get the owner from.
	 * 
	 * @return The owner of the pet.
	 */
	public static Player getOwner(Entity entity) {
		return petOwnerMap.get(entity.getUniqueId().toString());
	}
	
	/**
	 * Gets the active pet of a player from the owner pet map.
	 * 
	 * @param player The player to get the pet from.
	 * .
	 * @return The pet of the player.
	 */
	public static WauzActivePet getPet(Player player) {
		return ownerPetMap.get(player);
	}
	
	/**
	 * Adds a player to the owner map.
	 * 
	 * @param player The owner of the pet.
	 * @param petEntity The pet entity.
	 * @param pet The pet type.
	 * @param eggItemStack The pet egg item stack.
	 */
	public static void setOwner(Player player, Entity petEntity, WauzPet pet, ItemStack eggItemStack) {
		if(petEntity instanceof Tameable) {
			((Tameable) petEntity).setOwner(player);
		}
		petOwnerMap.put(petEntity.getUniqueId().toString(), player);
		ownerPetMap.put(player, new WauzActivePet(player, petEntity, pet, eggItemStack));
		if(!WauzModules.isPetsModuleStandalone()) {
			SpeedCalculator.resetWalkSpeed(player);
		}
	}
	
	/**
	 * Removes a player from the owner map.
	 * 
	 * @param petId The uuid of the pet, owned by the player.
	 * @param player The player to remove from the owner map.
	 */
	public static void removeOwner(String petId, Player player) {
		petOwnerMap.remove(petId);
		ownerPetMap.remove(player);
		if(!WauzModules.isPetsModuleStandalone()) {
			SpeedCalculator.resetWalkSpeed(player);
		}
	}
	
	/**
	 * Unsummons the currently active pet, if possible.
	 * 
	 * @param player The owner of the pet.
	 * @param showMessage If a message should be shown to the owner.
	 * 
	 * @return If there was a pet to unsummon.
	 */
	public static boolean tryToUnsummon(Player player, boolean showMessage) {
		WauzActivePet pet = WauzActivePet.getPet(player);
		if(pet != null) {
			pet.unsummon(showMessage);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets a stat of the currently active pet.
	 * 
	 * @param player The owner of the pet.
	 * @param stat  The stat to get.
	 * @return The stat value of the pet.
	 */
	public static int getPetStat(Player player, WauzPetStat stat) {
		WauzActivePet pet = WauzActivePet.getPet(player);
		return pet != null ? pet.getActivePetStat(stat) : 0;
	}
	
	/**
	 * Called when a player interacts with an entity.
	 * Cancels the sit command for pets.
	 * 
	 * @param event The interact event.
	 */
	public static void handlePetInteraction(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		if(player.equals(getOwner(entity)) && entity instanceof Wolf) {
			event.setCancelled(true);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	            
				public void run() {
	            	try {
	            		((Wolf) entity).setSitting(false);
	            	}
	            	catch (NullPointerException e) {
	            		WauzDebugger.catchException(getClass(), e);
	            	}
	            }
				
			}, 10);
		}
	}
	
	/**
	 * The owner of the pet.
	 */
	private Player owner;
	
	/**
	 * The pet entity.
	 */
	private Entity petEntity;
	
	/**
	 * The pet type.
	 */
	private WauzPet pet;
	
	/**
	 * The name of the pet.
	 */
	private String petName;
	
	/**
	 * The pet ability.
	 */
	private WauzPetAbility petAbility;
	
	/**
	 * A map of pet stat values, indexed by corresponding stat objects.
	 */
	private Map<WauzPetStat, Integer> petStatMap = new HashMap<>();
	
	/**
	 * Creates a new active pet data.
	 * 
	 * @param owner The owner of the pet.
	 * @param petEntity The pet entity.
	 * @param pet The pet type.
	 * @param eggItemStack The pet egg item stack.
	 */
	public WauzActivePet(Player owner, Entity petEntity, WauzPet pet, ItemStack eggItemStack) {
		this.owner = owner;
		this.petEntity = petEntity;
		this.pet = pet;
		this.petName = ChatColor.stripColor(eggItemStack.getItemMeta().getDisplayName());
		this.petAbility = PetEggUtils.getPetAbility(eggItemStack);
		for(WauzPetStat stat : WauzPetStat.getAllPetStats()) {
			petStatMap.put(stat, PetEggUtils.getPetStat(eggItemStack, stat));
		}
	}
	
	/**
	 * @return The pet entity.
	 */
	public Entity getPetEntity() {
		if(!petEntity.isValid()) {
			removeOwner(petEntity.getUniqueId().toString(), owner);
			return null;
		}
		return petEntity;
	}
	
	/**
	 * @return The pet type.
	 */
	public WauzPet getPet() {
		return pet;
	}

	/**
	 * @return The name of the pet.
	 */
	final String getPetName() {
		return petName;
	}

	/**
	 * @return The pet ability.
	 */
	public WauzPetAbility getPetAbility() {
		return petAbility;
	}

	/**
	 * Gets a stat of the pet.
	 * 
	 * @param stat The stat to get.
	 * 
	 * @return The stat value of the pet.
	 */
	public int getActivePetStat(WauzPetStat stat) {
		return petStatMap.get(stat);
	}
	
	/**
	 * Shows a random pet message to the Owner, if possible.
	 */
	public void showRandomMessage() {
		String randomMessage = pet.getRandomMessage();
		if(randomMessage != null) {
			String msg = ChatColor.WHITE + "[" + ChatColor.GREEN + petName + ChatColor.WHITE + " (" +
					 ChatColor.AQUA  + "Pet" + ChatColor.WHITE + ")] " +
					 ChatColor.GRAY + randomMessage;
			owner.sendMessage(msg);
		}
	}
	
	/**
	 * Unsummons the pet.
	 * 
	 * @param showMessage If a message should be shown to the owner.
	 */
	public void unsummon(boolean showMessage) {
		if(petEntity.isValid()) {
			petEntity.remove();
			if(showMessage) {
				owner.sendMessage(ChatColor.GREEN + petName + " was unsommoned!");
			}
		}
		removeOwner(petEntity.getUniqueId().toString(), owner);
	}

}
