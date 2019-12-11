package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.system.WauzDebugger;

public class WauzRuneInserter {
	
	private static Map<String, WauzRune> runeMap = new HashMap<>();
	
	public static WauzRune getRune(String runeId) {
		return runeMap.get(runeId);
	}
	
	public static List<WauzRune> getAllRunes() {
		return new ArrayList<>(runeMap.values());
	}
	
	public static List<String> getAllRuneIds() {
		return new ArrayList<>(runeMap.keySet());
	}
	
	public static void registerRune(WauzRune rune) {
		runeMap.put(rune.getRuneId(), rune);
	}
	
	private Player player;
	
	private ItemStack equipmentItemStack;
	
	private EquipmentType equipmentType;
	
	private ItemStack runeItemStack;
	
	private String runeType;
	
	private double runeMightDecimal;
	
	public boolean insertRune(Player player, ItemStack equipmentItemStack, ItemStack runeItemStack) {
		this.player = player;
		this.equipmentItemStack = equipmentItemStack;
		this.runeItemStack = runeItemStack;
		
		if(!EquipmentUtils.hasRuneSocket(equipmentItemStack) || !TryToDetermineEquipmentType() || !TryToDetermineRuneType()) {
			return false;
		}
		
		boolean success = getRune(runeType).insertInto(equipmentItemStack, equipmentType, runeMightDecimal);
		if(success) {
			player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 0);
		}
		return success;
	}
	
	private boolean TryToDetermineEquipmentType() {
		equipmentType = EquipmentUtils.getEquipmentType(equipmentItemStack);
		WauzDebugger.log(player, "Equipment-Type: " + equipmentType);
		return equipmentType != EquipmentType.UNKNOWN;
	}
	
	private boolean TryToDetermineRuneType() {
		runeType = runeItemStack.getItemMeta().getDisplayName().split(" ")[2];
		WauzDebugger.log(player, "Rune-Type: " + runeType);
		int runeMight = EquipmentUtils.getRuneMight(runeItemStack);
		if(runeMight == 0) {
			return false;
		}
		runeMightDecimal = (double) ((double) runeMight / (double) 100);
		runeMightDecimal = runeMightDecimal * (1 + EquipmentUtils.getEnhancementRuneEffectivenessMultiplier(equipmentItemStack));
		WauzDebugger.log(player, "Rune-Might: " + runeMight + " (" + runeMightDecimal + ")");
		return true;
	}
	
}
