package eu.wauz.wauzcore.items;

import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;

/**
 * Parameters of equipment items, for usage in identifiers.
 * 
 * @author Wauzmons
 * 
 * @see WauzEquipmentIdentifier
 */
public class EquipmentParameters {
	
	/**
	 * The type of the equipment.
	 */
	protected WauzEquipment equipmentType;
	
	/**
	 * The item meta of the equipment item stack.
	 */
	protected ItemMeta itemMeta;
	
	/**
	 * The lores of the equipment item stack.
	 */
	protected List<String> lores;
	
	/**
	 * The main stat of the equipment, as displayed in lore.
	 */
	protected String mainStatString;
	
	/**
	 * The attack stat of the equipment.
	 */
	protected int attackStat;
	
	/**
	 * The defense stat of the equipment.
	 */
	protected int defenseStat;
	
	/**
	 * The speed stat of the equipment.
	 */
	protected double speedStat;
	
	/**
	 * The durability stat of the equipment.
	 */
	protected int durabilityStat;
	
	/**
	 * The level of the equipment's enhancement.
	 */
	protected int enhancementLevel = 0;
	
	/**
	 * @return The type of the equipment.
	 */
	public EquipmentType getEquipmentType() {
		return equipmentType.getType();
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
	 * @return The speed stat of the equipment.
	 */
	public double getSpeedStat() {
		return speedStat;
	}

	/**
	 * @param speedStat The new speed stat of the equipment.
	 */
	public void setSpeedStat(double speedStat) {
		this.speedStat = speedStat;
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
	
	/**
	 * @return The level of the equipment's enhancement.
	 */
	public int getEnhancementLevel() {
		return enhancementLevel;
	}

}
