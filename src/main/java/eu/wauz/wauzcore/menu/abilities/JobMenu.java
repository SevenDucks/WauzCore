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
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkillPool;
import eu.wauz.wauzcore.skills.passive.PassiveBreeding;
import eu.wauz.wauzcore.skills.passive.PassiveHerbalism;
import eu.wauz.wauzcore.skills.passive.PassiveMining;
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
		List<String> skillMiningLores = new ArrayList<String>();
		skillMiningLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + miningSkill.getLevel());
		skillMiningLores.add("");
		skillMiningLores.add(ChatColor.GRAY + "Mine Ores with Pickaxes");
		skillMiningLores.add(ChatColor.GRAY + "to unlock higher tiered Tools.");
		skillMiningLores.add(ChatColor.GRAY + "This Job has no Menu.");
		skillMiningLores.add("");
		skillMiningLores.add(ChatColor.WHITE + "Highest Pickaxe Tier: " + ChatColor.YELLOW
				+ (Math.min(6, (int) ((miningSkill.getLevel() / 5) + 1))));
		skillMiningLores.addAll(miningSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillMiningItemMeta, skillMiningLores);
		skillMiningItemStack.setItemMeta(skillMiningItemMeta);
		menu.setItem(1, skillMiningItemStack);
		
		AbstractPassiveSkill herbalismSkill = playerData.getSkills().getCachedPassive(PassiveHerbalism.PASSIVE_NAME);
		ItemStack skillHerbalismItemStack = SkillIconHeads.getHerbalismItem();
		ItemMeta skillHerbalismItemMeta = skillHerbalismItemStack.getItemMeta();
		Components.displayName(skillHerbalismItemMeta, ChatColor.YELLOW + "Herbalism");
		List<String> skillHerbalismLores = new ArrayList<String>();
		skillHerbalismLores.add(ChatColor.WHITE + "Current Level: " + ChatColor.GOLD + herbalismSkill.getLevel());
		skillHerbalismLores.add("");
		skillHerbalismLores.add(ChatColor.GRAY + "Gather Herbs with Spades");
		skillHerbalismLores.add(ChatColor.GRAY + "to unlock higher tiered Tools.");
		skillHerbalismLores.add(ChatColor.GRAY + "This Job has no Menu.");
		skillHerbalismLores.add("");
		skillHerbalismLores.add(ChatColor.WHITE + "Highest Spade Tier: " + ChatColor.YELLOW
				+ (Math.min(6, (int) ((herbalismSkill.getLevel() / 5) + 1))));
		skillHerbalismLores.addAll(herbalismSkill.getProgressLores(ChatColor.YELLOW));
		Components.lore(skillHerbalismItemMeta, skillHerbalismLores);
		skillHerbalismItemStack.setItemMeta(skillHerbalismItemMeta);
		menu.setItem(2, skillHerbalismItemStack);
		
		MenuUtils.setComingSoon(menu, "Fishing", 3);
		
		AbstractPassiveSkill breedingSkill = playerData.getSkills().getCachedPassive(PassiveBreeding.PASSIVE_NAME);
		ItemStack skillBreedingItemStack = SkillIconHeads.getTamesItem();
		ItemMeta skillBreedingItemMeta = skillBreedingItemStack.getItemMeta();
		Components.displayName(skillBreedingItemMeta, ChatColor.YELLOW + "Breeding");
		List<String> skillBreedingLores = new ArrayList<String>();
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
		
		MenuUtils.setComingSoon(menu, "Smithing", 5);
		MenuUtils.setComingSoon(menu, "Cooking", 6);
		MenuUtils.setComingSoon(menu, "Inscription", 7);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
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
			
		}
		else if(slot == 6) {
			
		}
		else if(slot == 7) {
			
		}
	}
	
}
