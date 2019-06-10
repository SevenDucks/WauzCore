package eu.wauz.wauzcore.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class WauzPlayerGroupPool {
	
	private static HashMap<String, WauzPlayerGroup> storage = new HashMap<String, WauzPlayerGroup>();
	
	public static WauzPlayerGroup getGroup(Player player) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null || StringUtils.isBlank(pd.getGroupUuidString()))
			return null;
		else
			return getGroup(pd.getGroupUuidString());
	}
	
	public static WauzPlayerGroup getGroup(String groupUuidString) {
		return storage.get(groupUuidString);
	}
	
	public static List<WauzPlayerGroup> getGroups() {
		return new ArrayList<>(storage.values());
	}
	
	public static WauzPlayerGroup regGroup(WauzPlayerGroup wauzPlayerGroup) {
		storage.put(wauzPlayerGroup.getGroupUuidString(), wauzPlayerGroup);
		return getGroup(wauzPlayerGroup.getGroupUuidString());
	}
	
	public static void unregGroup(String groupUuidString) {
		storage.remove(groupUuidString);
	}

}
