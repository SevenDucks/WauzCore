package eu.wauz.wauzcore.items;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.ArrowMenu;
import eu.wauz.wauzcore.skills.execution.SkillUtils;
import eu.wauz.wauzcore.skills.particles.SkillParticle;
import eu.wauz.wauzcore.system.WauzRegion;
import eu.wauz.wauzcore.system.commands.WauzDebugger;
import eu.wauz.wauzcore.system.util.Cooldown;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class CustomWeaponBow {
	
	public static void use(PlayerInteractEvent event) {
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
		
		int damage = ItemUtils.getBaseAtk(bow);
		Vector vector = player.getLocation().getDirection().multiply(1.75);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        Arrow arrow = (Arrow) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARROW);
        arrow.setMetadata("wzArrowType", new FixedMetadataValue(WauzCore.getInstance(), arrowType));
        arrow.setMetadata("wzArrowDmg", new FixedMetadataValue(WauzCore.getInstance(), damage));
        arrow.setShooter(player);
        arrow.setVelocity(vector);
        spawnArrowTrail(arrow, getArrowColor(arrowType));
        
        DurabilityCalculator.takeDamage(player, bow, false);
	}
	
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
	
	public static boolean cancelArrowImpact(EntityDamageByEntityEvent event) {
		Arrow arrow = (Arrow) event.getDamager();
		Entity entity = event.getEntity();
		if(!(arrow.getShooter() instanceof Player))
			return false;
		
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
	
	public static void onArrowHit(Player player, Entity entity, Arrow arrow) {
		if(arrow.getMetadata("wzArrowType").isEmpty() && arrow.getMetadata("wzArrowDmg").isEmpty())
			return;
		
		String arrowType = arrow.getMetadata("wzArrowType").get(0).asString();
		int arrowDamage = arrow.getMetadata("wzArrowDmg").get(0).asInt();
		
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
