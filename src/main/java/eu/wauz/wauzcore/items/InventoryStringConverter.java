package eu.wauz.wauzcore.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.menu.Backpack;
import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.oneblock.OneBlockProgression;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.ExperienceCalculator;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.skills.passive.PassiveBreath;
import eu.wauz.wauzcore.skills.passive.PassiveNutrition;
import eu.wauz.wauzcore.skills.passive.PassiveWeight;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A class to serialize and deserialize player inventories, including stats, to a character file.
 * 
 * @author Wauzmons
 */
public class InventoryStringConverter {

	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
    
	/**
	 * Serializes the player inventory and current stats to the selected character config.
	 * Includes gamemode, last played time, health, mana, level, pvp resistance and saturation.
	 * 
	 * @param player The player whose inventory should be serialized.
	 * 
	 * @see WauzPlayerData#getSelectedCharacterSlot()
	 */
    public static void saveInventory(Player player) {
    	WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
    	File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		File playerDataFile = new File(playerDirectory, playerData.getSelections().getSelectedCharacterSlot() + ".yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
		
		playerDataConfig.set("gamemode", player.getGameMode().toString());
		playerDataConfig.set("lastplayed", System.currentTimeMillis());
		
		if(WauzMode.isMMORPG(player)) {
			for(AbstractPassiveSkill passive : playerData.getSkills().getAllCachedPassives()) {
				playerDataConfig.set("skills." + passive.getPassiveName(), passive.getExp());
			}
			playerDataConfig.set("stats.current.health", playerData.getStats().getHealth());
			playerDataConfig.set("stats.current.mana", playerData.getStats().getMana());
			playerDataConfig.set("inventory.backpack", Backpack.getBackpack(player).getContents());
			playerDataConfig.set("inventory.materials", MaterialPouch.getInventory(player, "materials").getContents());
			playerDataConfig.set("inventory.questitems", MaterialPouch.getInventory(player, "questitems").getContents());
		}
		else {
			if(WauzMode.inOneBlock(player)) {
				OneBlockProgression.getPlayerOneBlock(player).save(playerDataConfig);
			}
			playerDataConfig.set("stats.current.health", player.getHealth());
			playerDataConfig.set("level", player.getLevel());
			playerDataConfig.set("exp", player.getExp() * 100F);
			playerDataConfig.set("pvp.resticks", playerData.getStats().getResistancePvP());
		}
		playerDataConfig.set("stats.current.hunger", player.getFoodLevel());
		playerDataConfig.set("stats.current.saturation", player.getSaturation());
		playerDataConfig.set("inventory.items", player.getInventory().getContents());
		try {
			playerDataConfig.save(playerDataFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * Deserializes the player inventory and current stats to the selected character config.
	 * Includes gamemode, health, mana, level, pvp resistance and saturation.
	 * 
	 * @param player The player whose inventory should be deserialized.
	 * 
	 * @see WauzPlayerData#getSelectedCharacterSlot()
	 */
    public static void loadInventory(Player player) {
    	WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
    	File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		File playerDataFile = new File(playerDirectory, playerData.getSelections().getSelectedCharacterSlot() + ".yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    	
    	player.setGameMode(GameMode.valueOf(playerDataConfig.getString("gamemode")));
    	
    	if(WauzMode.isMMORPG(player)) {
    		playerData.getSkills().cachePassive(new PassiveBreath(playerDataConfig.getLong("skills." + PassiveBreath.PASSIVE_NAME)));
    		playerData.getSkills().cachePassive(new PassiveNutrition(playerDataConfig.getLong("skills." + PassiveNutrition.PASSIVE_NAME)));
    		playerData.getSkills().cachePassive(new PassiveWeight(playerDataConfig.getLong("skills." + PassiveWeight.PASSIVE_NAME)));
    		DamageCalculator.setHealth(player, playerDataConfig.getInt("stats.current.health"));
    		playerData.getStats().setMana(playerDataConfig.getInt("stats.current.mana"));
    		playerData.getStats().setRage(0);
    		Backpack.unloadBackpack(player);
    		MaterialPouch.unloadInventory(player, "materials");
    		MaterialPouch.unloadInventory(player, "questitems");
    	}
    	else {
    		if(WauzMode.inOneBlock(player)) {
				OneBlockProgression.getPlayerOneBlock(player).load(playerDataConfig);
			}
    		player.setHealth(playerDataConfig.getInt("stats.current.health"));
    		playerData.getStats().setResistancePvP((short) playerDataConfig.getInt("pvp.resticks"));
    	}
    	player.setLevel(playerDataConfig.getInt("level"));
    	ExperienceCalculator.updateExperienceBar(player);
    	player.setFoodLevel(playerDataConfig.getInt("stats.current.hunger"));
    	player.setSaturation((float) playerDataConfig.getDouble("stats.current.saturation"));
    	player.getInventory().setContents(playerDataConfig.getList("inventory.items").toArray(new ItemStack[0]));
    }
    
    /**
	 * Serializes the given inventory into a string list.
	 * 
	 * @param inventory The inventory to save.
	 * 
	 * @return The created string list.
	 */
	public static List<String> serailizeInventory(Inventory inventory) {
		List<String> dataStrings = new ArrayList<>();
		try {
			for(int index = 0; index < inventory.getContents().length; index++) {
				ItemStack itemStack = inventory.getItem(index);
				dataStrings.add(itemStack == null ? null : WauzNmsClient.nmsStringFromItem(itemStack));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return dataStrings;
		
	}
	
	/**
	 * Deserializes the given inventory from a string list.
	 * 
	 * @param inventory The inventory to load.
	 * @param dataStrings The data to insert.
	 * 
	 * @return The filled inventory.
	 */
	public static Inventory deserializeInventory(Inventory inventory, List<?> dataStrings) {
		try {
			for(int index = 0; index < inventory.getContents().length && index < dataStrings.size(); index++) {
				String dataString = (String) dataStrings.get(index);
				inventory.setItem(index, dataString == null ? null : WauzNmsClient.nmsItemFromString(dataString));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return inventory;
	}
    
}