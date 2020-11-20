package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.mobs.pets.WauzPetBreedingLevel;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.mobs.pets.WauzPetRarity;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that is used to breed pets.
 * 
 * @author Wauzmons
 *
 * @see WauzPetEgg
 */
public class BreedingMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "breeding";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		throw new RuntimeException("Inventory cannot be opened directly!");
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows an interface to breed pets and display breeding skill progression.
	 * 
	 * @param player The player that should view the inventory.
	 * @param exp The breeding experience of the player.
	 * 
	 * @see UnicodeUtils#createProgressBar(double, double, int, ChatColor)
	 * @see MenuUtils#setBorders(Inventory)
	 * @see BreedingMenu#updateBreedButton()
	 */
	public static void open(Player player, int exp) {
		BreedingMenu breedingMenu = new BreedingMenu(exp);
		WauzInventoryHolder holder = new WauzInventoryHolder(breedingMenu);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Breeding Menu" +
				ChatColor.DARK_RED + ChatColor.BOLD + " Level " + breedingMenu.getLevel().getLevel());
		breedingMenu.setMenu(menu);
		
		ItemStack levelItemStack = MenuIconHeads.getTamesItem();
		ItemMeta levelItemMeta = levelItemStack.getItemMeta();
		levelItemMeta.setDisplayName(ChatColor.YELLOW + "Breeding Times for Current Level");
		List<String> levelLores = new ArrayList<>();
		for(WauzPetRarity rarity : WauzPetRarity.values()) {
			int time = breedingMenu.getLevel().getTime(rarity);
			if(time > 0) {
				String timeString = WauzDateUtils.formatHoursMins(time * 1000);
				levelLores.add(rarity.getColor() + rarity.toString() + ChatColor.YELLOW + ": " + timeString);
			}
		}
		levelLores.add(ChatColor.GRAY + "Tame more Pets to gain Experience");
		levelLores.add(ChatColor.GRAY + "and unlock more breedable rarities!");
		String expString = Formatters.INT.format(exp);
		WauzPetBreedingLevel currentLevel = breedingMenu.getLevel();
		WauzPetBreedingLevel nextLevel = currentLevel.getNextLevel();
		levelLores.add("");
		levelLores.add(ChatColor.WHITE + "Breeding Level: " + ChatColor.GREEN + currentLevel.getLevel());
		if(nextLevel != null) {
			String nextString = Formatters.INT.format(nextLevel.getExp());
			levelLores.add(ChatColor.WHITE + "Experience: " + ChatColor.GREEN + expString + " / " + nextString + ChatColor.WHITE + " to next Level");
			levelLores.add(UnicodeUtils.createProgressBar(exp, nextLevel.getExp(), 50, ChatColor.GREEN));
		}
		else {
			levelLores.add(ChatColor.WHITE + "Experience: " + ChatColor.GREEN + expString);
		}
		levelItemMeta.setLore(levelLores);
		levelItemStack.setItemMeta(levelItemMeta);
		menu.setItem(1, levelItemStack);
		
		MenuUtils.setBorders(menu);
		menu.setItem(3, null);
		menu.setItem(5, null);
		breedingMenu.updateBreedButton();
		player.openInventory(menu);
	}
	
	/**
	 * The breeding level of the player.
	 */
	private WauzPetBreedingLevel level;
	
	/**
	 * The breeding inventory menu.
	 */
	private Inventory menu;
	
	/**
	 * Constructs a new breeding menu instance
	 * 
	 * @param exp The breeding experience of the player.
	 */
	private BreedingMenu(int exp) {
		this.level = WauzPetBreedingLevel.getBreedingLevel(exp);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Lets the player select and breed their pets.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		
		if(slot < 0) {
			return;
		}
		else if(slot == 4) {
			tryToBreed(event);
		}
		else if(slot == 3 || slot == 5 || event.getClick().toString().contains("SHIFT")) {
			updateBreedButton();
		}
		else if(slot < 9) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Cleans up everything, so the inventory can be closed.
	 * Pets in the breeding slots will be dropped.
	 * 
	 * @param event The inventory close event.
	 */
	@Override
	public void destroyInventory(InventoryCloseEvent event) {
		Location dropLocation = event.getPlayer().getLocation();
		ItemStack leftPetItemStack = getMenu().getItem(3);
		ItemStack rightPetItemStack = getMenu().getItem(3);
		if(ItemUtils.isNotAir(leftPetItemStack)) {
			dropLocation.getWorld().dropItemNaturally(dropLocation, leftPetItemStack);
		}
		if(ItemUtils.isNotAir(rightPetItemStack)) {
			dropLocation.getWorld().dropItemNaturally(dropLocation, rightPetItemStack);
		}
	}
	
	/**
	 * Updates the breed button by checking if the selected pets are compatible.
	 * Sets the type and lore of the button accordingly.
	 */
	public void updateBreedButton() {
//		ItemStack breedItemStack;
//		List<String> breedLores = new ArrayList<>();
//		ItemStack leftPetItemStack = getMenu().getItem(3);
//		ItemStack rightPetItemStack = getMenu().getItem(3);
//		WauzPetRarity leftPetRarity;
//		WauzPetRarity rightPetRarity;
//		String leftPetType;
//		String rightPetType;
//		if(!PetEggUtils.isEggItem(leftPetItemStack) || !PetEggUtils.isEggItem(rightPetItemStack)) {
//			breedItemStack = GenericIconHeads.getDeclineItem();
//			breedLores.add(ChatColor.RED + "Please select valid Pets!");
//		}
//		else {
//			leftPet
//		}
//		else {
//			breedItemStack = GenericIconHeads.getConfirmItem();
//			breedLores.add(ChatColor.GREEN + "The Pets are ready to breed!");
//		}
//		ItemMeta breedItemMeta = breedItemStack.getItemMeta();
//		breedItemMeta.setDisplayName(ChatColor.YELLOW + "Click to Breed Pets");
//		breedLores.add("");
//		breedLores.add(ChatColor.GRAY + "Insert Pets in both Slots to combine them.");
//		breedLores.add(ChatColor.GRAY + "They need to be of same Type and Rarity.");
//		breedLores.add(ChatColor.GRAY + "The maximum Rarity depends on your Level.");
//		breedLores.add(ChatColor.GRAY + "The new Pet can be better, worse or equal.");
//		breedItemMeta.setLore(breedLores);
//		breedItemStack.setItemMeta(breedItemMeta);
//		getMenu().setItem(4, breedItemStack);
	}
	
	public void tryToBreed(InventoryClickEvent event) {
		event.setCancelled(true);
		if(ItemUtils.doesLoreContain(event.getCurrentItem(), ChatColor.GREEN + "The Pets are ready to breed!")) {
			// TODO
		}
	}
	
	/**
	 * @return The breeding level of the player.
	 */
	public WauzPetBreedingLevel getLevel() {
		return level;
	}

	/**
	 * @return The breeding inventory menu.
	 */
	public Inventory getMenu() {
		return menu;
	}

	/**
	 * @param menu The new breeding inventory menu.
	 */
	public void setMenu(Inventory menu) {
		this.menu = menu;
	}

}
