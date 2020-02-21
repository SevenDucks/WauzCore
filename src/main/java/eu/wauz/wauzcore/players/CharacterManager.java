package eu.wauz.wauzcore.players;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.items.InventoryStringConverter;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.QuestBuilder;
import eu.wauz.wauzcore.menu.TabardMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.nms.WauzNmsMinimap;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * The character manager is used to login/out characters and manage their data.
 * 
 * @author Wauzmons
 */
public class CharacterManager {
	
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
	 * @see WauzPlayerData#getSelectedCharacterSlot()
	 * @see InventoryStringConverter#loadInventory(Player)
	 * @see CharacterManager#equipCharacterItems(Player)
	 */
	public static void loginCharacter(final Player player, WauzMode wauzMode) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}

		player.setGameMode(wauzMode.equals(WauzMode.SURVIVAL) ? GameMode.SURVIVAL : GameMode.ADVENTURE);
		player.setExp((float) (PlayerConfigurator.getCharacterExperience(player) / 100F));
		player.setLevel(PlayerConfigurator.getCharacterLevel(player));
		
		playerData.setMaxHealth(PlayerPassiveSkillConfigurator.getHealth(player));
		if(wauzMode.equals(WauzMode.MMORPG)) {
			playerData.setMaxMana(PlayerPassiveSkillConfigurator.getMana(player));
		}
		
		Location spawn = PlayerConfigurator.getCharacterSpawn(player);
		Location destination = PlayerConfigurator.getCharacterLocation(player);
		if(wauzMode.equals(WauzMode.MMORPG)) {
			PlayerConfigurator.setTrackerDestination(player, spawn, "Spawn");
		}
		
		player.setCompassTarget(spawn);
		player.setBedSpawnLocation(spawn, true);
		player.teleport(destination);

		player.getInventory().clear();
		InventoryStringConverter.loadInventory(player);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			equipCharacterItems(player);
			
			try {
				WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
				if(guild != null)
					player.sendMessage(
							ChatColor.WHITE + "[" + ChatColor.GREEN + guild.getGuildName() + ChatColor.WHITE + "] " +
							ChatColor.GRAY + guild.getGuildDescription());
				WauzRewards.daily(player);
			}
			catch(Exception e) {
				e.printStackTrace();
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
		player.setGameMode(GameMode.ADVENTURE);
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(WauzMode.isMMORPG(player)) {
			PetOverviewMenu.unsummon(player);
		}
		
		if(playerData.isInGroup()) {
			WauzPlayerGroupPool.getGroup(playerData.getGroupUuidString()).removePlayer(player);
			playerData.setGroupUuidString(null);
		}
		
		for(PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
		
		saveCharacter(player);
		
		playerData.setSelectedCharacterSlot(null);
		playerData.setSelectedCharacterWorld(null);
		playerData.setSelectedCharacterRace(null);
		
	    player.setExp(0);
		player.setLevel(0);

		playerData.setMaxHealth(20);
		DamageCalculator.setHealth(player, 20);
		playerData.setMaxMana(0);
		playerData.setMana(0);
		
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		player.getInventory().clear();
		equipHubItems(player);
		
		player.setCompassTarget(WauzCore.getHubLocation());
		player.setBedSpawnLocation(WauzCore.getHubLocation(), true);
		player.teleport(WauzCore.getHubLocation());
	}
	
	/**
	 * Saves the character specified in the player data.
	 * 
	 * @param player The player that selected the character.
	 * 
	 * @see InventoryStringConverter#saveInventory(Player)
	 * @see PlayerConfigurator#setCharacterLocation(Player, Location)
	 */
	public static void saveCharacter(final Player player) {
		if(WauzPlayerDataPool.isCharacterSelected(player)) {
			InventoryStringConverter.saveInventory(player);
			if(!StringUtils.startsWith(player.getWorld().getName(), "WzInstance")) {
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
		
		String characterSlot = null;
		if(playerData.getSelectedCharacterSlot() != null) {
			characterSlot = playerData.getSelectedCharacterSlot();
		}
		else {
			return;
		}
		
		String characterWorld = null;
		if(playerData.getSelectedCharacterWorld() != null) {
			characterWorld = playerData.getSelectedCharacterWorld();
		}
		else {
			return;
		}
		
		String characterRaceAndClass = null;
		boolean classNephilim;
		boolean classCrusader;
		boolean classAssassin;
		if(playerData.getSelectedCharacterRace() != null) {
			characterRaceAndClass = playerData.getSelectedCharacterRace();
			classNephilim = characterRaceAndClass.contains("Nephilim");
			classCrusader = characterRaceAndClass.contains("Crusader");
			classAssassin = characterRaceAndClass.contains("Assassin");
		}
		else {
			return;
		}
		
		File playerDataFile = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/" + characterSlot + ".yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		
		Location spawnLocation = core.getServer().getWorld(characterWorld).getSpawnLocation();
		String characterPosition = (spawnLocation.getX() + 0.5) + " " + spawnLocation.getY() + " " + (spawnLocation.getZ() + 0.5);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			player.setGameMode(GameMode.ADVENTURE);
			player.setExp(0);
			player.setLevel(1);
			
			playerData.setMaxHealth(10);
			playerData.setHealth(10);
			playerData.setMaxMana(10);
			playerData.setMana(10);
			
			player.setFoodLevel(20);
			player.setSaturation(10);
		}
		else if(wauzMode.equals(WauzMode.SURVIVAL)) {
			player.setGameMode(GameMode.SURVIVAL);
			player.setExp(0);
			player.setLevel(0);
			
			player.setFoodLevel(20);
			player.setSaturation(10);
		}
		
// Create new Player-Config
		
		playerDataConfig.set("exists", true);
		playerDataConfig.set("lastplayed", System.currentTimeMillis());
		playerDataConfig.set("race", characterRaceAndClass);
		playerDataConfig.set("level", wauzMode.equals(WauzMode.MMORPG) ? 1 : 0);
		playerDataConfig.set("pos.world", characterWorld);
		playerDataConfig.set("pos.spawn", characterPosition);
		playerDataConfig.set("pos.location", characterPosition);
	
		playerDataConfig.set("stats.current.health", 10);
		playerDataConfig.set("stats.current.hunger", 20);
		playerDataConfig.set("stats.current.saturation", 10);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			playerDataConfig.set("stats.current.mana", 10);
			
			playerDataConfig.set("tracker.coords", characterPosition);
			playerDataConfig.set("tracker.name", "Spawn");
			
			playerDataConfig.set("arrows.selected", "normal");
			playerDataConfig.set("arrows.amount.reinforced", 0);
			playerDataConfig.set("arrows.amount.fire", 0);
			playerDataConfig.set("arrows.amount.ice", 0);
			playerDataConfig.set("arrows.amount.shock", 0);
			playerDataConfig.set("arrows.amount.bomb", 0);

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
			
			playerDataConfig.set("skills.crafting", 1);
			playerDataConfig.set("skills.sword", classAssassin ? 135000 : 100000);
			playerDataConfig.set("skills.swordmax", classAssassin ? 250000 : 200000);
			playerDataConfig.set("skills.axe", classCrusader ? 135000 : 100000);
			playerDataConfig.set("skills.axemax", classCrusader ? 250000 : 200000);
			playerDataConfig.set("skills.staff", classNephilim ? 135000 : 100000);
			playerDataConfig.set("skills.staffmax", classNephilim ? 250000 : 200000);
				
			playerDataConfig.set("reput.exp", 0);
			playerDataConfig.set("reput.cow", 0);
			playerDataConfig.set("reput.souls", 0);
			playerDataConfig.set("reput.wauzland", 0);
			playerDataConfig.set("reput.empire", 0);
			playerDataConfig.set("reput.legion", 0);
			
			playerDataConfig.set("options.hideSpecialQuests", 0);
			playerDataConfig.set("options.hideCompletedQuests", 0);
			playerDataConfig.set("options.tabard", "No Tabard");
			
			playerDataConfig.set("cooldown.reward", 0);
			
			playerDataConfig.set("pets.active.id", "none");
			playerDataConfig.set("pets.active.slot", -1);
			
			playerDataConfig.set("pets.slot0.type", "none");
			playerDataConfig.set("pets.slot1.type", "none");
			playerDataConfig.set("pets.slot2.type", "none");
			playerDataConfig.set("pets.slot3.type", "none");
			playerDataConfig.set("pets.slot4.type", "none");
			playerDataConfig.set("pets.slot6.type", "none");
			playerDataConfig.set("pets.slot8.type", "none");
			playerDataConfig.set("pets.egg.time", 0);
			
			playerDataConfig.set("quest.running.main", "none");
			playerDataConfig.set("quest.running.campaign1", "none");
			playerDataConfig.set("quest.running.campaign2", "none");
			playerDataConfig.set("quest.running.daily1", "none");
			playerDataConfig.set("quest.running.daily2", "none");
			playerDataConfig.set("quest.running.daily3", "none");
			
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
		}
		else if(wauzMode.equals(WauzMode.SURVIVAL)) {
			playerDataConfig.set("pvp.resticks", 720);
			playerData.setResistancePvP((short) 720);
		}
		
		try {
			playerDataConfig.save(playerDataFile);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		player.getInventory().clear();
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			ItemStack starterWeapon = null;
			if(classNephilim) {
				starterWeapon = WauzEquipment.getNephilimStarterWeapon();
			}
			else if(classCrusader) {
				starterWeapon = WauzEquipment.getCrusaderStarterWeapon();
			}
			else if(classAssassin) {
				starterWeapon = WauzEquipment.getAssassinStarterWeapon();
			}
			player.getInventory().addItem(starterWeapon);
			player.getInventory().addItem(WauzEquipment.getStarterRune());
			equipCharacterItems(player);
			
			if(characterWorld.equals("Wauzland")) {
				QuestBuilder.accept(player, "Yamir", "Yamir");
			}
			try {
				WauzRewards.daily(player);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		Location spawn = PlayerConfigurator.getCharacterSpawn(player);
		
		player.setCompassTarget(spawn);
		player.setBedSpawnLocation(spawn, true);
		player.teleport(spawn);
	}
	
	/**
	 * Equips a player with default hub items.
	 * Currently only the mode selection menu.
	 * 
	 * @param player The player that should receive the items.
	 */
	public static void equipHubItems(Player player) {
		ItemStack mainMenuItemStack = new ItemStack(Material.NETHER_STAR);
		ItemMeta mainMenuItemMeta = mainMenuItemStack.getItemMeta();
		mainMenuItemMeta.setDisplayName(ChatColor.GOLD + "Open Menu");
		mainMenuItemStack.setItemMeta(mainMenuItemMeta);
		player.getInventory().clear();
		player.getInventory().setItem(4, mainMenuItemStack);
	}
	
	/**
	 * Equips a player with default mmorpg character items.
	 * Contains quest tracker, main menu, minimap, trashcan and selected tabard.
	 * 
	 * @param player The player that should receive the items.
	 * 
	 * @see MenuUtils#setTrashcan(org.bukkit.inventory.Inventory, int...)
	 * @see TabardMenu#equipSelectedTabard(Player)
	 */
	public static void equipCharacterItems(Player player) {
		ItemStack trackerItemStack = new ItemStack(Material.COMPASS);
		ItemMeta trackerItemMeta = trackerItemStack.getItemMeta();
		trackerItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Tracked: " + PlayerConfigurator.getTrackerDestinationName(player));
		trackerItemStack.setItemMeta(trackerItemMeta);
		player.getInventory().setItem(7, trackerItemStack);

		ItemStack mainMenuItemStack = new ItemStack(Material.NETHER_STAR);
		ItemMeta mainMenuItemMeta = mainMenuItemStack.getItemMeta();
		mainMenuItemMeta.setDisplayName(ChatColor.GOLD + "Open Menu");
		mainMenuItemStack.setItemMeta(mainMenuItemMeta);
		player.getInventory().setItem(8, mainMenuItemStack);
		
		ItemStack mapItemStack = new ItemStack(Material.FILLED_MAP);
		ItemMeta mapItemMeta = mapItemStack.getItemMeta();
		mapItemMeta.setDisplayName(ChatColor.GOLD + "Explorer Map");
		mapItemStack.setItemMeta(mapItemMeta);
		player.getEquipment().setItemInOffHand(mapItemStack);
		WauzNmsMinimap.init(player);
		
		MenuUtils.setTrashcan(player.getInventory(), 35);
		
		TabardMenu.equipSelectedTabard(player);
	}

}
