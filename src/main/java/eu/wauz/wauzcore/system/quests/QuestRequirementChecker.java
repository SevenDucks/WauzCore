package eu.wauz.wauzcore.system.quests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.InventoryItemRemover;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A helper class for checking the completion of quest phase requirements.
 * 
 * @author Wauzmons
 */
public class QuestRequirementChecker {
	
	/**
	 * The player that is doing the quest.
	 */
	private Player player;
	
	/**
	 * The quest to check requirements for.
	 */
	private WauzQuest quest;
	
	/**
	 * The quest phase to check requirements for.
	 */
	private int phase = 1;
	
	/**
	 * The requirements, how they would appear on an item.
	 */
	private List<String> itemStackLores;
	
	/**
	 * The requirements, how they would appear in the sidebar.
	 */
	private List<String> objectiveLores;
	
	/**
	 * The location of the next objective for the quest tracker.
	 */
	private String trackerLocationString;
	
	/**
	 * The name of the next objective for the quest tracker.
	 */
	private String trackerName;
	
	/**
	 * The item remover to collect quest items.
	 */
	private InventoryItemRemover itemRemover;
	
	/**
	 * Initializes a new quest requirement checker.
	 * 
	 * @param player The player that is doing the quest.
	 * @param quest The quest to check requirements for.
	 * @param phase The quest phase to check requirements for.
	 */
	public QuestRequirementChecker(Player player, WauzQuest quest, int phase) {
		this.player = player;
		this.quest = quest;
		this.phase = phase;
	}
	
	/**
	 * @return The requirements, how they would appear on an item.
	 */
	public List<String> getItemStackLores() {
		execute(false);
		return itemStackLores;
	}
	
	/**
	 * @return The requirements, without current progress, how they would appear on an item.
	 */
	public List<String> getItemStackLoresUnaccepted() {
		execute(true);
		return itemStackLores;
	}
	
	/**
	 * Generates a list of a quest and its objectives, to show in the sidebar.
	 * 
	 * @param questMargin The empty space above the title.
	 * @param questColor The color of the quest type.
	 * 
	 * @return The list of quest objectives.
	 */
	public List<String> getObjectiveLores(String questMargin, ChatColor questColor) {
		List<String> questObjectives = new ArrayList<>();
		questObjectives.add(questMargin);
		questObjectives.add(questColor + UnicodeUtils.ICON_BULLET + " " + ChatColor.WHITE + quest.getDisplayName());
		
		if(!execute(false)) {
			questObjectives.addAll(objectiveLores);
		}
		else if(!PlayerConfigurator.getHideCompletedQuestsForCharacter(player)) {
			questObjectives.add(ChatColor.GREEN + "  > " + ChatColor.WHITE + "Talk to " + quest.getQuestName() + " " + quest.getCoordinates());
		}
		else {
			return new ArrayList<>();
		}
		return questObjectives;
	}
	
	/**
	 * Tracks the current quest objective, or the questgiver, if the quest hasn't started yet.
	 * 
	 * @see PlayerConfigurator#setTrackerDestination(Player, Location, String)
	 */
	public void trackQuestObjective() {
		if(phase > 0) {
			execute(false);
		}
		else {
			trackerLocationString = quest.getCoordinates();
			trackerName = quest.getQuestName();
		}
		
		if(StringUtils.isNotBlank(trackerName) && StringUtils.isNotBlank(trackerLocationString)) {
			String[] trackerCoordinateStrings = trackerLocationString.split(" ");
			Location location = PlayerConfigurator.getCharacterSpawn(player);
			location.setX(Float.parseFloat(trackerCoordinateStrings[0]));
			location.setY(Float.parseFloat(trackerCoordinateStrings[1]));
			location.setZ(Float.parseFloat(trackerCoordinateStrings[2]));
			
			PlayerConfigurator.setTrackerDestination(player, location, trackerName);
			
			ItemStack trackerItemStack = new ItemStack(Material.COMPASS);
			ItemMeta trackerItemMeta = trackerItemStack.getItemMeta();
			trackerItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getTrackerDestinationName(player));
			trackerItemMeta.setUnbreakable(true);
			trackerItemStack.setItemMeta(trackerItemMeta);
			player.getInventory().setItem(7, trackerItemStack);
			player.closeInventory();
			
			player.setCompassTarget(location);
			player.sendMessage(ChatColor.GREEN
					+ "You are now tracking " + trackerName + " (" + trackerLocationString
					+ ") for the quest [" + quest.getDisplayName() + "]");
		}
	}
	
	/**
	 * Checks the requirements and hands in the quest items, if all requirements were met.
	 * 
	 * @return If the quest phase is completed.
	 */
	public boolean tryToHandInQuest() {
		boolean success = execute(false);
		if(success) {
			itemRemover.execute();
		}
		return success;
	}
	
	/**
	 * Checks the requirements of the quest and creates corresponding tracker locations and lore.
	 * 
	 * @param onlyObjectives If only objectives and no progrss should appear in the lore.
	 * 
	 * @return If the quest phase is completed.
	 */
	private boolean execute(boolean onlyObjectives) {
		int requirementAmount = quest.getRequirementAmount(phase);
		int fulfilledAmount = 0;
		
		itemStackLores = new ArrayList<>();
		objectiveLores = new ArrayList<>();
		trackerLocationString = null;
		trackerName = null;
		
		itemRemover = new InventoryItemRemover(player.getInventory());
		
		for(int requirement = requirementAmount; requirement > 0; requirement--) {		
			String itemName = quest.getRequirementNeededItemName(phase, requirement);
			String itemCoordinates = quest.getRequirementNeededItemCoordinates(phase, requirement);
			itemStackLores.add(ChatColor.GRAY + "Collect " + itemName + " around " + itemCoordinates);
			
			if(onlyObjectives) {
				continue;
			}
				
			int requiredAmount = quest.getRequirementNeededItemAmount(phase, requirement);
			int collectedAmount = 0;
			ChatColor finished = ChatColor.RED;
			
			itemRemover.addItemNameToRemove(itemName, requiredAmount);
			for(ItemStack itemStack : player.getInventory().getContents()) {
				if((itemStack != null) && ItemUtils.isQuestItem(itemStack) && ItemUtils.isSpecificItem(itemStack, itemName)) {
					collectedAmount += itemStack.getAmount();
				}
			}
			
			if(collectedAmount >= requiredAmount) {
				fulfilledAmount++;
				finished = ChatColor.GREEN;
				collectedAmount = requiredAmount;
			}
			else if(trackerLocationString == null) {
				trackerLocationString = itemCoordinates;
				trackerName = itemName;
			}
			
			itemStackLores.add(finished + "Amount: " + collectedAmount + " / " + requiredAmount);
			objectiveLores.add(finished + "  > " + ChatColor.WHITE + collectedAmount + " / " + requiredAmount + " " + itemName);
			if(requirement > 1) {
				itemStackLores.add("");
			}
		}
		
		boolean success = fulfilledAmount == requirementAmount;
		if(success) {
			trackerLocationString = quest.getCoordinates();
			trackerName = quest.getQuestName();
		}
		
		return success;
	}

}
