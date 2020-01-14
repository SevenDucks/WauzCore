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

/**
 * Typed identifier, used for identifying equipment items.
 * 
 * @author Wauzmons
 * 
 * @see WauzIdentifier
 */
public class WauzEquipmentIdentifier {
	
	/**
	 * A list of all possible equipment types.
	 */
	private static List<Equipment> equipTypes = new ArrayList<>();
	
	/**
	 * Adds a nre equipment type.
	 * 
	 * @param equip The equipment type to add.
	 */
	public static void addEquipType(Equipment equip) {
		equipTypes.add(equip);
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
	 * A random instance, for rolling item stats and rarities.
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
	 * The item meta of the equipment item stack.
	 */
	private ItemMeta itemMeta;
	
	/**
	 * The initial display name of the equipment item stack.
	 */
	private String itemName;
	
	/**
	 * The final display name of the equipment item stack.
	 */
	private String identifiedItemName;
	
	/**
	 * The lores of the equipment item stack.
	 */
	private List<String> lores;
	
	/**
	 * The type of the equipment.
	 */
	private Equipment equipmentType;
	
	/**
	 * The main stat of the equipment.
	 */
	private int mainStat;
	
	/**
	 * The main stat of the equipment, as displayed in lore.
	 */
	private String mainStatString;
	
	/**
	 * The attack stat of the equipment.
	 */
	private int attackStat;
	
	/**
	 * The defense stat of the equipment.
	 */
	private int defenseStat;
	
	/**
	 * The speed stat of the equipment.
	 */
	private double speedStat;
	
	/**
	 * The durability stat of the equipment.
	 */
	private int durabilityStat;
	
	/**
	 * The multiplier of the equipment's type.
	 */
	private double typeMultiplicator = 0;
	
	/**
	 * The random base multiplier of the equipment.
	 */
	private double baseMultiplier = 0;
	
	/**
	 * The level of the equipment's enhancement.
	 */
	private int enhancementLevel = 0;
	
	
	/**
	 * The name of the equipment's rarity.
	 */
	private String rarityName;
	
	/**
	 * The stars of the equipment's rarity.
	 */
	private String rarityStars;
	
	/**
	 * The color of the equipment's rarity.
	 */
	private ChatColor rarityColor;
	
	/**
	 * The multiplier of the equipment's rarity.
	 */
	private double rarityMultiplier = 0;
	
	
	/**
	 * The name of the equipment's tier.
	 */
	private String tierName;
	
	/**
	 * The level of the equipment's tier.
	 */
	private int tier = 0;
	
	/**
	 * The multiplier of the equipment's tier.
	 */
	private double tierMultiplier = 0;
	
	/**
	 * Identifies the item, based on the given event.
	 * Firstly the equipment type, material, type multiplicator, speed, durability and dye are determined.
	 * Then additional methods are called to roll base multiplier, rarity and tier.
	 * Finally the new name will be set and the item lores are generated.
	 * 
	 * @param event The inventory event, which triggered the identifying.
	 * 
	 * @see WauzEquipmentIdentifier#determineBaseMultiplier()
	 * @see WauzEquipmentIdentifier#determineRarity()
	 * @see WauzEquipmentIdentifier#determineTier()
	 * @see WauzEquipmentIdentifier#generateIdentifiedEquipment()
	 */
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
		
		String verb = equipPrefixes.get(random.nextInt(equipPrefixes.size()));
		identifiedItemName = rarityColor + verb + equipmentType.getName();
		itemMeta.setDisplayName(identifiedItemName);
		
		generateIdentifiedEquipment();
	}
	
	/**
	 * Generates the equipment, by calculating the main stat and setting lore, flags and meta.
	 * Equipment type, rarity, tier and so on have to be set before calling this method.
	 * Plays an anvil sound to the player, when the identifying has been completed.
	 */
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
	
	/**
	 * Determines the random rarity with a multiplier of 1-3 on a scale of 1-5 stars.
	 * Automatically sets the rarity name and color and creates the star string.
	 */
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
	
	/**
	 * Determines the tier, based on the item name, with a multiplier of 2^1 to 2^3 on a scale of T1 to T3.
	 * Automatically sets the tier name and level.
	 */
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
	 * If the equipment type name contains bow or hook, it will receive the fitting lore.
	 * All other types with magic or higher rarity have a 50% chance to receive a skilgem slot.
	 * Magic or higher items will get 1 rune slot, while epic or higher items will receive 2 slots.
	 */
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

	/**
	 * @return The level of the equipment's enhancement.
	 */
	public int getEnhancementLevel() {
		return enhancementLevel;
	}

	/**
	 * @return The type of the equipment.
	 */
	public EquipmentType getEquipmentType() {
		return equipmentType.getType();
	}

	/**
	 * @return The item meta of the equipment item stack.
	 */
	public ItemMeta getItemMeta() {
		return itemMeta;
	}

	/**
	 * @param itemMeta The new item meta of the equipment item stack.
	 */
	public void setItemMeta(ItemMeta itemMeta) {
		this.itemMeta = itemMeta;
	}

	/**
	 * @return The lores of the equipment item stack.
	 */
	public List<String> getLores() {
		return lores;
	}

	/**
	 * @param lores The new lores of the equipment item stack.
	 */
	public void setLores(List<String> lores) {
		this.lores = lores;
	}

	/**
	 * @return The main stat of the equipment, as displayed in lore.
	 */
	public String getMainStatString() {
		return mainStatString;
	}

	/**
	 * @param mainStatString The new main stat of the equipment, as displayed in lore.
	 */
	public void setMainStatString(String mainStatString) {
		this.mainStatString = mainStatString;
	}

	/**
	 * @return The attack stat of the equipment.
	 */
	public int getAttackStat() {
		return attackStat;
	}

	/**
	 * @param attackStat The new attack stat of the equipment.
	 */
	public void setAttackStat(int attackStat) {
		this.attackStat = attackStat;
	}

	/**
	 * @return The defense stat of the equipment.
	 */
	public int getDefenseStat() {
		return defenseStat;
	}

	/**
	 * @param defenseStat The new defense stat of the equipment.
	 */
	public void setDefenseStat(int defenseStat) {
		this.defenseStat = defenseStat;
	}

	/**
	 * @return The speed stat of the equipment.
	 */
	public double getSpeedStat() {
		return speedStat;
	}

	/**
	 * @param speedStat The new speed stat of the equipment.
	 */
	public void setSpeedStat(double speedStat) {
		this.speedStat = speedStat;
	}

	/**
	 * @return The durability stat of the equipment.
	 */
	public int getDurabilityStat() {
		return durabilityStat;
	}

	/**
	 * @param durabilityStat The new durability stat of the equipment.
	 */
	public void setDurabilityStat(int durabilityStat) {
		this.durabilityStat = durabilityStat;
	}
	
}
