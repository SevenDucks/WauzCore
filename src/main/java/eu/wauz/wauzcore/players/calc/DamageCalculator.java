package eu.wauz.wauzcore.players.calc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPassiveSkillConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.Formatters;
import net.md_5.bungee.api.ChatColor;

/**
 * Used to recalculate how much damage or heal players deal and receive.
 * Also handles stuff like damage boni, reflection, leech and PvP-potions.
 * 
 * Suppresses deprecation of DamageModifier,
 * because there are currently no better alternatives.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData#getHealth()
 */
@SuppressWarnings("deprecation")
public class DamageCalculator {
	
	/**
	 * The chance that a weapon skill increases on weapon usage. One in x.
	 */
	private static int INCREASE_SKILL_CHANCE = 5;
	
	/**
	 * Determines the amount of damage a player dealt.
	 * Determines if it is normal (with boni), fixed (without boni) or magic (multiplied) damage.
	 * Checks level requirements and cooldown on the used weapon,
	 * so the attack can be cancelled and an according message can be shown.
	 * Minecraft damage modifiers are removed and replaced by custom bonuses and (crit) randomizers.
	 * In attack debug mode, the damage output is multiplied by 100.
	 * Also lets damage indicators pop up after the attack,
	 * aswell as reducing weapon durability, if necessary.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DamageCalculator#applyAttackBonus(int, Player, String)
	 * @see DamageCalculator#removeDamageModifiers(EntityDamageEvent)
	 * @see EquipmentUtils#getBaseAtk(ItemStack)
	 * @see EquipmentUtils#getLevelRequirement(ItemStack)
	 * @see ValueIndicator#spawnDamageIndicator(Entity, Integer)
	 * @see ValueIndicator#spawnMissedIndicator(Entity)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 * @see WauzDebugger#toggleAttackDebugMode(Player)
	 */
	public static void attack(EntityDamageByEntityEvent event) {
		Player player = (Player) event.getDamager();
		Entity entity = event.getEntity();
		
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		boolean isFixedDamage = false;
		if(!entity.getMetadata("wzFixedDmg").isEmpty()) {
			isFixedDamage = entity.getMetadata("wzFixedDmg").get(0).asBoolean();
			entity.setMetadata("wzFixedDmg", new FixedMetadataValue(WauzCore.getInstance(), false));
		}
		
		int damage = 1;
		int unmodifiedDamage = (int) event.getDamage();
		
		boolean isAttackDebugMode = player.hasPermission(WauzPermission.DEBUG_ATTACK.toString());
		boolean isMagic = false;
		double magicMultiplier = 1;
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(isFixedDamage) {
			damage = (int) event.getDamage();
		}
		else {
			if((itemStack.getType().equals(Material.AIR)) || !ItemUtils.hasLore(itemStack)) {
				event.setDamage(isAttackDebugMode ? 100: 1);
				removeDamageModifiers(event);
				DurabilityCalculator.damageItem(player, itemStack, false);
				ValueIndicator.spawnDamageIndicator(event.getEntity(), 1);
				return;
			}
			
			int requiredLevel = EquipmentUtils.getLevelRequirement(itemStack);
			WauzDebugger.log(player, "Required Level: " + requiredLevel);
			if(player.getLevel() < requiredLevel) {
				event.setCancelled(true);
				DurabilityCalculator.damageItem(player, itemStack, false);
				player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
				return;
			}
			
			if(!entity.getMetadata("wzMagic").isEmpty()) {
				double wzMagicValue = entity.getMetadata("wzMagic").get(0).asDouble();
				if(wzMagicValue > 0) {
					magicMultiplier = wzMagicValue + EquipmentUtils.getEnhancementSkillDamageMultiplier(itemStack);
				}
				entity.setMetadata("wzMagic", new FixedMetadataValue(WauzCore.getInstance(), 0d));
				WauzDebugger.log(player, "Magic Damage-Multiplier: " + magicMultiplier);
				isMagic = true;
			}
			else if(!Cooldown.playerWeaponUse(player)) {
				WauzDebugger.log(player, "Missed - Weapon Not Ready");
				event.setCancelled(true);
				player.resetCooldown();
				
				ValueIndicator.spawnMissedIndicator(entity);
				return;
			}
			
			damage = EquipmentUtils.getBaseAtk(itemStack);
			unmodifiedDamage = (int) (damage * magicMultiplier);
			damage = applyAttackBonus(unmodifiedDamage, player, itemStack.getType().name());
		}
		
		boolean isCritical = Chance.percent(PlayerPassiveSkillConfigurator.getAgility(player));
		float multiplier = 1;
		if(isCritical) {
			multiplier += 1 + EquipmentUtils.getEnhancementCriticalDamageMultiplier(player.getEquipment().getItemInMainHand());
		}
		else {
			multiplier += Chance.negativePositive(0.15f);
		}
		
		if(isAttackDebugMode) {
			multiplier += 100;
		}
		if(entity.hasMetadata("wzModMassive")) {
			multiplier = 0.2f * multiplier;
		}
		if(entity.hasMetadata("wzModDeflecting")) {
			SkillUtils.throwBackEntity(player, entity.getLocation(), 1.2);
		}
		
		WauzDebugger.log(player, "Randomized Multiplier: " + Formatters.DEC.format(multiplier) + (isCritical ? " CRIT" : ""));
		damage = (int) ((float) damage * (float) multiplier);
		damage = damage < 1 ? 1 : damage;
		event.setDamage(damage);
		removeDamageModifiers(event);
		
		if(!isMagic && !isFixedDamage && !event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK)) {
			DurabilityCalculator.damageItem(player, itemStack, false);
		}
		ValueIndicator.spawnDamageIndicator(event.getEntity(), damage, isCritical);
		
		WauzDebugger.log(player, "You inflicted " + damage + " (" + unmodifiedDamage + ") damage!");
		WauzDebugger.log(player, "Cause: " + event.getCause() + " " + event.getFinalDamage());
	}
	
	/**
	 * Determines the amount of damage a player reflects.
	 * If the player is blocking, the attacker and themselve are thrown back slightly.
	 * 
	 * @param event The damage event.
	 * 
	 * @see EquipmentUtils#getReflectionDamage(ItemStack)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 */
	public static void reflect(EntityDamageByEntityEvent event) {
		Player player = (Player) event.getEntity();
		
		if(event.getDamager() instanceof Damageable) {
			Damageable damageable = (Damageable) event.getDamager();
			
			int reflectionDamage = 0;
			reflectionDamage += EquipmentUtils.getReflectionDamage(player.getEquipment().getItemInMainHand());
			reflectionDamage += EquipmentUtils.getReflectionDamage(player.getEquipment().getChestplate());
			
			if(reflectionDamage > 0) {
				WauzDebugger.log(player, "Reflecting " + reflectionDamage + " damage!");
				SkillUtils.callPlayerFixedDamageEvent(player, damageable, reflectionDamage);
			}
			
			if(player.isBlocking()) {
				SkillUtils.throwBackEntity(player, damageable.getLocation(), 0.8);
				SkillUtils.throwBackEntity(damageable, player.getLocation(), 0.4);
			}
		}
	}
	
	/**
	 * Determines the amount of damage a player takes, with a minimum of 1.
	 * If a shield is used, damage is reduced by 60% and the minimum damage is set to 0.
	 * The player can also evade damage by having either invisibility, defense debug mode enabled or simply high agility.
	 * Also lets damage indicators pop up after the attack,
	 * aswell as reducing armor / shield durability and adding no-damage-ticks, if necessary.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DamageCalculator#applyDefendBonus(int, Player)
	 * @see EquipmentUtils#getBaseDef(ItemStack)
	 * @see ValueIndicator#spawnDamageIndicator(Entity, Integer)
	 * @see ValueIndicator#spawnEvadedIndicator(Entity)
	 * @see EquipmentUtils#getBaseDef(ItemStack)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 * @see WauzDebugger#toggleDefenseDebugMode(Player)
	 */
	public static void defend(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || player.getNoDamageTicks() != 0) {
			return;
		}
		
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)
				|| player.hasPermission(WauzPermission.DEBUG_DEFENSE.toString())
				|| Chance.percent(PlayerPassiveSkillConfigurator.getAgility(player))) {
			
			event.setDamage(0);
			
			ValueIndicator.spawnEvadedIndicator(player);
			player.setNoDamageTicks(10);
			
			WauzDebugger.log(player, "You evaded an attack!");
			return;
		}
		
		int damage = (int) event.getDamage();
		int unmodifiedDamage = damage;
		int blockedDamage = 0;
		
		if(player.isBlocking()) {
			blockedDamage = (int) Math.ceil(damage * 0.60);
			damage = damage - blockedDamage;
			WauzDebugger.log(player, "Blocked Damage: " + blockedDamage);
			
			ItemStack shieldItemStack = player.getEquipment().getItemInMainHand();
			DurabilityCalculator.damageItem(player, shieldItemStack, false);
		}
		
		ItemStack itemStack = player.getEquipment().getChestplate();
		if((itemStack != null) && (!itemStack.getType().equals(Material.AIR)) && (itemStack.getItemMeta().getLore() != null)) {
			int defense = EquipmentUtils.getBaseDef(itemStack);
			if(defense > 0 ) {
				damage = (int) (damage - applyDefendBonus(defense, player));
				DurabilityCalculator.damageItem(player, itemStack, true);
			}
		}
		
		event.setDamage(0);
		if(damage < 1) {
			damage = (blockedDamage >  0) ? 0 : 1;
		}
		int hp = playerData.getHealth() - damage;
		if(hp < 0) {
			hp = 0;
		}
		setHealth(player, hp);
		
		ValueIndicator.spawnDamageIndicator(player, damage);
		player.setNoDamageTicks(10);
		
		WauzDebugger.log(player, "You took " + damage + " (" + unmodifiedDamage + ") damage!");
		WauzDebugger.log(player, "Cause: " + event.getCause() + " " + event.getFinalDamage());
	}
	
	/**
	 * Determines the amount of healing a player receives.
	 * Spawns a heal indicator afterwards.
	 * 
	 * @param event The heal event.
	 * 
	 * @see ValueIndicator#spawnHealIndicator(org.bukkit.Location, int)
	 */
	public static void heal(EntityRegainHealthEvent event) {
		Player player = (Player) event.getEntity();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		int heal = (int) event.getAmount();
		
		event.setAmount(0);
		int hp = playerData.getHealth() + heal;
		if(hp > playerData.getMaxHealth()) hp = playerData.getMaxHealth();
		setHealth(player, hp);
		
		ValueIndicator.spawnHealIndicator(player.getLocation(), heal);
		
		WauzDebugger.log(player, "You restored " + heal + " health!");
	}
	
	/**
	 * Handles health or mana leech, when a player kills an entity.
	 * 
	 * @param event The death event.
	 * 
	 * @see EquipmentUtils#getEnhancementOnKillHP(ItemStack)
	 * @see EquipmentUtils#getEnhancementOnKillMP(ItemStack)
	 */
	public static void kill(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();
		
		int onKillHP = EquipmentUtils.getEnhancementOnKillHP(player.getEquipment().getItemInMainHand());
		if(onKillHP > 0) {
			heal(new EntityRegainHealthEvent(player, onKillHP, RegainReason.CUSTOM));
		}
		
		int onKillMP = EquipmentUtils.getEnhancementOnKillMP(player.getEquipment().getItemInMainHand());
		if(onKillMP > 0) {
			ManaCalculator.regenerateMana(player, onKillMP);
		}
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
	 * Sets the new health of a player.
	 * Handles value overflows and health restore after death.
	 * Updates the action bar afterwards.
	 * 
	 * @param player The player that gets their health set.
	 * @param hp The new amount of health.
	 */
	public static void setHealth(Player player, int hp) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		
		if(hp == 0) {
			player.setHealth(0);
			playerData.setHealth(playerData.getMaxHealth());
			WauzPlayerActionBar.update(player);
			return;
		}
		
		playerData.setHealth(hp);
		hp = (hp * 20) / playerData.getMaxHealth();
		if(hp == 20 && playerData.getHealth() > playerData.getMaxHealth()) hp = 19;
		if(hp == 0) hp = 1;
		player.setHealth(hp);
		WauzPlayerActionBar.update(player);
	}
	
	/**
	 * Applies attack bonuses to a damage value, based on weapon and passive skills.
	 * Also has a chance to increse the weapon skill.
	 * Increases the damge to 150%, if the player has a strength status effect.
	 * 
	 * @param damage The damage value.
	 * @param player The player that is attacking.
	 * @param weaponType The weapon that the player uses.
	 * 
	 * @return The new damage value.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getSwordSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getAxeSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getStaffSkill(Player)
	 * @see PlayerPassiveSkillConfigurator#getAgility(Player)
	 * @see PlayerPassiveSkillConfigurator#getStrength(Player)
	 * @see PlayerPassiveSkillConfigurator#getManaStatpoints(Player)
	 */
	private static int applyAttackBonus(int damage, Player player, String weaponType) {
		WauzDebugger.log(player, "Attacking with weapon-type: " + weaponType);
		
		float multiplier = 1;
		if(weaponType.contains("SWORD")) {
			multiplier = (float) ((float) PlayerPassiveSkillConfigurator.getSwordSkill(player) / 100000)
					* ((float) PlayerPassiveSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE)) {
				PlayerPassiveSkillConfigurator.increaseSwordSkill(player);
			}
		}
		else if(weaponType.contains("AXE")) {
			multiplier = (float) ((float) PlayerPassiveSkillConfigurator.getAxeSkill(player) / 100000)
					* ((float) PlayerPassiveSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE)) {
				PlayerPassiveSkillConfigurator.increaseAxeSkill(player);
			}
		}
		else if(weaponType.contains("HOE")) {
			multiplier = (float) ((float) PlayerPassiveSkillConfigurator.getStaffSkill(player) / 100000)
					* ((float) PlayerPassiveSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE)) {
				PlayerPassiveSkillConfigurator.increaseStaffSkill(player);
			}
		}
		
		if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			multiplier *= 1.5;
		}
		
		WauzDebugger.log(player, "Base Multiplier: " + Formatters.DEC.format(multiplier));	
		return (int) ((float) damage * (float) multiplier);
	}
	
	/**
	 * Applies defense bonuses to a resist value, based on strength bonus and pet absorption.
	 * 
	 * @param resist The resist value.
	 * @param player The player that gets attacked.
	 * 
	 * @return The new resist value.
	 * 
	 * @see PlayerPassiveSkillConfigurator#getStrengthFloat(Player)
	 * @see PlayerConfigurator#getCharacterPetAbsorption(Player, int)
	 */
	private static int applyDefendBonus(int resist, Player player) {
		float multiplier = PlayerPassiveSkillConfigurator.getStrengthFloat(player);
		
		int petSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
		if(petSlot >= 0) {
			multiplier += (float) ((float) PlayerConfigurator.getCharacterPetAbsorption(player, petSlot) / (float) 10f);
		}
		
		WauzDebugger.log(player, "Base Multiplier: " + Formatters.DEC.format(multiplier));	
		return (int) ((float) resist * (float) multiplier);
	}
	
	/**
	 * Checks if a player has a pvp protection effect.
	 * 
	 * @param player The player to check.
	 * 
	 * @return If the player has a pvp protection effect.
	 * 
	 * @see WauzPlayerData#getResistancePvP()
	 */
	public static boolean hasPvPProtection(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return false;
		}
		
		return playerData.getResistancePvP() > 0;
	}
	
	/**
	 * Decreases the pvp protection time for a player.
	 * 
	 * @param player The player to decrease the protection time for.
	 * 
	 * @see WauzPlayerData#decreasePvPProtection()
	 */
	public static void decreasePvPProtection(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		playerData.decreasePvPProtection();
	}
	
	/**
	 * Increases the pvp protection time for a player, based on a potion interaction.
	 * This status effect increase, shares the cooldown for food effects.
	 * 
	 * @param event The interact event.
	 * 
	 * @see Cooldown#playerFoodConsume(Player)
	 * @see ItemUtils#getPvPProtection(ItemStack)
	 */
	public static void increasePvPProtection(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(ItemUtils.containsPvPProtectionModifier(itemStack)) {
			event.setCancelled(true);
			if(!Cooldown.playerFoodConsume(player)) {
				return;
			}
			if(player.getHealth() < 20) {
				player.sendMessage(ChatColor.RED + "You can only use this on full health!");
				return;
			}
			
			long addedPvsPRes = ItemUtils.getPvPProtection(itemStack);
			playerData.setResistancePvP(FoodCalculator.parseEffectTicksToShort(playerData.getResistancePvP(), addedPvsPRes));
			
			itemStack.setAmount(itemStack.getAmount() - 1);
			player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_LAND, 0);
			player.sendMessage(ChatColor.GREEN + "Your PvP-Protection was extended by " + (addedPvsPRes * 60) + " seconds!");
			WauzPlayerActionBar.update(player);
		}
	}
	
}
