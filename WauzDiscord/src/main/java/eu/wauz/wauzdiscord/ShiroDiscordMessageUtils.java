package eu.wauz.wauzdiscord;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.kodehawa.lib.imageboards.DefaultImageBoards;
import net.kodehawa.lib.imageboards.entities.impl.Rule34Image;

/**
 * An util class for sending messages from the Discord bot.
 * 
 * @author Wauzmons
 */
public class ShiroDiscordMessageUtils {
	
	/**
	 * An instance of the random class to select hentai images. (Oof)
	 */
	private static Random random = new Random();
	
	/**
	 * Sends an embed with custom title and color.
	 * 
	 * @param title The title of the embed. 
	 * @param color The color of the embed.
	 * @param textChannel The channel to send the embed to.
	 */
	public static void sendEmbed(String title, Color color, TextChannel textChannel) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(title);
		embedBuilder.setColor(color);
		textChannel.sendMessage(embedBuilder.build()).queue();
	}
	
	/**
	 * Executes a Minecraft command, sent from Discord to the server.
	 * 
	 * @param message The message that contains the command.
	 * 
	 * @return The response from the bot.
	 */
	public static String executeCommand(String message) {
		try {
			String command = StringUtils.substringAfter(message, "shiro command " + WauzCore.IP_AND_PORT + " ");
			Bukkit.getScheduler().callSyncMethod(WauzDiscord.getInstance(),
					() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
			
			return "Your command was executed on " + WauzCore.IP_AND_PORT + ", my master!";
		}
		catch(Exception e) {
			return ShiroDiscordBotConfiguration.ERROR_MESSAGE;
		}
	}
	
	/**
	 * Gives out a Minecraft profile, requested from Discord.
	 * 
	 * @param message The message that contains the Minecraft name.
	 * 
	 * @return The response from the bot.
	 */
	public static String getProfileString(String message) {
		try {
			OfflinePlayer player = WauzCore.getOfflinePlayer(message.split(" ")[2]);
			if(player.getFirstPlayed() == 0)
				throw new NullPointerException("Unknown Player!");
			
			String profileString = "";
			StatisticsFetcher statistics = new StatisticsFetcher(player);
			
			profileString += player.getName()
					+ " has travelled " + statistics.getWalkedMetresString()
					+ " metres and killed " + statistics.getKilledMobsString()
					+ " Mobs on their " + statistics.getPlayedHoursString() + " hour long adventure!"
					+ (player.getName().equals("Wauzmons") ? System.lineSeparator() + "Also he's my master! :heart:" : "")
					+ System.lineSeparator()
					+ System.lineSeparator()
					+ "MMORPG Characters: "
					+ statistics.getCharacterString(1) + " | "
					+ statistics.getCharacterString(2) + " | "
					+ statistics.getCharacterString(3) + " ";

			return profileString;
		}
		catch(Exception e) {
			return "Sorry! Shiro doesn't know this player. :/";
		}
	}
	
	/**
	 * Finds a hentai image from Rule 34, with an optional tag.
	 * I am not proud of this one.
	 * 
	 * @param channel The channel, to check if it contains "nsfw" in its name.
	 * @param message The message that may contain a tag.
	 * 
	 * @return The response from the bot.
	 */
	public static String getHentaiString(MessageChannel channel, String message) {
		if(!channel.getName().contains("nsfw"))
			return "No, no! This isn't a nsfw channel!";
		
		try {
			String[] tags = message.split(" ");
			List<Rule34Image> result;
			
			if(tags.length >= 3) {
				result = DefaultImageBoards.RULE34.search(300, tags[2]).blocking();
			}
			else {
				result = DefaultImageBoards.RULE34.get(300).blocking();
			}
			
			if(result.isEmpty()) {
				return "Shiro didn't find anything...";
			}
			else {
				return result.get(random.nextInt(result.size())).getURL();
			}
		}
		catch (Exception e) {
			WauzDebugger.catchException(ShiroDiscordMessageUtils.class, e);
			return ShiroDiscordBotConfiguration.ERROR_MESSAGE;
		}
	}
	
	/**
	 * Generates a list of command syntax, for controlling the bot from Discord.
	 * 
	 * @return The list of commans.
	 */
	public static String getHelpString() {
		String helpString = "";
		String br = System.lineSeparator();
		helpString += "--- NORMAL COMMANDS ---";
		helpString += br + "`shiro profile <Minecraft Name (Case Sensitive)>` shows player stats";
		helpString += br + "`shiro play <YouTube-URL>` plays song in the configured audio channel";
		helpString += br + "`shiro playnow <YouTube-URL>` same but instantly skip to song";
		helpString += br + "`shiro skip` skips to next song in queue";
		helpString += br + "`shiro stop` stops song and clears queue";
		helpString += br + br + "--- NSFW COMMANDS ---";
		helpString += br + "`shiro hentai` finds random rule34 image";
		helpString += br + "`shiro hentai <tag>` finds tagged rule34 image";
		helpString += br + br + "--- ADMIN COMMANDS ---";
		helpString += br + "`shiro servers` lists all running servers";
		helpString += br + "`shiro command <IP> <Command>` executes commands on remote machine";
		return helpString;
	}

}
