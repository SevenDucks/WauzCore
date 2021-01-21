package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.CharacterManager;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassStats;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * A character creation menu, that will let the player select their class.
 * 
 * @author Wauzmons
 * 
 * @see CharacterSlotMenu
 * @see CharacterWorldMenu
 */
public class CharacterClassMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "classes";
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows all the classes to choose from including descriptions and stats.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerClassPool#getAllClasses()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new CharacterClassMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Choose your Class!");
		
		List<WauzPlayerClass> characterClasses = WauzPlayerClassPool.getAllClasses();
		for(int index = 0; index < characterClasses.size(); index++) {
			WauzPlayerClass characterClass = characterClasses.get(index);
			WauzPlayerClassStats stats = characterClass.getStartingStats();
			
			ItemStack classItemStack = characterClass.getClassItemStack();
			ItemMeta classItemMeta = classItemStack.getItemMeta();
			classItemMeta.setDisplayName(characterClass.getClassColor() + "" + ChatColor.BOLD + characterClass.getClassName());
			
			List<String> classLores = new ArrayList<>();
			for(String textPart : UnicodeUtils.wrapText(characterClass.getClassDescription())) {
				classLores.add(ChatColor.GRAY + textPart);
			}
			classLores.add("");
			
			classLores.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Learnable Masteries");
			for(WauzPlayerSubclass subclass : characterClass.getSubclasses()) {
				String subclassDescription = subclass.getSublassColor() + subclass.getSublassDescription();
				classLores.add(ChatColor.GOLD + subclass.getSubclassName() + ": " + subclassDescription);
			}
			classLores.add("");
			
			classLores.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Equipment Proficiency");
			String armorType = characterClass.getArmorCategory().toString();
			classLores.add(ChatColor.BLUE + "" + armorType + ChatColor.GOLD + " Armor Type");
			int staffSkill = (int) Math.ceil(stats.getStaffSkill() / 1000);
			classLores.add(ChatColor.RED + "" + staffSkill + ChatColor.GOLD + " Staff Skill");
			int axeSkill = (int) Math.ceil(stats.getAxeSkill() / 1000);
			classLores.add(ChatColor.RED + "" + axeSkill + ChatColor.GOLD + " Axe Skill");
			int swordSkill = (int) Math.ceil(stats.getSwordSkill() / 1000);
			classLores.add(ChatColor.RED + "" + swordSkill + ChatColor.GOLD + " Sword Skill");
			
			classItemMeta.setLore(classLores);
			classItemStack.setItemMeta(classItemMeta);
			menu.setItem(index * 2 + 1, classItemStack);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If the clicked item is a class selection, it will be cached in the player data.
	 * Next the character creation will be triggered.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPlayerData#setSelectedCharacterClass(String)
	 * @see CharacterManager#createCharacter(Player, WauzMode)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(playerData == null || !ItemUtils.hasDisplayName(clicked) || !ItemUtils.isMaterial(clicked, Material.PLAYER_HEAD)) {
			return;
		}
		
		String className = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
		if(WauzPlayerClassPool.getClass(className) != null) {
			playerData.getSelections().setSelectedCharacterClass(className);
			CharacterManager.createCharacter(player, WauzMode.MMORPG);
			player.closeInventory();
		}
	}

}
