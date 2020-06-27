package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.LootContainer;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerTrade;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Lets two players trade witch eachother.
 * 
 * @author eddshine
 *
 * @see WauzPlayerTrade
 */
public class TradeMenu implements WauzInventory {
	
	/**
	 * Item slots on the left side of the trade menu.
	 */
	private static List<Integer> leftSlots = Arrays.asList(9, 10, 11, 18, 19, 20, 27, 28, 29);
	
	/**
	 * Item slots on the right side of the trade menu.
	 */
    private static List<Integer> rightSlots = Arrays.asList(15, 16, 17, 24, 25, 26, 33, 34, 35);
    
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "trading";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		throw new RuntimeException("Inventory cannot be opened directly!");
	}
	
	/**
	 * Opens the menu for the given players.
	 * The players can put items on their side of the window.
	 * If one player is ready items cannot be changed anymore.
	 * If both players are ready the items will be traded.
	 * When cancel is clicked, both receive their items back.
	 * 
	 * @param requestingPlayer The player on the left side of the trade menu.
	 * @param requestedPlayer The player on the right side of the trade menu.
	 * 
	 * @see TradeMenu#setMenuInventory(Inventory)
	 * @see TradeMenu#setReadyStatus(boolean, boolean)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void onTrade(Player requestingPlayer, Player requestedPlayer) {
		TradeMenu tradeMenu = new TradeMenu(requestingPlayer, requestedPlayer);
		WauzInventoryHolder holder = new WauzInventoryHolder(tradeMenu);
		Inventory menu = Bukkit.createInventory(holder, 36, ChatColor.BLACK + "" + ChatColor.BOLD + "Player Trading");
		tradeMenu.setMenuInventory(menu);
		tradeMenu.setReadyStatus(true, false);
		tradeMenu.setReadyStatus(false, false);
		
		ItemStack requestingPlayerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta requestingPlayerHeadMeta = (SkullMeta) requestingPlayerHead.getItemMeta();
		requestingPlayerHeadMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + requestingPlayer.getName());
		requestingPlayerHeadMeta.setOwningPlayer(requestingPlayer);
		requestingPlayerHead.setItemMeta(requestingPlayerHeadMeta);
		menu.setItem(1, requestingPlayerHead);
		
		ItemStack requestedPlayerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta requestedPlayerHeadMeta = (SkullMeta) requestedPlayerHead.getItemMeta();
		requestedPlayerHeadMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + requestedPlayer.getName());
		requestedPlayerHeadMeta.setOwningPlayer(requestedPlayer);
		requestedPlayerHead.setItemMeta(requestedPlayerHeadMeta);
		menu.setItem(7, requestedPlayerHead);
		
		ItemStack cancelButton = HeadUtils.getDeclineItem();
		ItemMeta cancelButtonName = cancelButton.getItemMeta();
		cancelButtonName.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCEL TRADE");
		cancelButton.setItemMeta(cancelButtonName);
		menu.setItem(30, cancelButton);
		menu.setItem(32, cancelButton);
		
		MenuUtils.setBorders(menu);
		for(int leftSlot : leftSlots) {
			menu.setItem(leftSlot, null);
		}
		for(int rightSlot : rightSlots) {
			menu.setItem(rightSlot, null);
		}
		
		requestingPlayer.openInventory(menu);
		requestedPlayer.openInventory(menu);
		requestingPlayer.playSound(requestingPlayer.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		requestedPlayer.playSound(requestedPlayer.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
	}
	
	/**
	 * The trade menu inventory.
	 */
	private Inventory menu;
	
	/**
	 * The player on the left side of the trade menu.
	 */
	private final Player leftPlayer;
	
	/**
	 * The player on the right side of the trade menu.
	 */
	private final Player rightPlayer;
	
	/**
	 * The ready status of the player on the left side of the trade menu.
	 */
	private boolean leftPlayerReady;
	
	/**
	 * The ready status of the player on the right side of the trade menu.
	 */
	private boolean rightPlayerReady;
	
	/**
	 * Creates a new trade menu instance.
	 * 
	 * @param leftPlayer The player on the left side of the trade menu.
	 * @param rightPlayer The player on the right side of the trade menu.
	 */
	public TradeMenu(Player leftPlayer, Player rightPlayer) {
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
	}
	
	/**
	 * Sets the inventory for the trade menu.
	 * 
	 * @param menu The new trade menu inventory.
	 */
	public void setMenuInventory(Inventory menu) {
		this.menu = menu;
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * Players can move items in their own inventories and on their menu side.
	 * The ready status can be changed or the trade can be cancelled.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see TradeMenu#setReadyStatus(boolean, boolean)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		String clickType = event.getClick().toString();
		if(clickType.contains("SHIFT")) {
			event.setCancelled(true);
			return;
		}
		int slot = event.getRawSlot();
        if(slot > 35) {
        	return;
        }
        
        Player player = (Player) event.getWhoClicked();
        boolean anyoneReady = leftPlayerReady || rightPlayerReady;
        if(player.equals(leftPlayer)) {
        	if(leftSlots.contains(slot) && !anyoneReady) {
        		return;
        	}
        	else if(slot == 12) {
        		setReadyStatus(true, !leftPlayerReady);
        		rightPlayer.playSound(rightPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
        	}
        	else if(slot == 30) {
        		player.closeInventory();
        	}
        }
        else if(player.equals(rightPlayer)) {
        	if(rightSlots.contains(slot) && !anyoneReady) {
        		return;
        	}
        	else if(slot == 14) {
        		setReadyStatus(false, !rightPlayerReady);
        		leftPlayer.playSound(leftPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
        	}
        	else if(slot == 32) {
        		player.closeInventory();
        	}
        }
        event.setCancelled(true);
	}
	
	/**
	 * Cleans up everything, so the inventory can be closed.
	 * The player's will be notified, that the trade ended and will receive their items.
	 * 
	 * @param event The inventory close event.
	 * 
	 * @see TradeMenu#handOutItems(boolean)
	 */
	@Override
	public void destroyInventory(InventoryCloseEvent event) {
		if(leftPlayerReady && rightPlayerReady) {
			return;
		}
		leftPlayerReady = true;
		rightPlayerReady = true;
		
		leftPlayer.sendMessage(ChatColor.RED + "The trade was cancelled!");
		rightPlayer.sendMessage(ChatColor.RED + "The trade was cancelled!");
		leftPlayer.playSound(leftPlayer.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
		rightPlayer.playSound(rightPlayer.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {

			@Override
			public void run() {
				handOutItems(true);
			}
			
		}, 5);
	}
	
	/**
	 * Sets the ready status of one side of the trade menu.
	 * When both players are ready the trade will be executed.
	 * 
	 * @param leftSide If the status of the left side should be set. Else it will be the right side.
	 * @param isReady If the given side is ready.
	 * 
	 * @see TradeMenu#handOutItems(boolean)
	 */
	public void setReadyStatus(boolean leftSide, boolean isReady) {
		ItemStack readyButtonItemStack = isReady ? HeadUtils.getConfirmItem() : HeadUtils.getTitlesItem();
		MenuUtils.setItemDisplayName(readyButtonItemStack, ChatColor.GOLD + "" + ChatColor.BOLD + "CHANGE READY STATUS");
		
		ItemStack readyStatusItemStack = new ItemStack(isReady ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
		String readyString = isReady ? ChatColor.GREEN + "ready" : ChatColor.RED + "not ready";
		readyString = ChatColor.WHITE + "This player is " + readyString + ChatColor.WHITE + "!";
		MenuUtils.setItemDisplayName(readyStatusItemStack, readyString);
		
		if(leftSide) {
			leftPlayerReady = isReady;
			menu.setItem(12, readyButtonItemStack);
			menu.setItem(21, readyStatusItemStack);
		}
		else {
			rightPlayerReady = isReady;
			menu.setItem(14, readyButtonItemStack);
			menu.setItem(23, readyStatusItemStack);
		}
		
		if(leftPlayerReady && rightPlayerReady) {
			leftPlayer.sendMessage(ChatColor.GREEN + "The trade was successful!");
			rightPlayer.sendMessage(ChatColor.GREEN + "The trade was successful!");
			leftPlayer.playSound(leftPlayer.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
			rightPlayer.playSound(rightPlayer.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
			handOutItems(false);
		}
	}
	
	/**
	 * Hands out the traded items to the players.
	 * 
	 * @param ownItemsBack If the players should receive their own items back, instead of the traded ones.
	 * 
	 * @see TradeMenu#fillItemList(List, List)
	 * @see LootContainer#open(Player, List)
	 */
	private void handOutItems(boolean ownItemsBack) {
		List<ItemStack> leftItems = new ArrayList<>();
		fillItemList(leftItems, leftSlots);
		List<ItemStack> rightItems = new ArrayList<>();
		fillItemList(rightItems, rightSlots);
		
		LootContainer.open(ownItemsBack ? leftPlayer : rightPlayer, leftItems);
		LootContainer.open(ownItemsBack ? rightPlayer : leftPlayer, rightItems);
	}
	
	/**
	 * Fills the given list with the item stacks from the given menu slots.
	 * 
	 * @param itemList The list that should be filled.
	 * @param itemIndexes The indexes of the menu slots.
	 */
	private void fillItemList(List<ItemStack> itemList, List<Integer> itemIndexes) {
		for(int index : itemIndexes) {
			ItemStack itemStack = menu.getItem(index);
			if(ItemUtils.isNotAir(itemStack)) {
				itemList.add(itemStack);
			}
		}
	}
	
}
