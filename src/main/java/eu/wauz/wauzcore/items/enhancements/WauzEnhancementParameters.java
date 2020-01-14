package eu.wauz.wauzcore.items.enhancements;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;

/**
 * Parameters of equipment items, that can be affected by enhancements.
 * 
 * @author Wauzmons
 * 
 * @see WauzEquipmentEnhancer
 */
public class WauzEnhancementParameters {
	
	/**
	 * The level of the equipment's enhancement.
	 */
	private int enhancementLevel;
	
	/**
	 * The enhancement info, as displayed in lore.
	 */
	private String enhancementLore;
	
	/**
	 * The item meta of the equipment item stack.
	 */
	private ItemMeta itemMeta;
	
	/**
	 * The lores of the equipment item stack.
	 */
	private List<String> lores;
	
	/**
	 * The main stat of the equipment, as displayed in lore.
	 */
	private String mainStatString;
	
	/**
	 * The attack stat of the equipment.
	 */
	private int attackStat;
	
	/**
	 * The defense stat of the equipment.
	 */
	private int defenseStat;
	
	/**
	 * The durability stat of the equipment.
	 */
	private int durabilityStat;
	
	/**
	 * Initializes the parameters, for an enhancement of the given level.
	 * 
	 * @param enhancementLevel The level of the equipment's enhancement.
	 */
	public WauzEnhancementParameters(int enhancementLevel) {
		this.enhancementLevel = enhancementLevel;
	}

	/**
	 * @return The level of the equipment's enhancement.
	 */
	public int getEnhancementLevel() {
		return enhancementLevel;
	}

	/**
	 * @return The enhancement info, as displayed in lore.
	 */
	public String getEnhancementLore() {
		return enhancementLore;
	}

	/**
	 * @param enhancementLore The new enhancement info, as displayed in lore.
	 */
	public void setEnhancementLore(String enhancementLore) {
		this.enhancementLore = enhancementLore;
	}

	/**
	 * @return The item meta of the equipment item stack.
	 */
	public ItemMeta getItemMeta() {
		return itemMeta;
	}

	/**
	 * @param itemMeta The new item meta of the equipment item stack.
	 */
	public void setItemMeta(ItemMeta itemMeta) {
		this.itemMeta = itemMeta;
	}

	/**
	 * @return The lores of the equipment item stack.
	 */
	public List<String> getLores() {
		return lores;
	}

	/**
	 * @param lores The new lores of the equipment item stack.
	 */
	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	/**
	 * @return The main stat of the equipment, as displayed in lore.
	 */
	public String getMainStatString() {
		return mainStatString;
	}

	/**
	 * @param mainStatString The new main stat of the equipment, as displayed in lore.
	 */
	public void setMainStatString(String mainStatString) {
		this.mainStatString = mainStatString;
	}

	/**
	 * @return The attack stat of the equipment.
	 */
	public int getAttackStat() {
		return attackStat;
	}

	/**
	 * @param attackStat The new attack stat of the equipment.
	 */
	public void setAttackStat(int attackStat) {
		this.attackStat = attackStat;
	}

	/**
	 * @return The defense stat of the equipment.
	 */
	public int getDefenseStat() {
		return defenseStat;
	}

	/**
	 * @param defenseStat The new defense stat of the equipment.
	 */
	public void setDefenseStat(int defenseStat) {
		this.defenseStat = defenseStat;
	}

	/**
	 * @return The durability stat of the equipment.
	 */
	public int getDurabilityStat() {
		return durabilityStat;
	}

	/**
	 * @param durabilityStat The new durability stat of the equipment.
	 */
	public void setDurabilityStat(int durabilityStat) {
		this.durabilityStat = durabilityStat;
	}

}
