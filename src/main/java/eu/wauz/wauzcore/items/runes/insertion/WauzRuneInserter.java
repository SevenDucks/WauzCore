package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A helper class for registering runes and inserting them into items.
 * 
 * @author Wauzmons
 * 
 * @see WauzRune
 */
public class WauzRuneInserter {
	
	/**
	 * A map of all runes, indexed by rune id.
	 */
	private static Map<String, WauzRune> runeMap = new HashMap<>();
	
	/**
	 * Gets a rune for given id from the map.
	 * 
	 * @param runeId The id of the rune.
	 * 
	 * @return The rune or null, if not found.
	 */
	public static WauzRune getRune(String runeId) {
		return runeMap.get(runeId);
	}
	
	/**
	 * @return A list of all runes.
	 */
	public static List<WauzRune> getAllRunes() {
		return new ArrayList<>(runeMap.values());
	}
	
	/**
	 * @return A list of all rune ids.
	 */
	public static List<String> getAllRuneIds() {
		return new ArrayList<>(runeMap.keySet());
	}
	
	/**
	 * Registers a rune.
	 * 
	 * @param rune The rune to register.
	 */
	public static void registerRune(WauzRune rune) {
		runeMap.put(rune.getRuneId(), rune);
	}
	
	/**
	 * The player that is inserting the rune.
	 */
	private Player player;
	
	/**
	 * The equipment item stack that the rune is inserted into.
	 */
	private ItemStack equipmentItemStack;
	
	/**
	 * The type of the equipment.
	 */
	private EquipmentType equipmentType;
	
	/**
	 * The rune item stack that is getting inserted.
	 */
	private ItemStack runeItemStack;
	
	/**
	 * The type of the rune.
	 */
	private String runeType;
	
	/**
	 * The might of the rune, determinig its power.
	 */
	private double runeMightDecimal;
	
	/**
	 * Tries to insert a rune into the given equipment item stack.
	 * Returns false if the item has no rune slots or the rune is invalid.
	 * The item can get destroyed, if the rune's success chance is below 100%.
	 * 
	 * @param player The player that is inserting the rune.
	 * @param equipmentItemStack The equipment item stack that the rune is inserted into.
	 * @param runeItemStack The rune item stack that is getting inserted.
	 * 
	 * @return If the action was successful (also true when the item got destroyed).
	 * 
	 * @see EquipmentUtils#getRuneSuccessChance(ItemStack)
	 */
	public boolean insertRune(Player player, ItemStack equipmentItemStack, ItemStack runeItemStack) {
		this.player = player;
		this.equipmentItemStack = equipmentItemStack;
		this.runeItemStack = runeItemStack;
		
		if(!EquipmentUtils.hasRuneSocket(equipmentItemStack) || !TryToDetermineEquipmentType() || !TryToDetermineRuneType()) {
			return false;
		}
		
		int successChance = EquipmentUtils.getRuneSuccessChance(runeItemStack);
		if(!Chance.percent(successChance != 0 ? successChance : 50)) {
			String displayName = equipmentItemStack.getItemMeta().getDisplayName();
			equipmentItemStack.setAmount(0);
			player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_BREAK, 0);
			player.sendMessage(ChatColor.RED + "Your " + displayName + ChatColor.RED + " just broke due to an unstable rune!");
			return true;
		}
		
		boolean success = getRune(runeType).insertInto(equipmentItemStack, equipmentType, runeMightDecimal);
		if(success) {
			player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 0);
		}
		return success;
	}
	
	/**
	 * Sets the equipment type for the rune inserter.
	 * 
	 * @return If the type is known.
	 */
	private boolean TryToDetermineEquipmentType() {
		equipmentType = EquipmentUtils.getEquipmentType(equipmentItemStack);
		WauzDebugger.log(player, "Equipment-Type: " + equipmentType);
		return equipmentType != EquipmentType.UNKNOWN;
	}
	
	/**
	 * Sets the rune type and might for the rune inserter.
	 * 
	 * @return If the might is greater than zero.
	 */
	private boolean TryToDetermineRuneType() {
		runeType = runeItemStack.getItemMeta().getDisplayName().split(" ")[2];
		WauzDebugger.log(player, "Rune-Type: " + runeType);
		int runeMight = EquipmentUtils.getRuneMight(runeItemStack);
		if(runeMight <= 0) {
			return false;
		}
		runeMightDecimal = (double) ((double) runeMight / (double) 100);
		runeMightDecimal = runeMightDecimal * (1 + EquipmentUtils.getEnhancementRuneEffectivenessMultiplier(equipmentItemStack));
		WauzDebugger.log(player, "Rune-Might: " + runeMight + " (" + runeMightDecimal + ")");
		return true;
	}
	
}
