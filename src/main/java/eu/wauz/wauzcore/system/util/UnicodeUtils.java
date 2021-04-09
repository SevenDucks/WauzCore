package eu.wauz.wauzcore.system.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * An util class for stuff like unicode icons.
 * 
 * @author Wauzmons
 */
public class UnicodeUtils {
	
	/**
	 * A paragraph icon in unicode.
	 */
	public static final String ICON_PARAGRAPH = "\u00A7";
	
	/**
	 * A degree icon in unicode.
	 */
	public static final String ICON_DEGREES = "\u00B0";
	
	/**
	 * A caret icon in unicode.
	 */
	public static final String ICON_CARET = "\u00BB";
	
	/**
	 * A zero width space icon in unicode.
	 */
	public static final String ICON_ZERO_WIDTH_SPACE = "\u200B";
	
	/**
	 * A bullet icon in unicode.
	 */
	public static final String ICON_BULLET = "\u2022";
	
	/**
	 * A diameter icon in unicode.
	 */
	public static final String ICON_DIAMETER = "\u2300";
	
	/**
	 * A bullseye icon in unicode.
	 */
	public static final String ICON_BULLSEYE = "\u25CE";
	
	/**
	 * A sun icon in unicode.
	 */
	public static final String ICON_SUN = "\u2600";
	
	/**
	 * A diamond icon in unicode.
	 */
	public static final String ICON_DIAMOND = "\u2666";
	
	/**
	 * An anchor icon in unicode.
	 */
	public static final String ICON_ANCHOR = "\u2693";
	
	/**
	 * A swords icon in unicode.
	 */
	public static final String ICON_SWORDS = "\u2694";
	
	/**
	 * A lightning icon in unicode.
	 */
	public static final String ICON_LIGHTNING = "\u26A1";
	
	/**
	 * A pickaxe icon in unicode.
	 */
	public static final String ICON_PICKAXE = "\u26CF";
	
	/**
	 * A shield icon in unicode.
	 */
	public static final String ICON_SHIELD = "\u26E8";
	
	/**
	 * A star icon in unicode.
	 */
	public static final String ICON_STAR = "\u2B50";
	
	/**
	 * A heart icon in unicode.
	 */
	public static final String ICON_HEART = "\u2764";
	
	/**
	 * Wraps the given text, to be better displayable in lore.
	 * 
	 * @param text The text to wrap.
	 * 
	 * @return The wrapped text.
	 */
	public static List<String> wrapText(String text) {
		String doubleParagraph = UnicodeUtils.ICON_PARAGRAPH + UnicodeUtils.ICON_PARAGRAPH;
		String[] textParts = WordUtils.wrap(text, 42, doubleParagraph, true).split(doubleParagraph);
		return Arrays.asList(textParts);
	}
	
	/**
	 * Creates a progress bar, with a percentage display.
	 * 
	 * @param progress The current value.
	 * @param goal The maximum value.
	 * @param lines The amount of vertical lines, the bar consists of.
	 * @param color The color of the bar.
	 * 
	 * @return The progress bar string.
	 */
	public static String createProgressBar(double progress, double goal, int lines, ChatColor color) {
		double precisePercantage = progress * 100.0 / goal;
		int barPercentage = (int) (progress * lines / goal);
		
		String progressBar = String.valueOf(color);
		for(int bar = 1; barPercentage >= bar; bar++) {
			progressBar += "|";
		}
		progressBar += String.valueOf(ChatColor.GRAY);
		for(int bar = barPercentage; bar < lines; bar++) {
			progressBar += "|";
		}
		return progressBar + " " + ChatColor.WHITE + ((int) precisePercantage) + "%";
	}
	
	/**
	 * Sends a hoverable message of an item stack to all players.
	 * 
	 * @param itemStack The item stack that should be shown.
	 * @param message The message that should be shown.
	 */
	public static void shareChatItem(ItemStack itemStack, String message) {
		Components.broadcast(Components.itemComponent(message, itemStack));
	}

	/**
	 * Sends a clickable message to execute a command to a player.
	 * 
	 * @param player The player that should receive the message.
	 * @param command The command that should be executed on click.
	 * @param message The message that should be shown.
	 * @param border If the message has borders like this: ----------
	 */
	public static void sendChatCommand(Player player, String command, String message, boolean border) {
		if(border) {
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		}
		
		player.sendMessage(Components.commandComponent(message, command));
	    
	    if(border) {
	    	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
	    }
	}

	/**
	 * Sends a clickable message to open an url to a player.
	 * 
	 * @param player The player that should receive the message.
	 * @param url The url that should be opened on click.
	 * @param message The message that should be shown.
	 * @param border If the message has borders like this: ----------
	 */
	public static void sendChatHyperlink(Player player, String url, String message, boolean border) {
		if(border) {
			player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
		}
		
		player.sendMessage(Components.hyperlinkComponent(message, url));
	    
	    if(border) {
	    	player.sendMessage(ChatColor.DARK_BLUE + "------------------------------");
	    }
	}

}
