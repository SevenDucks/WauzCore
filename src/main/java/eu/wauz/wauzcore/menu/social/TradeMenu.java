package eu.wauz.wauzcore.menu.social;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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

import eu.wauz.wauzcore.menu.LootContainer;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerTrade;


public class TradeMenu implements WauzInventory {
	
	/**
	 * Players (Requester and Requested)
	 */
    public static Player requestingPlayerName;
    public static Player requestedPlayerName;
    
    /**
     * Requesting Player respected slots
     */
    
    private int[] unavailableSlots = {0,1,2,3,4,5,6,7,8,13,12,22,23,31,12,21,23};
    private int[] requestingPlayerSlots = {9,10,11,18,19,20,27,28,19};
    private int[] requestedPlayerSlots = {15,16,17,24,25,26,33,34,35};
   // private List<int[]> ArraySlots = Arrays.asList(unavailableSlots);
    //private List<int[]> requestingPlayerSlotsArray = Arrays.asList(requestingPlayerSlots);
   // private List<int[]> requestedPlayerSlotsArray = Arrays.asList(requestedPlayerSlots);
    private List<ItemStack> requestingPlayerItems = new ArrayList<>();
    private List<ItemStack> requestedPlayerItems = new ArrayList<>();
    /**
     * Player Status 
     */
	private boolean requestingPlayerStatus = false;
	private boolean requestedPlayerStatus = false;
	private static String boltUnicode = StringEscapeUtils.unescapeJava("❖");
	/**
	 * Trade GUI Title
	 */
	private static String menuTitle =   "       " + ChatColor.BOLD + "" + ChatColor.DARK_GRAY + boltUnicode + ChatColor.GOLD + " TRADE " + ChatColor.DARK_GRAY + boltUnicode;

    /**
     * Lime Wool serves as a button for Ready and Means the Player is ready to trade.
     */
    private static ItemStack readyButton = new ItemStack(Material.LIME_WOOL);
    /**
     * Yellow Wool serves as a button for Unready and Means the Player is not Ready to trade.
     */
    private static ItemStack unreadyButton = new ItemStack(Material.YELLOW_WOOL);
    /**
     * Red Wool serves as a button for Cancel and Means the Player want discontinue the trade.
     */
    private static ItemStack cancelButton = new ItemStack(Material.RED_WOOL);
    /**
	 * Red Stain Glass indicates if a Player is Not Ready.
	 */
   private static ItemStack requestingPlayerNotReady = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    /**
     * Lime Stain Glass indicates if a Player is Ready.
     */
   private static ItemStack requestingPlayerReady = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
	/**
	 * Red Stain Glass indicates if a requested Player is Not Ready.
	 */
   private static ItemStack requestedPlayerNotReady = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    /**
     * Lime Stain Glass indicates if a requested Player is Ready.
     */
   private static ItemStack requestedPlayerReady = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
	
   
   private static WauzInventoryHolder holder = new WauzInventoryHolder(new TradeMenu());
	/**
	 * Create GUI for Requesting Player.
	 */
   private static Inventory tradeGUI = Bukkit.createInventory(holder, 36, menuTitle );
	/**
	 * Create GUI for Requested Player.
	 */
	
	/**
	 * Requesting Player Trade GUI
	 * @param requestingPlayer
	 */
	public static void onTrade(Player requestingPlayer, Player requestedPlayer) {
		
		/**
		 *  Create Items as Controls using Item Stack.
		 */
		/**
		 * Black Stain Glass serves as a border between Players GUI or Slots.
		 */
		ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		/**
		 * Requesting Player Head.
		 */
		ItemStack requestingPlayerHead = new ItemStack(Material.PLAYER_HEAD);
		/**
		 * Requested Player Head.
		 */
		ItemStack requestedPlayerHead = new ItemStack(Material.PLAYER_HEAD);
		/*
		 * Item Meta - Get item stacks meta
		 */
		ItemMeta borderMeta = border.getItemMeta();
		SkullMeta requestingPlayerHeadMeta = (SkullMeta) requestingPlayerHead.getItemMeta();
		SkullMeta requestedPlayerHeadMeta = (SkullMeta) requestedPlayerHead.getItemMeta();
		ItemMeta requestingPlayerNotReadyMeta = requestingPlayerNotReady.getItemMeta();
		ItemMeta requestingPlayerReadyMeta = requestingPlayerReady.getItemMeta();
		ItemMeta requestedPlayerNotReadyMeta = requestedPlayerNotReady.getItemMeta();
		ItemMeta requestedPlayerReadyMeta = requestedPlayerReady.getItemMeta();
		ItemMeta readyButtonMeta = readyButton.getItemMeta();
		ItemMeta unreadyButtonMeta = readyButton.getItemMeta();
		ItemMeta cancelButtonName = cancelButton.getItemMeta();
		/**
		 *  Setting Display names for Items
		 */
		borderMeta.setDisplayName(" ");
		requestingPlayerHeadMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + requestingPlayerName.getName());
		requestingPlayerHeadMeta.setOwningPlayer(requestingPlayerName);
		requestedPlayerHeadMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + requestedPlayerName.getName());
		requestedPlayerHeadMeta.setOwningPlayer(requestedPlayerName);
		requestingPlayerNotReadyMeta.setDisplayName(ChatColor.BOLD + requestingPlayer.getName() + ": " + ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"NOT READY");
		requestingPlayerReadyMeta.setDisplayName(ChatColor.BOLD + requestingPlayer.getName() + ": " + ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"READY");
		requestedPlayerNotReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayerName.getName() + ": " + ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"NOT READY");
		requestedPlayerReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayerName.getName() + ": " + ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"READY");
		readyButtonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "READY");
		unreadyButtonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "UNREADY");
		cancelButtonName.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCEL TRADE");
		
		/**
		 * Finalize Display Name
		 */
		border.setItemMeta(borderMeta);
		requestingPlayerHead.setItemMeta(requestingPlayerHeadMeta);
		requestedPlayerHead.setItemMeta(requestedPlayerHeadMeta);
		requestingPlayerNotReady.setItemMeta(requestingPlayerNotReadyMeta);
		requestingPlayerReady.setItemMeta(requestingPlayerReadyMeta);
		requestedPlayerNotReady.setItemMeta(requestedPlayerNotReadyMeta);
		requestedPlayerReady.setItemMeta(requestedPlayerReadyMeta);
		readyButton.setItemMeta(readyButtonMeta);
		unreadyButton.setItemMeta(unreadyButtonMeta);
		cancelButton.setItemMeta(cancelButtonName);
		
		/**
		 * Requesting Player GUI View
		 */
		tradeGUI.setItem(1,requestingPlayerHead);
		tradeGUI.setItem(12, readyButton);
		tradeGUI.setItem(21, requestingPlayerNotReady);
		tradeGUI.setItem(30, cancelButton);
		
		/**
		 * Requesting Player clearing trade slots.
		 */
		tradeGUI.setItem(9, null);
		tradeGUI.setItem(10, null);
		tradeGUI.setItem(11, null);
		tradeGUI.setItem(18, null);
		tradeGUI.setItem(19, null);
		tradeGUI.setItem(20, null);
		tradeGUI.setItem(27, null);
		tradeGUI.setItem(28, null);
		tradeGUI.setItem(29, null);
		/**
		 * Borders
		 */
		tradeGUI.setItem(0, border);
		tradeGUI.setItem(2, border);
		tradeGUI.setItem(3, border);
		tradeGUI.setItem(4, border);
		tradeGUI.setItem(5, border);
		tradeGUI.setItem(6, border);
		tradeGUI.setItem(8, border);
		tradeGUI.setItem(13, border);
		tradeGUI.setItem(22, border);
		tradeGUI.setItem(31, border);
		
		/**
		 * Requested Player GUI View
		 */
		tradeGUI.setItem(7,requestedPlayerHead);
		tradeGUI.setItem(14, readyButton);
		tradeGUI.setItem(23, requestedPlayerNotReady);
		tradeGUI.setItem(32, cancelButton);
		/**
		 * Requesting Player clearing trade slots.
		 */
		tradeGUI.setItem(15, null);
		tradeGUI.setItem(16, null);
		tradeGUI.setItem(17, null);
		tradeGUI.setItem(24, null);
		tradeGUI.setItem(25, null);
		tradeGUI.setItem(26, null);
		tradeGUI.setItem(33, null);
		tradeGUI.setItem(34, null);
		tradeGUI.setItem(35, null);

		/**
		 * Open TradeGUI View for Requesting Player
		 */
		requestingPlayer.openInventory(tradeGUI);
		requestedPlayer.openInventory(tradeGUI);
		requestingPlayer.playSound(requestingPlayer.getLocation(), Sound.BLOCK_CHEST_OPEN, 10, 1);
		requestedPlayer.playSound(requestedPlayer.getLocation(), Sound.BLOCK_CHEST_OPEN, 10, 1);
	}
	
	
	

	@Override
	public String getInventoryId() {
		return null;
	}

	
	/*
	 * InventoryCloseEvent - execute code when TradeGUI get close.
	 */
	@Override
	public void destroyInventory(InventoryCloseEvent event) {
		/*
		Player player = (Player) event.getPlayer();
        if(player.getName().equals(requestingPlayerName.getName())) {
        	player.sendMessage(ChatColor.RED + "You cancelled the trade.");
        	requestedPlayerName.sendMessage(ChatColor.RED + player.getName() + " cancelled the trade.");
        	WauzPlayerTrade.playersOnTrading.remove(player.getName(), requestedPlayerName.getName());
        	WauzPlayerTrade.playersOnTrading.remove(requestedPlayerName.getName(),player.getName());
        }
        if(player.getName().equals(requestedPlayerName.getName())) {
        	player.sendMessage(ChatColor.RED + "You cancelled the trade.");
        	requestingPlayerName.sendMessage(ChatColor.RED + player.getName() + " cancelled the trade.");
        	WauzPlayerTrade.playersOnTrading.remove(player.getName(), requestedPlayerName.getName());
        	WauzPlayerTrade.playersOnTrading.remove(requestedPlayerName.getName(),player.getName());
        }

		
		*/
			Player player = (Player) event.getPlayer();
			if(player.getName().equals(requestingPlayerName.getName())) { 

					player.sendMessage(ChatColor.RED + "You cancelled the trade.");
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestedPlayerName.sendMessage(ChatColor.RED + player.getName() + "cancelled the trade.");
					requestedPlayerName.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestedPlayerName.closeInventory();
					player.closeInventory();
					WauzPlayerTrade.playersOnTrading.remove(player.getName());
					WauzPlayerTrade.playersOnTrading.remove(requestedPlayerName.getName());
				
			}
			if(player.getName().equals(requestedPlayerName.getName())){

					player.sendMessage(ChatColor.RED + "You cancelled the trade.");
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestingPlayerName.sendMessage(ChatColor.RED + player.getName() + "cancelled the trade.");
					requestingPlayerName.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestingPlayerName.closeInventory();
					player.closeInventory();
					WauzPlayerTrade.playersOnTrading.remove(player.getName());
					WauzPlayerTrade.playersOnTrading.remove(requestedPlayerName.getName());
				
			}
       
	}
	
	@Override
	public void openInstance(Player player) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * InventoryClickEvent - Trade controls.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
	Player player = (Player) event.getWhoClicked();
	
		
		//if(!event.getView().getTitle().equals(menuTitle)) return; 

        
        for(int i = 0; i < unavailableSlots.length; i++) {
        	if(event.getSlot() == unavailableSlots[i]) {
        		event.setCancelled(true);
        	}
        }

        /**
         * Requesting Player on click.
         */
		if(player.getName().equals(requestingPlayerName.getName())) {
	        for(int i = 0; i < requestingPlayerSlots.length; i++) {
	        	if(event.getSlot() == requestingPlayerSlots[i] && requestingPlayerStatus == true) {
	        		event.setCancelled(true);
	        	}
	        }
	        /**
			if(event.getClickedInventory() == null) return;
			if(event.getClickedInventory().getItem(event.getSlot()) == null) return;
			/*
			if(event.getClickedInventory().getHolder() instanceof MenuHolder) {
				
			}
			*/
			if(event.getSlot() == 12) {
				
				if(requestingPlayerStatus == false && requestedPlayerStatus == false) {
					requestingPlayerStatus = true;
					
					tradeGUI.setItem(12, unreadyButton);
					tradeGUI.setItem(21, requestingPlayerReady);
					requestingPlayerName.updateInventory();
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 1);
					requestedPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
				}else if(requestingPlayerStatus == false && requestedPlayerStatus == true) {
					requestingPlayerStatus = true;
					if(requestingPlayerStatus == true && requestedPlayerStatus == true) {
						for(int loopingSlots = 0; loopingSlots < 9; loopingSlots++) {
							int requestingPlayerSlot = requestingPlayerSlots[loopingSlots];
							int requestedPlayerSlot = requestedPlayerSlots[loopingSlots];
							requestingPlayerItems.add(tradeGUI.getItem(requestingPlayerSlot));
							requestedPlayerItems.add(tradeGUI.getItem(requestedPlayerSlot));
						}
						LootContainer.open(requestedPlayerName, requestingPlayerItems);
						LootContainer.open(requestingPlayerName, requestedPlayerItems);
					}
				}else if(requestingPlayerStatus == true && requestedPlayerStatus == false) {
					requestingPlayerStatus = false;
					tradeGUI.setItem(12, readyButton);
					tradeGUI.setItem(21, requestingPlayerNotReady);
					requestingPlayerName.updateInventory();
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 1);
					requestedPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
				}else {
					return;
				}
			event.setCancelled(true);
		}else if(event.getSlot() == 9 || event.getSlot() == 10 || event.getSlot() == 11 || event.getSlot() == 18 || event.getSlot() == 19 || event.getSlot() == 20 || event.getSlot() == 27 || event.getSlot() == 28 || event.getSlot() == 29) {
				if(requestingPlayerStatus == true) {
					event.setCancelled(true);
				}
		}else if(event.getSlot() == 15 || event.getSlot() == 16 || event.getSlot() == 17 || event.getSlot() == 24 || event.getSlot() == 25 || event.getSlot() == 26 || event.getSlot() == 33 || event.getSlot() == 34 || event.getSlot() == 35) {
				event.setCancelled(true);
		}else if(event.getSlot() == 30) {
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				requestedPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				player.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD + "TRADE" + ChatColor.DARK_RED + "]" + ChatColor.GOLD + "You" + ChatColor.DARK_RED + " Cancelled the trade.");
				requestedPlayerName.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD + "TRADE" + ChatColor.DARK_RED + "]" + ChatColor.GOLD + player.getName() + ChatColor.DARK_RED + " Cancelled the trade.");
				requestingPlayerName.closeInventory();
				requestedPlayerName.closeInventory();
				requestingPlayerStatus = false;
				requestedPlayerStatus = false;
				event.setCancelled(true);
		}else {
			return;
		}

	}
		/**
		 * Requested Player on click.
		 */
		if(player.getName().equals(requestedPlayerName.getName())) {
	        for(int i = 0; i < requestedPlayerSlots.length; i++) {
	        	if(event.getSlot() == requestedPlayerSlots[i] && requestedPlayerStatus == true) {
	        		event.setCancelled(true);
	        	}
	        }
	        /**
			if(event.getClickedInventory() == null) return;
			if(event.getClickedInventory().getItem(event.getSlot()) == null) return;
			/*
			if(event.getClickedInventory().getHolder() instanceof MenuHolder) {
				
			}
			*/
			if(event.getSlot() == 14) {
				
				if(requestedPlayerStatus == false && requestingPlayerStatus == false) {
					requestedPlayerStatus = true;
					tradeGUI.setItem(14, unreadyButton);
					tradeGUI.setItem(23, requestedPlayerReady);
					requestedPlayerName.updateInventory();
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 1);
					requestingPlayerName.playSound(requestingPlayerName.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
				}else if(requestedPlayerStatus == false && requestingPlayerStatus == true) {
					requestedPlayerStatus = true;
					if(requestedPlayerStatus == true && requestingPlayerStatus == true) {
						for(int loopingSlots = 0; loopingSlots < 9; loopingSlots++) {
							int requestingPlayerSlot = requestingPlayerSlots[loopingSlots];
							int requestedPlayerSlot = requestedPlayerSlots[loopingSlots];
							requestingPlayerItems.add(tradeGUI.getItem(requestingPlayerSlot));
							requestedPlayerItems.add(tradeGUI.getItem(requestedPlayerSlot));
						}
						LootContainer.open(requestedPlayerName, requestingPlayerItems);
						LootContainer.open(requestingPlayerName, requestedPlayerItems);
					}
				}else if(requestedPlayerStatus == true && requestingPlayerStatus == false) {
					requestedPlayerStatus = false;
					tradeGUI.setItem(14, readyButton);
					tradeGUI.setItem(23, requestingPlayerNotReady);
					requestedPlayerName.updateInventory();
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 1);
					requestingPlayerName.playSound(requestingPlayerName.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
				}else {
					return;
				}
			event.setCancelled(true);
		}else if(event.getSlot() == 15 || event.getSlot() == 16 || event.getSlot() == 17 || event.getSlot() == 24 || event.getSlot() == 25 || event.getSlot() == 26 || event.getSlot() == 33 || event.getSlot() == 34 || event.getSlot() == 35) {
				if(requestedPlayerStatus == true) {
					event.setCancelled(true);
				}
		}else if(event.getSlot() == 9 || event.getSlot() == 10 || event.getSlot() == 11 || event.getSlot() == 18 || event.getSlot() == 19 || event.getSlot() == 20 || event.getSlot() == 27 || event.getSlot() == 28 || event.getSlot() == 29) {
			event.setCancelled(true);
		}else if(event.getSlot() == 32) {
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				requestingPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				player.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD + "TRADE" + ChatColor.DARK_RED + "]" + ChatColor.GOLD + "You" + ChatColor.DARK_RED + " Cancelled the trade.");
				requestingPlayerName.sendMessage(ChatColor.RED + "[" + ChatColor.GOLD + "TRADE" + ChatColor.DARK_RED + "]" + ChatColor.GOLD + player.getName() + ChatColor.DARK_RED + " Cancelled the trade.");
				requestingPlayerName.closeInventory();
				requestedPlayerName.closeInventory();
				requestingPlayerStatus = false;
				requestedPlayerStatus = false;
				event.setCancelled(true);

		}else {
			return;
		}

		}
	}
}