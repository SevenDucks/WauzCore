package eu.wauz.wauzcore.players.calc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class ManaCalculator {

	public static void updateManaItem(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || playerData.getMaxMana() == 0)
			return;
		
		ItemStack mana = playerData.getMana() < 1
				? new ItemStack(Material.CLOCK, 1)
				: new ItemStack(Material.DIAMOND, playerData.getMana());

		ItemMeta mim = mana.getItemMeta();
		String manaString = playerData.getMana() + " / " + playerData.getMaxMana();
		mim.setDisplayName(ChatColor.LIGHT_PURPLE + "Mana Points: " + manaString);
		mana.setItemMeta(mim);
		player.getInventory().setItem(6, mana);
		
		WauzDebugger.log(player, "Mana: " + manaString);
	}
	
	public static void regenerateMana(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		if(playerData.getMaxMana() > playerData.getMana())
			playerData.setMana(playerData.getMana() + 1);
		
		updateManaItem(player);
	}
	
	public static void regenerateMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		if(playerData.getMaxMana() > playerData.getMana() + amount)
			playerData.setMana(playerData.getMana() + amount);
		else
			playerData.setMana(playerData.getMaxMana());
		
		updateManaItem(player);
	}
	
	public static boolean useMana(Player player, int amount) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || (playerData.getMana() - amount) < 0) {
			player.sendMessage(ChatColor.RED + "Not enough Mana! " + amount + " Points are needed!");
			return false;
		}
		playerData.setMana(playerData.getMana() - amount);
		updateManaItem(player);
		return true;
	}
	
}
