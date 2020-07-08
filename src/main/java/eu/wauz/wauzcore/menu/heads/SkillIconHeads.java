package eu.wauz.wauzcore.menu.heads;

import org.bukkit.inventory.ItemStack;

/**
 * A collection of heads / skulls usable as icons for skills.
 * 
 * @author Wauzmons
 */
public class SkillIconHeads {
	
// Passive Skill Icons

	/**
	 * @param points The statpoints spent in this category.
	 * 
	 * @return An item stack representing the health stat.
	 */
	public static ItemStack getSkillHealthItem(int points) {
		String inUse = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ5ZTMzZDc0M2VlMzQyMjQzMzEyMjkxY2NkY2ZmZDdmY2NhNWJkYzhhNmE4NDU5ZWU4ZTYyY2U1N2FkZDcifX19";
		String empty = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2JhOWMzM2E5NWZhMWU1MTlmODVhNDFjYTU2Nzk5Mzg0ZGI0MWZlN2UxZDdhNzkxNzUxZWNlOWJiYWU1ZDI3ZiJ9fX0=";
		ItemStack skillItem = null;
		if(points > 0) {
			skillItem = HeadUtils.getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = HeadUtils.getPlayerHead(empty);
		}
		return skillItem;
	}
	
	/**
	 * @param points The statpoints spent in this category.
	 * 
	 * @return An item stack representing the trading stat.
	 */
	public static ItemStack getSkillTradingItem(int points) {
		String inUse = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNjNDE0ZmJiNmE5MzU5NzgzMzE4Y2EwYmYxN2E4Njg4ZjkyYmZhMzk4MDI3N2FiYmZkZjY5ZjFlZGViMzQifX19";
		String empty = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNmYjUwZmU3NTU5YmM5OWYxM2M0NzM1NmNjZTk3ZmRhM2FhOTIzNTU3ZmI1YmZiMTdjODI1YWJmNGIxZDE5In19fQ==";
		ItemStack skillItem = null;
		if(points > 0) {
			skillItem = HeadUtils.getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = HeadUtils.getPlayerHead(empty);
		}
		return skillItem;
	}
	
	/**
	 * @param points The statpoints spent in this category.
	 * 
	 * @return An item stack representing the luck stat.
	 */
	public static ItemStack getSkillLuckItem(int points) {
		String inUse = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWE4MzdkODRlYjM2M2ZmYmI5MzM0OTZlMzlkOTIwNjQ5NmZmNzU3Njk4MjIxOWVhMjVjZjVkNTE1YmM1YyJ9fX0=";
		String empty = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRhZmZhNDU1YjdmNTgyMTdkZThhY2JiZDkyMDFjOWVhODdjMTM0YWEzNTYyNTQ5NGY1ZDNmNjVjZTk0NiJ9fX0=";
		ItemStack skillItem = null;
		if(points > 0) {
			skillItem = HeadUtils.getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = HeadUtils.getPlayerHead(empty);
		}
		return skillItem;
	}
	
	/**
	 * @param points The statpoints spent in this category.
	 * 
	 * @return An item stack representing the magic stat.
	 */
	public static ItemStack getSkillMagicItem(int points) {
		String inUse = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZmOTczM2NlZGIyOWUxNTA3NDZlOWMyYmViOTg4ZjUzMWY4ZjY5MGJkMzg2ZTZiMmM4NDdkNDAxODkwZjkifX19";
		String empty = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhkZWM0NjY2YjRjNjdkODc1OTcxNGM4NTcxNGJlNmVhNGUzOWZmOTYyODg0OWY5OGI1MTRlZGYxYzNlNDY4MCJ9fX0=";
		ItemStack skillItem = null;
		if(points > 0) {
			skillItem = HeadUtils.getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = HeadUtils.getPlayerHead(empty);
		}
		return skillItem;
	}
	
	/**
	 * @param points The statpoints spent in this category.
	 * 
	 * @return An item stack representing the strength stat.
	 */
	public static ItemStack getSkillStrengthItem(int points) {
		String inUse = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTcyOWQwM2Y3YjY0M2VjNmZiMjVlMzkyYTg4ZjA1OWU2NjM5YjZkYzU5M2UwYmE4NDU4NzlmY2VjZjMzOWJlIn19fQ==";
		String empty = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDcxMDEzODQxNjUyODg4OTgxNTU0OGI0NjIzZDI4ZDg2YmJiYWU1NjE5ZDY5Y2Q5ZGJjNWFkNmI0Mzc0NCJ9fX0=";
		ItemStack skillItem = null;
		if(points > 0) {
			skillItem = HeadUtils.getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = HeadUtils.getPlayerHead(empty);
		}
		return skillItem;
	}
	
	/**
	 * @param points The statpoints spent in this category.
	 * 
	 * @return An item stack representing the agility stat.
	 */
	public static ItemStack getSkillAgilityItem(int points) {
		String inUse = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEwODAyNzM1M2Q2ZWU4ZDcxZjI3NmI2YTI2NzllZTIzMzE2ZmIyMTVkZTI1MDNlZDViYTZjY2FjNGUwIn19fQ==";
		String empty = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0MTc0ODEyMTYyNmYyMmFlMTZhNGM2NjRjNzMwMWE5ZjhlYTU5MWJmNGQyOTg4ODk1NzY4MmE5ZmRhZiJ9fX0=";
		ItemStack skillItem = null;
		if(points > 0) {
			skillItem = HeadUtils.getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = HeadUtils.getPlayerHead(empty);
		}
		return skillItem;
	}
	
// Guild Skill Icons
	
	/**
	 * @return An item stack representing a castle.
	 */
	public static ItemStack getGuildHallItem() {
		return HeadUtils.getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ1NTlkNzU0NjRiMmU0MGE1MThlNGRlOGU2Y2YzMDg1ZjBhM2NhMGIxYjcwMTI2MTRjNGNkOTZmZWQ2MDM3OCJ9fX0=");
	}

}
