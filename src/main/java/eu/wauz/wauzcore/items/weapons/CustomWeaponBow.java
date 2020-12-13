package eu.wauz.wauzcore.items.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.DurabilityCalculator;
import eu.wauz.wauzcore.items.util.EquipmentUtils;
import eu.wauz.wauzcore.menu.ArrowMenu;
import eu.wauz.wauzcore.mobs.MobMetadataUtils;
import eu.wauz.wauzcore.skills.SkillUtils;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.annotations.Item;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A collection of methods for using the bow weapon.
 * 
 * @author Wauzmons
 */
@Item
public class CustomWeaponBow implements CustomWeapon {
	
	/**
	 * Cancels the event of a bow interaction.
	 * Opens the arrow selection menu, if the player is sneaking.
	 * Otherwise shoots an arrow, if it was a right click.
	 * 
	 * @param event The interaction event.
	 * 
	 * @see ArrowMenu#open(Player)
	 * @see CustomWeaponBow#tryToShoot(Player, ItemStack)
	 */
	@Override
	public void use(PlayerInteractEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		if(player.isSneaking()) {
			ArrowMenu.open(player);
		}
		else if(event.getAction().toString().contains("RIGHT")) {
			if(Cooldown.playerProjectileShoot(player)) {
				tryToShoot(player, player.getEquipment().getItemInMainHand());
			}
		}
	}
	
	/**
	 * Returns the materials that trigger events with the item.
	 * 
	 * @return The list of materials.
	 */
	@Override
	public List<Material> getCustomItemMaterials() {
		return Arrays.asList(Material.BOW);
	}
	
	/**
	 * Determines if the custom weapon can have a skillgem slot.
	 * 
	 * @return If the custom weapon can have a skillgem slot.
	 */
	@Override
	public boolean canHaveSkillSlot() {
		return false;
	}

	/**
	 * Gets the lores to show on an instance of the custom weapon.
	 * 
	 * @param hasSkillSlot If the weapon has a skillgem slot.
	 */
	@Override
	public List<String> getCustomWeaponLores(boolean hasSkillSlot) {
		List<String> lores = new ArrayList<>();
		lores.add("");
		lores.add(ChatColor.GRAY + "Use while Sneaking to switch Arrows");
		lores.add(ChatColor.GRAY + "Right Click to shoot Arrows");
		return lores;
	}
	
	/**
	 * Lets the player shoot an arrow from the given bow.
	 * Removes an arrow of the selected type and switches to normal arrows when empty.
	 * Arrow type and bow damage are transported as metadata on the arrow entity.
	 * Creates an arrow trail based on the type and damages the bow it was shot from.
	 * 
	 * @param player The player that is shooting.
	 * @param bow The bow item stack.
	 * 
	 * @see PlayerConfigurator#getSelectedArrows(Player)
	 * @see PlayerConfigurator#getArrowAmount(Player, String)
	 * @see MobMetadataUtils#setArrowTypeAndDamage(Arrow, String, int)
	 * @see CustomWeaponBow#spawnArrowTrail(Arrow, Color)
	 * @see CustomWeaponBow#getArrowColor(String)
	 * @see DurabilityCalculator#damageItem(Player, ItemStack, boolean)
	 * @see CustomWeaponBow#onArrowHit(Player, Entity, Arrow)
	 */
	public static void tryToShoot(Player player, ItemStack bow) {
		String arrowType = PlayerConfigurator.getSelectedArrows(player);
		boolean usingNormalArrows = arrowType.equals("normal");
		int arrowAmount = usingNormalArrows ? 999 : PlayerConfigurator.getArrowAmount(player, arrowType);
		if(arrowAmount < 1) {
			player.sendMessage(ChatColor.YELLOW
					+ "You ran out of " + StringUtils.capitalize(arrowType)
					+ " Arrows! Switched to normal ones!");
			arrowType = "normal";
			PlayerConfigurator.setSelectedArrowType(player, arrowType);
		}
		else if(!usingNormalArrows) {
			PlayerConfigurator.setArrowAmount(player, arrowType, arrowAmount - 1);
		}
		
		int damage = EquipmentUtils.getBaseAtk(bow);
		Vector vector = player.getLocation().getDirection().multiply(1.75);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        Arrow arrow = (Arrow) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARROW);
        MobMetadataUtils.setArrowTypeAndDamage(arrow, arrowType, damage);
        arrow.setShooter(player);
        arrow.setVelocity(vector);
        spawnArrowTrail(arrow, getArrowColor(arrowType));
        
        DurabilityCalculator.damageItem(player, bow, false);
	}
	
	/**
	 * Gets a color based on the name of an arrow type.
	 * 
	 * @param arrowType The name of the arrow type.
	 * 
	 * @return The color of the arrow type.
	 */
	private static Color getArrowColor(String arrowType) {
		switch (arrowType) {
		case "reinforced":
			return Color.GREEN;
		case "fire":
			return Color.RED;
		case "ice":
			return Color.BLUE;
		case "shock":
			return Color.YELLOW;
		case "bomb":
			return Color.BLACK;
		default:
			return Color.WHITE;
		}
	}
	
	/**
	 * Spawns a trail for an arrow with the given color.
	 * A particle is spawned recursively every tick, as long as the entity is valid.
	 * 
	 * @param arrow The arrow entity.
	 * @param color The trail particle color.
	 * 
	 * @see SkillParticle#spawn(org.bukkit.Location, int)
	 */
	public static void spawnArrowTrail(Arrow arrow, Color color) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        public void run() {
	        	try {
	        		if(arrow != null && !arrow.isOnGround() && arrow.isValid()) {
	        			new SkillParticle(color).spawn(arrow.getLocation(), 1);
	        			spawnArrowTrail(arrow, color);
	        		}
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
	        
		}, 1);
	}
	
	/**
	 * Decides when to cancel arrow damage.
	 * The event is never cancelled, if it was not shot by a player.
	 * The event is always cancelled, if the shooter or target have pvp protection.
	 * Thr event is cancelled and replaced by a custom method call in MMORPG mode.
	 * 
	 * @param event The entity damage event.
	 * 
	 * @return If the arrow damage should be cancelled.
	 * 
	 * @see CustomWeaponBow#onArrowHit(Player, Entity, Arrow)
	 */
	public static boolean cancelArrowImpact(EntityDamageByEntityEvent event) {
		Arrow arrow = (Arrow) event.getDamager();
		Entity entity = event.getEntity();
		if(!(arrow.getShooter() instanceof Player)) {
			return false;
		}
		
		Player player = (Player) arrow.getShooter();
		if(entity instanceof Player) {
			return WauzRegion.disallowPvP(player, entity);
		}
		else if(WauzMode.isMMORPG(player)) {
			WauzDebugger.log(player, "Canceled Arrow Impact");
			onArrowHit(player, entity, arrow);
			return true;
		}
		return false;
	}
	
	/**
	 * Applies damage and effects from an arrow shot by a custom bow.
	 * Arrow type and bow damage are transported as metadata on the arrow entity.</br>
	 * Normal: <b>Weapon Damage x1.0</b></br>
	 * Reinforced: <b>Weapon Damage x2.0</b></br>
	 * Fire: <b>Normal + x0.5 every 1s for 5s</b></br>
	 * Ice: <b>Normal + Slowness for 5s</b></br>
	 * Shock: <b>Normal + optional Fall Damage</b></br>
	 * Bomb: <b>Normal in radius of 2.5 blocks</b></br>
	 * 
	 * @param player The player that shot the arrow.
	 * @param entity The entity that got hit by the arrow.
	 * @param arrow The arrow that hit the entity.
	 * 
	 * @see CustomWeaponBow#tryToShoot(Player, ItemStack)
	 * @see MobMetadataUtils#hasArrowTypeAndDamage(Arrow)
	 * @see MobMetadataUtils#getArrowType(Arrow)
	 * @see MobMetadataUtils#getArrowDamage(Arrow)
	 * @see SkillUtils#callPlayerFixedDamageEvent(Player, Entity, double)
	 * @see SkillUtils#callPlayerDamageOverTimeEvent(Player, Entity, Color, int, int, int)
	 * @see SkillUtils#addPotionEffect(Entity, PotionEffectType, int, int)
	 * @see SkillUtils#throwBackEntity(Entity, org.bukkit.Location, double)
	 * @see SkillUtils#createExplosion(org.bukkit.Location, float)
	 */
	public static void onArrowHit(Player player, Entity entity, Arrow arrow) {
		if(!MobMetadataUtils.hasArrowTypeAndDamage(arrow)) {
			return;
		}
		
		String arrowType = MobMetadataUtils.getArrowType(arrow);
		int arrowDamage = MobMetadataUtils.getArrowDamage(arrow);
		
		if(arrowType.equals("normal")) {
			WauzDebugger.log(player, "Normal Arrow Hit");
			SkillUtils.callPlayerFixedDamageEvent(player, entity, arrowDamage);
		}
		else if(arrowType.equals("reinforced")) {
			WauzDebugger.log(player, "Reinforced Arrow Hit");
			SkillUtils.callPlayerFixedDamageEvent(player, entity, arrowDamage * 2);
		}
		else if(arrowType.equals("fire")) {
			WauzDebugger.log(player, "Fire Arrow Hit");
			SkillUtils.callPlayerFixedDamageEvent(player, entity, arrowDamage);
			SkillUtils.callPlayerDamageOverTimeEvent(player, entity, Color.ORANGE, arrowDamage / 2 + 1, 5, 20);
		}
		else if(arrowType.equals("ice")) {
			WauzDebugger.log(player, "Ice Arrow Hit");
			SkillUtils.callPlayerFixedDamageEvent(player, entity, arrowDamage);
			SkillUtils.callPlayerDamageOverTimeEvent(player, entity, Color.AQUA, 0, 5, 20);
			SkillUtils.addPotionEffect(entity, PotionEffectType.SLOW, 6, 2);
		}
		else if(arrowType.equals("shock")) {
			WauzDebugger.log(player, "Shock Arrow Hit");
			SkillUtils.callPlayerFixedDamageEvent(player, entity, arrowDamage);
			SkillUtils.throwBackEntity(entity, arrow.getLocation(), 2);
		}
		else if(arrowType.equals("bomb")) {
			WauzDebugger.log(player, "Bomb Arrow Hit");
			List<Entity> targets = SkillUtils.getTargetsInRadius(entity.getLocation(), 2.5);
			SkillUtils.callPlayerFixedDamageEvent(player, targets, arrowDamage);
			SkillUtils.createExplosion(entity.getLocation(), 5);
		}
	}

}
