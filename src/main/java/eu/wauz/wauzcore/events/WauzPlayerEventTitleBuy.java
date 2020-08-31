package eu.wauz.wauzcore.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.players.PlayerCollectionConfigurator;
import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.menu.social.TitleMenu;
import eu.wauz.wauzcore.system.WauzTitle;

/**
 * An event to let a player buy a new chat title.
 * 
 * @author Wauzmons
 */
public class WauzPlayerEventTitleBuy implements WauzPlayerEvent {
	
	/**
	 * The name of the title to buy.
	 */
	private WauzTitle title;

	/**
	 * Creates an event to buy a title.
	 * 
	 * @param title The title to buy.
	 */
	public WauzPlayerEventTitleBuy(WauzTitle title) {
		this.title = title;
	}
	
	/**
	 * Executes the event for the given player.
	 * Adds the title to the player's title list and selects it automatically.
	 * Re-opens the title menu, after subtracting the cost from the player's balance.
	 * 
	 * @param player The player for the execution.
	 * 
	 * @return If the event was executed successfully.
	 * 
	 * @see PlayerConfigurator#setCharacterTitleList(Player, List)
	 * @see PlayerConfigurator#setCharacterTitle(Player, String)
	 * @see TitleMenu#open(Player)
	 */
	@Override
	public boolean execute(Player player) {
		try {
			String titleName = title.getTitleName();
			List<String> titleList = PlayerConfigurator.getCharacterTitleList(player);
			titleList.add(titleName);
			PlayerConfigurator.setCharacterTitleList(player, titleList);
			PlayerConfigurator.setCharacterTitle(player, titleName);
			
			long currentSouls = PlayerCollectionConfigurator.getCharacterSoulstones(player);
			PlayerCollectionConfigurator.setCharacterSoulstones(player, currentSouls - title.getTitleCost());
			
			player.sendMessage(ChatColor.GREEN + "Your chat title was changed to \"" + title.getTitleDisplayName() + "\"!");
			TitleMenu.open(player);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An Error occurred while buying the title!");
			player.closeInventory();
			return false;
		}
	}

}
