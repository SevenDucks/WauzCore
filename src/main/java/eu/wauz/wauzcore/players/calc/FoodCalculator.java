package eu.wauz.wauzcore.players.calc;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;

public class FoodCalculator {
	
	public static void tryToConsume(Player player, ItemStack itemStack) {
		if(!Cooldown.playerFoodConsume(player) || !ItemUtils.isFoodItem(itemStack))
			return;
		
		WauzDebugger.log(player, "Try to consume Food Item");
		
		PlayerItemConsumeEvent event = new PlayerItemConsumeEvent(player, itemStack);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		itemStack.setAmount(itemStack.getAmount() - 1);
		player.getWorld().playEffect(player.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
	}
	
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
			WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
			long addedHeatRes = ItemUtils.getHeatResistance(itemStack);
			pd.setResistanceHeat(parseEffectTicksToShort(pd.getResistanceHeat(), addedHeatRes));
			long addedColdRes = ItemUtils.getColdResistance(itemStack);
			pd.setResistanceCold(parseEffectTicksToShort(pd.getResistanceCold(), addedColdRes));
		}
	}
	
	public static short parseEffectTicksToShort(long value, long added) {
		value += (added * 12);
		return value > Short.MAX_VALUE ? Short.MAX_VALUE : (short) value;
	}

}
