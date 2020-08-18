package eu.wauz.wauzcore.system.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * An util class for getting and setting cooldowns.
 * 
 * @author Wauzmons
 */
public class Cooldown {
	
	/**
	 * Returns the entity interaction cooldown from the player data.
	 * Cooldown lasts 1 second. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerEntityInteraction(Player player) {
		return getAndUpdateCooldownFromCache(player, "ENTITY_INTERACT", 1000L);
	}
	
	/**
	 * Returns the weapon usage cooldown from the player data.
	 * Cooldown lasts depending on weapon. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerWeaponUse(Player player) {
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.hasDisplayName(itemStack)) {
			return true;
		}
		WauzDebugger.log(player, "Reset Attack Cooldown");
		String itemName = itemStack.getItemMeta().getDisplayName();
		long cooldown = (long) (player.getCooldownPeriod() * 50);
		return getAndUpdateCooldownFromCache(player, "WEAPON_USE :: " + itemName, cooldown, true);
	}
	
	/**
	 * Returns the weapon skill usage cooldown from the player data.
	 * Cooldown lasts 2.8 seconds for each weapon skill. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * @param skillName
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerWeaponSkillUse(Player player, String skillName) {
		return getAndUpdateCooldownFromCache(player, "WEAPON_SKILL_USE :: " + skillName, 2800L);
	}
	
	/**
	 * Returns the skill usage cooldown from the player data.
	 * Cooldown lasts 1 second. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerSkillUse(Player player) {
		return getAndUpdateCooldownFromCache(player, "SKILL_USE", 1000L);
	}
	
	/**
	 * Returns the quick slot usage cooldown from the player data.
	 * Cooldown lasts 0.1 seconds. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerQuickSlotUse(Player player) {
		return getAndUpdateCooldownFromCache(player, "QUICK_SLOT_USE", 100L, false);
	}
	
	/**
	 * Returns the food consumtion cooldown from the player data.
	 * Cooldown lasts 1 second. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerFoodConsume(Player player) {
		return getAndUpdateCooldownFromCache(player, "FOOD_CONSUME", 1000L);
	}
	
	/**
	 * Returns the projectile shooting cooldown from the player data.
	 * Cooldown lasts 2 seconds. Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean playerProjectileShoot(Player player) {
		return getAndUpdateCooldownFromCache(player, "PROJECTILE_SHOOT", 2000L);
	}
	
	/**
	 * Returns the daily reward cooldown from the config.
	 * Cooldown lasts 79,200 seconds (22 hours). Resets it, if it is ready.
	 * 
	 * @param player The player who has selected the character.
	 * 
	 * @return If the cooldown is ready.
	 */
	public static boolean characterDailyReward(Player player) {
		return getAndUpdateCooldownFromConfig(player, "reward", 79200000L);
	}
	
	/**
	 * Returns a session specific cooldown from the player data.
	 * Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * @param actionId The id of the action the cooldown is set for.
	 * @param cooldown The timestamp, till when the cooldown lasts.
	 * 
	 * @return If the cooldown is ready.
	 */
	private static boolean getAndUpdateCooldownFromCache(Player player, String actionId, Long cooldown) {
		return getAndUpdateCooldownFromCache(player, actionId, cooldown, false);
	}
	
	/**
	 * Returns a session specific cooldown from the player data.
	 * Resets it, if it is ready.
	 * 
	 * @param player The player who owns the player data.
	 * @param actionId The id of the action the cooldown is set for.
	 * @param cooldown The timestamp, till when the cooldown lasts.
	 * @param forceUpdate Also resets the cooldown if it is not ready.
	 * 
	 * @return If the cooldown is ready.
	 */
	private static boolean getAndUpdateCooldownFromCache(Player player, String actionId, Long cooldown, boolean forceUpdate) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData != null && playerData.isActionReady(actionId)) {
			playerData.updateActionCooldown(actionId, cooldown);
			return true;
		}
		else {
			if(forceUpdate) {
				playerData.updateActionCooldown(actionId, cooldown);
			}
			return false;
		}
	}
	
	/**
	 * Returns a character specific cooldown from the config.
	 * Resets it, if it is ready.
	 * 
	 * @param player The player who has selected the character.
	 * @param actionId The id of the action the cooldown is set for.
	 * @param cooldown The timestamp, till when the cooldown lasts.
	 * 
	 * @return If the cooldown is ready.
	 */
	private static boolean getAndUpdateCooldownFromConfig(Player player, String actionId, Long cooldown) {
		if(PlayerConfigurator.isCharacterCooldownReady(player, actionId)) {
			PlayerConfigurator.updateCharacterCooldown(player, actionId, cooldown);
			return true;
		}
		else {
			return false;
		}
	}

}
