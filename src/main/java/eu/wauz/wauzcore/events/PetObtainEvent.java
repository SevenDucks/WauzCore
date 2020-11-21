package eu.wauz.wauzcore.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.wauz.wauzcore.mobs.pets.WauzPet;

/**
 * A custom Bukkit event to track players obtaining pets.
 * 
 * @author Wauzmons
 */
public class PetObtainEvent extends Event {
	
	/**
	 * A list of handlers for this event.
	 */
	private static final HandlerList HANDLERS = new HandlerList();
	
	/**
	 * The player who obtained the pet.
	 */
	private final Player player;
	
	/**
	 * The pet that was obtained.
	 */
	private final WauzPet pet;
	
	/**
	 * Creates and calls a new instance of the event.
	 * 
	 * @param player The player who obtained the pet.
	 * @param pet The pet that was obtained.
	 */
	public static void call(Player player, WauzPet pet) {
		Bukkit.getServer().getPluginManager().callEvent(new PetObtainEvent(player, pet));
	}

	/**
	 * @return A list of handlers for this event.
	 */
	public static final HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	/**
	 * Creates a new instance of the event.
	 * 
	 * @param player The player who obtained the pet.
	 * @param pet The pet that was obtained.
	 */
	private PetObtainEvent(Player player, WauzPet pet) {
		this.player = player;
		this.pet = pet;
	}
	
	/**
	 * @return A list of handlers for this event.
	 */
	@Override
	public final HandlerList getHandlers() {
		return HANDLERS;
	}

	/**
	 * @return The player who obtained the pet.
	 */
	public final Player getPlayer() {
		return player;
	}

	/**
	 * @return The pet that was obtained.
	 */
	public final WauzPet getPet() {
		return pet;
	}

}
