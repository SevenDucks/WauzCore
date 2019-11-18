package eu.wauz.wauzcore.items;

import java.text.DecimalFormat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import net.md_5.bungee.api.ChatColor;

public class WauzRewards {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");

	public static void daily(final Player player) throws Exception {
		if(!Cooldown.characterDailyReward(player))
			return;
			
    	long money = PlayerConfigurator.getCharacterCoins(player);
    	long amount = 0;
    	String reward = null;
    	
    	switch(PlayerConfigurator.getRank(player)) {
			case "Admin":
				amount = 20000;
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
    	player.sendMessage(ChatColor.GOLD + reward + "You claimed your daily " + amount + " coins!");	
	}
	
	public static int level(Player player, int tier, double earnedxp) {
		return level(player, tier, earnedxp, null, false);
	}
	
	public static int level(Player player, int tier, double earnedxp, Location location) {
		return level(player, tier, earnedxp, location, false);
	}
	
	public static int level(Player player, int tier, double earnedxp, Location location, boolean shared) {
		try {
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
			if(playerGroup != null && !shared)
				for(Player member : playerGroup.getPlayers())
					level(member, tier, earnedxp / 4, null, true);
			
			if(player == null || player.getLevel() > tier || player.getLevel() >= WauzCore.MAX_PLAYER_LEVEL)
				return 0;
			
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
			
			if(location != null)
				ValueIndicator.spawnExpIndicator(location, displayexp);
			
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
	
	private static double applyExperienceBonus(Player player, double experience) {		
		ItemStack weaponItemStack = player.getEquipment().getItemInMainHand();
		double weaponBonus = ItemUtils.isNotAir(weaponItemStack) ? ItemUtils.getExperienceBonus(weaponItemStack) : 0;	
		ItemStack armorItemStack = player.getEquipment().getChestplate();
		double armorBonus = ItemUtils.isNotAir(armorItemStack) ? ItemUtils.getExperienceBonus(armorItemStack) : 0;
		
		double multiplier = 1 + ((double) (weaponBonus / 100)) + ((double) (armorBonus / 100));
		
		int petSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
		if(petSlot >= 0)
			multiplier += (float) ((float) PlayerConfigurator.getCharacterPetIntelligence(player, petSlot) / (float) 10f);
		
		return (double) ((double) experience * (double) multiplier);
	}
	
	public static void mmorpgToken(Player player) {
		final int limit = 30;
		final int today = tokensEarnedToday(player, "mmorpg");
		if(today >= limit) {
			player.sendMessage(ChatColor.YELLOW + "Tokenlimit (MMORPG) of " + limit + " reached for today!");
			return;
		}
		token(player, "MMORPG", today + 1, limit);
	}
	
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
		player.sendMessage(ChatColor.GOLD + "You reached Survival Score " + formatter.format(survivalScore) + "!");
		token(player, "Survival", today + 1, limit);
	}
	
	private static int tokensEarnedToday(Player player, String mode) {
		long dateLong = PlayerConfigurator.getTokenLimitDate(player, mode);
		long currentDateLong = WauzDateUtils.getDateLong();
		return dateLong < currentDateLong ? 0 : PlayerConfigurator.getTokenLimitAmount(player, mode);
	}
	
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
