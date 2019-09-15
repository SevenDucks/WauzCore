package eu.wauz.wauzcore.mobs;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import net.md_5.bungee.api.ChatColor;

public class MenacingMobsLoot {
	
	public static void dropExp(Entity entity, Entity killer) {
		if(killer == null || !(killer instanceof Player)) {
			return;
		}
		
		int tier = entity.getMetadata("wzExpTier").get(0).asInt();
		double amount = entity.getMetadata("wzExpAmount").get(0).asDouble();
		if(tier > 0 && amount > 0) {
			WauzRewards.level((Player) killer, tier, amount, entity.getLocation());
		}
	}
	
	public static void dropKey(Entity entity) {
		String keyId = entity.getMetadata("wzKeyId").get(0).asString();
		if(StringUtils.isNotBlank(keyId)) {
			World world = entity.getWorld();
			InstanceConfigurator.setInstanceWorldKeyStatus(world, keyId, InstanceConfigurator.KEY_STATUS_OBTAINED);
			
			for(Player player : world.getPlayers()) {
				WauzPlayerScoreboard.scheduleScoreboard(player);
				player.sendMessage(ChatColor.GREEN + "You obtained the Key \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\"!");
			}
		}
	}

}
