package eu.wauz.wauzcore;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.WauzIdentifier;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.players.WauzPlayerGuildTabCompleter;
import eu.wauz.wauzcore.skills.SkillJudgement;
import eu.wauz.wauzcore.skills.SkillTemperance;
import eu.wauz.wauzcore.skills.SkillTheEmpress;
import eu.wauz.wauzcore.skills.SkillTheHierophant;
import eu.wauz.wauzcore.skills.SkillTheHighPriestess;
import eu.wauz.wauzcore.skills.SkillTheLovers;
import eu.wauz.wauzcore.skills.SkillTheMagician;
import eu.wauz.wauzcore.skills.SkillTheStar;
import eu.wauz.wauzcore.skills.SkillTheWorld;
import eu.wauz.wauzcore.skills.SkillWheelOfFortune;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillTabCompleter;
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import net.md_5.bungee.api.ChatColor;

public class WauzLoader {
	
	public static void init() {
		WauzRegion.init();
		WauzQuest.init();
		WauzPlayerGuild.init();
		
		InstanceManager.removeInactiveInstances();
		
		StatisticsFetcher.calculate();
		
		Bukkit.getPluginCommand("apply").setTabCompleter(new WauzPlayerGuildTabCompleter());
		Bukkit.getPluginCommand("wzSkill").setTabCompleter(new WauzPlayerSkillTabCompleter());
		Bukkit.getPluginCommand("wzSkill.weapon").setTabCompleter(new WauzPlayerSkillTabCompleter());
		
		MenuUtils.staticItems.add(Material.FILLED_MAP);
		MenuUtils.staticItems.add(Material.DIAMOND);
		MenuUtils.staticItems.add(Material.CLOCK);
		MenuUtils.staticItems.add(Material.NETHER_STAR);
		MenuUtils.staticItems.add(Material.BARRIER);
		MenuUtils.staticItems.add(Material.PLAYER_HEAD);
		MenuUtils.staticItems.add(Material.FISHING_ROD);
		MenuUtils.staticItems.add(Material.SNOWBALL);
		MenuUtils.staticItems.add(Material.BLAZE_ROD);
		MenuUtils.staticItems.add(Material.FEATHER);
		
		ShopBuilder.currency.put(ChatColor.GOLD + "Tokens", "tokens");
		ShopBuilder.currency.put(ChatColor.GOLD + "Coins", "reput.cow");
		ShopBuilder.currency.put(ChatColor.GOLD + "Soulstones", "reput.souls");
		
		ShopBuilder.currency.put(ChatColor.BLUE + "Republic", "reput.wauzland");
		ShopBuilder.currency.put(ChatColor.BLUE + "Eternal", "reput.empire");
		ShopBuilder.currency.put(ChatColor.BLUE + "Dark", "reput.legion");
		
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheMagician.SKILL_NAME, new SkillTheMagician());			// Tarot (01) I
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheHighPriestess.SKILL_NAME, new SkillTheHighPriestess());	// Tarot (02) II
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheEmpress.SKILL_NAME, new SkillTheEmpress());				// Tarot (03) III
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheHierophant.SKILL_NAME, new SkillTheHierophant());		// Tarot (05) V
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheLovers.SKILL_NAME, new SkillTheLovers());				// Tarot (06) VI
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillWheelOfFortune.SKILL_NAME, new SkillWheelOfFortune());		// Tarot (10) X
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTemperance.SKILL_NAME, new SkillTemperance());				// Tarot (14) XIV
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheStar.SKILL_NAME, new SkillTheStar());					// Tarot (17) XVII
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillJudgement.SKILL_NAME, new SkillJudgement());				// Tarot (20) XX
		WauzPlayerSkillExecutor.playerSkillMap.put(SkillTheWorld.SKILL_NAME, new SkillTheWorld());					// Tarot (21) XXI
		
		WauzIdentifier.material.add(new Equipment("Weapon", Material.WOODEN_SWORD, " Shortsword", 1.50));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.GOLDEN_SWORD, " Rapier", 1.75));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.STONE_SWORD, " Longsword", 1.50));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.IRON_SWORD, " Claymore", 1.50));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.DIAMOND_SWORD, " Excalibur", 1.60));
		
		WauzIdentifier.material.add(new Equipment("Weapon", Material.WOODEN_AXE, " Hatchet", 1.70));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.GOLDEN_AXE, " Halberd", 1.95));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.STONE_AXE, " Waraxe", 1.70));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.IRON_AXE, " Greataxe", 1.70));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.DIAMOND_AXE, " Worldbreaker", 1.80));
		
		WauzIdentifier.material.add(new Equipment("Weapon", Material.WOODEN_HOE, " Staff", 1.30));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.GOLDEN_HOE, " Pole", 1.55));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.STONE_HOE, " Mace", 1.30));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.IRON_HOE, " Sceptre", 1.30));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.DIAMOND_HOE, " Soulreaver", 1.40));
		
		WauzIdentifier.material.add(new Equipment("Weapon", Material.BOW, " Crude Bow", 0.60));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.BOW, " Recurve Bow", 0.90));
		WauzIdentifier.material.add(new Equipment("Weapon", Material.BOW, " Infractem Bow", 1.20));
		
		WauzIdentifier.material.add(new Equipment("Armor", Material.LEATHER_CHESTPLATE, " Vest", 1.15));
		WauzIdentifier.material.add(new Equipment("Armor", Material.GOLDEN_CHESTPLATE, " Robe", 1.30));
		WauzIdentifier.material.add(new Equipment("Armor", Material.CHAINMAIL_CHESTPLATE, " Mail", 1.45));
		WauzIdentifier.material.add(new Equipment("Armor", Material.IRON_CHESTPLATE, " Plate", 1.60));
		WauzIdentifier.material.add(new Equipment("Armor", Material.DIAMOND_CHESTPLATE, " Herogarb", 1.75));
		
		WauzIdentifier.runeNames.add("Power");
		WauzIdentifier.runeNames.add("Knowledge");
		WauzIdentifier.runeNames.add("Thorns");
		
		WauzIdentifier.equipNames.add("Adamantite");
		WauzIdentifier.equipNames.add("Ancient");
		WauzIdentifier.equipNames.add("Alloyed");
		WauzIdentifier.equipNames.add("Barbarian");
		WauzIdentifier.equipNames.add("Blessed");
		WauzIdentifier.equipNames.add("Broken");
		WauzIdentifier.equipNames.add("Ceremonial");
		WauzIdentifier.equipNames.add("Cobalt");
		WauzIdentifier.equipNames.add("Colossal");
		WauzIdentifier.equipNames.add("Corrupted");
		WauzIdentifier.equipNames.add("Cruel");
		WauzIdentifier.equipNames.add("Cursed");
		WauzIdentifier.equipNames.add("Damaged");
		WauzIdentifier.equipNames.add("Dragonbone");
		WauzIdentifier.equipNames.add("Enchanted");
		WauzIdentifier.equipNames.add("Fallen");
		WauzIdentifier.equipNames.add("Fierce");
		WauzIdentifier.equipNames.add("Flaming");
		WauzIdentifier.equipNames.add("Forgotten");
		WauzIdentifier.equipNames.add("Forsaken");
		WauzIdentifier.equipNames.add("Frozen");
		WauzIdentifier.equipNames.add("Gay");
		WauzIdentifier.equipNames.add("Giant");
		WauzIdentifier.equipNames.add("Goddess");
		WauzIdentifier.equipNames.add("Guardian");
		WauzIdentifier.equipNames.add("Hellforged");
		WauzIdentifier.equipNames.add("Holy");
		WauzIdentifier.equipNames.add("Lightforged");
		WauzIdentifier.equipNames.add("Lost");
		WauzIdentifier.equipNames.add("Majestic");
		WauzIdentifier.equipNames.add("Malevolent");
		WauzIdentifier.equipNames.add("Merciful");
		WauzIdentifier.equipNames.add("Mighty");
		WauzIdentifier.equipNames.add("Mythril");
		WauzIdentifier.equipNames.add("Outlandish");
		WauzIdentifier.equipNames.add("Plain");
		WauzIdentifier.equipNames.add("Polished");
		WauzIdentifier.equipNames.add("Robust");
		WauzIdentifier.equipNames.add("Royal");
		WauzIdentifier.equipNames.add("Ruined");
		WauzIdentifier.equipNames.add("Rusty");
		WauzIdentifier.equipNames.add("Savage");
		WauzIdentifier.equipNames.add("Soldier");
		WauzIdentifier.equipNames.add("Spiked");
		WauzIdentifier.equipNames.add("Stained");
		WauzIdentifier.equipNames.add("Timeworn");
		WauzIdentifier.equipNames.add("Warforged");
		WauzIdentifier.equipNames.add("Weakened");
		WauzIdentifier.equipNames.add("Weathered");
		WauzIdentifier.equipNames.add("Worthless");
		
		WauzIdentifier.shrineNames.add("Amon");
		WauzIdentifier.shrineNames.add("Atar");
		WauzIdentifier.shrineNames.add("Baka");
		WauzIdentifier.shrineNames.add("Bael");
		WauzIdentifier.shrineNames.add("Cata");
		WauzIdentifier.shrineNames.add("Cesa");
		WauzIdentifier.shrineNames.add("Daku");
		WauzIdentifier.shrineNames.add("Deus");
		WauzIdentifier.shrineNames.add("Elek");
		WauzIdentifier.shrineNames.add("Esto");
		WauzIdentifier.shrineNames.add("Furo");
		WauzIdentifier.shrineNames.add("Fitu");
		WauzIdentifier.shrineNames.add("Garo");
		WauzIdentifier.shrineNames.add("Gyro");
		WauzIdentifier.shrineNames.add("Hino");
		WauzIdentifier.shrineNames.add("Hane");
		WauzIdentifier.shrineNames.add("Ivel");
		WauzIdentifier.shrineNames.add("Inuk");
		WauzIdentifier.shrineNames.add("Jago");
		WauzIdentifier.shrineNames.add("Jojo");
		WauzIdentifier.shrineNames.add("Kaka");
		WauzIdentifier.shrineNames.add("Kars");
		WauzIdentifier.shrineNames.add("Leto");
		WauzIdentifier.shrineNames.add("Lupo");
		WauzIdentifier.shrineNames.add("Maro");
		WauzIdentifier.shrineNames.add("Magi");
		WauzIdentifier.shrineNames.add("Naga");
		WauzIdentifier.shrineNames.add("Notu");
		WauzIdentifier.shrineNames.add("Omek");
		WauzIdentifier.shrineNames.add("Oraa");
		WauzIdentifier.shrineNames.add("Popo");
		WauzIdentifier.shrineNames.add("Pino");
		WauzIdentifier.shrineNames.add("Qire");
		WauzIdentifier.shrineNames.add("Quez");
		WauzIdentifier.shrineNames.add("Raka");
		WauzIdentifier.shrineNames.add("Reem");
		WauzIdentifier.shrineNames.add("Sora");
		WauzIdentifier.shrineNames.add("Sado");
		WauzIdentifier.shrineNames.add("Tera");
		WauzIdentifier.shrineNames.add("Toem");
		WauzIdentifier.shrineNames.add("Utga");
		WauzIdentifier.shrineNames.add("Uros");
		WauzIdentifier.shrineNames.add("Vari");
		WauzIdentifier.shrineNames.add("Vago");
		WauzIdentifier.shrineNames.add("Wesa");
		WauzIdentifier.shrineNames.add("Wamu");
		WauzIdentifier.shrineNames.add("Xera");
		WauzIdentifier.shrineNames.add("Xulu");
		WauzIdentifier.shrineNames.add("Yaga");
		WauzIdentifier.shrineNames.add("Yare");
		WauzIdentifier.shrineNames.add("Zaga");
		WauzIdentifier.shrineNames.add("Zeli");
	}

}
