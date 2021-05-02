package eu.wauz.wauzcore.system.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.events.ArmorEquipEvent.ArmorType;
import eu.wauz.wauzcore.events.ArmorEquipEvent.EquipMethod;
import eu.wauz.wauzcore.items.util.ItemUtils;

/**
 * A listener to create armor equip events from normal Bukkit events.
 * Ripped from another unknown plugin's source.
 * 
 * @author Wauzmons
 *
 * @see ArmorEquipEvent
 */
public class ArmorEquipEventListener implements Listener {
	
	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event The click event.
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#SHIFT_CLICK
	 * @see EquipMethod#HOTBAR_SWAP
	 * @see EquipMethod#PICK_DROP
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!isInventoryClickEventRelevant(event)) {
			return;
		}
		
		ClickType clickType = event.getClick();
		boolean shiftKeyPressed = clickType.equals(ClickType.SHIFT_LEFT) || clickType.equals(ClickType.SHIFT_RIGHT);
		boolean numberKeyPressed = clickType.equals(ClickType.NUMBER_KEY);
		
		ArmorType newArmorType = ArmorType.getArmorType(shiftKeyPressed ? event.getCurrentItem() : event.getCursor());
		if(!shiftKeyPressed && newArmorType != null && event.getRawSlot() != newArmorType.getSlot()) {
			return;
		}
		
		if(shiftKeyPressed){
			createShiftClickEvent(event, newArmorType);
		}
		else{
			createItemClickEvent(event, numberKeyPressed, newArmorType);
		}
	}
	
	/**
	 * Checks iif the given event is releavant to the (un)equipping of armor.
	 * 
	 * @param event The click event.
	 * 
	 * @return If it is relevant.
	 */
	private boolean isInventoryClickEventRelevant(InventoryClickEvent event) {
		if(event.isCancelled()) {
			return false;
		}
		if(event.getAction() == InventoryAction.NOTHING) {
			return false;
		}
		SlotType slotType = event.getSlotType();
		if(slotType != SlotType.ARMOR && slotType != SlotType.QUICKBAR && slotType != SlotType.CONTAINER) {
			return false;
		}
		Inventory clickedInventory = event.getClickedInventory();
		if(clickedInventory != null && !clickedInventory.getType().equals(InventoryType.PLAYER)) {
			return false;
		}
		Inventory inventory = event.getInventory();
		if (!inventory.getType().equals(InventoryType.CRAFTING) && !inventory.getType().equals(InventoryType.PLAYER)) {
			return false;
		}
		if(!(event.getWhoClicked() instanceof Player)) {
			return false;
		}
		return true;
	}

	/**
	 * Tries to create an armor equip event for a shift click action.
	 * 
	 * @param event The click event.
	 * @param newArmorType The type of the new armor piece.
	 */
	private void createShiftClickEvent(InventoryClickEvent event, ArmorType newArmorType) {
		if(event.isCancelled() || newArmorType == null) {
			return;
		}
		
		boolean equipping = event.getRawSlot() != newArmorType.getSlot();
		Player player = (Player) event.getWhoClicked();
		
		ItemStack armorItem;
		switch (newArmorType) {
		case HELMET:
			armorItem = player.getEquipment().getHelmet();
			break;
		case CHESTPLATE:
			armorItem = player.getEquipment().getChestplate();
			break;
		case LEGGINGS:
			armorItem = player.getEquipment().getLeggings();
			break;
		case BOOTS:
			armorItem = player.getEquipment().getBoots();
			break;
		default:
			return;
		}
		
		if(equipping != ItemUtils.isNotAir(armorItem)) {
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.SHIFT_CLICK,
					newArmorType, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			
			if(armorEquipEvent.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}
	
	/**
	 * Tries to create an armor equip event for a hotbar or item click action.
	 * 
	 * @param event The click event.
	 * @param numberKeyPressed If a hotbar number was pressed.
	 * @param newArmorType The type of the new armor piece.
	 */
	private void createItemClickEvent(InventoryClickEvent event, boolean numberKeyPressed, ArmorType newArmorType) {
		if(event.isCancelled()) {
			return;
		}
		
		ItemStack newArmorPiece = event.getCursor();
		ItemStack oldArmorPiece = event.getCurrentItem();
		if(numberKeyPressed) {
			if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
				ItemStack hotbarItem = event.getClickedInventory().getItem(event.getHotbarButton());
				if(ItemUtils.isNotAir(hotbarItem)) {
					newArmorType = ArmorType.getArmorType(hotbarItem);
					newArmorPiece = hotbarItem;
					oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
				}
				else{
					ItemStack currentItem = event.getCurrentItem();
					newArmorType = ArmorType.getArmorType(ItemUtils.isNotAir(currentItem) ? currentItem : newArmorPiece);
				}
			}
		}
		else if(!ItemUtils.isNotAir(event.getCursor()) && ItemUtils.isNotAir(event.getCurrentItem())) {
			newArmorType = ArmorType.getArmorType(event.getCurrentItem());
		}
		if(newArmorType != null && event.getRawSlot() == newArmorType.getSlot()) {
			boolean hotbarSwap = event.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberKeyPressed;
			EquipMethod equipMethod = hotbarSwap ? EquipMethod.HOTBAR_SWAP : EquipMethod.PICK_DROP;
			
			Player player = (Player) event.getWhoClicked();
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, equipMethod,
					newArmorType, oldArmorPiece, newArmorPiece);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			
			if(armorEquipEvent.isCancelled()){
				event.setCancelled(true);
			}
		}
	}

	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event The interact event.
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#HOTBAR
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void playerInteractEvent(PlayerInteractEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		Action action = event.getAction();
		if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		final Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		ArmorType armorType = ArmorType.getArmorType(itemStack);
		if(armorType == null) {
			return;
		}
		
		if(isSlotFree(player, armorType)) {
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.HOTBAR, armorType, null, itemStack);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			if(armorEquipEvent.isCancelled()){
				event.setCancelled(true);
			}
		}
	}
	
	/**
	 * Checks if the slot of the given armor type is free.
	 * 
	 * @param player The player to check for.
	 * @param armorType The type of the armor slot.
	 * 
	 * @return If the slot is free.
	 */
	private boolean isSlotFree(Player player, ArmorType armorType) {
		switch (armorType) {
		case HELMET:
			return !ItemUtils.isNotAir(player.getInventory().getHelmet());
		case CHESTPLATE:
			return !ItemUtils.isNotAir(player.getInventory().getChestplate());
		case LEGGINGS:
			return !ItemUtils.isNotAir(player.getInventory().getLeggings());
		case BOOTS:
			return !ItemUtils.isNotAir(player.getInventory().getBoots());
		default:
			return false;
		}
	}

	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event The drag event.
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#DRAG
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void inventoryDrag(InventoryDragEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		ItemStack itemStack = event.getOldCursor();
		ArmorType armorType = ArmorType.getArmorType(event.getOldCursor());
		if(event.getRawSlots().isEmpty()) {
			return;
		}
		
		if(armorType != null && armorType.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)) {
			Player player = (Player) event.getWhoClicked();
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.DRAG, armorType, null, itemStack);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			
			if(armorEquipEvent.isCancelled()) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event The break event.
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#BROKE
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void itemBreakEvent(PlayerItemBreakEvent event) {
		ItemStack brokenItem = event.getBrokenItem();
		ArmorType armorType = ArmorType.getArmorType(brokenItem);
		if(armorType == null) {
			return;
		}
		
		Player player = event.getPlayer();
		ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.BROKE, armorType, brokenItem, null);
		Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
		
		if(armorEquipEvent.isCancelled()) {
			ItemStack itemStack = brokenItem.clone();
			itemStack.setAmount(1);
			
			Damageable damageable = (Damageable) itemStack.getItemMeta();
			damageable.setDamage(damageable.getDamage() - 1);
			itemStack.setItemMeta((ItemMeta) damageable);
			
			if(armorType.equals(ArmorType.HELMET)) {
				player.getInventory().setHelmet(itemStack);
			}
			else if(armorType.equals(ArmorType.CHESTPLATE)) {
				player.getInventory().setChestplate(itemStack);
			}
			else if(armorType.equals(ArmorType.LEGGINGS)) {
				player.getInventory().setLeggings(itemStack);
			}
			else if(armorType.equals(ArmorType.BOOTS)) {
				player.getInventory().setBoots(itemStack);
			}
		}
	}

	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event The death event.
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#DEATH
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void playerDeathEvent(PlayerDeathEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		Player player = event.getEntity();
		for(ItemStack itemStack : player.getInventory().getArmorContents()){
			if(ItemUtils.isNotAir(itemStack)){
				ArmorType armorType = ArmorType.getArmorType(itemStack);
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.DEATH, armorType, itemStack, null);
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			}
		}
	}
	
	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event The dispense event.
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#DISPENSER
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void dispenseArmorEvent(BlockDispenseArmorEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		ItemStack itemStack = event.getItem();
		ArmorType armorType = ArmorType.getArmorType(itemStack);
		if(armorType == null) {
			return;
		}
		
		if(!(event.getTargetEntity() instanceof Player)) {
			return;
		}
		
		Player player = (Player) event.getTargetEntity();
		ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DISPENSER,
				armorType, null, event.getItem());
		Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
		
		if(armorEquipEvent.isCancelled()) {
			event.setCancelled(true);
		}
	}

}
