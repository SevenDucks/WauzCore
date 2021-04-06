package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.events.PetObtainEvent;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.menu.LootContainer;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbility;
import eu.wauz.wauzcore.mobs.pets.WauzPetBreedingLevel;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.mobs.pets.WauzPetRarity;
import eu.wauz.wauzcore.system.WauzModules;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that is used to breed pets.
 * 
 * @author Wauzmons
 *
 * @see WauzPetEgg
 */
@PublicMenu
public class BreedingMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "breeding";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
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
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Breeding Menu" +
				ChatColor.DARK_RED + ChatColor.BOLD + " Level " + breedingMenu.getLevel().getLevel();
		Inventory menu = Components.inventory(breedingMenu, menuTitle, 9);
		breedingMenu.setMenu(menu);
		
		ItemStack levelItemStack = MenuIconHeads.getTamesItem();
		ItemMeta levelItemMeta = levelItemStack.getItemMeta();
		Components.displayName(levelItemMeta, ChatColor.YELLOW + "Breeding Times for Current Level");
		List<String> levelLores = new ArrayList<>();
		for(WauzPetRarity rarity : WauzPetRarity.values()) {
			int time = breedingMenu.getLevel().getTime(rarity);
			if(time > 0) {
				String timeString = WauzDateUtils.formatHoursMins(time * 1000);
				levelLores.add(rarity.getColor() + rarity.toString() + ChatColor.YELLOW + ": " + timeString);
			}
		}
		levelLores.add(ChatColor.GRAY + "Tame more Pets to gain Experience");
		levelLores.add(ChatColor.GRAY + "and unlock more breedable Rarities!");
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
		Components.lore(levelItemMeta, levelLores);
		levelItemStack.setItemMeta(levelItemMeta);
		menu.setItem(1, levelItemStack);
		
		if(!WauzModules.isPetsModuleStandalone()) {
			ItemStack abilityItemStack = GenericIconHeads.getColorCubeItem();
			ItemMeta abilityItemMeta = abilityItemStack.getItemMeta();
			Components.displayName(abilityItemMeta, ChatColor.YELLOW + "Possible Abilities for Current Level");
			List<String> abilityLores = new ArrayList<>();
			List<WauzPetAbility> abilities = WauzPetAbilities.getAbilitiesForLevel(currentLevel);
			if(abilities.isEmpty()) {
				abilityLores.add(ChatColor.GREEN + "None yet...");
			}
			for(WauzPetAbility ability : abilities) {
				String description = ChatColor.YELLOW + ability.getAbilityDescription();
				abilityLores.add(ChatColor.GREEN + ability.getAbilityName() + " " + description);
			}
			abilityLores.add(ChatColor.GRAY + "Bred Pets can have special Abilities");
			abilityLores.add(ChatColor.GRAY + "which they will use once a minute!");
			Components.lore(abilityItemMeta, abilityLores);
			abilityItemStack.setItemMeta(abilityItemMeta);
			menu.setItem(7, abilityItemStack);
		}
		
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
	 * The item stack, used to breed pets.
	 */
	private ItemStack breedItemStack;
	
	/**
	 * The status text of the pets selected for breeding.
	 */
	private String breedStatusText;
	
	/**
	 * If the selected pets can be bred.
	 */
	private boolean breedStatusValid;
	
	/**
	 * The type of the new pet.
	 */
	private String newPetType;
	
	/**
	 * The rarity of the new pet;
	 */
	private WauzPetRarity newPetRarity;
	
	/**
	 * The seconds it takes the new pet to hatch.
	 */
	private int newPetSeconds;
	
	/**
	 * Constructs an empty breeding menu instance.
	 */
	public BreedingMenu() {
		this.level = WauzPetBreedingLevel.getBreedingLevel(0);
	}
	
	/**
	 * Constructs a new breeding menu instance.
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
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
				
				public void run() {
					updateBreedButton();
				}
				
			}, 1);
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
		ItemStack leftPetItemStack = menu.getItem(3);
		ItemStack rightPetItemStack = menu.getItem(5);
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
	 * 
	 * @see BreedingMenu#updatePetCompatibility(ItemStack, ItemStack)
	 */
	public void updateBreedButton() {
		updatePetCompatibility(menu.getItem(3), menu.getItem(5));
		ItemMeta breedItemMeta = breedItemStack.getItemMeta();
		Components.displayName(breedItemMeta, ChatColor.YELLOW + "Click to Breed Pets");
		List<String> breedLores = new ArrayList<>();
		breedLores.add(breedStatusText);
		breedLores.add("");
		breedLores.add(ChatColor.GRAY + "Insert Pets in both Slots to combine them.");
		breedLores.add(ChatColor.GRAY + "They need to be of same Type and Rarity.");
		breedLores.add(ChatColor.GRAY + "The maximum Rarity depends on your Level.");
		breedLores.add(ChatColor.GRAY + "New Pets can be better, worse or equal.");
		Components.lore(breedItemMeta, breedLores);
		breedItemStack.setItemMeta(breedItemMeta);
		menu.setItem(4, breedItemStack);
	}
	
	/**
	 * Checks if the given pets are compatible and sets the status accordingly.
	 * 
	 * @param leftPetItemStack The pet in the left slot.
	 * @param rightPetItemStack The pet in the right slot.
	 */
	private void updatePetCompatibility(ItemStack leftPetItemStack, ItemStack rightPetItemStack) {
		breedStatusValid = false;
		breedItemStack = GenericIconHeads.getDeclineItem();
		if(!PetEggUtils.isEggItem(leftPetItemStack) || !PetEggUtils.isEggItem(rightPetItemStack)) {
			breedStatusText = ChatColor.RED + "Please select two valid Pets!";
			return;
		}
		String leftPetType = PetEggUtils.getPetCategory(leftPetItemStack);
		String rightPetType = PetEggUtils.getPetCategory(rightPetItemStack);
		if(leftPetType == null || rightPetType == null || !StringUtils.equals(leftPetType, rightPetType)) {
			breedStatusText = ChatColor.RED + "The Pet Types do not match!";
			return;
		}
		newPetType = leftPetType;
		WauzPetRarity leftPetRarity = WauzPetRarity.determineRarity(leftPetItemStack);
		WauzPetRarity rightPetRarity = WauzPetRarity.determineRarity(rightPetItemStack);
		if(!leftPetRarity.equals(rightPetRarity)) {
			breedStatusText = ChatColor.RED + "The Pet Rarities do not match!";
			return;
		}
		newPetRarity = leftPetRarity;
		newPetSeconds = level.getTime(newPetRarity);
		if(newPetSeconds <= 0) {
			breedStatusText = ChatColor.RED + "Breeding Level too low for Rarity!";
			return;
		}
		breedStatusValid = true;
		breedItemStack = GenericIconHeads.getConfirmItem();
		breedStatusText = ChatColor.GREEN + "The Pets are ready to breed!";
	}
	
	/**
	 * Tries to breed the selected pets when their status is valid.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzPet#getOffspring(WauzPetRarity, String)
	 * @see PetObtainEvent#call(Player, WauzPet)
	 */
	public void tryToBreed(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if(breedStatusValid) {
			menu.setItem(3, null);
			menu.setItem(5, null);
			WauzPet newPet = WauzPet.getOffspring(newPetRarity, newPetType);
			if(newPet == null) {
				player.sendMessage(ChatColor.RED + "Your pets were invalid or outdated!");
				return;
			}
			long hatchTime = System.currentTimeMillis() + (newPetSeconds * 1000);
			WauzPetAbility petAbility = WauzModules.isPetsModuleStandalone() ? null : WauzPetAbilities.getAbilityForLevel(level);
			ItemStack newPetItemStack = WauzPetEgg.getEggItem(player, newPet, petAbility, hatchTime);
			PetObtainEvent.call(player, newPet);
			LootContainer.open(player, Collections.singletonList(newPetItemStack));
			player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_HATCH, 1, 1);
		}
	}
	
	/**
	 * @return The breeding level of the player.
	 */
	public WauzPetBreedingLevel getLevel() {
		return level;
	}

	/**
	 * @param menu The new breeding inventory menu.
	 */
	public void setMenu(Inventory menu) {
		this.menu = menu;
	}

}
