package eu.wauz.wauzcore.oneblock;

import eu.wauz.wauzcore.system.util.Chance;

/**
 * The types of chests, that can spawn in the one-block gamemode.
 * 
 * @author Wauzmons
 */
public enum OneChestType {
	
	/**
	 * The most common chest type.
	 */
	COMMON("common"),
	
	/**
	 * A rarer than common chest type.
	 */
	RARE("rare"),
	
	/**
	 * A really rare chest type.
	 */
	EPIC("epic"),
	
	/**
	 * The rarest chest type of all.
	 */
	MYTHIC("mythic");
	
	/**
	 * Determines a random chest type, based on rarity rates.</br>
	 * 55% Chance for Common</br>
	 * 30% Chance for Rare</br>
	 * 12% Chance for Epic</br>
	 * 3% Chance for Mythic
	 * 
	 * @return The random chest type.
	 */
	public static OneChestType getRandomChestType() {
		int rarity = Chance.randomInt(10000) + 1;
		
		if(rarity <= 5500) {
			return COMMON;
		}
		else if(rarity <= 8500) {
			return RARE;
		}
		else if(rarity <= 9700) {
			return EPIC;
		}
		else if(rarity <= 10000) {
			return MYTHIC;
		}
		return COMMON;
	}

	/**
	 * The name of the chest type.
	 */
	private String name;
	
	/**
	 * Creates a new chest type with given name.
	 * 
	 * @param name The name of the chest type.
	 */
	OneChestType(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the chest type in a description friendly format.
	 * 
	 * @return The name of the chest type.
	 */
	@Override
	public String toString() {
		return name;
	}
	
}
