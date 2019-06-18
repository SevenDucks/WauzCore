package eu.wauz.wauzcore.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerGuild;
import net.md_5.bungee.api.ChatColor;

public class GuildApplicationMenu implements WauzInventory {
	
	private static DecimalFormat formatter = new DecimalFormat("#,###");
	
	public static void open(Player player) {
		WauzPlayerGuild pg = PlayerConfigurator.getGuild(player);
		if(pg == null) {
			player.sendMessage(ChatColor.RED + "You are not in a guild!");
			player.closeInventory();
			return;
		}
		if(!pg.isGuildOfficer(player)) {
			player.sendMessage(ChatColor.RED + "You are no guild-officer!");
			player.closeInventory();
			return;
		}
		
		WauzInventoryHolder holder = new WauzInventoryHolder(new GuildApplicationMenu());
		Inventory menu = Bukkit.createInventory(holder, 27, ChatColor.BLACK + "" + ChatColor.BOLD + "Guild Applications");
		
		List<OfflinePlayer> applicants = pg.getApplicantUuidStrings().stream()
				.map(uuid -> Bukkit.getOfflinePlayer(UUID.fromString(uuid)))
				.filter(offlinePlayer -> offlinePlayer != null)
				.collect(Collectors.toList());
		
		for(int slot = 0; slot < 27 && slot < applicants.size(); slot++) {
			OfflinePlayer applicant = applicants.get(slot);
			ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta sm = (SkullMeta) skull.getItemMeta();
			sm.setDisplayName(ChatColor.LIGHT_PURPLE + applicant.getName() + " [Applicant]");
			sm.setOwningPlayer(applicant);
			List<String> slores = new ArrayList<String>();
			slores.add(ChatColor.GRAY + "Left Click to Accept, "
					+ "Free Slots: " + (pg.getMaxMemberAmount() - pg.getMemberAmount()));
			slores.add(ChatColor.GRAY + "Right Click to Reject");
			slores.add("");
			slores.add(ChatColor.GRAY + "Last Online: " + (applicant.isOnline()
					? ChatColor.GREEN + "Now"
					: ChatColor.BLUE + PlayerConfigurator.getLastPlayed(player) + " ago"));
			slores.add("");
			slores.add(ChatColor.DARK_PURPLE + "MMORPG Characters: ");
			for(int character = 1; character <= 3; character++) {
				if(PlayerConfigurator.doesCharacterExist(applicant, character)) {
					slores.add(ChatColor.WHITE 
							+ PlayerConfigurator.getRaceString(applicant, character) + ", "
							+ PlayerConfigurator.getWorldString(applicant, character) + ", "
							+ PlayerConfigurator.getLevelString(applicant, character));
				}
				else {
					slores.add(ChatColor.GRAY + "Empty");
				}
			}
			slores.add("");
			slores.add(ChatColor.DARK_PURPLE + "Survival Score: ");
			slores.add(ChatColor.WHITE + formatter.format(PlayerConfigurator.getSurvivalScore(applicant)));
			sm.setLore(slores);
			skull.setItemMeta(sm);
			menu.setItem(slot, skull);
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		WauzPlayerGuild guild = PlayerConfigurator.getGuild(player);
		
		if(clicked == null || guild == null)
			return;
		
		else if(clicked.getType().equals(Material.PLAYER_HEAD)) {
			SkullMeta sm = (SkullMeta) clicked.getItemMeta();
			OfflinePlayer applicant = sm.getOwningPlayer();
			if(applicant == null)
				return;
			
			if(event.getClick().toString().contains("RIGHT")) {
				if(applicant.isOnline()) {
					applicant.getPlayer().sendMessage(org.bukkit.ChatColor.RED + "Your application for " + guild.getGuildName() + " was rejected.");
				}
				guild.removeApplicant(applicant.getUniqueId().toString());
			}
			else {
				if(!guild.isFull()) {
					guild.removeApplicant(applicant.getUniqueId().toString());
					guild.addPlayer(applicant);
				}
			}
			open(player);
		}
	}

}
