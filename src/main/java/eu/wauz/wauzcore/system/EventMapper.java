package eu.wauz.wauzcore.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.building.shapes.WauzBrushes;
import eu.wauz.wauzcore.items.CustomItem;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.items.util.FoodUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.menu.Backpack;
import eu.wauz.wauzcore.menu.MaterialPouch;
import eu.wauz.wauzcore.menu.ShopMenu;
import eu.wauz.wauzcore.menu.heads.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.players.WauzPlayerSit;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.FoodCalculator;
import eu.wauz.wauzcore.professions.WauzResource;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * Used for mapping interaction events to WauzCore functionalities.
 * 
 * @author Wauzmons
 */
public class EventMapper {
	
	/**
	 * A list of crafting stations to block interactions for, in MMORPG mode.
	 */
	private static List<Material> blockedCraftingStations = Arrays.asList(
			Material.CRAFTING_TABLE, Material.FURNACE, Material.ENCHANTING_TABLE, Material.BREWING_STAND, Material.ANVIL,
			Material.DISPENSER, Material.DROPPER, Material.CAKE, Material.BLAST_FURNACE, Material.CAMPFIRE,
			Material.CARTOGRAPHY_TABLE, Material.COMPOSTER, Material.FLETCHING_TABLE, Material.GRINDSTONE, Material.LOOM,
			Material.SMITHING_TABLE, Material.SMOKER, Material.STONECUTTER, Material.LODESTONE, Material.RESPAWN_ANCHOR,
			Material.LECTERN, Material.CAULDRON, Material.CHEST, Material.TRAPPED_CHEST);
	
	/**
	 * A map of all coustom items for the MMORPG mode, indexed by trigger materials.
	 */
	private static Map<Material, CustomItem> customItemMap = new HashMap<>();
	
	/**
	 * Gets a generic custom item for the MMORPG mode.
	 * 
	 * @param customItemMaterial The material of the item to get.
	 * 
	 * @return The custom item with that material or null.
	 */
	public static CustomItem getCustomItem(Material customItemMaterial) {
		return customItemMap.get(customItemMaterial);
	}
	
	/**
	 * Registers a generic custom item for the MMORPG mode.
	 * 
	 * @param customItem The custom item to register.
	 */
	public static void registerCustomItem(CustomItem customItem) {
		for(Material material : customItem.getCustomItemMaterials()) {
			customItemMap.put(material, customItem);
		}
	}
	
	/**
	 * Called when a player interacts with specific items.
	 * Prevents destruction of farmland and hitting air with a weapon.
	 * Handles opening the main menu, using scrolls, weapons, maps, skills etc.
	 * Redirects block interactions to the corresponding handler.
	 * 
	 * @param event The interact event.
	 * 
	 * @see Cooldown#playerWeaponUse(Player)
	 * @see CustomItem#use(PlayerInteractEvent)
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 * @see FoodCalculator#tryToConsume(Player, ItemStack)
	 * @see WauzPetEgg#tryToSummon(PlayerInteractEvent)
	 * @see EventMapper#handleBlockInteraction(PlayerInteractEvent)
	 */
	public static void handleItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() == Action.PHYSICAL) {
			if(event.getClickedBlock().getType().equals(Material.FARMLAND)) {
				event.setCancelled(true);
			}
			return;
		}
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			WauzBrushes.tryToUse(event);
			return;
		}
		
		if(event.getAction() == Action.LEFT_CLICK_AIR) {
			Cooldown.playerWeaponUse(player);
			event.setCancelled(true);
		}
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		Material itemType = itemStack.getType();
		CustomItem customItem = customItemMap.get(itemType);
		if(customItem != null) {
			customItem.use(event);
		}
		else if(itemType.toString().endsWith("_SPAWN_EGG") && PetEggUtils.isEggItem(itemStack)) {
			WauzPetEgg.tryToSummon(event);
			event.setCancelled(true);
			return;
		}
		else if(event.getAction().toString().contains("RIGHT") && FoodUtils.isFoodItem(itemStack)) {
			FoodCalculator.tryToConsume(player, itemStack);
			event.setCancelled(true);
			return;
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			handleBlockInteraction(event);
		}
	}
	
	/**
	 * Called when a player interacts with specific blocks.
	 * Cancels interactions with crafting stations.
	 * Redirects oak sign clicks to the according handler.
	 * Lets the player mine resource blocks.
	 * Lets the player sit on stairs.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzResource#tryToInteractWithResource(Player, Block)
	 * @see WauzSigns#interact(Player, Block)
	 * @see WauzPlayerSit#sit(Player, Block)
	 */
	public static void handleBlockInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Material blockType = block.getType();
		Material itemType = player.getEquipment().getItemInMainHand().getType();
		WauzDebugger.log(player, "Clicked Block: " + blockType.toString());
		
		if(WauzResource.tryToInteractWithResource(player, block)
				|| blockedCraftingStations.contains(blockType)
				|| blockType.toString().contains("TRAPDOOR")
				|| blockType.toString().endsWith("BED")) {
			event.setCancelled(true);
		}
		else if(blockType.equals(Material.OAK_SIGN) || blockType.equals(Material.OAK_WALL_SIGN)) {
			WauzSigns.interact(player, block);
		}
		else if(blockType.toString().endsWith("STAIRS") && itemType.equals(Material.AIR))  {
			WauzPlayerSit.sit(player, block);
		}
	}
	
	/**
	 * Called when a Survival player interacts with specific items.
	 * Prevents destruction of farmland.
	 * Handles opening the ender chest shop, using maps and pvp protection potions.
	 * Redirects oak sign clicks to the according handler.
	 * 
	 * @param event The interact event.
	 * 
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 * @see DamageCalculator#applyPvPProtection(PlayerInteractEvent)
	 * @see WauzSigns#interact(Player, org.bukkit.block.Block)
	 * @see ShopMenu#open(Player, String, String)
	 */
	public static void handleSurvivalItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction() == Action.PHYSICAL) {
			if(event.getClickedBlock().getType().equals(Material.FARMLAND)) {
				event.setCancelled(true);
			}
			return;
		}
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			WauzBrushes.tryToUse(event);
			return;
		}
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(itemStack != null) {
			Material type = itemStack.getType();
			
			if(type.equals(Material.PAPER)) {
				WauzTeleporter.enterInstanceTeleportManual(event);
			}
			else if(type.equals(Material.EXPERIENCE_BOTTLE)) {
				DamageCalculator.applyPvPProtection(event);
			}
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			WauzDebugger.log(event.getPlayer(), "Clicked Block: " + type.toString());
			
			if(type.equals(Material.ENDER_CHEST)) {
				event.setCancelled(true);
				ShopMenu.open(player, "SurvivalShop", null);
			}
			else if(type.equals(Material.OAK_SIGN) || type.equals(Material.OAK_WALL_SIGN)) {
				WauzSigns.interact(player, event.getClickedBlock());
			}
		}
	}
	
	/**
	 * Called when a player interacts with an inventory menu.
	 * If the inventory has a fitting inventory holder, it tries to select a menu point.
	 * The trashcan and other special MMORPG items are handled here.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzInventoryHolder#selectMenuPoint(InventoryClickEvent)
	 * @see MenuUtils#onSpecialItemInventoryClick(InventoryClickEvent)
	 */
	public static void handleMenuInteraction(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		String inventoryName = ChatColor.stripColor(Components.inventoryTitle(player.getOpenInventory()));
		String inventoryType = event.getInventory().getType().toString();
		WauzDebugger.log(player, "You clicked in Inventory: " + inventoryName + " " + inventoryType);
		
		if(event.getInventory().getHolder() instanceof WauzInventoryHolder) {
			WauzInventoryHolder holder = (WauzInventoryHolder) event.getInventory().getHolder();
			WauzDebugger.log(player, "Selected Option in "
					+ StringUtils.substringAfterLast(holder.getInventoryName(), "."));
			holder.selectMenuPoint(event);
		}
		if(WauzMode.isMMORPG(player)) {
			ItemStack clicked = event.getCurrentItem();
			if(ItemUtils.isNotAir(clicked)) {
				if(HeadUtils.isHeadMenuItem(clicked, "Trashcan") && ItemUtils.isNotAir(player.getItemOnCursor())) {
					player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
					player.setItemOnCursor(null);
				}
				else if(HeadUtils.isHeadMenuItem(clicked, "Materials")) {
					MaterialPouch.openDelayed(player);
				}
				else if(HeadUtils.isHeadMenuItem(clicked, "Backpack")) {
					Backpack.openDelayed(player);
				}
			}
			MenuUtils.onSpecialItemInventoryClick(event);
		}
		else if(WauzMode.isArcade(player)) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Called when a player closes an inventory menu.
	 * If the inventory has a fitting inventory holder, it tries to properly destroy the menu contents.
	 * 
	 * @param event The inventory close event.
	 * 
	 * @see WauzInventoryHolder#destroyInventory(InventoryCloseEvent)
	 */
	public static void handleMenuClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}
		String inventoryName = ChatColor.stripColor(Components.inventoryTitle(player.getOpenInventory()));
		String inventoryType = event.getInventory().getType().toString();
		WauzDebugger.log(player, "You closed the Inventory: " + inventoryName + " " + inventoryType);
		
		if(event.getInventory().getHolder() instanceof WauzInventoryHolder) {
			WauzInventoryHolder holder = (WauzInventoryHolder) event.getInventory().getHolder();
			holder.destroyInventory(event);
		}
	}
	
}
