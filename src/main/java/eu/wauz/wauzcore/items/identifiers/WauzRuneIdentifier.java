package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.system.ChatFormatter;
import net.md_5.bungee.api.ChatColor;

public class WauzRuneIdentifier {
	
	private Random random = new Random();
	
	private Player player;
	
	
	private ItemStack runeItemStack;
	
	private String itemName;
	
	private String identifiedItemName;
	
	
	private String rarityName;
	
	private String rarityStars;
	
	private ChatColor rarityColor;
	
	private double rarityMultiplier = 0;
	
	
	private String tierName;
	
	private double tierMultiplier = 0;
	
	public void identifyRune(InventoryClickEvent event) {
		player = (Player) event.getWhoClicked();
		runeItemStack = event.getCurrentItem();	
		itemName = runeItemStack.getItemMeta().getDisplayName();
		
		determineRarity();
		determineTier();
		
		List<String> runeNames = WauzRuneInserter.getAllRuneIds();
		String runeName = runeNames.get(random.nextInt(runeNames.size()));
		identifiedItemName = rarityColor + "Rune of " + runeName;
		
		generateIdentifiedRune();
	}
	
	private void generateIdentifiedRune() {
		ItemMeta itemMeta = runeItemStack.getItemMeta();
		itemMeta.setDisplayName(identifiedItemName);
			
		int power = (int) ((2 + random.nextDouble() / 1.5) * tierMultiplier * rarityMultiplier);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.WHITE + tierName + rarityName + "Rune " + rarityStars);
		lores.add("");
		lores.add(ChatColor.GRAY + "Can be inserted into Equipment,");
		lores.add(ChatColor.GRAY + "which possesses an empty Rune Slot.");
		lores.add("");
		lores.add("Might:" + ChatColor.YELLOW + " " + power);
		
		itemMeta.setLore(lores);	
		runeItemStack.setItemMeta(itemMeta);
		runeItemStack.setType(Material.FIREWORK_STAR);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	private void determineRarity() {
		int rarity = random.nextInt(1000);
		String x = ChatFormatter.ICON_DIAMOND;
				
		if(rarity <= 800) {
			rarityColor = ChatColor.GREEN;
			rarityName = "Whispering ";
			rarityStars = ChatColor.GREEN +x + ChatColor.GRAY +x +x;
			rarityMultiplier = 1.00;
		}
		else if(rarity <= 975) {
			rarityColor = ChatColor.BLUE;
			rarityName = "Screaming ";
			rarityStars = ChatColor.GREEN +x +x + ChatColor.GRAY +x;
			rarityMultiplier = 1.50;
		}
		else if(rarity <= 999) {
			rarityColor = ChatColor.GOLD;
			rarityName = "Deafening ";
			rarityStars = ChatColor.GREEN +x +x +x;
			rarityMultiplier = 2.00;
		}
	}
	
	private void determineTier() {
		if(itemName.contains("T1")) {
			tierMultiplier = 8;
			tierName = "Lesser" + ChatColor.GRAY + " T1 " + ChatColor.WHITE;
		}
		else if(itemName.contains("T2")) {
			tierMultiplier = 12;
			tierName = "Greater" + ChatColor.GRAY + " T2 " + ChatColor.WHITE;
		}
		else if(itemName.contains("T3")) {
			tierMultiplier = 16;
			tierName = "Angelic" + ChatColor.GRAY + " T3 " + ChatColor.WHITE;
		}
	}

}
