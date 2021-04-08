package eu.wauz.wauzcore.commands;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.util.PetEggUtils;
import eu.wauz.wauzcore.mobs.pets.WauzActivePet;
import eu.wauz.wauzcore.system.annotations.Command;
import eu.wauz.wauzcore.system.util.Components;

/**
 * A command, that can be executed by a player with fitting permissions.<br/>
 * - Description: <b>Rename Pet</b><br/>
 * - Usage: <b>/rename [name]</b><br/>
 * - Permission: <b>wauz.normal</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdRename implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("rename");
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
		String newName = StringUtils.join(args, " ");
		if(StringUtils.isBlank(newName)) {
			return false;
		}
		
		Player player = (Player) sender;
		ItemStack itemStack = player.getEquipment().getItemInMainHand();
		if(!PetEggUtils.isEggItem(itemStack)) {
			player.sendMessage(ChatColor.RED + "Invalid pet egg selected (in hand)!");
			return true;
		}
		if(!StringUtils.isAlphanumericSpace(newName)) {
			player.sendMessage(ChatColor.RED + "Pet name can't contain special characters!");
			return true;
		}
		if(newName.length() > 16) {
			player.sendMessage(ChatColor.RED + "Pet name can't exceed 16 characters!");
			return true;
		}
		
		WauzActivePet.tryToUnsummon(player, true);
		ItemMeta itemMeta = itemStack.getItemMeta();
		String petName = Components.displayName(itemMeta);
		Components.displayName(itemMeta, petName.replace(ChatColor.stripColor(petName), newName));
		itemStack.setItemMeta(itemMeta);
		player.sendMessage(ChatColor.GREEN + "Your pet was renamed to " + newName + "!");
		return true;
	}

}
