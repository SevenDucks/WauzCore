package eu.wauz.wauzcore.system.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.system.commands.execution.WauzCommand;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class CmdWzHeal implements WauzCommand {

	@Override
	public String getCommandId() {
		return "wzHeal";
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
		
		Player player = args.length == 0 ? (Player) sender : WauzCore.getOnlinePlayer(args[0]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		if(WauzMode.isMMORPG(player)) {
			DamageCalculator.setHealth(player, WauzPlayerDataPool.getPlayer(player).getMaxHealth());
		}
		else {
			player.setHealth(20);
		}
		player.setFoodLevel(20);
		player.setSaturation(20);
		return true;
	}

}
