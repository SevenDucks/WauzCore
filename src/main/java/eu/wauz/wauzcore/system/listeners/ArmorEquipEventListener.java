package eu.wauz.wauzcore.system.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
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
	 * @param event
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#SHIFT_CLICK
	 * @see EquipMethod#HOTBAR_SWAP
	 * @see EquipMethod#PICK_DROP
	 */
	@EventHandler
	public final void onInventoryClick(final InventoryClickEvent event){
		if(event.isCancelled()) {
			return;
		}
		
		if(event.getAction() == InventoryAction.NOTHING) {
			return;
		}
		
		SlotType slotType = event.getSlotType();
		if(slotType != SlotType.ARMOR && slotType != SlotType.QUICKBAR && slotType != SlotType.CONTAINER) {
			return;
		}
		
		Inventory clickedInventory = event.getClickedInventory();
		if(clickedInventory != null && !clickedInventory.getType().equals(InventoryType.PLAYER)) {
			return;
		}
		
		Inventory inventory = event.getInventory();
		if (!inventory.getType().equals(InventoryType.CRAFTING) && !inventory.getType().equals(InventoryType.PLAYER)) {
			return;
		}
		
		if(!(event.getWhoClicked() instanceof Player)) {
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
			if(newArmorType == null) {
				return;
			}
			
			boolean equipping = event.getRawSlot() != newArmorType.getSlot();
			Player player = (Player) event.getWhoClicked();
			
			boolean isHelmet = newArmorType.equals(ArmorType.HELMET) && (equipping
					? isAirOrNull(event.getWhoClicked().getInventory().getHelmet())
					: !isAirOrNull(event.getWhoClicked().getInventory().getHelmet()));
			boolean isChestplate = newArmorType.equals(ArmorType.CHESTPLATE) && (equipping
					? isAirOrNull(event.getWhoClicked().getInventory().getChestplate())
					: !isAirOrNull(event.getWhoClicked().getInventory().getChestplate()));
			boolean isLeggings = newArmorType.equals(ArmorType.LEGGINGS) && (equipping
					? isAirOrNull(event.getWhoClicked().getInventory().getLeggings())
					: !isAirOrNull(event.getWhoClicked().getInventory().getLeggings()));
			boolean isBoots = newArmorType.equals(ArmorType.BOOTS) && (equipping
					? isAirOrNull(event.getWhoClicked().getInventory().getBoots())
					: !isAirOrNull(event.getWhoClicked().getInventory().getBoots()));

			if(isHelmet || isChestplate || isLeggings || isBoots) {
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.SHIFT_CLICK,
						newArmorType, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null);
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
				
				if(armorEquipEvent.isCancelled()) {
					event.setCancelled(true);
				}
			}
		}
		else{
			ItemStack newArmorPiece = event.getCursor();
			ItemStack oldArmorPiece = event.getCurrentItem();
			if(numberKeyPressed) {
				if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
					ItemStack hotbarItem = event.getClickedInventory().getItem(event.getHotbarButton());
					if(!isAirOrNull(hotbarItem)) {
						newArmorType = ArmorType.getArmorType(hotbarItem);
						newArmorPiece = hotbarItem;
						oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
					}
					else{
						ItemStack currentItem = event.getCurrentItem();
						newArmorType = ArmorType.getArmorType(!isAirOrNull(currentItem) ? currentItem : newArmorPiece);
					}
				}
			}
			else{
				if(isAirOrNull(event.getCursor()) && !isAirOrNull(event.getCurrentItem())) {
					newArmorType = ArmorType.getArmorType(event.getCurrentItem());
				}
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
	}

	/**
	 * An event handler to listen for the (un)equipment of armor.
	 * Creates corresponding armor equip events for fitting equip methods.
	 * 
	 * @param event
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#HOTBAR
	 */
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		if(event.getAction() == Action.PHYSICAL) {
			return;
		}
		
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		final Player player = event.getPlayer();
		
		ItemStack itemStack = event.getItem();
		ArmorType armorType = ArmorType.getArmorType(itemStack);
		if(armorType == null) {
			return;
		}
		
		boolean isHelmet = armorType.equals(ArmorType.HELMET)
				&& isAirOrNull(event.getPlayer().getInventory().getHelmet());
		boolean isChestplate = armorType.equals(ArmorType.CHESTPLATE)
				&& isAirOrNull(event.getPlayer().getInventory().getChestplate());
		boolean isLeggings = armorType.equals(ArmorType.LEGGINGS)
				&& isAirOrNull(event.getPlayer().getInventory().getLeggings());
		boolean isBoots = armorType.equals(ArmorType.BOOTS)
				&& isAirOrNull(event.getPlayer().getInventory().getBoots());
		
		if(isHelmet || isChestplate || isLeggings || isBoots) {
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, EquipMethod.HOTBAR, armorType, null, itemStack);
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
	 * @param event
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#DRAG
	 */
	@EventHandler
	public void inventoryDrag(InventoryDragEvent event) {
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
	 * @param event
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#BROKE
	 */
	@EventHandler
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
	 * @param event
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#DEATH
	 */
	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		for(ItemStack itemStack : player.getInventory().getArmorContents()){
			if(!isAirOrNull(itemStack)){
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
	 * @param event
	 * 
	 * @see ArmorEquipEvent
	 * @see EquipMethod#DISPENSER
	 */
	@EventHandler
	public void dispenseArmorEvent(BlockDispenseArmorEvent event){
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
		
		if(armorEquipEvent.isCancelled()){
			event.setCancelled(true);
		}
	}

	/**
	 * @param itemStack An item to check.
	 * 
	 * @return If the given item is air or null.
	 */
	private boolean isAirOrNull(ItemStack itemStack) {
		return itemStack == null || itemStack.getType().equals(Material.AIR);
	}

}
