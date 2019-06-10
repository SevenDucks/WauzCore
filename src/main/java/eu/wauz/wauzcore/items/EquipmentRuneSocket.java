package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class EquipmentRuneSocket {
	
	public static boolean insertRune(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		ItemStack itemStack = event.getCurrentItem();
		ItemMeta im = itemStack.getItemMeta();
		String itemMaterial = itemStack.getType().toString();
		String itemType = "Unknown";
		
		ItemStack rune = event.getWhoClicked().getItemOnCursor();
		String runeName = rune.getItemMeta().getDisplayName();
		int runePower = 0;
		
		boolean valid = false;
		if(!ItemUtils.hasLore(itemStack))
			return valid;
	
// Set Rune Might and Item Type
		
		List<String> lores = rune.getItemMeta().getLore();
		for(String lore : lores) {
			if(lore.contains("Might")) {
				String[] val = lore.split(" ");
				runePower = runePower + Integer.parseInt(val[1]);
				break;
			}
		}
		
		if(itemMaterial.contains("CHESTPLATE"))
			itemType = "Armor";
		else if(itemMaterial.contains("SWORD") || itemMaterial.contains("AXE") || itemMaterial.contains("HOE")) {
			itemType = "Weapon";
		}
		
		if(itemType == "Unknown" || runePower == 0) return false;
		double runeDecimal = (double) ((double) runePower / (double) 100);
		
		WauzDebugger.log(player, "Rune-Power: " + runePower + " (" + runeDecimal + ")");
		WauzDebugger.log(player, "Item-Type: " + itemType);
		
// Apply Rune Effect ~ POWER ~ THORNS ~
		
		if(runeName.contains("Power") || runeName.contains("Thorns")) {		
			lores = itemStack.getItemMeta().getLore();
			List<String> newLores = new ArrayList<>();
			double bonusPower = 00;
			
			for(String lore : lores) {
				
				if(lore.contains("Attack:" + ChatColor.RED)) {
					String[] val = lore.split(" ");
					Integer attack = Integer.parseInt(val[1]);
					bonusPower = attack * runeDecimal;
					bonusPower = bonusPower + 1;
					if(runeName.contains("Power")) {
						lore = lore.replace(
								ChatColor.RED + " " + attack,
								ChatColor.RED + " " + (int) (attack + bonusPower));
						WauzDebugger.log(player, "Bonus: " + bonusPower);
						WauzDebugger.log(player, lore);
					}
				}
				
				else if(lore.contains("Defense:" + ChatColor.BLUE)) {
					String[] val = lore.split(" ");
					Integer defense = Integer.parseInt(val[1]);
					bonusPower = defense * runeDecimal;
					bonusPower = bonusPower + 1;
					if(runeName.contains("Power")) {
						lore = lore.replace(
								ChatColor.BLUE + " " + defense,
								ChatColor.BLUE + " " + (int) (defense + bonusPower));	
						WauzDebugger.log(player, "Bonus: " + bonusPower);
						WauzDebugger.log(player, lore);
					}
				}
				
				if(lore.contains(ChatColor.GREEN + "Empty") && !valid) {
					
					if(runeName.contains("Power")) {
						if(itemType == "Weapon")
							lore = ChatColor.YELLOW + "Power Rune (" + ChatColor.RED + "+" + (int) bonusPower 
							+ " Atk" + ChatColor.YELLOW + ")";
						else if(itemType == "Armor")
							lore = ChatColor.YELLOW + "Power Rune (" + ChatColor.BLUE + "+" + (int) bonusPower 
							+ " Def" + ChatColor.YELLOW + ")";
					}
					
					else if(runeName.contains("Thorns")) {
						if(itemType == "Armor")
							bonusPower = bonusPower * 4;
						lore = ChatColor.YELLOW + "Thorns Rune (" + ChatColor.GREEN + "+" + (int) bonusPower 
						+ " Rfl" + ChatColor.YELLOW + ")";
					}
					
					WauzDebugger.log(player, lore);
					valid = true;
				}
				
				newLores.add(lore);
			}			
			if(valid) {
				im.setLore(newLores);
				itemStack.setItemMeta(im);
				return true;
			} else return false;
		}
		
// Apply Rune Effect ~ KNOWLEDGE ~
		
		else if(runeName.contains("Knowledge")) {
			lores = itemStack.getItemMeta().getLore();
			List<String> newLores = new ArrayList<>();
			double bonusKnowledge = ((runeDecimal * 50) < 1) ? (1) : (runeDecimal * 50);
			
			for(String lore : lores) {
				
				if(lore.contains(ChatColor.GREEN + "Empty") && !valid) {
					lore = ChatColor.YELLOW + "Knowledge Rune (" + ChatColor.AQUA + "+" + (int) bonusKnowledge 
							+ "% Exp" + ChatColor.YELLOW + ")";
					
					WauzDebugger.log(player, lore);
					valid = true;
				}
				
				newLores.add(lore);
			}			
			if(valid) {
				im.setLore(newLores);
				itemStack.setItemMeta(im);
				return true;
			} else return false;
		}
		
		return false;
	}
	
	public static boolean insertSkillgem(InventoryClickEvent event) {
		ItemStack skillgem = event.getWhoClicked().getItemOnCursor();
		String skillName = StringUtils.substringAfter(skillgem.getItemMeta().getDisplayName(), ": " + ChatColor.LIGHT_PURPLE);
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.playerSkillMap.get(skillName);
		
		ItemStack itemStack = event.getCurrentItem();
		ItemMeta im = itemStack.getItemMeta();
		
		if(ItemUtils.hasSkillgemSocket(itemStack)) {
			List<String> newLores = new ArrayList<>();
			for(String lore : im.getLore()) {
				if(lore.contains(ChatColor.DARK_RED + "Empty")) {
					newLores.add(ChatColor.WHITE + "Skillgem (" + ChatColor.LIGHT_PURPLE + skill.getSkillId() + ChatColor.WHITE + ")");
					newLores.add(ChatColor.WHITE + skill.getSkillDescription());
					lore = ChatColor.WHITE + skill.getSkillStats();
				}
				newLores.add(lore);
			}
			im.setLore(newLores);
			itemStack.setItemMeta(im);
			return true;
		}
		
		return false;
	}
	
	public static boolean clearAllSockets(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		ItemStack itemStack = event.getCurrentItem();
		ItemMeta im = itemStack.getItemMeta();
		
		boolean valid = false;
		if(!ItemUtils.hasLore(itemStack))
			return valid;
		
		int skipLines = 0;
		int atkManus = ItemUtils.getRuneAtkBoost(itemStack);
		int defManus = ItemUtils.getRuneDefBoost(itemStack);
		
		List<String> newLores = new ArrayList<>();
		for(String lore : im.getLore()) {
			if(skipLines > 0) {
				skipLines--;
				continue;
			}
			else if(lore.contains("Attack") && atkManus > 0) {
				String[] val = lore.split(" ");
				Integer attack = Integer.parseInt(val[1]);
				int newValue = attack - atkManus;
				lore = lore.replace(
						ChatColor.RED + " " + attack,
						ChatColor.RED + " " + newValue);
				WauzDebugger.log(player, "Manus: " + atkManus);
			}
			else if(lore.contains("Defense") && defManus > 0) {
				String[] val = lore.split(" ");
				Integer defense = Integer.parseInt(val[1]);
				int newValue = defense - defManus;
				lore = lore.replace(
						ChatColor.BLUE + " " + defense,
						ChatColor.BLUE + " " + newValue);	
				WauzDebugger.log(player, "Manus: " + defManus);
			}
			else if(lore.contains("Rune (")) {
				WauzDebugger.log(player, "Cleared Rune Slot");
				lore = WauzIdentifier.EMPTY_RUNE_SLOT;
				valid = true;
			}
			else if(lore.contains("Skillgem (")) {
				WauzDebugger.log(player, "Cleared Skill Slot");
				lore = WauzIdentifier.EMPTY_SKILL_SLOT;
				valid = false;
				skipLines = 2;
			}
			newLores.add(lore);
		}
		
		if(valid) {
			im.setLore(newLores);
			itemStack.setItemMeta(im);
		}
		return valid;
	}
	
}
