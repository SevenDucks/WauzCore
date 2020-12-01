package eu.wauz.wauzcore.system;

import org.bukkit.Bukkit;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.arcade.MinigameHexAGone;
import eu.wauz.wauzcore.arcade.MinigameJinxed;
import eu.wauz.wauzcore.arcade.MinigameTipToe;
import eu.wauz.wauzcore.arcade.MinigameVanHelsing;
import eu.wauz.wauzcore.commands.completion.TabCompleterEnhancements;
import eu.wauz.wauzcore.commands.completion.TabCompleterEquip;
import eu.wauz.wauzcore.commands.completion.TabCompleterGuilds;
import eu.wauz.wauzcore.commands.completion.TabCompleterInstances;
import eu.wauz.wauzcore.commands.completion.TabCompleterMenus;
import eu.wauz.wauzcore.commands.completion.TabCompleterMinigames;
import eu.wauz.wauzcore.commands.completion.TabCompleterPetAbilities;
import eu.wauz.wauzcore.commands.completion.TabCompleterPets;
import eu.wauz.wauzcore.commands.completion.TabCompleterRanks;
import eu.wauz.wauzcore.commands.completion.TabCompleterRunes;
import eu.wauz.wauzcore.commands.completion.TabCompleterSkills;
import eu.wauz.wauzcore.commands.completion.TabCompleterWaypoints;
import eu.wauz.wauzcore.commands.completion.TabCompleterWorlds;
import eu.wauz.wauzcore.items.WauzDungeonMap;
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
import eu.wauz.wauzcore.items.weapons.CustomWeaponAxe;
import eu.wauz.wauzcore.items.weapons.CustomWeaponBow;
import eu.wauz.wauzcore.items.weapons.CustomWeaponGlider;
import eu.wauz.wauzcore.items.weapons.CustomWeaponHook;
import eu.wauz.wauzcore.items.weapons.CustomWeaponLance;
import eu.wauz.wauzcore.items.weapons.CustomWeaponShield;
import eu.wauz.wauzcore.items.weapons.CustomWeaponStaff;
import eu.wauz.wauzcore.items.weapons.CustomWeaponSword;
import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.abilities.CraftingMenu;
import eu.wauz.wauzcore.menu.abilities.SkillAssignMenu;
import eu.wauz.wauzcore.menu.abilities.SkillMenu;
import eu.wauz.wauzcore.menu.abilities.TravellingMenu;
import eu.wauz.wauzcore.menu.collection.AchievementsMenu;
import eu.wauz.wauzcore.menu.collection.BestiaryMenu;
import eu.wauz.wauzcore.menu.collection.CurrencyMenu;
import eu.wauz.wauzcore.menu.collection.QuestMenu;
import eu.wauz.wauzcore.menu.social.FriendsMenu;
import eu.wauz.wauzcore.menu.social.GroupMenu;
import eu.wauz.wauzcore.menu.social.GuildOverviewMenu;
import eu.wauz.wauzcore.menu.social.MailMenu;
import eu.wauz.wauzcore.menu.social.TitleMenu;
import eu.wauz.wauzcore.menu.util.MenuRegister;
import eu.wauz.wauzcore.mobs.bestiary.WauzBestiarySpecies;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.mobs.pets.PetAbilityHatred;
import eu.wauz.wauzcore.mobs.pets.PetAbilityHide;
import eu.wauz.wauzcore.mobs.pets.PetAbilityMend;
import eu.wauz.wauzcore.mobs.pets.PetAbilityRefresh;
import eu.wauz.wauzcore.mobs.pets.PetAbilityRoar;
import eu.wauz.wauzcore.mobs.pets.PetAbilitySmith;
import eu.wauz.wauzcore.mobs.pets.PetAbilitySupply;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;
import eu.wauz.wauzcore.mobs.towers.TowerDamageBooster;
import eu.wauz.wauzcore.mobs.towers.TowerFreezingPulse;
import eu.wauz.wauzcore.mobs.towers.TowerHealthRestorer;
import eu.wauz.wauzcore.mobs.towers.TowerKnockbackCannon;
import eu.wauz.wauzcore.mobs.towers.WauzTowers;
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
import eu.wauz.wauzcore.skills.execution.SkillPlaceholder;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzcore.system.economy.WauzCurrency;
import eu.wauz.wauzcore.system.economy.WauzShop;
import eu.wauz.wauzcore.system.instances.InstanceManager;
import eu.wauz.wauzcore.system.instances.WauzInstance;
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
		WauzWaypoint.init();
		WauzEquipment.init();
		WauzCurrency.init();
		WauzAchievement.init();
		WauzShop.init();
		WauzQuest.init();
		WauzBestiarySpecies.init();
		WauzCitizen.init();
		WauzPet.init();
		WauzInstance.init();
		WauzRank.init();
		WauzTitle.init();
		WauzPlayerGuild.init();
		OnePhase.init();
		
		InstanceManager.removeInactiveInstances();
		StatisticsFetcher.calculate();
		
		registerPublicMenus();
//		registerPlayerCommands();
//		registerAdministrativeCommands();
		registerCommandCompleters();
		
		registerClasses();
		registerCustomItems();
		registerScrolls();
		registerTowers();
		registerPetAbilities();
		registerSkillgems();
		registerRunes();
		registerEnhancements();
		
		registerMinigames();
	}
	
	/**
	 * Initializes all predefined public menus for usage in commands or similar.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerPublicMenus() {
		MenuRegister.registerInventory(new AchievementsMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new BestiaryMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new CraftingMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new CurrencyMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new FriendsMenu(), WauzMode.MMORPG, WauzMode.SURVIVAL);
		MenuRegister.registerInventory(new GroupMenu(), WauzMode.MMORPG, WauzMode.SURVIVAL);
		MenuRegister.registerInventory(new GuildOverviewMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new MailMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new MaterialPouch("materials"), WauzMode.MMORPG);
		MenuRegister.registerInventory(new QuestMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new SkillAssignMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new SkillMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new TitleMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new TravellingMenu(), WauzMode.MMORPG);
		MenuRegister.registerInventory(new WauzMenu(), WauzMode.MMORPG);
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
		Bukkit.getPluginCommand("wzEnter").setTabCompleter(new TabCompleterInstances());
		Bukkit.getPluginCommand("wzEnter.dev").setTabCompleter(new TabCompleterWorlds());
		Bukkit.getPluginCommand("wzGetEquip").setTabCompleter(new TabCompleterEquip());
		Bukkit.getPluginCommand("wzGetEquip.enhanced").setTabCompleter(new TabCompleterEnhancements());
		Bukkit.getPluginCommand("wzGetPet").setTabCompleter(new TabCompleterPets());
		Bukkit.getPluginCommand("wzGetPet.ability").setTabCompleter(new TabCompleterPetAbilities());
		Bukkit.getPluginCommand("wzGetRune").setTabCompleter(new TabCompleterRunes());
		Bukkit.getPluginCommand("wzRank").setTabCompleter(new TabCompleterRanks());
		Bukkit.getPluginCommand("wzSkill").setTabCompleter(new TabCompleterSkills());
		Bukkit.getPluginCommand("wzSkill.weapon").setTabCompleter(new TabCompleterSkills());
		Bukkit.getPluginCommand("wzStart").setTabCompleter(new TabCompleterMinigames());
		Bukkit.getPluginCommand("wzTravel").setTabCompleter(new TabCompleterWaypoints());
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
		EventMapper.registerCustomItem(new WauzDungeonMap());
		EventMapper.registerCustomItem(new CustomWeaponSword());
		EventMapper.registerCustomItem(new CustomWeaponAxe());
		EventMapper.registerCustomItem(new CustomWeaponStaff());
		EventMapper.registerCustomItem(new CustomWeaponLance());
		EventMapper.registerCustomItem(new CustomWeaponShield());
		EventMapper.registerCustomItem(new CustomWeaponGlider());
		EventMapper.registerCustomItem(new CustomWeaponHook());
		EventMapper.registerCustomItem(new CustomWeaponBow());
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
	 * Initializes all predefined towers.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerTowers() {
		WauzTowers.registerTower(new TowerKnockbackCannon());
		WauzTowers.registerTower(new TowerFreezingPulse());
		WauzTowers.registerTower(new TowerDamageBooster());
		WauzTowers.registerTower(new TowerHealthRestorer());
	}
	
	/**
	 * Initializes all predefined pet abilities.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerPetAbilities() {
		WauzPetAbilities.registerAbility(new PetAbilityMend());
		WauzPetAbilities.registerAbility(new PetAbilityRoar());
		WauzPetAbilities.registerAbility(new PetAbilityHide());
		WauzPetAbilities.registerAbility(new PetAbilityRefresh());
		WauzPetAbilities.registerAbility(new PetAbilityHatred());
		WauzPetAbilities.registerAbility(new PetAbilitySmith());
		WauzPetAbilities.registerAbility(new PetAbilitySupply());
	}
	
	/**
	 * Initializes all predefined skillgems.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerSkillgems() {
		WauzPlayerSkillExecutor.registerSkill(new SkillPlaceholder(), false);
		
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
	 * Initializes all predefined arcade minigames.
	 * Called by the init() method.
	 * 
	 * @see WauzLoader#init()
	 */
	private static void registerMinigames() {
		ArcadeLobby.registerMinigame(new MinigameJinxed());
		ArcadeLobby.registerMinigame(new MinigameTipToe());
		ArcadeLobby.registerMinigame(new MinigameHexAGone());
		ArcadeLobby.registerMinigame(new MinigameVanHelsing());
	}
	
}
