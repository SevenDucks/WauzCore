package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.events.ArmorEquipEvent;
import eu.wauz.wauzcore.events.ArmorEquipEvent.ArmorType;
import eu.wauz.wauzcore.events.ArmorEquipEvent.EquipMethod;
import eu.wauz.wauzcore.items.runes.WauzRuneInserter;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;
import net.md_5.bungee.api.ChatColor;

public class Equipment {
	
	private String type;
	
	private Material material;
	
	private String name;
	
	private double damage;
	
	private int durability;
	
	public Equipment(String type, Material material, String name, double damage, int durability) {
		this.type = type;
		this.material = material;
		this.name = name;
		this.damage = damage;
		this.durability = durability;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
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
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RESET + "Cosmetic Item");
		itemMeta.setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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
		ItemStack skillgem = event.getWhoClicked().getItemOnCursor();
		String skillName = StringUtils.substringAfter(skillgem.getItemMeta().getDisplayName(), ": " + ChatColor.LIGHT_PURPLE);
		WauzPlayerSkill skill = WauzPlayerSkillExecutor.getSkill(skillName);
		
		ItemStack itemStack = event.getCurrentItem();
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		if(ItemUtils.hasSkillgemSocket(itemStack)) {
			List<String> newLores = new ArrayList<>();
			for(String lore : itemMeta.getLore()) {
				if(lore.contains(ChatColor.DARK_RED + "Empty")) {
					newLores.add(ChatColor.WHITE + "Skillgem (" + ChatColor.LIGHT_PURPLE + skill.getSkillId() + ChatColor.WHITE + ")");
					newLores.add(ChatColor.WHITE + skill.getSkillDescription());
					lore = ChatColor.WHITE + skill.getSkillStats();
				}
				newLores.add(lore);
			}
			itemMeta.setLore(newLores);
			itemStack.setItemMeta(itemMeta);
			return true;
		}
		
		return false;
	}
	
	public static boolean clearAllSockets(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		ItemStack itemStack = event.getCurrentItem();
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		boolean valid = false;
		if(!ItemUtils.hasLore(itemStack))
			return valid;
		
		int skipLines = 0;
		int atkManus = ItemUtils.getRuneAtkBoost(itemStack);
		int defManus = ItemUtils.getRuneDefBoost(itemStack);
		
		List<String> newLores = new ArrayList<>();
		for(String lore : itemMeta.getLore()) {
			if(skipLines > 0) {
				skipLines--;
				continue;
			}
			else if(lore.contains("Attack") && atkManus > 0) {
				String[] val = lore.split(" ");
				Integer attack = Integer.parseInt(val[1]);
				int newValue = attack - atkManus;
				lore = lore.replace(
						ChatColor.RED + " " + attack,
						ChatColor.RED + " " + newValue);
				WauzDebugger.log(player, "Manus: " + atkManus);
			}
			else if(lore.contains("Defense") && defManus > 0) {
				String[] val = lore.split(" ");
				Integer defense = Integer.parseInt(val[1]);
				int newValue = defense - defManus;
				lore = lore.replace(
						ChatColor.BLUE + " " + defense,
						ChatColor.BLUE + " " + newValue);	
				WauzDebugger.log(player, "Manus: " + defManus);
			}
			else if(lore.contains("Rune (")) {
				WauzDebugger.log(player, "Cleared Rune Slot");
				lore = WauzIdentifier.EMPTY_RUNE_SLOT;
				valid = true;
			}
			else if(lore.contains("Skillgem (")) {
				WauzDebugger.log(player, "Cleared Skill Slot");
				lore = WauzIdentifier.EMPTY_SKILL_SLOT;
				valid = false;
				skipLines = 2;
			}
			newLores.add(lore);
		}
		
		if(valid) {
			itemMeta.setLore(newLores);
			itemStack.setItemMeta(itemMeta);
		}
		return valid;
	}

}
