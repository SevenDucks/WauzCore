package eu.wauz.wauzcore.players.calc;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.mobs.pets.WauzPetStat;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * A calculation of the received damage after a player defense.
 * 
 * @author Wauzmons
 * 
 * @see DamageCalculator
 */
public class DamageCalculatorDefense {
	
	/**
	 * The damage event.
	 */
	private EntityDamageEvent event;
	
	/**
	 * The player who gets damaged.
	 */
	private Player player;
	
	/**
	 * The damage value, blocked off by a shield or similar.
	 */
	private int blockedDamage;
	
	/**
	 * The damage value before any multipliers are applied.
	 */
	private int unmodifiedDamage;
	
	/**
	 * The final calculated damage value.
	 */
	private int damage;
	
	/**
	 * Creates a new calculator defense.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DamageCalculatorDefense#run()
	 */
	public DamageCalculatorDefense(EntityDamageEvent event) {
		this.event = event;
		player = (Player) event.getEntity();
		damage = (int) event.getDamage();
		unmodifiedDamage = damage;
		blockedDamage = 0;
	}
	
	/**
	 * Determines the amount of damage a player takes, with a default minimum of 1.
	 * The player takes no damage, if they evade or have still no damage ticks left.
	 * Automatically subtracts defense and blocked damage form the value.
	 * Also lets damage indicators pop up after the attack,
	 * aswell as reducing armor / shield durability and adding no-damage-ticks, if necessary.
	 * 
	 * @see DamageCalculatorDefense#calculateBlockedDamage()
	 * @see DamageCalculatorDefense#calculateDefense()
	 * @see DamageCalculator#setHealth(Player, int)
	 * @see RageCalculator#generateRage(Player)
	 * @see ValueIndicator#spawnDamageIndicator(Entity, Integer)
	 */
	public void run() {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || player.getNoDamageTicks() != 0 || event.getDamage() == 0 || tryToEvade()) {
			return;
		}
		
		calculateBlockedDamage();
		calculateDefense();
		
		event.setDamage(0);
		if(damage < 1) {
			damage = (blockedDamage > 0) ? 0 : 1;
		}
		int hp = playerData.getStats().getHealth() - damage;
		if(hp < 0) {
			hp = 0;
		}
		DamageCalculator.setHealth(player, hp);
		
		RageCalculator.generateRage(player);
		ValueIndicator.spawnDamageIndicator(player, damage);
		player.setNoDamageTicks(10);
		
		WauzDebugger.log(player, "You took " + damage + " (" + unmodifiedDamage + ") damage!");
		WauzDebugger.log(player, "Cause: " + event.getCause() + " " + event.getFinalDamage());
	}
	
	/**
	 * Lets the player evade all damage by having either invisibility,
	 * defense debug mode enabled or simply high agility.
	 * 
	 * @return If the evade was successful.
	 * 
	 * @see ValueIndicator#spawnEvadedIndicator(Entity)
	 * @see WauzDebugger#toggleDefenseDebugMode(Player)
	 */
	private boolean tryToEvade() {
		WauzPlayerEffects effects = WauzPlayerDataPool.getPlayer(player).getStats().getEffects();
		int effectBonus = effects.getEffectPowerSum(WauzPlayerEffectType.EVASION_CHANCE);
		boolean evaded = Chance.percent(PlayerSkillConfigurator.getAgility(player) + effectBonus);
		if(evaded || player.hasPermission(WauzPermission.DEBUG_DEFENSE.toString())) {
			event.setDamage(0);
			
			ValueIndicator.spawnEvadedIndicator(player);
			player.setNoDamageTicks(10);
			
			WauzDebugger.log(player, "You evaded an attack!");
			return true;
		}
		return false;
	}
	
	/**
	 * If a shield is used, damage is reduced by 60% and the minimum damage is set to 0.
	 * A block sound is played and the shield is damaged.
	 * 
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 */
	private void calculateBlockedDamage() {
		if(player.isBlocking() && event instanceof EntityDamageByEntityEvent) {
			blockedDamage = (int) Math.ceil(damage * 0.60);
			damage = damage - blockedDamage;
			player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 0.75f);
			WauzDebugger.log(player, "Blocked Damage: " + blockedDamage);
			
			ItemStack shieldItemStack = player.getEquipment().getItemInMainHand();
			DurabilityCalculator.damageItem(player, shieldItemStack, false);
		}
	}
	
	/**
	 * Applies bonuses to the armor defense value, based on strength bonus and pet absorption.
	 * Subtracts the resist value from the taken damage and damages the armor.
	 * 
	 * @see EquipmentUtils#getBaseDef(ItemStack)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 * @see PlayerSkillConfigurator#getStrengthFloat(Player)
	 */
	private void calculateDefense() {
		ItemStack armorItemStack = player.getEquipment().getChestplate();
		int defense = EquipmentUtils.getBaseDef(armorItemStack);
		if(defense <= 0 ) {
			return;
		}
		DurabilityCalculator.damageItem(player, armorItemStack, true);
		
		float multiplier = PlayerSkillConfigurator.getStrengthFloat(player);
		int petAbs = WauzActivePet.getPetStat(player, WauzPetStat.getPetStat("Absorption"));
		multiplier += (float) petAbs / 100f;
		
		WauzPlayerEffects effects = WauzPlayerDataPool.getPlayer(player).getStats().getEffects();
		double effectBonus = effects.getEffectPowerSumDecimal(WauzPlayerEffectType.DEFENSE_BOOST);
		multiplier *= (1.0 + effectBonus);
		
		WauzDebugger.log(player, "Defense Multiplier: " + Formatters.DEC.format(multiplier));	
		damage -= (int) ((float) defense * multiplier);
	}

}
