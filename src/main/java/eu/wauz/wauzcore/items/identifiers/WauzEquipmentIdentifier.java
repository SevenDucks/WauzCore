package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import net.md_5.bungee.api.ChatColor;

public class WauzEquipmentIdentifier {

	private static List<Equipment> equipTypes = new ArrayList<>();
	
	public static void addEquipType(Equipment equip) {
		equipTypes.add(equip);
	}
	
	public static int getEquipmentTypeCount() {
		return equipTypes.size();
	}
	
	private static List<String> equipNames = new ArrayList<>(Arrays.asList(
			"Adamantite", "Ancient", "Alloyed", "Barbarian", "Blessed",
			"Broken", "Ceremonial", "Cobalt", "Colossal", "Corrupted",
			"Cruel", "Cursed", "Damaged", "Dragonbone", "Enchanted",
			"Fallen", "Fierce", "Flaming", "Forgotten", "Forsaken",
			"Frozen", "Gay", "Giant", "Goddess", "Guardian",
			"Hellforged", "Holy", "Lightforged", "Lost", "Majestic",
			"Malevolent", "Merciful", "Mighty", "Mythril", "Outlandish",
			"Plain", "Polished", "Robust", "Royal", "Ruined",
			"Rusty", "Savage", "Soldier", "Spiked", "Stained",
			"Timeworn", "Warforged", "Weakened", "Weathered", "Worthless"));
	
	public static final String EMPTY_SKILL_SLOT =
			ChatColor.WHITE + "Skill Slot (" + ChatColor.DARK_RED + "Empty" + ChatColor.WHITE + ")";
	
	public static final String EMPTY_RUNE_SLOT =
			ChatColor.WHITE + "Rune Slot (" + ChatColor.GREEN + "Empty" + ChatColor.WHITE + ")";
	
	private Random random = new Random();
	
	private Player player;
	
	
	private ItemStack equipmentItemStack;
	
	private ItemMeta itemMeta;
	
	private String itemName;
	
	private String identifiedItemName;
	
	private List<String> lores;
	
	private Equipment equipmentType;
	
	private int mainStat;
	
	private String mainStatString;
	
	private int attackStat;
	
	private int defenseStat;
	
	private int durabilityStat;
	
	private double typeMultiplicator = 0;
	
	private double baseMultiplier = 0;
	
	
	private String rarityName;
	
	private String rarityStars;
	
	private ChatColor rarityColor;
	
	private double rarityMultiplier = 0;
	
	
	private String tierName;
	
	private int tier = 0;
	
	private double tierMultiplier = 0;
	
	public void identifyItem(InventoryClickEvent event) {
		player = (Player) event.getWhoClicked();
		equipmentItemStack = event.getCurrentItem();	
		itemMeta = equipmentItemStack.getItemMeta();
		itemName = equipmentItemStack.getItemMeta().getDisplayName();
		
		equipmentType = equipTypes.get(random.nextInt(equipTypes.size()));
		equipmentItemStack.setType(equipmentType.getMaterial());
		typeMultiplicator = equipmentType.getDamage();
		durabilityStat = equipmentType.getDurability();
		
		determineBaseMultiplier();
		determineRarity();
		determineTier();
		
		String verb = equipNames.get(random.nextInt(equipNames.size()));
		identifiedItemName = rarityColor + verb + equipmentType.getName();
		itemMeta.setDisplayName(identifiedItemName);
		
		generateIdentifiedEquipment();
	}
	
	private void generateIdentifiedEquipment() {
		mainStat = (int) (baseMultiplier * typeMultiplicator * tierMultiplier * rarityMultiplier);
		
		lores = new ArrayList<String>();
		addMainStatToEquipment();
		addEnhancementsToEquipment();
		addDurabilityToEquipment();
		addSlotsToEquipment();
		
		itemMeta.setLore(lores);	
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		equipmentItemStack.setItemMeta(itemMeta);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	private void determineBaseMultiplier() {
		if(Chance.oneIn(150)) {
			if(Chance.oneIn(2)) {
				rarityName = "Primal ";
				rarityStars = "" + ChatColor.RED;
				baseMultiplier = 3.5;
			}
			else {
				rarityName = "Stable ";
				rarityStars = "" + ChatColor.DARK_AQUA;
				baseMultiplier = 1.5;
				
				itemMeta.setUnbreakable(true);
				itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}
		}
		else {
			rarityName = "";
			rarityStars = "" + ChatColor.YELLOW;
			baseMultiplier = 2 + random.nextDouble();
		}
	}
	
	private void determineRarity() {
		int rarity = random.nextInt(1000);
		String x = ChatFormatter.ICON_DIAMS;
		
		if(rarity <= 550) {
			rarityColor = ChatColor.GREEN;
			rarityName += "Normal ";
			rarityStars += x + ChatColor.GRAY +x +x +x +x;
			rarityMultiplier = 1.00;
		}
		else if(rarity <= 800) {
			rarityColor = ChatColor.BLUE;
			rarityName += "Magic ";
			rarityStars += x +x + ChatColor.GRAY +x +x +x;
			rarityMultiplier = 1.50;
		}
		else if(rarity <= 920) {
			rarityColor = ChatColor.GOLD;
			rarityName += "Rare ";
			rarityStars += x +x +x + ChatColor.GRAY +x +x;
			rarityMultiplier = 2.00;
		}
		else if(rarity <= 975) {
			rarityColor = ChatColor.DARK_PURPLE;
			rarityName += "Epic ";
			rarityStars += x +x +x +x + ChatColor.GRAY +x;
			rarityMultiplier = 2.50;
		}
		else if(rarity <= 999) {
			rarityColor = ChatColor.DARK_RED;
			rarityName += "Unique ";
			rarityStars += x +x +x +x +x;
			rarityMultiplier = 3.00;
		}
	}
	
	private void determineTier() {
		if(itemName.contains("T1")) {
			tier = 1;
			tierName = "Lesser" + ChatColor.GRAY + " T1 " + ChatColor.WHITE;
		}
		else if(itemName.contains("T2")) {
			tier = 2;
			tierName = "Greater" + ChatColor.GRAY + " T2 " + ChatColor.WHITE;
		}
		else if(itemName.contains("T3")) {
			tier = 3;
			tierName = "Angelic" + ChatColor.GRAY + " T3 " + ChatColor.WHITE;
		}
		tierMultiplier = (double) (Math.pow(2, tier));
	}
	
	private void addMainStatToEquipment() {
		float scalingLevel = player.getLevel() - (tier * 10 - 10);
		scalingLevel = (float) (scalingLevel < 1 ? 3 : (scalingLevel + 2 > 10 ? 10 : scalingLevel + 2)) / 10;	
		WauzDebugger.log(player, "Level-Scaling Weapon: " + mainStat + " * " + scalingLevel);
		int level = Math.max(Math.min((tier * 10), player.getLevel()), tier * 10 - 15);
		String levelString = ChatColor.YELLOW + "lvl " + ChatColor.AQUA + level + ChatColor.DARK_GRAY + ")";
		String scalingString = scalingLevel == 1
				? " " + ChatColor.DARK_GRAY + "(" + levelString
				: " " + ChatColor.DARK_GRAY + "(Scaled x" + scalingLevel + " " + levelString;
		mainStat = (int) (mainStat * scalingLevel) + 1;
		
		attackStat = mainStat;
		defenseStat = (mainStat / 4);
		
		mainStatString = "";
		if(equipmentType.getType().equals(EquipmentType.WEAPON)) {	
			lores.add(ChatColor.WHITE + tierName + rarityName + "Weapon " + rarityStars);
			lores.add("");
			mainStatString = "Attack:" + ChatColor.RED + " " + attackStat + scalingString;
			lores.add(mainStatString);
		}
		else if(equipmentType.getType().equals(EquipmentType.ARMOR)) {		
			lores.add(ChatColor.WHITE + tierName + rarityName + "Armor " + rarityStars);
			lores.add("");
			mainStatString = "Defense:" + ChatColor.BLUE + " " + defenseStat + scalingString;
			lores.add(mainStatString);
		}
	}
	
	// TODO Replace this shit
	private void addEnhancementsToEquipment() {
		if(Chance.oneIn(3)) {
			int enhancementLevel = 0;
			int luck = PlayerPassiveSkillConfigurator.getLuck(player);
			WauzDebugger.log(player, "Rolling for Enhancement with: " + luck + "% Luck");
			while(luck >= 100) {
				enhancementLevel++;
				luck -= 100;
			}
			if(Chance.percent(luck))
				enhancementLevel++;
			
			if(enhancementLevel > 0) {
				String enhancementName = "";
				String enhancementDescription = "";
				
				if(equipmentType.getType().equals(EquipmentType.WEAPON)) {
					int enhancementType = random.nextInt(5);
					
					if(enhancementType == 0) {
						enhancementName = "Destruction";
						enhancementDescription = (enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Attack Boost";
						double newDamage = 1 + attackStat * (1 + enhancementLevel * 0.1);
						lores.remove(mainStatString);
						lores.add(mainStatString.replace(ChatColor.RED + " " + attackStat, ChatColor.RED + " " + (int) newDamage));
						WauzDebugger.log(player, "Rolled Attack Boost: " + attackStat + " -> " + (int) newDamage);
					}
					else if(enhancementType == 1) {
						enhancementName = "Nourishment";
						enhancementDescription = (enhancementLevel * 3) + " " + ChatColor.GRAY + "HP on Kill";
						WauzDebugger.log(player, "Rolled HP on Kill");
					}
					else if(enhancementType == 2) {
						enhancementName = "Consumption";
						enhancementDescription = (enhancementLevel * 1) + " " + ChatColor.GRAY + "MP on Kill";
						WauzDebugger.log(player, "Rolled MP on Kill");
					}
					else if(enhancementType ==  3) {
						enhancementName = "Ferocity";
						enhancementDescription = (enhancementLevel * 20) + " " + ChatColor.GRAY + "% Crit Multiplier";
						WauzDebugger.log(player, "Rolled Crit Multiplier");
					}
					else if(enhancementType ==  4) {
						enhancementName = "Expertise";
						enhancementDescription = (enhancementLevel * 15) + " " + ChatColor.GRAY + "% Skill Damage";
						WauzDebugger.log(player, "Rolled Skill Damage");
					}
					
					itemMeta.setDisplayName(itemMeta.getDisplayName() + " of " + enhancementName + " + " + enhancementLevel);
					lores.add("Enhancement:" + ChatColor.RED + " " + enhancementDescription);
				}
				
				else if(equipmentType.getType().equals(EquipmentType.ARMOR)) {
					int enhancementType = random.nextInt(3);
					
					if(enhancementType == 0) {
						enhancementName = "Numbing";
						enhancementDescription = (enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Defense Boost";
						double newDefense = 1 + defenseStat * (1 + enhancementLevel * 0.1);
						lores.remove(mainStatString);
						lores.add(mainStatString.replace(ChatColor.BLUE + " " + defenseStat, ChatColor.BLUE + " " + (int) newDefense));
						WauzDebugger.log(player, "Rolled Defense Boost: " + defenseStat + " -> " + (int) newDefense);
					}
					else if(enhancementType == 1) {
						enhancementName = "Durability";
						enhancementDescription = (enhancementLevel * 128) + " " + ChatColor.GRAY + "Bonus Durability";
						durabilityStat += (enhancementLevel * 128);
						WauzDebugger.log(player, "Rolled Bonus Durability");
					}
					else if(enhancementType == 2) {
						enhancementName = "Mastery";
						enhancementDescription = (enhancementLevel * 15) + " " + ChatColor.GRAY + "% Rune Effectiveness";
						WauzDebugger.log(player, "Rolled Rune Effectiveness");
					}
					
					itemMeta.setDisplayName(itemMeta.getDisplayName() + " of " + enhancementName + " + " + enhancementLevel);
					lores.add("Enhancement:" + ChatColor.BLUE + " " + enhancementDescription);
				}
				
				WauzDebugger.log(player, "Rolled Enhancement Level: " + enhancementLevel);
			}
			else {
				WauzDebugger.log(player, "Rolled Nothing...");
			}
		}
	}
	
	private void addDurabilityToEquipment() {
		Damageable damageable = (Damageable) itemMeta;
		damageable.setDamage(0);
		
		String durabilityString = "Durability:" + ChatColor.DARK_GREEN + " " + durabilityStat;
		durabilityString += " " + ChatColor.DARK_GRAY + "/ " + durabilityStat;
		lores.add(durabilityString);
	}
	
	private void addSlotsToEquipment() {
		if(equipmentType.getName().contains("Bow")) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Click while Sneaking to switch Arrows");
			lores.add(ChatColor.GRAY + "Right Click to shoot Arrows");
		}
		else if(rarityMultiplier >= 1.5) {
			if(equipmentType.getType().equals(EquipmentType.WEAPON) && Chance.oneIn(2)) {
				lores.add("");
				lores.add(EMPTY_SKILL_SLOT);
			}
		}
		if(rarityMultiplier >= 1.5)	{
			lores.add("");
			lores.add(EMPTY_RUNE_SLOT);
			if(rarityMultiplier >= 2.5)
				lores.add(EMPTY_RUNE_SLOT);
		}
	}
	
}
