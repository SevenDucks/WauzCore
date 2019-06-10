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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventPetBreed;
import eu.wauz.wauzcore.events.WauzPlayerEventPetDelete;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class PetOptionsMenu implements WauzInventory {
	
	private static WauzCore core = WauzCore.getInstance();
	
// Single Pet Options Menu
	
	public static void open(Player player, Integer petSlot) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new PetOptionsMenu());
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Pet Options"
				+ " \"" + petSlot + "\" " + ChatColor.DARK_PURPLE + petType);
		
		{
			ItemStack sumn = new ItemStack(Material.LEAD);
			ItemMeta im = sumn.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Summon " + petType);
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Summon your Pet into the world!");
			lores.add(ChatColor.GRAY + "Your currently active Pet will be unsummoned.");
			im.setLore(lores);
			sumn.setItemMeta(im);
			menu.setItem(0, sumn);
		}
		
		{
			ItemStack bred = new ItemStack(Material.EGG);
			ItemMeta im = bred.getItemMeta();
			im.setDisplayName(ChatColor.LIGHT_PURPLE + "Breed " + petType);
			List<String> lores = new ArrayList<String>();
			String disallowString = PlayerConfigurator.getCharcterPetBreedingDisallowString(player, petSlot);
			lores.add(ChatColor.GRAY + "Combine Pets to raise the Offspring's max Stats.");
			lores.add(ChatColor.GRAY + "WARNING: Both Parents will be lost!");
			lores.add("");
			if(StringUtils.isNotBlank(disallowString)) {
				lores.add(ChatColor.RED + "Disallowed: " + disallowString);
				lores.add("");
			}
			lores.add(ChatColor.DARK_GRAY + "The Pet will be moved to the Breeding Station.");
			lores.add(ChatColor.DARK_GRAY + "You can't take it back, once inside.");
			lores.add(ChatColor.DARK_GRAY + "Once two Pets are inside they will lay an Egg.");
			lores.add(ChatColor.DARK_GRAY + "After a long Cooldown you can claim the Newborn.");
			im.setLore(lores);
			bred.setItemMeta(im);
			menu.setItem(1, bred);
		}
		
		{
			ItemStack disc = new ItemStack(Material.BARRIER);
			ItemMeta im = disc.getItemMeta();
			im.setDisplayName(ChatColor.RED + "Discard " + petType);
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.GRAY + "Release your Pet to the Wildness.");
			lores.add(ChatColor.GRAY + "WARNING: Cannot be restored!");
			im.setLore(lores);
			disc.setItemMeta(im);
			menu.setItem(2, disc);
		}
		
		{
			int level = PlayerConfigurator.getCharacterPetIntelligence(player, petSlot);
			int levelMax = PlayerConfigurator.getCharacterPetIntelligenceMax(player, petSlot);
			
			ItemStack sinv;
			if(level == 0)
				sinv = new ItemStack(Material.PORKCHOP);
			else if(level < levelMax)
				sinv = new ItemStack(Material.COOKED_PORKCHOP, level);
			else
				sinv = new ItemStack(Material.COOKED_BEEF, level);
			
			ItemMeta im = sinv.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Intelligence");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + level + " / " + levelMax + ChatColor.GRAY + " (Max: 10)");
			lores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerConfigurator.getCharacterPetIntelligenceExpNeeded(player, petSlot));
			lores.add("");
			lores.add(ChatColor.GRAY + "Adds 10% Bonus Experience Points per Level.");
			lores.add(ChatColor.GRAY + "This Boost is only applied while your Pet is active.");
			lores.add("");
			lores.add(ChatColor.GRAY + "Drag Food-Items here, to feed your Pet.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Intelligence: " + ChatColor.DARK_AQUA + (level * 10) + "%");
			im.setLore(lores);
			sinv.setItemMeta(im);
			menu.setItem(3, sinv);
		}
		
		{
			int level = PlayerConfigurator.getCharacterPetDexterity(player, petSlot);
			int levelMax = PlayerConfigurator.getCharacterPetDexterityMax(player, petSlot);
			
			ItemStack sspd;
			if(level == 0)
				sspd = new ItemStack(Material.PORKCHOP);
			else if(level < levelMax)
				sspd = new ItemStack(Material.COOKED_PORKCHOP, level);
			else
				sspd = new ItemStack(Material.COOKED_BEEF, level);
			
			ItemMeta im = sspd.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Dexterity");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + level + " / " + levelMax + ChatColor.GRAY + " (Max: 10)");
			lores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerConfigurator.getCharacterPetDexterityExpNeeded(player, petSlot));
			lores.add("");
			lores.add(ChatColor.GRAY + "Adds 10% per Level to your Movement Speed.");
			lores.add(ChatColor.GRAY + "This Boost is only applied while your Pet is active.");
			lores.add("");
			lores.add(ChatColor.GRAY + "Drag Food-Items here, to feed your Pet.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Dexterity: " + ChatColor.DARK_AQUA + (level * 10) + "%");
			im.setLore(lores);
			sspd.setItemMeta(im);
			menu.setItem(4, sspd);
		}
		
		{
			int level = PlayerConfigurator.getCharacterPetAbsorption(player, petSlot);
			int levelMax = PlayerConfigurator.getCharacterPetAbsorptionMax(player, petSlot);
			
			ItemStack sinv;
			if(level == 0)
				sinv = new ItemStack(Material.PORKCHOP);
			else if(level < levelMax)
				sinv = new ItemStack(Material.COOKED_PORKCHOP, level);
			else
				sinv = new ItemStack(Material.COOKED_BEEF, level);
			
			ItemMeta im = sinv.getItemMeta();
			im.setDisplayName(ChatColor.DARK_GREEN + "Absorption");
			List<String> lores = new ArrayList<String>();
			lores.add(ChatColor.DARK_PURPLE + "Level: " + ChatColor.GREEN + level + " / " + levelMax + ChatColor.GRAY + " (Max: 10)");
			lores.add(ChatColor.DARK_PURPLE + "Food to next Level: " + ChatColor.GREEN + PlayerConfigurator.getCharacterPetAbsorptionExpNeeded(player, petSlot));
			lores.add("");
			lores.add(ChatColor.GRAY + "Absorbs 10% of your Armor per Level from Damage.");
			lores.add(ChatColor.GRAY + "This Boost is only applied while your Pet is active.");
			lores.add("");
			lores.add(ChatColor.GRAY + "Drag Food-Items here, to feed your Pet.");
			lores.add("");
			lores.add(ChatColor.WHITE + "Absorption: " + ChatColor.DARK_AQUA + (level * 10) + "%");
			im.setLore(lores);
			sinv.setItemMeta(im);
			menu.setItem(5, sinv);
		}
		
		{
			ItemStack move = HeadUtils.getPrevItem();
			ItemMeta im = move.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Move LEFT");
			move.setItemMeta(im);
			menu.setItem(6, move);
		}
		
		{
			ItemStack move = HeadUtils.getNextItem();
			ItemMeta im = move.getItemMeta();
			im.setDisplayName(ChatColor.YELLOW + "Move RIGHT");
			move.setItemMeta(im);
			menu.setItem(8, move);
		}
		
		{
			ItemStack back = new ItemStack(Material.PUFFERFISH_SPAWN_EGG);
			ItemMeta im = back.getItemMeta();
			im.setDisplayName(ChatColor.BLUE + "Back to Pet-Selection");
			back.setItemMeta(im);
			menu.setItem(7, back);
		}
		
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
		
		PlayerConfigurator.setCharacterActivePetSlot(player, petSlot);
		
		String command = "mm m spawn " + petType + " 1 " + player.getWorld().getName() +
			"," + location.getX() + "," + location.getY() + "," + location.getZ();
		
		WauzDebugger.log(player, command);
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		player.sendMessage(ChatColor.GREEN + petType + " was summoned!");
	}
	
	public static void discard(Player player, Integer petSlot) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
		pd.setWauzPlayerEventName("Release " + petType);
		pd.setWauzPlayerEvent(new WauzPlayerEventPetDelete(petSlot));
		WauzDialog.open(player);
	}
	
	public static void move(Player player, int fromIndex, int toIndex, boolean openMenu) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		String characterSlot = pd.getSelectedCharacterSlot();
		
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
		
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		String petType = PlayerConfigurator.getCharacterPetType(player, petSlot);
		pd.setWauzPlayerEventName("Breed " + petType);
		pd.setWauzPlayerEvent(new WauzPlayerEventPetBreed(petSlot));
		WauzDialog.open(player);
	}

}
