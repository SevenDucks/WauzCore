package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.players.classes.Learnable;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;
import eu.wauz.wauzcore.skills.execution.Castable;
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
	private byte heat = 5;
	
	/**
	 * The temperature display randomizer of the player.
	 */
	private byte heatRandomizer = 0;
	
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
	private int health = 20;
	
	/**
	 * The maximum health of the player.
	 */
	private int maxHealth = 20;
	
	/**
	 * The current mana of the player.
	 */
	private int mana = 0;
	
	/**
	 * The maximum mana of the player.
	 */
	private int maxMana = 0;
	
	/**
	 * The current rage of the player.
	 */
	private int rage = 0;
	
	/**
	 * The maximum rage of the player.
	 */
	private int maxRage = 0;
	
	/**
	 * The number of the currently shown action bar.
	 */
	private int actionBar = 0;
	
	/**
	 * The list of selected castables.
	 */
	private List<Castable> selectedCastables = new ArrayList<>();
	
	/**
	 * The list of unlocked castables.
	 */
	private List<Castable> unlockedCastables = new ArrayList<>();
	
	/**
	 * A map of the unlocked castables, indexed by castable key.
	 */
	private Map<String, Castable> castableMap = new HashMap<>();
	
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
	public byte getHeat() {
		return heat;
	}

	/**
	 * @param heat The new temperature of the player.
	 */
	public void setHeat(byte heat) {
		this.heat = heat;
	}

	/**
	 * @return The temperature display randomizer of the player.
	 */
	public byte getHeatRandomizer() {
		return heatRandomizer;
	}

	/**
	 * @param heatRandomizer The new temperature display randomizer of the player.
	 */
	public void setHeatRandomizer(byte heatRandomizer) {
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
	public int getHealth() {
		return health;
	}

	/**
	 * @param health The new current health of the player.
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return The maximum health of the player.
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * @param maxHealth The new maximum health of the player.
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * @return The current mana of the player.
	 */
	public int getMana() {
		return mana;
	}

	/**
	 * @param mana The new current mana of the player.
	 */
	public void setMana(int mana) {
		this.mana = mana;
	}

	/**
	 * @return The maximum mana of the player.
	 */
	public int getMaxMana() {
		return maxMana;
	}

	/**
	 * @param maxMana The new maximum mana of the player.
	 */
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
	
	/**
	 * @return The current rage of the player.
	 */
	public int getRage() {
		return rage;
	}

	/**
	 * @param rage The new current rage of the player.
	 */
	public void setRage(int rage) {
		this.rage = rage;
	}

	/**
	 * @return The maximum rage of the player.
	 */
	public int getMaxRage() {
		return maxRage;
	}

	/**
	 * @param maxRage The new maximum rage of the player.
	 */
	public void setMaxRage(int maxRage) {
		this.maxRage = maxRage;
	}
	
	/**
	 * @return The number of the currently shown action bar.
	 */
	public int getActionBar() {
		return actionBar;
	}

	/**
	 * @param actionBar The new number of the currently shown action bar.
	 */
	public void setActionBar(int actionBar) {
		this.actionBar = actionBar;
	}
	
	/**
	 * @return The list of selected castables.
	 */
	public List<Castable> getSelectedCastables() {
		return selectedCastables;
	}

	/**
	 * @return The list of unlocked castables.
	 */
	public List<Castable> getUnlockedCastables() {
		return unlockedCastables;
	}
	
	/**
	 * Gets an unlocked castable of the player.
	 * 
	 * @param castableKey The key of the castable.
	 * 
	 * @return The requested castable or null.
	 */
	public Castable getCastable(String castableKey) {
		return castableMap.get(castableKey);
	}

	/**
	 * Refreshes the list of unlocked castables. Also refreshes selected castables.
	 * 
	 * @param player The player to get the castable list from.
	 */
	public void refreshUnlockedCastables(Player player) {
		unlockedCastables.clear();
		castableMap.clear();
		List<WauzPlayerSubclass> subclasses = WauzPlayerClassPool.getClass(player).getSubclasses();
		for(int index = 0; index < subclasses.size(); index++) {
			WauzPlayerSubclass subclass = subclasses.get(index);
			int masteryLevel = PlayerSkillConfigurator.getMasteryStatpoints(player, index + 1);
			for(Learnable learnable : subclass.getLearned(masteryLevel)) {
				Castable castable = new Castable(subclass.getSubclassItemStack(), learnable.getSkill());
				unlockedCastables.add(castable);
				castableMap.put("Skill :: " + learnable.getSkill().getSkillId(), castable);
			}
		}
		refreshSelectedCastables(player);
	}
	
	/**
	 * Refreshes the list of selected castables.
	 * 
	 * @param player The player to get the castable list from.
	 */
	public void refreshSelectedCastables(Player player) {
		selectedCastables.clear();
		for(int slot = 1; slot <= 8; slot++) {
			String castableKey = PlayerSkillConfigurator.getQuickSlotSkill(player, slot);
			selectedCastables.add(castableMap.get(castableKey));
		}
	}
	
	/**
	 * Gets the remaining cooldown milliseconds for the given skill.
	 * 
	 * @param skillId The id of the skill.
	 * 
	 * @return The remaining millis.
	 */
	public long getRemainingSkillCooldown(String skillId) {
		Long cooldown = skillCooldownMap.get(skillId);
		return cooldown == null ? 0 : cooldown - System.currentTimeMillis();
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
