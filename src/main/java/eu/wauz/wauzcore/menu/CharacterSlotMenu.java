package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventCharacterDelete;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * A character creation menu, that will let the player select their slot.
 * 
 * @author Wauzmons
 * 
 * @see CharacterWorldMenu
 * @see CharacterRaceClassMenu
 */
public class CharacterSlotMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Shows three choosable slots for MMO mode or one seasonal slot for Survival mode.
	 * 
	 * @param player The player that should view the inventory.
	 * @param wauzMode The wauz mode of the character slots.
	 * 
	 * @see CharacterSlotMenu#getCharacterSlot(Player, int, boolean)
	 * @see WauzDateUtils#getSurvivalSeasonInteger()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, WauzMode wauzMode) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CharacterSlotMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Character!");

		if(wauzMode.equals(WauzMode.MMORPG)) {
			for(int slotId = 1; slotId <= 3; slotId++)
				menu.setItem(slotId * 2, getCharacterSlot(player, slotId, true));
		}
		else if(wauzMode.equals(WauzMode.SURVIVAL)) {
			menu.setItem(4, getCharacterSlot(player, WauzDateUtils.getSurvivalSeasonInteger(), true));
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Generates an item stack, to show infos about a character slot.
	 * Most slots will be handled as MMO slots and will show race/class, world and level.
	 * Slots with an id over 20000 (survival seasons) will be Survival slots
	 * and will show the time left till the current season will end.
	 * Empty slots will show "Empty" and deletable slots a delete instruction.
	 * 
	 * @param player The player who owns the character.
	 * @param slotId The slot of the character.
	 * @param deletable If the player can clear this slot.
	 * 
	 * @return The character slot item stack.
	 * 
	 * @see PlayerConfigurator#getRaceString(org.bukkit.OfflinePlayer, int)
	 * @see PlayerConfigurator#getWorldString(org.bukkit.OfflinePlayer, int)
	 * @see PlayerConfigurator#getLevelString(org.bukkit.OfflinePlayer, int)
	 * @see PlayerConfigurator#getLastCharacterLogin(org.bukkit.OfflinePlayer, int)
	 * @see WauzDateUtils#getTimeTillNextSeason()
	 */
	private static ItemStack getCharacterSlot(Player player, int slotId, boolean deletable) {
		boolean characterExists = PlayerConfigurator.doesCharacterExist(player, slotId);
		ItemStack slotItemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta slotItemMeta = slotItemStack.getItemMeta();
		slotItemMeta.setDisplayName((characterExists ? ChatColor.GOLD : ChatColor.RED) + "Slot " + slotId);
		List<String> lores = new ArrayList<String>();
		
		if(characterExists) {
			lores.add(ChatColor.WHITE 
					+ PlayerConfigurator.getRaceString(player, slotId) + ", "
					+ PlayerConfigurator.getWorldString(player, slotId) + ", "
					+ PlayerConfigurator.getLevelString(player, slotId));
			lores.add("");
			lores.add(ChatColor.GRAY + "Last Played: " + ChatColor.GREEN
					+ PlayerConfigurator.getLastCharacterLogin(player, slotId) + " ago");
			if(slotId > 20000) {
				String timeTillNextSeason = WauzDateUtils.getTimeTillNextSeason();
				lores.add(ChatColor.GRAY + "End of Season: " + ChatColor.RED + timeTillNextSeason);
			}
			if(deletable) {
				lores.add(ChatColor.GRAY + "Right Click to Delete");
			}
		}
		else {
			lores.add(ChatColor.GRAY + "Empty");
		}
		
		slotItemMeta.setLore(lores);
		slotItemStack.setItemMeta(slotItemMeta);
		return slotItemStack;
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If the clicked item is a slot selection, it will be cached in the player data.
	 * Next the world selection will be shown, for new MMORPG characters.
	 * New survival characters will instantly be created.
	 * Existing characters will be logged in normally over the character manager.
	 * If the slot is deletable, it can be done so, with a right click.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerData#setSelectedCharacterSlot(String)
	 * @see CharacterWorldMenu#open(Player)
	 * @see CharacterManager#createCharacter(Player, WauzMode)
	 * @see CharacterManager#loginCharacter(Player, WauzMode)
	 * @see WauzPlayerEventCharacterDelete
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData == null) {
			return;
		}
		
		if(clicked == null || (!clicked.getType().equals(Material.TOTEM_OF_UNDYING) && !clicked.getType().equals(Material.BARRIER))) {
			return;
		}

		int slotId = Integer.parseInt(clicked.getItemMeta().getDisplayName().split(" ")[1]);
		playerData.setSelectedCharacterSlot("char" + slotId);		
		
		String clickedName = clicked.getItemMeta().getDisplayName();
		if(clickedName.contains("" + ChatColor.RED)) {
			if(slotId > 20000) {
				playerData.setSelectedCharacterWorld("Survival");
				playerData.setSelectedCharacterRace(WauzDateUtils.getSurvivalSeason());
				CharacterManager.createCharacter(player, WauzMode.SURVIVAL);
			}
			else {
				CharacterWorldMenu.open(player);
			}
		}
		else if(event.getClick().toString().contains("RIGHT")) {
			String characterSlot = playerData.getSelectedCharacterSlot();
			String characterSlotNumber = characterSlot.substring(4, 5);
			playerData.setWauzPlayerEventName("Delete Char " + characterSlotNumber);
			playerData.setWauzPlayerEvent(new WauzPlayerEventCharacterDelete());
			WauzDialog.open(player, getCharacterSlot(player, event.getSlot() / 2, false));
		}
		else {
			CharacterManager.loginCharacter(player, WauzMode.getMode(PlayerConfigurator.getCharacterWorldString(player)));
		}
	}

}
