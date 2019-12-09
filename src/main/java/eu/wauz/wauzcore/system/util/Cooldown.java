package eu.wauz.wauzcore.system.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;

public class Cooldown {
	
	public static boolean playerEntityInteraction(Player player) {
		return getAndUpdateCooldownFromCache(player, "ENTITY_INTERACT", 1000L);
	}
	
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
	
	public static boolean playerSkillUse(Player player) {
		return getAndUpdateCooldownFromCache(player, "SKILL_USE", 1000L);
	}
	
	public static boolean playerFoodConsume(Player player) {
		return getAndUpdateCooldownFromCache(player, "FOOD_CONSUME", 1000L);
	}
	
	public static boolean playerProjectileShoot(Player player) {
		return getAndUpdateCooldownFromCache(player, "PROJECTILE_SHOOT", 2000L);
	}
	
	public static boolean playerBombThrow(Player player) {
		return getAndUpdateCooldownFromCache(player, "BOMB_THROW", 3000L);
	}
	
	public static boolean characterDailyReward(Player player) {
		return getAndUpdateCooldownFromConfig(player, "reward", 80000000L);
	}
	
	private static boolean getAndUpdateCooldownFromCache(Player player, String actionId, Long cooldown) {
		return getAndUpdateCooldownFromCache(player, actionId, cooldown, false);
	}
	
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
