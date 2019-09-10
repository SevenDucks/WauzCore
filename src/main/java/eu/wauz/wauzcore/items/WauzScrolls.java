package eu.wauz.wauzcore.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.identifiers.WauzIdentifier;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.ShopBuilder;

public class WauzScrolls {
	
	private static List<Material> validScrollMaterials = new ArrayList<Material>(Arrays.asList(
			Material.NAME_TAG, Material.FIREWORK_STAR, Material.REDSTONE));
	
	public static void onScrollItemInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack scroll = player.getEquipment().getItemInMainHand();
		if(!ItemUtils.hasDisplayName(scroll))
			return;
		String scrollName = scroll.getItemMeta().getDisplayName();
		
		if(scrollName.contains("Scroll of Summoning")) {
			PetOverviewMenu.addPet(event);
		}
		
		else if(scrollName.contains("Scroll of Comfort")) {
			new WauzPlayerEventHomeChange(player, scroll).execute(player);
		}
	}
	
	public static void onScrollItemInteract(InventoryClickEvent event, String itemName) {
		Player player = (Player) event.getWhoClicked();
		ItemStack scroll = (player.getItemOnCursor());
		if(!validScrollMaterials.contains(scroll.getType())) {
			return;
		}
		String scrollName = scroll.getItemMeta().getDisplayName();
		
		ItemStack itemStack = event.getCurrentItem();
		boolean isNotScroll = !itemName.contains("Scroll");
		boolean isIdentified = !itemName.contains("Unidentified");
		
		if(isNotScroll && scrollName.contains("Scroll of Wisdom")) {
			if(!isIdentified) {
				WauzIdentifier.identify(event, itemName);
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(isNotScroll && scrollName.contains("Scroll of Fortune")) {
			if(ShopBuilder.sell((Player) player, itemStack, false)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}	
		}
		else if(isNotScroll && scrollName.contains("Scroll of Toughness")) {
			if(ShopBuilder.repair((Player) player, itemStack, false)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(isNotScroll && scrollName.contains("Scroll of Regret")) {
			if(Equipment.clearAllSockets(event)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(scrollName.contains("Rune")) {
			if(isIdentified && Equipment.insertRune(event)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
		else if(scrollName.contains("Skillgem")) {
			if(isIdentified && Equipment.insertSkillgem(event)) {
				scroll.setAmount(scroll.getAmount() - 1);
				event.setCancelled(true);
			}
		}
	}

}
