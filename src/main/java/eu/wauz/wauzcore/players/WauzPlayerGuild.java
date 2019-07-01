package eu.wauz.wauzcore.players;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.data.players.GuildConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.GuildOverviewMenu;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.WauzTeleporter;
import eu.wauz.wauzcore.system.util.WauzMode;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerGuild {
	
	private static Map<String, WauzPlayerGuild> guildMap = new HashMap<>();
	
	private static Map<String, WauzPlayerGuild> guildNameMap = new HashMap<>();
	
	public static void init() {
		for(String guildUuidString : GuildConfigurator.getGuildUuidList()) {
			WauzPlayerGuild guild = new WauzPlayerGuild(guildUuidString);
			guildMap.put(guildUuidString, guild);
			guildNameMap.put(guild.getGuildName(), guild);
		}
	}
	
	public static List<WauzPlayerGuild> getGuilds() {
		List<WauzPlayerGuild> guilds = new ArrayList<>(guildMap.values());
		Collections.shuffle(guilds);
		return guilds;
	}
	
	public static List<String> getGuildNames() {
		List<String> guilds = new ArrayList<>(guildNameMap.keySet());
		Collections.shuffle(guilds);
		return guilds;
	}
	
	public static WauzPlayerGuild getGuild(String uuid) {
		return guildMap.get(uuid);
	}
	
	public static WauzPlayerGuild getGuildByName(String guildName) {
		return guildNameMap.get(guildName);
	}
	
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
						+ new DecimalFormat("#,###").format(PlayerConfigurator.getTokens(leader)) + " Tokens.");
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
				return true;
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
	
	public static boolean isNameTaken(String guildName) {
		return guildNameMap.keySet().contains(guildName);
	}
	
	private String guildUuidString;
	
	private String adminUuidString;
	
	private List<String> officerUuidStrings = new ArrayList<>();
	
	private List<String> memberUuidStrings = new ArrayList<>();
	
	private List<String> applicantUuidStrings = new ArrayList<>();
	
	private String guildName;
	
	private String guildDescription;
	
	private ItemStack guildTabard;
	
	private int upgradeAdditionalSlots;
	
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

	public String getGuildUuidString() {
		return guildUuidString;
	}

	public void setGuildUuidString(String guildUuidString) {
		this.guildUuidString = guildUuidString;
	}

	public String getAdminUuidString() {
		return adminUuidString;
	}

	public void setAdminUuidString(String adminUuidString) {
		this.adminUuidString = adminUuidString;
	}
	
	public boolean isGuildAdmin(OfflinePlayer player) {
		return adminUuidString.equals(player.getUniqueId().toString());
	}

	public List<String> getOfficerUuidStrings() {
		return officerUuidStrings;
	}

	public void setOfficerUuidStrings(List<String> officerUuidStrings) {
		this.officerUuidStrings = officerUuidStrings;
	}
	
	public boolean isGuildOfficer(OfflinePlayer player) {
		return isGuildAdmin(player) || officerUuidStrings.contains(player.getUniqueId().toString());
	}

	public List<String> getMemberUuidStrings() {
		return memberUuidStrings;
	}

	public void setMemberUuidStrings(List<String> memberUuidStrings) {
		this.memberUuidStrings = memberUuidStrings;
	}
	
	public boolean isGuildMember(OfflinePlayer player) {
		return memberUuidStrings.contains(player.getUniqueId().toString());
	}
	
	public void sendMessageToGuildMembers(String message) {
		for(String uuidString : memberUuidStrings) {
			try {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuidString));
				Player player = offlinePlayer.getPlayer();
				if(player != null)
					player.sendMessage(message);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getMemberAmount() {
		return memberUuidStrings.size();
	}
	
	public int getMaxMemberAmount() {
		return 5 + upgradeAdditionalSlots;
	}
	
	public boolean isFull() {
		return getMemberAmount() >= getMaxMemberAmount();
	}
	
	public boolean isEmpty() {
		return getMemberAmount() == 0;
	}
	
	public void addPlayer(OfflinePlayer player) {
		memberUuidStrings.add(player.getUniqueId().toString());
		GuildConfigurator.setGuildMemberUuidStrings(guildUuidString, memberUuidStrings);
		PlayerConfigurator.setGuild(player, guildUuidString);
		sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " joined " + guildName + "!");
	}
	
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
	
	public void demoteToMember(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!officerUuidStrings.contains(memberUuid))
			return;
		
		officerUuidStrings.remove(memberUuid);
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		
		sendMessageToGuildMembers(ChatColor.RED + player.getName() + " demoted " + member.getName() + " to a normal Guild-Member!");
	}
	
	public void promoteToOfficer(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!memberUuidStrings.contains(memberUuid) || officerUuidStrings.contains(memberUuid))
			return;
		
		officerUuidStrings.add(memberUuid);
		GuildConfigurator.setGuildOfficerUuidStrings(guildUuidString, officerUuidStrings);
		
		sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " promoted " + member.getName() + " to a Guild-Officer!");
	}
	
	public void kickMember(Player player, OfflinePlayer member) {
		String memberUuid = member.getUniqueId().toString();
		if(!memberUuidStrings.contains(memberUuid))
			return;
		
		String noGuild = "none";
		PlayerConfigurator.setGuild(member, noGuild);
		
		sendMessageToGuildMembers(ChatColor.RED + player.getName() + " kicked " + member.getName() + " out of the Guild!");
		removePlayer(member);
	}
	
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

	public List<String> getApplicantUuidStrings() {
		updateApplications();
		return applicantUuidStrings;
	}

	public void setApplicantUuidStrings(List<String> applicantUuidStrings) {
		this.applicantUuidStrings = applicantUuidStrings;
		updateApplications();
	}
	
	public void addApplicant(String applicantUuidString) {
		applicantUuidStrings.add(applicantUuidString);
		updateApplications();
	}
	
	public void removeApplicant(String applicantUuidString) {
		applicantUuidStrings.remove(applicantUuidString);
		updateApplications();
	}
	
	public int getApplicationCount() {
		updateApplications();
		return applicantUuidStrings.size();
	}
	
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

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public String getGuildDescription() {
		return guildDescription;
	}
	
	public String[] getWrappedGuildDescription() {
		String doubleParagraph = ChatFormatter.ICON_PGRPH + ChatFormatter.ICON_PGRPH;
		return WordUtils.wrap(guildDescription, 42, doubleParagraph, true).split(doubleParagraph);
	}

	public void setGuildDescription(Player player, String guildDescription) {
		this.guildDescription = guildDescription;
		if(player != null)
			sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " set the guild MotD to: " + guildDescription);
	}

	public ItemStack getGuildTabard() {
		return guildTabard;
	}

	public void setGuildTabard(Player player, ItemStack guildTabard) {
		this.guildTabard = guildTabard;
		if(player != null)
			sendMessageToGuildMembers(ChatColor.GREEN + player.getName() + " changed the guild tabard!");
	}

	public int getUpgradeAdditionalSlots() {
		return upgradeAdditionalSlots;
	}

	public void setUpgradeAdditionalSlots(int upgradeAdditionalSlots) {
		this.upgradeAdditionalSlots = upgradeAdditionalSlots;
	}

}
