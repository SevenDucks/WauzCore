package eu.wauz.wauzcore.items.identifiers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import eu.wauz.wauzcore.items.CustomItem;
import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.enums.Rarity;
import eu.wauz.wauzcore.items.enums.Tier;
import eu.wauz.wauzcore.items.runes.insertion.WauzSkillgemInserter;
import eu.wauz.wauzcore.items.weapons.CustomWeapon;
import eu.wauz.wauzcore.items.weapons.CustomWeaponShield;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.system.EventMapper;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A class for generating equipment with all possible skill and stat combinations.
 * 
 * @author Wauzmons
 */
public class WauzEquipmentBuilder {
	
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
	 * The equipment item stack.
	 */
	private ItemStack itemStack;
	
	/**
	 * The item meta of the equipment item.
	 */
	private ItemMeta itemMeta;
	
	/**
	 * The prefix of the name of the equipment's rarity.
	 */
	private String rarityNamePrefix= "";
	
	/**
	 * The prefix of the star rating of the equipment's rarity.
	 */
	private String rarityStarPrefix = "" + ChatColor.YELLOW;
	
	/**
	 * The attack stat of the equipment.
	 */
	private int attackStat = 1;
	
	/**
	 * The defense stat of the equipment.
	 */
	private int defenseStat = 1;
	
	/**
	 * The display of the scaled level of the equipment.
	 */
	private String scalingString;
	
	/**
	 * The display of the durability stat of the equipment.
	 */
	private String durabilityString;
	
	/**
	 * The display of the speed stat of the equipment.
	 */
	private String speedString;
	
	/**
	 * The display of the swiftness stat of the equipment.
	 */
	private String swiftnessString;
	
	/**
	 * The display of the armor category of the equipment.
	 */
	private String armorCategoryString;
	
	/**
	 * The displays of the enhancements of the equipment.
	 */
	private List<String> enhancementStrings = new ArrayList<>();
	
	/**
	 * The suffix to add behind the name of the equipment.
	 */
	private String enhancementSuffix;
	
	/**
	 * The skillgem to socket into the equipment.
	 */
	private WauzPlayerSkill skillgem;
	
	/**
	 * Constructs a builder for creating a new equipment item.
	 * 
	 * @param material The material of the equipment item.
	 */
	public WauzEquipmentBuilder(Material material) {
		itemStack = new ItemStack(material);
		itemMeta = itemStack.getItemMeta();
	}
	
	/**
	 * Adds a random shield pattern to the equipment item.
	 */
	public void addShieldPattern() {
		itemMeta = new ItemStack(Material.SHIELD).getItemMeta();
		CustomWeaponShield.addPattern(itemMeta);
	}
	
	/**
	 * Adds a leather dye to the equipment item.
	 * 
	 * @param color The color of the dye.
	 */
	public void addLeatherDye(Color color) {
		itemMeta = new ItemStack(Material.LEATHER_CHESTPLATE).getItemMeta();
		LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
		leatherArmorMeta.setColor(color);
	}
	
	/**
	 * Adds a skillgem to socket into the equipment item.
	 * 
	 * @param skill The skill to add.
	 */
	public void addSkillgem(WauzPlayerSkill skill) {
		skillgem = skill;
	}
	
	/**
	 * Makes the equipment item unbreakable.
	 */
	public void makeUnbreakable() {
		itemMeta.setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
	}
	
	/**
	 * Adds custom prefixes to the rarity display of the equipment item.
	 * 
	 * @param rarityNamePrefix The prefix of the name of the equipment's rarity.
	 * @param rarityStarPrefix The prefix of the star rating of the equipment's rarity.
	 */
	public void addRarityPrefixes(String rarityNamePrefix, String rarityStarPrefix) {
		this.rarityNamePrefix = rarityNamePrefix;
		this.rarityStarPrefix = rarityStarPrefix;
	}
	
	/**
	 * Adds a main stat, including level requirement to the equipment item.
	 * 
	 * @param attackStat The value of the attack stat.
	 * @param defenseStat The value of the defense stat.
	 * @param requiredLevel The level requirement.
	 * @param scalingLevel The scaling of the stats, where 1 = unscaled.
	 */
	public void addMainStats(int attackStat, int defenseStat, int requiredLevel, float scalingLevel) {
		this.attackStat = attackStat;
		this.defenseStat = defenseStat;
		String levelString = ChatColor.YELLOW + "lvl " + ChatColor.AQUA + requiredLevel + ChatColor.DARK_GRAY + ")";
		scalingString = scalingLevel == 1
				? " " + ChatColor.DARK_GRAY + "(" + levelString
				: " " + ChatColor.DARK_GRAY + "(Scaled x" + scalingLevel + " " + levelString;
	}
	
	/**
	 * Adds a durability stat to the equipment item.
	 * 
	 * @param durabilityStat The value of the stat.
	 */
	public void addDurabilityStat(int durabilityStat) {
		String icon = ChatColor.DARK_GREEN + UnicodeUtils.ICON_ANCHOR + " ";
		Damageable damageable = (Damageable) itemMeta;
		damageable.setDamage(0);
		durabilityString = icon + ChatColor.WHITE + "Durability:" + ChatColor.DARK_GREEN + " " + durabilityStat;
		durabilityString += " " + ChatColor.DARK_GRAY + "/ " + durabilityStat;
	}
	
	/**
	 * Adds a speed stat to the equipment item.
	 * 
	 * @param speedStat The value of the stat.
	 */
	public void addSpeedStat(double speedStat) {
		String icon = ChatColor.RED + UnicodeUtils.ICON_LIGHTNING + " ";
		double genericAttackSpeed = speedStat - 4.0;
		AttributeModifier modifier = new AttributeModifier("generic.attack_speed", genericAttackSpeed, Operation.ADD_NUMBER);
		itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
		speedString = icon + ChatColor.WHITE + "Speed:" + ChatColor.RED + " " + Formatters.DEC_SHORT.format(speedStat);
	}
	
	/**
	 * Adds a swiftness stat to the equipment item.
	 * 
	 * @param swiftnessStat The value of the stat.
	 */
	public void addSwiftnessStat(int swiftnessStat) {
		String icon = ChatColor.BLUE + UnicodeUtils.ICON_LIGHTNING + " ";
		String symbol = swiftnessStat >= 0 ? "+" : "";
		swiftnessString = icon + ChatColor.WHITE + "Swiftness:" + ChatColor.BLUE + " " + symbol + swiftnessStat + " %";
	}
	
	/**
	 * Adds an armor category to the equipment item.
	 * 
	 * @param armorCategory The category.
	 */
	public void addArmorCategory(ArmorCategory armorCategory) {
		String icon = ChatColor.BLUE + UnicodeUtils.ICON_SHIELD + " ";
		armorCategoryString = icon + ChatColor.WHITE + "Category:" + ChatColor.BLUE + " " + armorCategory;
	}
	
	/**
	 * Adds an enhancement string to the equipment item.
	 * 
	 * @param enhancementString The enhancement string.
	 * @param enhancementSuffix The suffix to add behind the name of the equipment or null.
	 */
	public void addEnhancementString(String enhancementString, String enhancementSuffix) {
		enhancementStrings.add(enhancementString);
		if(enhancementSuffix != null) {
			this.enhancementSuffix = enhancementSuffix;
		}
	}
	
	/**
	 * Generates the equipment item stack, as configured in the builder.
	 * 
	 * @param tier The tier of the equipment item.
	 * @param rarity The rarity of the equipment item.
	 * @param type The type of the equipment item.
	 * @param name The name of the equipment item.
	 * 
	 * @return The generated equipment item stack.
	 * 
	 * @see WauzEquipmentBuilder#addLoreIfNotBlank(List, String)
	 * @see WauzEquipmentBuilder#applyCustomItemProperties(List, CustomItem, Rarity)
	 */
	public ItemStack generate(Tier tier, Rarity rarity, EquipmentType type, String name) {
		String identifiedItemName = rarity.getColor() + name;
		if(StringUtils.isNotBlank(enhancementSuffix)) {
			identifiedItemName += enhancementSuffix;
		}
		Components.displayName(itemMeta, identifiedItemName);
		
		List<String> lores = new ArrayList<>();
		String rarityName = rarityNamePrefix + rarity.getName() + " ";
		String rarityStars = rarityStarPrefix + rarity.getStars();
		int sellValue = 0;
		ChatColor typeColor = null;
		if(type.equals(EquipmentType.WEAPON)) {
			typeColor = ChatColor.RED;
			String icon = typeColor + UnicodeUtils.ICON_SWORDS + " ";
			lores.add(ChatColor.WHITE + tier.getName() + " " + rarityName + "Weapon " + rarityStars);
			lores.add("");
			lores.add(icon + ChatColor.WHITE + "Attack:" + typeColor + " " + attackStat + scalingString);
			sellValue = (int) (attackStat * (Math.random() + 0.5) + 1);
		}
		else if(type.equals(EquipmentType.TOOL)) {
			typeColor = ChatColor.DARK_AQUA;
			String icon = typeColor + UnicodeUtils.ICON_PICKAXE + " ";
			lores.add(ChatColor.WHITE + tier.getName() + " " + rarityName + "Tool " + rarityStars);
			lores.add("");
			lores.add(icon + ChatColor.WHITE + "Efficiency:" + typeColor + " " + attackStat);
			sellValue = (int) (attackStat * (Math.random() + 0.5) + 1);
		}
		else if(type.equals(EquipmentType.ARMOR)) {
			typeColor = ChatColor.BLUE;
			String icon = typeColor + UnicodeUtils.ICON_SHIELD + " ";
			lores.add(ChatColor.WHITE + tier.getName() + " " + rarityName + "Armor " + rarityStars);
			lores.add("");
			lores.add(icon + ChatColor.WHITE + "Defense:" + typeColor + " " + defenseStat + scalingString);
			sellValue = (int) (attackStat * (Math.random() + 0.5) * 3 + 1);
		}
		addLoreIfNotBlank(lores, armorCategoryString);
		for(String enhancementString : enhancementStrings) {
			lores.add(typeColor + UnicodeUtils.ICON_DIAMOND + " " + ChatColor.WHITE + enhancementString);
		}
		addLoreIfNotBlank(lores, speedString);
		addLoreIfNotBlank(lores, swiftnessString);
		addLoreIfNotBlank(lores, durabilityString);
		String icon = ChatColor.DARK_GREEN + UnicodeUtils.ICON_BULLSEYE + " ";
		lores.add(icon + ChatColor.WHITE + "Sell Value:" + ChatColor.DARK_GREEN + " " + sellValue);
		if(type.equals(EquipmentType.TOOL)) {
			lores.add("");
			lores.add(ChatColor.GRAY + "Right Click to gather Resource Nodes");
		}
		else {
			applyCustomItemProperties(lores, EventMapper.getCustomItem(itemStack.getType()), rarity);
		}
		
		itemMeta.setLore(lores);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(itemMeta);
		
		if(skillgem != null) {
			ItemStack skillgemItemStack = new ItemStack(Material.REDSTONE);
			WauzSkillgemIdentifier.createSkillgem(skillgemItemStack, skillgem);
			new WauzSkillgemInserter().insertSkillgem(itemStack, skillgemItemStack);
		}
		return itemStack;
	}
	
	/**
	 * Extends the lore list, if the given lore string is not blank.
	 * 
	 * @param lores The list of lores.
	 * @param lore The lore to add.
	 */
	private void addLoreIfNotBlank(List<String> lores, String lore) {
		if(StringUtils.isNotBlank(lore)) {
			lores.add(lore);
		}
	}
	
	/**
	 * Adds the possible properties of a custom item class to the lores.
	 * Custom weapons with magic or higher rarity have a 50% chance to receive a skilgem slot.
	 * Magic or higher items will get 1 rune slot, while epic or higher items will receive 2 slots.
	 * 
	 * @param lores The list of lores.
	 * @param customItem The custom item class.
	 * @param rarity The rarity of the item.
	 */
	private void applyCustomItemProperties(List<String> lores, CustomItem customItem, Rarity rarity) {
		if(customItem != null && customItem instanceof CustomWeapon) {
			CustomWeapon customWeapon = ((CustomWeapon) customItem);
			boolean hasSkillSlot = false;
			if(customWeapon.canHaveSkillSlot() && (skillgem != null || (rarity.getMultiplier() >= 1.5 && Chance.oneIn(2)))) {
				lores.add("");
				lores.add(EMPTY_SKILL_SLOT);
				hasSkillSlot = true;
			}
			lores.addAll(customWeapon.getCustomWeaponLores(hasSkillSlot));
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
