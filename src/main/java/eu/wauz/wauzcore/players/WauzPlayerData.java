package eu.wauz.wauzcore.players;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

import eu.wauz.wauzcore.events.WauzPlayerEvent;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.WauzRegion;

public class WauzPlayerData {
	
	private int wauzId;

	private Byte heat = 5;
	
	private Byte heatRandomizer = 0;
	
	private short resistanceHeat = 0;
	
	private short resistanceCold = 0;
	
	private short resistancePvsP = 0;
	
	private Integer health = 20;
	
	private Integer maxHealth = 20;
	
	private Integer mana = 0;
	
	private Integer maxMana = 0;
	
	private Map<String, Long> skillCooldownMap = new HashMap<>();
	
	private Map<String, Long> actionCooldownMap = new HashMap<>();
	
	private String pet;
	
	private String selectedPetSlot;
	
	private String selectedCharacterSlot;
	
	private String selectedCharacterWorld;
	
	private String selectedCharacterRace;
	
	private String groupUuidString;
	
	private String wauzPlayerEventName;
	
	private SongPlayer songPlayer;
	
	private WauzRegion region;
	
	private WauzPlayerEvent wauzPlayerEvent;
	
	public WauzPlayerData(int wauzId) {
		this.wauzId = wauzId;
	}
	
	public int getWauzId() {
		return wauzId;
	}

	public Byte getHeat() {
		return heat;
	}

	public void setHeat(Byte heat) {
		this.heat = heat;
	}

	public Byte getHeatRandomizer() {
		return heatRandomizer;
	}

	public void setHeatRandomizer(Byte heatRandomizer) {
		this.heatRandomizer = heatRandomizer;
	}

	public short getResistanceHeat() {
		return resistanceHeat;
	}

	public void setResistanceHeat(short resistanceHeat) {
		this.resistanceHeat = resistanceHeat;
	}

	public short getResistanceCold() {
		return resistanceCold;
	}
	
	public void setResistanceCold(short resistanceCold) {
		this.resistanceCold = resistanceCold;
	}
	
	public short getResistancePvsP() {
		return resistancePvsP;
	}

	public void setResistancePvsP(short resistancePvsP) {
		this.resistancePvsP = resistancePvsP;
	}

	public void decreaseTemperatureResistance() {
		if(resistanceHeat != 0)
			resistanceHeat--;
		if(resistanceCold != 0)
			resistanceCold--;
	}
	
	public void decreasePvPProtection() {
		if(resistancePvsP != 0) resistancePvsP--;
	}

	public Integer getHealth() {
		return health;
	}

	public void setHealth(Integer health) {
		this.health = health;
	}

	public Integer getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(Integer maxHealth) {
		this.maxHealth = maxHealth;
	}

	public Integer getMana() {
		return mana;
	}

	public void setMana(Integer mana) {
		this.mana = mana;
	}

	public Integer getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(Integer maxMana) {
		this.maxMana = maxMana;
	}
	
	public boolean isSkillReady(Player player, String skillId) {
		Long cooldown = skillCooldownMap.get(skillId);
		if(cooldown == null || cooldown <= System.currentTimeMillis())
			return true;
		long remaining = (cooldown - System.currentTimeMillis()) / 1000;
		remaining = remaining < 1 ? 1 : remaining;
		player.sendMessage(ChatColor.RED + "Skill not ready! " + remaining + " Seconds remain!");
		return false;
	}
	
	public void updateSkillCooldown(Player player, String skillId) {
		Long cooldown = (long) (player.hasPermission("wauz.debug.magic") ? 1 : WauzPlayerSkillExecutor.getSkill(skillId).getCooldownSeconds());
		skillCooldownMap.put(skillId, cooldown * 1000 + System.currentTimeMillis());
	}
	
	public boolean isActionReady(Player player, String actionId) {
		Long cooldown = actionCooldownMap.get(actionId);
		return cooldown == null || cooldown <= System.currentTimeMillis();
	}
	
	public void updateActionCooldown(Player player, String actionId, Long cooldown) {
		actionCooldownMap.put(actionId, cooldown + System.currentTimeMillis());
	}

	public String getPet() {
		return pet;
	}

	public void setPet(String pet) {
		this.pet = pet;
	}

	public String getSelectedPetSlot() {
		return selectedPetSlot;
	}

	public void setSelectedPetSlot(String selectedPetSlot) {
		this.selectedPetSlot = selectedPetSlot;
	}

	public String getSelectedCharacterSlot() {
		return selectedCharacterSlot;
	}

	public void setSelectedCharacterSlot(String selectedCharacterSlot) {
		this.selectedCharacterSlot = selectedCharacterSlot;
	}
	
	public boolean isCharacterSelected() {
		return StringUtils.isNotBlank(selectedCharacterSlot);
	}

	public String getSelectedCharacterWorld() {
		return selectedCharacterWorld;
	}

	public void setSelectedCharacterWorld(String selectedCharacterWorld) {
		this.selectedCharacterWorld = selectedCharacterWorld;
	}

	public String getSelectedCharacterRace() {
		return selectedCharacterRace;
	}

	public void setSelectedCharacterRace(String selectedCharacterRace) {
		this.selectedCharacterRace = selectedCharacterRace;
	}

	public String getGroupUuidString() {
		return groupUuidString;
	}

	public void setGroupUuidString(String groupUuidString) {
		this.groupUuidString = groupUuidString;
	}
	
	public boolean isInGroup() {
		return groupUuidString != null;
	}

	public String getWauzPlayerEventName() {
		return wauzPlayerEventName;
	}

	public void setWauzPlayerEventName(String wauzPlayerEventName) {
		this.wauzPlayerEventName = wauzPlayerEventName;
	}

	public WauzPlayerEvent getWauzPlayerEvent() {
		return wauzPlayerEvent;
	}

	public void setWauzPlayerEvent(WauzPlayerEvent wauzPlayerEvent) {
		this.wauzPlayerEvent = wauzPlayerEvent;
	}

	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	public void setSongPlayer(SongPlayer songPlayer) {
		this.songPlayer = songPlayer;
	}

	public WauzRegion getRegion() {
		return region;
	}

	public void setRegion(WauzRegion region) {
		this.region = region;
	}
	
}
