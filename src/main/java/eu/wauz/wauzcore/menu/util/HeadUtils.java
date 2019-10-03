package eu.wauz.wauzcore.menu.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.deanveloper.skullcreator.SkullCreator;

import eu.wauz.wauzcore.items.ItemUtils;

public class HeadUtils {
	
// Main Menu
	
	public static ItemStack getMoneyItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTA2YjIxNTVmMTkzNzg3MDQyMzM4ZDA1Zjg0MDM5MWMwNWE2ZDNlODE2MjM5MDFiMjk2YmVlM2ZmZGQyIn19fQ==");
	}
	
	public static ItemStack getPortsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGUzZjlkYjdiNDU3MzE3MWEyYjU4MzZlNjliYzZhNjMxNDUxNGZmZjViYzc5NzQzMzE5ZmQxOTFmNTM0NDQifX19");
	}
	
	public static ItemStack getGuildItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYwYThhMGJlNWQzZjNmOTRiZDMyYTNlZDkyNjJlNjYyYTVkYTkxMTkxOWZlZTkxZWJjY2YzYjc5YmY2NTgifX19");
	}
	
	public static ItemStack getGroupItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhhMzMyNGM3N2ZkY2NkNTk3Nzc0MzA0YmJkYTE1NTE3ZWEyMzU5ZGU1Mzg5N2FlZDA5NTI4ZDFjNmVjOSJ9fX0=");
	}
	
	public static ItemStack getItemsItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM1NjNiOWI1ODI0MDlmNDFmMGUwNzgxYTk4M2FmZTNkOGZlMmZiZjM4M2M1M2E1ZDI3NDMxNTU1NjRkNjgifX19");
	}
	
	public static ItemStack getQuestItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVlOGQ2ZjVjYjdhMzVhNGRkYmRhNDZmMDQ3ODkxNWRkOWViYmNlZjkyNGViOGNhMjg4ZTkxZDE5YzhjYiJ9fX0=");
	}
	
	public static ItemStack getCraftItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNkYzBmZWI3MDAxZTJjMTBmZDUwNjZlNTAxYjg3ZTNkNjQ3OTMwOTJiODVhNTBjODU2ZDk2MmY4YmU5MmM3OCJ9fX0=");
	}
	
	public static ItemStack getTamesItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVhNzU5Zjk3OWI5YjhlYTgzMWNhN2UyZDY2ZGYxNDgyOTNmMWE1MTQ3OTgzYjUyYzQ4ZWZlMmMzMTVlIn19fQ==");
	}
	
	public static ItemStack getSkillItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5ZDIxMmJmYzBhMzRhNzA3NjNlMmE2OGRlNGZhOTI3MGNjZjJkODA3MWIxY2M4MzgxM2U0MTA2YjlkMWRmZSJ9fX0=");
	}
	
// Class Heads
	
	public static ItemStack getNephilimItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ0NzcyZGM0ZGVmMjIyMTllZTZkODg5Y2NkYzJmOTIzMmVlMjNkMzU2ZGQ5ZTRhZGNlYTVmNzJjYzBjNjg5In19fQ==");
	}
	
	public static ItemStack getCrusaderItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE5MTUxYTYyMzIxZWIzNGFlNGIzOWRmMzhjODE5MmVhNTliNGFkZDQ4OGNjYmQyMjI4ZTVjN2JhY2U5YzZhNCJ9fX0=");
	}
	
	public static ItemStack getAssassinItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdlZjljMDg2NWQ3ZTVlMmUxYzI5ODBjNjVlYTYwZWIxZTFhM2IwZmZhODdlYmY4NTI1MDEzODE2NzRiZTBkOCJ9fX0=");
	}
	
// Passive Skill Icons
	
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
	
	public static ItemStack getGuildHallItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQ1NTlkNzU0NjRiMmU0MGE1MThlNGRlOGU2Y2YzMDg1ZjBhM2NhMGIxYjcwMTI2MTRjNGNkOTZmZWQ2MDM3OCJ9fX0=");
	}
	
	public static ItemStack getItem() {
		return getPlayerHead("");
	}
	
// General Elements
	
	public static ItemStack getPrevItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIwZjZlOGFmNDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ==");
	}
	
	public static ItemStack getNextItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==");
	}
	
	public static ItemStack getConfirmItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=");
	}
	
	public static ItemStack getDeclineItem() {
		return getPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==");
	}
	
// Skull Creator
	
	public static ItemStack getPlayerHead(String base64) {
		return SkullCreator.withBase64(new ItemStack(Material.PLAYER_HEAD), base64);
	}
	
	public static boolean isHeadMenuItem(ItemStack itemStack, String itemName) {
		return ItemUtils.isMaterial(itemStack, Material.PLAYER_HEAD) && ItemUtils.isSpecificItem(itemStack, itemName);
	}
	
}
