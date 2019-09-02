package eu.wauz.wauzcore;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.WauzIdentifier;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.players.WauzPlayerGuildTabCompleter;
import eu.wauz.wauzcore.skills.SkillDeath;
import eu.wauz.wauzcore.skills.SkillJudgement;
import eu.wauz.wauzcore.skills.SkillStrength;
import eu.wauz.wauzcore.skills.SkillTemperance;
import eu.wauz.wauzcore.skills.SkillTheChariot;
import eu.wauz.wauzcore.skills.SkillTheEmperor;
import eu.wauz.wauzcore.skills.SkillTheEmpress;
import eu.wauz.wauzcore.skills.SkillTheFool;
import eu.wauz.wauzcore.skills.SkillTheHermit;
import eu.wauz.wauzcore.skills.SkillTheHierophant;
import eu.wauz.wauzcore.skills.SkillTheHighPriestess;
import eu.wauz.wauzcore.skills.SkillTheLovers;
import eu.wauz.wauzcore.skills.SkillTheMagician;
import eu.wauz.wauzcore.skills.SkillTheMoon;
import eu.wauz.wauzcore.skills.SkillTheStar;
import eu.wauz.wauzcore.skills.SkillTheSun;
import eu.wauz.wauzcore.skills.SkillTheTower;
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
		
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Tokens", "tokens");
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Coins", "reput.cow");
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Soulstones", "reput.souls");
		
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Republic", "reput.wauzland");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Eternal", "reput.empire");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Dark", "reput.legion");
		
		WauzPlayerSkillExecutor.registerSkill(new SkillTheFool());			// Tarot (00) 0
		WauzPlayerSkillExecutor.registerSkill(new SkillTheMagician());		// Tarot (01) I
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHighPriestess());	// Tarot (02) II
		WauzPlayerSkillExecutor.registerSkill(new SkillTheEmpress());		// Tarot (03) III
		WauzPlayerSkillExecutor.registerSkill(new SkillTheEmperor());		// Tarot (04) IV
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHierophant());	// Tarot (05) V
		WauzPlayerSkillExecutor.registerSkill(new SkillTheLovers());		// Tarot (06) VI
		WauzPlayerSkillExecutor.registerSkill(new SkillTheChariot());		// Tarot (07) VII
		WauzPlayerSkillExecutor.registerSkill(new SkillStrength());			// Tarot (08) VIII
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHermit());		// Tarot (09) IX
		WauzPlayerSkillExecutor.registerSkill(new SkillWheelOfFortune());	// Tarot (10) X
		WauzPlayerSkillExecutor.registerSkill(new SkillDeath());			// Tarot (13) XIII
		WauzPlayerSkillExecutor.registerSkill(new SkillTemperance());		// Tarot (14) XIV
		WauzPlayerSkillExecutor.registerSkill(new SkillTheTower());			// Tarot (16) XVI
		WauzPlayerSkillExecutor.registerSkill(new SkillTheStar());			// Tarot (17) XVII
		WauzPlayerSkillExecutor.registerSkill(new SkillTheMoon());			// Tarot (18) XVIII
		WauzPlayerSkillExecutor.registerSkill(new SkillTheSun());			// Tarot (19) XIX
		WauzPlayerSkillExecutor.registerSkill(new SkillJudgement());		// Tarot (20) XX
		WauzPlayerSkillExecutor.registerSkill(new SkillTheWorld());			// Tarot (21) XXI
		
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.WOODEN_SWORD, " Shortsword", 1.50, 32));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.GOLDEN_SWORD, " Rapier", 1.55, 64));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.STONE_SWORD, " Longsword", 1.60, 128));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.IRON_SWORD, " Claymore", 1.65, 256));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.DIAMOND_SWORD, " Excalibur", 1.70, 512));
		
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.WOODEN_AXE, " Hatchet", 1.70, 32));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.GOLDEN_AXE, " Halberd", 1.75, 64));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.STONE_AXE, " Waraxe", 1.80, 128));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.IRON_AXE, " Greataxe", 1.85, 256));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.DIAMOND_AXE, " Worldbreaker", 1.90, 512));
		
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.WOODEN_HOE, " Staff", 1.30, 32));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.GOLDEN_HOE, " Pole", 1.35, 64));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.STONE_HOE, " Mace", 1.40, 128));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.IRON_HOE, " Sceptre", 1.45, 256));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.DIAMOND_HOE, " Soulreaver", 1.50, 512));
		
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.BOW, " Crude Bow", 0.60, 64));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.BOW, " Recurve Bow", 0.90, 128));
		WauzIdentifier.addEquipType(new Equipment("Weapon", Material.BOW, " Infractem Bow", 1.20, 256));
		
		WauzIdentifier.addEquipType(new Equipment("Armor", Material.LEATHER_CHESTPLATE, " Vest", 1.15, 64));
		WauzIdentifier.addEquipType(new Equipment("Armor", Material.GOLDEN_CHESTPLATE, " Robe", 1.30, 128));
		WauzIdentifier.addEquipType(new Equipment("Armor", Material.CHAINMAIL_CHESTPLATE, " Mail", 1.45, 256));
		WauzIdentifier.addEquipType(new Equipment("Armor", Material.IRON_CHESTPLATE, " Plate", 1.60, 512));
		WauzIdentifier.addEquipType(new Equipment("Armor", Material.DIAMOND_CHESTPLATE, " Herogarb", 1.75, 768));
	}

}
