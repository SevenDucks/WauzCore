package eu.wauz.wauzcore.players;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.WauzRegion;

/**
 * A player data to save session scoped player information.
 * 
 * @author Wauzmons
 */
public class WauzPlayerData {
	
	/**
	 * The session id of the player.
	 */
	private int wauzId;

	/**
	 * The temperature of the player.
	 */
	private Byte heat = 5;
	
	/**
	 * The temperature display randomizer of the player.
	 */
	private Byte heatRandomizer = 0;
	
	/**
	 * The heat resistance duration of the player.
	 */
	private short resistanceHeat = 0;
	
	/**
	 * The cold resistance duration of the player.
	 */
	private short resistanceCold = 0;
	
	/**
	 * The player vs player resistance duration of the player.
	 */
	private short resistancePvP = 0;
	
	/**
	 * The current health of the player.
	 */
	private Integer health = 20;
	
	/**
	 * The maximum health of the player.
	 */
	private Integer maxHealth = 20;
	
	/**
	 * The current mana of the player.
	 */
	private Integer mana = 0;
	
	/**
	 * The maximum mana of the player.
	 */
	private Integer maxMana = 0;
	
	/**
	 * The skill coodlown times by id.
	 */
	private Map<String, Long> skillCooldownMap = new HashMap<>();
	
	/**
	 * The action cooldown times by id.
	 */
	private Map<String, Long> actionCooldownMap = new HashMap<>();
	
	/**
	 * The current pet id.
	 */
	private String pet;
	
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
	 * The currently selected character race.
	 */
	private String selectedCharacterRace;
	
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
	 * The current note block song player.
	 */
	private SongPlayer songPlayer;
	
	/**
	 * The current region.
	 */
	private WauzRegion region;
	
	/**
	 * Creates a player data with given session id.
	 * 
	 * @param The session id of the player.
	 */
	public WauzPlayerData(int wauzId) {
		this.wauzId = wauzId;
	}
	
	/**
	 * @return The session id of the player.
	 */
	public int getWauzId() {
		return wauzId;
	}

	/**
	 * @return The temperature of the player.
	 */
	public Byte getHeat() {
		return heat;
	}

	/**
	 * @param heat The new temperature of the player.
	 */
	public void setHeat(Byte heat) {
		this.heat = heat;
	}

	/**
	 * @return The temperature display randomizer of the player.
	 */
	public Byte getHeatRandomizer() {
		return heatRandomizer;
	}

	/**
	 * @param heatRandomizer The new temperature display randomizer of the player.
	 */
	public void setHeatRandomizer(Byte heatRandomizer) {
		this.heatRandomizer = heatRandomizer;
	}

	/**
	 * @return The heat resistance duration of the player.
	 */
	public short getResistanceHeat() {
		return resistanceHeat;
	}

	/**
	 * @param resistanceHeat The new heat resistance duration of the player.
	 */
	public void setResistanceHeat(short resistanceHeat) {
		this.resistanceHeat = resistanceHeat;
	}

	/**
	 * @return The cold resistance duration of the player.
	 */
	public short getResistanceCold() {
		return resistanceCold;
	}
	
	/**
	 * @param resistanceCold The new cold resistance duration of the player.
	 */
	public void setResistanceCold(short resistanceCold) {
		this.resistanceCold = resistanceCold;
	}
	
	/**
	 * @return The player vs player resistance duration of the player.
	 */
	public short getResistancePvP() {
		return resistancePvP;
	}

	/**
	 * @param resistancePvP The new player vs player resistance duration of the player.
	 */
	public void setResistancePvP(short resistancePvP) {
		this.resistancePvP = resistancePvP;
	}

	/**
	 * Decreases temperature resistances by 1, down to 0.
	 */
	public void decreaseTemperatureResistance() {
		if(resistanceHeat != 0) {
			resistanceHeat--;
		}
		if(resistanceCold != 0) {
			resistanceCold--;
		}
	}
	
	/**
	 * Decreases player vs player resistance by 1, down to 0.
	 */
	public void decreasePvPProtection() {
		if(resistancePvP != 0) {
			resistancePvP--;
		}
	}

	/**
	 * @return The current health of the player.
	 */
	public Integer getHealth() {
		return health;
	}

	/**
	 * @param health The new current health of the player.
	 */
	public void setHealth(Integer health) {
		this.health = health;
	}

	/**
	 * @return The maximum health of the player.
	 */
	public Integer getMaxHealth() {
		return maxHealth;
	}

	/**
	 * @param maxHealth The new maximum health of the player.
	 */
	public void setMaxHealth(Integer maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * @return The current mana of the player.
	 */
	public Integer getMana() {
		return mana;
	}

	/**
	 * @param mana The new current mana of the player.
	 */
	public void setMana(Integer mana) {
		this.mana = mana;
	}

	/**
	 * @return The maximum mana of the player.
	 */
	public Integer getMaxMana() {
		return maxMana;
	}

	/**
	 * @param maxMana The new maximum mana of the player.
	 */
	public void setMaxMana(Integer maxMana) {
		this.maxMana = maxMana;
	}
	
	/**
	 * Checks if the cooldown timestamp for the given skill is smaller than the current time.
	 * 
	 * @param player The player that should receive the "not ready" message.
	 * @param skillId The id of the skill.
	 * 
	 * @return If the cooldown is ready.
	 */
	public boolean isSkillReady(Player player, String skillId) {
		Long cooldown = skillCooldownMap.get(skillId);
		if(cooldown == null || cooldown <= System.currentTimeMillis()) {
			return true;
		}
		long remaining = (cooldown - System.currentTimeMillis()) / 1000;
		remaining = remaining < 1 ? 1 : remaining;
		player.sendMessage(ChatColor.RED + "Skill not ready! " + remaining + " Seconds remain!");
		return false;
	}
	
	/**
	 * Resets the cooldown for the given skill.
	 * Only one second if the player has magic debug permissions.
	 * 
	 * @param player The player to check for permissions.
	 * @param skillId The id of the skill.
	 * 
	 * @see WauzDebugger#toggleMagicDebugMode(Player)
	 */
	public void updateSkillCooldown(Player player, String skillId) {
		Long cooldown = (long) (player.hasPermission(WauzPermission.DEBUG_MAGIC.toString()) ? 1 : WauzPlayerSkillExecutor.getSkill(skillId).getCooldownSeconds());
		skillCooldownMap.put(skillId, cooldown * 1000 + System.currentTimeMillis());
	}
	
	/**
	 * Checks if the cooldown timestamp for the given id is smaller than the current time.
	 * 
	 * @param actionId The id of the action.
	 * 
	 * @return If the cooldown is ready.
	 */
	public boolean isActionReady(String actionId) {
		Long cooldown = actionCooldownMap.get(actionId);
		return cooldown == null || cooldown <= System.currentTimeMillis();
	}
	
	/**
	 * Sets a cooldown in milliseconds from now, for the given id.
	 * 
	 * @param actionId The id of the action.
	 * @param cooldown The milliseconds of the cooldown.
	 */
	public void updateActionCooldown(String actionId, Long cooldown) {
		actionCooldownMap.put(actionId, cooldown + System.currentTimeMillis());
	}

	/**
	 * @return The current pet id.
	 */
	public String getPet() {
		return pet;
	}

	/**
	 * @param pet The new current pet id.
	 */
	public void setPet(String pet) {
		this.pet = pet;
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
	 * @return The currently selected character race.
	 */
	public String getSelectedCharacterRace() {
		return selectedCharacterRace;
	}

	/**
	 * @param selectedCharacterRace The new currently selected character race.
	 */
	public void setSelectedCharacterRace(String selectedCharacterRace) {
		this.selectedCharacterRace = selectedCharacterRace;
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
