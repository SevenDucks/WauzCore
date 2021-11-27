package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
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
import eu.wauz.wauzcore.menu.abilities.JobMenu;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.heads.SkillIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbility;
import eu.wauz.wauzcore.mobs.pets.WauzPetBreedingLevel;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.mobs.pets.WauzPetRarity;
import eu.wauz.wauzcore.skills.passive.PassiveBreeding;
import eu.wauz.wauzcore.system.util.Components;
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
	 * Opens the menu for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 * @param exp The breeding experience of the player.
	 * 
	 * @see BreedingMenu#open(Player, PassiveBreeding)
	 */
	public static void open(Player player, int exp) {
		open(player, new PassiveBreeding(exp));
	}
	
	/**
	 * Opens the menu for the given player.
	 * Shows an interface to breed pets and displays breeding skill progression.
	 * 
	 * @param player The player that should view the inventory.
	 * @param breedingSkill The breeding skill of the player.
	 * 
	 * @see UnicodeUtils#createProgressBar(double, double, int, ChatColor)
	 * @see BreedingMenu#updateBreedButton()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, PassiveBreeding breedingSkill) {
		BreedingMenu breedingMenu = new BreedingMenu(breedingSkill);
		String levelText = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Level " + breedingSkill.getLevel();
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Breeding " + levelText;
		Inventory menu = Components.inventory(breedingMenu, menuTitle, 9);
		breedingMenu.setMenu(menu);
		
		ItemStack levelItemStack = SkillIconHeads.getTamesItem();
		ItemMeta levelItemMeta = levelItemStack.getItemMeta();
		Components.displayName(levelItemMeta, ChatColor.YELLOW + "Breeding Times");
		List<String> levelLores = new ArrayList<>();
		boolean canBreed = false;
		for(WauzPetRarity rarity : WauzPetRarity.getAllPetRarities()) {
			int time = breedingMenu.getLevel().getTime(rarity);
			if(time > 0) {
				String timeString = WauzDateUtils.formatHoursMins(time * 1000);
				levelLores.add(rarity.getColor() + rarity.getName() + ChatColor.YELLOW + " " + timeString);
				canBreed = true;
			}
		}
		if(!canBreed) {
			levelLores.add(ChatColor.GREEN + "None yet...");
		}
		levelLores.add(ChatColor.GRAY + "Tame more Pets to gain Experience");
		levelLores.add(ChatColor.GRAY + "and unlock more breedable Rarities!");
		Components.lore(levelItemMeta, levelLores);
		levelItemStack.setItemMeta(levelItemMeta);
		menu.setItem(2, levelItemStack);
		
		ItemStack backItemStack = MenuIconHeads.getCraftItem();
		MenuUtils.setItemDisplayName(backItemStack, ChatColor.YELLOW + "Back to Jobs");
		menu.setItem(0, backItemStack);
		
		ItemStack abilityItemStack = GenericIconHeads.getColorCubeItem();
		ItemMeta abilityItemMeta = abilityItemStack.getItemMeta();
		Components.displayName(abilityItemMeta, ChatColor.YELLOW + "Obtainable Abilities");
		List<String> abilityLores = new ArrayList<>();
		List<WauzPetAbility> abilities = WauzPetAbilities.getAbilitiesForLevel(breedingMenu.getLevel());
		if(abilities.isEmpty()) {
			abilityLores.add(ChatColor.GREEN + "None yet...");
		}
		for(WauzPetAbility ability : abilities) {
			String description = ChatColor.YELLOW + ability.getAbilityDescription();
			abilityLores.add(ChatColor.GREEN + ability.getAbilityName() + " " + description);
		}
		abilityLores.add(ChatColor.GRAY + "Hatched Pets can have special Abilities");
		abilityLores.add(ChatColor.GRAY + "which they will use once a minute.");
		Components.lore(abilityItemMeta, abilityLores);
		abilityItemStack.setItemMeta(abilityItemMeta);
		menu.setItem(8, abilityItemStack);
		
		MenuUtils.setBorders(menu);
		menu.setItem(4, null);
		menu.setItem(6, null);
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
	 * Constructs a new breeding menu instance.
	 * 
	 * @param breedingSkill The breeding skill of the player.
	 */
	private BreedingMenu(PassiveBreeding breedingSkill) {
		this.level = breedingSkill.getBreedingLevel();
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Lets the player select and breed their pets.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		String clickType = event.getClick().toString();
		if(clickType.contains("SHIFT")) {
			event.setCancelled(true);
			return;
		}
		
		int slot = event.getRawSlot();
		if(slot >= 9) {
			return;
		}
		
		if(slot == 4 || slot == 6) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					updateBreedButton();
				}
				
			}, 1);
			return;
		}
		
		event.setCancelled(true);
		if(slot == 0) {
			JobMenu.open((Player) event.getWhoClicked());
		}
		else if(slot == 5) {
			tryToBreed(event);
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
		ItemStack leftPetItemStack = menu.getItem(4);
		ItemStack rightPetItemStack = menu.getItem(6);
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
		updatePetCompatibility(menu.getItem(4), menu.getItem(6));
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
		menu.setItem(5, breedItemStack);
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
		if(newPetSeconds == 0) {
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
			menu.setItem(4, null);
			menu.setItem(6, null);
			WauzPet newPet = WauzPet.getOffspring(newPetRarity, newPetType);
			if(newPet == null) {
				player.sendMessage(ChatColor.RED + "Your pets were invalid or outdated!");
				return;
			}
			long hatchTime = System.currentTimeMillis() + (newPetSeconds * 1000);
			WauzPetAbility petAbility = WauzPetAbilities.getAbilityForLevel(level);
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
