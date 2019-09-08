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
import eu.wauz.wauzcore.items.InventoryStringConverter;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.QuestBuilder;
import eu.wauz.wauzcore.menu.TabardMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ManaCalculator;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.nms.WauzNmsMinimap;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class CharacterManager {
	
	private static WauzCore core = WauzCore.getInstance();
	
	public static void loginCharacter(final Player player, WauzMode wauzMode) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;

		player.setGameMode(wauzMode.equals(WauzMode.SURVIVAL) ? GameMode.SURVIVAL : GameMode.ADVENTURE);
		player.setExp((float) (PlayerConfigurator.getCharacterExperience(player) / 100F));
		player.setLevel(PlayerConfigurator.getCharacterLevel(player));
		
		playerData.setMaxHealth(PlayerPassiveSkillConfigurator.getHealth(player));
		if(wauzMode.equals(WauzMode.MMORPG))
			playerData.setMaxMana(PlayerPassiveSkillConfigurator.getMana(player));
		
		Location spawn = PlayerConfigurator.getCharacterSpawn(player);
		Location destination = PlayerConfigurator.getCharacterLocation(player);
		if(wauzMode.equals(WauzMode.MMORPG))
			PlayerConfigurator.setDungeonItemTrackerDestination(player, spawn, "Spawn");
		
		player.setCompassTarget(spawn);
		player.setBedSpawnLocation(spawn, true);
		player.teleport(destination);

		player.getInventory().clear();
		InventoryStringConverter.loadInventory(player);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			ManaCalculator.updateManaItem(player);
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
		
		player.setCompassTarget(WauzCore.getHubLocation());
		player.setBedSpawnLocation(WauzCore.getHubLocation(), true);
		player.teleport(WauzCore.getHubLocation());
	}
	
	public static void saveCharacter(final Player player) {
		if(WauzPlayerDataPool.isCharacterSelected(player)) {
			InventoryStringConverter.saveInventory(player);
			
			if(!StringUtils.startsWith(player.getWorld().getName(), "WzInstance"))
				PlayerConfigurator.setCharacterLocation(player, player.getLocation());
			
			WauzDebugger.log(player, ChatColor.GREEN + "Saving... Character-Data saved!");
		}
		else {
			WauzDebugger.log(player, ChatColor.GREEN + "Saving... No Character-Data selected!");
		}
	}
	
	public static void createCharacter(final Player player, WauzMode wauzMode) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		String characterSlot = null;
		if(playerData.getSelectedCharacterSlot() != null)
			characterSlot = playerData.getSelectedCharacterSlot();
		else
			return;
		
		String characterWorld = null;
		if(playerData.getSelectedCharacterWorld() != null)
			characterWorld = playerData.getSelectedCharacterWorld();
		else
			return;
		
		String characterRace = null;
		if(playerData.getSelectedCharacterRace() != null)
			characterRace = playerData.getSelectedCharacterRace();
		else
			return;
		
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
		playerDataConfig.set("race", characterRace);
		playerDataConfig.set("level", wauzMode.equals(WauzMode.MMORPG) ? 1 : 0);
		playerDataConfig.set("pos.world", characterWorld);
		playerDataConfig.set("pos.spawn", characterPosition);
		playerDataConfig.set("pos.location", characterPosition);
	
		playerDataConfig.set("stats.current.health", 10);
		playerDataConfig.set("stats.current.hunger", 20);
		playerDataConfig.set("stats.current.saturation", 10);
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			playerDataConfig.set("stats.current.mana", 10);
			
			playerDataConfig.set("ditem.coll", 4);
			playerDataConfig.set("ditem.hook", "unobtained");
			playerDataConfig.set("ditem.bomb", "unobtained");
			playerDataConfig.set("ditem.trod", "unobtained");
			playerDataConfig.set("ditem.glid", "unobtained");
			playerDataConfig.set("ditem.tracker.coords", characterPosition);
			playerDataConfig.set("ditem.tracker.name", "Spawn");
			
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
			playerDataConfig.set("skills.sword", 100000);
			playerDataConfig.set("skills.swordmax", 200000);
			playerDataConfig.set("skills.axe", 100000);
			playerDataConfig.set("skills.axemax", 200000);
			playerDataConfig.set("skills.staff", 100000);
			playerDataConfig.set("skills.staffmax", 200000);
				
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
			
			playerDataConfig.set("quest.completed", 0);
			playerDataConfig.set("quest.running.main", "none");
			playerDataConfig.set("quest.running.campaign1", "none");
			playerDataConfig.set("quest.running.campaign2", "none");
			playerDataConfig.set("quest.running.daily1", "none");
			playerDataConfig.set("quest.running.daily2", "none");
			playerDataConfig.set("quest.running.daily3", "none");
		}
		else if(wauzMode.equals(WauzMode.SURVIVAL)) {
			playerDataConfig.set("pvp.resticks", 720);
			playerData.setResistancePvsP((short) 720);
		}
		
		try {
			playerDataConfig.save(playerDataFile);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		player.getInventory().clear();
		
		if(wauzMode.equals(WauzMode.MMORPG)) {
			equipCharacterItems(player);
			
			if(characterWorld.equals("Wauzland")) {
				QuestBuilder.accept(player, "Yamir");
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
	
	private static void equipCharacterItems(Player player) {
		ItemStack clockItemStack = new ItemStack(Material.CLOCK);
		ItemMeta clockItemMeta = clockItemStack.getItemMeta();
		clockItemMeta.setDisplayName(ChatColor.RED + "No Item Equipped");
		clockItemStack.setItemMeta(clockItemMeta);
		player.getInventory().setItem(7, clockItemStack);

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
