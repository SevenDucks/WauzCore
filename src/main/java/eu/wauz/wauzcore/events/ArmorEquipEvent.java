package eu.wauz.wauzcore.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A custom Bukkit event to track the change of player equipment.
 * 
 * @author Wauzmons
 * 
 * @see ArmorEquipEventListener
 */
public class ArmorEquipEvent extends PlayerEvent implements Cancellable {
	
	/**
	 * A list of handlers for this event.
	 */
	private static final HandlerList handlers = new HandlerList();
	
	/**
	 * If the event has been cancelled.
	 */
	private boolean cancelled = false;
	
	/**
	 * How the armor was equipped.
	 */
	private final EquipMethod equipMethod;
	
	/**
	 * For which slot armor was euipped.
	 */
	private final ArmorType armorType;
	
	/**
	 * The armor piece that was in the slot before.
	 */
	private ItemStack oldArmorPiece;
	
	/**
	 * The armor piece that is in the slot now.
	 */
	private ItemStack newArmorPiece;

	/**
	 * Creates a new armor equip event with given values.
	 * 
	 * @param player The player that equipped the armor.
	 * @param equipMethod How the armor was equipped.
	 * @param armorType For which slot armor was euipped.
	 * @param oldArmorPiece The armor piece that was in the slot before.
	 * @param newArmorPiece The armor piece that is in the slot now.
	 */
	public ArmorEquipEvent(
			final Player player,
			final EquipMethod equipMethod,
			final ArmorType armorType,
			final ItemStack oldArmorPiece,
			final ItemStack newArmorPiece) {
		
		super(player);
		this.equipMethod = equipMethod;
		this.armorType = armorType;
		this.oldArmorPiece = oldArmorPiece;
		this.newArmorPiece = newArmorPiece;
	}

	
	/**
	 * @return A list of handlers for this event.
	 */
	public final static HandlerList getHandlerList() {
		return handlers;
	}
	
	/**
	 * @return A list of handlers for this event.
	 */
	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * @param cancelled If the event has been cancelled now.
	 */
	public final void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @return If the event has been cancelled.
	 */
	public final boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * @return How the armor was equipped.
	 */
	public EquipMethod getEquipMethod() {
		return equipMethod;
	}

	/**
	 * @return For which slot armor was euipped.
	 */
	public final ArmorType getArmorType() {
		return armorType;
	}

	/**
	 * @return The armor piece that was in the slot before.
	 */
	public final ItemStack getOldArmorPiece() {
		return oldArmorPiece;
	}

	/**
	 * @param oldArmorPiece The new armor piece that was in the slot before.
	 */
	public final void setOldArmorPiece(final ItemStack oldArmorPiece) {
		this.oldArmorPiece = oldArmorPiece;
	}

	/**
	 * @return The armor piece that is in the slot now.
	 */
	public final ItemStack getNewArmorPiece() {
		return newArmorPiece;
	}

	/**
	 * @param newArmorPiece The new armor piece that is in the slot now.
	 */
	public final void setNewArmorPiece(final ItemStack newArmorPiece) {
		this.newArmorPiece = newArmorPiece;
	}

	/**
	 * Represents how armor was equipped.
	 * 
	 * @author Wauzmons
	 */
	public enum EquipMethod {
		
	    /**
	     * When you shift click an armor piece to equip or unequip.
	     */
		SHIFT_CLICK,
		
		/**
		 * When you drag and drop the item to equip or unequip.
		 */
		DRAG,
		
		/**
		 * When you manually equip or unequip the item.
		 */
		PICK_DROP,
		
		/**
		 * When you right click an armor piece in the hotbar without the inventory open to equip.
		 */
		HOTBAR,
		
		/**
		 * When you press the hotbar slot number while hovering over the armor slot to equip or unequip.
		 */
		HOTBAR_SWAP,
		
		/**
		 * When in range of a dispenser that shoots an armor piece to equip.
		 */
		DISPENSER,
		
		/**
		 * When an armor piece is removed due to it losing all durability.
		 */
		BROKE,
		
		/**
		 * When you die causing all armor to unequip.
		 */
		DEATH;
		
	}
	
	/**
	 * Represents for which slot armor was euipped.
	 * 
	 * @author Wauzmons
	 */
	public enum ArmorType {
		
		/**
		 * Equipment for the helmet slot.
		 */
		HELMET(5),
		
		/**
		 * Equipment for the chestplate slot.
		 */
		CHESTPLATE(6),
		
		/**
		 * Equipment for the leggings slot.
		 */
		LEGGINGS(7),
		
		/**
		 * Equipment for the boots slot.
		 */
		BOOTS(8);

		/**
		 * The slot id of the armor type.
		 */
		private final int slot;

		/**
		 * Creates a new armor type with given slot id.
		 * 
		 * @param slot The slot id of the armor type.
		 */
		ArmorType(int slot) {
			this.slot = slot;
		}
		
		/**
		 * @return The slot id of the armor type.
		 */
		public int getSlot() {
			return slot;
		}

		/**
		 * Finds the matching armor type for an item.
		 * 
		 * @param itemStack The armor item.
		 * 
		 * @return The matching armor type.
		 */
		public final static ArmorType getArmorType(final ItemStack itemStack) {
			if(itemStack == null || itemStack.getType().equals(Material.AIR)) {
				return null;
			}
			
			String type = itemStack.getType().name();
			if(type.endsWith("_HELMET") || type.endsWith("_SKULL")) {
				return HELMET;
			}
			else if(type.endsWith("_CHESTPLATE")) {
				return CHESTPLATE;
			}
			else if(type.endsWith("_LEGGINGS")) {
				return LEGGINGS;
			}
			else if(type.endsWith("_BOOTS")) {
				return BOOTS;
			}
			else {
				return null;
			}
		}
		
	}

}
