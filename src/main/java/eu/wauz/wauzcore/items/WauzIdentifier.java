package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import net.md_5.bungee.api.ChatColor;

public class WauzIdentifier {

	private static List<Equipment> equipTypes = new ArrayList<>();
	
	public static void addEquipType(Equipment equip) {
		equipTypes.add(equip);
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
	
	private static List<String> runeNames = new ArrayList<>(Arrays.asList(
			"Power", "Knowledge", "Thorns"));
	
	private static List<String> shrineNames = new ArrayList<>(Arrays.asList(
			"Amon", "Atar", "Baka", "Bael", "Cata", "Cesa", "Daku", "Deus", "Elek", "Esto", "Furo", "Fitu", "Garo",
			"Gyro", "Hino", "Hane", "Ivel", "Inuk", "Jago", "Jojo", "Kaka", "Kars", "Leto", "Lupo", "Maro", "Magi",
			"Naga", "Notu", "Omek", "Oraa", "Popo", "Pino", "Qire", "Quez", "Raka", "Reem", "Sora", "Sado", "Tera",
			"Toem", "Utga", "Uros", "Vari", "Vago", "Wesa", "Wamu", "Xera", "Xulu", "Yaga", "Yare", "Zaga", "Zeli"));
	
	public static final String EMPTY_SKILL_SLOT =
			ChatColor.WHITE + "Skill Slot (" + ChatColor.DARK_RED + "Empty" + ChatColor.WHITE + ")";
	
	public static final String EMPTY_RUNE_SLOT =
			ChatColor.WHITE + "Rune Slot (" + ChatColor.GREEN + "Empty" + ChatColor.WHITE + ")";
	
	public static void identify(InventoryClickEvent event, String itemName) {
		if(itemName.contains("Item"))
			WauzIdentifier.identifyItem(event);
		else if(itemName.contains("Rune"))
			WauzIdentifier.identifyRune(event);
		else if(itemName.contains("Map"))
			WauzIdentifier.identifyShrine(event);
		else if(itemName.contains("Skillgem"))
			WauzIdentifier.identifySkillgem(event);
	}
	
	public static void identifyItem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack itemStack = event.getCurrentItem();	
		String name = itemStack.getItemMeta().getDisplayName();
		
		Random random = new Random();
		String verb = equipNames.get(random.nextInt(equipNames.size()));
		int rarity = random.nextInt(1000);
	
// Set Item Material
		
		double typeMultiplicator = 0;
		Equipment equip = equipTypes.get(random.nextInt(equipTypes.size()));
		
		itemStack.setType(equip.getMaterial());
		typeMultiplicator = equip.getDamage();
		int maxDurability = equip.getDurability();
		
		ItemMeta itemMeta = itemStack.getItemMeta();
		
// Set Item Rarity
		
		String rareName;
		String rareStars = "";
		String x = ChatFormatter.ICON_DIAMS;
		ChatColor color = null;
		double rarityMultiplicator = 0;
		double baseMultiplicator;
		
		if(Chance.oneIn(150)) {
			if(Chance.oneIn(2)) {
				rareName = "Primal ";
				rareStars = "" + ChatColor.RED;
				baseMultiplicator = 3.5;
			}
			else {
				rareName = "Stable ";
				rareStars = "" + ChatColor.DARK_AQUA;
				baseMultiplicator = 1.5;
				
				itemMeta.setUnbreakable(true);
				itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}
		}
		else {
			rareName = "";
			rareStars = "" + ChatColor.YELLOW;
			baseMultiplicator = 2 + random.nextDouble();
		}
		
		if(rarity <= 550) {
			color = ChatColor.GREEN;
			rareName += "Normal ";
			rareStars += x + ChatColor.GRAY +x +x +x +x;
			rarityMultiplicator = 1.00;
		}
		else if(rarity <= 800) {
			color = ChatColor.BLUE;
			rareName += "Magic ";
			rareStars += x +x + ChatColor.GRAY +x +x +x;
			rarityMultiplicator = 1.50;
		}
		else if(rarity <= 920) {
			color = ChatColor.GOLD;
			rareName += "Rare ";
			rareStars += x +x +x + ChatColor.GRAY +x +x;
			rarityMultiplicator = 2.00;
		}
		else if(rarity <= 975) {
			color = ChatColor.DARK_PURPLE;
			rareName += "Epic ";
			rareStars += x +x +x +x + ChatColor.GRAY +x;
			rarityMultiplicator = 2.50;
		}
		else if(rarity <= 999) {
			color = ChatColor.DARK_RED;
			rareName += "Unique ";
			rareStars += x +x +x +x +x;
			rarityMultiplicator = 3.00;
		}
		
// Set Item Tier
		
		String tierName = null;
		int tier = 0;
		
		if(name.contains("T1")) {
			tier = 1;
			tierName = "Lesser" + ChatColor.GRAY + " T1 " + ChatColor.WHITE;
		}
		else if(name.contains("T2")) {
			tier = 2;
			tierName = "Greater" + ChatColor.GRAY + " T2 " + ChatColor.WHITE;
		}
		else if(name.contains("T3")) {
			tier = 3;
			tierName = "Angelic" + ChatColor.GRAY + " T3 " + ChatColor.WHITE;
		}
		
		itemMeta.setDisplayName(color + verb + equip.getName());	
		
// Scaling and Main Stat
		
		int attack = (int) ((double) (baseMultiplicator) * (double) (typeMultiplicator) * (double) (Math.pow(2, tier)) * (double) (rarityMultiplicator));
		
		List<String> lores = new ArrayList<String>();
		
		float scalingLevel = player.getLevel() - (tier * 10 - 10);
		scalingLevel = (float) (scalingLevel < 1 ? 3 : (scalingLevel + 2 > 10 ? 10 : scalingLevel + 2)) / 10;	
		WauzDebugger.log(player, "Level-Scaling Weapon: " + attack + " * " + scalingLevel);
		int level = Math.max(Math.min((tier * 10), player.getLevel()), tier * 10 - 15);
		String levelString = ChatColor.YELLOW + "lvl " + ChatColor.AQUA + level + ChatColor.DARK_GRAY + ")";
		String scalingString = scalingLevel == 1
				? " " + ChatColor.DARK_GRAY + "(" + levelString
				: " " + ChatColor.DARK_GRAY + "(Scaled x" + scalingLevel + " " + levelString;
		attack = (int) (attack * scalingLevel);
		
		int defense = (attack / 4) + 1;
		attack += 1;
		
		String mainStatString = "";
		if(equip.getType().equals("Weapon")) {	
			lores.add(ChatColor.WHITE + tierName + rareName + "Weapon " + rareStars);
			lores.add("");
			mainStatString = "Attack:" + ChatColor.RED + " " + attack + scalingString;
			lores.add(mainStatString);
		}
		else if(equip.getType().equals("Armor")) {		
			lores.add(ChatColor.WHITE + tierName + rareName + "Armor " + rareStars);
			lores.add("");
			mainStatString = "Defense:" + ChatColor.BLUE + " " + defense + scalingString;
			lores.add(mainStatString);
		}
		
// Add Enhancements
		
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
				
				if(equip.getType().equals("Weapon")) {
					int enhancementType = random.nextInt(5);
					
					if(enhancementType == 0) {
						enhancementName = "Destruction";
						enhancementDescription = (enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Attack Boost";
						double newDamage = 1 + attack * (1 + enhancementLevel * 0.1);
						lores.remove(mainStatString);
						lores.add(mainStatString.replace(ChatColor.RED + " " + attack, ChatColor.RED + " " + (int) newDamage));
						WauzDebugger.log(player, "Rolled Attack Boost: " + attack + " -> " + (int) newDamage);
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
				
				else if(equip.getType().equals("Armor")) {
					int enhancementType = random.nextInt(3);
					
					if(enhancementType == 0) {
						enhancementName = "Numbing";
						enhancementDescription = (enhancementLevel * 10) + " " + ChatColor.GRAY + "% Base Defense Boost";
						double newDefense = 1 + defense * (1 + enhancementLevel * 0.1);
						lores.remove(mainStatString);
						lores.add(mainStatString.replace(ChatColor.BLUE + " " + defense, ChatColor.BLUE + " " + (int) newDefense));
						WauzDebugger.log(player, "Rolled Defense Boost: " + defense + " -> " + (int) newDefense);
					}
					else if(enhancementType == 1) {
						enhancementName = "Durability";
						enhancementDescription = (enhancementLevel * 128) + " " + ChatColor.GRAY + "Bonus Durability";
						maxDurability += (enhancementLevel * 128);
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
			else
				WauzDebugger.log(player, "Rolled Nothing...");
		}
		
// Set Durability
		
		Damageable damageable = (Damageable) itemMeta;
		damageable.setDamage(0);
		
		String durabilityString = "Durability:" + ChatColor.DARK_GREEN + " " + maxDurability;
		durabilityString += " " + ChatColor.DARK_GRAY + "/ " + maxDurability;
		lores.add(durabilityString);
		
// Add Rune and Skill Slots
		
		if(equip.getName().contains("Bow")) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Click while Sneaking to switch Arrows");
			lores.add(ChatColor.GRAY + "Right Click to shoot Arrows");
		}
		else if(rarityMultiplicator >= 1.5) {
			if(equip.getType().equals("Weapon") && Chance.oneIn(2)) {
				lores.add("");
				lores.add(EMPTY_SKILL_SLOT);
			}
		}
		if(rarityMultiplicator >= 1.5)	{
			lores.add("");
			lores.add(EMPTY_RUNE_SLOT);
			if(rarityMultiplicator >= 2.5)
				lores.add(EMPTY_RUNE_SLOT);
		}
		
		itemMeta.setLore(lores);	
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(itemMeta);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	public static void identifyRune(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack itemStack = event.getCurrentItem();	
		String name = itemStack.getItemMeta().getDisplayName();
		
		Random random = new Random();
		String rune = runeNames.get(random.nextInt(runeNames.size()));
		int rarity = random.nextInt(1000);
		
		String identifiedName = null;
		
// Set Rune Rarity
		
		String rareName = null;
		String rareStars = null;
		String x = ChatFormatter.ICON_DIAMS;
		ChatColor color = null;
		double rare = 0;
				
		if(rarity <= 800) {
			color = ChatColor.GREEN;
			rareName = "Whispering ";
			rareStars = ChatColor.GREEN +x + ChatColor.GRAY +x +x;
			rare = 1.00;
		}
		else if(rarity <= 975) {
			color = ChatColor.BLUE;
			rareName = "Screaming ";
			rareStars = ChatColor.GREEN +x +x + ChatColor.GRAY +x;
			rare = 1.50;
		}
		else if(rarity <= 999) {
			color = ChatColor.GOLD;
			rareName = "Deafening ";
			rareStars = ChatColor.GREEN +x +x +x;
			rare = 2.00;
		}
				
// Set Rune Tier
				
		String tierName = null;
		int tier = 0;
				
		if(name.contains("T1")) {
			tier = 8;
			tierName = "Lesser" + ChatColor.GRAY + " T1 " + ChatColor.WHITE;
		}
		else if(name.contains("T2")) {
			tier = 12;
			tierName = "Greater" + ChatColor.GRAY + " T2 " + ChatColor.WHITE;
		}
		else if(name.contains("T3")) {
			tier = 16;
			tierName = "Angelic" + ChatColor.GRAY + " T3 " + ChatColor.WHITE;
		}
		
// Generate Identified Rune
		
		identifiedName =  color + "Rune of " + rune;
			
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(identifiedName);
			
		int power = (int) ((2 + random.nextDouble() / 1.5) * tier * rare);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.WHITE + tierName + rareName + "Rune " + rareStars);
		lores.add("");
		lores.add(ChatColor.GRAY + "Can be inserted into Equipment,");
		lores.add(ChatColor.GRAY + "which possesses an empty Rune Slot.");
		lores.add("");
		lores.add("Might:" + ChatColor.YELLOW + " " + power);
		
		itemMeta.setLore(lores);	
		itemStack.setItemMeta(itemMeta);
		itemStack.setType(Material.FIREWORK_STAR);
		
		player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_USE, 0);
	}
	
	public static void identifyShrine(InventoryClickEvent event) {
		ItemStack itemStack = event.getCurrentItem();	
		
		Random random = new Random();
		String shrineName = ChatColor.RESET 
				+ shrineNames.get(random.nextInt(shrineNames.size())) + "-" 
				+ shrineNames.get(random.nextInt(shrineNames.size())) + " Shrine";
		
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(shrineName);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.RED + "Shrine Map");
		lores.add("");
		lores.add(ChatColor.YELLOW + "" + (random.nextInt(9) + 5) + " Rooms");
		lores.add("");
		lores.add(ChatColor.GRAY + "Right Click to enter Shrine.");
		lores.add(ChatColor.GRAY + "Map resets upon Death.");
		
		itemMeta.setLore(lores);	
		itemStack.setItemMeta(itemMeta);
		itemStack.setType(Material.PAPER);
	}
	
	public static void identifySkillgem(InventoryClickEvent event) {
		ItemStack itemStack = event.getCurrentItem();
		
		Random random = new Random();
		List<String> skills = WauzPlayerSkillExecutor.getAllSkillIds();
		String skillgemName = skills.get(random.nextInt(skills.size()));
		
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Skillgem: " + ChatColor.LIGHT_PURPLE + skillgemName);
		
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillgemName);
		
		List<String> lores = new ArrayList<String>();
		lores.add(ChatColor.GRAY + "Can be inserted into a Weapon,");
		lores.add(ChatColor.GRAY + "which possesses an empty Skill Slot.");
		lores.add("");
		lores.add(ChatColor.WHITE + "Adds Right-Click Skill:");
		lores.add(ChatColor.WHITE + skill.getSkillDescription());
		lores.add(ChatColor.WHITE + skill.getSkillStats());
		
		itemMeta.setLore(lores);
		itemStack.setItemMeta(itemMeta);
		itemStack.setType(Material.REDSTONE);
	}
	
}
