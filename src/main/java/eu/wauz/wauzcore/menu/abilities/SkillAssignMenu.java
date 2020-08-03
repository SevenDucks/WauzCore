package eu.wauz.wauzcore.menu.abilities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.MenuIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.skills.execution.SkillQuickSlots;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the skill menu, that is used for assigning active skills to action slots.
 * 
 * @author Wauzmons
 *
 * @see PlayerSkillConfigurator
 */
public class SkillAssignMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "skill-assign";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		SkillAssignMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Lets the player select a slot to assign a skill to.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see SkillQuickSlots#getSkillInfo(Player, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new SkillAssignMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Assign Active Skills");
		
		ItemStack skillSlot1ItemStack = GenericIconHeads.getNumber1Item();
		ItemMeta skillSlot1ItemMeta = skillSlot1ItemStack.getItemMeta();
		skillSlot1ItemMeta.setDisplayName(ChatColor.RED + "Quick Slot 1");
		skillSlot1ItemMeta.setLore(SkillQuickSlots.getSkillInfo(player, 1));
		skillSlot1ItemStack.setItemMeta(skillSlot1ItemMeta);
		menu.setItem(1, skillSlot1ItemStack);
		
		ItemStack skillSlot2ItemStack = GenericIconHeads.getNumber2Item();
		ItemMeta skillSlot2ItemMeta = skillSlot2ItemStack.getItemMeta();
		skillSlot2ItemMeta.setDisplayName(ChatColor.GOLD + "Quick Slot 2");
		skillSlot2ItemMeta.setLore(SkillQuickSlots.getSkillInfo(player, 2));
		skillSlot2ItemStack.setItemMeta(skillSlot2ItemMeta);
		menu.setItem(3, skillSlot2ItemStack);
		
		ItemStack skillSlot3ItemStack = GenericIconHeads.getNumber3Item();
		ItemMeta skillSlot3ItemMeta = skillSlot3ItemStack.getItemMeta();
		skillSlot3ItemMeta.setDisplayName(ChatColor.YELLOW + "Quick Slot 3");
		skillSlot3ItemMeta.setLore(SkillQuickSlots.getSkillInfo(player, 3));
		skillSlot3ItemStack.setItemMeta(skillSlot3ItemMeta);
		menu.setItem(5, skillSlot3ItemStack);
		
		ItemStack skillSlot4ItemStack = GenericIconHeads.getNumber4Item();
		ItemMeta skillSlot4ItemMeta = skillSlot4ItemStack.getItemMeta();
		skillSlot4ItemMeta.setDisplayName(ChatColor.GREEN + "Quick Slot 4");
		skillSlot4ItemMeta.setLore(SkillQuickSlots.getSkillInfo(player, 4));
		skillSlot4ItemStack.setItemMeta(skillSlot4ItemMeta);
		menu.setItem(7, skillSlot4ItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Opens the skill list to choose from for the given player.
	 * Also shows options to return or open the skill menu, to learn new skills.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerData#getUnlockedSkills()
	 * @see SkillQuickSlots#getSkillInfo(WauzPlayerSkill)
	 */
	public void showSkillSelection(Player player) {
		if(!Arrays.asList(1, 2, 3, 4).contains(slot)) {
			return;
		}
		WauzInventoryHolder holder = new WauzInventoryHolder(this);
		List<WauzPlayerSkill> unlockedSkills = WauzPlayerDataPool.getPlayer(player).getUnlockedSkills();
		int size = MenuUtils.roundInventorySize(unlockedSkills.size());
		Inventory menu = Bukkit.createInventory(holder, size, ChatColor.BLACK + "" + ChatColor.BOLD + "Assign Skill to Slot " + slot);
		page = "choose-skill";
		
		int slot = 0;
		for(WauzPlayerSkill skill : unlockedSkills) {
			ItemStack skillItemStack = MenuIconHeads.getSkillItem();
			ItemMeta skillItemMeta = skillItemStack.getItemMeta();
			skillItemMeta.setDisplayName(ChatColor.DARK_AQUA + "Assign Skill");
			skillItemMeta.setLore(SkillQuickSlots.getSkillInfo(skill));
			skillItemStack.setItemMeta(skillItemMeta);
			menu.setItem(slot, skillItemStack);
			slot++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The name of the current assignment page view.
	 */
	private String page = "choose-slot";
	
	/**
	 * The quick slot, the skill should be assigned to.
	 */
	private int slot;

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and lets the player select a skill for the selected slot.
	 * Also handles the event, to open the skill menu, to learn new skills.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see SkillAssignMenu#showSkillSelection(Player)
	 * @see PlayerSkillConfigurator#setQuickSlotSkill(Player, int, String)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !clicked.getType().equals(Material.PLAYER_HEAD)) {
			return;
		}
		else if(page.equals("choose-slot")) {
			slot = (event.getRawSlot() + 1) / 2;
			showSkillSelection(player);
		}
		else if(page.equals("choose-skill") && ItemUtils.isSpecificItem(clicked, "Assign Skill")) {
			String skillName = ItemUtils.getStringBetweenFromLore(clicked, "Skill (", ")");
			PlayerSkillConfigurator.setQuickSlotSkill(player, slot, ChatColor.stripColor(skillName));
			open(player);
		}
	}

}
