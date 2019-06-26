package eu.wauz.wauzcore.system;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.InstanceConfigurator;
import eu.wauz.wauzcore.data.players.GuildConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.events.WauzPlayerEventHomeChange;
import eu.wauz.wauzcore.items.WauzRewards;
import eu.wauz.wauzcore.items.WauzSigns;
import eu.wauz.wauzcore.menu.GroupMenu;
import eu.wauz.wauzcore.menu.PetOverviewMenu;
import eu.wauz.wauzcore.menu.SkillMenu;
import eu.wauz.wauzcore.menu.WauzMenu;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.WauzPlayerGroup;
import eu.wauz.wauzcore.players.WauzPlayerGroupPool;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import eu.wauz.wauzcore.players.calc.DamageCalculator;
import eu.wauz.wauzcore.players.ui.WauzPlayerNotifier;
import eu.wauz.wauzcore.players.ui.WauzPlayerScoreboard;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzCommandExecutor {
	
	public static boolean execute(CommandSender sender, Command cmd, String[] args) {
		if(sender instanceof Player)
			WauzDebugger.log((Player) sender, "Execute Command: " + cmd.getName() + " " + Arrays.asList(args));
		
		try {
			// Teleport to Hub
			if (cmd.getName().equalsIgnoreCase("hub"))
				return WauzTeleporter.hubTeleportManual((Player) sender);
			// Teleport to Spawn
			else if (cmd.getName().equalsIgnoreCase("spawn"))
				return WauzTeleporter.spawnTeleportManual((Player) sender);
			// Teleport to Home
			else if (cmd.getName().equalsIgnoreCase("home"))
				return WauzTeleporter.hearthstoneTeleport((Player) sender);
			// Set Home Location
			else if (cmd.getName().equalsIgnoreCase("sethome"))
				return new WauzPlayerEventHomeChange((Player) sender, true).execute((Player) sender);
			// Open a Menu
			else if (cmd.getName().equalsIgnoreCase("menu"))
				return cmdOpenMenu(sender, args);
			// Open the Group Menu
			else if (cmd.getName().equalsIgnoreCase("group"))
				return cmdOpenGroupMenu(sender, args);
			// Send Message in Group Chat
			else if (cmd.getName().equalsIgnoreCase("grp"))
				return ChatFormatter.group((Player) sender, StringUtils.join(args, " "));
			// Set the Group Description
			else if (cmd.getName().equalsIgnoreCase("desc"))
				return cmdSetGroupDescription(sender, args);
			// Create a Guild
			else if (cmd.getName().equalsIgnoreCase("guild"))
				return WauzPlayerGuild.createGuild((Player) sender, StringUtils.join(args, " "));
			// Apply for a Guild
			else if (cmd.getName().equalsIgnoreCase("apply"))
				return WauzPlayerGuild.applyForGuild((Player) sender, StringUtils.join(args, " "));
			// Send Message in Guild Chat
			else if (cmd.getName().equalsIgnoreCase("gld"))
				return ChatFormatter.guild((Player) sender, StringUtils.join(args, " "));
			// Set the Guild Message of the Day
			else if (cmd.getName().equalsIgnoreCase("motd"))
				return cmdSetGuildDescription(sender, args);
			// Prints a Tip in Chat
			else if (cmd.getName().equalsIgnoreCase("tip"))
				return WauzPlayerNotifier.execute((Player) sender);
			// Print System Analytics
			else if (cmd.getName().equalsIgnoreCase("wzSystem"))
				return WauzCore.printSystemAnalytics(sender);
			// Heal and Feed Player
			else if (cmd.getName().equalsIgnoreCase("wzHeal"))
				return cmdHealAndFeed(sender, args);
			// Gain Experience Points
			else if (cmd.getName().equalsIgnoreCase("wzExp"))
				return cmdEarnExperiencePoints(sender, args);
			// Enter Dev Instance	
			else if(cmd.getName().equalsIgnoreCase("wzEnter.dev"))
				return WauzTeleporter.enterInstanceTeleportBetaDev((Player) sender, args[0].replace("_", " "));
			// Enter Instance
			else if(cmd.getName().equalsIgnoreCase("wzEnter"))
				return WauzTeleporter.enterInstanceTeleportDev((Player) sender, args[0].replace("_", " "));	
			// Leave Instance		
			else if(cmd.getName().equalsIgnoreCase("wzLeave"))
				return WauzTeleporter.exitInstanceTeleportDev((Player) sender);
			// Change Dungeon Key Status
			else if(cmd.getName().equalsIgnoreCase("wzKey"))
				return cmdChangeDungeonKeyStatus(sender, args);
			// Toggle Debug Mode
			else if (cmd.getName().equalsIgnoreCase("wzDebug"))			
				return WauzDebugger.toggleDebugMode((Player) sender);
			// Toggle Magic Debug Mode
			else if (cmd.getName().equalsIgnoreCase("wzDebug.magic"))			
				return WauzDebugger.toggleMagicDebugMode((Player) sender);
			// Toggle Crafting Debug Mode
			else if (cmd.getName().equalsIgnoreCase("wzDebug.crafting"))			
				return WauzDebugger.toggleCraftingDebugMode((Player) sender);
			// Toggle Building Debug Mode
			else if (cmd.getName().equalsIgnoreCase("wzDebug.building"))			
				return WauzDebugger.toggleBuildingDebugMode((Player) sender);
			// Toggle Dungeon Items Debug Mode
			else if (cmd.getName().equalsIgnoreCase("wzDebug.ditems"))			
				return WauzDebugger.toggleDungeonItemsDebugMode((Player) sender);
			// Registers Pet to Owner
			else if(cmd.getName().equalsIgnoreCase("wzRegPet"))
				return PetOverviewMenu.regPet(WauzCore.getOnlinePlayer(args[0]), args[1]);
			// Get Pet from String
			else if(cmd.getName().equalsIgnoreCase("wzGetPet"))
				return cmdGetPetFromString(sender, args);
			// Phantom-Travel to Location
			else if(cmd.getName().equalsIgnoreCase("wzTravel"))
				return cmdTravelToLocation(sender, args);
			// Event-Travel to Location per UUID
			else if(cmd.getName().equalsIgnoreCase("wzTravelEvent"))
				return cmdTravelToEventLocation(sender, args);
			// Execute Skill
			else if(cmd.getName().equalsIgnoreCase("wzSkill"))
				return WauzPlayerSkillExecutor.execute((Player) sender, null, args[0].replace("_", " "));
			// Get Weapon with Skillgem
			else if(cmd.getName().equalsIgnoreCase("wzSkill.weapon"))
				return WauzDebugger.getSkillgemWeapon((Player) sender, args[0].replace("_", " "));
			else
				return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	private static boolean cmdOpenMenu(CommandSender sender, String[] args) {
		if(args.length < 1)
			return false;
		
		try {
			Player player = (Player) sender;
			if(WauzMode.isMMORPG(player) && !WauzMode.inHub(player)) {
				if(args[0].equals("main")) {
					WauzMenu.open((Player) sender);
					return true;
				}
				else if(args[0].equals("skills")) {
					SkillMenu.open((Player) sender);
					return true;
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't open that menu here!");
				return true;
			}
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdOpenGroupMenu(CommandSender sender, String[] args) {
		try {
			Player player = (Player) sender;
			if((WauzMode.isMMORPG(player) || WauzMode.isSurvival(player)) && !WauzMode.inHub(player)) {
				GroupMenu.open(player);
				return true;
			}
			else {
				player.sendMessage(ChatColor.RED + "You can't open that menu here!");
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdSetGroupDescription(CommandSender sender, String[] args) {
		try {
			Player player = (Player) sender;
			String message = StringUtils.join(args, " ");
			
			WauzPlayerGroup playerGroup = WauzPlayerGroupPool.getGroup(player);
			if(playerGroup == null) {
				player.sendMessage(ChatColor.RED + "You are not in a group!");
				return true;
			}
			if(!playerGroup.isGroupAdmin(player)) {
				player.sendMessage(ChatColor.RED + "You are not the group-leader!");
				return true;
			}
			if(StringUtils.isBlank(message)) {
				player.sendMessage(ChatColor.RED + "Please specify the text to set!");
				return false;
			}
			else {
				playerGroup.setGroupDescription(player, message);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdSetGuildDescription(CommandSender sender, String[] args) {
		try {
			Player player = (Player) sender;
			String message = StringUtils.join(args, " ");
			
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild == null) {
				player.sendMessage(ChatColor.RED + "You are not in a guild!");
				return true;
			}
			if(!playerGuild.isGuildOfficer(player)) {
				player.sendMessage(ChatColor.RED + "You are no guild-officer!");
				return true;
			}
			if(StringUtils.isBlank(message)) {
				player.sendMessage(ChatColor.RED + "Please specify the text to set!");
				return false;
			}
			else {
				GuildConfigurator.setGuildDescription(playerGuild.getGuildUuidString(), message);
				playerGuild.setGuildDescription(player, message);
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdHealAndFeed(CommandSender sender, String[] args) {		
		try {
			Player player = args.length == 0 ? (Player) sender : WauzCore.getOnlinePlayer(args[0]);
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
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdEarnExperiencePoints(CommandSender sender, String[] args) {
		if(args.length < 3)
			return false;
		
		try {
			if(args.length < 4)
				WauzRewards.level(WauzCore.getOnlinePlayer(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]));
			else {
				String[] splitLocation = args[3].split(";");
				Double x = Double.parseDouble(splitLocation[1]);
				Double y = Double.parseDouble(splitLocation[2]);
				Double z = Double.parseDouble(splitLocation[3]);
				Location location = new Location(Bukkit.getWorld(splitLocation[0]), x, y, z);
				WauzRewards.level(WauzCore.getOnlinePlayer(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]), location);
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdChangeDungeonKeyStatus(CommandSender sender, String[] args) {
		if(args.length != 3)
			return false;
		
		try {
			World world = args[0].equals("this") ? ((Player) sender).getWorld() : Bukkit.getWorld(args[0]);
			String keyId = args[1];
			String keyStatus;
			
			switch (args[2]) {
			case "1":
				keyStatus = InstanceConfigurator.KEY_STATUS_OBTAINED;
				break;
			case "2":
				keyStatus = InstanceConfigurator.KEY_STATUS_USED;
				break;
			default:
				keyStatus = InstanceConfigurator.KEY_STATUS_UNOBTAINED;
				break;
			}
			
			InstanceConfigurator.setInstanceWorldKeyStatus(world, keyId, keyStatus);
			for(Player player : world.getPlayers()) {
				WauzPlayerScoreboard.scheduleScoreboard(player);
				player.sendMessage(ChatColor.GREEN + "You obtained the Key \"" + ChatColor.DARK_AQUA + keyId + ChatColor.GREEN + "\"!");
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdGetPetFromString(CommandSender sender, String[] args) {
		if(args.length < 1)
			return false;
		
		try {
			Player player = null;
			String type = null;
			
			if(args.length > 1) {
				player = WauzCore.getOnlinePlayer(args[0]);
				type = args[1];
			}
			else {
				player = (Player) sender;
				type = args[0];
			}
			PetOverviewMenu.addPet(player, null, type);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean cmdTravelToLocation(CommandSender sender, String[] args) {
		if(args.length < 3)
			return false;
		
		try {
			Player player = (Player) sender;
			WauzSigns.startTravelling(player, args[0],  args[1], args[2]);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Map<String, Location> eventTravelMap = new HashMap<String, Location>();
	
	private static boolean cmdTravelToEventLocation(CommandSender sender, String[] args) {
		if(args.length < 1)
			return false;
		
		try {
			Player player = (Player) sender;
			WauzTeleporter.eventTeleport(player, eventTravelMap.get(args[0]));
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
