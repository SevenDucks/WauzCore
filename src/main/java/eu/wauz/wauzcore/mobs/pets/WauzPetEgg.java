package eu.wauz.wauzcore.mobs.pets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.items.WauzEquipment;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.system.nms.NmsEntityHorseMount;
import eu.wauz.wauzcore.system.util.Components;
import eu.wauz.wauzcore.system.util.MythicUtils;
import eu.wauz.wauzcore.system.util.WauzDateUtils;

/**
 * A pet egg that can be used to interact with pets.
 * 
 * @author Wauzmons
 */
public class WauzPetEgg {
	
	/**
	 * Generates an egg item stack for the given pet.
	 * 
	 * @param owner The owner of the pet.
	 * @param pet The pet to get an egg item of.
	 * @param ability The ability of the pet. Can be null.
	 * @param hatchTime The timestamp when the pet can hatch.
	 * 
	 * @return The generated egg item stack.
	 */
	public static ItemStack getEggItem(Player owner, WauzPet pet, WauzPetAbility ability, long hatchTime) {
		WauzPetRarity rarity = pet.getRarity();
		boolean isHorse = pet.isHorse();
		ItemStack itemStack = new ItemStack(rarity.getMaterial());
		ItemMeta itemMeta = itemStack.getItemMeta();
		Components.displayName(itemMeta, rarity.getColor() + pet.getKey());
		
		List<String> lores = new ArrayList<>();
		lores.add(ChatColor.WHITE + rarity.getName() + " Pet Egg " + ChatColor.LIGHT_PURPLE + rarity.getStars());
		lores.add("");
		lores.add(ChatColor.WHITE + "Type:" + ChatColor.GREEN + " " + pet.getKey());
		lores.add(ChatColor.WHITE + "Category:" + ChatColor.GREEN + " " + pet.getCategory());
		if(ability != null && !isHorse) {
			String description = ChatColor.GRAY + ability.getAbilityDescription();
			lores.add(ChatColor.WHITE + "Ability:" + ChatColor.GREEN + " " + ability.getAbilityName() + " " + description);
		}
		for(WauzPetStat stat : WauzPetStat.getAllPetStats()) {
			int maxStat = stat.getStatGrowth() * rarity.getMultiplier();
			if((isHorse && stat.isHorse()) || stat.getCategories().contains(pet.getCategory())) {
				String description = " " + ChatColor.GRAY + stat.getDescription();
				lores.add(ChatColor.WHITE + stat.getName() + ":" + ChatColor.GREEN + " " + 0 + " / " + maxStat + description);
			}
		}
		lores.add("");
		lores.add(ChatColor.GRAY + "Right Click to (un)summon Pet");
		lores.add(ChatColor.GRAY + "Type /rename to Rename Pet");
		lores.add(ChatColor.GRAY + "Drag Pet Food on Egg to raise Stats");
		lores.add("");
		lores.add(ChatColor.DARK_GRAY + "Owner-ID: " + owner.getUniqueId().toString());
		lores.add(ChatColor.DARK_GRAY + "Hatch Time: " + hatchTime);
		
		Components.lore(itemMeta, lores);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	/**
	 * Tries to spawn a pet by interacting with its egg item stack.
	 * 
	 * @param event The interact event.
	 */
	public static void tryToSummon(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(!event.getAction().toString().contains("RIGHT")) {
			return;
		}
		tryToSummon(player, itemStack);
	}
	
	/**
	 * Tries to spawn a pet by interacting with its egg item stack.
	 * 
	 * @param player The player interacting with the pet.
	 * @param eggItemStack The pet egg item stack that is being interacted with.
	 */
	public static void tryToSummon(Player player, ItemStack eggItemStack) {
		try {
			if(WauzActivePet.tryToUnsummon(player, true)) {
				return;
			}
			String petType = PetEggUtils.getPetType(eggItemStack);
			WauzPet pet = WauzPet.getPet(petType);
			if(pet == null) {
				player.sendMessage(ChatColor.RED + "Your pet is invalid or outdated!");
				return;
			}
			long hatchTime = PetEggUtils.getPetHatchTime(eggItemStack);
			if(hatchTime > System.currentTimeMillis()) {
				String time = WauzDateUtils.formatHoursMins(hatchTime - System.currentTimeMillis() + 60000);
				player.sendMessage(ChatColor.RED + "You have to wait " + time + " before the pet is hatched!");
				return;
			}
			
			String petName = ChatColor.stripColor(Components.displayName(eggItemStack.getItemMeta()));
			if(pet.isHorse()) {
				Horse horse = NmsEntityHorseMount.create(player.getLocation(), pet.getHorseColor(), petType);
				Components.customName(horse, ChatColor.GREEN + petName);
				WauzActivePet.setOwner(player, horse, pet, eggItemStack);
				int petSpd = WauzActivePet.getPetStat(player, WauzPetStat.getPetStat("Speed"));
				horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2 + (float) petSpd * 0.003f);
				horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
				horse.setHealth(20);
				horse.getInventory().setSaddle(WauzEquipment.getCosmeticItem(Material.SADDLE));
				horse.addPassenger(player);
			}
			else {
				Entity entity = MythicUtils.spawnMob(pet.getName(), player.getLocation(), "Pet Egg");
				if(entity == null) {
					player.sendMessage(ChatColor.RED + "Your pet is invalid or outdated!");
					return;
				}
				Components.customName(entity, ChatColor.GREEN + petName);
				WauzActivePet.setOwner(player, entity, pet, eggItemStack);
			}
			player.sendMessage(ChatColor.GREEN + petName + " was summoned!");
			WauzActivePet.getPet(player).showRandomMessage();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tries to feed a pet by dragging food on its egg item stack.
	 * 
	 * @param event The click event.
	 * 
	 * @return If the pet and food items were valid.
	 */
	public static boolean tryToFeed(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack eggItemStack = event.getCurrentItem();
		ItemStack foodItemStack = event.getCursor();
		if(PetEggUtils.isEggItem(eggItemStack) && PetEggUtils.isFoodItem(foodItemStack)) {
			event.setCancelled(true);
			return tryToFeedInternal(player, eggItemStack, foodItemStack);
		}
		return false;
	}
	
	/**
	 * Tries to feed a pet by dragging food on its egg item stack.
	 * 
	 * @param player The player interacting with the pet.
	 * @param eggItemStack The pet egg item stack that is being interacted with.
	 * @param foodItemStack The food that is being fed to the pet.
	 * 
	 * @return If the pet and food items were valid.
	 */
	public static boolean tryToFeed(Player player, ItemStack eggItemStack, ItemStack foodItemStack) {
		if(PetEggUtils.isEggItem(eggItemStack) && PetEggUtils.isFoodItem(foodItemStack)) {
			return tryToFeedInternal(player, eggItemStack, foodItemStack);
		}
		return false;
	}
	
	/**
	 * Tries to feed a pet by dragging food on its egg item stack.
	 * 
	 * @param player The player interacting with the pet.
	 * @param eggItemStack The pet egg item stack that is being interacted with.
	 * @param foodItemStack The food that is being fed to the pet.
	 * 
	 * @return If the food was consumed by the pet.
	 */
	private static boolean tryToFeedInternal(Player player, ItemStack eggItemStack, ItemStack foodItemStack) {
		if(new WauzPetFoodStats(foodItemStack).tryToApply(eggItemStack)) {
			WauzActivePet.tryToUnsummon(player, true);
			foodItemStack.setAmount(foodItemStack.getAmount() - 1);
			player.playSound(player.getLocation(), Sound.ENTITY_FOX_EAT, 1, 1);
			return true;
		}
		return false;
	}

}
