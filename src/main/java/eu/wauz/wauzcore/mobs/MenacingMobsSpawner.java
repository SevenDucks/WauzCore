package eu.wauz.wauzcore.mobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.ui.WauzPlayerBossBar;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

/**
 * Responsible for spawning mobs with menacing modifiers
 * and for writing metadata like exp or boss bars into the entity.
 * 
 * @author Wauzmons
 */
public class MenacingMobsSpawner {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * A list of possible menacing modifiers (prefixes):
	 * 
	 * Explosive = Explodes on death, dealing 500% damage.
	 * Deflecting = Knocks back everyone who attacks the mob.
	 * Massive = Takes only 20% of inflicted damage.
	 * Ravenous = Chases the player with 200% speed.
	 * Splitting = Splits into 4 mobs on death.
	 */
	private static String[] possibleModifiers = {"Deflecting", "Explosive", "Massive", "Ravenous", "Splitting"};
	
	/**
	 * Spawns a mob and configures its metadata according to the generated config.
	 * This includes menacing modifiers, boss bars, aswell as exp and key drops.
	 * 
	 * @param entity
	 * @param mythicMob
	 * 
	 * @see MenacingMobsConfig
	 * @see MenacingMobsSpawner#possibleModifiers
	 */
	public static void addMenacingMob(Entity entity, MythicMob mythicMob) {
		MenacingMobsConfig config = new MenacingMobsConfig(mythicMob.getConfig());
		List<String> modifiers = new ArrayList<String>();
		
		if(config.isEnableModifiers()) {
			modifiers = getRandomModifiers(config.isEnableSecondModifier() ? 2 : 1);
			for(String modifier : modifiers) {
				entity.setMetadata("wzMod" + modifier, new FixedMetadataValue(core, true));
			}
		}
		if(config.isEnableHealthBar()) {
			new WauzPlayerBossBar(entity, modifiers, mythicMob.getBaseHealth(), config.isEnableRaidHealthBar());
		}
		if(StringUtils.isNotBlank(config.getExpDropString())) {
			String[] expStrings = config.getExpDropString().split(" ");
			entity.setMetadata("wzExpTier", new FixedMetadataValue(core, Integer.parseInt(expStrings[0])));
			entity.setMetadata("wzExpAmount", new FixedMetadataValue(core, Double.parseDouble(expStrings[1])));
		}
		if(StringUtils.isNotBlank(config.getKeyDropString())) {
			entity.setMetadata("wzKeyId", new FixedMetadataValue(core, config.getKeyDropString()));
		}
	}
	
	/**
	 * @param amount How many modifiers should be returned.
	 * 
	 * @return A list of unique menacing modifiers.
	 * 
	 * @see MenacingMobsSpawner#possibleModifiers
	 */
	public static List<String> getRandomModifiers(int amount) {
		List<String> modifiers = new ArrayList<String>();
		List<String> unusedModifiers = new ArrayList<String>(Arrays.asList(possibleModifiers));
		
		Random random = new Random();
		while(modifiers.size() < amount) {
			String modifier = unusedModifiers.get(random.nextInt(unusedModifiers.size()));
			unusedModifiers.remove(modifier);
			modifiers.add(modifier);
		}
		return modifiers;
	}

}
