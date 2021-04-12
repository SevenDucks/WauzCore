package eu.wauz.wauzcore.system;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.DiscordConfigurator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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
public class WauzDiscordBot extends ListenerAdapter {
	
	/**
	 * The api to grab player skins.
	 */
	private static final String SKIN_API = "https://crafatar.com/avatars/";
	
	/**
	 * The API to access Discord from the bot account.
	 */
	private JDA jda;
	
	/**
	 * The general channel for the chat.
	 */
	private TextChannel generalChannel;
	
	/**
	 * The logging channel for the server log.
	 */
	private TextChannel loggingChannel;
	
	/**
	 * If the bot is still running.
	 */
	private boolean isRunning = false;
	
	/**
	 * Creates and logs in a new bot and loads all channels.
	 */
	public WauzDiscordBot() {
		List<GatewayIntent> intents = new ArrayList<>();
		intents.addAll(Arrays.asList(GatewayIntent.values()));
		intents.remove(GatewayIntent.GUILD_MEMBERS);
		intents.remove(GatewayIntent.GUILD_PRESENCES);
		JDABuilder jdaBuilder = JDABuilder.create(DiscordConfigurator.getToken(), intents);
		jdaBuilder.disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS);
		jdaBuilder.setAutoReconnect(true);
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setActivity(Activity.playing(DiscordConfigurator.getPlaysMessage()));
		
		try {
			jda = jdaBuilder.build();
			jda.awaitReady();
			jda.addEventListener(this);
			generalChannel = jda.getTextChannelById(DiscordConfigurator.getGeneralChannelId());
			loggingChannel = jda.getTextChannelById(DiscordConfigurator.getLoggingChannelId());
			isRunning = true;
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
		String formattedMessage = ChatColor.stripColor(message).replace("`", "");
		if(inLogChannel) {
			loggingChannel.sendMessage("`" + formattedMessage + "`").queue();
		}
		else {
			generalChannel.sendMessage("**Minecraft**: `" + formattedMessage + "`").queue();
		}
	}
	
	/**
	 * Sends an embed with custom title and color.
	 * 
	 * @param player The player whose head to use in the embed image.
	 * @param title The title of the embed.
	 * @param description The description of the embed.
	 * @param color The color of the embed.
	 * @param inLogChannel If the message should be sent to the log channel.
	 */
	public void sendEmbedFromMinecraft(Player player, String title, String description, Color color, boolean inLogChannel) {
		sendEmbed(player, title, description, color, inLogChannel ? loggingChannel : generalChannel);
	}
	
	/**
	 * Sends an embed with custom title and color.
	 * 
	 * @param player The player whose head to use in the embed image.
	 * @param title The title of the embed.
	 * @param description The description of the embed.
	 * @param color The color of the embed.
	 * @param textChannel The channel to send the embed to.
	 */
	public static void sendEmbed(Player player, String title, String description, Color color, TextChannel textChannel) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		if(player != null) {
			embedBuilder.setAuthor(title, null, SKIN_API + player.getUniqueId());
		}
		else {
			embedBuilder.setTitle(title);
		}
		if(description != null) {
			embedBuilder.setDescription(description);
		}
		embedBuilder.setColor(color != null ? color : new Color(250, 250, 250));
		textChannel.sendMessage(embedBuilder.build()).queue();
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
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			User user = event.getAuthor();
			MessageChannel channel = event.getChannel();
			String message = event.getMessage().getContentRaw();
			
			if(channel.getId().equals(generalChannel.getId())
					&& !message.startsWith("**Minecraft**")
					&& StringUtils.isNotBlank(message)) {
				ChatFormatter.discord(message, user.getName());
			}
		}
		catch(Exception e) {
			WauzDebugger.catchException(getClass(), e);
		}
	}
	
}
