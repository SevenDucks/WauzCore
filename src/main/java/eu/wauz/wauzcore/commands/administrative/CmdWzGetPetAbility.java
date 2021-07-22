package eu.wauz.wauzcore.commands.administrative;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.items.InventorySerializer;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbility;
import eu.wauz.wauzcore.mobs.pets.WauzPet;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;
import eu.wauz.wauzcore.mobs.pets.WauzPetEgg;
import eu.wauz.wauzcore.system.annotations.Command;

/**
 * A command, that can be executed by a player with fitting permissions.<br>
 * - Description: <b>Get Ability Pet from String</b><br>
 * - Usage: <b>/wzGetPet.abilty [petname] [ability] [player]</b><br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
@Command
public class CmdWzGetPetAbility implements WauzCommand {

	/**
	 * @return The id of the command, aswell as aliases.
	 */
	@Override
	public List<String> getCommandIds() {
		return Arrays.asList("wzGetPet.ability", "getpet.ability");
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
		
		WauzPet pet = WauzPet.getPet(args[0]);
		if(pet == null) {
			sender.sendMessage(ChatColor.RED + "Unknown pet type specified!");
			return false;
		}
		WauzPetAbility petAbility = WauzPetAbilities.getAbility(args[1]);
		if(petAbility == null) {
			sender.sendMessage(ChatColor.RED + "Unknown ability specified!");
			return false;
		}
		Player player = args.length < 3 ? (Player) sender : WauzCore.getOnlinePlayer(args[1]);
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "Unknown player specified!");
			return false;
		}
		player.getInventory().addItem(InventorySerializer.serialize(WauzPetEgg.getEggItem(player, pet, petAbility, System.currentTimeMillis())));
		return true;
	}

}
