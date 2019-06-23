package eu.wauz.wauzcore.data;

import java.util.List;

import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

public class CraftingConfigurator extends GlobalConfigurationUtils {
	
// General Parameters
	
	public static String getItemMaterial(int itemIndex) {
		return mainConfigGetString("Crafting", itemIndex + ".type");
	}
	
	public static int getItemAmount(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".amount");
	}
	
	public static String getItemName(int itemIndex) {
		return mainConfigGetString("Crafting", itemIndex + ".name");
	}
	
	public static List<String> getItemLores(int itemIndex) {
		return mainConfigGetStringList("Crafting", itemIndex + ".lores");
	}
	
	public static int getItemLevel(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".level");
	}
	
	public static int getItemCraftingCostsAmount(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".cost.amount");
	}
	
// Potions
	
	public static PotionEffectType getPotionType(int itemIndex) {
		return PotionEffectType.getByName(mainConfigGetString("Crafting", itemIndex + ".effect.type"));
	}
	
	public static int getPotionDuration(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".effect.duration");
	}
	
	public static int getPotionLevel(int itemIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".effect.level");
	}
	
// Crafting Costs
	
	public static String getCraftingCostItemString(int itemIndex, int requirementIndex) {
		return mainConfigGetString("Crafting", itemIndex + ".cost." + requirementIndex + "." + "item");
	}
	
	public static int getCraftingCostItemAmount(int itemIndex, int requirementIndex) {
		return mainConfigGetInt("Crafting", itemIndex + ".cost." + requirementIndex + "." + "amount");
	}

}
