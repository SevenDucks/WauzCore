package eu.wauz.wauzcore.mobs.bestiary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import eu.wauz.wauzcore.data.BestiaryConfigurator;
import eu.wauz.wauzcore.mobs.MenacingMobsConfig;
import eu.wauz.wauzcore.system.util.MythicUtils;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import io.lumine.mythic.api.mobs.MythicMob;

/**
 * An entry of a bestiary species, generated from a bestiary config and a mythic mobs config file.
 * 
 * @author Wauzmons
 * 
 * @see WauzBestiarySpecies
 */
public class WauzBestiaryEntry {
	
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
	 * The mythic mob bound to the entry.
	 */
	private MythicMob entryMob;
	
	/**
	 * The mythic mob display name of the entry.
	 */
	private String entryMobDisplayName;
	
	/**
	 * The mob description of the entry.
	 */
	private String entryMobDescription;
	
	/**
	 * If the mob is a raid boss.
	 */
	private boolean isBoss;
	
	/**
	 * A map of lore lists for each observation rank, indexed by rank tier.
	 */
	private Map<Integer, List<String>> rankLoreMap = new HashMap<>();
	
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
		entryMob = MythicUtils.getMob(entryMobName);
		entryMobDisplayName = entryMob.getDisplayName().get();
		entryMobDescription = BestiaryConfigurator.getEntryMobDescription(category, species, entryName);
		addRankCLores();
		addRankBLores();
		addRankALores();
		addRankSLores();
	}
	
	/**
	 * Adds the lores, unlocked at observation rank C.
	 * 
	 * @see ObservationRank#C
	 */
	private void addRankCLores() {
		List<String> lores = new ArrayList<>();
		for(String textPart : UnicodeUtils.wrapText(entryMobDescription)) {
			lores.add(ChatColor.WHITE + textPart);
		}
		rankLoreMap.put(ObservationRank.C.getRankTier(), lores);
	}
	
	/**
	 * Adds the lores, unlocked at observation rank B.
	 * 
	 * @see ObservationRank#B
	 */
	private void addRankBLores() {
		List<String> lores = new ArrayList<>();
		MenacingMobsConfig menacingMob = new MenacingMobsConfig(entryMob.getConfig());
		isBoss = menacingMob.isEnableRaidHealthBar();
		int roundedBaseExp = (int) (menacingMob.getExpAmount() * 100);
		lores.add("");
		lores.add(ChatColor.WHITE + "Health: " + ChatColor.LIGHT_PURPLE + ((int) entryMob.getHealth().get()));
		lores.add(ChatColor.WHITE + "Attack: " + ChatColor.RED + ((int) entryMob.getDamage().get()));
		lores.add(ChatColor.WHITE + "Base Exp: " + ChatColor.AQUA + roundedBaseExp);
		rankLoreMap.put(ObservationRank.B.getRankTier(), lores);
	}
	
	/**
	 * Adds the lores, unlocked at observation rank A.
	 * 
	 * @see ObservationRank#A
	 */
	private void addRankALores() {
		List<String> lores = new ArrayList<>();
		lores.add("");
		lores.add(ChatColor.YELLOW + "+25% Damage Dealt to Mob");
		rankLoreMap.put(ObservationRank.A.getRankTier(), lores);
	}
	
	/**
	 * Adds the lores, unlocked at observation rank S.
	 * 
	 * @see ObservationRank#S
	 */
	private void addRankSLores() {
		List<String> lores = new ArrayList<>();
		lores.add(ChatColor.YELLOW + "+40% Experience Gained from Mob");
		rankLoreMap.put(ObservationRank.S.getRankTier(), lores);
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
	 * @return The full config name of the entry.
	 */
	public String getEntryFullName() {
		return entrySpecies.getSpeciesCategory().toString() + "." + entrySpecies.getSpeciesName() + "." + entryName;
	}

	/**
	 * @return The mythic mob name of the entry.
	 */
	public String getEntryMobName() {
		return entryMobName;
	}

	/**
	 * @return The mythic mob display name of the entry.
	 */
	public String getEntryMobDisplayName() {
		return entryMobDisplayName;
	}

	/**
	 * @return The mob description of the entry.
	 */
	public String getEntryMobDescription() {
		return entryMobDescription;
	}

	/**
	 * @return If the mob is a raid boss.
	 */
	public boolean isBoss() {
		return isBoss;
	}
	
	/**
	 * @param rankTier The tier of the rank to get the lores for.
	 * 
	 * @return The lores unlocked with the rank.
	 */
	public List<String> getRankLores(int rankTier) {
		return rankLoreMap.get(rankTier);
	}

}
