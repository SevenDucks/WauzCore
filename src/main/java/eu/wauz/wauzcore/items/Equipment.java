package eu.wauz.wauzcore.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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
	
	private Color leatherDye;
	
	public Equipment(EquipmentType type, Material material, String name) {
		this.type = type;
		this.material = material;
		this.name = name;
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

	public Color getLeatherDye() {
		return leatherDye;
	}

	public Equipment withLeatherDye(Color leatherDye) {
		this.leatherDye = leatherDye;
		return this;
	}

	public static void equipArmor(ArmorEquipEvent event) {
		if(EquipMethod.DEATH.equals(event.getEquipMethod()))
			return;
		
		Player player = event.getPlayer();
		ItemStack newItem = event.getNewArmorPiece();
		ItemStack oldItem = event.getOldArmorPiece();
		WauzDebugger.log(player, "Equipping: " + event.getEquipMethod() + " " + event.getArmorType());
		WauzDebugger.log(player, "New: " + (newItem != null ? newItem.getType() : "none"));
		WauzDebugger.log(player, "Old: " + (oldItem != null ? oldItem.getType() : "none"));
		
		if(!ArmorType.CHESTPLATE.equals(event.getArmorType()))
			return;
		
		ItemStack armor = event.getNewArmorPiece();
		int requiredLevel = ItemUtils.getLevelRequirement(armor);
		WauzDebugger.log(player, "Required Level: " + requiredLevel);
		if(player.getLevel() < requiredLevel) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
			return;
		}
		
		if(armor == null || armor.getType().equals(Material.AIR)) {
			player.getEquipment().setLeggings(null);
			player.getEquipment().setBoots(null);
			return;
		}
		if(armor.getType().equals(Material.LEATHER_CHESTPLATE)) {
			if(armor.getItemMeta() instanceof LeatherArmorMeta) {
				Color color = ((LeatherArmorMeta) armor.getItemMeta()).getColor();
				player.getEquipment().setLeggings(getCosmeticItem(Material.LEATHER_LEGGINGS, color));
				player.getEquipment().setBoots(getCosmeticItem(Material.LEATHER_BOOTS, color));
				return;
			}
			player.getEquipment().setLeggings(getCosmeticItem(Material.LEATHER_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.LEATHER_BOOTS));
			return;
		}
		if(armor.getType().equals(Material.GOLDEN_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.GOLDEN_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.GOLDEN_BOOTS));
			return;
		}
		if(armor.getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.CHAINMAIL_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.CHAINMAIL_BOOTS));
			return;
		}
		if(armor.getType().equals(Material.IRON_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.IRON_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.IRON_BOOTS));
			return;
		}
		if(armor.getType().equals(Material.DIAMOND_CHESTPLATE)) {
			player.getEquipment().setLeggings(getCosmeticItem(Material.DIAMOND_LEGGINGS));
			player.getEquipment().setBoots(getCosmeticItem(Material.DIAMOND_BOOTS));
			return;
		}
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
