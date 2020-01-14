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
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import net.md_5.bungee.api.ChatColor;

/**
 * Typed identifier, used for identifying rune items.
 * 
 * @author Wauzmons
 * 
 * @see WauzIdentifier
 */
public class WauzRuneIdentifier {
	
	/**
	 * A random instance, for rolling item stats and rarities.
	 */
	private Random random = new Random();
	
	/**
	 * The player who identifies the item.
	 */
	private Player player;
	
	/**
	 * The rune item stack, that is getting identified.
	 */
	private ItemStack runeItemStack;
	
	/**
	 * The initial display name of the rune item stack.
	 */
	private String itemName;
	
	/**
	 * The final display name of the rune item stack.
	 */
	private String identifiedItemName;
	
	
	/**
	 * The name of the rune's rarity.
	 */
	private String rarityName;
	
	/**
	 * The stars of the rune's rarity.
	 */
	private String rarityStars;
	
	/**
	 * The color of the rune's rarity.
	 */
	private ChatColor rarityColor;
	
	/**
	 * The multiplier of the rune's rarity.
	 */
	private double rarityMultiplier = 0;
	
	
	/**
	 * The name of the rune's tier.
	 */
	private String tierName;
	
	/**
	 * The multiplier of the rune's tier.
	 */
	private double tierMultiplier = 0;
	
	/**
	 * Identifies the item, based on the given event.
	 * After setting the name, additional methods are called to roll rarity and tier.
	 * Then a random rune type is taken and added to the item's display name.
	 * Finally the new name will be set and the item lores are generated.
	 * 
	 * @param event The inventory event, which triggered the identifying.
	 * 
	 * @see WauzRuneIdentifier#determineRarity()
	 * @see WauzRuneIdentifier#determineTier()
	 * @see WauzRuneIdentifier#generateIdentifiedRune()
	 */
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
	
	/**
	 * Generates the rune, by calculating the might stat and setting lore, flags and meta.
	 * Rarity, tier and so on have to be set before calling this method.
	 * Plays an anvil sound to the player, when the identifying has been completed.
	 */
	private void generateIdentifiedRune() {
		ItemMeta itemMeta = runeItemStack.getItemMeta();
		itemMeta.setDisplayName(identifiedItemName);
			
		int power = (int) ((2 + random.nextDouble() / 2) * tierMultiplier * rarityMultiplier);
		
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
	
	/**
	 * Determines the random rarity with a multiplier of 1-2 on a scale of 1-3 stars.
	 * Automatically sets the rarity name and color and creates the star string.
	 */
	private void determineRarity() {
		int rarity = random.nextInt(1000) + 1;
		String x = UnicodeUtils.ICON_DIAMOND;
				
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
		else if(rarity <= 1000) {
			rarityColor = ChatColor.GOLD;
			rarityName = "Deafening ";
			rarityStars = ChatColor.GREEN +x +x +x;
			rarityMultiplier = 2.00;
		}
	}
	
	/**
	 * Determines the tier, based on the item name, with a multiplier of 6, 9 or 12 on a scale of T1 to T3.
	 * Automatically sets the tier name and level.
	 */
	private void determineTier() {
		if(itemName.contains("T1")) {
			tierMultiplier = 6;
			tierName = "Lesser" + ChatColor.GRAY + " T1 " + ChatColor.WHITE;
		}
		else if(itemName.contains("T2")) {
			tierMultiplier = 9;
			tierName = "Greater" + ChatColor.GRAY + " T2 " + ChatColor.WHITE;
		}
		else if(itemName.contains("T3")) {
			tierMultiplier = 12;
			tierName = "Angelic" + ChatColor.GRAY + " T3 " + ChatColor.WHITE;
		}
	}

}
