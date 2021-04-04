package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.skills.Castable;
import eu.wauz.wauzcore.skills.SkillQuickSlots;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.kyori.adventure.text.Component;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the skill menu, that is used for assigning active skills to action slots.
 * 
 * @author Wauzmons
 *
 * @see PlayerSkillConfigurator
 */
@PublicMenu
public class SkillAssignMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "skill-assign";
	}
	
	/**
	 * @return The modes in which the inventory can be opened.
	 */
	@Override
	public List<WauzMode> getGamemodes() {
		return Arrays.asList(WauzMode.MMORPG);
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
	 * @see SkillAssignMenu#getQuickSlotInfoItemStack()
	 * @see SkillQuickSlots#getCastableInfo(Player, int)
	 * @see MenuUtils#setMenuItem(Inventory, int, ItemStack, String, List)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new SkillAssignMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, Component.text(ChatColor.BLACK + "" + ChatColor.BOLD + "Assign Abilities"));
		
		menu.setItem(0, getQuickSlotInfoItemStack());
		MenuUtils.setMenuItem(menu, 1, GenericIconHeads.getNumber1Item(), ChatColor.RED + "Quick Slot 1", SkillQuickSlots.getCastableInfo(player, 1));
		MenuUtils.setMenuItem(menu, 2, GenericIconHeads.getNumber2Item(), ChatColor.GOLD + "Quick Slot 2", SkillQuickSlots.getCastableInfo(player, 2));
		MenuUtils.setMenuItem(menu, 3, GenericIconHeads.getNumber3Item(), ChatColor.YELLOW + "Quick Slot 3", SkillQuickSlots.getCastableInfo(player, 3));
		MenuUtils.setMenuItem(menu, 4, GenericIconHeads.getNumber4Item(), ChatColor.GREEN + "Quick Slot 4", SkillQuickSlots.getCastableInfo(player, 4));
		MenuUtils.setMenuItem(menu, 5, GenericIconHeads.getNumber5Item(), ChatColor.AQUA + "Quick Slot 5", SkillQuickSlots.getCastableInfo(player, 5));
		MenuUtils.setMenuItem(menu, 6, GenericIconHeads.getNumber6Item(), ChatColor.BLUE + "Quick Slot 6", SkillQuickSlots.getCastableInfo(player, 6));
		MenuUtils.setMenuItem(menu, 7, GenericIconHeads.getNumber7Item(), ChatColor.LIGHT_PURPLE + "Quick Slot 7", SkillQuickSlots.getCastableInfo(player, 7));
		MenuUtils.setMenuItem(menu, 8, GenericIconHeads.getNumber8Item(), ChatColor.DARK_PURPLE + "Quick Slot 8", SkillQuickSlots.getCastableInfo(player, 8));
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Gets an item stack, displaying general information about quick slots.
	 * 
	 * @return The created item stack.
	 */
	public static ItemStack getQuickSlotInfoItemStack() {
		ItemStack infoItemStack = GenericIconHeads.getUnknownItem();
		ItemMeta infoItemMeta = infoItemStack.getItemMeta();
		infoItemMeta.displayName(Component.text(ChatColor.GOLD + "About Quick Slots"));
		List<String> infoLores = new ArrayList<String>();
		infoLores.add(ChatColor.YELLOW + "How to learn Abilities?");
		infoLores.add(ChatColor.GRAY + "You can unlock new combat skills,");
		infoLores.add(ChatColor.GRAY + "by leveling and assigning skill points to");
		infoLores.add(ChatColor.GRAY + "\"Masteries\" in the skill menu. (Click)");
		infoLores.add(ChatColor.YELLOW + "How to use Abilities?");
		infoLores.add(ChatColor.GRAY + "To use the abilities, you have assigned here,");
		infoLores.add(ChatColor.GRAY + "you have to press the F (Swap Items) key,");
		infoLores.add(ChatColor.GRAY + "which will open the casting bar.");
		infoLores.add(ChatColor.GRAY + "Then click the item with the slot number ");
		infoLores.add(ChatColor.GRAY + "of the ability you want to use.");
		infoLores.add("");
		infoLores.add(ChatColor.GRAY + "Tip: Most combat skills' damage output");
		infoLores.add(ChatColor.GRAY + "is determined by the item in the slot.");
		infoItemMeta.setLore(infoLores);
		infoItemStack.setItemMeta(infoItemMeta);
		return infoItemStack;
	}
	
	/**
	 * Opens the skill list to choose from for the given player.
	 * Also shows options to return or open the skill menu, to learn new skills.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerData#getUnlockedCastables()
	 * @see SkillAssignMenu#getQuickSlotInfoItemStack()
	 * @see Castable#getAssignmentItem()
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public void showSkillSelection(Player player) {
		if(!Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8).contains(slot)) {
			return;
		}
		WauzInventoryHolder holder = new WauzInventoryHolder(this);
		List<Castable> unlockedCastables = WauzPlayerDataPool.getPlayer(player).getSkills().getUnlockedCastables();
		int size = MenuUtils.roundInventorySize(unlockedCastables.size() + 2);
		Inventory menu = Bukkit.createInventory(holder, size, Component.text(ChatColor.BLACK + "" + ChatColor.BOLD + "Assign Ability to Slot " + slot));
		page = "choose-skill";
		
		menu.setItem(0, getQuickSlotInfoItemStack());
		List<String> clearLores = Collections.singletonList(ChatColor.GRAY + "Clear Quick Slot Assignment");
		MenuUtils.setMenuItem(menu, 1, GenericIconHeads.getColorCubeItem(), ChatColor.DARK_AQUA + "Assign Nothing", clearLores);
		
		int slot = 2;
		for(Castable castable : unlockedCastables) {
			menu.setItem(slot, castable.getAssignmentItem());
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
	 * @see SkillMenu#open(Player)
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
		else if(ItemUtils.isSpecificItem(clicked, "About Quick Slots")) {
			SkillMenu.open(player);
		}
		else if(page.equals("choose-slot")) {
			slot = event.getRawSlot();
			showSkillSelection(player);
		}
		else if(ItemUtils.isSpecificItem(clicked, "Assign Nothing")) {
			PlayerSkillConfigurator.setQuickSlotSkill(player, slot, "none");
			open(player);
		}
		else if(ItemUtils.isSpecificItem(clicked, "Assign Skill")) {
			String skillName = ItemUtils.getStringBetweenFromLore(clicked, "Skill (", ")");
			PlayerSkillConfigurator.setQuickSlotSkill(player, slot, "Skill :: " + ChatColor.stripColor(skillName));
			open(player);
		}
	}

}
