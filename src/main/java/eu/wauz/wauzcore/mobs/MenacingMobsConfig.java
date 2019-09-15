package eu.wauz.wauzcore.mobs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.system.util.Chance;
import io.lumine.xikage.mythicmobs.io.MythicConfig;

public class MenacingMobsConfig {
	
	private boolean enableModifiers;
	
	private boolean enableSecondModifier;
	
	private boolean enableHealthBar;
	
	private boolean enableRaidHealthBar;
	
	private String expDropString;
	
	private String keyDropString;
	
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
			else if(StringUtils.startsWithIgnoreCase(modifier, "GrantExp ")) {
				expDropString = StringUtils.substringAfter(modifier, "GrantExp ");
			}
			else if(StringUtils.startsWithIgnoreCase(modifier, "GrantKey ")) {
				keyDropString = StringUtils.substringAfter(modifier, "GrantKey ");
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

	public String getExpDropString() {
		return expDropString;
	}

	public String getKeyDropString() {
		return keyDropString;
	}

}
