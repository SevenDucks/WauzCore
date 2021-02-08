package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.EquipmentParameters;
import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Chance;

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
	private static Map<String, WauzEquipment> equipTypes = new HashMap<>();
	
	/**
	 * Adds a new equipment type.
	 * 
	 * @param equip The equipment type to add.
	 */
	public static void addEquipType(WauzEquipment equip) {
		equipTypes.put(equip.getName(), equip);
	}
	
	/**
	 * Gets a list of all equipment types.
	 * 
	 * @return A list of all possible equipment types.
	 */
	public static List<WauzEquipment> getAllEquipTypes() {
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
	private static List<String> equipPrefixes = Arrays.asList(
			"Adamantite", "Ancient", "Alloyed", "Barbarian", "Blessed",
			"Broken", "Ceremonial", "Cobalt", "Colossal", "Corrupted",
			"Cruel", "Cursed", "Damaged", "Dragonbone", "Enchanted",
			"Fallen", "Fierce", "Flaming", "Forgotten", "Forsaken",
			"Frozen", "Gay", "Giant", "Goddess", "Guardian",
			"Hellforged", "Holy", "Lightforged", "Lost", "Majestic",
			"Malevolent", "Merciful", "Mighty", "Mythril", "Outlandish",
			"Plain", "Polished", "Robust", "Royal", "Ruined",
			"Rusty", "Savage", "Soldier", "Spiked", "Stained",
			"Timeworn", "Warforged", "Weakened", "Weathered", "Worthless");
	
	/**
	 * A random instance, for rolling item stats.
	 */
	private static Random random = new Random();
	
	/**
	 * The player who identifies the item.
	 */
	private Player player;
	
	/**
	 * The builder to generate the equipment item.
	 */
	private WauzEquipmentBuilder builder;
	
	/**
	 * The equipment item stack, that is getting identified.
	 */
	private ItemStack equipmentItemStack;
	
	/**
	 * The initial display name of the equipment item stack.
	 */
	private String itemName;
	
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
	 * The level requirement of the equipment.
	 */
	private int requiredLevel;
	
	/**
	 * The scaling of the equipment's main stats, where 1 = unscaled.
	 */
	private float scalingLevel;
	
	/**
	 * Identifies the item, based on the given event.
	 * Firstly the equipment type, material, type multiplicator, speed, durability, swiftness
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
		itemName = equipmentItemStack.getItemMeta().getDisplayName();
		
		if(itemName.contains(" : ")) {
			equipmentType = equipTypes.get(StringUtils.substringAfter(itemName, " : "));
		}
		if(equipmentType == null) {
			equipmentType = new ArrayList<>(equipTypes.values()).get(random.nextInt(equipTypes.size()));
		}
		
		builder = new WauzEquipmentBuilder(equipmentType.getMaterial());
		typeMultiplicator = equipmentType.getMainStat();
		speedStat = equipmentType.getSpeedStat();
		durabilityStat = equipmentType.getDurabilityStat();
		swiftnessStat = equipmentType.getSwiftnessStat();
		
		if(equipmentType.getMaterial().equals(Material.SHIELD)) {
			builder.addShieldPattern();
		}
		else if(equipmentType.getLeatherDye() != null) {
			builder.addLeatherDye(equipmentType.getLeatherDye());
		}
		
		determineBaseMultiplier();
		rarity = Rarity.getRandomEquipmentRarity();
		tier = Tier.getEquipmentTier(itemName);
		
		generateIdentifiedEquipment();
	}
	
	/**
	 * Generates the equipment, by calculating the main stat and setting lore, flags and meta.
	 * Equipment type, rarity, tier and so on have to be set before calling this method.
	 * Plays an anvil sound to the player, when the identifying has been completed.
	 */
	private void generateIdentifiedEquipment() {
		calculateMainStats();
		addEnhancementsToEquipment();
		builder.addMainStats(attackStat, defenseStat, requiredLevel, scalingLevel);
		builder.addDurabilityStat(durabilityStat);
		addSpeedToEquipment();
		addSwiftnessToEquipment();
		addArmorCategoryToEquipment();
		
		String verb = equipPrefixes.get(random.nextInt(equipPrefixes.size()));
		String name = verb + " " + equipmentType.getName();
		ItemStack generatedItemStack = WauzNmsClient.nmsSerialize(builder.generate(tier, rarity, equipmentType.getType(), name));
		equipmentItemStack.setType(generatedItemStack.getType());
		equipmentItemStack.setItemMeta(generatedItemStack.getItemMeta());
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
				builder.addRarityPrefixes("Primal ", "" + ChatColor.RED);
				baseMultiplier = 3.5;
			}
			else {
				builder.makeUnbreakable();
				builder.addRarityPrefixes("Stable ", "" + ChatColor.DARK_AQUA);
				baseMultiplier = 1.5;
			}
		}
		else {
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
	private void calculateMainStats() {
		mainStat = (int) (baseMultiplier * typeMultiplicator * tier.getMultiplier() * rarity.getMultiplier());
		scalingLevel = player.getLevel() - (tier.getLevel() * 10 - 10);
		scalingLevel = (float) (scalingLevel < 1 ? 3 : (scalingLevel + 2 > 10 ? 10 : scalingLevel + 2)) / 10;	
		WauzDebugger.log(player, "Level-Scaling Weapon: " + mainStat + " * " + scalingLevel);
		requiredLevel = Math.max(Math.min((tier.getLevel() * 10), player.getLevel()), tier.getLevel() * 10 - 15);
		mainStat = (int) (mainStat * scalingLevel);
		attackStat = mainStat + 1;
		defenseStat = (mainStat / 4) + 1;
	}
	
	/**
	 * Adds an optional enhancement to the lore and equipment name.
	 * Only works if it is not typed tool, otherwise it does nothing.
	 * The base chance is 1 in 3, with the enhancement level based on the luck stat.
	 * Each 100% enhance-rate will be a guaranteed level.</br>
	 * Example 1: 50% = 50% chance for a lvl 1 enhancement, 50% chance for nothing at all.</br>
	 * Example 3: 200% = 100% chance for a lvl 2 enhancement</br>
	 * Example 2: 350% = 100% chance for a lvl 3 enhancement + additional 50% chance for lvl 4.
	 * 
	 * @see PlayerSkillConfigurator#getLuck(Player)
	 * @see WauzEquipmentEnhancer#enhanceEquipment(WauzEquipmentIdentifier)
	 */
	private void addEnhancementsToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.TOOL)) {
			return;
		}
		if(Chance.oneIn(3)) {
			int enhancementLevel = 0;
			int luck = PlayerSkillConfigurator.getLuck(player);
			WauzDebugger.log(player, "Rolling for Enhancement with: " + luck + "% Luck");
			while(luck >= 100) {
				enhancementLevel++;
				luck -= 100;
			}
			if(Chance.percent(luck)) {
				enhancementLevel++;
			}
			
			if(enhancementLevel > 0) {
				WauzEquipmentEnhancer.enhanceEquipment(this, enhancementLevel);
				WauzDebugger.log(player, "Rolled Enhancement Level: " + enhancementLevel);
			}
			else {
				WauzDebugger.log(player, "Rolled Nothing...");
			}
		}
	}
	
	/**
	 * Adds the speed stat from the equipment type to the lore and applies it as attribute modifier.
	 * Only works if it is typed weapon, otherwise it does nothing.
	 */
	private void addSpeedToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.WEAPON)) {
			builder.addSpeedStat(speedStat);
		}
	}
	
	/**
	 * Adds the swiftness stat from the equipment type to the lore and applies it as attribute modifier.
	 * Only works if it is typed armor, otherwise it does nothing.
	 */
	private void addSwiftnessToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.ARMOR)) {
			builder.addSwiftnessStat(swiftnessStat);
		}
	}
	
	/**
	 * Adds the armor category from the equipment type to the lore.
	 * Only works if it is typed armor, otherwhise it does nothing.
	 */
	private void addArmorCategoryToEquipment() {
		if(equipmentType.getType().equals(EquipmentType.ARMOR)) {
			builder.addArmorCategory(equipmentType.getCategory());
		}
	}

	/**
	 * @return The builder to generate the equipment item.
	 */
	public WauzEquipmentBuilder getBuilder() {
		return builder;
	}

}
