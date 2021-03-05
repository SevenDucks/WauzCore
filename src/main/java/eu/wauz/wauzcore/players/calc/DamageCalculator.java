package eu.wauz.wauzcore.players.calc;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectSource;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffectType;
import eu.wauz.wauzcore.players.effects.WauzPlayerEffects;
import eu.wauz.wauzcore.players.ui.ValueIndicator;
import eu.wauz.wauzcore.players.ui.WauzPlayerActionBar;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;

/**
 * Used to recalculate how much damage or heal players deal and receive.
 * Also handles stuff like damage boni, reflection, leech and PvP-potions.
 * 
 * @author Wauzmons
 * 
 * @see WauzPlayerData#getHealth()
 */
public class DamageCalculator {
	
	/**
	 * Determines the amount of damage a player dealt.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DamageCalculatorAttack
	 */
	public static void attack(EntityDamageByEntityEvent event) {
		new DamageCalculatorAttack(event).run();
	}
	
	/**
	 * Determines the amount of damage a player takes.
	 * 
	 * @param event The damage event.
	 * 
	 * @see DamageCalculatorDefense
	 */
	public static void defend(EntityDamageEvent event) {
		new DamageCalculatorDefense(event).run();
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
		int hp = playerData.getStats().getHealth() + heal;
		if(hp > playerData.getStats().getMaxHealth()) hp = playerData.getStats().getMaxHealth();
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
			playerData.getStats().setHealth(playerData.getStats().getMaxHealth());
			WauzPlayerActionBar.update(player);
			return;
		}
		
		playerData.getStats().setHealth(hp);
		hp = (hp * 20) / playerData.getStats().getMaxHealth();
		if(hp == 20 && playerData.getStats().getHealth() > playerData.getStats().getMaxHealth()) hp = 19;
		if(hp == 0) hp = 1;
		player.setHealth(hp);
		WauzPlayerActionBar.update(player);
	}
	
	/**
	 * Checks if a player has a pvp protection effect.
	 * 
	 * @param player The player to check.
	 * 
	 * @return If the player has a pvp protection effect.
	 */
	public static boolean hasPvPProtection(Player player) {
		WauzPlayerData playerData = WauzPlayerDataPool.getPlayer(player);
		if(playerData == null) {
			return false;
		}
		return playerData.getStats().getEffects().hasEffect(WauzPlayerEffectType.PVP_PROTECTION);
	}
	
	/**
	 * Applies pvp protection for a player, based on a potion interaction.
	 * This status effect increase shares the cooldown for food effects.
	 * 
	 * @param event The interact event.
	 * 
	 * @see Cooldown#playerFoodConsume(Player)
	 * @see ItemUtils#getPvPProtection(ItemStack)
	 */
	public static void applyPvPProtection(PlayerInteractEvent event) {
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
			
			int pvpRes = ItemUtils.getPvPProtection(itemStack) * 60;
			WauzPlayerEffects effects = playerData.getStats().getEffects();
			if(effects.addEffect(WauzPlayerEffectType.PVP_PROTECTION, WauzPlayerEffectSource.ITEM, pvpRes)) {
				itemStack.setAmount(itemStack.getAmount() - 1);
				player.getWorld().playEffect(player.getLocation(), Effect.ANVIL_LAND, 0);
				player.sendMessage(ChatColor.GREEN + "Your PvP-Protection was extended to " + pvpRes + " seconds!");
				WauzPlayerActionBar.update(player);
			}
			else {
				player.sendMessage(ChatColor.YELLOW + "You already hava a longer PvP-Protection active!");
			}
		}
	}
	
}
