package eu.wauz.wauzcore.commands.admins;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.WauzCommand;
import eu.wauz.wauzcore.commands.WauzCommandExecutor;
import eu.wauz.wauzcore.skills.particles.Colors;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.Chance;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Create Firework Show</b><br>
 * - Usage: <b>/wzFirework</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzFirework implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzFirework", "firework");
	}

	/**
	 * Executes the command for given sender with arguments.
	 * 
	 * @param sender The sender of the command.
	 * @param args The arguments of the command.
	 * 
	 * @return If the command had correct syntax.
	 */
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		spawnFirework(player.getLocation(), 20, 600, 2);
		return true;
	}
	
	/**
	 * Spawns random firework based on a specified interval.
	 * 
	 * @param location The location to spawn the firework at.
	 * @param radius The radius of the spawn location.
	 * @param ticks How often firework should be fired.
	 * @param interval The server ticks between each rocket.
	 */
	public static void spawnFirework(Location location, int radius, int ticks, int interval) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
			
	        @Override
			public void run() {
	        	try {
	        		float x = Chance.negativePositive(radius);
	        		float z = Chance.negativePositive(radius);
	        		Location fireworkLocation = location.clone().add(x, 1, z);
	        		Firework firework = (Firework) fireworkLocation.getWorld().spawnEntity(fireworkLocation, EntityType.FIREWORK);
	        		FireworkMeta fireworkMeta = firework.getFireworkMeta();
	        		Type type = Type.values()[Chance.randomInt(Type.values().length)];
	        		Color color1 = Colors.getRandom();
	        		Color color2 = Colors.getRandom();
	        		Color color3 = Colors.getRandom();
	        		FireworkEffect effect = FireworkEffect.builder()
	        				.with(type)
	        				.withColor(color1, color2)
	        				.withFade(color3)
	        				.flicker(Chance.randomBoolean())
	        				.trail(Chance.randomBoolean())
	        				.build();
	        		fireworkMeta.addEffect(effect);
	        		fireworkMeta.setPower(Chance.randomInt(4) + 1);
	        		firework.setFireworkMeta(fireworkMeta);
        			if(ticks - 1 > 0) {
        				spawnFirework(location, radius, ticks - 1, interval);
        			}
	        	}
	        	catch (NullPointerException e) {
	        		WauzDebugger.catchException(getClass(), e);
	        	}
	        }
	        
		}, interval);
	}

}
