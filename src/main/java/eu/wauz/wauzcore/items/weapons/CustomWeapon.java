package eu.wauz.wauzcore.items.weapons;

import java.util.List;

import eu.wauz.wauzcore.items.CustomItem;

public interface CustomWeapon extends CustomItem {
	
	/**
	 * Determines if the custom weapon can have a skillgem slot.
	 * 
	 * @return If the custom weapon can have a skillgem slot.
	 */
	public boolean canHaveSkillSlot();
	
	/**
	 * Gets the lores to show on an instance of the custom weapon.
	 * 
	 * @param hasSkillSlot If the weapon has a skillgem slot.
	 * 
	 * @return The list of lores.
	 */
	public List<String> getCustomWeaponLores(boolean hasSkillSlot);

}
