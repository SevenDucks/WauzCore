package eu.wauz.wauzcore.system.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * An util class for usage of unsave or deprecated methods.
 * 
 * @author Wauzmons
 */
@SuppressWarnings("deprecation")
public class DeprecatedUtils {
	
	/**
	 * Checks if the given player interact event was cancelled.
	 * 
	 * @param event The interact event.
	 * 
	 * @return If it was cancelled.
	 */
	public static boolean isPlayerInteractionCancelled(PlayerInteractEvent event) {
		return event.isCancelled();
	}
	
	/**
	 * Removes the Minecraft damage modifiers from an event.
	 * 
	 * @param event The damage event.
	 */
	public static void removeDamageModifiers(EntityDamageEvent event) {
		List<DamageModifier> damageModifiers = Arrays.asList(DamageModifier.values()).stream()
				.filter(damageModifier -> event.isApplicable(damageModifier))
				.collect(Collectors.toList());
		
		for(int iterator = 0; iterator < damageModifiers.size(); iterator++) {
			event.setDamage(damageModifiers.get(iterator), iterator == 0 ? event.getDamage() : 0);
		}
	}
	
	/**
	 * Adds a texture to a player head item stack, based on a base64 string.
	 * 
	 * @param headItemStack The player head item stack, that should be modified.
	 * @param base64 A base64 string representing the data value of a skin.
	 * @param hashAsId The hash of the base64 string.
	 * 
	 * @return The modified player head item.
	 */
	public static ItemStack addPlayerHeadTexture(ItemStack headItemStack, String base64, UUID hashAsId) {
		return Bukkit.getUnsafe().modifyItemStack(headItemStack,
				"{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
		);
	}

}
