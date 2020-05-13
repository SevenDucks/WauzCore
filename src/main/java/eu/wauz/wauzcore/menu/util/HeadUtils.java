package eu.wauz.wauzcore.menu.util;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.util.DeprecatedUtils;

/**
 * An util class to create item stacks of specific heads / skulls.
 * 
 * @author Wauzmons
 */
public class HeadUtils {
	
// Main Menu Categories
	
	/**
	 * @return An item stack representing a spirit orb.
	 */
	public static ItemStack getAbilityItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2JkY2Q2YTA3NGQ1NzBkYmIzNDNlYzRhZGVmMTIxMmE1Y2M1NDlkZDdiNDkwMWJkNjYyYWE2MzMxMWQzYTgzZiJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a decorated chest.
	 */
	public static ItemStack getCollectionItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM5NmJlNzg4NmViN2RmNzU1MjVhMzYzZTVmNTQ5NjI2YzIxMzg4ZjBmZGE5ODhhNmU4YmY0ODdhNTMifX19");
	}
	
	/**
	 * @return An item stack representing a villager.
	 */
	public static ItemStack getSocialItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQ5ZWU1ZDhiNThmZGRkMjdiYzY3OWMwNTQ4ZjU1YmFhODQ1ZjlmMWRmNWU4OGM3YzViZGE2ZWI5ZGYyYjM5OSJ9fX0=");
	}
	
// Ability Menu
	
	/**
	 * @return An item stack representing a portal.
	 */
	public static ItemStack getPortsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGUzZjlkYjdiNDU3MzE3MWEyYjU4MzZlNjliYzZhNjMxNDUxNGZmZjViYzc5NzQzMzE5ZmQxOTFmNTM0NDQifX19");
	}
	
	/**
	 * @return An item stack representing a workbench.
	 */
	public static ItemStack getCraftItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNkYzBmZWI3MDAxZTJjMTBmZDUwNjZlNTAxYjg3ZTNkNjQ3OTMwOTJiODVhNTBjODU2ZDk2MmY4YmU5MmM3OCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing an ender crystal.
	 */
	public static ItemStack getSkillItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5ZDIxMmJmYzBhMzRhNzA3NjNlMmE2OGRlNGZhOTI3MGNjZjJkODA3MWIxY2M4MzgxM2U0MTA2YjlkMWRmZSJ9fX0=");
	}

// Collection Menu
	
	/**
	 * @return An item stack representing a money bag.
	 */
	public static ItemStack getMoneyItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTA2YjIxNTVmMTkzNzg3MDQyMzM4ZDA1Zjg0MDM5MWMwNWE2ZDNlODE2MjM5MDFiMjk2YmVlM2ZmZGQyIn19fQ==");
	}
	
	/**
	 * @return An item stack representing a questlog book.
	 */
	public static ItemStack getQuestItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVlOGQ2ZjVjYjdhMzVhNGRkYmRhNDZmMDQ3ODkxNWRkOWViYmNlZjkyNGViOGNhMjg4ZTkxZDE5YzhjYiJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a golden trophy.
	 */
	public static ItemStack getAchievementsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNlNDU4MzFjMWVhODE3Zjc0NzdiY2FlYmZhM2QyZWUzYTkzNmVlOGVhMmI4YmRlMjkwMDZiN2U5YmRmNTgifX19");
	}
	
	/**
	 * @return An item stack representing a little bird.
	 */
	public static ItemStack getTamesItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVhNzU5Zjk3OWI5YjhlYTgzMWNhN2UyZDY2ZGYxNDgyOTNmMWE1MTQ3OTgzYjUyYzQ4ZWZlMmMzMTVlIn19fQ==");
	}
	
// Social Menu
	
	/**
	 * @return An item stack representing a letter.
	 */
	public static ItemStack getMailItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJmM2ZjZGNjZmZkOTYzZTQzMzQ4MTgxMDhlMWU5YWUzYTgwNTY2ZDBkM2QyZDRhYjMwNTFhMmNkODExMzQ4YyJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a blue group icon.
	 */
	public static ItemStack getGroupItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhhMzMyNGM3N2ZkY2NkNTk3Nzc0MzA0YmJkYTE1NTE3ZWEyMzU5ZGU1Mzg5N2FlZDA5NTI4ZDFjNmVjOSJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a green guild icon.
	 */
	public static ItemStack getGuildItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYwYThhMGJlNWQzZjNmOTRiZDMyYTNlZDkyNjJlNjYyYTVkYTkxMTkxOWZlZTkxZWJjY2YzYjc5YmY2NTgifX19");
	}
	
	/**
	 * @return An item stack representing a yellow friends icon.
	 */
	public static ItemStack getFriendsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTIzN2QwOTkyYzYzZjNhNDlmZDIzNmE1ZDY4NjgwYmRhNWQxN2MzOGJkZDhjYWQ0MTdjYzkxZDUxMDY4ZDIifX19");
	}
	
// Class Heads
	
	/**
	 * @return An item stack representing the head of a nephilim.
	 */
	public static ItemStack getNephilimItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ0NzcyZGM0ZGVmMjIyMTllZTZkODg5Y2NkYzJmOTIzMmVlMjNkMzU2ZGQ5ZTRhZGNlYTVmNzJjYzBjNjg5In19fQ==");
	}
	
	/**
	 * @return An item stack representing the head of a crusader.
	 */
	public static ItemStack getCrusaderItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE5MTUxYTYyMzIxZWIzNGFlNGIzOWRmMzhjODE5MmVhNTliNGFkZDQ4OGNjYmQyMjI4ZTVjN2JhY2U5YzZhNCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing the head of an assassin.
	 */
	public static ItemStack getAssassinItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdlZjljMDg2NWQ3ZTVlMmUxYzI5ODBjNjVlYTYwZWIxZTFhM2IwZmZhODdlYmY4NTI1MDEzODE2NzRiZTBkOCJ9fX0=");
	}
	
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
			skillItem = getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = getPlayerHead(empty);
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
			skillItem = getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = getPlayerHead(empty);
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
			skillItem = getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = getPlayerHead(empty);
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
			skillItem = getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = getPlayerHead(empty);
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
			skillItem = getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = getPlayerHead(empty);
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
			skillItem = getPlayerHead(inUse);
			skillItem.setAmount(Math.min(points, 64));
		}
		else {
			skillItem = getPlayerHead(empty);
		}
		return skillItem;
	}
	
// Guild Icons
	
	/**
	 * @return An item stack representing a castle.
	 */
	public static ItemStack getGuildHallItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ1NTlkNzU0NjRiMmU0MGE1MThlNGRlOGU2Y2YzMDg1ZjBhM2NhMGIxYjcwMTI2MTRjNGNkOTZmZWQ2MDM3OCJ9fX0=");
	}
	
// Achievement Icons
	
	/**
	 * @return An item stack representing a reaper skull.
	 */
	public static ItemStack getAchievementKillsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ1ZGM1NWRhYmE0ZTJkMzJhMjg0NDQ4YmI5ZTM4MzM1ZDg0M2Y5M2M0YTc3Njg4NWZkMzU2NTNmZGZjNzU4In19fQ==");
	}
	
	/**
	 * @return An item stack representing a red chest.
	 */
	public static ItemStack getAchievementIdentifiesItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIzMzQ4YTYxY2ZmYzc5ZWI0MmQ4NDlkMzc4MjI5NDMyYWE4MWQxOTg1NmIyNWZlYWFjNzUzODQ4NzEzN2E1In19fQ==");
	}
	
	/**
	 * @return An item stack representing a blue potion.
	 */
	public static ItemStack getAchievementManaItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQxYWQ3NGZmY2I2OTc5OGEwYmI0YTNhMGY3YjJiMzUzZDgxMWQxMWM4MjhiMzQxOTc4OTRkYTg1ZjQ5ZSJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a manuscript collection.
	 */
	public static ItemStack getAchievementQuestsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdhNTMwZjVjNTc0MmJmMTllMTc1YTRkNzhhZDQzNWFjMGY0Mzk2ZDNiNTQ2NGJkNjE4MmFiMzgyYWNhNDE3ZCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a burning furnace.
	 */
	public static ItemStack getAchievementCraftingItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE3YjhiNDNmOGM0YjVjZmViOTE5YzlmOGZlOTNmMjZjZWI2ZDJiMTMzYzJhYjFlYjMzOWJkNjYyMWZkMzA5YyJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a dinosaur egg.
	 */
	public static ItemStack getAchievementPetsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxYzU1YWI3MWI0YjdlNGVlZTA2NTk0MmVhYzZkNjQyMzMyMjgwMTQxZjk0MWNiMjJiNjg3MjhmOGI0MGY1In19fQ==");
	}
	
	/**
	 * @return An item stack representing a gold nugget.
	 */
	public static ItemStack getAchievementCoinsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ1ZjQ3ZmViNGQ3NWNiMzMzOTE0YmZkYjk5OWE0ODljOWQwZTMyMGQ1NDhmMzEwNDE5YWQ3MzhkMWUyNGI5In19fQ==");
	}
	
	/**
	 * @return An item stack representing an old clock.
	 */
	public static ItemStack getAchievementPlaytimeItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ3N2RhZmM4YzllYTA3OTk2MzMzODE3OTM4NjZkMTQ2YzlhMzlmYWQ0YzY2ODRlNzExN2Q5N2U5YjZjMyJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a cyan arrow.
	 */
	public static ItemStack getAchievementLevelsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGIyMjFjYjk2MDdjOGE5YmYwMmZlZjVkNzYxNGUzZWIxNjljYzIxOWJmNDI1MGZkNTcxNWQ1ZDJkNjA0NWY3In19fQ==");
	}
	
// Citizen Icons
	
	/**
	 * @return An item stack representing a heart icon.
	 */
	public static ItemStack getCitizenRelationItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDg2NWEzODNhNGRjYTA4YzVkYWFiNmFjZTQxMTc3MjA5NWVlNjMyN2MxMjI3MDNhMjIyNDQyZjIxNDFjMmMzZCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a speech bubble.
	 */
	public static ItemStack getCitizenTalkItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ4Y2UxY2YxOGFmMDVhNTc2ZDYwODEyMzAwMWI3OTFmZWRiNjIyOTExZWY4ZDM4YTMyMGRhM2JjYmY2ZmQyMCJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a green dollar sign.
	 */
	public static ItemStack getCitizenShopItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdiNjljOWRmYjYxMDY3Yzk0ODRkZjdkMDNlNjNmMTc4OTVjOWNkYTMzMjVjMmM1MzRhNWMyMjM1ODU1NzYzMSJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a yellow exclamation mark.
	 */
	public static ItemStack getCitizenQuestItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZlNTIyZDkxODI1MjE0OWU2ZWRlMmVkZjNmZTBmMmMyYzU4ZmVlNmFjMTFjYjg4YzYxNzIwNzIxOGFlNDU5NSJ9fX0=");
	}
	
	/**
	 * @return An item stack representing an inn building.
	 */
	public static ItemStack getCitizenInnItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc0MTBjMDdiZmJiNDE0NTAwNGJmOTE4YzhkNjMwMWJkOTdjZTEzMjcwY2UxZjIyMWQ5YWFiZWUxYWZkNTJhMyJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a command block.
	 */
	public static ItemStack getCitizenCommandItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc3MTJjYTY1NTEyODcwMWVhM2U1ZjI4ZGRkNjllNmE4ZTYzYWRmMjgwNTJjNTFiMmZkNWFkYjUzOGUxIn19fQ==");
	}
	
// General Elements
	
	/**
	 * @return An item stack representing an arrow backward.
	 */
	public static ItemStack getPrevItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIwZjZlOGFmNDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ==");
	}
	
	/**
	 * @return An item stack representing an arrow forward.
	 */
	public static ItemStack getNextItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==");
	}
	
	/**
	 * @return An item stack representing a green cgeckmark.
	 */
	public static ItemStack getConfirmItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=");
	}
	
	/**
	 * @return An item stack representing a red cross.
	 */
	public static ItemStack getDeclineItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==");
	}
	
// Skull Creator
	
	/**
	 * @return An item stack representing ...
	 */
	public static ItemStack getItem() {
		return getPlayerHead("");
	}
	
	/**
	 * Creates a player head item stack, based on a base64 string.
	 * 
	 * @param base64 A base64 string representing the data value of a skin.
	 * 
	 * @return A player head with the given skin.
	 */
	public static ItemStack getPlayerHead(String base64) {
		ItemStack headItemStack = new ItemStack(Material.PLAYER_HEAD);
		UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());
		return DeprecatedUtils.addPlayerHeadTexture(headItemStack, base64, hashAsId);
	}

	/**
	 * Name based equals check for head menu items.
	 * 
	 * @param itemStack The item to check.
	 * @param itemName The name to check for.
	 * 
	 * @return If the item is a head and has the given name.
	 */
	public static boolean isHeadMenuItem(ItemStack itemStack, String itemName) {
		return ItemUtils.isMaterial(itemStack, Material.PLAYER_HEAD) && ItemUtils.isSpecificItem(itemStack, itemName);
	}
	
}
