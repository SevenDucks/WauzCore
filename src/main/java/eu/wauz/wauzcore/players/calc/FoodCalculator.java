package eu.wauz.wauzcore.players.calc;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectSource;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.skills.passive.PassiveNutrition;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * Used to calculate saturation of custom food items.
 * Also handles effects like health regain based on lore.
 * 
 * @author Wauzmons
 * 
 * @see PlayerItemConsumeEvent
 */
public class FoodCalculator {
	
	/**
	 * Lets a player consume an item, if it is valid and the cooldown is ready.
	 * If it was a succes the item is removed and an eating sound is played.
	 * A new event is called to apply the item effects.
	 * 
	 * @param player The player who tris to consume the item.
	 * @param itemStack The item the player tries to consume.
	 * 
	 * @see PlayerItemConsumeEvent
	 * @see FoodCalculator#applyItemEffects(PlayerItemConsumeEvent)
	 */
	public static void tryToConsume(Player player, ItemStack itemStack) {
		if(!ItemUtils.isFoodItem(itemStack) || !Cooldown.playerFoodConsume(player)) {
			return;
		}
		WauzDebugger.log(player, "Try to consume Food Item");
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String foodId = itemStack.getItemMeta().getDisplayName();
		if(playerData == null || !playerData.getSkills().isFoodReady(foodId)) {
			return;
		}
		int foodCooldown = ItemUtils.getCooldown(itemStack);
		playerData.getSkills().updateFoodCooldown(foodId, foodCooldown);
		
		PlayerItemConsumeEvent event = new PlayerItemConsumeEvent(player, itemStack);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		WauzPlayerDataPool.getPlayer(player).getSkills().getCachedPassive(PassiveNutrition.PASSIVE_NAME).grantExperience(player, 1);
		itemStack.setAmount(itemStack.getAmount() - 1);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
	}
	
	/**
	 * Applies status effects based on a consumed item's lore.
	 * This includes saturation, healing and temperature modifiers.
	 * 
	 * @param event The consumtion event.
	 * 
	 * @see ItemUtils#getSaturation(ItemStack)
	 * @see ItemUtils#getHealing(ItemStack)
	 * @see ItemUtils#getHeatResistance(ItemStack)
	 * @see ItemUtils#getColdResistance(ItemStack)
	 */
	public static void applyItemEffects(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		
		if(!ItemUtils.isFoodItem(itemStack)) {
			event.setCancelled(true);
			return;
		}
		
		if(ItemUtils.containsSaturationModifier(itemStack) ) {
			short saturation = ItemUtils.getSaturation(itemStack);
			player.setFoodLevel(player.getFoodLevel() + saturation);
			player.setSaturation(5);
		}
		
		if(ItemUtils.containsHealingModifier(itemStack)) {
			short healing = ItemUtils.getHealing(itemStack);
			EntityRegainHealthEvent healEvent = new EntityRegainHealthEvent(player, healing, RegainReason.EATING);
			DamageCalculator.heal(healEvent);
		}
		
		if(ItemUtils.containsTemperatureModifier(itemStack)) {
			WauzPlayerEffects effects = WauzPlayerDataPool.getPlayer(player).getStats().getEffects();
			short heatRes = ItemUtils.getHeatResistance(itemStack);
			if(heatRes > 0) {
				effects.addEffect(WauzPlayerEffectType.HEAT_RESISTANCE, WauzPlayerEffectSource.ITEM, heatRes * 60);
			}
			short coldRes = ItemUtils.getColdResistance(itemStack);
			if(coldRes > 0) {
				effects.addEffect(WauzPlayerEffectType.COLD_RESISTANCE, WauzPlayerEffectSource.ITEM, coldRes * 60);
			}
		}
	}

}
