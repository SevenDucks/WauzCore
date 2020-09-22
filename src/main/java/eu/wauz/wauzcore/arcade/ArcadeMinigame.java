package eu.wauz.wauzcore.arcade;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * A minigame playable in the arcade mode.
 * 
 * @author Wauzmons
 */
public interface ArcadeMinigame {
	
	public String getName();
	
	public String getDescription();
	
	public void startGame(List<Player> players);
	
	public void endGame();
	
	public void handleDamageEvent(EntityDamageEvent event);

}
