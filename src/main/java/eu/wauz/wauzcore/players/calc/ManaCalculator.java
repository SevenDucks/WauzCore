package eu.wauz.wauzcore.players.calc;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import net.md_5.bungee.api.ChatColor;

public class ManaCalculator {
	
	public static void regenerateMana(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		if(playerData.getMaxMana() > playerData.getMana()) {
			playerData.setMana(playerData.getMana() + 1);
		}
		WauzPlayerActionBar.update(player);
	}
	
	public static void regenerateMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		if(playerData.getMaxMana() > playerData.getMana() + amount) {
			playerData.setMana(playerData.getMana() + amount);
		}
		else {
			playerData.setMana(playerData.getMaxMana());
		}
		WauzPlayerActionBar.update(player);
	}
	
	public static boolean useMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || (playerData.getMana() - amount) < 0) {
			player.sendMessage(ChatColor.RED + "Not enough Mana! " + amount + " Points are needed!");
			return false;
		}
		playerData.setMana(playerData.getMana() - amount);
		WauzPlayerActionBar.update(player);
		return true;
	}
	
}
