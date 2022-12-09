package eu.wauz.wauzcore.players;

import org.apache.commons.lang3.StringUtils;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.system.WauzRegion;

/**
 * A section of the player data for storing selection data.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData
 */
public class WauzPlayerDataSectionSelections {
	
	/**
	 * The player data the section belongs to.
	 */
	private final WauzPlayerData playerData;
	
	/**
	 * The current pet id.
	 */
	private String selectedPetId;
	
	/**
	 * The currently selected pet slot.
	 */
	private String selectedPetSlot;
	
	/**
	 * The currently selected character slot.
	 */
	private String selectedCharacterSlot;
	
	/**
	 * The currently selected character world.
	 */
	private String selectedCharacterWorld;
	
	/**
	 * The currently selected character class.
	 */
	private String selectedCharacterClass;
	
	/**
	 * The uuid of this players group.
	 */
	private String groupUuidString;
	
	/**
	 * The title of the active player event.
	 */
	private String wauzPlayerEventName;
	
	/**
	 * The active player event.
	 */
	private WauzPlayerEvent wauzPlayerEvent;
	
	/**
	 * The amount of conversations the player is engaged in.
	 */
	private int activeConversations = 0;
	
	/**
	 * The current note block song player.
	 */
	private SongPlayer songPlayer;
	
	/**
	 * The current region.
	 */
	private WauzRegion region;
	
	/**
	 * Constructs a new instance of the section.
	 * 
	 * @param playerData The player data the section belongs to.
	 */
	public WauzPlayerDataSectionSelections(WauzPlayerData playerData) {
		this.playerData = playerData;
	}
	
	/**
	 * @return The player data the section belongs to.
	 */
	public WauzPlayerData getPlayerData() {
		return playerData;
	}
	
	/**
	 * @return The current pet id.
	 */
	public String getSelectedPetId() {
		return selectedPetId;
	}

	/**
	 * @param selectedPetId The new current pet id.
	 */
	public void setSelectedPetId(String selectedPetId) {
		this.selectedPetId = selectedPetId;
	}

	/**
	 * @return The currently selected pet slot.
	 */
	public String getSelectedPetSlot() {
		return selectedPetSlot;
	}

	/**
	 * @param selectedPetSlot The new currently selected pet slot.
	 */
	public void setSelectedPetSlot(String selectedPetSlot) {
		this.selectedPetSlot = selectedPetSlot;
	}

	/**
	 * @return The currently selected character slot.
	 */
	public String getSelectedCharacterSlot() {
		return selectedCharacterSlot;
	}

	/**
	 * @param selectedCharacterSlot The new currently selected character slot.
	 */
	public void setSelectedCharacterSlot(String selectedCharacterSlot) {
		this.selectedCharacterSlot = selectedCharacterSlot;
	}
	
	/**
	 * @return If a character slot is selected.
	 */
	public boolean isCharacterSelected() {
		return StringUtils.isNotBlank(selectedCharacterSlot);
	}

	/**
	 * @return The currently selected character world.
	 */
	public String getSelectedCharacterWorld() {
		return selectedCharacterWorld;
	}

	/**
	 * @param selectedCharacterWorld The new currently selected character world.
	 */
	public void setSelectedCharacterWorld(String selectedCharacterWorld) {
		this.selectedCharacterWorld = selectedCharacterWorld;
	}

	/**
	 * @return The currently selected character class.
	 */
	public String getSelectedCharacterClass() {
		return selectedCharacterClass;
	}

	/**
	 * @param selectedCharacterClass The new currently selected character class.
	 */
	public void setSelectedCharacterClass(String selectedCharacterClass) {
		this.selectedCharacterClass = selectedCharacterClass;
	}

	/**
	 * @return The uuid of this players group.
	 */
	public String getGroupUuidString() {
		return groupUuidString;
	}

	/**
	 * @param groupUuidString The new uuid of this players group.
	 */
	public void setGroupUuidString(String groupUuidString) {
		this.groupUuidString = groupUuidString;
	}
	
	/**
	 * @return If the player is in a group.
	 */
	public boolean isInGroup() {
		return StringUtils.isNotBlank(groupUuidString);
	}

	/**
	 * @return The title of the active player event.
	 */
	public String getWauzPlayerEventName() {
		return wauzPlayerEventName;
	}

	/**
	 * @param wauzPlayerEventName The new title of the active player event.
	 */
	public void setWauzPlayerEventName(String wauzPlayerEventName) {
		this.wauzPlayerEventName = wauzPlayerEventName;
	}

	/**
	 * @return The active player event.
	 */
	public WauzPlayerEvent getWauzPlayerEvent() {
		return wauzPlayerEvent;
	}

	/**
	 * @param wauzPlayerEvent The new active player event.
	 */
	public void setWauzPlayerEvent(WauzPlayerEvent wauzPlayerEvent) {
		this.wauzPlayerEvent = wauzPlayerEvent;
	}

	/**
	 * @return The amount of conversations the player is engaged in.
	 */
	public int getActiveConversations() {
		return activeConversations;
	}
	
	/**
	 * Increases the amount of conversations the player is engaged in by one.
	 */
	public void increaseActiveConversations() {
		activeConversations++;
	}
	
	/**
	 * Decreases the amount of conversations the player is engaged in by one.
	 */
	public void decreaseActiveConversations() {
		activeConversations--;
	}

	/**
	 * @return The current note block song player.
	 */
	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	/**
	 * @param songPlayer The new current note block song player.
	 */
	public void setSongPlayer(SongPlayer songPlayer) {
		this.songPlayer = songPlayer;
	}

	/**
	 * @return The current region.
	 */
	public WauzRegion getRegion() {
		return region;
	}

	/**
	 * @param region The new current region.
	 */
	public void setRegion(WauzRegion region) {
		this.region = region;
	}

}
