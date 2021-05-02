package eu.wauz.wauzcore.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A custom Bukkit event to share items to the chat.
 * 
 * @author Wauzmons
 */
public class ShareItemEvent extends PlayerEvent {
	
	/**
	 * A list of handlers for this event.
	 */
	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * The item to be shared.
	 */
	private final ItemStack itemStack;
	
	/**
	 * Creates and calls a new instance of the event.
	 * 
	 * @param player The player that shared the item.
	 * @param itemStack The item to be shared.
	 */
	public static void call(Player player, ItemStack itemStack) {
		Bukkit.getServer().getPluginManager().callEvent(new ShareItemEvent(player, itemStack));
	}
	
	/**
	 * @return A list of handlers for this event.
	 */
	public final static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	/**
	 * Creates a new armor equip event with given values.
	 * 
	 * @param player The player that shared the item.
	 * @param itemStack The item to be shared.
	 */
	private ShareItemEvent(Player player, ItemStack itemStack) {
		super(player);
		this.itemStack = itemStack;
	}
	
	/**
	 * @return A list of handlers for this event.
	 */
	@Override
	public final HandlerList getHandlers() {
		return HANDLERS;
	}
	
	/**
	 * @return The item to be shared.
	 */
	public final ItemStack getItemStack() {
		return itemStack;
	}
	
}
