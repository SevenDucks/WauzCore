package eu.wauz.wauzcore.menu;


import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


public class TradeMenu implements Listener {
	
	/**
	 * Players (Requester and Requested)
	 */
    public static Player requestingPlayerName;
    public static Player requestedPlayerName;
    
    /**
     * Requesting Player respected slots
     */
    private int[] unavailableSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 12, 22, 23, 31, 12, 21, 23};
    
    /**
     * Player Status 
     */
	private boolean requestingPlayerStatus = false;
	private boolean requestedPlayerStatus = false;
	
	/**
	 * Trade GUI Title
	 */
	private static String menuTitle = ChatColor.DARK_GREEN + "Trading...";

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
	
	/**
	 * Create GUI for Requesting Player.
	 */
   private static Inventory requestingPlayerTradeGUI = Bukkit.createInventory(requestingPlayerName, 36, menuTitle );
	/**
	 * Create GUI for Requested Player.
	 */
	
	/**
	 * Requesting Player Trade GUI
	 * @param requestingPlayer
	 */
	public static void requestingPlayerMenu(Player requestingPlayer) {
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
		requestingPlayerNotReadyMeta.setDisplayName(ChatColor.BOLD + requestingPlayer.getName() + " " + ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"NOT READY");
		requestingPlayerReadyMeta.setDisplayName(ChatColor.BOLD + requestingPlayer.getName() + " " + ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"READY");
		requestedPlayerNotReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayerName.getName() + " " + ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"NOT READY");
		requestedPlayerReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayerName.getName() + " " + ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"READY");
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
		requestingPlayerTradeGUI.setItem(1,requestingPlayerHead);
		requestingPlayerTradeGUI.setItem(12, readyButton);
		requestingPlayerTradeGUI.setItem(21, requestingPlayerNotReady);
		requestingPlayerTradeGUI.setItem(30, cancelButton);
		
		/**
		 * Borders
		 */
		requestingPlayerTradeGUI.setItem(0, border);
		requestingPlayerTradeGUI.setItem(2, border);
		requestingPlayerTradeGUI.setItem(3, border);
		requestingPlayerTradeGUI.setItem(4, border);
		requestingPlayerTradeGUI.setItem(5, border);
		requestingPlayerTradeGUI.setItem(6, border);
		requestingPlayerTradeGUI.setItem(8, border);
		requestingPlayerTradeGUI.setItem(13, border);
		requestingPlayerTradeGUI.setItem(22, border);
		requestingPlayerTradeGUI.setItem(31, border);
		
		/**
		 * Requested Player GUI View
		 */
		requestingPlayerTradeGUI.setItem(7,requestedPlayerHead);
		requestingPlayerTradeGUI.setItem(14, readyButton);
		requestingPlayerTradeGUI.setItem(23, requestedPlayerNotReady);
		requestingPlayerTradeGUI.setItem(32, cancelButton);

		/**
		 * Open TradeGUI View for Requesting Player
		 */
		requestingPlayer.openInventory(requestingPlayerTradeGUI);
		requestingPlayer.playSound(requestingPlayer.getLocation(), Sound.BLOCK_CHEST_OPEN, 10, 1);
	}
	
	
	private static  Inventory requestedPlayerTradeGUI = Bukkit.createInventory(requestedPlayerName, 36, menuTitle );
	/**
	 * Requested Player Trade GUI
	 * @param requestingPlayer
	 */
	public static void requestedPlayerMenu(Player requestedPlayer) {
		
		/**
		 * Create GUI for Requested Player.
		 */

		/**
		 *  Create Items as Controls using Item Stack.
		 */
		/**
		 * Black Stain Glass serves as a border between Players GUI or Slots.
		 */
		ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		/**
		 * Requested Player Head.
		 */
		ItemStack requestedPlayerHead = new ItemStack(Material.PLAYER_HEAD);
		/**
		 * Requesting Player Head.
		 */
		ItemStack requestingPlayerHead = new ItemStack(Material.PLAYER_HEAD);
		/*
		 * Item Meta - Get item stacks meta
		 */
		ItemMeta borderMeta = border.getItemMeta();
		SkullMeta requestedPlayerHeadMeta = (SkullMeta) requestedPlayerHead.getItemMeta();
		SkullMeta requestingPlayerHeadMeta = (SkullMeta) requestingPlayerHead.getItemMeta();
		ItemMeta requestedPlayerNotReadyMeta = requestedPlayerNotReady.getItemMeta();
		ItemMeta requestedPlayerReadyMeta = requestedPlayerReady.getItemMeta();
		ItemMeta requestingPlayerNotReadyMeta = requestingPlayerNotReady.getItemMeta();
		ItemMeta requestingPlayerReadyMeta = requestingPlayerReady.getItemMeta();
		ItemMeta readyButtonMeta = readyButton.getItemMeta();
		ItemMeta unreadyButtonMeta = readyButton.getItemMeta();
		ItemMeta cancelButtonName = cancelButton.getItemMeta();
		/**
		 *  Setting Display names for Items
		 */
		borderMeta.setDisplayName(" ");
		requestedPlayerHeadMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + requestedPlayerName.getName());
		requestedPlayerHeadMeta.setOwningPlayer(requestedPlayerName);
		requestingPlayerHeadMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + requestingPlayerName.getName());
		requestingPlayerHeadMeta.setOwningPlayer(requestingPlayerName);
		requestedPlayerNotReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayer.getName() + ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"NOT READY");
		requestedPlayerReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayer.getName() + ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"READY");
		requestingPlayerNotReadyMeta.setDisplayName(ChatColor.BOLD + requestedPlayer.getName() + " " + ChatColor.RED +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"NOT READY");
		requestingPlayerReadyMeta.setDisplayName(ChatColor.BOLD + requestingPlayerName.getName() + " " + ChatColor.GREEN +""+ ChatColor.BOLD +""+ ChatColor.ITALIC+"READY");
		readyButtonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "READY");
		unreadyButtonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "UNREADY");
		cancelButtonName.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CANCEL TRADE");
		
		/**
		 * Finalize Display Name
		 */
		border.setItemMeta(borderMeta);
		requestedPlayerHead.setItemMeta(requestedPlayerHeadMeta);
		requestingPlayerHead.setItemMeta(requestingPlayerHeadMeta);
		requestedPlayerNotReady.setItemMeta(requestedPlayerNotReadyMeta);
		requestedPlayerReady.setItemMeta(requestedPlayerReadyMeta);
		requestingPlayerNotReady.setItemMeta(requestingPlayerNotReadyMeta);
		requestingPlayerReady.setItemMeta(requestingPlayerReadyMeta);
		readyButton.setItemMeta(readyButtonMeta);
		unreadyButton.setItemMeta(unreadyButtonMeta);
		cancelButton.setItemMeta(cancelButtonName);
		
		/**
		 * Requested Player GUI View
		 */
		requestedPlayerTradeGUI.setItem(1,requestedPlayerHead);
		requestedPlayerTradeGUI.setItem(3, readyButton);
		requestedPlayerTradeGUI.setItem(12, requestedPlayerNotReady);
		requestedPlayerTradeGUI.setItem(21, cancelButton);
		
		/**
		 * Borders
		 */
		requestedPlayerTradeGUI.setItem(0, border);
		requestedPlayerTradeGUI.setItem(2, border);
		requestedPlayerTradeGUI.setItem(3, border);
		requestedPlayerTradeGUI.setItem(4, border);
		requestedPlayerTradeGUI.setItem(5, border);
		requestedPlayerTradeGUI.setItem(6, border);
		requestedPlayerTradeGUI.setItem(8, border);
		requestingPlayerTradeGUI.setItem(13, border);
		requestingPlayerTradeGUI.setItem(22, border);
		requestingPlayerTradeGUI.setItem(31, border);
		
		/**
		 * Requesting Player GUI View
		 */
		requestedPlayerTradeGUI.setItem(7,requestingPlayerHead);
		requestedPlayerTradeGUI.setItem(14, readyButton);
		requestedPlayerTradeGUI.setItem(23, requestingPlayerNotReady);
		requestedPlayerTradeGUI.setItem(32, cancelButton);

		/**
		 * Open TradeGUI View for Requested Player
		 */
		requestedPlayer.openInventory(requestedPlayerTradeGUI);
		requestedPlayer.playSound(requestedPlayer.getLocation(), Sound.BLOCK_CHEST_OPEN, 10, 1);
		
		
	}
	@EventHandler
    public void onClose(InventoryCloseEvent event){
        
		requestingPlayerName.sendMessage(ChatColor.RED + "Trade cancelled");
		requestingPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
		requestedPlayerName.sendMessage(ChatColor.RED + "Trade cancelled");
		requestedPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
		
		/*
			Player player = (Player) event.getPlayer();
			if(player.getName().equals(requestingPlayerName.getName())) { 
				if(event.getView().getTitle().equals(menuTitle)) {
					player.sendMessage(ChatColor.RED + "You cancelled the trade.");
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestedPlayerName.sendMessage(ChatColor.RED + player.getName() + "cancelled the trade.");
					requestedPlayerName.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestedPlayerName.closeInventory();
				
					WauzPlayerTrade.playersOnTrading.remove(player.getName());
					WauzPlayerTrade.playersOnTrading.remove(requestedPlayerName.getName());
				}
			}
			if(player.getName().equals(requestedPlayerName.getName())){
				if(event.getInventory().equals(menuTitle)) {
					requestedPlayerName.sendMessage(ChatColor.RED + "You cancelled the trade.");
					requestedPlayerName.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestingPlayerName.sendMessage(ChatColor.RED + player.getName() + "cancelled the trade.");
					requestingPlayerName.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
					requestingPlayerName.closeInventory();
				
					WauzPlayerTrade.playersOnTrading.remove(player.getName());
					WauzPlayerTrade.playersOnTrading.remove(requestedPlayerName.getName());
				}
			}
       */
    }
	// This part is still incomplete.
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		if(!event.getView().getTitle().equals(menuTitle)) return; 
        
		boolean contains = IntStream.of(unavailableSlots).anyMatch(x -> x == event.getSlot());
		if(contains == true) {
			event.setCancelled(true);
		}
        /**
         * Requesting Player on click.
         */
		if(player.getName().equals(requestingPlayerName.getName())) {
			if(event.getSlot() == 12) {
				event.setCancelled(true);
				if(requestingPlayerStatus == true) {
					event.setCancelled(false);
					requestingPlayerTradeGUI.setItem(12, readyButton);
					requestingPlayerStatus = false;
					requestingPlayerTradeGUI.setItem(21, requestingPlayerNotReady);
					requestedPlayerTradeGUI.setItem(23, requestingPlayerNotReady);
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 1);
					requestedPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					
				}
			}else {
				if(requestedPlayerStatus == true) {
						
				}else {
					requestingPlayerStatus = true;
					requestingPlayerTradeGUI.setItem(12, unreadyButton);
					requestingPlayerTradeGUI.setItem(21, requestingPlayerReady);
					requestedPlayerTradeGUI.setItem(23, requestingPlayerReady);
					event.setCancelled(true);
				}

			}
			if(event.getSlot() == 30) {
				event.setCancelled(true);
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				requestedPlayerName.playSound(requestedPlayerName.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				requestingPlayerName.getInventory().clear();
				requestedPlayerName.getInventory().clear();
				requestingPlayerName.closeInventory();
				requestedPlayerName.closeInventory();
				requestingPlayerStatus = false;
				requestedPlayerStatus = false;

			}
		}
		/**
		 * Requested Player on click.
		 */
		if(player.getName().equals(requestedPlayerName.getName())) {
			if(event.getSlot() == 12) {
				event.setCancelled(true);
				if(requestedPlayerStatus == true) {
					event.setCancelled(false);
					player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 10, 1);
					requestingPlayerName.playSound(requestingPlayerName.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					requestedPlayerTradeGUI.setItem(12, readyButton);
					requestedPlayerStatus = false;
					requestedPlayerTradeGUI.setItem(21, requestingPlayerNotReady);
					requestingPlayerTradeGUI.setItem(23, requestingPlayerNotReady);
				}else {
					if(requestingPlayerStatus == true) {
						
					}else {
						requestedPlayerStatus = true;
						requestedPlayerTradeGUI.setItem(12, unreadyButton);
						requestedPlayerTradeGUI.setItem(21, requestedPlayerReady);
						requestingPlayerTradeGUI.setItem(23, requestingPlayerReady);
						event.setCancelled(true);
					}
				}

			}
			if(event.getSlot() == 30) {
				event.setCancelled(true);
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				requestingPlayerName.playSound(requestingPlayerName.getLocation(), Sound.BLOCK_CHEST_CLOSE, 10, 1);
				requestingPlayerName.getInventory().clear();
				requestedPlayerName.getInventory().clear();
				requestingPlayerName.closeInventory();
				requestedPlayerName.closeInventory();
				requestingPlayerStatus = false;
				requestedPlayerStatus = false;

			}
		}
		
	}
	
}
