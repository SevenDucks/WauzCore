package eu.wauz.wauzcore.players;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.InventorySerializer;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentHelper;
import eu.wauz.wauzcore.items.runes.RuneHardening;
import eu.wauz.wauzcore.menu.social.TabardMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.oneblock.OneBlockProgression;
import eu.wauz.wauzcore.oneblock.OnePlotManager;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.players.calc.RageCalculator;
import eu.wauz.wauzcore.players.calc.SpeedCalculator;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassStats;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectSource;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkillPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.nms.NmsMinimap;
import eu.wauz.wauzcore.system.quests.QuestProcessor;
import eu.wauz.wauzcore.system.quests.QuestSlot;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzFileUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * The character manager is used to login/out characters and manage their data.
 * 
 * @author Wauzmons
 */
public class CharacterManager {
	
	/**
	 * The current schema version of character files.
	 */
	public static final int SCHEMA_VERSION = 8;
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Logs in the character specified in the player data.
	 * Sets gamemode, level, health, mana, spawn, current location and inventory.
	 * Also shows guild motd and triggers daily rewards.
	 * 
	 * @param player The player that selected the character.
	 * @param wauzMode The mode of the character.
	 * 
	 * @see WauzPlayerDataSectionSelections#getSelectedCharacterSlot()
	 * @see InventorySerializer#loadInventory(Player)
	 * @see CharacterManager#equipCharacterItems(Player)
	 */
	public static void loginCharacter(final Player player, WauzMode wauzMode) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		Location spawn = PlayerConfigurator.getCharacterSpawn(player);
		Location destination = PlayerConfigurator.getCharacterLocation(player);
		playerData.getStats().setMaxHealth(PlayerSkillConfigurator.getHealth(player));
		if(wauzMode.equals(WauzMode.MMORPG)) {
			playerData.getStats().setMaxMana(PlayerSkillConfigurator.getMana(player));
			playerData.getStats().setMaxRage(RageCalculator.MAX_RAGE);
			playerData.getSkills().refreshUnlockedCastables();
			PlayerConfigurator.setTrackerDestination(player, spawn, "Spawn");
		}
		player.setCompassTarget(spawn);
		player.setBedSpawnLocation(spawn, true);
		player.teleport(destination);

		player.getInventory().clear();
		InventorySerializer.loadInventory(player);
		SpeedCalculator.resetWalkSpeed(player);
		SpeedCalculator.resetFlySpeed(player);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			equipCharacterItems(player);
			
			WauzRewards.earnDailyReward(player);
			
			WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
			if(guild != null) {
				player.sendMessage(
						ChatColor.WHITE + "[" + ChatColor.GREEN + guild.getGuildName() + ChatColor.WHITE + "] " +
						ChatColor.GRAY + guild.getGuildDescription());
			}
		}
	}
	
	/**
	 * Logs out the current chracter of the player.
	 * Resets gamemode, pet, group, potions, character selection, level, health, mana, saturation, inventory, spawn, location.
	 * 
	 * @param player The player that is logging out their character.
	 * 
	 * @see CharacterManager#saveCharacter(Player)
	 * @see CharacterManager#equipHubItems(Player)
	 */
	public static void logoutCharacter(final Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(WauzMode.isMMORPG(player)) {
			WauzActivePet.tryToUnsummon(player, false);
		}
		else if(WauzMode.isArcade(player)) {
			ArcadeLobby.removePlayer(player);
		}
		
		if(playerData.getSelections().isInGroup()) {
			WauzPlayerGroupPool.getGroup(playerData.getSelections().getGroupUuidString()).removePlayer(player);
			playerData.getSelections().setGroupUuidString(null);
		}
		
		playerData.getStats().getEffects().clearEffects();
		for(PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
		
		saveCharacter(player);
		
		playerData.getSelections().setSelectedCharacterSlot(null);
		playerData.getSelections().setSelectedCharacterWorld(null);
		playerData.getSelections().setSelectedCharacterClass(null);
		
		player.setGameMode(GameMode.ADVENTURE);
	    player.setExp(0);
		player.setLevel(0);

		playerData.getStats().setMaxHealth(20);
		DamageCalculator.setHealth(player, 20);
		playerData.getStats().setMaxMana(0);
		playerData.getStats().setMana(0);
		playerData.getStats().setMaxRage(0);
		playerData.getStats().setRage(0);
		playerData.getSkills().setActionBar(0);
		
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		player.getInventory().clear();
		equipHubItems(player);
		
		player.setCompassTarget(WauzCore.getHubLocation());
		player.setBedSpawnLocation(WauzCore.getHubLocation(), true);
		player.teleport(WauzCore.getHubLocation());
		SpeedCalculator.resetWalkSpeed(player);
		SpeedCalculator.resetFlySpeed(player);
	}
	
	/**
	 * Saves the character specified in the player data.
	 * 
	 * @param player The player that selected the character.
	 * 
	 * @see InventorySerializer#saveInventory(Player)
	 * @see PlayerConfigurator#setCharacterLocation(Player, Location)
	 */
	public static void saveCharacter(final Player player) {
		if(WauzPlayerDataPool.isCharacterSelected(player)) {
			InventorySerializer.saveInventory(player);
			if(StringUtils.equals(player.getWorld().getName(), PlayerConfigurator.getCharacterWorldString(player))) {
				PlayerConfigurator.setCharacterLocation(player, player.getLocation());
			}
			WauzDebugger.log(player, ChatColor.GREEN + "Saving... Character-Data saved!");
		}
		else {
			WauzDebugger.log(player, ChatColor.GREEN + "Saving... No Character-Data selected!");
		}
	}
	
	/**
	 * Creates and logs in the new character specified in the player data.
	 * Sets player data file content, gamemode, level, health, mana, saturation, start-equip and quest.
	 * Also triggers first daily reward.
	 * 
	 * @param player The player that selected the new character.
	 * @param wauzMode The mode of the new character.
	 */
	public static void createCharacter(final Player player, WauzMode wauzMode) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		String characterSlot = playerData.getSelections().getSelectedCharacterSlot();
		String characterWorldString = playerData.getSelections().getSelectedCharacterWorld();
		String characterClassString = playerData.getSelections().getSelectedCharacterClass();
		if(characterSlot == null || characterWorldString == null || characterClassString == null) {
			return;
		}
		File playerDataFile = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/" + characterSlot + ".yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		
		String characterPosition = null;
		if(characterSlot.contains("OneBlock")) {
			Location oneBlockLocation = OnePlotManager.getNextFreePlotLocation();
			oneBlockLocation.getBlock().setType(Material.OAK_LOG);
			characterPosition = (oneBlockLocation.getX() + 0.5) + " " + (oneBlockLocation.getY() + 1) + " " + (oneBlockLocation.getZ() + 0.5);
			playerDataConfig.set("oneblock.maxphase", 1);
			playerDataConfig.set("oneblock.phase", 1);
			playerDataConfig.set("oneblock.level", 1);
			playerDataConfig.set("oneblock.block", 1);
			playerDataConfig.set("oneblock.blocks", 0);
			OneBlockProgression.getPlayerOneBlock(player).load(playerDataConfig);
		}
		else {
			Location spawnLocation = core.getServer().getWorld(characterWorldString).getSpawnLocation();
			characterPosition = (spawnLocation.getX() + 0.5) + " " + spawnLocation.getY() + " " + (spawnLocation.getZ() + 0.5);
		}
		
		player.getInventory().clear();
		player.setFoodLevel(20);
		player.setSaturation(10);
		player.setExp(0);
		
		playerDataConfig.set("exists", true);
		playerDataConfig.set("schemaversion", SCHEMA_VERSION);
		playerDataConfig.set("lastplayed", System.currentTimeMillis());
		playerDataConfig.set("class", characterClassString);
		playerDataConfig.set("level", wauzMode.equals(WauzMode.MMORPG) ? 1 : 0);
		playerDataConfig.set("exp", 0);
		playerDataConfig.set("pos.world", characterWorldString);
		playerDataConfig.set("pos.spawn", characterPosition);
		playerDataConfig.set("pos.location", characterPosition);
	
		playerDataConfig.set("stats.current.health", 10);
		playerDataConfig.set("stats.current.hunger", 20);
		playerDataConfig.set("stats.current.saturation", 10);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			playerDataConfig.set("gamemode", GameMode.ADVENTURE.toString());
			player.setGameMode(GameMode.ADVENTURE);
			player.setLevel(1);
			
			playerData.getStats().setMaxHealth(10);
			playerData.getStats().setHealth(10);
			playerData.getStats().setMaxMana(ManaCalculator.MAX_MANA);
			playerData.getStats().setMana(ManaCalculator.MAX_MANA);
			playerData.getStats().setMaxRage(RageCalculator.MAX_RAGE);
			playerData.getStats().setRage(0);
			for(AbstractPassiveSkill passive : AbstractPassiveSkillPool.getPassives()) {
				playerData.getSkills().cachePassive(passive.getInstance(0));
			}
			
			WauzPlayerClass characterClass = WauzPlayerClassPool.getClass(characterClassString);
			WauzPlayerClassStats startingStats = characterClass.getStartingStats();
			
			playerDataConfig.set("tracker.coords", characterPosition);
			playerDataConfig.set("tracker.name", "Spawn");
			
			playerDataConfig.set("arrows.selected", "normal");
			playerDataConfig.set("arrows.amount.reinforced", 0);
			playerDataConfig.set("arrows.amount.fire", 0);
			playerDataConfig.set("arrows.amount.ice", 0);
			playerDataConfig.set("arrows.amount.shock", 0);
			playerDataConfig.set("arrows.amount.bomb", 0);

			playerDataConfig.set("stats.current.mana", 10);
			playerDataConfig.set("stats.points.spent", 0);
			playerDataConfig.set("stats.points.total", 0);
			playerDataConfig.set("stats.health", 10);
			playerDataConfig.set("stats.healthpts", 0);
			playerDataConfig.set("stats.trading", 100);
			playerDataConfig.set("stats.tradingpts", 0);
			playerDataConfig.set("stats.luck", 10);
			playerDataConfig.set("stats.luckpts", 0);
			playerDataConfig.set("stats.mana", 10);
			playerDataConfig.set("stats.manapts", 0);
			playerDataConfig.set("stats.strength", 100);
			playerDataConfig.set("stats.strengthpts", 0);
			playerDataConfig.set("stats.agility", 0);
			playerDataConfig.set("stats.agilitypts", 0);
			
			playerDataConfig.set("skills.sword", startingStats.getSwordSkill());
			playerDataConfig.set("skills.swordmax", startingStats.getSwordSkillMax());
			playerDataConfig.set("skills.axe", startingStats.getAxeSkill());
			playerDataConfig.set("skills.axemax", startingStats.getAxeSkillMax());
			playerDataConfig.set("skills.staff", startingStats.getStaffSkill());
			playerDataConfig.set("skills.staffmax", startingStats.getStaffSkillMax());
			
			playerDataConfig.set("skills.active.1", "none");
			playerDataConfig.set("skills.active.2", "none");
			playerDataConfig.set("skills.active.3", "none");
			playerDataConfig.set("skills.active.4", "none");
			playerDataConfig.set("skills.active.5", "none");
			playerDataConfig.set("skills.active.6", "none");
			playerDataConfig.set("skills.active.7", "none");
			playerDataConfig.set("skills.active.8", "none");
			
			playerDataConfig.set("masteries.1", 0);
			playerDataConfig.set("masteries.2", 0);
			playerDataConfig.set("masteries.3", 0);
			playerDataConfig.set("masteries.4", 0);
			
			playerDataConfig.set("currencies", new ArrayList<>());
				
			playerDataConfig.set("options.hideSpecialQuests", 0);
			playerDataConfig.set("options.hideCompletedQuests", 0);
			playerDataConfig.set("options.tabard", "No Tabard");
			playerDataConfig.set("options.title", "default");
			playerDataConfig.set("options.titlelist", new ArrayList<>());
			
			playerDataConfig.set("cooldown.reward", 0);
			
			playerDataConfig.set(QuestSlot.MAIN.getConfigKey(), "none");
			playerDataConfig.set(QuestSlot.CAMPAIGN1.getConfigKey(), "none");
			playerDataConfig.set(QuestSlot.CAMPAIGN2.getConfigKey(), "none");
			playerDataConfig.set(QuestSlot.DAILY1.getConfigKey(), "none");
			playerDataConfig.set(QuestSlot.DAILY2.getConfigKey(), "none");
			playerDataConfig.set(QuestSlot.DAILY3.getConfigKey(), "none");
			
			playerDataConfig.set("achievements.completed", 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.KILL_ENEMIES.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.IDENTIFY_ITEMS.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.USE_MANA.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.COMPLETE_QUESTS.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.CRAFT_ITEMS.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.COLLECT_PETS.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.EARN_COINS.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.PLAY_HOURS.getKey(), 0);
			playerDataConfig.set("achievements.generic." + WauzAchievementType.GAIN_LEVELS.getKey(), 1);
			playerDataConfig.set("achievements." + WauzAchievementType.COLLECT_ARTIFACTS.getKey(), "");
			playerDataConfig.set("achievements." + WauzAchievementType.COMPLETE_CAMPAIGNS.getKey(), "");
			playerDataConfig.set("achievements." + WauzAchievementType.DEFEAT_BOSSES.getKey(), "");
			playerDataConfig.set("achievements." + WauzAchievementType.EXPLORE_REGIONS.getKey(), "");
			
			playerDataConfig.set("inventory", new ArrayList<>());
			persistCharacterFile(playerDataFile, playerDataConfig);
			
			player.getInventory().addItem(InventorySerializer.serialize(characterClass.getStartingWeapon()));
			player.getInventory().addItem(InventorySerializer.serialize(WauzEquipmentHelper.getRune(new RuneHardening(), false)));
			equipCharacterItems(player);
			WauzRewards.earnDailyReward(player);
			QuestProcessor.processQuest(player, "CalamityBeneathWauzland");
		}
		else if(wauzMode.equals(WauzMode.SURVIVAL)) {
			playerDataConfig.set("gamemode", GameMode.SURVIVAL.toString());
			player.setGameMode(GameMode.SURVIVAL);
			player.setLevel(0);
			
			playerData.getStats().getEffects().addEffect(WauzPlayerEffectType.PVP_PROTECTION, WauzPlayerEffectSource.ITEM, 1800);
			persistCharacterFile(playerDataFile, playerDataConfig);
		}
		
		Location spawn = PlayerConfigurator.getCharacterSpawn(player);
		player.setCompassTarget(spawn);
		player.setBedSpawnLocation(spawn, true);
		player.teleport(spawn);
		SpeedCalculator.resetWalkSpeed(player);
		SpeedCalculator.resetFlySpeed(player);
	}

	/**
	 * Saves a newly created character config to a file.
	 * 
	 * @param playerDataFile The file the config should be saved in.
	 * @param playerDataConfig The config to save.
	 */
	private static void persistCharacterFile(File playerDataFile, FileConfiguration playerDataConfig) {
		try {
			playerDataConfig.save(playerDataFile);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the given character permanently.
	 * 
	 * @param player The player who owns the character.
	 * @param characterSlot The slot of the character.
	 */
	public static void deleteCharacter(Player player, String characterSlot) {
		String basePath = core.getDataFolder().getAbsolutePath() + "/PlayerData/" + player.getUniqueId() + "/" + characterSlot;
		new File(basePath + ".yml").delete();
		new File(basePath + "-bestiary.yml").delete();
		WauzFileUtils.removeFilesRecursive(new File(basePath + "-quests"));
		WauzFileUtils.removeFilesRecursive(new File(basePath + "-relations"));
		WauzDebugger.log(player, "Deleted Character: " + characterSlot);
	}
	
	/**
	 * Equips a player with default hub items.
	 * Currently only the mode selection menu.
	 * 
	 * @param player The player that should receive the items.
	 * 
	 * @see MenuUtils#setMainMenuOpener(org.bukkit.inventory.Inventory, int)
	 */
	public static void equipHubItems(Player player) {
		player.getInventory().clear();
		MenuUtils.setMainMenuOpener(player.getInventory(), 4);
	}
	
	/**
	 * Equips a player with default mmorpg character items.
	 * Contains minimap, quest tracker, main menu and selected tabard.
	 * Additionally trashcan, materials and backpack are placed inside the inventory.
	 * 
	 * @param player The player that should receive the items.
	 * 
	 * @see MenuUtils#setMainMenuOpener(org.bukkit.inventory.Inventory, int)
	 * @see TabardMenu#equipSelectedTabard(Player)
	 */
	public static void equipCharacterItems(Player player) {
		ItemStack mapItemStack = new ItemStack(Material.FILLED_MAP);
		ItemMeta mapItemMeta = mapItemStack.getItemMeta();
		Components.displayName(mapItemMeta, ChatColor.DARK_AQUA + "Map of the Grand Explorer");
		mapItemStack.setItemMeta(mapItemMeta);
		player.getInventory().setItem(6, mapItemStack);
		NmsMinimap.init(player);
		
		ItemStack trackerItemStack = new ItemStack(Material.COMPASS);
		ItemMeta trackerItemMeta = trackerItemStack.getItemMeta();
		Components.displayName(trackerItemMeta, ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getTrackerDestinationName(player));
		trackerItemStack.setItemMeta(trackerItemMeta);
		player.getInventory().setItem(7, trackerItemStack);
		MenuUtils.setMainMenuOpener(player.getInventory(), 8);
		
		MenuUtils.setTrashcan(player.getInventory(), 35);
		MenuUtils.setMaterials(player.getInventory(), 26);
		MenuUtils.setBackpack(player.getInventory(), 17);
		
		TabardMenu.equipSelectedTabard(player);
	}

}
