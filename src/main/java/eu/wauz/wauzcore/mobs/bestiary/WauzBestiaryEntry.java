package eu.wauz.wauzcore.mobs.bestiary;

import eu.wauz.wauzcore.data.BestiaryConfigurator;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

/**
 * An entry of a bestiary species, generated from a bestiary config and a mythic mobs config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzBestiarySpecies
 */
public class WauzBestiaryEntry {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	/**
	 * The species the entry belongs to.
	 */
	private WauzBestiarySpecies entrySpecies;
	
	/**
	 * The config name of the entry.
	 */
	private String entryName;
	
	/**
	 * The mythic mob name of the entry.
	 */
	private String entryMobName;
	
	/**
	 * The mob description of the entry.
	 */
	private String entryMobDescription;
	
	/**
	 * Constructs an entry of a bestiary species, based on the bestiary file in the /WauzCore/BestiaryData/Category folder.
	 * 
	 * @param entrySpecies The species the entry belongs to.
	 * @param entryName The config name of the entry.
	 */
	public WauzBestiaryEntry(WauzBestiarySpecies entrySpecies, String entryName) {
		this.entrySpecies = entrySpecies;
		this.entryName = entryName;
		WauzBestiaryCategory category = entrySpecies.getSpeciesCategory();
		String species = entrySpecies.getSpeciesName();
		entryMobName = BestiaryConfigurator.getEntryMobName(category, species, entryName);
		entryMobDescription = BestiaryConfigurator.getEntryMobDescription(category, species, entryName);
		
		MythicMob mythicMob = mythicMobs.getMythicMob(entryMobName);
		mythicMob.getDisplayName();
		mythicMob.getHealth().get();
		mythicMob.getDamage().get();
		
		mythicMob.getDropTable();
	}

	/**
	 * @return The species the entry belongs to.
	 */
	public WauzBestiarySpecies getEntrySpecies() {
		return entrySpecies;
	}

	/**
	 * @return The config name of the entry.
	 */
	public String getEntryName() {
		return entryName;
	}

	/**
	 * @return The mythic mob name of the entry.
	 */
	public String getEntryMobName() {
		return entryMobName;
	}

	/**
	 * @return The mob description of the entry.
	 */
	public String getEntryMobDescription() {
		return entryMobDescription;
	}

}
