package eu.wauz.wauzcore.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.EquipmentConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.events.ArmorEquipEvent.ArmorType;
import eu.wauz.wauzcore.events.ArmorEquipEvent.EquipMethod;
import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.enums.EquipmentType;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.items.runes.RuneHardening;
import eu.wauz.wauzcore.items.runes.insertion.WauzRune;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneRemover;
import eu.wauz.wauzcore.items.runes.insertion.WauzSkillgemInserter;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.skills.SkillTheChariot;
import eu.wauz.wauzcore.skills.SkillTheMagician;
import eu.wauz.wauzcore.skills.SkillTheStar;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * A class that represents a base type of equipment.
 * Also contains static methods to verify or generate equipment items.
 * 
 * @author Wauzmons
 */
public class WauzEquipment {
	
	/**
	 * Initializes all equipment base types and registers them.
	 * 
	 * @see EquipmentConfigurator#getEquipmentKeys()
	 * @see WauzEquipmentIdentifier#addEquipType(WauzEquipment)
	 */
	public static void init() {
		for(String equipmentKey : EquipmentConfigurator.getEquipmentKeys()) {
			EquipmentType type = EquipmentConfigurator.getEquipmentType(equipmentKey);
			Material material = EquipmentConfigurator.getMaterial(equipmentKey);
			String name = EquipmentConfigurator.getName(equipmentKey);
			
			WauzEquipment equipment = new WauzEquipment(type, material, name);
			equipment.withMainStat(EquipmentConfigurator.getMainStat(equipmentKey));
			equipment.withSpeedStat(EquipmentConfigurator.getSpeedStat(equipmentKey));
			equipment.withDurabilityStat(EquipmentConfigurator.getDurabilityStat(equipmentKey));
			equipment.withCategory(EquipmentConfigurator.getCategory(equipmentKey));
			equipment.withLeatherDye(EquipmentConfigurator.getLeatherDye(equipmentKey));
			
			WauzEquipmentIdentifier.addEquipType(equipment);
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + WauzEquipmentIdentifier.getEquipmentTypeCount() + " Equipment Types!");
	}
	
	/**
	 * Equips a fitting cosmetic set of armor, when the chestplate changed.
	 * If level or class aren't matching, the event is cancelled.
	 * 
	 * @param event The armor equip event.
	 * 
	 * @see WauzEquipment#doesLevelMatch(Player, ItemStack)
	 * @see WauzEquipment#doesClassMatch(Player, ItemStack)
	 */
	public static void equipArmor(ArmorEquipEvent event) {
		if(EquipMethod.DEATH.equals(event.getEquipMethod())) {
			return;
		}
		if(!ArmorType.CHESTPLATE.equals(event.getArmorType())) {
			return;
		}
		
		Player player = event.getPlayer();
		ItemStack armorItemStack = event.getNewArmorPiece();
		ItemStack oldItemStack = event.getOldArmorPiece();
		WauzDebugger.log(player, "Equipping: " + event.getEquipMethod() + " " + event.getArmorType());
		WauzDebugger.log(player, "New: " + (armorItemStack != null ? armorItemStack.getType() : "none"));
		WauzDebugger.log(player, "Old: " + (oldItemStack != null ? oldItemStack.getType() : "none"));
		
		
		if(!doesLevelMatch(player, armorItemStack) || !doesClassMatch(player, armorItemStack)) {
			event.setCancelled(true);
			return;
		}
		
		if(armorItemStack == null || armorItemStack.getType().equals(Material.AIR)) {
			player.getEquipment().setLeggings(null);
			player.getEquipment().setBoots(null);
			return;
		}
		if(armorItemStack.getType().equals(Material.LEATHER_CHESTPLATE)) {
			if(armorItemStack.getItemMeta() instanceof LeatherArmorMeta) {
				Color color = ((LeatherArmorMeta) armorItemStack.getItemMeta()).getColor();
				player.getEquipment().setLeggings(getCosmeticItem(Material.LEATHER_LEGGINGS, color));
				player.getEquipment().setBoots(getCosmeticItem(Material.LEATHER_BOOTS, color));
				return;
			}
			player.getEquipment().setLeggings(getCosmeticItem(Material.LEATHER_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.LEATHER_BOOTS));
			return;
		}
		if(armorItemStack.getType().equals(Material.GOLDEN_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.GOLDEN_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.GOLDEN_BOOTS));
			return;
		}
		if(armorItemStack.getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.CHAINMAIL_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.CHAINMAIL_BOOTS));
			return;
		}
		if(armorItemStack.getType().equals(Material.IRON_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.IRON_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.IRON_BOOTS));
			return;
		}
		if(armorItemStack.getType().equals(Material.DIAMOND_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.DIAMOND_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.DIAMOND_BOOTS));
			return;
		}
	}
	
	/**
	 * Checks if the player has the right level to equip an item.
	 * 
	 * @param player The player to check.
	 * @param armorItemStack The item, that the player wants to equip.
	 * 
	 * @return If the level matches.
	 */
	private static boolean doesLevelMatch(Player player, ItemStack armorItemStack) {
		int requiredLevel = EquipmentUtils.getLevelRequirement(armorItemStack);
		boolean levelMatches = player.getLevel() >= requiredLevel;
		if(!levelMatches) {
			player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
		}
		WauzDebugger.log(player, "Required Level: " + requiredLevel);
		return levelMatches;
	}
	
	/**
	 * Checks if the player has the right class to equip an item.
	 * 
	 * @param player The player to check.
	 * @param armorItemStack The item, that the player wants to equip.
	 * 
	 * @return If the class matches.
	 */
	private static boolean doesClassMatch(Player player, ItemStack armorItemStack) {
		String characterClass = PlayerConfigurator.getCharacterClass(player);
		ArmorCategory armorCategory = EquipmentUtils.getArmorCategory(armorItemStack);
		ArmorCategory classArmorCategory = ArmorCategory.fromClass(characterClass);
		boolean unknownCategory = armorCategory.equals(ArmorCategory.UNKNOWN);
		boolean classMatches = unknownCategory || armorCategory.equals(classArmorCategory); 
		if(!classMatches) {
			player.sendMessage(ChatColor.RED + "Your class can't wear " + armorCategory.toString().toLowerCase() + " items!");
		}
		WauzDebugger.log(player, "Armor Category: " + armorCategory);
		return classMatches;
	}
	
	/**
	 * Gets a cosmetic item with given material.
	 * 
	 * @param material The material of the item.
	 * 
	 * @return The generated item stack.
	 */
	private static ItemStack getCosmeticItem(Material material) {
		return getCosmeticItem(material, null);
	}
	
	/**
	 * Gets a cosmetic item with given material and color.
	 * 
	 * @param material The material of the item.
	 * @param color The color of the leather dye.
	 * 
	 * @return The generated item stack.
	 */
	private static ItemStack getCosmeticItem(Material material, Color color) {
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RESET + "Cosmetic Item");
		itemMeta.setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		if(color != null) {
			((LeatherArmorMeta) itemMeta).setColor(color);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	/**
	 * Tries to insert a rune into a piece of equipment.
	 * 
	 * @param event The inventory event.
	 * 
	 * @return If it was successful.
	 * 
	 * @see WauzRuneInserter#insertRune(Player, ItemStack, ItemStack)
	 */
	public static boolean insertRune(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack equipmentItemStack = event.getCurrentItem();
		ItemStack runeItemStack = event.getWhoClicked().getItemOnCursor();
		return new WauzRuneInserter().insertRune(player, equipmentItemStack, runeItemStack);
	}
	
	/**
	 * Tries to insert a skillgem into a piece of equipment.
	 * 
	 * @param event The inventory event.
	 * 
	 * @return If it was successful.
	 * 
	 * @see WauzSkillgemInserter#insertSkillgem(Player, ItemStack, ItemStack)
	 */
	public static boolean insertSkillgem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack equipmentItemStack = event.getCurrentItem();
		ItemStack skillgemItemStack = event.getWhoClicked().getItemOnCursor();
		return new WauzSkillgemInserter().insertSkillgem(player, equipmentItemStack, skillgemItemStack);
	}
	
	/**
	 * Tries to remove all runes and gems from a piece of equipment.
	 * 
	 * @param event The inventory event.
	 * 
	 * @return If it was successful.
	 * 
	 * @see WauzRuneRemover#clearAllSockets(Player, ItemStack)
	 */
	public static boolean clearAllSockets(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack equipmentItemStack = event.getCurrentItem();
		return new WauzRuneRemover().clearAllSockets(player, equipmentItemStack);
	}
	
	/**
	 * Gets a starter weapon for the nephilim.
	 * Contains the skill "the magician".
	 * 
	 * @return The generated weapon.
	 * 
	 * @see WauzDebugger#getSkillgemWeapon(WauzPlayerSkill, boolean)
	 */
	public static ItemStack getNephilimStarterWeapon() {
		WauzPlayerSkill skill = new SkillTheMagician();
		return WauzDebugger.getSkillgemWeapon(skill, false);
	}
	
	/**
	 * Gets a starter weapon for the crusader.
	 * Contains the skill "the chariot".
	 * 
	 * @return The generated weapon.
	 * 
	 * @see WauzDebugger#getSkillgemWeapon(WauzPlayerSkill, boolean)
	 */
	public static ItemStack getCrusaderStarterWeapon() {
		WauzPlayerSkill skill = new SkillTheChariot();
		return WauzDebugger.getSkillgemWeapon(skill, false);
	}

	/**
	 * Gets a starter weapon for the assassin.
	 * Contains the skill "the star".
	 * 
	 * @return The generated weapon.
	 * 
	 * @see WauzDebugger#getSkillgemWeapon(WauzPlayerSkill, boolean)
	 */
	public static ItemStack getAssassinStarterWeapon() {
		WauzPlayerSkill skill = new SkillTheStar();
		return WauzDebugger.getSkillgemWeapon(skill, false);
	}
	
	/**
	 * Gets a starter rune of hardening.
	 * 
	 * @return The generated rune.
	 * 
	 * @see WauzDebugger#getRune(Player, String)
	 */
	public static ItemStack getStarterRune() {
		WauzRune rune = new RuneHardening();
		return WauzDebugger.getRune(rune.getRuneId(), false);
	}
	
	/**
	 * The general type of the equipment.
	 */
	private EquipmentType type;
	
	/**
	 * The material of the equipment.
	 */
	private Material material;
	
	/**
	 * The name of the equipment.
	 */
	private String name;
	
	/**
	 * The main stat value of the equipment.
	 */
	private double mainStat;
	
	/**
	 * The speed stat value of the equipment.
	 */
	private double speedStat;
	
	/**
	 * The durability stat value of the equipment.
	 */
	private int durabilityStat;
	
	/**
	 * The armor category of the equipment.
	 */
	private ArmorCategory category;
	
	/**
	 * The leather color of the equipment.
	 */
	private Color leatherDye;
	
	/**
	 * Creates a new base equipment type.
	 * 
	 * @param type The general type of the equipment.
	 * @param material The material of the equipment.
	 * @param name The name of the equipment.
	 */
	public WauzEquipment(EquipmentType type, Material material, String name) {
		this.type = type;
		this.material = material;
		this.name = name;
		
		category = ArmorCategory.UNKNOWN;
	}

	/**
	 * @return The general type of the equipment.
	 */
	public EquipmentType getType() {
		return type;
	}

	/**
	 * @return The material of the equipment.
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @return The name of the equipment.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The main stat value of the equipment.
	 */
	public double getMainStat() {
		return mainStat;
	}

	/**
	 * @param mainStat The new main stat value of the equipment.
	 * 
	 * @return The updated equipment.
	 */
	public WauzEquipment withMainStat(double mainStat) {
		this.mainStat = mainStat;
		return this;
	}

	/**
	 * @return The speed stat value of the equipment.
	 */
	public double getSpeedStat() {
		return speedStat;
	}

	/**
	 * @param speedStat The new speed stat value of the equipment.
	 * 
	 * @return The updated equipment.
	 */
	public WauzEquipment withSpeedStat(double speedStat) {
		this.speedStat = speedStat;
		return this;
	}

	/**
	 * @return The durability stat value of the equipment.
	 */
	public int getDurabilityStat() {
		return durabilityStat;
	}

	/**
	 * @param durabilityStat The new durability stat value of the equipment.
	 * 
	 * @return The updated equipment.
	 */
	public WauzEquipment withDurabilityStat(int durabilityStat) {
		this.durabilityStat = durabilityStat;
		return this;
	}
	
	/**
	 * @return The armor category of the equipment.
	 */
	public ArmorCategory getCategory() {
		return category;
	}
	
	/**
	 * @param category The new armor category of the equipment.
	 * 
	 * @return The updated equipment.
	 */
	public WauzEquipment withCategory(ArmorCategory category) {
		this.category = category;
		return this;
	}

	/**
	 * @return The leather color of the equipment.
	 */
	public Color getLeatherDye() {
		return leatherDye;
	}

	/**
	 * @param leatherDye The new leather color of the equipment.
	 * 
	 * @return The updated equipment.
	 */
	public WauzEquipment withLeatherDye(Color leatherDye) {
		this.leatherDye = leatherDye;
		return this;
	}

}
