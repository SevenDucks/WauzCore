package eu.wauz.wauzcore.system.util;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

public class Cooldown {
	
	public static boolean playerEntityInteraction(Player player) {
		return getAndUpdateCooldownFromCache(player, "ENTITY_INTERACT", 1000L);
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
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd != null && pd.isActionReady(player, actionId)) {
			pd.updateActionCooldown(player, actionId, cooldown);
			return true;
		}
		else
			return false;
	}
	
	private static boolean getAndUpdateCooldownFromConfig(Player player, String actionId, Long cooldown) {
		if(PlayerConfigurator.isCharacterCooldownReady(player, actionId)) {
			PlayerConfigurator.updateCharacterCooldown(player, actionId, cooldown);
			return true;
		}
		else
			return false;
	}

}
