package eu.wauz.wauzcore.events;

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
	private ItemStack itemStack;
	
	/**
	 * Creates a new armor equip event with given values.
	 * 
	 * @param player The player that shared the item.
	 * @param itemStack The item to be shared.
	 */
	public ShareItemEvent(Player player, ItemStack itemStack) {
		super(player);
		
		this.itemStack = itemStack;
	}
	
	/**
	 * @return A list of handlers for this event.
	 */
	public final static HandlerList getHandlerList() {
		return HANDLERS;
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
	public ItemStack getItemStack() {
		return itemStack;
	}
	
}
