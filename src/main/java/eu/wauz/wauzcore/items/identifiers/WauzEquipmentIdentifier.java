package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.Equipment;
import eu.wauz.wauzcore.items.EquipmentParameters;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.weapons.CustomWeaponShield;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Formatters;
import net.md_5.bungee.api.ChatColor;

/**
 * Typed identifier, used for identifying equipment items.
 * 
 * @author Wauzmons
 * 
 * @see WauzIdentifier
 */
public class WauzEquipmentIdentifier extends EquipmentParameters {
	
	/**
	 * A map of all possible equipment types, by name.
	 */
	private static Map<String, Equipment> equipTypes = new HashMap<>();
	
	/**
	 * Adds a new equipment type.
	 * 
	 * @param equip The equipment type to add.
	 */
	public static void addEquipType(Equipment equip) {
		equipTypes.put(equip.getName(), equip);
	}
	
	/**
	 * Gets a list of all equipment types.
	 * 
	 * @return A list of all possible equipment types.
	 */
	public static List<Equipment> getAllEquipTypes() {
		return new ArrayList<>(equipTypes.values());
	}
	
	/**
	 * Counts all possible equipment types.
	 * 
	 * @return Count of equipment types.
	 */
	public static int getEquipmentTypeCount() {
		return equipTypes.size();
	}
	
	/**
	 * All possible equipment prefixes.
	 */
	private static List<String> equipPrefixes = new ArrayList<>(Arrays.asList(
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
	
	/**
	 * An empty skillgem slot, to put in the equipment lore.
	 */
	public static final String EMPTY_SKILL_SLOT =
			ChatColor.WHITE + "Skill Slot (" + ChatColor.DARK_RED + "Empty" + ChatColor.WHITE + ")";
	
	/**
	 * An empty rune slot, to put in the equipment lore.
	 */
	public static final String EMPTY_RUNE_SLOT =
			ChatColor.WHITE + "Rune Slot (" + ChatColor.GREEN + "Empty" + ChatColor.WHITE + ")";
	
	/**
	 * A random instance, for rolling item stats.
	 */
	private Random random = new Random();
	
	/**
	 * The player who identifies the item.
	 */
	private Player player;
	
	/**
	 * The equipment item stack, that is getting identified.
	 */
	private ItemStack equipmentItemStack;
	
	/**
	 * The initial display name of the equipment item stack.
	 */
	private String itemName;
	
	/**
	 * The final display name of the equipment item stack.
	 */
	private String identifiedItemName;
	
	/**
	 * The main stat of the equipment.
	 */
	private int mainStat;
	
	/**
	 * The multiplier of the equipment's type.
	 */
	private double typeMultiplicator = 0;
	
	/**
	 * The random base multiplier of the equipment.
	 */
	private double baseMultiplier = 0;
	
	/**
	 * The rarity of the equipment.
	 */
	private Rarity rarity;
	
	/**
	 * The tier of the equipment.
	 */
	private Tier tier;
	
	/**
	 * The prefix of the name of the equipment's rarity.
	 */
	private String rarityNamePrefix;
	
	/**
	 * The prefix of the star rating of the equipment's rarity.
	 */
	private String rarityStarPrefix;
	
	/**
	 * Identifies the item, based on the given event.
	 * Firstly the equipment type, material, type multiplicator, speed, durability
	 * and optionally the leather dye and shield pattern are determined.
	 * If the item name specifies an equipment type like "Item : Greataxe", it will automatically be used.
	 * Then additional methods are called to roll base multiplier, rarity and tier.
	 * Finally the new name will be set and the item lores are generated.
	 * 
	 * @param player The player who identifies the item.
	 * @param equipmentItemStack The equipment item stack, that is getting identified.
	 * 
	 * @see WauzEquipmentIdentifier#determineBaseMultiplier()
	 * @see Rarity#getRandomEquipmentRarity()
	 * @see Tier#getEquipmentTier(String)
	 * @see WauzEquipmentIdentifier#generateIdentifiedEquipment()
	 */
	public void identifyItem(Player player, ItemStack equipmentItemStack) {
		this.player = player;
		this.equipmentItemStack = equipmentItemStack;
		itemMeta = equipmentItemStack.getItemMeta();
		itemName = itemMeta.getDisplayName();
		
		if(itemName.contains(" : ")) {
			equipmentType = equipTypes.get(StringUtils.substringAfter(itemName, " : "));
		}
		if(equipmentType == null) {
			equipmentType = new ArrayList<>(equipTypes.values()).get(random.nextInt(equipTypes.size()));
		}
		
		equipmentItemStack.setType(equipmentType.getMaterial());
		typeMultiplicator = equipmentType.getMainStat();
		speedStat = equipmentType.getSpeedStat();
		durabilityStat = equipmentType.getDurabilityStat();
		
		if(equipmentType.getMaterial().equals(Material.SHIELD)) {
			itemMeta = new ItemStack(Material.SHIELD).getItemMeta();
			CustomWeaponShield.addPattern(itemMeta);
		}
		else if(equipmentType.getLeatherDye() != null) {
			itemMeta = new ItemStack(Material.LEATHER_CHESTPLATE).getItemMeta();
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
			leatherArmorMeta.setColor(equipmentType.getLeatherDye());
		}
		
		determineBaseMultiplier();
		rarity = Rarity.getRandomEquipmentRarity();
		tier = Tier.getEquipmentTier(itemName);
		
		String verb = equipPrefixes.get(random.nextInt(equipPrefixes.size()));
		identifiedItemName = rarity.getColor() + verb + " " + equipmentType.getName();
		itemMeta.setDisplayName(identifiedItemName);
		
		generateIdentifiedEquipment();
	}
	
	/**
	 * Generates the equipment, by calculating the main stat and setting lore, flags and meta.
	 * Equipment type, rarity, tier and so on have to be set before calling this method.
	 * Plays an anvil sound to the player, when the identifying has been completed.
	 */
	private void generateIdentifiedEquipment() {
		mainStat = (int) (baseMultiplier * typeMultiplicator * tier.getMultiplier() * rarity.getMultiplier());
		
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
	
	/**
	 * Determines the random base multiplier between 2 and 3.
	 * There is a 1 in 150 chance, to get a rarity prefix of primal or stable.
	 * Primal sets the multiplier automatically to 3.5 and colors the rarity stars red.
	 * Stable sets the multiplier to 1.5, but makes the item unbreakable and colors the rarity stars aqua.
	 * The default rarity star color is yellow.
	 */
	private void determineBaseMultiplier() {
		if(Chance.oneIn(150)) {
			if(Chance.oneIn(2)) {
				rarityNamePrefix = "Primal ";
				rarityStarPrefix = "" + ChatColor.RED;
				baseMultiplier = 3.5;
			}
			else {
				rarityNamePrefix = "Stable ";
				rarityStarPrefix = "" + ChatColor.DARK_AQUA;
				baseMultiplier = 1.5;
				
				itemMeta.setUnbreakable(true);
				itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}
		}
		else {
			rarityNamePrefix = "";
			rarityStarPrefix = "" + ChatColor.YELLOW;
			baseMultiplier = 2 + random.nextDouble();
		}
	}
	
	/**
	 * Adds the rarity/tier string, aswell as the scaled main stat to the lore.
	 * The defense stat will always be divided by 4, for balancing reasons.
	 * If the player is under the recommended level (T1 = 8~10, T2 = 18~20 etc.), the stat will be scaled down.
	 * This makes it more efficient to use weapons of your level, than to use higher ones.
	 * 
	 * The scaling also affects the minimum level, required to use the equipment.
	 * Normally it is the level of the player who identified it,
	 * but it can't be more than the recommended item level (20 for T2)
	 * or less than the recommended item level minus 15 (5 for T2),
	 * to prevent the player from using items, out of their reach.
	 */
	private void addMainStatToEquipment() {
		float scalingLevel = player.getLevel() - (tier.getLevel() * 10 - 10);
		scalingLevel = (float) (scalingLevel < 1 ? 3 : (scalingLevel + 2 > 10 ? 10 : scalingLevel + 2)) / 10;	
		WauzDebugger.log(player, "Level-Scaling Weapon: " + mainStat + " * " + scalingLevel);
		int level = Math.max(Math.min((tier.getLevel() * 10), player.getLevel()), tier.getLevel() * 10 - 15);
		String levelString = ChatColor.YELLOW + "lvl " + ChatColor.AQUA + level + ChatColor.DARK_GRAY + ")";
		String scalingString = scalingLevel == 1
				? " " + ChatColor.DARK_GRAY + "(" + levelString
				: " " + ChatColor.DARK_GRAY + "(Scaled x" + scalingLevel + " " + levelString;
		mainStat = (int) (mainStat * scalingLevel);
		
		attackStat = mainStat + 1;
		defenseStat = (mainStat / 4) + 1;
		
		mainStatString = "";
		String rarityName = rarityNamePrefix + rarity.getName() + " ";
		String rarityStars = rarityStarPrefix + rarity.getStars();
		
		if(equipmentType.getType().equals(EquipmentType.WEAPON)) {	
			lores.add(ChatColor.WHITE + tier.getName() + " " + rarityName + "Weapon " + rarityStars);
			lores.add("");
			mainStatString = "Attack:" + ChatColor.RED + " " + attackStat + scalingString;
			lores.add(mainStatString);
		}
		else if(equipmentType.getType().equals(EquipmentType.ARMOR)) {		
			lores.add(ChatColor.WHITE + tier.getName() + " " + rarityName + "Armor " + rarityStars);
			lores.add("");
			mainStatString = "Defense:" + ChatColor.BLUE + " " + defenseStat + scalingString;
			lores.add(mainStatString);
		}
	}
	
	/**
	 * Adds an optional enhancement to the lore and equipment name.
	 * The base chance is 1 in 3, with the enhancement level based on the luck stat.
	 * Each 100% enhance-rate will be a guaranteed level.</br>
	 * Example 1: 50% = 50% chance for a lvl 1 enhancement, 50% chance for nothing at all.</br>
	 * Example 3: 200% = 100% chance for a lvl 2 enhancement</br>
	 * Example 2: 350% = 100% chance for a lvl 3 enhancement + additional 50% chance for lvl 4.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getLuck(Player)
	 * @see WauzEquipmentEnhancer#enhanceEquipment(WauzEquipmentIdentifier)
	 */
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
	
	/**
	 * Adds the durability stat from the equipment type to the lore.
	 * Also sets the item to full durability.
	 */
	private void addDurabilityToEquipment() {
		Damageable damageable = (Damageable) itemMeta;
		damageable.setDamage(0);
		
		String durabilityString = "Durability:" + ChatColor.DARK_GREEN + " " + durabilityStat;
		durabilityString += " " + ChatColor.DARK_GRAY + "/ " + durabilityStat;
		lores.add(durabilityString);
	}
	
	/**
	 * Adds the speed stat from the equipment type to the lore and applies it as attribute modifier.
	 * Only works if it is typed weapon, otherwhise it does nothing.
	 */
	private void addSpeedToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.WEAPON)) {
			double genericAttackSpeed = speedStat - 4.0;
			WauzDebugger.log(player, "Generic Attack Speed: " + genericAttackSpeed);
			AttributeModifier modifier = new AttributeModifier("generic.attackSpeed", genericAttackSpeed, Operation.ADD_NUMBER);
			itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
			lores.add("Speed:" + ChatColor.RED + " " + Formatters.DEC_SHORT.format(speedStat));
		}
	}
	
	/**
	 * Adds the armor category from the equipment type to the lore.
	 * Only works if it is typed armor, otherwhise it does nothing.
	 */
	private void addArmorCategoryToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.ARMOR)) {
			lores.add("Category:" + ChatColor.BLUE + " " + equipmentType.getCategory());
		}
	}
	
	/**
	 * Adds slots to the equipment's lores.
	 * If the equipment type is a lance, shield, hook or bow, it will receive the fitting lore.
	 * All other types with magic or higher rarity have a 50% chance to receive a skilgem slot.
	 * Magic or higher items will get 1 rune slot, while epic or higher items will receive 2 slots.
	 */
	private void addSlotsToEquipment() {
		Material material = equipmentType.getMaterial();
		if(material.equals(Material.BOW)) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to switch Arrows");
			lores.add(ChatColor.GRAY + "Right Click to shoot Arrows");
		}
		else if (material.equals(Material.TRIDENT)) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to perform a Spin Attack");
			lores.add(ChatColor.GRAY + "Right Click to Thrust (Throwing Disabled)");
		}
		else if(material.equals(Material.SHIELD)) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to taunt nearby Enemies");
			lores.add(ChatColor.GRAY + "Right Click to block Attacks");
		}
		else if(material.equals(Material.FISHING_ROD)) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to pull you to a Block");
			lores.add(ChatColor.GRAY + "Right Click to grab Enemies");
		}
		else if(material.equals(Material.FEATHER)) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Use while Sneaking to fly into the Air");
			lores.add(ChatColor.GRAY + "Right Click to throw Chickens");
		}
		else if(rarity.getMultiplier() >= 1.5) {
			if(equipmentType.getType().equals(EquipmentType.WEAPON) && Chance.oneIn(2)) {
				lores.add("");
				lores.add(EMPTY_SKILL_SLOT);
			}
		}
		
		if(rarity.getMultiplier() >= 1.5)	{
			lores.add("");
			lores.add(EMPTY_RUNE_SLOT);
			if(rarity.getMultiplier() >= 2.5) {
				lores.add(EMPTY_RUNE_SLOT);
			}
		}
	}

}
