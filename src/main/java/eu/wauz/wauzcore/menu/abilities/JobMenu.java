package eu.wauz.wauzcore.menu.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.collection.BreedingMenu;
import eu.wauz.wauzcore.menu.heads.SkillIconHeads;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerDataSectionSkills;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingItem;
import eu.wauz.wauzcore.professions.crafting.WauzCraftingRecipes;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkillPool;
import eu.wauz.wauzcore.skills.passive.PassiveBreeding;
import eu.wauz.wauzcore.skills.passive.PassiveCooking;
import eu.wauz.wauzcore.skills.passive.PassiveHerbalism;
import eu.wauz.wauzcore.skills.passive.PassiveInscription;
import eu.wauz.wauzcore.skills.passive.PassiveMining;
import eu.wauz.wauzcore.skills.passive.PassiveSmithing;
import eu.wauz.wauzcore.system.annotations.PublicMenu;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the abilities menu, that is used for overviewing jobs.
 * 
 * @author Wauzmons
 */
@PublicMenu
public class JobMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "jobs";
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
		JobMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * Lists all job  skills, and shows options to open their sub menus.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzPlayerDataSectionSkills#getCachedPassive(String)
	 * @see JobMenu#getRecipeCountString(List, int)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player) {
		String menuTitle = ChatColor.BLACK + "" + ChatColor.BOLD + "Job Skills";
		Inventory menu = Components.inventory(new JobMenu(), menuTitle, 9);
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		AbstractPassiveSkill miningSkill = playerData.getSkills().getCachedPassive(PassiveMining.PASSIVE_NAME);
		ItemStack skillMiningItemStack = SkillIconHeads.getMiningItem();
		ItemMeta skillMiningItemMeta = skillMiningItemStack.getItemMeta();
		Components.displayName(skillMiningItemMeta, ChatColor.YELLOW + "Mining");
		List<String> skillMiningLores = new ArrayList<>();
		skillMiningLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + miningSkill.getLevel());
		skillMiningLores.add("");
		skillMiningLores.add(ChatColor.GRAY + "Mine Ores with Pickaxes");
		skillMiningLores.add(ChatColor.GRAY + "to unlock higher tiered Tools.");
		skillMiningLores.add(ChatColor.GRAY + "This Job has no Menu.");
		skillMiningLores.add("");
		skillMiningLores.add(ChatColor.WHITE + "Highest Pickaxe Tier: " + ChatColor.YELLOW
				+ (Math.min(6, (int) ((miningSkill.getLevel() / 5) + 1))) + "/ 6");
		skillMiningLores.addAll(miningSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillMiningItemMeta, skillMiningLores);
		skillMiningItemStack.setItemMeta(skillMiningItemMeta);
		menu.setItem(1, skillMiningItemStack);
		
		AbstractPassiveSkill herbalismSkill = playerData.getSkills().getCachedPassive(PassiveHerbalism.PASSIVE_NAME);
		ItemStack skillHerbalismItemStack = SkillIconHeads.getHerbalismItem();
		ItemMeta skillHerbalismItemMeta = skillHerbalismItemStack.getItemMeta();
		Components.displayName(skillHerbalismItemMeta, ChatColor.YELLOW + "Herbalism");
		List<String> skillHerbalismLores = new ArrayList<>();
		skillHerbalismLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + herbalismSkill.getLevel());
		skillHerbalismLores.add("");
		skillHerbalismLores.add(ChatColor.GRAY + "Gather Herbs with Spades");
		skillHerbalismLores.add(ChatColor.GRAY + "to unlock higher tiered Tools.");
		skillHerbalismLores.add(ChatColor.GRAY + "This Job has no Menu.");
		skillHerbalismLores.add("");
		skillHerbalismLores.add(ChatColor.WHITE + "Highest Spade Tier: " + ChatColor.YELLOW
				+ (Math.min(6, (int) ((herbalismSkill.getLevel() / 5) + 1))) + " / 6");
		skillHerbalismLores.addAll(herbalismSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillHerbalismItemMeta, skillHerbalismLores);
		skillHerbalismItemStack.setItemMeta(skillHerbalismItemMeta);
		menu.setItem(2, skillHerbalismItemStack);
		
		MenuUtils.setComingSoon(menu, "Fishing", 3);
		
		AbstractPassiveSkill breedingSkill = playerData.getSkills().getCachedPassive(PassiveBreeding.PASSIVE_NAME);
		ItemStack skillBreedingItemStack = SkillIconHeads.getTamesItem();
		ItemMeta skillBreedingItemMeta = skillBreedingItemStack.getItemMeta();
		Components.displayName(skillBreedingItemMeta, ChatColor.YELLOW + "Breeding");
		List<String> skillBreedingLores = new ArrayList<>();
		skillBreedingLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + breedingSkill.getLevel());
		skillBreedingLores.add("");
		skillBreedingLores.add(ChatColor.GRAY + "Breed your collected Pets");
		skillBreedingLores.add(ChatColor.GRAY + "to obtain stronger Offsprings.");
		skillBreedingLores.add(ChatColor.GRAY + "Click to Open Breeding Menu.");
		skillBreedingLores.add("");
		skillBreedingLores.add(ChatColor.WHITE + "Highest Breedable Rarity: "
				+ ((PassiveBreeding) breedingSkill).getBreedingLevel().getHighestRarityString());
		skillBreedingLores.addAll(breedingSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillBreedingItemMeta, skillBreedingLores);
		skillBreedingItemStack.setItemMeta(skillBreedingItemMeta);
		menu.setItem(4, skillBreedingItemStack);
		
		AbstractPassiveSkill smithingSkill = playerData.getSkills().getCachedPassive(PassiveSmithing.PASSIVE_NAME);
		ItemStack skillSmithingItemStack = SkillIconHeads.getSmithingItem();
		ItemMeta skillSmithingItemMeta = skillSmithingItemStack.getItemMeta();
		Components.displayName(skillSmithingItemMeta, ChatColor.YELLOW + "Smithing");
		List<String> skillSmithingLores = new ArrayList<>();
		skillSmithingLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + smithingSkill.getLevel());
		skillSmithingLores.add("");
		skillSmithingLores.add(ChatColor.GRAY + "Craft Equipment and Gems");
		skillSmithingLores.add(ChatColor.GRAY + "to unlock more Crafting Recipes.");
		skillSmithingLores.add(ChatColor.GRAY + "Click to Open Crafting Menu.");
		skillSmithingLores.add("");
		skillSmithingLores.add(ChatColor.WHITE + "Unlocked Smithing Recipes: " + ChatColor.YELLOW
				+ getRecipeCountString(WauzCraftingRecipes.getSmithingRecipes(), smithingSkill.getLevel()));
		skillSmithingLores.addAll(smithingSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillSmithingItemMeta, skillSmithingLores);
		skillSmithingItemStack.setItemMeta(skillSmithingItemMeta);
		menu.setItem(5, skillSmithingItemStack);
		
		AbstractPassiveSkill cookingSkill = playerData.getSkills().getCachedPassive(PassiveCooking.PASSIVE_NAME);
		ItemStack skillCookingItemStack = SkillIconHeads.getCookingItem();
		ItemMeta skillCookingItemMeta = skillCookingItemStack.getItemMeta();
		Components.displayName(skillCookingItemMeta, ChatColor.YELLOW + "Cooking");
		List<String> skillCookingLores = new ArrayList<>();
		skillCookingLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + cookingSkill.getLevel());
		skillCookingLores.add("");
		skillCookingLores.add(ChatColor.GRAY + "Craft Food and Potions");
		skillCookingLores.add(ChatColor.GRAY + "to unlock more Crafting Recipes.");
		skillCookingLores.add(ChatColor.GRAY + "Click to Open Crafting Menu.");
		skillCookingLores.add("");
		skillCookingLores.add(ChatColor.WHITE + "Unlocked Cooking Recipes: " + ChatColor.YELLOW
				+ getRecipeCountString(WauzCraftingRecipes.getCookingRecipes(), cookingSkill.getLevel()));
		skillCookingLores.addAll(cookingSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillCookingItemMeta, skillCookingLores);
		skillCookingItemStack.setItemMeta(skillCookingItemMeta);
		menu.setItem(6, skillCookingItemStack);
		
		AbstractPassiveSkill inscriptionSkill = playerData.getSkills().getCachedPassive(PassiveInscription.PASSIVE_NAME);
		ItemStack skillInscriptionItemStack = SkillIconHeads.getInscriptionItem();
		ItemMeta skillInscriptionItemMeta = skillInscriptionItemStack.getItemMeta();
		Components.displayName(skillInscriptionItemMeta, ChatColor.YELLOW + "Inscription");
		List<String> skillInscriptionLores = new ArrayList<>();
		skillInscriptionLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + inscriptionSkill.getLevel());
		skillInscriptionLores.add("");
		skillInscriptionLores.add(ChatColor.GRAY + "Craft Scrolls and Runes");
		skillInscriptionLores.add(ChatColor.GRAY + "to unlock more Crafting Recipes.");
		skillInscriptionLores.add(ChatColor.GRAY + "Click to Open Crafting Menu.");
		skillInscriptionLores.add("");
		skillInscriptionLores.add(ChatColor.WHITE + "Unlocked Inscription Recipes: " + ChatColor.YELLOW
				+ getRecipeCountString(WauzCraftingRecipes.getInscriptionRecipes(), inscriptionSkill.getLevel()));
		skillInscriptionLores.addAll(inscriptionSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillInscriptionItemMeta, skillInscriptionLores);
		skillInscriptionItemStack.setItemMeta(skillInscriptionItemMeta);
		menu.setItem(7, skillInscriptionItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * Generates a string to display how many recipes have been unlocked.
	 * 
	 * @param recipes The list of recipes.
	 * @param level The crafting level.
	 * 
	 * @return The recipe count string.
	 */
	private static String getRecipeCountString(List<WauzCraftingItem> recipes, int level) {
		int unlocked = 0;
		for(WauzCraftingItem recipe : recipes) {
			if(level < recipe.getCraftingItemLevel()) {
				break;
			}
			unlocked++;
		}
		return unlocked + " / " + recipes.size();
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Opens the corresponding sub menu, if a job skill was clicked.
	 * 
	 * @param event The inventory click event.
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();
		
		if(slot == 4) {
			BreedingMenu.open(player, (PassiveBreeding) AbstractPassiveSkillPool.getPassive(player, PassiveBreeding.PASSIVE_NAME));
		}
		else if(slot == 5) {
			AbstractPassiveSkill skill = AbstractPassiveSkillPool.getPassive(player, PassiveSmithing.PASSIVE_NAME);
			CraftingMenu.open(player, skill, WauzCraftingRecipes.getSmithingRecipes(), 0);
		}
		else if(slot == 6) {
			AbstractPassiveSkill skill = AbstractPassiveSkillPool.getPassive(player, PassiveCooking.PASSIVE_NAME);
			CraftingMenu.open(player, skill, WauzCraftingRecipes.getCookingRecipes(), 0);
		}
		else if(slot == 7) {
			AbstractPassiveSkill skill = AbstractPassiveSkillPool.getPassive(player, PassiveInscription.PASSIVE_NAME);
			CraftingMenu.open(player, skill, WauzCraftingRecipes.getInscriptionRecipes(), 0);
		}
	}
	
}
