package eu.wauz.wauzcore;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;

import eu.wauz.wauzcore.commands.CmdApply;
import eu.wauz.wauzcore.commands.CmdDesc;
import eu.wauz.wauzcore.commands.CmdGld;
import eu.wauz.wauzcore.commands.CmdGroup;
import eu.wauz.wauzcore.commands.CmdGrp;
import eu.wauz.wauzcore.commands.CmdGuild;
import eu.wauz.wauzcore.commands.CmdHome;
import eu.wauz.wauzcore.commands.CmdHub;
import eu.wauz.wauzcore.commands.CmdMenu;
import eu.wauz.wauzcore.commands.CmdMotd;
import eu.wauz.wauzcore.commands.CmdSethome;
import eu.wauz.wauzcore.commands.CmdSpawn;
import eu.wauz.wauzcore.commands.CmdTip;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebug;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugBuilding;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugCrafting;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugMagic;
import eu.wauz.wauzcore.commands.administrative.CmdWzEnhanced;
import eu.wauz.wauzcore.commands.administrative.CmdWzEnter;
import eu.wauz.wauzcore.commands.administrative.CmdWzEnterDev;
import eu.wauz.wauzcore.commands.administrative.CmdWzExp;
import eu.wauz.wauzcore.commands.administrative.CmdWzGetPet;
import eu.wauz.wauzcore.commands.administrative.CmdWzGetRune;
import eu.wauz.wauzcore.commands.administrative.CmdWzHeal;
import eu.wauz.wauzcore.commands.administrative.CmdWzKey;
import eu.wauz.wauzcore.commands.administrative.CmdWzLeave;
import eu.wauz.wauzcore.commands.administrative.CmdWzRepair;
import eu.wauz.wauzcore.commands.administrative.CmdWzSkill;
import eu.wauz.wauzcore.commands.administrative.CmdWzSkillWeapon;
import eu.wauz.wauzcore.commands.administrative.CmdWzSystem;
import eu.wauz.wauzcore.commands.administrative.CmdWzTravel;
import eu.wauz.wauzcore.commands.administrative.CmdWzTravelEvent;
import eu.wauz.wauzcore.commands.completion.TabCompleterEnhancements;
import eu.wauz.wauzcore.commands.completion.TabCompleterGuilds;
import eu.wauz.wauzcore.commands.completion.TabCompleterMenus;
import eu.wauz.wauzcore.commands.completion.TabCompleterRunes;
import eu.wauz.wauzcore.commands.completion.TabCompleterSkills;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.ArmorCategory;
import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementDurability;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementMastery;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementNumbing;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementConsumption;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementDestruction;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementExpertise;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementFerocity;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementNourishment;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.items.runes.RuneHardening;
import eu.wauz.wauzcore.items.runes.RuneKnowledge;
import eu.wauz.wauzcore.items.runes.RunePower;
import eu.wauz.wauzcore.items.runes.RuneThorns;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.skills.SkillDeath;
import eu.wauz.wauzcore.skills.SkillJudgement;
import eu.wauz.wauzcore.skills.SkillJustice;
import eu.wauz.wauzcore.skills.SkillStrength;
import eu.wauz.wauzcore.skills.SkillTemperance;
import eu.wauz.wauzcore.skills.SkillTheChariot;
import eu.wauz.wauzcore.skills.SkillTheDevil;
import eu.wauz.wauzcore.skills.SkillTheEmperor;
import eu.wauz.wauzcore.skills.SkillTheEmpress;
import eu.wauz.wauzcore.skills.SkillTheFool;
import eu.wauz.wauzcore.skills.SkillTheHangedMan;
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
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import net.md_5.bungee.api.ChatColor;

/**
 * Used by the main class to load all static data.
 * Contains different private methods for all data types (commands, equip etc.).
 * 
 * @author Wauzmons
 * 
 * @see WauzCore
 */
public class WauzLoader {
	
	/**
	 * Calls all other methods for loading and initializing data.
	 * Also cleans data from last run by recalculating the statistics
	 * and removing inactive instances.
	 * Only called once per server run.
	 * 
	 * @see InstanceManager#removeInactiveInstances()
	 * @see StatisticsFetcher#calculate()
	 */
	public static void init() {
		WauzRegion.init();
		WauzAchievement.init();
		WauzQuest.init();
		WauzPlayerGuild.init();
		
		InstanceManager.removeInactiveInstances();
		StatisticsFetcher.calculate();
		
		registerPlayerCommands();
		registerAdministrativeCommands();
		registerCommandCompleters();
		
		registerCurrenciesAndReputation();
		
		registerSkillgems();
		registerRunes();
		registerEnhancements();
		
		registerSwords();
		registerAxes();
		registerStaves();
		registerBows();
		registerHooks();
		
		registerLightArmor();
		registerMediumArmor();
		registerHeavyArmor();
	}
	
	/**
	 * Initializes all predefined player commands.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerPlayerCommands() {
		WauzCommandExecutor.registerCommand(new CmdApply());
		WauzCommandExecutor.registerCommand(new CmdDesc());
		WauzCommandExecutor.registerCommand(new CmdGld());
		WauzCommandExecutor.registerCommand(new CmdGroup());
		WauzCommandExecutor.registerCommand(new CmdGrp());
		WauzCommandExecutor.registerCommand(new CmdGuild());
		WauzCommandExecutor.registerCommand(new CmdHome());
		WauzCommandExecutor.registerCommand(new CmdHub());
		WauzCommandExecutor.registerCommand(new CmdMenu());
		WauzCommandExecutor.registerCommand(new CmdMotd());
		WauzCommandExecutor.registerCommand(new CmdSethome());
		WauzCommandExecutor.registerCommand(new CmdSpawn());
		WauzCommandExecutor.registerCommand(new CmdTip());
	}
	
	/**
	 * Initializes all predefined admin and system commands.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerAdministrativeCommands() {
		WauzCommandExecutor.registerCommand(new CmdWzDebug());
		WauzCommandExecutor.registerCommand(new CmdWzDebugBuilding());
		WauzCommandExecutor.registerCommand(new CmdWzDebugCrafting());
		WauzCommandExecutor.registerCommand(new CmdWzDebugMagic());
		WauzCommandExecutor.registerCommand(new CmdWzEnhanced());
		WauzCommandExecutor.registerCommand(new CmdWzEnter());
		WauzCommandExecutor.registerCommand(new CmdWzEnterDev());
		WauzCommandExecutor.registerCommand(new CmdWzExp());
		WauzCommandExecutor.registerCommand(new CmdWzGetPet());
		WauzCommandExecutor.registerCommand(new CmdWzGetRune());
		WauzCommandExecutor.registerCommand(new CmdWzHeal());
		WauzCommandExecutor.registerCommand(new CmdWzKey());
		WauzCommandExecutor.registerCommand(new CmdWzLeave());
		WauzCommandExecutor.registerCommand(new CmdWzRepair());
		WauzCommandExecutor.registerCommand(new CmdWzSkill());
		WauzCommandExecutor.registerCommand(new CmdWzSkillWeapon());
		WauzCommandExecutor.registerCommand(new CmdWzSystem());
		WauzCommandExecutor.registerCommand(new CmdWzTravel());
		WauzCommandExecutor.registerCommand(new CmdWzTravelEvent());
	}
	
	/**
	 * Initializes all predefined command completers.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerCommandCompleters() {
		Bukkit.getPluginCommand("menu").setTabCompleter(new TabCompleterMenus());
		Bukkit.getPluginCommand("apply").setTabCompleter(new TabCompleterGuilds());
		Bukkit.getPluginCommand("wzGetRune").setTabCompleter(new TabCompleterRunes());
		Bukkit.getPluginCommand("wzEnhanced").setTabCompleter(new TabCompleterEnhancements());
		Bukkit.getPluginCommand("wzSkill").setTabCompleter(new TabCompleterSkills());
		Bukkit.getPluginCommand("wzSkill.weapon").setTabCompleter(new TabCompleterSkills());
	}
	
	/**
	 * Initializes all predefined currencies and reputation types.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerCurrenciesAndReputation() {
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Tokens", "tokens");
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Coins", "reput.cow");
		ShopBuilder.registerCurrency(ChatColor.GOLD + "Soulstones", "reput.souls");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Republic", "reput.wauzland");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Eternal", "reput.empire");
		ShopBuilder.registerCurrency(ChatColor.BLUE + "Dark", "reput.legion");
	}
	
	/**
	 * Initializes all predefined skillgems.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerSkillgems() {
		WauzPlayerSkillExecutor.registerSkill(new SkillTheFool());			/** Tarot (00) 0 */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheMagician());		/** Tarot (01) I */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHighPriestess());	/** Tarot (02) II */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheEmpress());		/** Tarot (03) III */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheEmperor());		/** Tarot (04) IV */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHierophant());	/** Tarot (05) V */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheLovers());		/** Tarot (06) VI */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheChariot());		/** Tarot (07) VII */
		WauzPlayerSkillExecutor.registerSkill(new SkillStrength());			/** Tarot (08) VIII */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHermit());		/** Tarot (09) IX */
		WauzPlayerSkillExecutor.registerSkill(new SkillWheelOfFortune());	/** Tarot (10) X */
		WauzPlayerSkillExecutor.registerSkill(new SkillJustice());			/** Tarot (11) XI */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheHangedMan());		/** Tarot (12) XII */
		WauzPlayerSkillExecutor.registerSkill(new SkillDeath());			/** Tarot (13) XIII */
		WauzPlayerSkillExecutor.registerSkill(new SkillTemperance());		/** Tarot (14) XIV */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheDevil());			/** Tarot (15) XV */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheTower());			/** Tarot (16) XVI */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheStar());			/** Tarot (17) XVII */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheMoon());			/** Tarot (18) XVIII */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheSun());			/** Tarot (19) XIX */
		WauzPlayerSkillExecutor.registerSkill(new SkillJudgement());		/** Tarot (20) XX */
		WauzPlayerSkillExecutor.registerSkill(new SkillTheWorld());			/** Tarot (21) XXI */
	}
	
	/**
	 * Initializes all predefined runes.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerRunes() {
		WauzRuneInserter.registerRune(new RunePower());
		WauzRuneInserter.registerRune(new RuneKnowledge());
		WauzRuneInserter.registerRune(new RuneThorns());
		WauzRuneInserter.registerRune(new RuneHardening());
	}
	
	/**
	 * Initializes all predefined enhancements.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerEnhancements() {
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementDestruction());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementNourishment());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementConsumption());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementFerocity());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementExpertise());
		
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementNumbing());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementDurability());
		WauzEquipmentEnhancer.registerEnhancement(new EnhancementMastery());
	}
	
	/**
	 * Initializes all predefined equipment of the type Weapon / Sword.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerSwords() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.WOODEN_SWORD, " Shortsword")
				.withMainStat(1.50).withSpeedStat(1.20).withDurabilityStat(32));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.GOLDEN_SWORD, " Rapier")
				.withMainStat(1.55).withSpeedStat(1.30).withDurabilityStat(64));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.STONE_SWORD, " Longsword")
				.withMainStat(1.60).withSpeedStat(1.20).withDurabilityStat(128));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.IRON_SWORD, " Claymore")
				.withMainStat(1.65).withSpeedStat(1.20).withDurabilityStat(256));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.DIAMOND_SWORD, " Excalibur")
				.withMainStat(1.70).withSpeedStat(1.40).withDurabilityStat(512));
	}
	
	/**
	 * Initializes all predefined equipment of the type Weapon / Axe.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerAxes() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.WOODEN_AXE, " Hatchet")
				.withMainStat(1.70).withSpeedStat(0.90).withDurabilityStat(32));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.GOLDEN_AXE, " Halberd")
				.withMainStat(1.75).withSpeedStat(1.00).withDurabilityStat(64));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.STONE_AXE, " Waraxe")
				.withMainStat(1.80).withSpeedStat(0.90).withDurabilityStat(128));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.IRON_AXE, " Greataxe")
				.withMainStat(1.85).withSpeedStat(0.90).withDurabilityStat(256));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.DIAMOND_AXE, " Worldbreaker")
				.withMainStat(1.90).withSpeedStat(1.10).withDurabilityStat(512));
	}
	
	/**
	 * Initializes all predefined equipment of the type Weapon / Staff.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerStaves() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.WOODEN_HOE, " Staff")
				.withMainStat(1.30).withSpeedStat(1.50).withDurabilityStat(32));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.GOLDEN_HOE, " Pole")
				.withMainStat(1.35).withSpeedStat(1.60).withDurabilityStat(64));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.STONE_HOE, " Mace")
				.withMainStat(1.40).withSpeedStat(1.50).withDurabilityStat(128));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.IRON_HOE, " Sceptre")
				.withMainStat(1.45).withSpeedStat(1.50).withDurabilityStat(256));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.DIAMOND_HOE, " Soulreaver")
				.withMainStat(1.50).withSpeedStat(1.70).withDurabilityStat(512));
	}
	
	/**
	 * Initializes all predefined equipment of the type Weapon / Bow.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerBows() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.BOW, " Crude Bow")
				.withMainStat(0.60).withSpeedStat(1.00).withDurabilityStat(64));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.BOW, " Recurve Bow")
				.withMainStat(0.90).withSpeedStat(1.00).withDurabilityStat(128));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.BOW, " Infractem Bow")
				.withMainStat(1.20).withSpeedStat(1.00).withDurabilityStat(256));
	}
	
	/**
	 * Initializes all predefined equipment of the type Weapon / Hook.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerHooks() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.FISHING_ROD, " Grappling Hook")
				.withMainStat(0.50).withSpeedStat(1.00).withDurabilityStat(72));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.WEAPON, Material.FISHING_ROD, " Dual Hook")
				.withMainStat(0.85).withSpeedStat(1.00).withDurabilityStat(144));
	}
	
	/**
	 * Initializes all predefined equipment of the type Armor / Light.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerLightArmor() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Vest")
				.withMainStat(1.15).withDurabilityStat(64)
				.withCategory(ArmorCategory.LIGHT));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Robe")
				.withMainStat(1.30).withDurabilityStat(128)
				.withCategory(ArmorCategory.LIGHT).withLeatherDye(Color.PURPLE));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Tunic")
				.withMainStat(1.45).withDurabilityStat(192)
				.withCategory(ArmorCategory.LIGHT).withLeatherDye(Color.GREEN));
	}
	
	/**
	 * Initializes all predefined equipment of the type Armor / Medium.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerMediumArmor() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.CHAINMAIL_CHESTPLATE, " Mail")
				.withMainStat(1.30).withDurabilityStat(128)
				.withCategory(ArmorCategory.MEDIUM));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.GOLDEN_CHESTPLATE, " Guard")
				.withMainStat(1.45).withDurabilityStat(256)
				.withCategory(ArmorCategory.MEDIUM));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Hellscales")
				.withMainStat(1.60).withDurabilityStat(384)
				.withCategory(ArmorCategory.MEDIUM).withLeatherDye(Color.MAROON));
	}

	/**
	 * Initializes all predefined equipment of the type Armor / Heavy.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerHeavyArmor() {
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.IRON_CHESTPLATE, " Plate")
				.withMainStat(1.45).withDurabilityStat(256)
				.withCategory(ArmorCategory.HEAVY));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.DIAMOND_CHESTPLATE, " Herogarb")
				.withMainStat(1.60).withDurabilityStat(512)
				.withCategory(ArmorCategory.HEAVY));
		
		WauzEquipmentIdentifier.addEquipType(
				new Equipment(EquipmentType.ARMOR, Material.LEATHER_CHESTPLATE, " Dragonbones")
				.withMainStat(1.75).withDurabilityStat(768)
				.withCategory(ArmorCategory.HEAVY).withLeatherDye(Color.ORANGE));
	}

}
