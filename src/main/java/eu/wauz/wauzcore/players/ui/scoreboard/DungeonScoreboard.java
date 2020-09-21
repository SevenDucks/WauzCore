package eu.wauz.wauzcore.players.ui.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.system.instances.InstanceMobArena;
import eu.wauz.wauzcore.system.instances.WauzActiveInstance;
import eu.wauz.wauzcore.system.instances.WauzActiveInstancePool;
import eu.wauz.wauzcore.system.instances.WauzInstanceType;
import eu.wauz.wauzcore.system.util.UnicodeUtils;

/**
 * A scoreboard to show the name and status of their instance to a player.
 * 
 * @author Wauzmons
 */
public class DungeonScoreboard extends BaseScoreboard {
	
	/**
	 * Initializes the scoreboard and fills it with data.
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	public DungeonScoreboard(Player player) {
		super(player);
	}

	/**
	 * @return The text to show as scoreboard title.
	 */
	@Override
	public String getTitleText() {
		return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Dungeon" + ChatColor.RESET;
	}

	/**
	 * Fills the scoreboard with entries for the given player
	 * 
	 * @param The player who should receive the scoreboard.
	 */
	@Override
	public void fillScoreboard(Player player) {
		WauzActiveInstance instance = WauzActiveInstancePool.getInstance(player);
		rowStrings.add("");
		rowStrings.add(ChatColor.WHITE + instance.getInstanceName());
		
		WauzInstanceType instanceType = instance.getType();
		if(instanceType.equals(WauzInstanceType.KEYS)) {
			rowStrings.add(" ");
			rowStrings.add(ChatColor.DARK_AQUA + UnicodeUtils.ICON_BULLET + " Dungeon Keys");
			for(String keyId : instance.getKeyIds()) {
				rowStrings.add(ChatColor.WHITE + "  > " + keyId + ": " + instance.getKeyStatus(keyId).toString());
			}
		}
		else if(instanceType.equals(WauzInstanceType.ARENA)) {
			rowStrings.add(" ");
			rowStrings.add(ChatColor.DARK_AQUA + UnicodeUtils.ICON_BULLET + " Arena Progress");
			InstanceMobArena arena = instance.getMobArena();
			String currentWave = ChatColor.GOLD + "" + arena.getCurrentWave();
			String maximumWave = ChatColor.GOLD + "" + arena.getMaximumWave();
			rowStrings.add(ChatColor.WHITE + "  > Wave: " + currentWave + ChatColor.WHITE + " / " + maximumWave);
			rowStrings.add(ChatColor.WHITE + "  > Remaining Enemies: " + ChatColor.RED + arena.getMobsLeft());
			long medals = PlayerCollectionConfigurator.getCharacterMedals(player);
			rowStrings.add(ChatColor.WHITE + "  > Earned Medals: " + ChatColor.AQUA + medals);
		}
	}

}
