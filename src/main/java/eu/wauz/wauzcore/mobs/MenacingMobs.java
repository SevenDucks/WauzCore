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
import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class MenacingMobs {
	
	private static String[] possibleModifiers = {"Deflecting", "Explosive", "Massive", "Ravenous", "Splitting"};
	
	public static void addMenacingMob(Entity entity, MythicMob mythicMob) {
		MenacingMobsConfig config = new MenacingMobsConfig(mythicMob.getConfig());
		List<String> modifiers = new ArrayList<String>();
		
		if(config.isEnableModifiers()) {
			modifiers = getRandomModifiers(config.isEnableSecondModifier() ? 2 : 1);
			for(String modifier : modifiers) {
				entity.setMetadata("wzMod" + modifier, new FixedMetadataValue(WauzCore.getInstance(), true));
			}
		}
		if(config.isEnableHealthBar()) {
			new WauzPlayerBossBar(entity, modifiers, mythicMob.getBaseHealth(), config.isEnableRaidHealthBar());
		}
	}
	
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
	
	private static class MenacingMobsConfig {
		
		private boolean enableModifiers;
		
		private boolean enableSecondModifier;
		
		private boolean enableHealthBar;
		
		private boolean enableRaidHealthBar;
		
		public MenacingMobsConfig(MythicConfig mythicConfig) {
			List<String> modifiers = mythicConfig.getStringList("WauzMods");
			if(modifiers == null || modifiers.isEmpty())
				return;
			
			for(String modifier : modifiers) {
				if(StringUtils.equalsIgnoreCase(modifier, "MenacingChance")) {
					enableModifiers = Chance.oneIn(20);
					enableSecondModifier = enableModifiers && Chance.oneIn(4);
				}
				else if(StringUtils.startsWithIgnoreCase(modifier, "CustomBossBar")) {
					enableHealthBar = true;
					enableRaidHealthBar = modifier.contains("--Raid");
				}
			}
		}
		
		public boolean isEnableModifiers() {
			return enableModifiers;
		}

		public boolean isEnableSecondModifier() {
			return enableSecondModifier;
		}

		public boolean isEnableHealthBar() {
			return enableHealthBar;
		}

		public boolean isEnableRaidHealthBar() {
			return enableRaidHealthBar;
		}

	}

}
