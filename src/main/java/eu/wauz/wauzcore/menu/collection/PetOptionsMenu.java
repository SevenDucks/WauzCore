package eu.wauz.wauzcore.menu.collection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPetsConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventPetBreed;
import eu.wauz.wauzcore.events.WauzPlayerEventPetDelete;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.SpeedCalculator;
import eu.wauz.wauzcore.system.WauzDebugger;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the pet menu, that is used for interacting with a specific pet.
 * 
 * @author Wauzmons
 *
 * @see PetOverviewMenu
 */
public class PetOptionsMenu implements WauzInventory {
	
	/**
	 * A direct reference to the main class.
	 */
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "petoptions";
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
	
// Single Pet Options Menu
	
	/**
	 * Opens the menu for the given player.
	 * Views the details of an pet and shows all possible interaction options.
	 * The first option is the summoning of the pet.
	 * The second option is to send the pet as parent to the breeding station, which may be disallowed for some pets.
	 * The third option is to release the pet back to the wild, deleting it permanently.
	 * All three stats of the pet will be shown differently based on their level.
	 * Level 0 = Raw Pork, Level X = Cooked Pork, Level Max = Beef.
	 * The levels can be increased by dragging food on these slots.
	 * Additionally there are options to move the pet one slot to the left or the right in the overview menu,
	 * aswell as an option to return to the overview menu.
	 * 
	 * @param player The player that should view the inventory.
	 * @param petSlot The slot of the pet to view.
	 * 
	 * @see PlayerConfigurator#getCharacterPetType(Player, int)
	 * @see PlayerConfigurator#getCharcterPetBreedingDisallowString(Player, int)
	 * @see PlayerConfigurator#getCharacterPetIntelligence(Player, int)
	 * @see PlayerConfigurator#getCharacterPetDexterity(Player, int)
	 * @see PlayerConfigurator#getCharacterPetAbsorption(Player, int)
	 */
	public static void open(Player player, Integer petSlot) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new PetOptionsMenu());
		String petType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Pet Options"
				+ " \"" + petSlot + "\" " + ChatColor.DARK_PURPLE + petType);
		
		ItemStack summonItemStack = new ItemStack(Material.LEAD);
		ItemMeta summonItemMeta = summonItemStack.getItemMeta();
		summonItemMeta.setDisplayName(ChatColor.GREEN + "Summon " + petType);
		List<String> summonLores = new ArrayList<String>();
		summonLores.add(ChatColor.GRAY + "Summon your Pet into the world!");
		summonLores.add(ChatColor.GRAY + "Your currently active Pet will be unsummoned.");
		summonItemMeta.setLore(summonLores);
		summonItemStack.setItemMeta(summonItemMeta);
		menu.setItem(0, summonItemStack);
		
		ItemStack breedingItemStack = new ItemStack(Material.EGG);
		ItemMeta breedingItemMeta = breedingItemStack.getItemMeta();
		breedingItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Breed " + petType);
		List<String> breedingLores = new ArrayList<String>();
		String disallowString = PlayerPetsConfigurator.getCharcterPetBreedingDisallowString(player, petSlot);
		breedingLores.add(ChatColor.GRAY + "Combine Pets to raise the Offspring's max Stats.");
		breedingLores.add(ChatColor.GRAY + "WARNING: Both Parents will be lost!");
		breedingLores.add("");
		if(StringUtils.isNotBlank(disallowString)) {
			breedingLores.add(ChatColor.RED + "Disallowed: " + disallowString);
			breedingLores.add("");
		}
		breedingLores.add(ChatColor.DARK_GRAY + "The Pet will be moved to the Breeding Station.");
		breedingLores.add(ChatColor.DARK_GRAY + "You can't take it back, once inside.");
		breedingLores.add(ChatColor.DARK_GRAY + "Once two Pets are inside they will lay an Egg.");
		breedingLores.add(ChatColor.DARK_GRAY + "After a long Cooldown you can claim the Newborn.");
		breedingItemMeta.setLore(breedingLores);
		breedingItemStack.setItemMeta(breedingItemMeta);
		menu.setItem(1, breedingItemStack);
		
		ItemStack discardItemStack = new ItemStack(Material.BARRIER);
		ItemMeta discardItemMeta = discardItemStack.getItemMeta();
		discardItemMeta.setDisplayName(ChatColor.RED + "Discard " + petType);
		List<String> discardLores = new ArrayList<String>();
		discardLores.add(ChatColor.GRAY + "Release your Pet to the Wildness.");
		discardLores.add(ChatColor.GRAY + "WARNING: Cannot be restored!");
		discardItemMeta.setLore(discardLores);
		discardItemStack.setItemMeta(discardItemMeta);
		menu.setItem(2, discardItemStack);
		
		int intelligenceLevel = PlayerPetsConfigurator.getCharacterPetIntelligence(player, petSlot);
		int intelligenceLevelMax = PlayerPetsConfigurator.getCharacterPetIntelligenceMax(player, petSlot);
		
		ItemStack intelligenceSkillItemStack;
		if(intelligenceLevel == 0) {
			intelligenceSkillItemStack = new ItemStack(Material.PORKCHOP);
		}
		else if(intelligenceLevel < intelligenceLevelMax) {
			intelligenceSkillItemStack = new ItemStack(Material.COOKED_PORKCHOP, intelligenceLevel);
		}
		else {
			intelligenceSkillItemStack = new ItemStack(Material.COOKED_BEEF, intelligenceLevel);
		}
		
		ItemMeta intelligenceSkillItemMeta = intelligenceSkillItemStack.getItemMeta();
		intelligenceSkillItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Intelligence");
		List<String> intelligenceSkillLores = new ArrayList<String>();
		intelligenceSkillLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + intelligenceLevel + " / " + intelligenceLevelMax + ChatColor.GRAY + " (Max: 10)");
		intelligenceSkillLores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerPetsConfigurator.getCharacterPetIntelligenceExpNeeded(player, petSlot));
		intelligenceSkillLores.add("");
		intelligenceSkillLores.add(ChatColor.GRAY + "Adds 10% Bonus Experience Points per Level.");
		intelligenceSkillLores.add(ChatColor.GRAY + "This Boost is only applied while your Pet is active.");
		intelligenceSkillLores.add("");
		intelligenceSkillLores.add(ChatColor.GRAY + "Drag Food-Items here, to feed your Pet.");
		intelligenceSkillLores.add("");
		intelligenceSkillLores.add(ChatColor.WHITE + "Intelligence: " + ChatColor.DARK_AQUA + (intelligenceLevel * 10) + "%");
		intelligenceSkillItemMeta.setLore(intelligenceSkillLores);
		intelligenceSkillItemStack.setItemMeta(intelligenceSkillItemMeta);
		menu.setItem(3, intelligenceSkillItemStack);
		
		int dexterityLevel = PlayerPetsConfigurator.getCharacterPetDexterity(player, petSlot);
		int dexterityLevelMax = PlayerPetsConfigurator.getCharacterPetDexterityMax(player, petSlot);
		
		ItemStack dexteritySkillItemStack;
		if(dexterityLevel == 0) {
			dexteritySkillItemStack = new ItemStack(Material.PORKCHOP);
		}
		else if(dexterityLevel < dexterityLevelMax) {
			dexteritySkillItemStack = new ItemStack(Material.COOKED_PORKCHOP, dexterityLevel);
		}
		else {
			dexteritySkillItemStack = new ItemStack(Material.COOKED_BEEF, dexterityLevel);
		}
		
		ItemMeta dexteritySkillItemMeta = dexteritySkillItemStack.getItemMeta();
		dexteritySkillItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Dexterity");
		List<String> dexteritySkillLores = new ArrayList<String>();
		dexteritySkillLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + dexterityLevel + " / " + dexterityLevelMax + ChatColor.GRAY + " (Max: 10)");
		dexteritySkillLores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerPetsConfigurator.getCharacterPetDexterityExpNeeded(player, petSlot));
		dexteritySkillLores.add("");
		dexteritySkillLores.add(ChatColor.GRAY + "Adds 10% per Level to your Movement Speed.");
		dexteritySkillLores.add(ChatColor.GRAY + "This Boost is only applied while your Pet is active.");
		dexteritySkillLores.add("");
		dexteritySkillLores.add(ChatColor.GRAY + "Drag Food-Items here, to feed your Pet.");
		dexteritySkillLores.add("");
		dexteritySkillLores.add(ChatColor.WHITE + "Dexterity: " + ChatColor.DARK_AQUA + (dexterityLevel * 10) + "%");
		dexteritySkillItemMeta.setLore(dexteritySkillLores);
		dexteritySkillItemStack.setItemMeta(dexteritySkillItemMeta);
		menu.setItem(4, dexteritySkillItemStack);
		
		int absorptionLevel = PlayerPetsConfigurator.getCharacterPetAbsorption(player, petSlot);
		int absorptionLevelMax = PlayerPetsConfigurator.getCharacterPetAbsorptionMax(player, petSlot);
		
		ItemStack absorptionSkillItemStack;
		if(absorptionLevel == 0) {
			absorptionSkillItemStack = new ItemStack(Material.PORKCHOP);
		}
		else if(absorptionLevel < absorptionLevelMax) {
			absorptionSkillItemStack = new ItemStack(Material.COOKED_PORKCHOP, absorptionLevel);
		}
		else {
			absorptionSkillItemStack = new ItemStack(Material.COOKED_BEEF, absorptionLevel);
		}
		
		ItemMeta absorptionSkillItemMeta = absorptionSkillItemStack.getItemMeta();
		absorptionSkillItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Absorption");
		List<String> absorptionSkillLores = new ArrayList<String>();
		absorptionSkillLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + absorptionLevel + " / " + absorptionLevelMax + ChatColor.GRAY + " (Max: 10)");
		absorptionSkillLores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerPetsConfigurator.getCharacterPetAbsorptionExpNeeded(player, petSlot));
		absorptionSkillLores.add("");
		absorptionSkillLores.add(ChatColor.GRAY + "Absorbs 10% of your Armor per Level from Damage.");
		absorptionSkillLores.add(ChatColor.GRAY + "This Boost is only applied while your Pet is active.");
		absorptionSkillLores.add("");
		absorptionSkillLores.add(ChatColor.GRAY + "Drag Food-Items here, to feed your Pet.");
		absorptionSkillLores.add("");
		absorptionSkillLores.add(ChatColor.WHITE + "Absorption: " + ChatColor.DARK_AQUA + (absorptionLevel * 10) + "%");
		absorptionSkillItemMeta.setLore(absorptionSkillLores);
		absorptionSkillItemStack.setItemMeta(absorptionSkillItemMeta);
		menu.setItem(5, absorptionSkillItemStack);
		
		ItemStack moveLeftItemStack = GenericIconHeads.getPrevItem();
		ItemMeta moveLeftItemMeta = moveLeftItemStack.getItemMeta();
		moveLeftItemMeta.setDisplayName(ChatColor.YELLOW + "Move LEFT");
		moveLeftItemStack.setItemMeta(moveLeftItemMeta);
		menu.setItem(6, moveLeftItemStack);
		
		ItemStack moveRightItemStack = GenericIconHeads.getNextItem();
		ItemMeta moveRightItemMeta = moveRightItemStack.getItemMeta();
		moveRightItemMeta.setDisplayName(ChatColor.YELLOW + "Move RIGHT");
		moveRightItemStack.setItemMeta(moveRightItemMeta);
		menu.setItem(8, moveRightItemStack);
		
		ItemStack backItemStack = new ItemStack(Material.PUFFERFISH_SPAWN_EGG);
		ItemMeta backItemMeta = backItemStack.getItemMeta();
		backItemMeta.setDisplayName(ChatColor.BLUE + "Back to Pet-Selection");
		backItemStack.setItemMeta(backItemMeta);
		menu.setItem(7, backItemStack);
		
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and tries to interact with the pet.
	 * Clicking the lead will summon the pet.
	 * Clicking the egg tries to move the pet to the breeding station.
	 * Clicking the barrier will show a dialog to release the pet.
	 * Clicking the porkchop with food on the cursor will feed the pet.
	 * Clicking left or right arrows will move the pet in the overview menu.
	 * Clicking the pufferfish egg will lead back to the overview menu.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PetOptionsMenu#getIndex(InventoryView)
	 * @see PetOptionsMenu#summon(Player, Integer)
	 * @see PetOptionsMenu#breed(Player, int, ItemStack)
	 * @see PetOptionsMenu#discard(Player, Integer)
	 * @see PetOptionsMenu#feed(Player, int, ItemStack)
	 * @see PetOptionsMenu#move(Player, int, int, boolean)
	 * @see PetOverviewMenu#open(Player, int)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || clicked.getType().equals(Material.AIR) || ItemUtils.isFoodItem(clicked)) {
			event.setCancelled(false);
			return;
		}
		else if(clicked.getType().equals(Material.LEAD)) {
			summon(player, getIndex(player.getOpenInventory()));
			player.closeInventory();
		}
		else if(clicked.getType().equals(Material.EGG)) {
			int index = getIndex(player.getOpenInventory());
			breed(player, index, clicked);
		}
		else if(clicked.getType().equals(Material.BARRIER)) {
			discard(player, getIndex(player.getOpenInventory()));
		}
		else if(clicked.getType().equals(Material.PORKCHOP) || clicked.getType().equals(Material.COOKED_PORKCHOP)) {
			int index = getIndex(player.getOpenInventory());
			feed(player, index, clicked);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Move LEFT")) {
			int index = getIndex(player.getOpenInventory());
			int moveTo = index > 0 ? index - 1 : 4;
			move(player, index, moveTo, true);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Move RIGHT")) {
			int index = getIndex(player.getOpenInventory());
			int moveTo = index < 4 ? index + 1 : 0;
			move(player, index, moveTo, true);
		}
		else if(clicked.getType().equals(Material.PUFFERFISH_SPAWN_EGG)) {
			PetOverviewMenu.open(player, -1);
		}
	}

// Support Methods
	
	/**
	 * Gets the index of a pet by reading the title of their options menu.
	 * 
	 * @param inventory The inventory of the pet's options menu.
	 * 
	 * @return The index of the pet.
	 */
	private static Integer getIndex(InventoryView inventory) {
		String indexString = StringUtils.substringBetween(inventory.getTitle(), "\"");
		return Integer.parseInt(indexString);
	}
	
// Summoning, Discarding and Moving
	
	/**
	 * Lets the given player summon a specific pet.
	 * Tries to remove the entity from an old pet first, if any exists.
	 * Sets the active pet id and slot, aswell as the pet's owner afterwards.
	 * Also recalculates the player movement speed, based on the pet's dexterity stat.
	 * 
	 * @param player The player who summons the pet.
	 * @param petSlot The slot of the pet being summoned.
	 * 
	 * @see PlayerConfigurator#getCharacterPetType(Player, int)
	 * @see PlayerConfigurator#getCharacterActivePetId(Player)
	 * @see PlayerConfigurator#setCharacterActivePetId(Player, String)
	 * @see PlayerConfigurator#setCharacterActivePetSlot(Player, int)
	 * @see PetOverviewMenu#setOwner(String, Player)
	 */
	public static void summon(Player player, Integer petSlot) {	
		Location location = player.getLocation();	
		String petType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
					
		String petId = PlayerPetsConfigurator.getCharacterActivePetId(player);
		if(!petId.contains("none")) {
			Entity entity = Bukkit.getServer().getEntity(UUID.fromString(petId));		
			if(entity != null) {
				entity.remove();
			}
		}
		
		try {
			Entity entity = mythicMobs.spawnMythicMob(petType, location);
			if(entity instanceof Tameable) {
				((Tameable) entity).setOwner(player);
			}
			petId = entity.getUniqueId().toString();
			PlayerPetsConfigurator.setCharacterActivePetId(player, petId);
			PlayerPetsConfigurator.setCharacterActivePetSlot(player, petSlot);
			
			PetOverviewMenu.setOwner(petId, player);
			SpeedCalculator.resetWalkSpeed(player);
			
			WauzDebugger.log(player, player.getName() + " summoned Pet " + petId);
			player.sendMessage(ChatColor.GREEN + petType + " was summoned!");
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + petType + " was not summoned, due to an error!");
		}
	}
	
	/**
	 * Lets the given player release a pet into the wild, permanently deleting it.
	 * A confirm dialog is shown beforehand.
	 * 
	 * @param player The player who discards the pet.
	 * @param petSlot The slot of the pet being discarded.
	 * 
	 * @see WauzPlayerEventPetDelete
	 */
	public static void discard(Player player, Integer petSlot) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String petType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
		playerData.setWauzPlayerEventName("Release " + petType);
		playerData.setWauzPlayerEvent(new WauzPlayerEventPetDelete(petSlot));
		WauzDialog.open(player);
	}
	
	/**
	 * Lets the given player move a pet to another slot in the overview menu. 
	 * Unsummons the player's current pet, if it is affected from the movement.
	 * The config file gets directly edited with a file writer, to swap the slots.
	 * Afterwards the overview menu is opened, highlighting the pet's new position.
	 * 
	 * @param player The player who moves the pet.
	 * @param fromIndex The current overview index of the pet.
	 * @param toIndex The new overview index of the pet.
	 * @param openMenu If the overview should be opened afterwards.
	 * 
	 * @see PlayerConfigurator#getCharacterActivePetSlot(Player)
	 * @see PetOverviewMenu#unsummon(Player)
	 * @see PetOverviewMenu#open(Player, int)
	 * @see BufferedReader
	 * @see FileWriter
	 */
	public static void move(Player player, int fromIndex, int toIndex, boolean openMenu) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String characterSlot = playerData.getSelectedCharacterSlot();
		
		Integer activePetSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
		if(activePetSlot != -1 && (activePetSlot == fromIndex || activePetSlot == toIndex)) {
			PetOverviewMenu.unsummon(player);
		}
		
		try {
			String playerConfigPath = core.getDataFolder().getAbsolutePath() + "/PlayerData/" + player.getUniqueId() + "/" + characterSlot + ".yml";
			File playerConfigFile = new File(playerConfigPath);
            BufferedReader reader = new BufferedReader(new FileReader(playerConfigFile));
            
            String line = "", content = "";
            String fromString = "slot" + fromIndex;
            String toString = "slot" + toIndex;
            
            while((line = reader.readLine()) != null) {
            	if(line.contains(fromString)) {
            		line = line.replace(fromString, toString);
            		WauzDebugger.log(player, "Replaced Pet " + fromString + " with " + toString);
            	}
            	else if(line.contains(toString)) {
            		line = line.replace(toString, fromString);
            		WauzDebugger.log(player, "Replaced Pet " + toString + " with " + fromString);
            	}
                content += line + "\r\n";
            }
            reader.close();
            
            FileWriter writer = new FileWriter(playerConfigPath);
            writer.write(content);
            writer.close();
            if(openMenu) {
            	PetOverviewMenu.open(player, toIndex);
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Feeding
	
	/**
	 * Feeds the current pet, the item on the player's curser, to increase the given stat's exp.
	 * The exp gain is based on the food item's saturation modifier.
	 * If the pet's stat reaches a new level, the stat will be increased by 1 and the exp to the next level will be set.
	 * If the pet is active while reaching a new level, it will be unsummoned.
	 * If the max level is reached, this method won't be called anymore from the stat item interaction.
	 * 
	 * @param player The player who feeds the pet.
	 * @param petSlot The slot of the pet, that is getting fed.
	 * @param statItem The item stack showing the pet stat.
	 * 
	 * @see ItemUtils#isFoodItem(ItemStack)
	 * @see ItemUtils#getSaturation(ItemStack)
	 * @see PetOptionsMenu#feedPetIntelligence(Player, int, int)
	 * @see PetOptionsMenu#feedPetDexterity(Player, int, int)
	 * @see PetOptionsMenu#feedPetAbsorption(Player, int, int)
	 */
	private static void feed(Player player, int petSlot, ItemStack statItem) {
		int foodStatId = 0;
		if(!statItem.hasItemMeta() || !statItem.getItemMeta().hasDisplayName()) {
			return;
		}
		else if(statItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Intelligence")) {
			foodStatId = 1;
		}
		else if(statItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Dexterity")) {
			foodStatId = 2;
		}
		else if(statItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Absorption")) {
			foodStatId = 3;
		}
		else {
			return;
		}
		
		ItemStack foodItem = player.getItemOnCursor();
		if(!ItemUtils.isFoodItem(foodItem) || !ItemUtils.containsSaturationModifier(foodItem)) {
			return;
		}
		int feedingValue = ItemUtils.getSaturation(foodItem) * foodItem.getAmount();
		foodItem.setAmount(0);
		
		if(foodStatId == 1) {
			feedPetIntelligence(player, petSlot, feedingValue);
		}
		else if(foodStatId == 2) {
			feedPetDexterity(player, petSlot, feedingValue);
		}
		else if(foodStatId == 3) {
			feedPetAbsorption(player, petSlot, feedingValue);
		}
		open(player, petSlot);
	}

	/**
	 * Feeds the given pet to increase its intelligence.
	 * 
	 * @param player The player who feeds the pet.
	 * @param petSlot The slot of the pet, that is getting fed.
	 * @param feedingValue The feeding value of the fed item.
	 * 
	 * @see PlayerPetsConfigurator#getCharacterPetIntelligence(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetIntelligence(Player, int, int)
	 * @see PlayerPetsConfigurator#getCharacterPetIntelligenceExpNeeded(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetIntelligenceExpNeeded(Player, int, int)
	 * @see PetOverviewMenu#unsummon(Player)
	 * @see PetOverviewMenu#getBaseExpToFeedingLevel(int)
	 * @see PetOptionsMenu#feed(Player, int, ItemStack)
	 */
	private static void feedPetIntelligence(Player player, int petSlot, int feedingValue) {
		int remainingExp = PlayerPetsConfigurator.getCharacterPetIntelligenceExpNeeded(player, petSlot) - feedingValue;
		if(remainingExp > 0) {
			PlayerPetsConfigurator.setCharacterPetIntelligenceExpNeeded(player, petSlot, remainingExp);
		}
		else {
			Integer activePetSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
			if(activePetSlot != -1 && activePetSlot == petSlot) {
				PetOverviewMenu.unsummon(player);
			}
			
			int level = PlayerPetsConfigurator.getCharacterPetIntelligence(player, petSlot) + 1;
			PlayerPetsConfigurator.setCharacterPetIntelligence(player, petSlot, level);
			PlayerPetsConfigurator.setCharacterPetIntelligenceExpNeeded(player, petSlot, PetOverviewMenu.getBaseExpToFeedingLevel(level + 1));
		}
	}

	/**
	 * Feeds the given pet to increase its dexterity.
	 * 
	 * @param player The player who feeds the pet.
	 * @param petSlot The slot of the pet, that is getting fed.
	 * @param feedingValue The feeding value of the fed item.
	 * 
	 * @see PlayerPetsConfigurator#getCharacterPetDexterity(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetDexterity(Player, int, int)
	 * @see PlayerPetsConfigurator#getCharacterPetDexterityExpNeeded(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetDexterityExpNeeded(Player, int, int)
	 * @see PetOverviewMenu#unsummon(Player)
	 * @see PetOverviewMenu#getBaseExpToFeedingLevel(int)
	 * @see PetOptionsMenu#feed(Player, int, ItemStack)
	 */
	private static void feedPetDexterity(Player player, int petSlot, int feedingValue) {
		int remainingExp = PlayerPetsConfigurator.getCharacterPetDexterityExpNeeded(player, petSlot) - feedingValue;
		if(remainingExp > 0) {
			PlayerPetsConfigurator.setCharacterPetDexterityExpNeeded(player, petSlot, remainingExp);
		}
		else {
			Integer activePetSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
			if(activePetSlot != -1 && activePetSlot == petSlot) {
				PetOverviewMenu.unsummon(player);
			}
			
			int level = PlayerPetsConfigurator.getCharacterPetDexterity(player, petSlot) + 1;
			PlayerPetsConfigurator.setCharacterPetDexterity(player, petSlot, level);
			PlayerPetsConfigurator.setCharacterPetDexterityExpNeeded(player, petSlot, PetOverviewMenu.getBaseExpToFeedingLevel(level + 1));
		}
	}

	/**
	 * Feeds the given pet to increase its absorption.
	 * 
	 * @param player The player who feeds the pet.
	 * @param petSlot The slot of the pet, that is getting fed.
	 * @param feedingValue The feeding value of the fed item.
	 * 
	 * @see PlayerPetsConfigurator#getCharacterPetAbsorption(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetAbsorption(Player, int, int)
	 * @see PlayerPetsConfigurator#getCharacterPetAbsorptionExpNeeded(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetAbsorptionExpNeeded(Player, int, int)
	 * @see PetOverviewMenu#unsummon(Player)
	 * @see PetOverviewMenu#getBaseExpToFeedingLevel(int)
	 * @see PetOptionsMenu#feed(Player, int, ItemStack)
	 */
	private static void feedPetAbsorption(Player player, int petSlot, int feedingValue) {
		int remainingExp = PlayerPetsConfigurator.getCharacterPetAbsorptionExpNeeded(player, petSlot) - feedingValue;
		if(remainingExp > 0) {
			PlayerPetsConfigurator.setCharacterPetAbsorptionExpNeeded(player, petSlot, remainingExp);
		}
		else {
			Integer activePetSlot = PlayerPetsConfigurator.getCharacterActivePetSlot(player);
			if(activePetSlot != -1 && activePetSlot == petSlot) {
				PetOverviewMenu.unsummon(player);
			}
			
			int level = PlayerPetsConfigurator.getCharacterPetAbsorption(player, petSlot) + 1;
			PlayerPetsConfigurator.setCharacterPetAbsorption(player, petSlot, level);
			PlayerPetsConfigurator.setCharacterPetAbsorptionExpNeeded(player, petSlot, PetOverviewMenu.getBaseExpToFeedingLevel(level + 1));
		}
	}
	
// Breeding
	
	/**
	 * Tries to add the pet to the breeding station, if it is allowed.
	 * The station must be free and the pet needs at least 1 maxed stat.
	 * A confirm dialog is shown beforehand.
	 * 
	 * @param player The player who wants to breed the pet.
	 * @param petSlot The slot of the pet to breed.
	 * @param breedStack The breeding option item stack.
	 * 
	 * @see WauzPlayerEventPetBreed
	 */
	public static void breed(Player player, int petSlot, ItemStack breedStack) {
		if(breedStack.getItemMeta().getLore().get(3).contains("Disallowed")) {
			return;
		}
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String petType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
		playerData.setWauzPlayerEventName("Breed " + petType);
		playerData.setWauzPlayerEvent(new WauzPlayerEventPetBreed(petSlot));
		WauzDialog.open(player);
	}

}
