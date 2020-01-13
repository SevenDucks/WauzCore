package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.EquipmentType;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
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
	
	private double speedStat;
	
	private int durabilityStat;
	
	private double typeMultiplicator = 0;
	
	private double baseMultiplier = 0;
	
	private int enhancementLevel = 0;
	
	
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
		typeMultiplicator = equipmentType.getMainStat();
		speedStat = equipmentType.getSpeedStat();
		durabilityStat = equipmentType.getDurabilityStat();
		
		if(equipmentType.getLeatherDye() != null) {
			itemMeta = new ItemStack(Material.LEATHER_CHESTPLATE).getItemMeta();
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
			leatherArmorMeta.setColor(equipmentType.getLeatherDye());
		}
		
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
		addSpeedToEquipment();
		addArmorCategoryToEquipment();
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
		int rarity = random.nextInt(10000) + 1;
		String x = UnicodeUtils.ICON_DIAMOND;
		
		if(rarity <= 7000) {
			rarityColor = ChatColor.GREEN;
			rarityName += "Normal ";
			rarityStars += x + ChatColor.GRAY +x +x +x +x;
			rarityMultiplier = 1.00;
		}
		else if(rarity <= 9500) {
			rarityColor = ChatColor.BLUE;
			rarityName += "Magic ";
			rarityStars += x +x + ChatColor.GRAY +x +x +x;
			rarityMultiplier = 1.50;
		}
		else if(rarity <= 9900) {
			rarityColor = ChatColor.GOLD;
			rarityName += "Rare ";
			rarityStars += x +x +x + ChatColor.GRAY +x +x;
			rarityMultiplier = 2.00;
		}
		else if(rarity <= 9995) {
			rarityColor = ChatColor.DARK_PURPLE;
			rarityName += "Epic ";
			rarityStars += x +x +x +x + ChatColor.GRAY +x;
			rarityMultiplier = 2.50;
		}
		else if(rarity <= 10000) {
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
		mainStat = (int) (mainStat * scalingLevel);
		
		attackStat = mainStat + 1;
		defenseStat = (mainStat / 4) + 1;
		
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
	
	private void addEnhancementsToEquipment() {
		if(Chance.oneIn(3)) {
			int luck = PlayerPassiveSkillConfigurator.getLuck(player);
			WauzDebugger.log(player, "Rolling for Enhancement with: " + luck + "% Luck");
			while(luck >= 100) {
				enhancementLevel++;
				luck -= 100;
			}
			if(Chance.percent(luck)) {
				enhancementLevel++;
			}
			
			if(enhancementLevel > 0) {
				WauzEquipmentEnhancer.enhanceEquipment(this);
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
	
	private void addSpeedToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.WEAPON)) {
			double genericAttackSpeed = speedStat - 4.0;
			WauzDebugger.log(player, "Generic Attack Speed: " + genericAttackSpeed);
			AttributeModifier modifier = new AttributeModifier("generic.attackSpeed", genericAttackSpeed, Operation.ADD_NUMBER);
			itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
			lores.add("Speed:" + ChatColor.RED + " " + Formatters.DEC_SHORT.format(speedStat));
		}
	}
	
	private void addArmorCategoryToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.ARMOR)) {
			lores.add("Category:" + ChatColor.BLUE + " " + equipmentType.getCategory());
		}
	}
	
	private void addSlotsToEquipment() {
		if(equipmentType.getName().contains("Bow")) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to switch Arrows");
			lores.add(ChatColor.GRAY + "Right Click to shoot Arrows");
		}
		if(equipmentType.getName().contains("Hook")) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to pull you to a Block");
			lores.add(ChatColor.GRAY + "Right Click to grab Enemies");
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

	public int getEnhancementLevel() {
		return enhancementLevel;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType.getType();
	}

	public ItemMeta getItemMeta() {
		return itemMeta;
	}

	public void setItemMeta(ItemMeta itemMeta) {
		this.itemMeta = itemMeta;
	}

	public List<String> getLores() {
		return lores;
	}

	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	public String getMainStatString() {
		return mainStatString;
	}

	public void setMainStatString(String mainStatString) {
		this.mainStatString = mainStatString;
	}

	public int getAttackStat() {
		return attackStat;
	}

	public void setAttackStat(int attackStat) {
		this.attackStat = attackStat;
	}

	public int getDefenseStat() {
		return defenseStat;
	}

	public void setDefenseStat(int defenseStat) {
		this.defenseStat = defenseStat;
	}

	public double getSpeedStat() {
		return speedStat;
	}

	public void setSpeedStat(double speedStat) {
		this.speedStat = speedStat;
	}

	public int getDurabilityStat() {
		return durabilityStat;
	}

	public void setDurabilityStat(int durabilityStat) {
		this.durabilityStat = durabilityStat;
	}
	
}
