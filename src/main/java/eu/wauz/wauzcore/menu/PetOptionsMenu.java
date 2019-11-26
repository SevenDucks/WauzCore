package eu.wauz.wauzcore.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import eu.wauz.wauzcore.events.WauzPlayerEventPetBreed;
import eu.wauz.wauzcore.events.WauzPlayerEventPetDelete;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.md_5.bungee.api.ChatColor;

public class PetOptionsMenu implements WauzInventory {
	
	private static WauzCore core = WauzCore.getInstance();
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
// Single Pet Options Menu
	
	public static void open(Player player, Integer petSlot) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new PetOptionsMenu());
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
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
		String disallowString = PlayerConfigurator.getCharcterPetBreedingDisallowString(player, petSlot);
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
		
		int intelligenceLevel = PlayerConfigurator.getCharacterPetIntelligence(player, petSlot);
		int intelligenceLevelMax = PlayerConfigurator.getCharacterPetIntelligenceMax(player, petSlot);
		
		ItemStack intelligenceSkillItemStack;
		if(intelligenceLevel == 0)
			intelligenceSkillItemStack = new ItemStack(Material.PORKCHOP);
		else if(intelligenceLevel < intelligenceLevelMax)
			intelligenceSkillItemStack = new ItemStack(Material.COOKED_PORKCHOP, intelligenceLevel);
		else
			intelligenceSkillItemStack = new ItemStack(Material.COOKED_BEEF, intelligenceLevel);
		
		ItemMeta intelligenceSkillItemMeta = intelligenceSkillItemStack.getItemMeta();
		intelligenceSkillItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Intelligence");
		List<String> intelligenceSkillLores = new ArrayList<String>();
		intelligenceSkillLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + intelligenceLevel + " / " + intelligenceLevelMax + ChatColor.GRAY + " (Max: 10)");
		intelligenceSkillLores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerConfigurator.getCharacterPetIntelligenceExpNeeded(player, petSlot));
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
		
		int dexterityLevel = PlayerConfigurator.getCharacterPetDexterity(player, petSlot);
		int dexterityLevelMax = PlayerConfigurator.getCharacterPetDexterityMax(player, petSlot);
		
		ItemStack dexteritySkillItemStack;
		if(dexterityLevel == 0)
			dexteritySkillItemStack = new ItemStack(Material.PORKCHOP);
		else if(dexterityLevel < dexterityLevelMax)
			dexteritySkillItemStack = new ItemStack(Material.COOKED_PORKCHOP, dexterityLevel);
		else
			dexteritySkillItemStack = new ItemStack(Material.COOKED_BEEF, dexterityLevel);
		
		ItemMeta dexteritySkillItemMeta = dexteritySkillItemStack.getItemMeta();
		dexteritySkillItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Dexterity");
		List<String> dexteritySkillLores = new ArrayList<String>();
		dexteritySkillLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + dexterityLevel + " / " + dexterityLevelMax + ChatColor.GRAY + " (Max: 10)");
		dexteritySkillLores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerConfigurator.getCharacterPetDexterityExpNeeded(player, petSlot));
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
		
		int absorptionLevel = PlayerConfigurator.getCharacterPetAbsorption(player, petSlot);
		int absorptionLevelMax = PlayerConfigurator.getCharacterPetAbsorptionMax(player, petSlot);
		
		ItemStack absorptionSkillItemStack;
		if(absorptionLevel == 0)
			absorptionSkillItemStack = new ItemStack(Material.PORKCHOP);
		else if(absorptionLevel < absorptionLevelMax)
			absorptionSkillItemStack = new ItemStack(Material.COOKED_PORKCHOP, absorptionLevel);
		else
			absorptionSkillItemStack = new ItemStack(Material.COOKED_BEEF, absorptionLevel);
		
		ItemMeta absorptionSkillItemMeta = absorptionSkillItemStack.getItemMeta();
		absorptionSkillItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Absorption");
		List<String> absorptionSkillLores = new ArrayList<String>();
		absorptionSkillLores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + absorptionLevel + " / " + absorptionLevelMax + ChatColor.GRAY + " (Max: 10)");
		absorptionSkillLores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerConfigurator.getCharacterPetAbsorptionExpNeeded(player, petSlot));
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
		
		ItemStack moveLeftItemStack = HeadUtils.getPrevItem();
		ItemMeta moveLeftItemMeta = moveLeftItemStack.getItemMeta();
		moveLeftItemMeta.setDisplayName(ChatColor.YELLOW + "Move LEFT");
		moveLeftItemStack.setItemMeta(moveLeftItemMeta);
		menu.setItem(6, moveLeftItemStack);
		
		ItemStack moveRightItemStack = HeadUtils.getNextItem();
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
	
	private static Integer getIndex(InventoryView inventory) {
		String indexString = StringUtils.substringBetween(inventory.getTitle(), "\"");
		return Integer.parseInt(indexString);
	}
	
// Summoning, Discarding and Moving
	
	public static void summon(Player player, Integer petSlot) {	
		Location location = player.getLocation();	
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
					
		String petId = PlayerConfigurator.getCharacterActivePetId(player);
		if(!petId.contains("none")) {
			Entity entity = Bukkit.getServer().getEntity(UUID.fromString(petId));		
			if(entity != null)
				entity.remove();
		}
		
		try {
			Entity entity = mythicMobs.spawnMythicMob(petType, location);
			if(entity instanceof Tameable) {
				((Tameable) entity).setOwner(player);
			}
			petId = entity.getUniqueId().toString();
			PlayerConfigurator.setCharacterActivePetId(player, petId);
			PlayerConfigurator.setCharacterActivePetSlot(player, petSlot);
			
			PetOverviewMenu.setOwner(petId, player);
			player.setWalkSpeed(0.2f + PlayerConfigurator.getCharacterPetDexterity(player, petSlot) * 0.02f);
			
			WauzDebugger.log(player, player.getName() + " summoned Pet " + petId);
			player.sendMessage(ChatColor.GREEN + petType + " was summoned!");
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + petType + " was not summoned, due to an error!");
		}
	}
	
	public static void discard(Player player, Integer petSlot) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
		playerData.setWauzPlayerEventName("Release " + petType);
		playerData.setWauzPlayerEvent(new WauzPlayerEventPetDelete(petSlot));
		WauzDialog.open(player);
	}
	
	public static void move(Player player, int fromIndex, int toIndex, boolean openMenu) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String characterSlot = playerData.getSelectedCharacterSlot();
		
		Integer activePetSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
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
            if(openMenu)
            	PetOverviewMenu.open(player, toIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Feeding
	
	private static void feed(Player player, int petSlot, ItemStack statItem) {
		int foodStatId = 0;
		if(!statItem.hasItemMeta() || !statItem.getItemMeta().hasDisplayName())
			return;
		else if(statItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Intelligence"))
			foodStatId = 1;
		else if(statItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Dexterity"))
			foodStatId = 2;
		else if(statItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Absorption"))
			foodStatId = 3;
		else
			return;
		
		ItemStack foodItem = player.getItemOnCursor();
		if(!ItemUtils.isFoodItem(foodItem) || !ItemUtils.containsSaturationModifier(foodItem))
			return;
		
		int feedingValue = ItemUtils.getSaturation(foodItem) * foodItem.getAmount();
		foodItem.setAmount(0);
		
		if(foodStatId == 1) {
			int remainingExp = PlayerConfigurator.getCharacterPetIntelligenceExpNeeded(player, petSlot) - feedingValue;
			if(remainingExp > 0)
				PlayerConfigurator.setCharacterPetIntelligenceExpNeeded(player, petSlot, remainingExp);
			else {
				Integer activePetSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
				if(activePetSlot != -1 && activePetSlot == petSlot) {
					PetOverviewMenu.unsummon(player);
				}
				
				int level = PlayerConfigurator.getCharacterPetIntelligence(player, petSlot) + 1;
				PlayerConfigurator.setCharacterPetIntelligence(player, petSlot, level);
				PlayerConfigurator.setCharacterPetIntelligenceExpNeeded(player, petSlot, PetOverviewMenu.getBaseExpToFeedingLevel(level + 1));
			}
		}
		else if(foodStatId == 2) {
			int remainingExp = PlayerConfigurator.getCharacterPetDexterityExpNeeded(player, petSlot) - feedingValue;
			if(remainingExp > 0)
				PlayerConfigurator.setCharacterPetDexterityExpNeeded(player, petSlot, remainingExp);
			else {
				Integer activePetSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
				if(activePetSlot != -1 && activePetSlot == petSlot) {
					PetOverviewMenu.unsummon(player);
				}
				
				int level = PlayerConfigurator.getCharacterPetDexterity(player, petSlot) + 1;
				PlayerConfigurator.setCharacterPetDexterity(player, petSlot, level);
				PlayerConfigurator.setCharacterPetDexterityExpNeeded(player, petSlot, PetOverviewMenu.getBaseExpToFeedingLevel(level + 1));
			}
		}
		else if(foodStatId == 3) {
			int remainingExp = PlayerConfigurator.getCharacterPetAbsorptionExpNeeded(player, petSlot) - feedingValue;
			if(remainingExp > 0)
				PlayerConfigurator.setCharacterPetAbsorptionExpNeeded(player, petSlot, remainingExp);
			else {
				Integer activePetSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
				if(activePetSlot != -1 && activePetSlot == petSlot) {
					PetOverviewMenu.unsummon(player);
				}
				
				int level = PlayerConfigurator.getCharacterPetAbsorption(player, petSlot) + 1;
				PlayerConfigurator.setCharacterPetAbsorption(player, petSlot, level);
				PlayerConfigurator.setCharacterPetAbsorptionExpNeeded(player, petSlot, PetOverviewMenu.getBaseExpToFeedingLevel(level + 1));
			}
		}
		
		open(player, petSlot);
	}
	
// Breeding
	
	public static void breed(Player player, int petSlot, ItemStack breedStack) {
		if(breedStack.getItemMeta().getLore().get(3).contains("Disallowed"))
			return;
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
		playerData.setWauzPlayerEventName("Breed " + petType);
		playerData.setWauzPlayerEvent(new WauzPlayerEventPetBreed(petSlot));
		WauzDialog.open(player);
	}

}
