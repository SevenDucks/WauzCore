package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class PetOverviewMenu implements WauzInventory {
	
	private static Map<String, Player> petOwnerMap = new HashMap<>();
	
// Pet Overview Menu
	
	public static void open(Player player, int highlightedSlot) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new PetOverviewMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Pet Overview");
		
		ItemStack emptySlotItemStack = new ItemStack(Material.BARRIER);
		ItemMeta emptySlotItemMeta = emptySlotItemStack.getItemMeta();
		emptySlotItemMeta.setDisplayName(ChatColor.RED + "Empty Pet Slot");
		emptySlotItemStack.setItemMeta(emptySlotItemMeta);
		
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
			if(!petType.equals("none")) {
				ItemStack petItemStack = new ItemStack(highlightedSlot == petSlot ? Material.PARROT_SPAWN_EGG : Material.CHICKEN_SPAWN_EGG);
				ItemMeta petItemMeta = petItemStack.getItemMeta();
				petItemMeta.setDisplayName(ChatColor.GREEN + petType);
				List<String> lores = new ArrayList<String>();
				lores.add(ChatColor.GRAY + "Click for Pet-Options");
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "Index: " + petSlot);
				petItemMeta.setLore(lores);
				petItemStack.setItemMeta(petItemMeta);
				menu.setItem(petSlot, petItemStack);
			}
			else {
				menu.setItem(petSlot, emptySlotItemStack);
			}
		}
		
		ItemStack unsummonItemStack = new ItemStack(Material.STRING);
		ItemMeta unsummonItemMeta = unsummonItemStack.getItemMeta();
		unsummonItemMeta.setDisplayName(ChatColor.RED + "Unsummon Active Pet");
		unsummonItemStack.setItemMeta(unsummonItemMeta);
		menu.setItem(5, unsummonItemStack);
		
		String parentAPetType = PlayerConfigurator.getCharacterPetType(player, 6);
		boolean parentSlotAIsEmpty = parentAPetType.equals("none");
		
		ItemStack parentAItemStack = new ItemStack(parentSlotAIsEmpty ? Material.GHAST_SPAWN_EGG : Material.SHEEP_SPAWN_EGG);
		ItemMeta parentAItemMeta = parentAItemStack.getItemMeta();
		parentAItemMeta.setDisplayName(parentSlotAIsEmpty ? ChatColor.RED + "Free Breeding Slot" : ChatColor.GREEN + parentAPetType);
		List<String> parentALores = new ArrayList<String>();
		parentALores.add(ChatColor.GRAY + "Breeding Slot A");
		parentAItemMeta.setLore(parentALores);
		parentAItemStack.setItemMeta(parentAItemMeta);
		menu.setItem(6, parentAItemStack);
		
		String parentBPetType = PlayerConfigurator.getCharacterPetType(player, 8);
		boolean parentSlotBIsEmpty = parentBPetType.equals("none");
		
		ItemStack parentBItemStack = new ItemStack(parentSlotBIsEmpty ? Material.GHAST_SPAWN_EGG : Material.SHEEP_SPAWN_EGG);
		ItemMeta parentBItemMeta = parentBItemStack.getItemMeta();
		parentBItemMeta.setDisplayName(parentSlotBIsEmpty ? ChatColor.RED + "Free Breeding Slot" : ChatColor.GREEN + parentBPetType);
		List<String> parentBLores = new ArrayList<String>();
		parentBLores.add(ChatColor.GRAY + "Breeding Slot B");
		parentBItemMeta.setLore(parentBLores);
		parentBItemStack.setItemMeta(parentBItemMeta);
		menu.setItem(8, parentBItemStack);
		
		ItemStack childItemStack = new ItemStack(Material.EGG);
		ItemMeta childItemMeta = childItemStack.getItemMeta();
		long hatchTime = PlayerConfigurator.getCharacterPetBreedingHatchTime(player);
		if(hatchTime == 0) {
			childItemMeta.setDisplayName(ChatColor.RED + "No Egg in Breeding Station");
		}
		else if(hatchTime > System.currentTimeMillis()) {
			hatchTime += 1000 - System.currentTimeMillis();
			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(hatchTime),
		            TimeUnit.MILLISECONDS.toMinutes(hatchTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hatchTime)),
		            TimeUnit.MILLISECONDS.toSeconds(hatchTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(hatchTime)));
			childItemMeta.setDisplayName(ChatColor.YELLOW + "Breeding... " + hms + " Hours Remain");
		}
		else {
			childItemMeta.setDisplayName(ChatColor.GREEN + "Click to hatch Pet");
		}
		childItemStack.setItemMeta(childItemMeta);
		menu.setItem(7, childItemStack);
		
		player.openInventory(menu);
	}
	
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null)
			return;
		
		else if(clicked.getType().equals(Material.CHICKEN_SPAWN_EGG) || clicked.getType().equals(Material.PARROT_SPAWN_EGG)) {
			PetOptionsMenu.open(player, getIndex(clicked));
		}
		
		else if(clicked.getType().equals(Material.STRING)) {
			unsummon(player);
		}
		
		else if(clicked.getType().equals(Material.EGG)) {
			hatch(player, clicked);
		}
	}

// Support Methods
	
	private static Integer getIndex(ItemStack item) {
		for(String string : item.getItemMeta().getLore()) {
			if(string.contains("Index")) {
				String[] indexStringParts = string.split(" ");
				return Integer.parseInt(indexStringParts[1]);
			}
		}
		return null;
	}
	
	public static Player getOwner(Entity entity) {
		return petOwnerMap.get(entity.getUniqueId().toString());
	}
	
	public static void setOwner(String petId, Player player) {
		petOwnerMap.put(petId, player);
	}
	
	public static void removeOwner(String petId, Player player) {
		petOwnerMap.remove(petId);
		player.setWalkSpeed(0.2f);
	}
	
// Unsummoning
	
	public static void unsummon(Player player) {
		try {
			if(!WauzMode.isMMORPG(player))
				return;
			if(!WauzPlayerDataPool.isCharacterSelected(player))
				return;
							
			String petId = PlayerConfigurator.getCharacterActivePetId(player);
			
			PlayerConfigurator.setCharacterActivePetSlot(player, -1);
			
			if(!petId.contains("none")) {
				Entity entity = Bukkit.getServer().getEntity(UUID.fromString(petId));		
				if(entity != null) {
					for(Entity passenger : entity.getPassengers())
						passenger.remove();
					entity.remove();
					removeOwner(petId, player);
					player.sendMessage(ChatColor.GREEN + "Your current Pet was unsommoned!");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Adding
	
	public static void addPet(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack scroll = event.getItem();
		
		if(scroll == null || !scroll.hasItemMeta() || !scroll.getItemMeta().hasDisplayName()) {
			return;
		}
		
		String type = scroll.getItemMeta().getLore().get(0);
		type = type.replaceAll("" + ChatColor.GREEN, "");	
		addPet(player, scroll, type);
	}
	
	public static void addPet(Player player, ItemStack scroll, String petType) {
		addPet(player, scroll, petType, 3, 3, 3);
	}

	public static void addPet(Player player, ItemStack scroll, String petType, int maxInt, int maxDex, int maxAbs) {
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String slotType = PlayerConfigurator.getCharacterPetType(player, petSlot);
			if(!slotType.equals("none")) continue;
			
			try {
				PlayerConfigurator.setCharacterPetType(player, petSlot, petType);
				
				int baseExp = getBaseExpToFeedingLevel(1);
				
				PlayerConfigurator.setCharacterPetIntelligence(player, petSlot, 0);
				PlayerConfigurator.setCharacterPetIntelligenceMax(player, petSlot, maxInt);
				PlayerConfigurator.setCharacterPetIntelligenceExpNeeded(player, petSlot, baseExp);
				
				PlayerConfigurator.setCharacterPetDexterity(player, petSlot, 0);
				PlayerConfigurator.setCharacterPetDexterityMax(player, petSlot, maxDex);
				PlayerConfigurator.setCharacterPetDexterityExpNeeded(player, petSlot, baseExp);
				
				PlayerConfigurator.setCharacterPetAbsorption(player, petSlot, 0);
				PlayerConfigurator.setCharacterPetAbsorptionMax(player, petSlot, maxAbs);
				PlayerConfigurator.setCharacterPetAbsorptionExpNeeded(player, petSlot, baseExp);
				
				player.sendMessage(ChatColor.GREEN + "You learned to summon " + petType + " from the Menu!");
				AchievementTracker.addProgress(player, WauzAchievementType.COLLECT_PETS, 1);
				
				if(scroll != null)
					scroll.setAmount(scroll.getAmount() - 1);
				
				open(player, petSlot);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		
		player.sendMessage(ChatColor.RED + "You have no free Pet-Slots!");
	}
	
// Leveling
	
	public static int getBaseExpToFeedingLevel(int level) {
		switch (level) {
		case 1:
			return   100;
		case 2:
			return   250;
		case 3:
			return   500;
		case 4:
			return  1000;
		case 5:
			return  1800;
		case 6:
			return  3000;
		case 7:
			return  4500;
		case 8:
			return  7000;
		case 9:
			return 12000;
		case 10:
			return 20000;
		default:
			return 0;
		}
	}
	
// Hatching
	
	public static void hatch(Player player, ItemStack eggStack) {
		String eggDisplayNamme = eggStack.getItemMeta().getDisplayName();
		if(eggDisplayNamme.contains("Breeding")) {
			if (eggDisplayNamme.contains("..."))
				open(player, -1);
			return;
		}
		
		boolean freeSlot = false;
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String slotType = PlayerConfigurator.getCharacterPetType(player, petSlot);
			if(slotType.equals("none")) {
				freeSlot = true;
				break;
			}
		}
		if(!freeSlot) {
			player.sendMessage(ChatColor.RED + "You have no free Pet-Slots!");
			player.closeInventory();
			return;
		}
		
		String typeA = PlayerConfigurator.getCharacterPetType(player, 6);
		int intA = PlayerConfigurator.getCharacterPetIntelligence(player, 6);
		int intMaxA = PlayerConfigurator.getCharacterPetIntelligenceMax(player, 6);
		int dexA = PlayerConfigurator.getCharacterPetDexterity(player, 6);
		int dexMaxA = PlayerConfigurator.getCharacterPetDexterityMax(player, 6);
		int absA = PlayerConfigurator.getCharacterPetAbsorption(player, 6);
		int absMaxA = PlayerConfigurator.getCharacterPetAbsorptionMax(player, 6);
		
		String typeB = PlayerConfigurator.getCharacterPetType(player, 8);
		int intB = PlayerConfigurator.getCharacterPetIntelligence(player, 8);
		int intMaxB = PlayerConfigurator.getCharacterPetIntelligenceMax(player, 8);
		int dexB = PlayerConfigurator.getCharacterPetDexterity(player, 8);
		int dexMaxB = PlayerConfigurator.getCharacterPetDexterityMax(player, 8);
		int absB = PlayerConfigurator.getCharacterPetAbsorption(player, 8);
		int absMaxB = PlayerConfigurator.getCharacterPetAbsorptionMax(player, 8);
		
		String petType = Chance.percent(50) ? typeA : typeB;
		
		int maxInt = Math.max(intMaxA, intMaxB) + (intA >= intMaxA ? 1 : 0) + (intB >= intMaxB ? 1 : 0);
		int maxDex = Math.max(dexMaxA, dexMaxB) + (dexA >= dexMaxA ? 1 : 0) + (dexB >= dexMaxB ? 1 : 0);
		int maxAbs = Math.max(absMaxA, absMaxB) + (absA >= absMaxA ? 1 : 0) + (absB >= absMaxB ? 1 : 0);
		
		PlayerConfigurator.setCharacterPetType(player, 6, "none");
		PlayerConfigurator.setCharacterPetType(player, 8, "none");
		PlayerConfigurator.setCharacterPetBreedingHatchTime(player, 0);
		
		addPet(player, null, petType, maxInt > 10 ? 10 : maxInt, maxDex > 10 ? 10 : maxDex, maxAbs > 10 ? 10 : maxAbs);
	}

}
