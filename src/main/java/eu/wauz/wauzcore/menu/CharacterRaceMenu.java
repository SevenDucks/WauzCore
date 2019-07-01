package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class CharacterRaceMenu implements WauzInventory {
	
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CharacterRaceMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Race!");
		
		ItemStack race1 = new ItemStack(Material.BLUE_CONCRETE);
		ItemMeta im1 = race1.getItemMeta();
		im1.setDisplayName(ChatColor.BLUE + "Human");
		List<String> lores1 = new ArrayList<String>();
		lores1.add(ChatColor.WHITE + "You probably know what a human is!");
		im1.setLore(lores1);
		race1.setItemMeta(im1);
		menu.setItem(4, race1);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData == null)
			return;
		
		if(clicked == null || !clicked.getType().toString().endsWith("CONCRETE"))
			return;
		
		else if(clicked.getItemMeta().getDisplayName().contains("Human")) {
			playerData.setSelectedCharacterRace("Human");
			CharacterManager.createCharacter(player, WauzMode.MMORPG);
			player.closeInventory();
		}
	}

}
