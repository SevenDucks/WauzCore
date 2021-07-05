package eu.wauz.wauzcore.mobs.citizens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.CitizenConfigurator;
import eu.wauz.wauzcore.data.players.PlayerRelationConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenCommand;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenInn;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenQuest;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenRest;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenShop;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenTalk;
import eu.wauz.wauzcore.events.WauzPlayerEventCitizenTravel;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.CitizenInteractionMenu;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzWaypoint;
import eu.wauz.wauzcore.system.economy.WauzShop;
import eu.wauz.wauzcore.system.quests.WauzQuest;
import eu.wauz.wauzcore.system.util.Components;

/**
 * The interaction options of a citizen.
 * 
 * @author Wauzmons
 *
 * @see WauzCitizen
 */
public class WauzCitizenInteractions {
	
	/**
	 * Item slots for the interactions in the citizen menu.
	 */
	private static List<Integer> interactionSlots = Arrays.asList(2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24);
	
	/**
	 * The name of the citizen, as shown in chat.
	 */
	private String displayName;
	
	/**
	 * The mode that should be selected from the hub on interaction.
	 */
	private String modeSelection;
	
	/**
	 * The list of item stacks, representing the interactions.
	 */
	private List<ItemStack> interactionItemStacks = new ArrayList<>();
	
	/**
	 * A map of interaction events, indexed by interaction name.
	 */
	private Map<String, WauzPlayerEvent> interactionEventMap = new HashMap<>();
	
	/**
	 * Constructs a set of interactions for the citizen with the given name.
	 * 
	 * @param citizenName The canonical name of the citizen.
	 * @param displayName The display name of the citizen.
	 * 
	 * @see CitizenConfigurator#getModeSelection(String)
	 * @see WauzCitizenInteractions#createInteractionItemStack(String, String)
	 */
	public WauzCitizenInteractions(String citizenName, String displayName) {
		this.displayName = displayName;
		modeSelection = CitizenConfigurator.getModeSelection(citizenName);
		if(StringUtils.isNotBlank(modeSelection)) {
			return;
		}
		Set<String> interactionKeys = CitizenConfigurator.getInteractionKeys(citizenName);
		for(String interactionKey : interactionKeys) {
			createInteractionItemStack(citizenName, interactionKey);
		}
	}
	
	/**
	 * Triggers the corresponding event, if the player clicked an interaction item stack.
	 * 
	 * @param player The player who chose the interaction.
	 * @param clickedItemStack The chosen interaction item stack.
	 * 
	 * @return If a successful interaction was made.
	 */
	public boolean checkForValidInteractions(Player player, ItemStack clickedItemStack) {
		if(clickedItemStack == null) {
			return false;
		}
		String interactionName = ItemUtils.getDisplayName(clickedItemStack);
		WauzPlayerEvent event = interactionEventMap.get(interactionName);
		if(event == null) {
			return false;
		}
		int relationLevel = RelationLevel.getRelationLevel(PlayerRelationConfigurator.getRelationProgress(player, displayName)).getRelationTier();
		int requiredLevel = Integer.parseInt(ItemUtils.getStringFromLore(clickedItemStack, "Required Relation Level", 3));
		if(relationLevel < requiredLevel) {
			player.sendMessage(ChatColor.RED + "Your relation with this citizen is not good enough to do that!");
			player.closeInventory();
			return false;
		}
		player.closeInventory();
		return event.execute(player);
	}
	
	/**
	 * Creates an inventory menu, containing all interaction item stacks.
	 * 
	 * @param interactionMenu The menu to create the base for.
	 * @param menuTitle The title of the menu.
	 * 
	 * @return The created inventory menu.
	 */
	public Inventory createInteractionMenuBase(CitizenInteractionMenu interactionMenu, String menuTitle) {
		Inventory menu = Components.inventory(interactionMenu, menuTitle, 27);
		
		ItemStack emptyItemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		MenuUtils.setItemDisplayName(emptyItemStack, " ");
		
		for(int index = 0; index < interactionSlots.size(); index++) {
			int indexSlot = interactionSlots.get(index);
			if(index < interactionItemStacks.size()) {
				menu.setItem(indexSlot, interactionItemStacks.get(index));
			}
			else {
				menu.setItem(indexSlot, emptyItemStack);
			}
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
		String interactionName = CitizenConfigurator.getInteractionName(citizenName, interactionKey);
		int level = CitizenConfigurator.getInteractionLevel(citizenName, interactionKey);
		
		switch (type) {
		case "talk":
			interactionItemStack = GenericIconHeads.getCitizenTalkItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.AQUA + "Talk: " + interactionName);
			List<String> messages = CitizenConfigurator.getInteractionMessages(citizenName, interactionKey);
			event = new WauzPlayerEventCitizenTalk(displayName, messages);
			break;
		case "shop":
			interactionItemStack = GenericIconHeads.getCitizenShopItem();
			WauzShop shop = WauzShop.getShop(interactionName);
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.GREEN + "Shop: " + shop.getShopDisplayName());
			event = new WauzPlayerEventCitizenShop(displayName, interactionName);
			break;
		case "quest":
			interactionItemStack = GenericIconHeads.getCitizenQuestItem();
			WauzQuest quest = WauzQuest.getQuest(interactionName);
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.GOLD + "Quest: " + quest.getDisplayName());
			event = new WauzPlayerEventCitizenQuest(displayName, interactionName);
			break;
		case "inn":
			interactionItemStack = GenericIconHeads.getCitizenInnItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.RED + "Inn: Set as New Home");
			Location location = CitizenConfigurator.getLocation(citizenName);
			event = new WauzPlayerEventCitizenInn(displayName, location);
			break;
		case "rest":
			interactionItemStack = GenericIconHeads.getCitizenRestItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.LIGHT_PURPLE + "Rest: Restore HP and Saturation");
			event = new WauzPlayerEventCitizenRest(displayName);
			break;
		case "travel":
			interactionItemStack = GenericIconHeads.getCitizenTravelItem();
			WauzWaypoint waypoint = WauzWaypoint.getWaypoint(interactionName);
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.DARK_PURPLE + "Travel: " + waypoint.getWaypointDisplayName());
			event = new WauzPlayerEventCitizenTravel(citizenName, waypoint);
			break;
		case "command":
			interactionItemStack = GenericIconHeads.getCitizenCommandItem();
			MenuUtils.setItemDisplayName(interactionItemStack, ChatColor.BLUE + "Action: " + interactionName);
			String command = CitizenConfigurator.getInteractionCommand(citizenName, interactionKey);
			event = new WauzPlayerEventCitizenCommand(displayName, command);
			break;
		default:
			WauzDebugger.log("Invalid Citizen Interaction Type: " + type);
			return;
		}
		MenuUtils.addItemLore(interactionItemStack, ChatColor.GRAY + "Required Relation Level:" + ChatColor.YELLOW + " " + level, false);
		interactionItemStacks.add(interactionItemStack);
		interactionEventMap.put(Components.displayName(interactionItemStack.getItemMeta()), event);
	}

	/**
	 * @return The mode that should be selected from the hub on interaction.
	 */
	public String getModeSelection() {
		return modeSelection;
	}
	
}
