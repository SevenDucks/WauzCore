package eu.wauz.wauzcore.players.calc;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.FoodUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectSource;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkillPool;
import eu.wauz.wauzcore.skills.passive.PassiveNutrition;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Components;
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
	 * 
	 * @param player The player who tries to consume the item.
	 * @param itemStack The item the player tries to consume.
	 * 
	 * @see Cooldown#playerFoodConsume(Player)
	 * @see FoodCalculator#applyItemEffects(Player, ItemStack)
	 */
	public static void tryToConsume(Player player, ItemStack itemStack) {
		WauzDebugger.log(player, "Try to consume Food Item");
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		String foodId = Components.displayName(itemStack.getItemMeta());
		if(!Cooldown.playerFoodConsume(player) || !playerData.getSkills().isFoodReady(foodId)) {
			return;
		}
		int foodCooldown = FoodUtils.getCooldown(itemStack);
		playerData.getSkills().updateFoodCooldown(foodId, foodCooldown);
		
		applyItemEffects(player, itemStack);
		AbstractPassiveSkillPool.getPassive(player, PassiveNutrition.PASSIVE_NAME).grantExperience(player, 1);
		itemStack.setAmount(itemStack.getAmount() - 1);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
	}
	
	/**
	 * Applies status effects based on a consumed item's lore.
	 * This includes saturation, healing and status modifiers.
	 * 
	 * @param player The player consuming the item.
	 * @param itemStack The item the player consumes.
	 * 
	 * @see FoodUtils
	 */
	public static void applyItemEffects(Player player, ItemStack itemStack) {
		WauzPlayerEffects effects = WauzPlayerDataPool.getPlayer(player).getStats().getEffects();
		
		if(FoodUtils.containsSaturationModifier(itemStack) ) {
			int saturation = FoodUtils.getSaturation(itemStack);
			player.setFoodLevel(player.getFoodLevel() + saturation);
			player.setSaturation(5);
		}
		if(FoodUtils.containsHealingModifier(itemStack)) {
			int healing = FoodUtils.getHealing(itemStack);
			EntityRegainHealthEvent healEvent = new EntityRegainHealthEvent(player, healing, RegainReason.EATING);
			DamageCalculator.heal(healEvent);
		}
		
		if(FoodUtils.containsHeatResistanceModifier(itemStack)) {
			int duration = FoodUtils.getHeatResistance(itemStack);
			effects.addEffect(WauzPlayerEffectType.HEAT_RESISTANCE, WauzPlayerEffectSource.ITEM, duration * 60);
		}
		if(FoodUtils.containsColdResistanceModifier(itemStack)) {
			int duration = FoodUtils.getColdResistance(itemStack);
			effects.addEffect(WauzPlayerEffectType.COLD_RESISTANCE, WauzPlayerEffectSource.ITEM, duration * 60);
		}
		
		if(FoodUtils.containsAttackBoostModifier(itemStack)) {
			Pair<Integer, Integer> boost = FoodUtils.getAttackBoost(itemStack);
			effects.addEffect(WauzPlayerEffectType.ATTACK_BOOST, WauzPlayerEffectSource.ITEM, boost.getLeft() * 60, boost.getRight());
		}
		if(FoodUtils.containsDefenseBoostModifier(itemStack)) {
			Pair<Integer, Integer> boost = FoodUtils.getDefenseBoost(itemStack);
			effects.addEffect(WauzPlayerEffectType.DEFENSE_BOOST, WauzPlayerEffectSource.ITEM, boost.getLeft() * 60, boost.getRight());
		}
		if(FoodUtils.containsExpBoostModifier(itemStack)) {
			Pair<Integer, Integer> boost = FoodUtils.getExpBoost(itemStack);
			effects.addEffect(WauzPlayerEffectType.EXP_BOOST, WauzPlayerEffectSource.ITEM, boost.getLeft() * 60, boost.getRight());
		}
		if(FoodUtils.containsEvasionChanceModifier(itemStack)) {
			Pair<Integer, Integer> boost = FoodUtils.getEvasionChance(itemStack);
			effects.addEffect(WauzPlayerEffectType.EVASION_CHANCE, WauzPlayerEffectSource.ITEM, boost.getLeft() * 60, boost.getRight());
		}
	}

}
