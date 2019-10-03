package eu.wauz.wauzcore.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.events.ArmorEquipEvent.ArmorType;
import eu.wauz.wauzcore.events.ArmorEquipEvent.EquipMethod;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneInserter;
import eu.wauz.wauzcore.items.runes.insertion.WauzRuneRemover;
import eu.wauz.wauzcore.items.runes.insertion.WauzSkillgemInserter;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class Equipment {
	
	private EquipmentType type;
	
	private Material material;
	
	private String name;
	
	private double mainStat;
	
	private int durabilityStat;
	
	private ArmorCategory category;
	
	private Color leatherDye;
	
	public Equipment(EquipmentType type, Material material, String name) {
		this.type = type;
		this.material = material;
		this.name = name;
		
		category = ArmorCategory.UNKNOWN;
	}

	public EquipmentType getType() {
		return type;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return name;
	}

	public double getMainStat() {
		return mainStat;
	}

	public Equipment withMainStat(double mainStat) {
		this.mainStat = mainStat;
		return this;
	}

	public int getDurabilityStat() {
		return durabilityStat;
	}

	public Equipment withDurabilityStat(int durabilityStat) {
		this.durabilityStat = durabilityStat;
		return this;
	}
	
	public ArmorCategory getCategory() {
		return category;
	}
	
	public Equipment withCategory(ArmorCategory category) {
		this.category = category;
		return this;
	}

	public Color getLeatherDye() {
		return leatherDye;
	}

	public Equipment withLeatherDye(Color leatherDye) {
		this.leatherDye = leatherDye;
		return this;
	}

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
	
	private static boolean doesLevelMatch(Player player, ItemStack armorItemStack) {
		int requiredLevel = ItemUtils.getLevelRequirement(armorItemStack);
		boolean levelMatches = player.getLevel() >= requiredLevel;
		if(!levelMatches) {
			player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
		}
		WauzDebugger.log(player, "Required Level: " + requiredLevel);
		return levelMatches;
	}
	
	private static boolean doesClassMatch(Player player, ItemStack armorItemStack) {
		String raceAndClass = PlayerConfigurator.getCharacterRace(player);
		ArmorCategory armorCategory = ItemUtils.getArmorCategory(armorItemStack);
		ArmorCategory classArmorCategory = ArmorCategory.fromRaceAndClass(raceAndClass);
		boolean unknownCategory = armorCategory.equals(ArmorCategory.UNKNOWN);
		boolean classMatches = unknownCategory || armorCategory.equals(classArmorCategory); 
		if(!classMatches) {
			player.sendMessage(ChatColor.RED + "Your class can't wear " + armorCategory.toString().toLowerCase() + " items!");
		}
		WauzDebugger.log(player, "Armor Category: " + armorCategory);
		return classMatches;
	}
	
	private static ItemStack getCosmeticItem(Material material) {
		return getCosmeticItem(material, null);
	}
	
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
	
	public static boolean insertRune(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack equipmentItemStack = event.getCurrentItem();
		ItemStack runeItemStack = event.getWhoClicked().getItemOnCursor();
		return new WauzRuneInserter().insertRune(player, equipmentItemStack, runeItemStack);
	}
	
	public static boolean insertSkillgem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack equipmentItemStack = event.getCurrentItem();
		ItemStack skillgemItemStack = event.getWhoClicked().getItemOnCursor();
		return new WauzSkillgemInserter().insertSkillgem(player, equipmentItemStack, skillgemItemStack);
	}
	
	public static boolean clearAllSockets(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack equipmentItemStack = event.getCurrentItem();
		return new WauzRuneRemover().clearAllSockets(player, equipmentItemStack);
	}

}
