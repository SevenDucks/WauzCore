package eu.wauz.wauzcore.items.runes.insertion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ItemUtils;
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
	
	private String equipmentType;
	
	private ItemStack runeItemStack;
	
	private String runeType;
	
	private double runeMightDecimal;
	
	public boolean insertRune(Player player, ItemStack equipmentItemStack, ItemStack runeItemStack) {
		this.player = player;
		this.equipmentItemStack = equipmentItemStack;
		this.runeItemStack = runeItemStack;
		
		if(!ItemUtils.hasRuneSocket(equipmentItemStack) || !TryToDetermineEquipmentType() || !TryToDetermineRuneMight()) {
			return false;
		}
		
		boolean success = getRune(runeType).insertInto(equipmentItemStack, runeMightDecimal);
		if(success) {
			player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 0);
		}
		return success;
		
//		ItemMeta itemMeta = equipmentItemStack.getItemMeta();
//		List<String> lores = itemMeta.getLore();
//		List<String> newLores = new ArrayList<>();
//		boolean valid = false;
//		
//// Apply Rune Effect ~ POWER ~ THORNS ~
//		
//		if(runeType.contains("Power") || runeType.contains("Thorns")) {		
//			double bonusPower = 0;
//			
//			for(String lore : lores) {
//				
//				if(lore.contains("Attack:" + ChatColor.RED)) {
//					String[] val = lore.split(" ");
//					Integer attack = Integer.parseInt(val[1]);
//					bonusPower = attack * runeMightDecimal;
//					bonusPower = bonusPower + 1;
//					if(runeType.contains("Power")) {
//						lore = lore.replace(
//								ChatColor.RED + " " + attack,
//								ChatColor.RED + " " + (int) (attack + bonusPower));
//						WauzDebugger.log(player, "Bonus: " + bonusPower);
//						WauzDebugger.log(player, lore);
//					}
//				}
//				
//				else if(lore.contains("Defense:" + ChatColor.BLUE)) {
//					String[] val = lore.split(" ");
//					Integer defense = Integer.parseInt(val[1]);
//					bonusPower = defense * runeMightDecimal;
//					bonusPower = bonusPower + 1;
//					if(runeType.contains("Power")) {
//						lore = lore.replace(
//								ChatColor.BLUE + " " + defense,
//								ChatColor.BLUE + " " + (int) (defense + bonusPower));	
//						WauzDebugger.log(player, "Bonus: " + bonusPower);
//						WauzDebugger.log(player, lore);
//					}
//				}
//				
//				if(lore.contains(ChatColor.GREEN + "Empty") && !valid) {
//					
//					if(runeType.contains("Power")) {
//						if(equipmentType == "Weapon")
//							lore = ChatColor.YELLOW + "Power Rune (" + ChatColor.RED + "+" + (int) bonusPower 
//							+ " Atk" + ChatColor.YELLOW + ")";
//						else if(equipmentType == "Armor")
//							lore = ChatColor.YELLOW + "Power Rune (" + ChatColor.BLUE + "+" + (int) bonusPower 
//							+ " Def" + ChatColor.YELLOW + ")";
//					}
//					
//					else if(runeType.contains("Thorns")) {
//						if(equipmentType == "Armor")
//							bonusPower = bonusPower * 4;
//						lore = ChatColor.YELLOW + "Thorns Rune (" + ChatColor.GREEN + "+" + (int) bonusPower 
//						+ " Rfl" + ChatColor.YELLOW + ")";
//					}
//					
//					WauzDebugger.log(player, lore);
//					valid = true;
//				}
//				
//				newLores.add(lore);
//			}			
//		}
//		
//// Apply Rune Effect ~ KNOWLEDGE ~
//		
//		else if(runeType.contains("Knowledge")) {
//			double bonusKnowledge = ((runeMightDecimal * 50) < 1) ? (1) : (runeMightDecimal * 50);
//			
//			for(String lore : lores) {
//				
//				if(lore.contains(ChatColor.GREEN + "Empty") && !valid) {
//					lore = ChatColor.YELLOW + "Knowledge Rune (" + ChatColor.AQUA + "+" + (int) bonusKnowledge 
//							+ "% Exp" + ChatColor.YELLOW + ")";
//					
//					WauzDebugger.log(player, lore);
//					valid = true;
//				}
//				
//				newLores.add(lore);
//			}			
//		}
//		
//		if(valid) {
//			itemMeta.setLore(newLores);
//			equipmentItemStack.setItemMeta(itemMeta);
//			
//			player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 0);
//		}
//		return valid;
	}
	
	private boolean TryToDetermineEquipmentType() {
		equipmentType = ItemUtils.getEquipmentType(equipmentItemStack);
		WauzDebugger.log(player, "Equipment-Type: " + equipmentType);
		return equipmentType != "Unknown";
	}
	
	private boolean TryToDetermineRuneMight() {
		runeType = runeItemStack.getItemMeta().getDisplayName();
		int runeMight = ItemUtils.getRuneMight(runeItemStack);
		if(runeMight == 0) {
			return false;
		}
		runeMightDecimal = (double) ((double) runeMight / (double) 100);
		runeMightDecimal = runeMightDecimal * (1 + ItemUtils.getEnhancementRuneEffectivenessMultiplier(equipmentItemStack));
		WauzDebugger.log(player, "Rune-Might: " + runeMight + " (" + runeMightDecimal + ")");
		return true;
	}
	
}
