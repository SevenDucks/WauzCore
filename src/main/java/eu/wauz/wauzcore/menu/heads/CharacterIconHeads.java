package eu.wauz.wauzcore.menu.heads;

import org.bukkit.inventory.ItemStack;

/**
 * A collection of heads / skulls usable as icons for characters.
 * 
 * @author Wauzmons
 */
public class CharacterIconHeads {
	
// Class Heads

	/**
	 * @return An item stack representing the head of a warrior.
	 */
	public static ItemStack getWarriorItem() {
		return HeadUtils.getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE5MTUxYTYyMzIxZWIzNGFlNGIzOWRmMzhjODE5MmVhNTliNGFkZDQ4OGNjYmQyMjI4ZTVjN2JhY2U5YzZhNCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing the head of a rogue.
	 */
	public static ItemStack getRogueItem() {
		return HeadUtils.getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdlZjljMDg2NWQ3ZTVlMmUxYzI5ODBjNjVlYTYwZWIxZTFhM2IwZmZhODdlYmY4NTI1MDEzODE2NzRiZTBkOCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing the head of a mage.
	 */
	public static ItemStack getMageItem() {
		return HeadUtils.getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ0NzcyZGM0ZGVmMjIyMTllZTZkODg5Y2NkYzJmOTIzMmVlMjNkMzU2ZGQ5ZTRhZGNlYTVmNzJjYzBjNjg5In19fQ==");
	}
	
	/**
	 * @return An item stack representing the head of a cleric.
	 */
	public static ItemStack getClericItem() {
		return HeadUtils.getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI1NTU3YmQ1YTgyYjBjMjEwNDVlMzIyMGMzMzgwYWM5ZTBlZDQ2ZGM4M2I1ZjAwYTlkZjJlM2QzZTUwNDVhMiJ9fX0=");
	}

}
