package eu.wauz.wauzcore.events;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;

public interface WauzPlayerEvent {
	
	static WauzCore core = WauzCore.getInstance();
	
	public boolean execute(Player player);

}
