package eu.wauz.wauzcore.items;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import net.md_5.bungee.api.ChatColor;

/**
 * A collection of static methods to pay out currency rewards to players.
 * 
 * @author Wauzmons
 */
public class WauzRewards {

	/**
	 * Gives a player coins, based on their rank, if the cooldown is ready.
	 * 
	 * @param player The player that should claim their daily reward.
	 * 
	 * @see Cooldown#characterDailyReward(Player)
	 * @see PlayerConfigurator#getRank(org.bukkit.OfflinePlayer)
	 * @see PlayerConfigurator#setCharacterCoins(Player, long)
	 */
	public static void daily(final Player player) {
		if(!Cooldown.characterDailyReward(player)) {
			return;
		}
			
    	long money = PlayerConfigurator.getCharacterCoins(player);
    	long amount = 0;
    	String reward = null;
    	
    	switch(PlayerConfigurator.getRank(player)) {
			case "Admin":
				amount = 500;
				reward = "Admin Reward: ";			
				break;
			case "Normal":
				amount = 100;
				reward = "Normal Reward: ";
				break;
			default:
				break;
		}

    	PlayerConfigurator.setCharacterCoins(player, money + amount);
    	AchievementTracker.addProgress(player, WauzAchievementType.EARN_COINS, amount);
    	player.sendMessage(ChatColor.GOLD + reward + "You claimed your daily " + amount + " coins!");	
	}
	
	/**
	 * Gives experience points to the player and levels them up, if 100% is reached.
	 * Adds bonuses, based on level and passive stats.
	 * 
	 * @param player The player to receive the exp.
	 * @param tier The tier (max player level) for the exp reward.
	 * @param earnedxp The earned exp without modifiers.
	 * 
	 * @return The exp amount to display.
	 * 
	 * @see WauzRewards#level(Player, int, double, Location, boolean)
	 */
	public static int level(Player player, int tier, double earnedxp) {
		return level(player, tier, earnedxp, null, false);
	}
	
	/**
	 * Gives experience points to the player and levels them up, if 100% is reached.
	 * Adds bonuses, based on level and passive stats.
	 * 
	 * @param player The player to receive the exp.
	 * @param tier The tier (max player level) for the exp reward.
	 * @param earnedxp The earned exp without modifiers.
	 * @param location The location for an exp indicator or null for none.
	 * 
	 * @return The exp amount to display.
	 * 
	 * @see WauzRewards#level(Player, int, double, Location, boolean)
	 */
	public static int level(Player player, int tier, double earnedxp, Location location) {
		return level(player, tier, earnedxp, location, false);
	}
	
	/**
	 * Gives experience points to the player and levels them up, if 100% is reached.
	 * Adds bonuses, based on level and passive stats.
	 * 
	 * @param player The player to receive the exp.
	 * @param tier The tier (max player level) for the exp reward.
	 * @param earnedxp The earned exp without modifiers.
	 * @param location The location for an exp indicator or null for none.
	 * @param shared If the exp was already shared (25%) with group members.
	 * 
	 * @return The exp amount to display.
	 * 
	 * @see WauzCore#MAX_PLAYER_LEVEL
	 * @see WauzRewards#applyExperienceBonus(Player, double)
	 * @see ValueIndicator#spawnExpIndicator(Location, int)
	 * @see PlayerConfigurator#levelUpCharacter(Player)
	 * @see PlayerConfigurator#setCharacterExperience(Player, double)
	 */
	public static int level(Player player, int tier, double earnedxp, Location location, boolean shared) {
		try {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
			if(playerGroup != null && !shared) {
				for(Player member : playerGroup.getPlayers()) {
					level(member, tier, earnedxp / 4, null, true);
				}
			}
			
			if(player == null || player.getLevel() > tier || player.getLevel() >= WauzCore.MAX_PLAYER_LEVEL) {
				return 0;
			}
			
			switch(player.getLevel()) {
			case 1:
				earnedxp = earnedxp * 5;
				break;
			case 2:
				earnedxp = earnedxp * 4;
				break;
			case 3:
				earnedxp = earnedxp * 3;
				break;
			default:
				earnedxp = earnedxp * 2;
				break;
			}
			
			double amplifiedxp = applyExperienceBonus(player, earnedxp);
			int displayexp = (int) (amplifiedxp * 100);
			
			if(location != null) {
				ValueIndicator.spawnExpIndicator(location, displayexp);
			}
			
			double currentxp = PlayerConfigurator.getCharacterExperience(player);
			currentxp = currentxp + amplifiedxp;
			WauzDebugger.log(player, "You earned " + amplifiedxp + " (" + earnedxp + ") experience!");
			
			if(currentxp >= 100) {
				player.setLevel(player.getLevel() + 1);
				player.sendTitle(ChatColor.GOLD + "Level Up!", "You reached level " + player.getLevel() + "!", 10, 70, 20);
				PlayerConfigurator.levelUpCharacter(player);
				currentxp = 0;
			}
			PlayerConfigurator.setCharacterExperience(player, currentxp);
			return displayexp;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Applies experience bonuses, based on the passive stats of the player.
	 * 
	 * @param player The player that will earn the exp.
	 * @param experience The exp without bonus.
	 * 
	 * @return The exp with added bonus.
	 * 
	 * @see EquipmentUtils#getExperienceBonus(ItemStack)
	 * @see PlayerConfigurator#getCharacterPetIntelligence(Player, int)
	 */
	private static double applyExperienceBonus(Player player, double experience) {		
		ItemStack weaponItemStack = player.getEquipment().getItemInMainHand();
		double weaponBonus = ItemUtils.isNotAir(weaponItemStack) ? EquipmentUtils.getExperienceBonus(weaponItemStack) : 0;	
		ItemStack armorItemStack = player.getEquipment().getChestplate();
		double armorBonus = ItemUtils.isNotAir(armorItemStack) ? EquipmentUtils.getExperienceBonus(armorItemStack) : 0;
		
		double multiplier = 1 + (weaponBonus / 100.0) + (armorBonus / 100.0);
		
		int petSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
		if(petSlot >= 0) {
			multiplier += (float) PlayerConfigurator.getCharacterPetIntelligence(player, petSlot) / 10f;
		}
		
		return experience * multiplier;
	}
	
	/**
	 * Adds a token from MMORPG mode, if the limit of 30 has not been reached today.
	 * 
	 * @param player The player to receive the token.
	 * 
	 * @see WauzRewards#tokensEarnedToday(Player, String)
	 * @see WauzRewards#token(Player, String, int, int)
	 */
	public static void mmorpgToken(Player player) {
		final int limit = 30;
		final int today = tokensEarnedToday(player, "mmorpg");
		if(today >= limit) {
			player.sendMessage(ChatColor.YELLOW + "Tokenlimit (MMORPG) of " + limit + " reached for today!");
			return;
		}
		token(player, "MMORPG", today + 1, limit);
	}
	
	/**
	 * Adds a token from Survival mode, if the limit of 20 has not been reached today.
	 * Also resets the level to the maximum and increases the survival score by 1.
	 * 
	 * @param player The player to receive the token.
	 * 
	 * @see WauzRewards#tokensEarnedToday(Player, String)
	 * @see WauzRewards#token(Player, String, int, int)
	 * @see WauzCore#MAX_PLAYER_LEVEL_SURVIVAL
	 * @see PlayerConfigurator#setSurvivalScore(org.bukkit.OfflinePlayer, long)
	 */
	public static void survivalToken(Player player) {
		player.setLevel(WauzCore.MAX_PLAYER_LEVEL_SURVIVAL);
		player.setExp(0);
		
		final int limit = 20;
		final int today = tokensEarnedToday(player, "survival");
		if(today >= limit) {
			player.sendMessage(ChatColor.YELLOW + "Tokenlimit (Survival) of " + limit + " reached for today!");
			return;
		}
		Long survivalScore = PlayerConfigurator.getSurvivalScore(player) + 1;
		PlayerConfigurator.setSurvivalScore(player, survivalScore);
		player.sendMessage(ChatColor.GOLD + "You reached Survival Score " + Formatters.INT.format(survivalScore) + "!");
		token(player, "Survival", today + 1, limit);
	}
	
	/**
	 * Gets the amount of tokens earned for the current date.
	 * 
	 * @param player The player that earned the tokens.
	 * @param mode The mode the tokens were earned in.
	 * 
	 * @return The amount of tokens earned today.
	 * 
	 * @see WauzDateUtils#getDateLong()
	 * @see PlayerConfigurator#getTokenLimitDate(Player, String)
	 * @see PlayerConfigurator#getTokenLimitAmount(Player, String)
	 */
	private static int tokensEarnedToday(Player player, String mode) {
		long dateLong = PlayerConfigurator.getTokenLimitDate(player, mode);
		long currentDateLong = WauzDateUtils.getDateLong();
		return dateLong < currentDateLong ? 0 : PlayerConfigurator.getTokenLimitAmount(player, mode);
	}
	
	/**
	 * Adds a token from the given mode.
	 * Also updates the date for the daily limit and the amount earned today.
	 * 
	 * @param player The player to receive the token.
	 * @param modeDisplay The mode name, how it should be displayed.
	 * @param earnedToday The amount of tokens earned today.
	 * @param maxToday The maximum amount of tokens earnable per day.
	 * 
	 * @see PlayerConfigurator#setTokenLimitDate(Player, String, long)
	 * @see PlayerConfigurator#setTokenLimitAmount(Player, String, int)
	 * @see PlayerConfigurator#setTokens(Player, long)
	 */
	private static void token(Player player, String modeDisplay, int earnedToday, int maxToday) {
		PlayerConfigurator.setTokenLimitDate(player, modeDisplay.toLowerCase(), WauzDateUtils.getDateLong());
		PlayerConfigurator.setTokenLimitAmount(player, modeDisplay.toLowerCase(), earnedToday);
		PlayerConfigurator.setTokens(player, PlayerConfigurator.getTokens(player) + 1);
		int leftToday = maxToday - earnedToday;
		String leftString = leftToday == 0
				? "You reached the daily limit in " + modeDisplay + "!"
				: "You can earn " + leftToday + " more in " + modeDisplay + " today!";
		player.sendMessage(ChatColor.GOLD + "You earned a token! " + leftString);
		WauzPlayerScoreboard.scheduleScoreboard(player);
	}

}
