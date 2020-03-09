package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.players.GuildConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.util.Formatters;
import eu.wauz.wauzcore.system.util.UnicodeUtils;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * A player guild loaded from a config file.
 * Also contains static methods for managing all guilds.
 * 
 * @author Wauzmons
 */
public class WauzPlayerGuild {
	
	/**
	 * All guilds by uuid.
	 */
	private static Map<String, WauzPlayerGuild> guildMap = new HashMap<>();
	
	/**
	 * All guilds by name.
	 */
	private static Map<String, WauzPlayerGuild> guildNameMap = new HashMap<>();
	
	/**
	 * Loads all guilds from the config files.
	 * 
	 * @see GuildConfigurator#getGuildUuidList()
	 */
	public static void init() {
		for(String guildUuidString : GuildConfigurator.getGuildUuidList()) {
			WauzPlayerGuild guild = new WauzPlayerGuild(guildUuidString);
			guildMap.put(guildUuidString, guild);
			guildNameMap.put(guild.getGuildName(), guild);
		}
		
		WauzCore.getInstance().getLogger().info("Loaded " + guildMap.size() + " Guilds!");
	}
	
	/**
	 * @return A shuffled list of all guilds.
	 */
	public static List<WauzPlayerGuild> getGuilds() {
		List<WauzPlayerGuild> guilds = new ArrayList<>(guildMap.values());
		Collections.shuffle(guilds);
		return guilds;
	}
	
	/**
	 * @return A shuffled list of all guild names.
	 */
	public static List<String> getGuildNames() {
		List<String> guilds = new ArrayList<>(guildNameMap.keySet());
		Collections.shuffle(guilds);
		return guilds;
	}
	
	/**
	 * Finds a guild by uuid.
	 * 
	 * @param uuid The uuid of the guild.
	 * 
	 * @return The requested guild.
	 */
	public static WauzPlayerGuild getGuild(String uuid) {
		return guildMap.get(uuid);
	}
	
	/**
	 * Finds a guild by name.
	 * 
	 * @param guildName The name of the guild.
	 * 
	 * @return The requested guild.
	 */
	public static WauzPlayerGuild getGuildByName(String guildName) {
		return guildNameMap.get(guildName);
	}
	
	/**
	 * Creates a new guild.
	 * The leader must be in a MMORPG world and cannot already be in a guild.
	 * 300 Tokens are required for the guild creation.
	 * 
	 * @param leader The leader of the new guild.
	 * @param guildName A guild name, that does not already exists, between 1 and 42 chars.
	 * 
	 * @return If the guild creation command had valid syntax.
	 */
	public static boolean createGuild(Player leader, String guildName) {
		try {
			if(!WauzMode.isMMORPG(leader) || WauzMode.inHub(leader)) {
				leader.sendMessage(ChatColor.RED + "You can't do that here!");
				return true;
			}
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(leader);
			if(playerGuild != null) {
				leader.sendMessage(ChatColor.RED + "You are already in a guild!");
				return true;
			}
			long tokens = PlayerConfigurator.getTokens(leader);
			if(tokens < 300) {
				leader.sendMessage(ChatColor.RED + "This requires 300 Tokens! You have: "
						+ Formatters.INT.format(PlayerConfigurator.getTokens(leader)) + " Tokens.");
				return true;
			}
			if(StringUtils.isBlank(guildName)) {
				leader.sendMessage(ChatColor.RED + "Please specify a guild-name!");
				return false;
			}
			if(guildName.length() > 42) {
				leader.sendMessage(ChatColor.RED + "This guild-name is too long!");
				return true;
			}
			if(isNameTaken(guildName)) {
				leader.sendMessage(ChatColor.RED + "This guild-name is already taken!");
				return true;
			}
			
			String guildUuidString = UUID.randomUUID().toString();
			String adminUuidString = leader.getUniqueId().toString();
			
			WauzPlayerGuild guild = new WauzPlayerGuild(guildUuidString, adminUuidString, guildName);
			guildMap.put(guildUuidString, guild);
			guildNameMap.put(guild.getGuildName(), guild);
			
			PlayerConfigurator.setTokens(leader, tokens - 300);
			PlayerConfigurator.setGuild(leader, guildUuidString);
			GuildOverviewMenu.open(leader);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends a guild application.
	 * The player must be in a MMORPG world and cannot already be in a guild.
	 * All online guild members will receive a notification.
	 * 
	 * @param player The player that wants to join a guild.
	 * @param guildName The name of the guild.
	 * 
	 * @return If the guild apply command had valid syntax.
	 */
	public static boolean applyForGuild(Player player, String guildName) {
		try {
			if(!WauzMode.isMMORPG(player) || WauzMode.inHub(player)) {
				player.sendMessage(ChatColor.RED + "You can't do that here!");
				return true;
			}
			WauzPlayerGuild playerGuild = PlayerConfigurator.getGuild(player);
			if(playerGuild != null) {
				player.sendMessage(ChatColor.RED + "You are already in a guild!");
				return true;
			}
			if(StringUtils.isBlank(guildName)) {
				player.sendMessage(ChatColor.RED + "Please specify a guild-name!");
				return false;
			}
			playerGuild = getGuildByName(guildName);
			if(playerGuild == null) {
				player.sendMessage(ChatColor.RED + "This guild-name is not valid!");
				return false;
			}
			String applicantUuidString = player.getUniqueId().toString();
			if(playerGuild.getApplicantUuidStrings().contains(applicantUuidString)) {
				player.sendMessage(ChatColor.RED + "You already applied for that guild!");
				return true;
			}
			
			playerGuild.addApplicant(applicantUuidString);
			playerGuild.sendMessageToGuildMembers(ChatColor.YELLOW + "New application for " + playerGuild.getGuildName()
					+ " by " + player.getName() + "!");
			player.sendMessage(ChatColor.GREEN + "You applied for " + playerGuild.getGuildName() + "!");
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if a guild name is already taken.
	 * 
	 * @param guildName The name of the guild.
	 * 
	 * @return If it is taken.
	 */
	public static boolean isNameTaken(String guildName) {
		return guildNameMap.keySet().contains(guildName);
	}
	
	/**
	 * The uuid of the guild.
	 */
	private String guildUuidString;
	
	/**
	 * The uuid of the guild leader.
	 */
	private String adminUuidString;
	
	/**
	 * The uuids of the guild officers.
	 */
	private List<String> officerUuidStrings = new ArrayList<>();
	
	/**
	 * The uuids of all guild members.
	 */
	private List<String> memberUuidStrings = new ArrayList<>();
	
	/**
	 * The uuids of guild applicants.
	 */
	private List<String> applicantUuidStrings = new ArrayList<>();
	
	/**
	 * The name of the guild.
	 */
	private String guildName;
	
	/**
	 * The description of the guild.
	 */
	private String guildDescription;
	
	/**
	 * The tabard / banner of the guild.
	 */
	private ItemStack guildTabard;
	
	/**
	 * How many additional slots are available.
	 */
	private int upgradeAdditionalSlots;
	
	/**
	 * Creates a guild object from a config file with given uuid.
	 * 
	 * @param guildUuidString The uuid of the guild.
	 */
	public WauzPlayerGuild(String guildUuidString) {
		this.guildUuidString = guildUuidString;
		this.guildName = GuildConfigurator.getGuildName(guildUuidString);
		this.guildDescription = GuildConfigurator.getGuildDescription(guildUuidString);
		this.guildTabard = GuildConfigurator.getGuildTabard(guildUuidString);
		this.adminUuidString = GuildConfigurator.getGuildLeaderUuidString(guildUuidString);
		this.officerUuidStrings = GuildConfigurator.getGuildOfficerUuidStrings(guildUuidString);
		this.memberUuidStrings = GuildConfigurator.getGuildMemberUuidStrings(guildUuidString);
		this.applicantUuidStrings = GuildConfigurator.getGuildApplicantUuidStrings(guildUuidString);
		
		this.upgradeAdditionalSlots = GuildConfigurator.getUpgradeAdditionalSlots(guildUuidString);
	}
	
	/**
	 * Creates an entirely new guild and saves it to a config file.
	 * 
	 * @param guildUuidString The uuid of the guild.
	 * @param adminUuidString The uuid of the guild leader.
	 * @param guildName The name of the guild.
	 */
	public WauzPlayerGuild(String guildUuidString, String adminUuidString, String guildName) {
		this.guildUuidString = guildUuidString;
		this.adminUuidString = adminUuidString;
		this.memberUuidStrings.add(adminUuidString);
		
		this.guildName = guildName;
		this.guildDescription = "No Message of the Day";
		this.guildTabard = new ItemStack(Material.WHITE_BANNER);
		
		GuildConfigurator.setGuildName(guildUuidString, guildName);
		GuildConfigurator.setGuildDescription(guildUuidString, guildDescription);
		GuildConfigurator.setGuildTabard(guildUuidString, guildTabard);
		GuildConfigurator.setGuildLeaderUuidString(guildUuidString, adminUuidString);
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		GuildConfigurator.setGuildMemberUuidStrings(guildUuidString, memberUuidStrings);
		GuildConfigurator.setGuildApplicantUuidStrings(guildUuidString, applicantUuidStrings);
		
		GuildConfigurator.setUpgradeAdditionalSlots(guildUuidString, 0);
	}

	/**
	 * @return The uuid of the guild.
	 */
	public String getGuildUuidString() {
		return guildUuidString;
	}

	/**
	 * @param guildUuidString The new uuid of the guild.
	 */
	public void setGuildUuidString(String guildUuidString) {
		this.guildUuidString = guildUuidString;
	}

	/**
	 * @return The uuid of the guild leader.
	 */
	public String getAdminUuidString() {
		return adminUuidString;
	}

	/**
	 * @param adminUuidString The new uuid of the guild leader.
	 */
	public void setAdminUuidString(String adminUuidString) {
		this.adminUuidString = adminUuidString;
	}
	
	/**
	 * Checks if a player is the guild leader.
	 * 
	 * @param player A member of the guild.
	 * 
	 * @return If they are the leader.
	 */
	public boolean isGuildAdmin(OfflinePlayer player) {
		return adminUuidString.equals(player.getUniqueId().toString());
	}

	/**
	 * @return The uuids of the guild officers.
	 */
	public List<String> getOfficerUuidStrings() {
		return officerUuidStrings;
	}

	/**
	 * @param officerUuidStrings The new uuids of the guild officers.
	 */
	public void setOfficerUuidStrings(List<String> officerUuidStrings) {
		this.officerUuidStrings = officerUuidStrings;
	}
	
	/**
	 * Checks if a player is a guild officer.
	 * 
	 * @param player A member of the guild.
	 * 
	 * @return If they are an officer.
	 */
	public boolean isGuildOfficer(OfflinePlayer player) {
		return isGuildAdmin(player) || officerUuidStrings.contains(player.getUniqueId().toString());
	}

	/**
	 * @return The uuids of the normal guild members.
	 */
	public List<String> getMemberUuidStrings() {
		return memberUuidStrings;
	}

	/**
	 * @param memberUuidStrings The new uuids of the normal guild members.
	 */
	public void setMemberUuidStrings(List<String> memberUuidStrings) {
		this.memberUuidStrings = memberUuidStrings;
	}
	
	/**
	 * Checks if a player is a guild member.
	 * 
	 * @param player A potential member of the guild.
	 * 
	 * @return If they are a member.
	 */
	public boolean isGuildMember(OfflinePlayer player) {
		return memberUuidStrings.contains(player.getUniqueId().toString());
	}
	
	/**
	 * Sends a message to all online guild members.
	 * 
	 * @param message The content of the message.
	 */
	public void sendMessageToGuildMembers(String message) {
		for(String uuidString : memberUuidStrings) {
			try {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuidString));
				Player player = offlinePlayer.getPlayer();
				if(player != null) {
					player.sendMessage(message);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return The current amount of guild members.
	 */
	public int getMemberAmount() {
		return memberUuidStrings.size();
	}
	
	/**
	 * @return The maximum amount of guild members.
	 */
	public int getMaxMemberAmount() {
		return 5 + upgradeAdditionalSlots;
	}
	
	/**
	 * @return If the guild is full.
	 */
	public boolean isFull() {
		return getMemberAmount() >= getMaxMemberAmount();
	}
	
	/**
	 * @return If the guild is empty.
	 */
	public boolean isEmpty() {
		return getMemberAmount() == 0;
	}
	
	/**
	 * Adds a player to the guild and announces it to all online members.
	 * 
	 * @param player The player to add.
	 */
	public void addPlayer(OfflinePlayer player) {
		memberUuidStrings.add(player.getUniqueId().toString());
		GuildConfigurator.setGuildMemberUuidStrings(guildUuidString, memberUuidStrings);
		PlayerConfigurator.setGuild(player, guildUuidString);
		sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " joined " + guildName + "!");
	}
	
	/**
	 * Removes a player from the guild and announces it to all online members.
	 * May cause the change of the leader or the deletion of the guild.
	 * 
	 * @param player The player to remove.
	 */
	public void removePlayer(OfflinePlayer player) {
		String memberUuid = player.getUniqueId().toString();
		
		officerUuidStrings.remove(memberUuid);
		memberUuidStrings.remove(memberUuid);
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		GuildConfigurator.setGuildMemberUuidStrings(guildUuidString, memberUuidStrings);
		
		if(player.isOnline()) {
			player.getPlayer().sendMessage(ChatColor.RED + "You left the Guild!");
			if(player.getPlayer().getWorld().getName().equals("WzInstance_MMORPG_" + guildUuidString)) {
				WauzTeleporter.exitInstanceTeleportManual(player.getPlayer());
			}
		}
		sendMessageToGuildMembers(ChatColor.RED + player.getName() + " left your Guild!");
		
		if(isGuildAdmin(player)) {
			if(isEmpty()) {
				GuildConfigurator.deleteGuild(guildUuidString);
			}
			else {
				String leaderUuid = !officerUuidStrings.isEmpty() ? officerUuidStrings.get(0) : memberUuidStrings.get(0);
				GuildConfigurator.setGuildLeaderUuidString(guildUuidString, leaderUuid);
				adminUuidString = leaderUuid;
				OfflinePlayer leader = Bukkit.getOfflinePlayer(UUID.fromString(leaderUuid));
				if(leader != null) {
					sendMessageToGuildMembers(ChatColor.GREEN + leader.getName() + " is now the Guild-Leader!");
				}
			}
		}
	}
	
	/**
	 * Demotes an officer to a member and announces it to all online members.
	 * 
	 * @param player The player that is demoting.
	 * @param member The member to demote.
	 */
	public void demoteToMember(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!officerUuidStrings.contains(memberUuid)) {
			return;
		}
		
		officerUuidStrings.remove(memberUuid);
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		
		sendMessageToGuildMembers(ChatColor.RED + player.getName() + " demoted " + member.getName() + " to a normal Guild-Member!");
	}
	
	/**
	 * Promotes a member to an officer and announces it to all online members.
	 * 
	 * @param player The player that is promoting.
	 * @param member The member to promote.
	 */
	public void promoteToOfficer(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!memberUuidStrings.contains(memberUuid) || officerUuidStrings.contains(memberUuid)) {
			return;
		}
		
		officerUuidStrings.add(memberUuid);
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		
		sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " promoted " + member.getName() + " to a Guild-Officer!");
	}
	
	/**
	 * Kicks a member out of the guild and announces it to all online members.
	 * 
	 * @param player The player that is kicking.
	 * @param member The player that gets kicked.
	 */
	public void kickMember(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!memberUuidStrings.contains(memberUuid)) {
			return;
		}
		
		String noGuild = "none";
		PlayerConfigurator.setGuild(member, noGuild);
		
		sendMessageToGuildMembers(ChatColor.RED + player.getName() + " kicked " + member.getName() + " out of the Guild!");
		removePlayer(member);
	}
	
	/**
	 * Promotes an officer to the leader and announces it to all online members.
	 * 
	 * @param player The player that is promoting.
	 * @param member The member to promote.
	 */
	public void promoteToLeader(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!officerUuidStrings.contains(memberUuid) || officerUuidStrings.contains(player.getUniqueId().toString()))
			return;
		
		officerUuidStrings.remove(memberUuid);
		officerUuidStrings.add(player.getUniqueId().toString());
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		adminUuidString = memberUuid;
		GuildConfigurator.setGuildLeaderUuidString(guildUuidString, adminUuidString);
		
		sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " crowned " + member.getName() + " to the new Guild-Leader!");
	}

	/**
	 * @return The uuids of guild applicants.
	 * 
	 * @see WauzPlayerGuild#updateApplications()
	 */
	public List<String> getApplicantUuidStrings() {
		updateApplications();
		return applicantUuidStrings;
	}

	/**
	 * @param applicantUuidStrings The new uuids of guild applicants.
	 * 
	 * @see WauzPlayerGuild#updateApplications()
	 */
	public void setApplicantUuidStrings(List<String> applicantUuidStrings) {
		this.applicantUuidStrings = applicantUuidStrings;
		updateApplications();
	}
	
	/**
	 * Adds an guild applicant to the list.
	 * 
	 * @param applicantUuidString The uuid of the applicant to add.
	 * 
	 * @see WauzPlayerGuild#updateApplications()
	 */
	public void addApplicant(String applicantUuidString) {
		applicantUuidStrings.add(applicantUuidString);
		updateApplications();
	}
	
	/**
	 * Removes an guild applicant from the list.
	 * 
	 * @param applicantUuidString The uuid of the applicant to remove.
	 * 
	 * @see WauzPlayerGuild#updateApplications()
	 */
	public void removeApplicant(String applicantUuidString) {
		applicantUuidStrings.remove(applicantUuidString);
		updateApplications();
	}
	
	/**
	 * Gets the count of all guild applicants.
	 * 
	 * @return The count of applicants
	 * 
	 * @see WauzPlayerGuild#updateApplications()
	 */
	public int getApplicationCount() {
		updateApplications();
		return applicantUuidStrings.size();
	}
	
	/**
	 * Checks if all applications are still valid and updates the config correspondingly.
	 */
	public void updateApplications() {
		List<String> applicants = new ArrayList<>();
		applicants.addAll(applicantUuidStrings);
		
		for(String applicantUuidString : applicants) {
			try {
				OfflinePlayer applicant = Bukkit.getOfflinePlayer(UUID.fromString(applicantUuidString));
				if(applicant == null || PlayerConfigurator.getGuild(applicant) != null) {
					applicantUuidStrings.remove(applicantUuidString);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				applicantUuidStrings.remove(applicantUuidString);
			}
		}
		
		GuildConfigurator.setGuildApplicantUuidStrings(guildUuidString, applicantUuidStrings);
	}

	/**
	 * @return The name of the guild.
	 */
	public String getGuildName() {
		return guildName;
	}

	/**
	 * @param guildName The new name of the guild.
	 */
	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	/**
	 * @return The description of the guild.
	 */
	public String getGuildDescription() {
		return guildDescription;
	}
	
	/**
	 * @return The wrapped description of the guild.
	 */
	public String[] getWrappedGuildDescription() {
		String doubleParagraph = UnicodeUtils.ICON_PARAGRAPH + UnicodeUtils.ICON_PARAGRAPH;
		return WordUtils.wrap(guildDescription, 42, doubleParagraph, true).split(doubleParagraph);
	}

	/**
	 * @param player The player who updated the description.
	 * @param guildDescription The new description of the guild.
	 */
	public void setGuildDescription(Player player, String guildDescription) {
		this.guildDescription = guildDescription;
		if(player != null) {
			sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " set the guild MotD to: " + guildDescription);
		}
	}

	/**
	 * @return The tabard / banner of the guild.
	 */
	public ItemStack getGuildTabard() {
		return guildTabard;
	}

	/**
	 * @param player The player who updated the tabard.
	 * @param guildTabard The new tabard / banner of the guild.
	 */
	public void setGuildTabard(Player player, ItemStack guildTabard) {
		this.guildTabard = guildTabard;
		if(player != null) {
			sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " changed the guild tabard!");
		}
	}

	/**
	 * @return How many additional slots are available.
	 */
	public int getUpgradeAdditionalSlots() {
		return upgradeAdditionalSlots;
	}

	/**
	 * @param upgradeAdditionalSlots How many additional slots are now available.
	 */
	public void setUpgradeAdditionalSlots(int upgradeAdditionalSlots) {
		this.upgradeAdditionalSlots = upgradeAdditionalSlots;
	}

}
