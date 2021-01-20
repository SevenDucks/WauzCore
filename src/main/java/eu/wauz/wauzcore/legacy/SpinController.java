//package eu.wauz.wauzcore.legacy;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.Bukkit;
//import org.bukkit.DyeColor;
//import org.bukkit.Location;
//import org.bukkit.entity.ArmorStand;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Shulker;
//
//import eu.wauz.wauzcore.WauzCore;
//
//public class SpinController {
//	
//	private Location origin;
//	
//	private int radius;
//	
//	private int points;
//	
//	private int currentPoint;
//	
//	private double increment;
//	
//	private List<Entity> entities;
//	
//	private boolean continueSpinning;
//	
//	public SpinController(Location origin, int radius, int points) {
//		this.origin = origin;
//		this.radius = radius;
//		this.points = points;
//		this.currentPoint = 0;
//		this.increment = (2 * Math.PI) / points;
//		this.entities = new ArrayList<>();
//		spawn();
//		spin();
//	}
//	
//	private void spawn() {
//		for(int currentRadius = 0; currentRadius < radius; currentRadius++) {
//			Location location = getPoint(currentRadius + 2);
//			Shulker shulker = (Shulker) location.getWorld().spawnEntity(location, EntityType.SHULKER);
//			shulker.setAI(false);
//			shulker.setSilent(true);
//			shulker.setInvulnerable(true);
//			shulker.setColor(DyeColor.YELLOW);
//			ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location.subtract(0, 1, 0), EntityType.ARMOR_STAND);
//			entity.setGravity(false);
//			entity.setInvulnerable(true);
//			entity.setSmall(true);
//			entity.addPassenger(shulker);
//			entities.add(entity);
//		}
//		continueSpinning = true;
//	}
//	
//	public void spin() {
//		if(currentPoint < points - 1) {
//			currentPoint++;
//		}
//		else {
//			currentPoint = 0;
//		}
//		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
//			
//			@Override
//			public void run() {
//				if(!continueSpinning) {
//					return;
//				}
//				for(int currentRadius = 0; currentRadius < radius; currentRadius++) {
//					Entity entity = entities.get(currentRadius);
//					Location location = getPoint(currentRadius + 2);
//					entity.teleport(location);
//				}
//				spin();
//			}
//			
//		}, 2);
//	}
//	
//	public void remove() {
//		continueSpinning = false;
//		for(Entity entity : entities) {
//			for(Entity passenger : entity.getPassengers()) {
//				passenger.remove();
//			}
//			entity.remove();
//		}
//	}
//	
//	private Location getPoint(int radius) {
//		double angle = currentPoint * increment;
//        double x = origin.getX() + (radius * Math.cos(angle));
//        double z = origin.getZ() + (radius * Math.sin(angle));
//        return new Location(origin.getWorld(), x, origin.getY() + 0.5, z, (float) angle, 0f);
//	}
//
//}
