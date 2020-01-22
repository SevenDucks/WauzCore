package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
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
	 * The rarity of the rune.
	 */
	private Rarity rarity;
	
	/**
	 * The tier of the rune.
	 */
	private Tier tier;
	
	/**
	 * Identifies the item, based on the given event.
	 * After setting the name, additional methods are called to roll rarity and tier.
	 * Then a random rune type is taken and added to the item's display name.
	 * Finally the new name will be set and the item lores are generated.
	 * 
	 * @param player The player who identifies the item.
	 * @param runeItemStack The rune item stack, that is getting identified.
	 * 
	 * @see Rarity#getRandomRuneRarity()
	 * @see Tier#getRuneTier(String)
	 * @see WauzRuneIdentifier#generateIdentifiedRune()
	 */
	public void identifyRune(Player player, ItemStack runeItemStack) {
		this.player = player;
		this.runeItemStack = runeItemStack;	
		itemName = runeItemStack.getItemMeta().getDisplayName();
		
		rarity = Rarity.getRandomRuneRarity();
		tier = Tier.getRuneTier(itemName);
		
		List<String> runeNames = WauzRuneInserter.getAllRuneIds();
		String runeName = runeNames.get(random.nextInt(runeNames.size()));
		identifiedItemName = rarity.getColor() + "Rune of " + runeName;
		
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
			
		int power = (int) ((2 + random.nextDouble() / 2) * tier.getMultiplier() * rarity.getMultiplier());
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.WHITE + tier.getName() + " " + rarity.getName() + " Rune " + ChatColor.GREEN + rarity.getStars());
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

}
