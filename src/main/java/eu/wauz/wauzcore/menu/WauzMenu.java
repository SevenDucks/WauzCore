package eu.wauz.wauzcore.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * The main menu of the game, that will by default show all stats and sub menus of the MMORPG mode.
 * 
 * @author Wauzmons
 * 
 * @see WauzModeMenu
 */
public class WauzMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * If it is opened in the hub, it will be redirected to the mode selection.
	 * Else all sub menus of the MMORPG mode plus a short information is shown.
	 * Here is a quick summary:</br>
	 * Slot 1: A dsiplay of all currencies and reputations.</br>
	 * Slot 2: The travelling menu + current region display.</br>
	 * Slot 3: The guild menu + guild name display.</br>
	 * Slot 4: The group menu + total active groups display.</br>
	 * Slot 5: The achievement menu + achievement count display.</br>
	 * Slot 6: The quest menu + completed quest count display.</br>
	 * Slot 7: The crafting menu + crafting level display.</br>
	 * Slot 8: The pet menu + used pet slots display.</br>
	 * Slot 9: The skill menu + spent skill points display.
	 * 
	 * @param player The player that should view the inventory.
	 * 
	 * @see WauzModeMenu#open(Player)
	 * @see MenuUtils#setCurrencyDisplay(Inventory, Player, int)
	 * @see WauzPlayerData#getRegion()
	 * @see PlayerConfigurator#getGuild(org.bukkit.OfflinePlayer)
	 * @see WauzPlayerGroupPool#getGroups()
	 * @see PlayerConfigurator#getCharacterCompletedAchievements(Player)
	 * @see PlayerConfigurator#getCharacterAchievementProgress(Player, WauzAchievementType)
	 * @see PlayerPassiveSkillConfigurator#getCraftingSkill(Player)
	 * @see PlayerConfigurator#getCharacterUsedPetSlots(Player)
	 * @see PlayerPassiveSkillConfigurator#getSpentStatpoints(Player)
	 */
	public static void open(Player player) {
		if(WauzMode.inHub(player)) {
			WauzModeMenu.open(player);
			return;
		}
		WauzInventoryHolder holder = new WauzInventoryHolder(new WauzMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Wauzland Main Menu");
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}

		MenuUtils.setCurrencyDisplay(menu, player, 0);
		
		ItemStack travellingItemStack = HeadUtils.getPortsItem();
		ItemMeta travellingItemMeta = travellingItemStack.getItemMeta();
		travellingItemMeta.setDisplayName(ChatColor.GOLD + "Travelling");
		List<String> travellingLores = new ArrayList<String>();
		WauzRegion region = playerData.getRegion();
		travellingLores.add(ChatColor.DARK_PURPLE + "Region: " + ChatColor.YELLOW
				+ (region != null ?  region.getTitle() : "(None)"));
		travellingLores.add("");
		travellingLores.add(ChatColor.GRAY + "Teleport yourself to other Locations");
		travellingLores.add(ChatColor.GRAY + "or view the Lore of visited Regions.");
		travellingItemMeta.setLore(travellingLores);
		travellingItemStack.setItemMeta(travellingItemMeta);
		menu.setItem(1, travellingItemStack);
		
		ItemStack guildItemStack = HeadUtils.getGuildItem();
		ItemMeta guildItemMeta = guildItemStack.getItemMeta();
		guildItemMeta.setDisplayName(ChatColor.GOLD + "Guild");
		List<String> guildLores = new ArrayList<String>();
		WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
		guildLores.add(ChatColor.DARK_PURPLE + "Your Guild: " + ChatColor.YELLOW
				+ (playerGuild != null ?  playerGuild.getGuildName() : "(None)"));
		guildLores.add("");
		guildLores.add(ChatColor.GRAY + "Join a Guild to get Bonuses, Tabards");
		guildLores.add(ChatColor.GRAY + "and Guildhall Access for all your Chars!");
		guildItemMeta.setLore(guildLores);
		guildItemStack.setItemMeta(guildItemMeta);
		menu.setItem(2, guildItemStack);
		
		ItemStack groupItemStack = HeadUtils.getGroupItem();
		ItemMeta groupItemMeta = groupItemStack.getItemMeta();
		groupItemMeta.setDisplayName(ChatColor.GOLD + "Group");
		List<String> groupLores = new ArrayList<String>();
		groupLores.add(ChatColor.DARK_PURPLE + "Listed Groups: " + ChatColor.YELLOW
				+ WauzPlayerGroupPool.getGroups().size());
		groupLores.add("");
		groupLores.add(ChatColor.GRAY + "Group up with other Players");
		groupLores.add(ChatColor.GRAY + "and enter their Instances together!");
		groupItemMeta.setLore(groupLores);
		groupItemStack.setItemMeta(groupItemMeta);
		menu.setItem(3, groupItemStack);
		
		ItemStack achievementsItemStack = HeadUtils.getAchievementsItem();
		ItemMeta achievementsItemMeta = achievementsItemStack.getItemMeta();
		achievementsItemMeta.setDisplayName(ChatColor.GOLD + "Achievements");
		List<String> achievementsLores = new ArrayList<String>();
		achievementsLores.add(ChatColor.DARK_PURPLE + "Collected Achievements: " + ChatColor.YELLOW
				+ Formatters.INT.format(PlayerConfigurator.getCharacterCompletedAchievements(player)));
		achievementsLores.add("");
		achievementsLores.add(ChatColor.GRAY + "Collect Achievements in many Categories,");
		achievementsLores.add(ChatColor.GRAY + "to earn a lot of precious Tokens.");
		achievementsItemMeta.setLore(achievementsLores);
		achievementsItemStack.setItemMeta(achievementsItemMeta);
		menu.setItem(4, achievementsItemStack);
		
		ItemStack questlogItemStack = HeadUtils.getQuestItem();
		ItemMeta questlogItemMeta = questlogItemStack.getItemMeta();
		questlogItemMeta.setDisplayName(ChatColor.GOLD + "Questlog");
		List<String> questlogLores = new ArrayList<String>();
		questlogLores.add(ChatColor.DARK_PURPLE + "Completed Quests: " + ChatColor.YELLOW
			+ Formatters.INT.format(PlayerConfigurator.getCharacterAchievementProgress(player, WauzAchievementType.COMPLETE_QUESTS)));
		questlogLores.add("");
		questlogLores.add(ChatColor.GRAY + "View or Cancel your running Quests.");
		questlogLores.add(ChatColor.GRAY + "Use the Questfinder to locate Questgivers.");
		questlogItemMeta.setLore(questlogLores);
		questlogItemStack.setItemMeta(questlogItemMeta);
		menu.setItem(5, questlogItemStack);
		
		ItemStack craftingItemStack = HeadUtils.getCraftItem();
		ItemMeta craftingItemMeta = craftingItemStack.getItemMeta();
		craftingItemMeta.setDisplayName(ChatColor.GOLD + "Crafting");
		List<String> craftingLores = new ArrayList<String>();
		craftingLores.add(ChatColor.DARK_PURPLE + "Crafting Level: " + ChatColor.YELLOW
			+ PlayerPassiveSkillConfigurator.getCraftingSkill(player) + " / " + WauzCore.MAX_CRAFTING_SKILL);
		craftingLores.add("");
		craftingLores.add(ChatColor.GRAY + "Make new Items out of Materials.");
		craftingLores.add(ChatColor.GRAY + "Craft Items to learn new Recipes.");
		craftingItemMeta.setLore(craftingLores);
		craftingItemStack.setItemMeta(craftingItemMeta);
		menu.setItem(6, craftingItemStack);
		
		ItemStack petsItemStack = HeadUtils.getTamesItem();
		ItemMeta petsItemMeta = petsItemStack.getItemMeta();
		petsItemMeta.setDisplayName(ChatColor.GOLD + "Pets");
		List<String> petsLores = new ArrayList<String>();
		petsLores.add(ChatColor.DARK_PURPLE + "Used Pet Slots: " + ChatColor.YELLOW
				+ PlayerConfigurator.getCharacterUsedPetSlots(player) + " / 5");
			petsLores.add("");
		petsLores.add(ChatColor.GRAY + "View and Summon your tamed Pets.");
		petsLores.add(ChatColor.GRAY + "Breed them to get stronger Offsprings.");
		petsItemMeta.setLore(petsLores);
		petsItemStack.setItemMeta(petsItemMeta);
		menu.setItem(7, petsItemStack);
		
		ItemStack skillsItemStack = HeadUtils.getSkillItem();
		ItemMeta skillsItemMeta = skillsItemStack.getItemMeta();
		skillsItemMeta.setDisplayName(ChatColor.GOLD + "Skills");
		List<String> skillsLores = new ArrayList<String>();
		skillsLores.add(ChatColor.DARK_PURPLE + "Spent Skillpoints: " + ChatColor.YELLOW
				+ PlayerPassiveSkillConfigurator.getSpentStatpoints(player) + " / "
				+ PlayerPassiveSkillConfigurator.getTotalStatpoints(player));
		skillsLores.add("");
		skillsLores.add(ChatColor.GRAY + "Spend Points to improve your Stats.");
		skillsLores.add(ChatColor.GRAY + "You gain 2 Points per Level-Up!");
		skillsItemMeta.setLore(skillsLores);
		if(PlayerPassiveSkillConfigurator.getUnusedStatpoints(player) > 0) {
			WauzDebugger.log(player, "Detected Unused Skillpoints");
			skillsItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
			skillsItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		skillsItemStack.setItemMeta(skillsItemMeta);
		menu.setItem(8, skillsItemStack);
		
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If a sub menu was clicked, it will be opened.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see TravellingMenu#open(Player)
	 * @see GuildOverviewMenu#open(Player)
	 * @see GroupMenu#open(Player)
	 * @see AchievementsMenu#open(Player)
	 * @see QuestBuilder#open(Player)
	 * @see CraftingMenu#open(Player)
	 * @see PetOverviewMenu#open(Player, int)
	 * @see SkillMenu#open(Player)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Travelling")) {
			TravellingMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Guild")) {
			GuildOverviewMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Group")) {
			GroupMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Achievements")) {
			AchievementsMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Questlog")) {
			QuestBuilder.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Crafting")) {
			CraftingMenu.open(player);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Pets")) {
			PetOverviewMenu.open(player, -1);
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Skills")) {
			SkillMenu.open(player);
		}
	}

}
