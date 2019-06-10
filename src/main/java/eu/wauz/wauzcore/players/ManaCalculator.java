package eu.wauz.wauzcore.players;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class ManaCalculator {

	public static void updateManaItem(Player player) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null || pd.getMaxMana() == 0) return;
		
		ItemStack mana = pd.getMana() < 1
				? new ItemStack(Material.CLOCK, 1)
				: new ItemStack(Material.DIAMOND, pd.getMana());

		ItemMeta mim = mana.getItemMeta();
		String manaString = pd.getMana() + " / " + pd.getMaxMana();
		mim.setDisplayName(ChatColor.LIGHT_PURPLE + "Mana Points: " + manaString);
		mana.setItemMeta(mim);
		player.getInventory().setItem(6, mana);
		
		WauzDebugger.log(player, "Mana: " + manaString);
	}
	
	public static void regenerateMana(Player player) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null) return;
		
		if(pd.getMaxMana() > pd.getMana())
			pd.setMana(pd.getMana() + 1);
		
		updateManaItem(player);
	}
	
	public static void regenerateMana(Player player, int amount) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null) return;
		
		if(pd.getMaxMana() > pd.getMana() + amount)
			pd.setMana(pd.getMana() + amount);
		else
			pd.setMana(pd.getMaxMana());
		
		updateManaItem(player);
	}
	
	public static boolean useMana(Player player, int amount) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null || (pd.getMana() - amount) < 0) {
			player.sendMessage(ChatColor.RED + "Not enough Mana! " + amount + " Points are needed!");
			return false;
		}
		pd.setMana(pd.getMana() - amount);
		updateManaItem(player);
		return true;
	}
	
}
