package eu.wauz.wauzcore.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipEvent extends PlayerEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	private final EquipMethod equipMethod;
	
	private final ArmorType armorType;
	
	private ItemStack oldArmorPiece;
	
	private ItemStack newArmorPiece;

	public ArmorEquipEvent(
			final Player player,
			final EquipMethod equipType,
			final ArmorType armorType,
			final ItemStack oldArmorPiece,
			final ItemStack newArmorPiece) {
		
		super(player);
		this.equipMethod = equipType;
		this.armorType = armorType;
		this.oldArmorPiece = oldArmorPiece;
		this.newArmorPiece = newArmorPiece;
	}

	public final static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}

	public final void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	public final boolean isCancelled() {
		return cancelled;
	}

	public final ArmorType getArmorType() {
		return armorType;
	}

	public final ItemStack getOldArmorPiece() {
		return oldArmorPiece;
	}

	public final void setOldArmorPiece(final ItemStack oldArmorPiece) {
		this.oldArmorPiece = oldArmorPiece;
	}

	public final ItemStack getNewArmorPiece() {
		return newArmorPiece;
	}

	public final void setNewArmorPiece(final ItemStack newArmorPiece) {
		this.newArmorPiece = newArmorPiece;
	}

	public EquipMethod getEquipMethod() {
		return equipMethod;
	}

	public enum EquipMethod {
	    /**
	     * When you shift click an armor piece to equip or unequip
	     */
		SHIFT_CLICK,
		/**
		 * When you drag and drop the item to equip or unequip
		 */
		DRAG,
		/**
		 * When you manually equip or unequip the item. Use to be DRAG
		 */
		PICK_DROP,
		/**
		 * When you right click an armor piece in the hotbar without the inventory open to equip.
		 */
		HOTBAR,
		/**
		 * When you press the hotbar slot number while hovering over the armor slot to equip or unequip
		 */
		HOTBAR_SWAP,
		/**
		 * When in range of a dispenser that shoots an armor piece to equip.<br>
		 * Requires the spigot version to have {@link org.bukkit.event.block.BlockDispenseArmorEvent} implemented.
		 */
		DISPENSER,
		/**
		 * When an armor piece is removed due to it losing all durability.
		 */
		BROKE,
		/**
		 * When you die causing all armor to unequip
		 */
		DEATH;
	}
	
	public enum ArmorType {
		HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);

		private final int slot;

		ArmorType(int slot) {
			this.slot = slot;
		}
		
		public int getSlot() {
			return slot;
		}

		public final static ArmorType matchType(final ItemStack itemStack) {
			if(itemStack == null || itemStack.getType().equals(Material.AIR))
				return null;
			
			String type = itemStack.getType().name();
			if(type.endsWith("_HELMET") || type.endsWith("_SKULL"))
				return HELMET;
			else if(type.endsWith("_CHESTPLATE"))
				return CHESTPLATE;
			else if(type.endsWith("_LEGGINGS"))
				return LEGGINGS;
			else if(type.endsWith("_BOOTS"))
				return BOOTS;
			else
				return null;
		}
		
	}

}
