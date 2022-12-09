package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

public class WauzRuneBuilder {
	
	/**
	 * The type of the rune to create.
	 */
	public WauzRune rune;
	
	/**
	 * The rune item stack.
	 */
	private ItemStack itemStack;
	
	/**
	 * The item meta of the rune item.
	 */
	private ItemMeta itemMeta;
	
	/**
	 * The might stat of the equipment.
	 */
	private int mightStat = 1;
	
	/**
	 * The display of the success stat of the equipment.
	 */
	private String successString;
	
	/**
	 * Constructs a builder for creating a new rune item.
	 * 
	 * @param rune The type of the rune to create.
	 */
	public WauzRuneBuilder(WauzRune rune) {
		this.rune = rune;
		itemStack = new ItemStack(Material.FIREWORK_STAR);
		itemMeta = itemStack.getItemMeta();
	}
	
	/**
	 * Adds a might stat to the rune item.
	 * 
	 * @param mightStat The value of the stat.
	 */
	public void addMightStat(int mightStat) {
		this.mightStat = mightStat;
	}
	
	/**
	 * Adds a success stat to the equipment item.
	 * 
	 * @param successStat The value of the stat.
	 */
	public void addSuccessStat(int successStat) {
		String icon = ChatColor.YELLOW + UnicodeUtils.ICON_DIAMETER + " ";
		successString = icon + ChatColor.WHITE + "Success Chance:" + ChatColor.YELLOW + " " + successStat + " " + ChatColor.GRAY + "%";
	}
	
	/**
	 * Generates the rune item stack, as configured in the builder.
	 * 
	 * @param tier The tier of the rune item.
	 * @param rarity The rarity of the rune item.
	 * 
	 * @return The generated rune item stack.
	 * 
	 * @see WauzRuneBuilder#addLoreIfNotBlank(List, String)
	 */
	public ItemStack generate(Tier tier, Rarity rarity) {
		String identifiedItemName = rarity.getColor() + "Rune of " + rune.getRuneId();
		Components.displayName(itemMeta, identifiedItemName);
		
		List<String> lores = new ArrayList<String>();
		int sellValue = (int) (mightStat * (Math.random() + 0.5) * 3 + 1);
		String mightIcon = ChatColor.YELLOW + UnicodeUtils.ICON_LIGHTNING + " ";
		String sellIcon = ChatColor.DARK_GREEN + UnicodeUtils.ICON_BULLSEYE + " ";
		lores.add(ChatColor.WHITE + tier.getName() + " " + rarity.getName() + " Rune " + ChatColor.GREEN + rarity.getStars());
		lores.add("");
		lores.add(mightIcon + ChatColor.WHITE + "Might:" + ChatColor.YELLOW + " " + mightStat);
		addLoreIfNotBlank(lores, successString);
		lores.add(sellIcon + ChatColor.WHITE + "Sell Value:" + ChatColor.DARK_GREEN + " " + sellValue);
		lores.add("");
		lores.add(ChatColor.GRAY + "Can be inserted into Equipment,");
		lores.add(ChatColor.GRAY + "which possesses an empty Rune Slot.");
		
		Components.lore(itemMeta, lores);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	/**
	 * Extends the lore list, if the given lore string is not blank.
	 * 
	 * @param lores The list of lores.
	 * @param lore The lore to add.
	 */
	private void addLoreIfNotBlank(List<String> lores, String lore) {
		if(StringUtils.isNotBlank(lore)) {
			lores.add(lore);
		}
	}

}
