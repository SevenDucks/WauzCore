package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.instances.InstanceManager;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.kyori.adventure.text.Component;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * A character creation menu, that will let the player select their slot.
 * 
 * @author Wauzmons
 * 
 * @see CharacterWorldMenu
 * @see CharacterClassMenu
 */
public class CharacterSlotMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "slots";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows three choosable chearacter slot for the given mode.
	 * 
	 * @param player The player that should view the inventory.
	 * @param wauzMode The wauz mode of the character slots.
	 * 
	 * @see CharacterSlotMenu#getCharacterSlot(Player, String, boolean)
	 * @see WauzDateUtils#getSurvivalSeasonInteger()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, WauzMode wauzMode) {
		if(wauzMode.equals(WauzMode.MMORPG) || wauzMode.equals(WauzMode.ARCADE)) {
			if(!WauzRank.getRank(player).isStaff()) {
				player.sendMessage(ChatColor.RED + "This gamemode isn't public yet!");
				return;
			}
		}
		WauzInventoryHolder holder = new WauzInventoryHolder(new CharacterSlotMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, Component.text(ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Slot!"));

		if(wauzMode.equals(WauzMode.MMORPG)) {
			for(int slotId = 1; slotId <= 3; slotId++) {
				menu.setItem(slotId * 2, getCharacterSlot(player, "MMORPG-" + slotId, true));
			}
		}
		else if(wauzMode.equals(WauzMode.SURVIVAL)) {
			int season = WauzDateUtils.getSurvivalSeasonInteger();
			menu.setItem(3, getCharacterSlot(player, "OneBlock-Survival-" + season, true));
			menu.setItem(5, getCharacterSlot(player, "Classic-Survival-" + season, true));
		}
		else if(wauzMode.equals(WauzMode.ARCADE)) {
			ItemStack slotItemStack = new ItemStack(Material.CLOCK);
			MenuUtils.setItemDisplayName(slotItemStack, ChatColor.GREEN + "Connect to Main Lobby");
			menu.setItem(4, slotItemStack);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Generates an item stack, to show infos about a character slot.
	 * The slots will show class, world and level of the character.
	 * Survival slots will also show the time left till the current season will end.
	 * Empty slots will show "Empty" and deletable slots a delete instruction.
	 * Outdated slots will be deleted automatically.
	 * 
	 * @param player The player who owns the character.
	 * @param slotId The slot of the character.
	 * @param deletable If the player can clear this slot.
	 * 
	 * @return The character slot item stack.
	 * 
	 * @see PlayerConfigurator#getCharecterSchemaVersion(org.bukkit.OfflinePlayer, String)
	 * @see CharacterManager#deleteCharacter(Player, String)
	 * @see PlayerConfigurator#getClassString(org.bukkit.OfflinePlayer, String)
	 * @see PlayerConfigurator#getWorldString(org.bukkit.OfflinePlayer, String)
	 * @see PlayerConfigurator#getLevelString(org.bukkit.OfflinePlayer, String)
	 * @see PlayerConfigurator#getLastCharacterLogin(org.bukkit.OfflinePlayer, String)
	 * @see WauzDateUtils#getTimeTillNextSeason()
	 */
	private static ItemStack getCharacterSlot(Player player, String slotId, boolean deletable) {
		boolean characterExists = PlayerConfigurator.doesCharacterExist(player, slotId);
		if(characterExists && PlayerConfigurator.getCharecterSchemaVersion(player, slotId) < CharacterManager.SCHEMA_VERSION) {
			WauzDebugger.log(player, "Outdated Character! Deleting: " + slotId);
			CharacterManager.deleteCharacter(player, "char" + slotId);
			characterExists = false;
		}
		
		ItemStack slotItemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta slotItemMeta = slotItemStack.getItemMeta();
		slotItemMeta.displayName(Component.text((characterExists ? ChatColor.GOLD : ChatColor.RED) + "Slot " + slotId));
		List<String> lores = new ArrayList<String>();
		
		if(characterExists) {
			lores.add(ChatColor.WHITE 
					+ PlayerConfigurator.getClassString(player, slotId) + ", "
					+ PlayerConfigurator.getWorldString(player, slotId) + ", "
					+ PlayerConfigurator.getLevelString(player, slotId));
			lores.add("");
			lores.add(ChatColor.GRAY + "Last Played: " + ChatColor.GREEN
					+ PlayerConfigurator.getLastCharacterLogin(player, slotId) + " ago");
			if(slotId.contains("Survival")) {
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
	 * @see InstanceManager#enterArcade(Player)
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
		
		if(playerData == null || clicked == null) {
			return;
		}
		
		Material type = clicked.getType();
		if(type.equals(Material.CLOCK)) {
			InstanceManager.enterArcade(player);
			return;
		}
		else if(!type.equals(Material.TOTEM_OF_UNDYING) && !clicked.getType().equals(Material.BARRIER)) {
			return;
		}
		String slotId = clicked.getItemMeta().getDisplayName().split(" ")[1];
		playerData.getSelections().setSelectedCharacterSlot("char" + slotId);		
		
		String clickedName = clicked.getItemMeta().getDisplayName();
		if(clickedName.contains("" + ChatColor.RED)) {
			if(slotId.contains("Survival")) {
				playerData.getSelections().setSelectedCharacterWorld(slotId.startsWith("OneBlock") ? "SurvivalOneBlock" : "Survival");
				playerData.getSelections().setSelectedCharacterClass(WauzDateUtils.getSurvivalSeason());
				CharacterManager.createCharacter(player, WauzMode.SURVIVAL);
			}
			else {
				playerData.getSelections().setSelectedCharacterWorld("MMORPG");
				CharacterClassMenu.open(player);
			}
		}
		else if(event.getClick().toString().contains("RIGHT")) {
			playerData.getSelections().setWauzPlayerEventName("Delete Slot");
			playerData.getSelections().setWauzPlayerEvent(new WauzPlayerEventCharacterDelete());
			WauzDialog.open(player, getCharacterSlot(player, slotId, false));
		}
		else {
			CharacterManager.loginCharacter(player, WauzMode.getMode(PlayerConfigurator.getCharacterWorldString(player)));
		}
	}

}
