package eu.wauz.wauzcore.mobs;

import java.util.List;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.mythic.api.config.MythicConfig;

/**
 * This is the place, where metadata is generated from the MythicMobs config,
 * so that the spawner can insert it into the mob.
 * This includes menacing modifiers, boss bars, aswell as exp and key drops.
 * 
 * @author Wauzmons
 * 
 * @see MenacingMobsSpawner
 */
public class MenacingMobsConfig {
	
	/**
	 * The full name of the mob's bestiary entry.
	 */
	private String bestiaryEntryName;
	
	/**
	 * If the mob will receive a menacing modifier.
	 * One in 20 chance, if enabled.
	 */
	private boolean enableModifiers;
	
	/**
	 * If the mob will receive a second menacing modifier.
	 * One in 4 chance, if it already has a first one.
	 */
	private boolean enableSecondModifier;
	
	/**
	 * If the mob will receive a health bar (in red, or yellow if menacing).
	 */
	private boolean enableHealthBar;
	
	/**
	 * If the mob will receive a boss health bar (in purple).
	 */
	private boolean enableRaidHealthBar;
	
	/**
	 * How much exp the mob will drop and for which level.
	 */
	private String expDropString;
	
	/**
	 * What key the mob will drop.
	 */
	private String keyDropString;
	
	/**
	 * Reads the values from the MythicConfig into the new MenacingMobsConfig
	 * Possible config values are:
	 * 
	 * BestiaryEntry (name) = The full name of the mob's bestiary entry.
	 * MenacingChance = The mob can have menacing modifiers.
	 * CustomBossBar --Raid = Has a boss bar, raid attribute colors it purple.
	 * GrantExp (level) (percent) = It will drop exp up to a specific level.
	 * GrantKey (name) = It will drop the named key.
	 * 
	 * @param mythicConfig The mythic mobs config.
	 */
	public MenacingMobsConfig(MythicConfig mythicConfig) {
		List<String> modifiers = mythicConfig.getStringList("WauzMods");
		if(modifiers == null || modifiers.isEmpty()) {
			return;
		}
		
		for(String modifier : modifiers) {
			if(StringUtils.startsWith(modifier, "BestiaryEntry")) {
				bestiaryEntryName = StringUtils.substringAfter(modifier, "BestiaryEntry ");
			}
			else if(StringUtils.equalsIgnoreCase(modifier, "MenacingChance")) {
				enableModifiers = Chance.oneIn(20);
				enableSecondModifier = enableModifiers && Chance.oneIn(4);
			}
			else if(StringUtils.startsWithIgnoreCase(modifier, "CustomBossBar")) {
				enableHealthBar = true;
				enableRaidHealthBar = modifier.contains("--Raid");
			}
			else if(StringUtils.startsWithIgnoreCase(modifier, "GrantExp ")) {
				expDropString = StringUtils.substringAfter(modifier, "GrantExp ");
			}
			else if(StringUtils.startsWithIgnoreCase(modifier, "GrantKey ")) {
				keyDropString = StringUtils.substringAfter(modifier, "GrantKey ");
			}
		}
	}
	
	/**
	 * @return The full name of the mob's bestiary entry.
	 */
	public String getBestiaryEntryName() {
		return bestiaryEntryName;
	}

	/**
	 * @return If the mob will receive a menacing modifier.
	 * One in 20 chance, if enabled.
	 */
	public boolean isEnableModifiers() {
		return enableModifiers;
	}

	/**
	 * @return If the mob will receive a second menacing modifier.
	 * One in 4 chance, if it already has a first one.
	 */
	public boolean isEnableSecondModifier() {
		return enableSecondModifier;
	}

	/**
	 * @return If the mob will receive a health bar (in red, or yellow if menacing).
	 */
	public boolean isEnableHealthBar() {
		return enableHealthBar;
	}

	/**
	 * @return If the mob will receive a boss health bar (in purple).
	 */
	public boolean isEnableRaidHealthBar() {
		return enableRaidHealthBar;
	}

	/**
	 * @return How much exp the mob will drop and for which level.
	 */
	public String getExpDropString() {
		return expDropString;
	}
	
	/**
	 * @return The tier (max level) of the exp to drop.
	 */
	public int getExpTier() {
		String[] expStrings = expDropString.split(" ");
		return Integer.parseInt(expStrings[0]);
	}
	
	/**
	 * @return The amount of exp to drop.
	 */
	public double getExpAmount() {
		String[] expStrings = expDropString.split(" ");
		return Double.parseDouble(expStrings[1]);
	}

	/**
	 * @return What key the mob will drop.
	 */
	public String getKeyDropString() {
		return keyDropString;
	}

}
