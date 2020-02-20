package eu.wauz.wauzcore.system;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.WauzScrolls;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.items.weapons.CustomWeaponBow;
import eu.wauz.wauzcore.items.weapons.CustomWeaponGlider;
import eu.wauz.wauzcore.items.weapons.CustomWeaponLance;
import eu.wauz.wauzcore.items.weapons.CustomWeaponShield;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.QuestBuilder;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.WauzModeMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.FoodCalculator;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

/**
 * Used for mapping interaction events to WauzCore functionalities.
 * 
 * @author Wauzmons
 */
public class EventMapper {
	
	/**
	 * Called when a player interacts with an entity.
	 * Only works after a certain cooldown.
	 * Cancels the sit command for pets.
	 * TODO: Refactor after npc update.
	 * 
	 * @param event The received PlayerInteractEvent.
	 * 
	 * @see Cooldown#playerEntityInteraction(Player)
	 * @see ShopBuilder#open(Player, String)
	 * @see QuestBuilder#accept(Player, String)
	 * @see WauzPlayerEventHomeChange
	 * @see WauzModeMenu#selectMode(Player, String)
	 */
	public static void handleEntityInteraction(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		if(!Cooldown.playerEntityInteraction(player)) {
			return;
		}
			
		String display = ChatColor.stripColor(event.getRightClicked().getCustomName());
		String[] name = display.split(" ");
		
		WauzDebugger.log(player, "Clicked NPC: " + display);
		
		if(player.equals(PetOverviewMenu.getOwner(entity)) && entity instanceof Wolf) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
	            public void run() {
	            	try {
	            		((Wolf) entity).setSitting(false);
	        			WauzDebugger.log(player, "Canceled Pet Sit");
	            	}
	            	catch (NullPointerException e) {
	            		WauzDebugger.catchException(getClass(), e);
	            	}
	            }
			}, 10);
			return;
		}
	}
	
	/**
	 * Called when a player interacts with specific items.
	 * Prevents destruction of farmland and hitting air with a weapon.
	 * Handles opening the main menu, using scrolls, weapons, maps, skills and food.
	 * Also cancels interactions with crafting stations.
	 * Redirects oak sign clicks to the according handler.
	 * 
	 * @param event The received PlayerInteractEvent.
	 * 
	 * @see Cooldown#playerWeaponUse(Player)
	 * @see WauzMenu#open(Player)
	 * @see WauzScrolls#onScrollItemInteract(PlayerInteractEvent)
	 * @see CustomWeaponBow#use(PlayerInteractEvent)
	 * @see CustomWeaponGlider#use(PlayerInteractEvent)
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 * @see WauzPlayerSkillExecutor#tryToUseSkill(Player, ItemStack)
	 * @see FoodCalculator#tryToConsume(Player, ItemStack)
	 * @see WauzSigns#interact(Player, org.bukkit.block.Block)
	 */
	public static void handleItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if(event.getAction() == Action.LEFT_CLICK_AIR) {
			Cooldown.playerWeaponUse(player);
			event.setCancelled(true);
		}
		
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.FARMLAND)) {
			event.setCancelled(true);
		}
		
		else if(itemStack != null) {
			Material type = itemStack.getType();
			
			if(type.equals(Material.NETHER_STAR)) {
				WauzMenu.open(player);
			}
			else if(type.equals(Material.NAME_TAG)) {
				WauzScrolls.onScrollItemInteract(event);
			}
			else if(type.equals(Material.BOW)) {
				CustomWeaponBow.use(event);
			}
			else if(type.equals(Material.TRIDENT)) {
				CustomWeaponLance.use(event);
			}
			else if(type.equals(Material.SHIELD)) {
				CustomWeaponShield.use(event);
			}
			else if(type.equals(Material.FEATHER)) {
				CustomWeaponGlider.use(event);
			}
			else if(type.equals(Material.PAPER)) {
				WauzTeleporter.enterInstanceTeleportManual(event);
			}
			else if(event.getAction().toString().contains("RIGHT")) {
				WauzPlayerSkillExecutor.tryToUseSkill(event.getPlayer(), itemStack);
				FoodCalculator.tryToConsume(event.getPlayer(), itemStack);
			}
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			WauzDebugger.log(event.getPlayer(), "Clicked Block: " + type.toString());
			
			if(type.equals(Material.CRAFTING_TABLE)
					|| type.equals(Material.FURNACE)
					|| type.equals(Material.ENCHANTING_TABLE)
					|| type.equals(Material.BREWING_STAND)
					|| type.equals(Material.ANVIL)
					|| type.equals(Material.DISPENSER)
					|| type.equals(Material.DROPPER)
					|| type.equals(Material.CAKE)
					
					|| type.equals(Material.BLAST_FURNACE)
					|| type.equals(Material.CAMPFIRE)
					|| type.equals(Material.CARTOGRAPHY_TABLE)
					|| type.equals(Material.COMPOSTER)
					|| type.equals(Material.FLETCHING_TABLE)
					|| type.equals(Material.GRINDSTONE)
					|| type.equals(Material.LOOM)
					|| type.equals(Material.SMITHING_TABLE)
					|| type.equals(Material.SMOKER)
					|| type.equals(Material.STONECUTTER)) {
				event.setCancelled(true);
			}
			
			else if(type.equals(Material.OAK_SIGN) || type.equals(Material.OAK_WALL_SIGN)) {
				WauzSigns.interact(player, event.getClickedBlock());
			}
		}
	}
	
	/**
	 * Called when a Survival player interacts with specific items.
	 * Prevents destruction of farmland.
	 * Handles opening the ender chest shop, using maps and pvp protection potions.
	 * Redirects oak sign clicks to the according handler.
	 * 
	 * @param event The received PlayerInteractEvent.
	 * 
	 * @see WauzTeleporter#enterInstanceTeleportManual(PlayerInteractEvent)
	 * @see DamageCalculator#increasePvPProtection(PlayerInteractEvent)
	 * @see WauzSigns#interact(Player, org.bukkit.block.Block)
	 * @see ShopBuilder#open(Player, String)
	 */
	public static void handleSurvivalItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.FARMLAND)) {
			event.setCancelled(true);
		}
		
		else if(itemStack != null) {
			Material type = itemStack.getType();
			
			if(type.equals(Material.PAPER)) {
				WauzTeleporter.enterInstanceTeleportManual(event);
			}
			else if(type.equals(Material.EXPERIENCE_BOTTLE)) {
				DamageCalculator.increasePvPProtection(event);
			}
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			WauzDebugger.log(event.getPlayer(), "Clicked Block: " + type.toString());
			
			
			if(type.equals(Material.ENDER_CHEST)) {
				event.setCancelled(true);
				ShopBuilder.open(player, "the Wild");
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
	 * @param event The received InventoryClickEvent.
	 * 
	 * @see WauzInventoryHolder#selectMenuPoint(InventoryClickEvent)
	 * @see MenuUtils#onSpecialItemInventoryClick(InventoryClickEvent)
	 */
	public static void handleMenuInteraction(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String inventoryName = ChatColor.stripColor(player.getOpenInventory().getTitle());
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
			if(ItemUtils.isSpecificItem(clicked, "Trashcan") && ItemUtils.isNotAir(player.getItemOnCursor())) {
				player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
				player.setItemOnCursor(null);
			}
			MenuUtils.onSpecialItemInventoryClick(event);
		}
	}
	
}
