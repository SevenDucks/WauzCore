package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Get Equip from String</b><br>
 * - Usage: <b>/wzGetEquip [equipname] [tier] [player]</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzGetEquip implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzGetEquip", "getequip");
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
		if(args.length < 2) {
			return false;
		}
		
		String type = args[0].replace("_", " ");
		String tier = "T" + args[1];
		Player player = args.length < 3 ? (Player) sender : WauzCore.getOnlinePlayer(args[2]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		
		ItemStack equipmentItemStack = new ItemStack(Material.SHEARS);
		ItemMeta equipmentItemMeta = equipmentItemStack.getItemMeta();
		Components.displayName(equipmentItemMeta, ChatColor.GRAY + "Unidentified " + tier + " Item : " + type);
		equipmentItemStack.setItemMeta(equipmentItemMeta);
		
		new WauzEquipmentIdentifier().identifyItem(player, equipmentItemStack, true);
		player.getInventory().addItem(WauzNmsClient.nmsSerialize(equipmentItemStack));
		return true;
	}

}
