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
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * An abstract helper class for checking the completion of quest phase requirements.
 * 
 * @author Wauzmons
 */
public abstract class QuestRequirementChecker {
	
	/**
	 * The player that is doing the quest.
	 */
	protected Player player;
	
	/**
	 * The quest to check requirements for.
	 */
	protected WauzQuest quest;
	
	/**
	 * The quest phase to check requirements for.
	 */
	protected int phase = 1;
	
	/**
	 * The requirements, how they would appear on an item.
	 */
	protected List<String> itemStackLores;
	
	/**
	 * The requirements, how they would appear in the sidebar.
	 */
	protected List<String> objectiveLores;
	
	/**
	 * The location of the next objective for the quest tracker.
	 */
	protected String trackerLocationString;
	
	/**
	 * The name of the next objective for the quest tracker.
	 */
	protected String trackerName;
	
	/**
	 * The item remover to collect quest items.
	 */
	protected InventoryItemRemover itemRemover;
	
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
	 * Creates a quest requirement checker, based on the type of the phase.
	 * 
	 * @param player The player that is doing the quest.
	 * @param quest The quest to check requirements for.
	 * @param phase The quest phase to check requirements for.
	 * 
	 * @return The created requirement checker.
	 */
	public static QuestRequirementChecker create(Player player, WauzQuest quest, int phase) {
		String type = quest.getRequirementType(phase).toLowerCase();
		switch (type) {
		case "kill":
			return new QuestRequirementCheckerKills(player, quest, phase);
		default:
			return new QuestRequirementCheckerItems(player, quest, phase);
		}
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
			questObjectives.add(ChatColor.GREEN + "  > " + ChatColor.WHITE + "Talk to " + quest.getQuestGiver() + " " + quest.getCoordinates());
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
			trackerName = quest.getDisplayName();
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
			Components.displayName(trackerItemMeta, ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getTrackerDestinationName(player));
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
	 * Initializes configuration values for the requirements, if any are needed.
	 */
	public abstract void initRequirements();
	
	/**
	 * Checks if the requirements have been fulfilled.
	 * 
	 * @return If the requirements were fulfilled.
	 */
	public abstract boolean checkRequirements();
	
	/**
	 * Hands in quest related items, if any were needed.
	 */
	public abstract void handInItems();
	
	/**
	 * Checks the requirements of the quest and creates corresponding tracker locations and lore.
	 * 
	 * @param onlyObjectives If only objectives and no progress should appear in the lore.
	 * 
	 * @return If the quest phase is completed.
	 */
	protected abstract boolean execute(boolean onlyObjectives);

}
