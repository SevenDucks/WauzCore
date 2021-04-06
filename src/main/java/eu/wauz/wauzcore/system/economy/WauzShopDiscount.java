package eu.wauz.wauzcore.system.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerRelationConfigurator;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.mobs.citizens.RelationLevel;
import eu.wauz.wauzcore.system.WauzRank;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A discount granted to a player for a certain shop.
 * 
 * @author Wauzmons
 *
 * @see WauzShopItem
 */
public class WauzShopDiscount {
	
	/**
	 * The name of the citizen who owns the shop. Can be null.
	 */
	private String citizenName;
	
	/**
	 * The discount from the relation to the shop owner.
	 */
	private double relationDiscount;
	
	/**
	 * The discount from the player rank.
	 */
	private double rankDiscount;
	
	/**
	 * The total discount granted to the player. Capped at 0.8 (80%).
	 */
	private double totalDiscount;
	
	/**
	 * Creates a discount granted to a player for a certain shop.
	 * All discount values are calculated automatically.
	 * 
	 * @param player The player who wants to buy items in the shop.
	 * @param citizenName The name of the citizen who owns the shop. Can be null.
	 */
	public WauzShopDiscount(Player player, String citizenName) {
		this.citizenName = citizenName;
		if(StringUtils.isNotBlank(citizenName)) {
			int relationProgress = PlayerRelationConfigurator.getRelationProgress(player, citizenName);
			double discountMultiplier = RelationLevel.getRelationLevel(relationProgress).getDiscountMultiplier();
			relationDiscount = 1.0 - discountMultiplier;
		}
		rankDiscount = WauzRank.getRank(player).getShopDiscount();
		totalDiscount = relationDiscount + rankDiscount;
		totalDiscount = totalDiscount > 0.8 ? 0.8 : totalDiscount;
	}
	
	/**
	 * @return The name of the citizen who owns the shop. Can be null.
	 */
	public String getCitizenName() {
		return citizenName;
	}

	/**
	 * @return The discount from the relation to the shop owner.
	 */
	public double getRelationDiscount() {
		return relationDiscount;
	}

	/**
	 * @return The discount from the player rank.
	 */
	public double getRankDiscount() {
		return rankDiscount;
	}

	/**
	 * @return The total discount granted to the player. Capped at 0.8 (80%).
	 */
	public double getTotalDiscount() {
		return totalDiscount;
	}
	
	/**
	 * Generates an item stack to show a list of all granted discounts.
	 * 
	 * @return The generated item stack.
	 */
	public ItemStack generateDiscountDisplay() {
		ItemStack discountItemStack = GenericIconHeads.getCitizenShopItem();
		ItemMeta discountItemMeta = discountItemStack.getItemMeta();
		Components.displayName(discountItemMeta, ChatColor.GREEN + "Shop Discount");
		List<String> discountLores = new ArrayList<String>();
		
		int relationDiscount = (int) Math.ceil(this.relationDiscount * 100.0);
		discountLores.add(ChatColor.GOLD + "" + relationDiscount + "%"
				+ ChatColor.YELLOW + " from Shop Owner Relation");
		
		int rankDiscount = (int) Math.ceil(this.rankDiscount * 100.0);
		discountLores.add(ChatColor.GOLD + "" + rankDiscount + "%"
				+ ChatColor.YELLOW + " from Player Rank");
		
		int totalDiscount = (int) Math.ceil(this.totalDiscount * 100.0);
		discountLores.add("");
		discountLores.add(ChatColor.GOLD + "" + totalDiscount + "%"
				+ ChatColor.YELLOW + " Total" + ChatColor.GRAY + " (Max: 80%)");
		
		discountItemMeta.setLore(discountLores);
		discountItemStack.setItemMeta(discountItemMeta);
		return discountItemStack;
	}

}
