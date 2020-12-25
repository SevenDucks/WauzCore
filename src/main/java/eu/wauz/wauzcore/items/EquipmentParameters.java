package eu.wauz.wauzcore.items;

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
	 * The attack stat of the equipment.
	 */
	protected int attackStat;
	
	/**
	 * The defense stat of the equipment.
	 */
	protected int defenseStat;
	
	/**
	 * The durability stat of the equipment.
	 */
	protected int durabilityStat;
	
	/**
	 * The swiftness stat of the equipment.
	 */
	protected int swiftnessStat;
	
	/**
	 * The speed stat of the equipment.
	 */
	protected double speedStat;
	
	/**
	 * @return The type of the equipment.
	 */
	public EquipmentType getEquipmentType() {
		return equipmentType.getType();
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
	
	/**
	 * @return The swiftness stat of the equipment.
	 */
	public int getSwiftnessStat() {
		return swiftnessStat;
	}
	
	/**
	 * @param swiftnessStat The new swiftness stat of the equipment.
	 */
	public void setSwiftnessStat(int swiftnessStat) {
		this.swiftnessStat = swiftnessStat;
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

}
