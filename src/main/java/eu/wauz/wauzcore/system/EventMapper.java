package eu.wauz.wauzcore.system;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import eu.wauz.wauzcore.items.CustomWeaponBow;
import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.items.dungeon.DungeonItemChickenGlider;
import eu.wauz.wauzcore.items.dungeon.DungeonItemThunderRod;
import eu.wauz.wauzcore.menu.CharacterSlotMenu;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.QuestBuilder;
import eu.wauz.wauzcore.menu.ShopBuilder;
import eu.wauz.wauzcore.menu.WauzDialog;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.calc.FoodCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class EventMapper {
	
	public static void entity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		if(!Cooldown.playerEntityInteraction(player))
			return;
			
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
		
		else if(name[0].contains("(S)")) {
			WauzDebugger.log(player, "Clicked Shop NPC '" + name[1] + "'");
			ShopBuilder.open(player, name[1]);
		}
		
		else if(name[0].contains("(Q)")) {
			WauzDebugger.log(player, "Clicked Quest NPC '" + name[1] + "'");
			QuestBuilder.accept(player, name[1], entity.getLocation());
			WauzPlayerScoreboard.scheduleScoreboard(player);
		}
		
		else if(name[0].contains("(I)")) {
			WauzDebugger.log(player, "Clicked Inn NPC '" + name[1] + "'");
			WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
			playerData.setWauzPlayerEventName("Change Home");
			playerData.setWauzPlayerEvent(new WauzPlayerEventHomeChange(event.getRightClicked(), false));
			WauzDialog.open(player);
		}
		
		else if(name[0].contains("(Play)")) {
			WauzDebugger.log(player, "Clicked Game NPC '" + name[1] + "'");
			if(name[1].equals("MMORPG"))
				CharacterSlotMenu.open(event, WauzMode.MMORPG);
			else if(name[1].equals("Survival"))
				CharacterSlotMenu.open(event, WauzMode.SURVIVAL);
		}
	}
	
	public static void item(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.FARMLAND))
            event.setCancelled(true);
		
		else if(itemStack != null) {
			Material type = itemStack.getType();
			if(type.equals(Material.NETHER_STAR))		WauzMenu.open(player);
			else if(type.equals(Material.NAME_TAG))		MenuUtils.onScrollItemInteract(event);
			else if(type.equals(Material.BOW))			CustomWeaponBow.use(event);
			else if(type.equals(Material.BLAZE_ROD))	DungeonItemThunderRod.use(event);
			else if(type.equals(Material.FEATHER))		DungeonItemChickenGlider.use(event);
			else if(type.equals(Material.PAPER))		WauzTeleporter.enterInstanceTeleportManual(event);
			
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
					|| type.equals(Material.DROPPER))
				event.setCancelled(true);
			
			else if(type.equals(Material.SIGN) || type.equals(Material.WALL_SIGN))
				WauzSigns.interact(player, event.getClickedBlock());
		}
		
		WauzDebugger.log(player, "EventMapper.item() canceled: " + event.isCancelled());
	}
	
	public static void itemSurvival(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(Material.FARMLAND))
            event.setCancelled(true);
		
		else if(itemStack != null) {
			Material type = itemStack.getType();
			if(type.equals(Material.PAPER))						WauzTeleporter.enterInstanceTeleportManual(event);
			else if(type.equals(Material.EXPERIENCE_BOTTLE))	DamageCalculator.increasePvPProtection(event);
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			WauzDebugger.log(event.getPlayer(), "Clicked Block: " + type.toString());
			
			
			if(type.equals(Material.ENDER_CHEST)) {
				event.setCancelled(true);
				ShopBuilder.open(player, "the Wild");
			}
			
			else if(type.equals(Material.SIGN) || type.equals(Material.WALL_SIGN))
				WauzSigns.interact(player, event.getClickedBlock());
		}
	}
	
	public static void menu(InventoryClickEvent event) {
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
				player.playSound(player.getLocation(), Sound.BLOCK_METAL_BREAK, 1, 1);
				player.setItemOnCursor(null);
			}
			MenuUtils.onSpecialItemInventoryClick(event);
		}
	}
	
}
