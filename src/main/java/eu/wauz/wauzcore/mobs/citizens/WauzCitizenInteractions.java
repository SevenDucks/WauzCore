package eu.wauz.wauzcore.mobs.citizens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.CitizenConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenCommand;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenInn;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenQuest;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenShop;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenTalk;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * The interaction options of a citizen.
 * 
 * @author Wauzmons
 *
 * @see WauzCitizen
 */
public class WauzCitizenInteractions {
	
	/**
	 * A map of interaction events, indexed by the triggering item stacks.
	 */
	public Map<ItemStack, WauzPlayerEvent> interactionEventMap = new HashMap<>();
	
	/**
	 * Constructs a set of interactions for the citizen with the given name.
	 * 
	 * @param citizenName The canonical name of the citizen.
	 * 
	 * @see WauzCitizenInteractions#createInteractionItemStack(String, String)
	 */
	public WauzCitizenInteractions(String citizenName) {
		Set<String> interactionKeys = CitizenConfigurator.getInteractionKeys(citizenName);
		for(String interactionKey : interactionKeys) {
			createInteractionItemStack(citizenName, interactionKey);
		}
	}
	
	/**
	 * Triggers the corresponding event, if the player clicked an interaction item stack.
	 * 
	 * @param player The player who chose the interaction.
	 * @param interactionItemStack The chosen interaction item stack.
	 * 
	 * @return If a successful interaction was made.
	 */
	public boolean checkForValidInteractions(Player player, ItemStack interactionItemStack) {
		WauzPlayerEvent event = interactionEventMap.get(interactionItemStack);
		if(event != null) {
			return event.execute(player);
		}
		return false;
	}
	
	/**
	 * Creates an inventory menu, containing all interaction item stacks.
	 * 
	 * @param holder The holder of the inventory menu.
	 * @param title The title of the menu.
	 * 
	 * @return The created inventory menu.
	 */
	public Inventory createInteractionMenuBase(InventoryHolder holder, String title) {
		int size = (int) Math.ceil(interactionEventMap.size() / 5);
		Inventory menu = Bukkit.createInventory(holder, size, title);
		int row = 0;
		int column = 0;
		for(ItemStack interactionItemStack : interactionEventMap.keySet()) {
			if(column >= 5) {
				row++;
				column = 0;
			}
			int index = row + 2 + (column * 9);
			menu.setItem(index, interactionItemStack);
			column++;
		}
		return menu;
	}
	
	/**
	 * Creates an interaction item stack from the given values.
	 * Also creates an event entry in the interaction map.
	 * 
	 * @param citizenName The name of the citizen.
	 * @param interactionKey The key of the citizen interaction.
	 */
	private void createInteractionItemStack(String citizenName, String interactionKey) {
		ItemStack interactionItemStack;
		WauzPlayerEvent event;
		String type = CitizenConfigurator.getInteractionType(citizenName, interactionKey);
		String displayName = CitizenConfigurator.getDisplayName(citizenName);
		String interactionName = CitizenConfigurator.getInteractionName(citizenName, interactionKey);
		int level = CitizenConfigurator.getInteractionLevel(citizenName, interactionKey);
		
		switch (type) {
		case "talk":
			interactionItemStack = HeadUtils.getCitizenTalkItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.AQUA + "Talk: " + interactionName);
			List<String> messages = CitizenConfigurator.getInteractionMessages(citizenName, interactionKey);
			event = new WauzPlayerEventCitizenTalk(citizenName, displayName, messages);
			break;
		case "shop":
			interactionItemStack = HeadUtils.getCitizenShopItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.GREEN + "Shop: " + interactionName);
			event = new WauzPlayerEventCitizenShop(citizenName, interactionName);
			break;
		case "quest":
			interactionItemStack = HeadUtils.getCitizenQuestItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.GOLD + "Quest: " + interactionName);
			event = new WauzPlayerEventCitizenQuest(citizenName, interactionName);
			break;
		case "inn":
			interactionItemStack = HeadUtils.getCitizenInnItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.RED + "Inn: Set as New Home");
			Location location = CitizenConfigurator.getLocation(citizenName);
			event = new WauzPlayerEventCitizenInn(citizenName, location);
			break;
		case "command":
			interactionItemStack = HeadUtils.getCitizenCommandItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.BLUE + "Action: " + interactionName);
			String command = CitizenConfigurator.getInteractionCommand(citizenName, interactionKey);
			event = new WauzPlayerEventCitizenCommand(citizenName, command);
			break;
		default:
			WauzDebugger.log("Invalid Citizen Interaction Type: " + type);
			return;
		}
		MenuUtils.addItemLore(interactionItemStack, ChatColor.GRAY + "Required Relation Level: " + level, false);
		interactionEventMap.put(interactionItemStack, event);
	}
	
}
