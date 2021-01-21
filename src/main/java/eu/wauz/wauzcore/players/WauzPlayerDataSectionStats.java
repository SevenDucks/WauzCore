package eu.wauz.wauzcore.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.skills.passive.PassiveNutrition;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A section of the player data for storing stat data.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData
 */
public class WauzPlayerDataSectionStats {
	
	/**
	 * The player data the section belongs to.
	 */
	private final WauzPlayerData playerData;
	
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
	 * Constructs a new instance of the section.
	 * 
	 * @param playerData The player data the section belongs to.
	 */
	public WauzPlayerDataSectionStats(WauzPlayerData playerData) {
		this.playerData = playerData;
	}
	
	/**
	 * @return The player data the section belongs to.
	 */
	public WauzPlayerData getPlayerData() {
		return playerData;
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
		int bonusHealth = 0;
		Player player = playerData.getPlayer();
		if(WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) {
			bonusHealth += playerData.getSkills().getCachedPassive(PassiveNutrition.PASSIVE_NAME).getLevel() * 2;
		}
		return maxHealth + bonusHealth;
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

}
