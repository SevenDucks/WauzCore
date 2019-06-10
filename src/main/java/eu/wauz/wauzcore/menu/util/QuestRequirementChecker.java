package eu.wauz.wauzcore.menu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.items.InventoryItemRemover;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.system.WauzQuest;
import net.md_5.bungee.api.ChatColor;

public class QuestRequirementChecker {
	
	private Player player;
	
	private WauzQuest quest;
	
	private int phase = 1;
	
	private List<String> itemStackLores;
	
	private List<String> objectiveLores;
	
	private String trackerLocationString;
	
	private String trackerName;
	
	private InventoryItemRemover itemRemover;
	
	public QuestRequirementChecker(Player player, WauzQuest quest, int phase) {
		this.player = player;
		this.quest = quest;
		this.phase = phase;
	}
	
	public List<String> getItemStackLores() {
		execute(false);
		return itemStackLores;
	}
	
	public List<String> getItemStackLoresUnaccepted() {
		execute(true);
		return itemStackLores;
	}
	
	public List<String> getObjectiveLores(String questMargin, ChatColor questColor) {
		List<String> questObjectives = new ArrayList<>();
		questObjectives.add(questMargin);
		questObjectives.add(questColor + "• " + ChatColor.WHITE + quest.getDisplayName());
		
		if(!execute(false))
			questObjectives.addAll(objectiveLores);
		else if(!PlayerConfigurator.getHideCompletedQuestsForCharacter(player))
			questObjectives.add(ChatColor.GREEN + "  > " + ChatColor.WHITE + "Talk to " + quest.getQuestName() + " " + quest.getCoordinates());
		else
			return new ArrayList<>();
		
		return questObjectives;
	}
	
	public void trackQuestObjective() {
		if(phase > 0)
			execute(false);
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
			
			PlayerConfigurator.setDungeonItemTrackerDestination(player, location, trackerName);
			
			ItemStack tracker = new ItemStack(Material.COMPASS);
			ItemMeta im = tracker.getItemMeta();
			im.setDisplayName(ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getDungeonItemTrackerDestinationName(player));
			im.setUnbreakable(true);
			tracker.setItemMeta(im);
			player.getInventory().setItem(7, tracker);
			player.closeInventory();
			
			player.setCompassTarget(location);
			player.sendMessage(ChatColor.GREEN
					+ "You are now tracking " + trackerName + " (" + trackerLocationString
					+ ") for the quest [" + quest.getDisplayName() + "]");
		}
	}
	
	public boolean tryToHandInQuest() {
		boolean success = execute(false);
		if(success)
			itemRemover.execute();
		
		return success;
	}
	
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
			
			if(onlyObjectives)
				continue;
				
				
				
			int requiredAmount = quest.getRequirementNeededItemAmount(phase, requirement);
			int collectedAmount = 0;
			ChatColor finished = ChatColor.RED;
			
			itemRemover.addItemNameToRemove(itemName, requiredAmount);
			for(ItemStack itemStack : player.getInventory().getContents())
				if((itemStack != null) && ItemUtils.isQuestItem(itemStack) && ItemUtils.isSpecificItem(itemStack, itemName))
					collectedAmount += itemStack.getAmount();
			
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
			if(requirement > 1)
				itemStackLores.add("");
		}
		
		boolean success = fulfilledAmount == requirementAmount;
		if(success) {
			trackerLocationString = quest.getCoordinates();
			trackerName = quest.getQuestName();
		}
		
		return success;
	}

}
