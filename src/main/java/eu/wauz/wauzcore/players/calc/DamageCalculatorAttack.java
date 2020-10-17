package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.wauz.wauzcore.data.players.PlayerBestiaryConfigurator;
import eu.wauz.wauzcore.data.players.PlayerSkillConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.mobs.MenacingModifier;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.mobs.bestiary.ObservationRank;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzPermission;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.DeprecatedUtils;
import eu.wauz.wauzcore.system.util.Formatters;

/**
 * A calculation of the dealt damage from a player attack.
 * 
 * @author Wauzmons
 * 
 * @see DamageCalculator
 */
public class DamageCalculatorAttack {
	
	/**
	 * The chance that a weapon skill increases on weapon usage. One in x.
	 */
	private static final int INCREASE_SKILL_CHANCE = 5;
	
	/**
	 * The damage event.
	 */
	private EntityDamageByEntityEvent event;
	
	/**
	 * The player who is attacking.
	 */
	private Player player;
	
	/**
	 * The entity who gets attacked.
	 */
	private Entity entity;
	
	/**
	 * The item stack, the player is attacking with.
	 */
	private ItemStack weaponItemStack;
	
	/**
	 * If the damage should have a fixed value. Doesn't prevent crits.
	 */
	private boolean isFixedDamage;
	
	/**
	 * If the damage was dealt by a magic skill.
	 */
	private boolean isMagic;
	
	/**
	 * If the attack was a critical hit.
	 */
	private boolean isCritical;
	
	/**
	 * In attack debug mode, the damage output is multiplied by 100.
	 */
	private boolean isAttackDebugMode;
	
	/**
	 * The multiplier for magic skills, still counting as "unmodified" damage.
	 */
	private double magicMultiplier;
	
	/**
	 * The damage value before weapon or random multipliers are applied.
	 */
	private int unmodifiedDamage;
	
	/**
	 * The final calculated damage value.
	 */
	private int damage;
	
	/**
	 * Creates a new calculator attack.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DamageCalculatorAttack#run()
	 */
	public DamageCalculatorAttack(EntityDamageByEntityEvent event) {
		this.event = event;
		player = (Player) event.getDamager();
		entity = event.getEntity();
		damage = 1;
		magicMultiplier = 1;
		unmodifiedDamage = (int) event.getDamage();
		isAttackDebugMode = player.hasPermission(WauzPermission.DEBUG_ATTACK.toString());
		weaponItemStack = player.getEquipment().getItemInMainHand();
	}
	
	/**
	 * Determines the amount of damage a player dealt.
	 * Determines if it is normal (with boni), fixed (without boni) or magic (multiplied) damage.
	 * Checks level requirements and cooldown on the used weapon,
	 * so the attack can be cancelled and an according message can be shown.
	 * 
	 * @see MobMetadataUtils#hasFixedDamage(Entity)
	 * @see DamageCalculatorAttack#checkWeaponRequirements()
	 * @see DamageCalculatorAttack#applyMagicMultiplier()
	 * @see DamageCalculatorAttack#applyWeaponMultiplier()
	 * @see DamageCalculatorAttack#applyRandomizedMultiplier()
	 * @see DamageCalculatorAttack#inflictDamage()
	 */
	public void run() {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return;
		}
		
		WauzDebugger.log(player, "Attacked Entity Type: " + entity.getType());
		if(MobMetadataUtils.hasFixedDamage(entity)) {
			MobMetadataUtils.setFixedDamage(entity, false);
			damage = (int) event.getDamage();
			isFixedDamage = true;
		}
		else if(!checkWeaponRequirements()) {
			return;
		}
		else {
			if(!applyMagicMultiplier()) {
				return;
			}
			applyWeaponMultiplier();
		}
		applyRandomizedMultiplier();
		inflictDamage();
	}
	
	/**
	 * Checks if the player meets the requirements to attack with their weapon.
	 * If the weapon is not valid, the damage is set to 1 and Minecraft damage modifiers are removed.
	 * If the level requirement is not met, the damage is set to 0.
	 * If one of these cases occur, the damage is applied and the rest of the event is cancelled.
	 * Also lets damage indicators pop up after the attack, aswell as reducing weapon durability, if necessary.
	 * 
	 * @return If the calculation should continue.
	 * 
	 * @see EquipmentUtils#getLevelRequirement(ItemStack)
	 * @see DeprecatedUtils#removeDamageModifiers(EntityDamageEvent)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 * @see RageCalculator#generateRage(Player)
	 * @see ValueIndicator#spawnDamageIndicator(Entity, Integer)
	 */
	private boolean checkWeaponRequirements() {
		if((weaponItemStack.getType().equals(Material.AIR)) || !ItemUtils.hasLore(weaponItemStack)) {
			event.setDamage(isAttackDebugMode ? 100: 1);
			DeprecatedUtils.removeDamageModifiers(event);
			DurabilityCalculator.damageItem(player, weaponItemStack, false);
			RageCalculator.generateRage(player);
			ValueIndicator.spawnDamageIndicator(event.getEntity(), 1);
			WauzDebugger.log(player, "Attacked with Non-Weapon");
			return false;
		}
		int requiredLevel = EquipmentUtils.getLevelRequirement(weaponItemStack);
		WauzDebugger.log(player, "Required Level: " + requiredLevel);
		if(player.getLevel() < requiredLevel) {
			event.setCancelled(true);
			DurabilityCalculator.damageItem(player, weaponItemStack, false);
			player.sendMessage(ChatColor.RED + "You must be at least lvl " + requiredLevel + " to use this item!");
			return false;
		}
		return true;
	}
	
	/**
	 * Applies attack bonuses to a damage value, based on the spell type.
	 * If it is not a spell and the weapon is not charged, the attack will miss.
	 * 
	 * @return If the calculation should continue.
	 * 
	 * @see MobMetadataUtils#getMagicDamageMultiplier(Entity)
	 * @see EquipmentUtils#getEnhancementSkillDamageMultiplier(ItemStack)
	 * @see EquipmentUtils#getBaseAtk(ItemStack)
	 * @see ValueIndicator#spawnMissedIndicator(Entity)
	 */
	private boolean applyMagicMultiplier() {
		double magicBaseMultiplier = MobMetadataUtils.getMagicDamageMultiplier(entity);
		if(magicBaseMultiplier > 0) {
			magicMultiplier = magicBaseMultiplier + EquipmentUtils.getEnhancementSkillDamageMultiplier(weaponItemStack);
			MobMetadataUtils.setMagicDamageMultiplier(entity, 0);
			WauzDebugger.log(player, "Magic Damage-Multiplier: " + magicMultiplier);
			isMagic = true;
		}
		else if(!Cooldown.playerWeaponUse(player)) {
			WauzDebugger.log(player, "Missed - Weapon Not Ready");
			event.setCancelled(true);
			player.resetCooldown();
			
			ValueIndicator.spawnMissedIndicator(entity);
			return false;
		}
		damage = EquipmentUtils.getBaseAtk(weaponItemStack);
		unmodifiedDamage = (int) (damage * magicMultiplier);
		damage = unmodifiedDamage;
		return true;
	}
	
	/**
	 * Applies attack bonuses to a damage value, based on weapon and passive skills.
	 * Also has a chance to increse the weapon skill.
	 * Increases the damge to 150%, if the player has a strength status effect.
	 * 
	 * @see PlayerSkillConfigurator#getSwordSkill(Player)
	 * @see PlayerSkillConfigurator#getAxeSkill(Player)
	 * @see PlayerSkillConfigurator#getStaffSkill(Player)
	 * @see PlayerSkillConfigurator#getAgility(Player)
	 * @see PlayerSkillConfigurator#getStrength(Player)
	 * @see PlayerSkillConfigurator#getManaStatpoints(Player)
	 */
	private void applyWeaponMultiplier() {
		String weaponType = weaponItemStack.getType().name();
		WauzDebugger.log(player, "Attacking with Weapon-Type: " + weaponType);
		
		float multiplier = 1;
		if(weaponType.contains("SWORD")) {
			multiplier = ((float) PlayerSkillConfigurator.getSwordSkill(player) / 100000)
					* ((float) PlayerSkillConfigurator.getAgilityStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE)) {
				PlayerSkillConfigurator.increaseSwordSkill(player);
			}
		}
		else if(weaponType.contains("AXE")) {
			multiplier = ((float) PlayerSkillConfigurator.getAxeSkill(player) / 100000)
					* ((float) PlayerSkillConfigurator.getStrengthStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE)) {
				PlayerSkillConfigurator.increaseAxeSkill(player);
			}
		}
		else if(weaponType.contains("HOE")) {
			multiplier = ((float) PlayerSkillConfigurator.getStaffSkill(player) / 100000)
					* ((float) PlayerSkillConfigurator.getManaStatpoints(player) * 5 / 100 + 1);
			
			if(Chance.oneIn(INCREASE_SKILL_CHANCE)) {
				PlayerSkillConfigurator.increaseStaffSkill(player);
			}
		}
		
		if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			multiplier *= 1.5;
		}
		
		WauzDebugger.log(player, "Weapon Damage-Multiplier: " + Formatters.DEC.format(multiplier));	
		damage = (int) ((float) damage * multiplier);
	}
	
	/**
	 * Applies attack bonuses to a damage value, based on randomness and mob modifiers.
	 * Even applied to fixed damage events.
	 * 
	 * @see EquipmentUtils#getEnhancementCriticalDamageMultiplier(ItemStack)
	 * @see MobMetadataUtils#hasBestiaryEntry(Entity)
	 * @see MobMetadataUtils#hasMenacingModifier(Entity, MenacingModifier)
	 */
	private void applyRandomizedMultiplier() {
		isCritical = Chance.percent(PlayerSkillConfigurator.getAgility(player));
		
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
		if(MobMetadataUtils.hasBestiaryEntry(entity)) {
			String entry = MobMetadataUtils.getBestiaryEntry(entity);
			boolean isBoss = MobMetadataUtils.isRaidBoss(entity);
			int killCount = PlayerBestiaryConfigurator.getBestiaryKills(player, entry);
			int neededKills = isBoss ? ObservationRank.A.getBossKills() : ObservationRank.A.getNormalKills();
			multiplier *= killCount >= neededKills ? 1.25f : 1;
		}
		if(MobMetadataUtils.hasMenacingModifier(entity, MenacingModifier.MASSIVE)) {
			multiplier = 0.2f * multiplier;
		}
		if(MobMetadataUtils.hasMenacingModifier(entity, MenacingModifier.DEFLECTING)) {
			SkillUtils.throwBackEntity(player, entity.getLocation(), 1.2);
		}
		
		WauzDebugger.log(player, "Randomized Damage-Multiplier: " + Formatters.DEC.format(multiplier) + (isCritical ? " CRIT" : ""));
		damage = (int) ((float) damage * multiplier);
	}
	
	/**
	 * Inflicts the calculated damage, with a minimum value of 1, to the entity.
	 * Minecraft damage modifiers are removed and replaced by already applied custom bonuses and (crit) randomizers.
	 * Also lets damage indicators pop up after the attack, aswell as reducing weapon durability, if necessary.
	 * 
	 * @see DeprecatedUtils#removeDamageModifiers(EntityDamageEvent)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 * @see RageCalculator#generateRage(Player)
	 * @see ValueIndicator#spawnDamageIndicator(Entity, Integer)
	 */
	private void inflictDamage() {
		damage = damage < 1 ? 1 : damage;
		event.setDamage(damage);
		DeprecatedUtils.removeDamageModifiers(event);
		
		if(!isMagic && !isFixedDamage && !event.getCause().equals(DamageCause.ENTITY_SWEEP_ATTACK)) {
			DurabilityCalculator.damageItem(player, weaponItemStack, false);
		}
		RageCalculator.generateRage(player);
		ValueIndicator.spawnDamageIndicator(event.getEntity(), damage, isCritical);
		
		WauzDebugger.log(player, "You inflicted " + damage + " (" + unmodifiedDamage + ") damage!");
		WauzDebugger.log(player, "Cause: " + event.getCause() + " " + event.getFinalDamage());
	}

}
