package eu.wauz.wauzcore.building.shapes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * A collection of all available brushes.
 * 
 * @author Wauzmons
 */
public class WauzBrushes {
	
	/**
	 * A map of all brushes, indexed by name.
	 */
	private static Map<String, WauzBrush> brushMap = new HashMap<>();
	
	/**
	 * A map of all instanced brushes, indexed by id.
	 */
	private static Map<String, WauzBrush> brushInstanceMap = new HashMap<>();
	
	/**
	 * Gets the brush with the given name.
	 * 
	 * @param brushName The name of the brush.
	 * 
	 * @return The brush or null, if not found.
	 */
	public static WauzBrush getBrush(String brushName) {
		return brushMap.get(brushName);
	}
	
	/**
	 * Gets a list of all brush names.
	 * 
	 * @return The list of all brush names.
	 */
	public static List<String> getAllBrushNames() {
		return new ArrayList<>(brushMap.keySet());
	}
	
	/**
	 * Registers the given brush.
	 * 
	 * @param brush The brush to register.
	 */
	public static void registerBrush(WauzBrush brush) {
		brushMap.put(brush.getName(), brush);
	}
	
	/**
	 * Registers the given brush as an instance, bound to a brush item stack.
	 * 
	 * @param brush The brush to register.
	 * 
	 * @return The brush item stack.
	 */
	public static ItemStack registerInstancedBrush(WauzBrush brush) {
		brushInstanceMap.put(brush.getUuid(), brush);
		
		ItemStack brushItemStack = new ItemStack(Material.AMETHYST_SHARD);
		ItemMeta brushItemMeta = brushItemStack.getItemMeta();
		Components.displayName(brushItemMeta, ChatColor.LIGHT_PURPLE + "WauzEdit Brush");
		Components.lore(brushItemMeta, brush.getLore());
		brushItemStack.setItemMeta(brushItemMeta);
		return brushItemStack;
	}
	
	/**
	 * Tries to use a specific brush.
	 * 
	 * @param event The event triggering the brush usage.
	 */
	public static void tryToUse(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack brushItemStack = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.hasLore(brushItemStack)) {
			return;
		}
		String uuid = ItemUtils.getStringFromLore(brushItemStack, "Brush UUID", 2);
		if(uuid == null) {
			return;
		}
		WauzBrush brush = brushInstanceMap.get(uuid);
		if(brush == null || !Cooldown.playerBrushUse(player)) {
			return;
		}
		for(Block block : player.getLineOfSight(null, 50)) {
			if(!block.getType().equals(Material.AIR)) {
				brush.paint(block.getLocation());
				event.setCancelled(true);
				return;
			}
		}
	}

}
