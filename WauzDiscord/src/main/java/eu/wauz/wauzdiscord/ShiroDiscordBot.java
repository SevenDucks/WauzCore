package eu.wauz.wauzdiscord;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.SystemAnalytics;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzdiscord.data.DiscordConfigurator;
import eu.wauz.wauzdiscord.music.WauzAudioEventAdapter;
import eu.wauz.wauzdiscord.music.WauzAudioLoadResultHandler;
import eu.wauz.wauzdiscord.music.WauzAudioSendHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

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
	 * The audio channel for playing songs.
	 */
	private VoiceChannel audioChannel;
	
	/**
	 * The audio manager to establish an audio connection.
	 */
	private AudioManager audioManager;
	
	/**
	 * The audio player manager to load songs into the audio connection.
	 */
	private AudioPlayerManager audioPlayerManager;
	
	/**
	 * The player created from the player manager that provides sound.
	 */
	private AudioPlayer audioPlayer;
	
	/**
	 * The list of all queued audio tracks.
	 */
	private List<AudioTrack> audioQueue = new ArrayList<>();
	
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
		JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
		jdaBuilder.setToken(ShiroDiscordBotConfiguration.TOKEN);
		jdaBuilder.setAutoReconnect(true);
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setGame(Game.of(GameType.DEFAULT, ShiroDiscordBotConfiguration.PLAYS_MESSAGE));
		
		try {
			jda = jdaBuilder.buildBlocking();
			jda.addEventListener(this);
			guild = jda.getGuildById(DiscordConfigurator.getGuildId());
			generalChannel = jda.getTextChannelById(DiscordConfigurator.getGeneralChannelId());
			loggingChannel = jda.getTextChannelById(DiscordConfigurator.getLoggingChannelId());
			botsChannel = jda.getTextChannelById(DiscordConfigurator.getBotsChannelId());
			audioChannel = guild.getVoiceChannelById(DiscordConfigurator.getAudioChannelId());
			audioManager = guild.getAudioManager();
			setupAudioPlayer();
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
	 * @param title The title of the embed. 
	 * @param color The color of the embed.
	 * @param inLogChannel If the message should be send to the log channel.
	 */
	public void sendEmbedFromMinecraft(String title, Color color, boolean inLogChannel) {
		ShiroDiscordMessageUtils.sendEmbed(title, color, inLogChannel ? loggingChannel : generalChannel);
	}
	
	/**
	 * Updates the topic of the logging channel, to display the newest system analytics.
	 * 
	 * @see SystemAnalytics
	 */
	public void updateLoggingChannelServerStats() {
		if(isRunning) {
			SystemAnalytics systemAnalytics = new SystemAnalytics();
			String topic = systemAnalytics.getPlayersOnline()
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
	 * @param event
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			User user = event.getAuthor();
			String id = user.getId();
			
			MessageChannel channel = event.getChannel();
			String message = event.getMessage().getContentRaw();
			
// Minecraft Chat Connector
			
			if(channel.getId().equals(generalChannel.getId()) && !message.startsWith("**Minecraft**")) {
				ChatFormatter.discord(message, user.getName(), isMaster(id));
			}
			
// Global Server Commands
			
			if(!StringUtils.startsWith(message, "shiro")) {
				return;
			}
			if(StringUtils.startsWith(message, "shiro hentai") && isMainServer) {
				channel.sendMessage(ShiroDiscordMessageUtils.getHentaiString(channel, message)).queue();
				return;
			}
			if(!channel.getId().equals(botsChannel.getId()) && isMainServer) {
				channel.sendMessage("No! Try this again here: " + botsChannel.getAsMention()).queue();
				return;
			}
			if(StringUtils.startsWith(message, "shiro servers") && isMaster(id)) {
				channel.sendMessage("`" + WauzCore.getServerKey() + "` `" + WauzCore.IP_AND_PORT + "`").queue();
				return;
			}
			if(StringUtils.startsWith(message, "shiro command " + WauzCore.IP_AND_PORT + " ") && isMaster(id)) {
				channel.sendMessage(ShiroDiscordMessageUtils.executeCommand(message)).queue();
				return;
			}
			if(!isMainServer) {
				return;
			}
			
// Main Server Commands
				
			if(StringUtils.startsWith(message, "shiro help")) {
				channel.sendMessage(ShiroDiscordMessageUtils.getHelpString()).queue();
			}
			else if(StringUtils.startsWith(message, "shiro profile ")) {
				channel.sendMessage(ShiroDiscordMessageUtils.getProfileString(message)).queue();
			}
			
// Music Player
			
			else if(StringUtils.startsWith(message, "shiro play ")) {
				playAudio(message, channel, false);
			}
			else if(StringUtils.startsWith(message, "shiro playnow ")) {
				playAudio(message, channel, true);
			}
			else if(StringUtils.startsWith(message, "shiro skip")) {
				skipAudio();
			}
			else if(StringUtils.startsWith(message, "shiro stop")) {
				leaveAudioChannel();
			}
			
// Fun Stuff
			
			else if(StringUtils.containsAny(message.toLowerCase(), "marc", "clara", "clarc", "gay", "gae")) {
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
		catch(Exception e) {
			WauzDebugger.catchException(getClass(), e);
		}
	}
	
	/**
	 * @param id The ID of an Discord user
	 * 
	 * @return If the user has administrator access to the bot.
	 */
	private boolean isMaster(String id) {
		return id.equals(ShiroDiscordBotConfiguration.ADMIN) || id.equals(jda.getSelfUser().getId());
	}
	
	/**
	 * Tells the bot to play a song from a Discord command with a YouTube URL.
	 * 
	 * @param message The message that contains the song URL.
	 * @param channel The channel to write the response.
	 * @param startPlaying If the bot should instantly skip to this song.
	 */
	public void playAudio(String message, MessageChannel channel, boolean startPlaying) {
		if(startPlaying) {
			playSong(StringUtils.substringAfter(message, "shiro playnow "), channel, true);
		}
		else {
			playSong(StringUtils.substringAfter(message, "shiro play "), channel, false);
		}
	}
	
	public void skipAudio() {
		if(!audioQueue.isEmpty()) {
			AudioTrack audioTrack = audioQueue.get(0);
			audioQueue.remove(audioTrack);
		}
		
		if(audioQueue.isEmpty()) {
			leaveAudioChannel();
		}
		else {
			audioPlayer.playTrack(audioQueue.get(0));
		}
	}
	
	/**
	 * Tells the bot to join the audio channel from its config.
	 */
	public void joinAudioChannel() {
		audioManager.setSendingHandler(new WauzAudioSendHandler(this));
		audioManager.openAudioConnection(audioChannel);
	}
	
	/**
	 * Tells the bot to leave its current audio channel.
	 */
	public void leaveAudioChannel() {
		audioManager.closeAudioConnection();
		audioPlayer.stopTrack();
		audioQueue.clear();
	}
	
	/**
	 * Sets up an audio player.
	 */
	private void setupAudioPlayer() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(new WauzAudioEventAdapter(this));
	}
	
	/**
	 * Tells the bot to play a song.
	 * 
	 * @param identifier The identifier that a specific source manager should be able to find the track with.
	 * @param messageChannel The channel to write the response.
	 * @param startPlaying If the bot should instantly skip to this song.
	 */
	private void playSong(String identifier, MessageChannel messageChannel, boolean startPlaying) {
		audioPlayerManager.loadItem(identifier, new WauzAudioLoadResultHandler(this, messageChannel, startPlaying));
	}

	/**
	 * @return The player created from the player manager that provides sound.
	 */
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	/**
	 * @return The list of all queued audio tracks.
	 */
	public List<AudioTrack> getAudioQueue() {
		return audioQueue;
	}
	
}
