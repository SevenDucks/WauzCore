package eu.wauz.wauzcore;

import org.bukkit.Bukkit;

import eu.wauz.wauzcore.commands.CmdApply;
import eu.wauz.wauzcore.commands.CmdDesc;
import eu.wauz.wauzcore.commands.CmdFriend;
import eu.wauz.wauzcore.commands.CmdGld;
import eu.wauz.wauzcore.commands.CmdGroup;
import eu.wauz.wauzcore.commands.CmdGrp;
import eu.wauz.wauzcore.commands.CmdGuild;
import eu.wauz.wauzcore.commands.CmdHome;
import eu.wauz.wauzcore.commands.CmdHub;
import eu.wauz.wauzcore.commands.CmdMenu;
import eu.wauz.wauzcore.commands.CmdMotd;
import eu.wauz.wauzcore.commands.CmdRoll;
import eu.wauz.wauzcore.commands.CmdSend;
import eu.wauz.wauzcore.commands.CmdSendCoins;
import eu.wauz.wauzcore.commands.CmdSendItem;
import eu.wauz.wauzcore.commands.CmdSethome;
import eu.wauz.wauzcore.commands.CmdSit;
import eu.wauz.wauzcore.commands.CmdSpawn;
import eu.wauz.wauzcore.commands.CmdTip;
import eu.wauz.wauzcore.commands.CmdTrade;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebug;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugAttack;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugBuilding;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugCrafting;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugDefense;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugFlying;
import eu.wauz.wauzcore.commands.administrative.CmdWzDebugMagic;
import eu.wauz.wauzcore.commands.administrative.CmdWzEnter;
import eu.wauz.wauzcore.commands.administrative.CmdWzEnterDev;
import eu.wauz.wauzcore.commands.administrative.CmdWzExp;
import eu.wauz.wauzcore.commands.administrative.CmdWzGetEquip;
import eu.wauz.wauzcore.commands.administrative.CmdWzGetEquipEnhanced;
import eu.wauz.wauzcore.commands.administrative.CmdWzGetPet;
import eu.wauz.wauzcore.commands.administrative.CmdWzGetRune;
import eu.wauz.wauzcore.commands.administrative.CmdWzHeal;
import eu.wauz.wauzcore.commands.administrative.CmdWzKey;
import eu.wauz.wauzcore.commands.administrative.CmdWzLeave;
import eu.wauz.wauzcore.commands.administrative.CmdWzRank;
import eu.wauz.wauzcore.commands.administrative.CmdWzRepair;
import eu.wauz.wauzcore.commands.administrative.CmdWzSkill;
import eu.wauz.wauzcore.commands.administrative.CmdWzSkillWeapon;
import eu.wauz.wauzcore.commands.administrative.CmdWzSystem;
import eu.wauz.wauzcore.commands.administrative.CmdWzTravel;
import eu.wauz.wauzcore.commands.administrative.CmdWzTravelEvent;
import eu.wauz.wauzcore.commands.completion.TabCompleterEnhancements;
import eu.wauz.wauzcore.commands.completion.TabCompleterEquip;
import eu.wauz.wauzcore.commands.completion.TabCompleterGuilds;
import eu.wauz.wauzcore.commands.completion.TabCompleterMenus;
import eu.wauz.wauzcore.commands.completion.TabCompleterRanks;
import eu.wauz.wauzcore.commands.completion.TabCompleterRunes;
import eu.wauz.wauzcore.commands.completion.TabCompleterSkills;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementDurability;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementMastery;
import eu.wauz.wauzcore.items.enhancements.armor.EnhancementNumbing;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementConsumption;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementDestruction;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementExpertise;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementFerocity;
import eu.wauz.wauzcore.items.enhancements.weapon.EnhancementNourishment;
import eu.wauz.wauzcore.items.runes.RuneHardening;
import eu.wauz.wauzcore.items.runes.RuneKnowledge;
import eu.wauz.wauzcore.items.runes.RunePower;
import eu.wauz.wauzcore.items.runes.RuneThorns;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.items.scrolls.ScrollFortune;
import eu.wauz.wauzcore.items.scrolls.ScrollGenericRune;
import eu.wauz.wauzcore.items.scrolls.ScrollGenericSkillgem;
import eu.wauz.wauzcore.items.scrolls.ScrollRegret;
import eu.wauz.wauzcore.items.scrolls.ScrollToughness;
import eu.wauz.wauzcore.items.scrolls.ScrollWisdom;
import eu.wauz.wauzcore.items.scrolls.WauzScrolls;
import eu.wauz.wauzcore.items.weapons.CustomWeaponBow;
import eu.wauz.wauzcore.items.weapons.CustomWeaponGlider;
import eu.wauz.wauzcore.items.weapons.CustomWeaponLance;
import eu.wauz.wauzcore.items.weapons.CustomWeaponShield;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.abilities.CraftingMenu;
import eu.wauz.wauzcore.menu.abilities.SkillMenu;
import eu.wauz.wauzcore.menu.abilities.TravellingMenu;
import eu.wauz.wauzcore.menu.collection.AchievementsMenu;
import eu.wauz.wauzcore.menu.collection.CurrencyMenu;
import eu.wauz.wauzcore.menu.collection.PetOverviewMenu;
import eu.wauz.wauzcore.menu.collection.QuestMenu;
import eu.wauz.wauzcore.menu.social.FriendsMenu;
import eu.wauz.wauzcore.menu.social.GroupMenu;
import eu.wauz.wauzcore.menu.social.GuildOverviewMenu;
import eu.wauz.wauzcore.menu.social.MailMenu;
import eu.wauz.wauzcore.menu.social.TitleMenu;
import eu.wauz.wauzcore.menu.util.MenuRegister;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.oneblock.OnePhase;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.players.classes.ClassCleric;
import eu.wauz.wauzcore.players.classes.ClassMage;
import eu.wauz.wauzcore.players.classes.ClassRogue;
import eu.wauz.wauzcore.players.classes.ClassWarrior;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
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
import eu.wauz.wauzcore.system.EventMapper;
import eu.wauz.wauzcore.system.InstanceManager;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.WauzTitle;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzcore.system.economy.WauzCurrency;
import eu.wauz.wauzcore.system.economy.WauzShop;
import eu.wauz.wauzcore.system.quests.WauzQuest;
import eu.wauz.wauzcore.system.util.WauzMode;

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
		WauzEquipment.init();
		WauzCurrency.init();
		WauzAchievement.init();
		WauzShop.init();
		WauzQuest.init();
		WauzCitizen.init();
		WauzRank.init();
		WauzTitle.init();
		WauzPlayerGuild.init();
		OnePhase.init();
		
		InstanceManager.removeInactiveInstances();
		StatisticsFetcher.calculate();
		
		registerPublicMenus();
		registerPlayerCommands();
		registerAdministrativeCommands();
		registerCommandCompleters();
		
		registerClasses();
		registerCustomItems();
		registerScrolls();
		registerSkillgems();
		registerRunes();
		registerEnhancements();
	}
	
	/**
	 * Initializes all predefined public menus for usage in commands or similar.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerPublicMenus() {
		MenuRegister.registerInventory(new AchievementsMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new CraftingMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new CurrencyMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new FriendsMenu(), WauzMode.MMORPG, WauzMode.SURVIVAL);
		MenuRegister.registerInventory(new GroupMenu(), WauzMode.MMORPG, WauzMode.SURVIVAL);
		MenuRegister.registerInventory(new GuildOverviewMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new MailMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new PetOverviewMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new QuestMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new SkillMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new TitleMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new TravellingMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new WauzMenu(), WauzMode.MMORPG);
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
		WauzCommandExecutor.registerCommand(new CmdFriend());
		WauzCommandExecutor.registerCommand(new CmdGld());
		WauzCommandExecutor.registerCommand(new CmdGroup());
		WauzCommandExecutor.registerCommand(new CmdGrp());
		WauzCommandExecutor.registerCommand(new CmdGuild());
		WauzCommandExecutor.registerCommand(new CmdHome());
		WauzCommandExecutor.registerCommand(new CmdHub());
		WauzCommandExecutor.registerCommand(new CmdMenu());
		WauzCommandExecutor.registerCommand(new CmdMotd());
		WauzCommandExecutor.registerCommand(new CmdRoll());
		WauzCommandExecutor.registerCommand(new CmdSend());
		WauzCommandExecutor.registerCommand(new CmdSendCoins());
		WauzCommandExecutor.registerCommand(new CmdSendItem());
		WauzCommandExecutor.registerCommand(new CmdSethome());
		WauzCommandExecutor.registerCommand(new CmdSpawn());
		WauzCommandExecutor.registerCommand(new CmdSit());
		WauzCommandExecutor.registerCommand(new CmdTip());
		WauzCommandExecutor.registerCommand(new CmdTrade());
	}
	
	/**
	 * Initializes all predefined admin and system commands.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerAdministrativeCommands() {
		WauzCommandExecutor.registerCommand(new CmdWzDebug());
		WauzCommandExecutor.registerCommand(new CmdWzDebugAttack());
		WauzCommandExecutor.registerCommand(new CmdWzDebugBuilding());
		WauzCommandExecutor.registerCommand(new CmdWzDebugCrafting());
		WauzCommandExecutor.registerCommand(new CmdWzDebugDefense());
		WauzCommandExecutor.registerCommand(new CmdWzDebugFlying());
		WauzCommandExecutor.registerCommand(new CmdWzDebugMagic());
		WauzCommandExecutor.registerCommand(new CmdWzEnter());
		WauzCommandExecutor.registerCommand(new CmdWzEnterDev());
		WauzCommandExecutor.registerCommand(new CmdWzExp());
		WauzCommandExecutor.registerCommand(new CmdWzGetEquip());
		WauzCommandExecutor.registerCommand(new CmdWzGetEquipEnhanced());
		WauzCommandExecutor.registerCommand(new CmdWzGetPet());
		WauzCommandExecutor.registerCommand(new CmdWzGetRune());
		WauzCommandExecutor.registerCommand(new CmdWzHeal());
		WauzCommandExecutor.registerCommand(new CmdWzKey());
		WauzCommandExecutor.registerCommand(new CmdWzLeave());
		WauzCommandExecutor.registerCommand(new CmdWzRank());
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
		Bukkit.getPluginCommand("wzGetEquip").setTabCompleter(new TabCompleterEquip());
		Bukkit.getPluginCommand("wzGetEquip.enhanced").setTabCompleter(new TabCompleterEnhancements());
		Bukkit.getPluginCommand("wzGetRune").setTabCompleter(new TabCompleterRunes());
		Bukkit.getPluginCommand("wzRank").setTabCompleter(new TabCompleterRanks());
		Bukkit.getPluginCommand("wzSkill").setTabCompleter(new TabCompleterSkills());
		Bukkit.getPluginCommand("wzSkill.weapon").setTabCompleter(new TabCompleterSkills());
	}
	
	/**
	 * Initializes all predefined player classes.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerClasses() {
		WauzPlayerClassPool.registerClass(new ClassWarrior());
		WauzPlayerClassPool.registerClass(new ClassRogue());
		WauzPlayerClassPool.registerClass(new ClassMage());
		WauzPlayerClassPool.registerClass(new ClassCleric());
	}
	
	/**
	 * Initializes all predefined custom items.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerCustomItems() {
		EventMapper.registerCustomItem(new WauzMenu());
		EventMapper.registerCustomItem(new WauzScrolls());
		EventMapper.registerCustomItem(new CustomWeaponBow());
		EventMapper.registerCustomItem(new CustomWeaponLance());
		EventMapper.registerCustomItem(new CustomWeaponShield());
		EventMapper.registerCustomItem(new CustomWeaponGlider());
	}
	
	/**
	 * Initializes all predefined scrolls.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerScrolls() {
		WauzScrolls.registerScroll(new ScrollWisdom());
		WauzScrolls.registerScroll(new ScrollFortune());
		WauzScrolls.registerScroll(new ScrollToughness());
		WauzScrolls.registerScroll(new ScrollRegret());
		WauzScrolls.registerScroll(new ScrollGenericRune());
		WauzScrolls.registerScroll(new ScrollGenericSkillgem());
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
	
}
