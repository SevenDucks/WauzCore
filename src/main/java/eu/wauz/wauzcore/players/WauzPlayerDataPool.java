package eu.wauz.wauzcore.players;

import java.util.HashMap;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.system.util.WauzMode;

public class WauzPlayerDataPool {
	
	private static HashMap<Player, WauzPlayerData> storage = new HashMap<Player, WauzPlayerData>();

	public static WauzPlayerData getPlayer(Player player) {
		return storage.get(player);
	}

	public static WauzPlayerData regPlayer(Player player) {
		storage.put(player, new WauzPlayerData(storage.size() + 1));
		DamageCalculator.setHealth(player, 20);
		return getPlayer(player);
	}
	
	public static boolean isCharacterSelected(Player player) {
		WauzPlayerData pd = getPlayer(player);
		return !WauzMode.inHub(player) && pd != null && pd.isCharacterSelected();
	}

}
