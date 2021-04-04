package eu.wauz.wauzcore.mobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;

import eu.wauz.wauzcore.players.ui.WauzPlayerEnemyBossBar;
import eu.wauz.wauzcore.system.util.Chance;
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
	 * @see MobMetadataUtils#setBestiaryEntry(Entity, String)
	 * @see MenacingMobsSpawner#possibleModifiers
	 * @see MobMetadataUtils#setMenacingModifier(Entity, String)
	 * @see MobMetadataUtils#setRaidBoss(Entity, boolean)
	 * @see MobMetadataUtils#setExpDrop(Entity, int, double)
	 * @see MobMetadataUtils#setKeyDrop(Entity, String)
	 */
	public static void addMenacingMob(Entity entity, MythicMob mythicMob) {
		MenacingMobsConfig config = new MenacingMobsConfig(mythicMob.getConfig());
		List<MenacingModifier> modifiers = new ArrayList<>();
		
		if(StringUtils.isNotBlank(config.getBestiaryEntryName())) {
			MobMetadataUtils.setBestiaryEntry(entity, config.getBestiaryEntryName());
		}
		if(config.isEnableModifiers()) {
			modifiers = getRandomModifiers(config.isEnableSecondModifier() ? 2 : 1);
			for(MenacingModifier modifier : modifiers) {
				MobMetadataUtils.setMenacingModifier(entity, modifier);
			}
		}
		if(config.isEnableHealthBar()) {
			new WauzPlayerEnemyBossBar(entity, modifiers, mythicMob.getHealth().get(), config.isEnableRaidHealthBar());
			MobMetadataUtils.setRaidBoss(entity, config.isEnableRaidHealthBar());
		}
		if(StringUtils.isNotBlank(config.getExpDropString())) {
			MobMetadataUtils.setExpDrop(entity, config.getExpTier(), config.getExpAmount());
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
		
		while(modifiers.size() < amount) {
			MenacingModifier modifier = unusedModifiers.get(Chance.randomInt(unusedModifiers.size()));
			unusedModifiers.remove(modifier);
			modifiers.add(modifier);
		}
		return modifiers;
	}

}
