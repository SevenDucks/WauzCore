package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

/**
 * A pet egg that can be used to interact with pets.
 * 
 * @author Wauzmons
 */
public class WauzPetEgg {
	
	/**
	 * Access to the MythicMobs API.
	 */
	private static BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();
	
	/**
	 * Generates an egg item stack for the given pet.
	 * 
	 * @param pet The pet to get an egg item of.
	 * 
	 * @return The generated egg item stack.
	 */
	public static ItemStack getEggItem(WauzPet pet) {
		WauzPetRarity rarity = pet.getRarity();
		ItemStack itemStack = new ItemStack(rarity.getMaterial());
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(rarity.getColor() + pet.getKey());
		
		List<String> lores = new ArrayList<>();
		lores.add(ChatColor.WHITE + rarity.getName() + " Pet Egg " + ChatColor.LIGHT_PURPLE + rarity.getStars());
		lores.add("");
		lores.add("Category:" + ChatColor.GREEN + " " + pet.getCategory());
		int maxStat = 2 * rarity.getMultiplier();
		for(WauzPetStat stat : WauzPetStat.getAllPetStats()) {
			String description = " " + ChatColor.GRAY + stat.getDescription();
			lores.add(stat.getName() + ":" + ChatColor.GREEN + " " + 0 + " / " + maxStat + description);
		}
		lores.add("");
		lores.add(ChatColor.GRAY + "Use while Sneaking to open Menu");
		lores.add(ChatColor.GRAY + "Right Click to summon Pet");
		
		itemMeta.setLore(lores);	
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	/**
	 * Tries to spawn a pet by interacting with its egg item stack.
	 * If it is a shift interaction the pet menu will be opened.
	 * 
	 * @param event The interact event.
	 */
	public static void tryToSummon(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(player.isSneaking()) {
			// TODO
		}
		else if(event.getAction().toString().contains("RIGHT")) {
			try {
				if(WauzActivePet.tryToUnsummon(player, true)) {
					return;
				}
				String petType = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
				WauzPet pet = WauzPet.getPet(petType);
				MythicMob mob = null;
				if(pet != null) {
					mob = mythicMobs.getMythicMob(pet.getName());
				}
				if(pet == null || mob == null) {
					player.sendMessage(ChatColor.RED + "Your pet is invalid or outdated!");
					return;
				}
				Entity entity = mythicMobs.spawnMythicMob(mob, player.getLocation(), 1);
				WauzActivePet.setOwner(player, entity, itemStack);
				player.sendMessage(ChatColor.GREEN + petType + " was summoned!");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
