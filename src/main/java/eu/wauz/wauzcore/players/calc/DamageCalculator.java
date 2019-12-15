package eu.wauz.wauzcore.players.calc;

import java.text.DecimalFormat;
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
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Cooldown;
import net.md_5.bungee.api.ChatColor;

/**
 * Used to recalculate how much damage or heal players deal and receive.
 * Also handles stuff like damage boni, reflection, leech and PvP-potions.
 * 
 * Suppresses deprecation of DamageModifier,
 * because there are currently no better alternatives.
 * TODO Find DamageModifier replacement.
 * 
 * @author Wauzmons
 */
@SuppressWarnings("deprecation")
public class DamageCalculator {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###.000");
	
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
		
		boolean isMagic = false;
		double magicMultiplier = 1;
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(isFixedDamage) {
			damage = (int) event.getDamage();
		}
		else {
			if((itemStack.getType().equals(Material.AIR)) || !ItemUtils.hasLore(itemStack)) {
				event.setDamage(1);
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
				if(wzMagicValue > 0)
					magicMultiplier = wzMagicValue + EquipmentUtils.getEnhancementSkillDamageMultiplier(itemStack);
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
		
		if(entity.hasMetadata("wzModMassive")) {
			multiplier = 0.2f * multiplier;
		}
		if(entity.hasMetadata("wzModDeflecting")) {
			SkillUtils.throwBackEntity(player, entity.getLocation(), 1.2);
		}
		
		WauzDebugger.log(player, "Randomized Multiplier: " + formatter.format(multiplier) + (isCritical ? " CRIT" : ""));
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
		}
	}
	
	public static void defend(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null || player.getNoDamageTicks() != 0) {
			return;
		}
		
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY) || Chance.percent(PlayerPassiveSkillConfigurator.getAgility(player))) {
			event.setDamage(0);
			
			ValueIndicator.spawnEvadedIndicator(player);
			player.setNoDamageTicks(10);
			
			WauzDebugger.log(player, "You evaded an attack!");
			return;
		}
		
		int damage = (int) event.getDamage();
		int unmodifiedDamage = damage;
		
		ItemStack itemStack = player.getEquipment().getChestplate();
		if((itemStack != null) && (!itemStack.getType().equals(Material.AIR)) && (itemStack.getItemMeta().getLore() != null)) {
			int defense = EquipmentUtils.getBaseDef(itemStack);
			if(defense > 0 ) {
				damage = (int) (damage - applyDefendBonus(defense, player));
				DurabilityCalculator.damageItem(player, itemStack, true);
			}
		}
		
		event.setDamage(0);
		if(damage < 1) damage = 1;
		int hp = playerData.getHealth() - damage;
		if(hp < 0) hp = 0;
		setHealth(player, hp);
		
		ValueIndicator.spawnDamageIndicator(player, damage);
		player.setNoDamageTicks(10);
		
		WauzDebugger.log(player, "You took " + damage + " (" + unmodifiedDamage + ") damage!");
		WauzDebugger.log(player, "Cause: " + event.getCause() + " " + event.getFinalDamage());
	}
	
	public static void heal(EntityRegainHealthEvent event) {
		Player player = (Player) event.getEntity();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		int heal = (int) event.getAmount();
		
		event.setAmount(0);
		int hp = playerData.getHealth() + heal;
		if(hp > playerData.getMaxHealth()) hp = playerData.getMaxHealth();
		setHealth(player, hp);
		
		ValueIndicator.spawnHealIndicator(player.getLocation(), heal);
		
		WauzDebugger.log(player, "You restored " + heal + " health!");
	}
	
	public static void kill(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();
		
		int onKillHP = EquipmentUtils.getEnhancementOnKillHP(player.getEquipment().getItemInMainHand());
		if(onKillHP > 0)
			heal(new EntityRegainHealthEvent(player, onKillHP, RegainReason.CUSTOM));
		
		int onKillMP = EquipmentUtils.getEnhancementOnKillMP(player.getEquipment().getItemInMainHand());
		if(onKillMP > 0)
			ManaCalculator.regenerateMana(player, onKillMP);
	}
	
	public static void removeDamageModifiers(EntityDamageEvent event) {
		List<DamageModifier> damageModifiers = Arrays.asList(DamageModifier.values()).stream()
				.filter(damageModifier -> event.isApplicable(damageModifier))
				.collect(Collectors.toList());
		
		for(int iterator = 0; iterator < damageModifiers.size(); iterator++) {
			event.setDamage(damageModifiers.get(iterator), iterator == 0 ? event.getDamage() : 0);
		}
	}
	
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
	
	private static int INCREASE_SKILL_CHANCE = 5;
	
	private static int applyAttackBonus(int damage, Player player, String weaponType) {
		WauzDebugger.log(player, "Attacking with weapon-type: " + weaponType);
		
		float multiplier = 1;
		if(weaponType.contains("SWORD")) {
			multiplier = (float) ((float) PlayerPassiveSkillConfigurator.getSwordSkill(player) / 100000)
					* ((float) PlayerPassiveSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE))
				PlayerPassiveSkillConfigurator.increaseSwordSkill(player);
		}
		else if(weaponType.contains("AXE")) {
			multiplier = (float) ((float) PlayerPassiveSkillConfigurator.getAxeSkill(player) / 100000)
					* ((float) PlayerPassiveSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE))
				PlayerPassiveSkillConfigurator.increaseAxeSkill(player);
		}
		else if(weaponType.contains("HOE")) {
			multiplier = (float) ((float) PlayerPassiveSkillConfigurator.getStaffSkill(player) / 100000)
					* ((float) PlayerPassiveSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE))
				PlayerPassiveSkillConfigurator.increaseStaffSkill(player);
		}
		
		if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			multiplier *= 1.5;
		}
		
		WauzDebugger.log(player, "Base Multiplier: " + formatter.format(multiplier));	
		return (int) ((float) damage * (float) multiplier);
	}
	
	private static int applyDefendBonus(int resist, Player player) {
		float multiplier = PlayerPassiveSkillConfigurator.getStrengthFloat(player);
		
		int petSlot = PlayerConfigurator.getCharacterActivePetSlot(player);
		if(petSlot >= 0)
			multiplier += (float) ((float) PlayerConfigurator.getCharacterPetAbsorption(player, petSlot) / (float) 10f);
		
		WauzDebugger.log(player, "Base Multiplier: " + formatter.format(multiplier));	
		return (int) ((float) resist * (float) multiplier);
	}
	
	public static boolean hasPvPProtection(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return false;
		
		return playerData.getResistancePvsP() > 0;
	}
	
	public static void decreasePvPProtection(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		playerData.decreasePvPProtection();
	}
	
	public static void increasePvPProtection(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null)
			return;
		
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(ItemUtils.containsPvPProtectionModifier(itemStack)) {
			event.setCancelled(true);
			if(!Cooldown.playerFoodConsume(player))
				return;
			if(player.getHealth() < 20) {
				player.sendMessage(ChatColor.RED + "You can only use this on full health!");
				return;
			}
			
			long addedPvsPRes = ItemUtils.getPvPProtection(itemStack);
			playerData.setResistancePvsP(FoodCalculator.parseEffectTicksToShort(playerData.getResistancePvsP(), addedPvsPRes));
			
			itemStack.setAmount(itemStack.getAmount() - 1);
			player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_LAND, 0);
			player.sendMessage(ChatColor.GREEN + "Your PvP-Protection was extended by " + (addedPvsPRes * 60) + " seconds!");
			WauzPlayerActionBar.update(player);
		}
	}
	
}
