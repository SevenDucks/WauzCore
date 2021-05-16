package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.Backpack;
import eu.wauz.wauzcore.menu.heads.GenericIconHeads;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.heads.SkillIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionSkills;
import eu.wauz.wauzcore.players.classes.Learnable;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.skills.passive.PassiveBreath;
import eu.wauz.wauzcore.skills.passive.PassiveNutrition;
import eu.wauz.wauzcore.skills.passive.PassiveWeight;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the abilities menu, that is used for viewing passive skills and spending statpoints.
 * 
 * @author Wauzmons
 *
 * @see PlayerSkillConfigurator
 */
@PublicMenu
public class SkillMenu implements WauzInventory {
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "skills";
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
		SkillMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Lists all passive skills, to spent points in, aswell as weapon skill stats.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see PlayerSkillConfigurator#getHealth(Player)
	 * @see PlayerSkillConfigurator#getTrading(Player)
	 * @see PlayerSkillConfigurator#getLuck(Player)
	 * @see PlayerSkillConfigurator#getMana(Player)
	 * @see PlayerSkillConfigurator#getStrength(Player)
	 * @see PlayerSkillConfigurator#getAgility(Player)
	 * @see SkillMenu#getMasteryItemStack(int)
	 * 
	 * @see PlayerSkillConfigurator#getStaffSkill(Player)
	 * @see PlayerSkillConfigurator#getAxeSkill(Player)
	 * @see PlayerSkillConfigurator#getSwordSkill(Player)
	 * @see WauzPlayerDataSectionSkills#getCachedPassive(String)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		SkillMenu skillMenu = new SkillMenu(player);
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		int pts = PlayerSkillConfigurator.getUnusedStatpoints(player);
		String pointsText = ChatColor.DARK_RED + "" + ChatColor.BOLD + pts + " Points";
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Passive Skills " + pointsText;
		Inventory menu = Components.inventory(skillMenu, menuTitle, 18);
		
		int spent = PlayerSkillConfigurator.getHealthStatpoints(player);
		ItemStack skillHealthItemStack = SkillIconHeads.getSkillHealthItem(spent);			
		ItemMeta skillHealthItemMeta = skillHealthItemStack.getItemMeta();
		Components.displayName(skillHealthItemMeta, ChatColor.DARK_GREEN + "Health");
		List<String> skillHealthLores = new ArrayList<String>();
		skillHealthLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		skillHealthLores.add("");
		skillHealthLores.add(ChatColor.GRAY + "Increases Maximum Hitpoints by 5.");
		skillHealthLores.add(ChatColor.GRAY + "Also instantly refills your Hitpoints.");
		skillHealthLores.add("");
		skillHealthLores.add(ChatColor.WHITE + "Maximum Base HP: " + ChatColor.RED
				+ PlayerSkillConfigurator.getHealth(player));
		Components.lore(skillHealthItemMeta, skillHealthLores);
		skillHealthItemStack.setItemMeta(skillHealthItemMeta);
		menu.setItem(0, skillHealthItemStack);
		
		spent = PlayerSkillConfigurator.getTradingStatpoints(player);
		ItemStack skillTradingItemStack = SkillIconHeads.getSkillTradingItem(spent);	
		ItemMeta skillTradingItemMeta = skillTradingItemStack.getItemMeta();
		Components.displayName(skillTradingItemMeta, ChatColor.DARK_GREEN + "Trading");
		List<String> skillTradingLores = new ArrayList<String>();
		skillTradingLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		skillTradingLores.add("");
		skillTradingLores.add(ChatColor.GRAY + "Increases Sell Income by 10%.");
		skillTradingLores.add(ChatColor.GRAY + "Applies for all ways of gaining Coins.");
		skillTradingLores.add("");
		skillTradingLores.add(ChatColor.WHITE + "Coin Multiplier: " + ChatColor.GOLD
				+ PlayerSkillConfigurator.getTrading(player) + "%");
		Components.lore(skillTradingItemMeta, skillTradingLores);
		skillTradingItemStack.setItemMeta(skillTradingItemMeta);
		menu.setItem(1, skillTradingItemStack);
		
		spent = PlayerSkillConfigurator.getLuckStatpoints(player);
		ItemStack skillLuckItemStack = SkillIconHeads.getSkillLuckItem(spent);	
		ItemMeta skillLuckItemMeta = skillLuckItemStack.getItemMeta();
		Components.displayName(skillLuckItemMeta, ChatColor.DARK_GREEN + "Luck");
		List<String> skillLuckLores = new ArrayList<String>();
		skillLuckLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		skillLuckLores.add("");
		skillLuckLores.add(ChatColor.GRAY + "Increases Rate on every 3rd Identify by 10%.");
		skillLuckLores.add(ChatColor.GRAY + "Enhanced Equipment has special Effects.");
		skillLuckLores.add("");
		skillLuckLores.add(ChatColor.WHITE + "Enhance-Rate: " + ChatColor.YELLOW
				+ PlayerSkillConfigurator.getLuck(player) + "%");
		Components.lore(skillLuckItemMeta, skillLuckLores);
		skillLuckItemStack.setItemMeta(skillLuckItemMeta);
		menu.setItem(2, skillLuckItemStack);
		
		spent = PlayerSkillConfigurator.getManaStatpoints(player);
		ItemStack skillMagicItemStack = SkillIconHeads.getSkillMagicItem(spent);	
		ItemMeta skillMagicItemMeta = skillMagicItemStack.getItemMeta();
		Components.displayName(skillMagicItemMeta, ChatColor.DARK_GREEN + "Magic");
		List<String> skillMagicLores = new ArrayList<String>();
		skillMagicLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		skillMagicLores.add("");
		skillMagicLores.add(ChatColor.GRAY + "Increases Maximum Mana Points by 1.");
		skillMagicLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Staff Fighting.");
		skillMagicLores.add("");
		skillMagicLores.add(ChatColor.WHITE + "Maximum MP: " + ChatColor.LIGHT_PURPLE
				+ PlayerSkillConfigurator.getMana(player)
				+ ChatColor.GRAY + " (Max: 50)");
		Components.lore(skillMagicItemMeta, skillMagicLores);
		skillMagicItemStack.setItemMeta(skillMagicItemMeta);
		menu.setItem(3, skillMagicItemStack);
		
		spent = PlayerSkillConfigurator.getStrengthStatpoints(player);
		ItemStack skillStrengthItemStack = SkillIconHeads.getSkillStrengthItem(spent);	
		ItemMeta skillStrengthItemMeta = skillStrengthItemStack.getItemMeta();
		Components.displayName(skillStrengthItemMeta, ChatColor.DARK_GREEN + "Strength");
		List<String> skillStrengthLores = new ArrayList<String>();
		skillStrengthLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		skillStrengthLores.add("");
		skillStrengthLores.add(ChatColor.GRAY + "Increases Defense from Equip by 5%.");
		skillStrengthLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Axe Combat.");
		skillStrengthLores.add("");
		skillStrengthLores.add(ChatColor.WHITE + "Defense Multiplier: " + ChatColor.BLUE
				+ PlayerSkillConfigurator.getStrength(player) + "%"
				+ ChatColor.GRAY + " (Max: 300%)");
		Components.lore(skillStrengthItemMeta, skillStrengthLores);
		skillStrengthItemStack.setItemMeta(skillStrengthItemMeta);
		menu.setItem(4, skillStrengthItemStack);
		
		spent = PlayerSkillConfigurator.getAgilityStatpoints(player);
		ItemStack skillAgilityItemStack = SkillIconHeads.getSkillAgilityItem(spent);	
		ItemMeta skillAgilityItemMeta = skillAgilityItemStack.getItemMeta();
		Components.displayName(skillAgilityItemMeta, ChatColor.DARK_GREEN + "Agility");
		List<String> skillAgilityLores = new ArrayList<String>();
		skillAgilityLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		skillAgilityLores.add("");
		skillAgilityLores.add(ChatColor.GRAY + "Increases Evasion/Crit-Chance by 1%.");
		skillAgilityLores.add(ChatColor.GRAY + "Additionally adds a 5% Bonus to Sword Art.");
		skillAgilityLores.add("");
		skillAgilityLores.add(ChatColor.WHITE + "Evasion/Crit-Chance: " + ChatColor.AQUA
				+ PlayerSkillConfigurator.getAgility(player) + "%"
				+ ChatColor.GRAY + " (Max: 40%)");
		Components.lore(skillAgilityItemMeta, skillAgilityLores);
		skillAgilityItemStack.setItemMeta(skillAgilityItemMeta);
		menu.setItem(5, skillAgilityItemStack);
		
		for(int index = 1; index <= skillMenu.getSubclassCount(); index++) {
			menu.setItem(index + 8, skillMenu.getMasteryItemStack(index));
		}
		
		ItemStack skillAssignmentItemStack = GenericIconHeads.getColorCubeItem();
		MenuUtils.setItemDisplayName(skillAssignmentItemStack, ChatColor.DARK_AQUA + "Assign Abilities");
		MenuUtils.addItemLore(skillAssignmentItemStack, ChatColor.WHITE + "Select Skills to use in Combat", true);
		menu.setItem(14, skillAssignmentItemStack);
		
		ItemStack skillWeaponStaffItemStack = new ItemStack(Material.IRON_HOE, 1);
		ItemMeta skillWeaponStaffItemMeta = skillWeaponStaffItemStack.getItemMeta();
		Components.displayName(skillWeaponStaffItemMeta, ChatColor.DARK_RED + "Staff Fighting");
		List<String> skillWeaponStaffLores = new ArrayList<String>();
		skillWeaponStaffLores.add(ChatColor.WHITE + "Atk: " + ChatColor.RED
				+ Formatters.DEC.format((float) ((float) PlayerSkillConfigurator.getStaffSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerSkillConfigurator.getStaffSkillMax(player) / 1000) + "%)");
		skillWeaponStaffLores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1) + " from Magic");
		skillWeaponStaffLores.add("");
		skillWeaponStaffLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponStaffLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponStaffLores.add(ChatColor.GRAY + "the damage dealt with them.");
		Components.lore(skillWeaponStaffItemMeta, skillWeaponStaffLores);
		skillWeaponStaffItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponStaffItemStack.setItemMeta(skillWeaponStaffItemMeta);
		menu.setItem(6, skillWeaponStaffItemStack);
		
		ItemStack skillWeaponAxeItemStack = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta skillWeaponAxeItemMeta = skillWeaponAxeItemStack.getItemMeta();
		Components.displayName(skillWeaponAxeItemMeta, ChatColor.DARK_RED + "Axe Combat");
		List<String> skillWeaponAxeLores = new ArrayList<String>();
		skillWeaponAxeLores.add(ChatColor.WHITE + "Atk: " + ChatColor.RED
				+ Formatters.DEC.format((float) ((float) PlayerSkillConfigurator.getAxeSkill(player) / 1000)) + "%"
				+ ChatColor.GRAY + " (Max: "
				+ (int) (PlayerSkillConfigurator.getAxeSkillMax(player) / 1000) + "%)");
		skillWeaponAxeLores.add(ChatColor.GRAY + "Multiplied by "
				+ ((float) PlayerSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1) + " from Strength");
		skillWeaponAxeLores.add("");
		skillWeaponAxeLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponAxeLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponAxeLores.add(ChatColor.GRAY + "the damage dealt with them.");
		Components.lore(skillWeaponAxeItemMeta, skillWeaponAxeLores);
		skillWeaponAxeItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponAxeItemStack.setItemMeta(skillWeaponAxeItemMeta);
		menu.setItem(7, skillWeaponAxeItemStack);
		
		ItemStack skillWeaponSwordItemStack = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta skillWeaponSwordItemMeta = skillWeaponSwordItemStack.getItemMeta();
		Components.displayName(skillWeaponSwordItemMeta, ChatColor.DARK_RED + "Sword Art");
		List<String> skillWeaponSwordLores = new ArrayList<String>();
		skillWeaponSwordLores.add(ChatColor.WHITE + "Atk: " + ChatColor.RED
			+ Formatters.DEC.format((float) ((float) PlayerSkillConfigurator.getSwordSkill(player) / 1000)) + "%"
			+ ChatColor.GRAY + " (Max: "
			+ (int) (PlayerSkillConfigurator.getSwordSkillMax(player) / 1000) + "%)");
		skillWeaponSwordLores.add(ChatColor.GRAY + "Multiplied by "
			+ ((float) PlayerSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1) + " from Agility");
		skillWeaponSwordLores.add("");
		skillWeaponSwordLores.add(ChatColor.GRAY + "Fighting with Weapons from this Type");
		skillWeaponSwordLores.add(ChatColor.GRAY + "will increase your skill and thus");
		skillWeaponSwordLores.add(ChatColor.GRAY + "the damage dealt with them.");
		Components.lore(skillWeaponSwordItemMeta, skillWeaponSwordLores);
		skillWeaponSwordItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeaponSwordItemStack.setItemMeta(skillWeaponSwordItemMeta);
		menu.setItem(8, skillWeaponSwordItemStack);
		
		AbstractPassiveSkill breathSkill = playerData.getSkills().getCachedPassive(PassiveBreath.PASSIVE_NAME);
		ItemStack skillBreathItemStack = new ItemStack(Material.DIAMOND_HELMET, 1);
		ItemMeta skillBreathItemMeta = skillBreathItemStack.getItemMeta();
		Components.displayName(skillBreathItemMeta, ChatColor.DARK_BLUE + "Breath (Passive)");
		List<String> skillBreathLores = new ArrayList<String>();
		skillBreathLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.BLUE + breathSkill.getLevel());
		skillBreathLores.add("");
		skillBreathLores.add(ChatColor.GRAY + "Sprinting long distances");
		skillBreathLores.add(ChatColor.GRAY + "will increase your skill and slowly");
		skillBreathLores.add(ChatColor.GRAY + "improve your walking speed.");
		skillBreathLores.add("");
		skillBreathLores.add(ChatColor.WHITE + "Additional Speed: " + ChatColor.DARK_BLUE
				+ breathSkill.getLevel() + "%");
		skillBreathLores.addAll(breathSkill.getProgressLores(ChatColor.DARK_BLUE));
		Components.lore(skillBreathItemMeta, skillBreathLores);
		skillBreathItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillBreathItemStack.setItemMeta(skillBreathItemMeta);
		menu.setItem(15, skillBreathItemStack);
		
		AbstractPassiveSkill nutritionSkill = playerData.getSkills().getCachedPassive(PassiveNutrition.PASSIVE_NAME);
		ItemStack skillNutritionItemStack = new ItemStack(Material.APPLE, 1);
		ItemMeta skillNutritionItemMeta = skillNutritionItemStack.getItemMeta();
		Components.displayName(skillNutritionItemMeta, ChatColor.DARK_BLUE + "Nutrition (Passive)");
		List<String> skillNutritionLores = new ArrayList<String>();
		skillNutritionLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.BLUE + nutritionSkill.getLevel());
		skillNutritionLores.add("");
		skillNutritionLores.add(ChatColor.GRAY + "Consuming lots of food items");
		skillNutritionLores.add(ChatColor.GRAY + "will increase your skill and slowly");
		skillNutritionLores.add(ChatColor.GRAY + "improve your maximum health.");
		skillNutritionLores.add("");
		skillNutritionLores.add(ChatColor.WHITE + "Additional HP: " + ChatColor.DARK_BLUE
				+ (nutritionSkill.getLevel() * 2));
		skillNutritionLores.addAll(nutritionSkill.getProgressLores(ChatColor.DARK_BLUE));
		Components.lore(skillNutritionItemMeta, skillNutritionLores);
		skillNutritionItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillNutritionItemStack.setItemMeta(skillNutritionItemMeta);
		menu.setItem(16, skillNutritionItemStack);
		
		AbstractPassiveSkill weightSkill = playerData.getSkills().getCachedPassive(PassiveWeight.PASSIVE_NAME);
		ItemStack skillWeightItemStack = new ItemStack(Material.CHAIN, 1);
		ItemMeta skillWeightItemMeta = skillWeightItemStack.getItemMeta();
		Components.displayName(skillWeightItemMeta, ChatColor.DARK_BLUE + "Weight (Passive)");
		List<String> skillWeightLores = new ArrayList<String>();
		skillWeightLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.BLUE + weightSkill.getLevel());
		skillWeightLores.add("");
		skillWeightLores.add(ChatColor.GRAY + "Collecting materials and quest items");
		skillWeightLores.add(ChatColor.GRAY + "will increase your skill and slowly");
		skillWeightLores.add(ChatColor.GRAY + "improve your backpack size.");
		skillWeightLores.add("");
		skillWeightLores.add(ChatColor.WHITE + "Backpack Slots: " + ChatColor.DARK_BLUE
				+ (weightSkill.getLevel() + Backpack.BASE_SIZE));
		skillWeightLores.addAll(weightSkill.getProgressLores(ChatColor.DARK_BLUE));
		Components.lore(skillWeightItemMeta, skillWeightLores);
		skillWeightItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		skillWeightItemStack.setItemMeta(skillWeightItemMeta);
		menu.setItem(17, skillWeightItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The player whose skills should be shown.
	 */
	private Player player;
	
	/**
	 * The subclasses of the player.
	 */
	private List<WauzPlayerSubclass> subclasses;
	
	/**
	 * Creates an empty skill menu.
	 */
	public SkillMenu() {
		
	}
	
	/**
	 * Creates a new skill menu for the given player.
	 * 
	 * @param player The player whose skills should be shown.
	 */
	public SkillMenu(Player player) {
		this.player = player;
		this.subclasses = WauzPlayerClassPool.getClass(player).getSubclasses();
	}

	/**
	 * @return The number of subclasses, of the player's class.
	 */
	public int getSubclassCount() {
		return subclasses.size();
	}
	
	/**
	 * Creates an item stack, showing information about the given mastery.
	 * 
	 * @param mastery The number of the mastery.
	 * 
	 * @return The created item stack.
	 */
	public ItemStack getMasteryItemStack(int mastery) {
		WauzPlayerSubclass subclass = subclasses.get(mastery - 1);
		int spent = PlayerSkillConfigurator.getMasteryStatpoints(player, mastery);
		ItemStack masteryItemStack;
		if(spent > 0) {
			masteryItemStack = subclass.getSubclassItemStack();
			masteryItemStack.setAmount(spent);
		}
		else {
			masteryItemStack = GenericIconHeads.getUnknownItem();
		}
		
		ItemMeta masteryItemMeta = masteryItemStack.getItemMeta();
		Components.displayName(masteryItemMeta, ChatColor.DARK_AQUA + "Mastery: " + subclass.getSubclassName());
		List<String> masteryLores = new ArrayList<String>();
		masteryLores.add(ChatColor.WHITE + "Spent Points: " + ChatColor.GREEN + spent);
		masteryLores.add("");
		masteryLores.add(ChatColor.GRAY + "Unlocks new Skills every few Points.");
		masteryLores.add(subclass.getSublassColor() + subclass.getSublassDescription());
		masteryLores.add("");
		Learnable learnable = subclass.getNextLearnable(spent);
		if(learnable != null) {
			masteryLores.add(ChatColor.WHITE + "Needed Points: " + learnable.getLevel());
			masteryLores.add(UnicodeUtils.createProgressBar(spent, learnable.getLevel(), 50, ChatColor.DARK_AQUA));
			masteryLores.add(ChatColor.WHITE + "Unlocks Skill: " + learnable.getSkill().getSkillId());
		}
		else {
			masteryLores.add(ChatColor.WHITE + "ALL SKILLS UNLOCKED");
		}
		Components.lore(masteryItemMeta, masteryLores);
		masteryItemStack.setItemMeta(masteryItemMeta);
		return masteryItemStack;
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and adds a point to the selected skill, if any statpoints are left.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerSkillConfigurator#getTotalStatpoints(Player)
	 * @see PlayerSkillConfigurator#getSpentStatpoints(Player)
	 * @see SkillAssignMenu#open(Player)
	 * 
	 * @see PlayerSkillConfigurator#increaseHealth(Player)
	 * @see PlayerSkillConfigurator#increaseTrading(Player)
	 * @see PlayerSkillConfigurator#increaseLuck(Player)
	 * @see PlayerSkillConfigurator#increaseMana(Player)
	 * @see PlayerSkillConfigurator#increaseStrength(Player)
	 * @see PlayerSkillConfigurator#increaseAgility(Player)
	 * @see SkillMenu#tryToIncreaseMastery(ItemStack, int)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		
		if(clicked == null || !clicked.getType().equals(Material.PLAYER_HEAD)) {
			return;
		}
		Integer total = PlayerSkillConfigurator.getTotalStatpoints(player);
		Integer spent = PlayerSkillConfigurator.getSpentStatpoints(player);
		Integer pts = total - spent;

		try {
			if(HeadUtils.isHeadMenuItem(clicked, "Assign Abilities")) {
				SkillAssignMenu.open(player);
			}
			else if(pts < 1) {
				player.sendMessage(ChatColor.RED + "You don't have Skillpoints left!");
				player.closeInventory();
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Health")) {
				PlayerSkillConfigurator.increaseHealth(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Trading")) {
				PlayerSkillConfigurator.increaseTrading(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Luck")) {
				PlayerSkillConfigurator.increaseLuck(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Magic")) {
				PlayerSkillConfigurator.increaseMana(player);
				SkillMenu.open(player);
			}	
			else if(HeadUtils.isHeadMenuItem(clicked, "Strength")) {
				PlayerSkillConfigurator.increaseStrength(player);
				SkillMenu.open(player);
			}
			else if(HeadUtils.isHeadMenuItem(clicked, "Agility")) {
				PlayerSkillConfigurator.increaseAgility(player);
				SkillMenu.open(player);
			}
			else {
				tryToIncreaseMastery(clicked, event.getRawSlot());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Increases the level of a valid mastery, if not maxed already.
	 * 
	 * @param clicked The clicked mastery item stack.
	 * @param slot The inventory slot of the mastery item stack.
	 * 
	 * @see PlayerSkillConfigurator#increaseMastery(Player, int)
	 */
	private void tryToIncreaseMastery(ItemStack clicked, int slot) {
		if(slot < 9 || slot > 13) {
			return;
		}
		
		if(ItemUtils.doesLoreContain(clicked, "ALL SKILLS UNLOCKED")) {
			player.sendMessage(ChatColor.RED + "This Mastery has already reached max level!");
		}
		else {
			PlayerSkillConfigurator.increaseMastery(player, slot - 8);
			SkillMenu.open(player);
		}
	}
	
}
