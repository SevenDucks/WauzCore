package eu.wauz.wauzcore.mobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;

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
	 * Spawns a mob and configures its metadata according to the generated config.
	 * This includes menacing modifiers, boss bars, aswell as exp and key drops.
	 * 
	 * @param entity
	 * @param mythicMob
	 * 
	 * @see MenacingMobsConfig
	 * @see MenacingMobsSpawner#possibleModifiers
	 * @see MobMetadataUtils#setMenacingModifier(Entity, String)
	 * @see MobMetadataUtils#setExpDrop(Entity, int, double)
	 * @see MobMetadataUtils#setKeyDrop(Entity, String)
	 */
	public static void addMenacingMob(Entity entity, MythicMob mythicMob) {
		MenacingMobsConfig config = new MenacingMobsConfig(mythicMob.getConfig());
		List<MenacingModifier> modifiers = new ArrayList<>();
		
		if(config.isEnableModifiers()) {
			modifiers = getRandomModifiers(config.isEnableSecondModifier() ? 2 : 1);
			for(MenacingModifier modifier : modifiers) {
				MobMetadataUtils.setMenacingModifier(entity, modifier);
			}
		}
		if(config.isEnableHealthBar()) {
			new WauzPlayerBossBar(entity, modifiers, mythicMob.getBaseHealth(), config.isEnableRaidHealthBar());
		}
		if(StringUtils.isNotBlank(config.getExpDropString())) {
			String[] expStrings = config.getExpDropString().split(" ");
			MobMetadataUtils.setExpDrop(entity, Integer.parseInt(expStrings[0]), Double.parseDouble(expStrings[1]));
		}
		if(StringUtils.isNotBlank(config.getKeyDropString())) {
			MobMetadataUtils.setKeyDrop(entity, config.getKeyDropString());
		}
	}
	
	/**
	 * @param amount How many modifiers should be returned.
	 * 
	 * @return A list of unique menacing modifiers.
	 * 
	 * @see MenacingMobsSpawner#possibleModifiers
	 */
	public static List<MenacingModifier> getRandomModifiers(int amount) {
		List<MenacingModifier> modifiers = new ArrayList<>();
		List<MenacingModifier> unusedModifiers = new ArrayList<>(Arrays.asList(MenacingModifier.values()));
		
		Random random = new Random();
		while(modifiers.size() < amount) {
			MenacingModifier modifier = unusedModifiers.get(random.nextInt(unusedModifiers.size()));
			unusedModifiers.remove(modifier);
			modifiers.add(modifier);
		}
		return modifiers;
	}

}
