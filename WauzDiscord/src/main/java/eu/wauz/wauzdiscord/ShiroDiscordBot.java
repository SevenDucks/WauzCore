package eu.wauz.wauzdiscord;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.SystemAnalytics;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzdiscord.data.DiscordConfigurator;
import eu.wauz.wauzdiscord.music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * The Discord bot running on the server.
 * 
 * @author Wauzmons
 */
public class ShiroDiscordBot extends ListenerAdapter {
	
	/**
	 * The API to access Discord from the bot account.
	 */
	private JDA jda;
	
	/**
	 * The Discord server.
	 */
	private Guild guild;
	
	/**
	 * The general channel for the chat.
	 */
	private TextChannel generalChannel;
	
	/**
	 * The logging channel for the server log.
	 */
	private TextChannel loggingChannel;
	
	/**
	 * The bots channel for commands.
	 */
	private TextChannel botsChannel;
	
	/**
	 * The manager for music functionalities.
	 */
	private MusicManager musicManager;
	
	/**
	 * If the bot is still running.
	 */
	private boolean isRunning = false;
	
	/**
	 * If the bot runs on the main server and should handle message responses.
	 */
	private boolean isMainServer = false;
	
	/**
	 * Creates and logs in a new bot and loads all channels.
	 * Also initializes the audio player.
	 * 
	 * @see ShiroDiscordBot#setupAudioPlayer()
	 */
	public ShiroDiscordBot() {
		List<GatewayIntent> intents = new ArrayList<>();
		intents.addAll(Arrays.asList(GatewayIntent.values()));
		intents.remove(GatewayIntent.GUILD_MEMBERS);
		intents.remove(GatewayIntent.GUILD_PRESENCES);
		JDABuilder jdaBuilder = JDABuilder.create(ShiroDiscordBotConfiguration.TOKEN, intents);
		jdaBuilder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS);
		jdaBuilder.setAutoReconnect(true);
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setActivity(Activity.playing(ShiroDiscordBotConfiguration.PLAYS_MESSAGE));
		
		try {
			jda = jdaBuilder.build();
			jda.awaitReady();
			jda.addEventListener(this);
			guild = jda.getGuildById(DiscordConfigurator.getGuildId());
			generalChannel = jda.getTextChannelById(DiscordConfigurator.getGeneralChannelId());
			loggingChannel = jda.getTextChannelById(DiscordConfigurator.getLoggingChannelId());
			botsChannel = jda.getTextChannelById(DiscordConfigurator.getBotsChannelId());
			musicManager = new MusicManager(guild);
			isRunning = true;
			isMainServer = WauzCore.IP_AND_PORT.equals(ShiroDiscordBotConfiguration.MAIN_SERVER_IP);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the bot user's status to offline and stops the bot.
	 */
	public void stop() {
		isRunning = false;
		loggingChannel.getManager().setTopic("Server Offline").submit();
		jda.getPresence().setStatus(OnlineStatus.OFFLINE);
		jda.shutdown();
	}
	
	/**
	 * Sends a message that will not be written into the Minecraft chat.
	 * The Minecraft prefix prevents infinite loops between Minecraft and Discord.
	 * 
	 * @param message The content of the message.
	 * @param inLogChannel If the message should be send to the log channel.
	 */
	public void sendMessageFromMinecraft(String message, boolean inLogChannel) {
		if(inLogChannel) {
			loggingChannel.sendMessage("`" + ChatColor.stripColor(message) + "`").queue();
		}
		else {
			generalChannel.sendMessage("**Minecraft**: `" + ChatColor.stripColor(message) + "`").queue();
		}
	}
	
	/**
	 * Sends an embed with custom title and color.
	 * 
	 * @param player The player whose head to use in the embed image.
	 * @param title The title of the embed. 
	 * @param color The color of the embed.
	 * @param inLogChannel If the message should be send to the log channel.
	 */
	public void sendEmbedFromMinecraft(Player player, String title, Color color, boolean inLogChannel) {
		ShiroDiscordMessageUtils.sendEmbed(player, title, color, inLogChannel ? loggingChannel : generalChannel);
	}
	
	/**
	 * Updates the topic of the general channel, to display the newest public stats.
	 * 
	 * @see SystemAnalytics
	 */
	public void updateGeneralChannelServerStats() {
		if(isRunning) {
			SystemAnalytics systemAnalytics = new SystemAnalytics();
			String topic = "Live Chat"
					+ " " + systemAnalytics.getServerTime()
					+ " " + systemAnalytics.getPlayersOnline();
			generalChannel.getManager().setTopic(topic).submit();
		}
	}
	
	/**
	 * Updates the topic of the logging channel, to display the newest private stats.
	 * 
	 * @see SystemAnalytics
	 */
	public void updateLoggingChannelServerStats() {
		if(isRunning) {
			SystemAnalytics systemAnalytics = new SystemAnalytics();
			String topic = "Live Log"
					+ " " + systemAnalytics.getCpuUsage()
					+ " " + systemAnalytics.getRamUsage()
					+ " " + systemAnalytics.getSsdUsage();
			loggingChannel.getManager().setTopic(topic).submit();
		}
	}

	/**
	 * Reads a message from Discord and checks if it is a command.
	 * Also sends it to the Minecraft chat.
	 * 
	 * @param event The message event.
	 * 
	 * @see ShiroDiscordBot#checkForGlobalCommands(String, MessageChannel, boolean)
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			User user = event.getAuthor();
			MessageChannel channel = event.getChannel();
			String message = event.getMessage().getContentRaw();
			boolean isAdmin = isAdmin(user.getId());
			
			if(channel.getId().equals(generalChannel.getId())
					&& !message.startsWith("**Minecraft**")
					&& StringUtils.isNotBlank(message)) {
				ChatFormatter.discord(message, user.getName(), isAdmin);
			}
			checkForGlobalCommands(message, channel, isAdmin);
		}
		catch(Exception e) {
			WauzDebugger.catchException(getClass(), e);
		}
	}
	
	/**
	 * Checks a message for global commands and executes the found command.
	 * If no commands where found, main server commands are checked next.
	 * 
	 * @param message The message to check for commands.
	 * @param channel The channel, the command was sent in.
	 * @param isAdmin
	 * 
	 * @see ShiroDiscordBot#checkForMainServerCommands(String, MessageChannel)
	 */
	private void checkForGlobalCommands(String message, MessageChannel channel, boolean isAdmin) {
		if(!StringUtils.startsWith(message, "shiro")) {
			return;
		}
		else if(StringUtils.startsWith(message, "shiro hentai") && isMainServer) {
			channel.sendMessage(ShiroDiscordMessageUtils.getHentaiString(channel, message)).queue();
		}
		else if(!channel.getId().equals(botsChannel.getId()) && isMainServer) {
			channel.sendMessage("No! Try this again here: " + botsChannel.getAsMention()).queue();
		}
		else if(StringUtils.startsWith(message, "shiro servers") && isAdmin) {
			channel.sendMessage("`" + WauzCore.getServerKey() + "` `" + WauzCore.IP_AND_PORT + "`").queue();
		}
		else if(StringUtils.startsWith(message, "shiro command " + WauzCore.IP_AND_PORT + " ") && isAdmin) {
			channel.sendMessage(ShiroDiscordMessageUtils.executeCommand(message)).queue();
		}
		else {
			checkForMainServerCommands(message, channel);
		}
	}
	
	/**
	 * Checks a message for main server commands and executes the found command.
	 * If no commands where found, music player commands are checked next.
	 * 
	 * @param message The message to check for commands.
	 * @param channel The channel, the command was sent in.
	 * 
	 * @see ShiroDiscordBot#checkForMusicPlayerCommands(String, MessageChannel)
	 */
	private void checkForMainServerCommands(String message, MessageChannel channel) {
		if(!isMainServer) {
			return;
		}
		else if(StringUtils.startsWith(message, "shiro help")) {
			channel.sendMessage(ShiroDiscordMessageUtils.getHelpString()).queue();
		}
		else if(StringUtils.startsWith(message, "shiro profile ")) {
			channel.sendMessage(ShiroDiscordMessageUtils.getProfileString(message)).queue();
		}
		else {
			checkForMusicPlayerCommands(message, channel);
		}
	}
	
	/**
	 * Checks a message for music player commands and executes the found command.
	 * If no commands where found, fun commands are checked next.
	 * 
	 * @param message The message to check for commands.
	 * @param channel The channel, the command was sent in.
	 * 
	 * @see ShiroDiscordBot#checkForFunCommands(String, MessageChannel)
	 */
	private void checkForMusicPlayerCommands(String message, MessageChannel channel) {
		if(StringUtils.startsWith(message, "shiro play ")) {
			musicManager.playAudio(message, channel, false);
		}
		else if(StringUtils.startsWith(message, "shiro playnow ")) {
			musicManager.playAudio(message, channel, true);
		}
		else if(StringUtils.startsWith(message, "shiro skip")) {
			musicManager.skipAudio();
		}
		else if(StringUtils.startsWith(message, "shiro stop")) {
			musicManager.leaveAudioChannel();
		}
		else {
			checkForFunCommands(message, channel);
		}
	}
	
	/**
	 * Checks a message for fun commands and executes the found command.
	 * If no commands where found, the message is ignored.
	 * 
	 * @param message The message to check for commands.
	 * @param channel The channel, the command was sent in.
	 */
	private void checkForFunCommands(String message, MessageChannel channel) {
		if(StringUtils.containsAny(message.toLowerCase(), "marc", "clara", "clarc", "gay", "gae")) {
			channel.sendMessage("Marc is really, really gay!").queue();
		}
		else if(StringUtils.containsAny(message.toLowerCase(), "good girl", "pat")) {
			channel.sendMessage(":3").queue();
		}
		else if(StringUtils.containsAny(message.toLowerCase(), "attack", "atacc", "stand")) {
			channel.sendMessage("ORAORAORAORAORA").queue();
		}
		else if(StringUtils.containsAny(message.toLowerCase(), "die", "baka", "fuck")) {
			channel.sendMessage("Baka!").queue();
		}
	}
	
	/**
	 * @param id The ID of a Discord user.
	 * 
	 * @return If the user is the administrator of the bot instance.
	 */
	private boolean isAdmin(String id) {
		return id.equals(ShiroDiscordBotConfiguration.ADMIN) || id.equals(jda.getSelfUser().getId());
	}
	
}
