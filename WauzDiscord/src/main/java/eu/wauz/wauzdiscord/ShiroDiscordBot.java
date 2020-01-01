package eu.wauz.wauzdiscord;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.system.ChatFormatter;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.api.StatisticsFetcher;
import eu.wauz.wauzdiscord.data.DiscordConfigurator;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.audio.AudioSendHandler;
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
import net.kodehawa.lib.imageboards.DefaultImageBoards;
import net.kodehawa.lib.imageboards.entities.impl.Rule34Image;
import net.md_5.bungee.api.ChatColor;

/**
 * The Discord bot running on this server.
 * Named after my Waifu.
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
	 * The track scheduler to handle audio events such as track changes.
	 */
	private TrackScheduler trackScheduler;
	
	/**
	 * The list of all queued audio tracks.
	 */
	private List<AudioTrack> audioQueue = new ArrayList<>();
	
	/**
	 * An instance of the random class to select hentai images. (Oof)
	 */
	private Random random = new Random(); 
	
	/**
	 * The token for the Discord bot user.
	 */
	private final String TOKEN = DiscordConfigurator.getToken();
	
	/**
	 * The Discord ID of the admin user.
	 */
	private final String ADMIN = DiscordConfigurator.getAdminUserId();
	
	/**
	 * The IP of the server that should handle bot commands.
	 */
	private final String MAIN_SERVER_IP = DiscordConfigurator.getMainServerIp();
	
	/**
	 * The text shown under the bot in the Discord user list.
	 */
	private final String PLAYS_MESSAGE = DiscordConfigurator.getPlaysMessage();
	
	/**
	 * The Discord message when an error occurs.
	 */
	private final String ERROR_MESSAGE = DiscordConfigurator.getErrorMessage();
	
	/**
	 * Creates and logs in a new bot and loads all channels.
	 * Also initializes the audio player.
	 * 
	 * @see ShiroDiscordBot#configureLavaPlayer()
	 */
	public ShiroDiscordBot() {
		JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
		jdaBuilder.setToken(TOKEN);
		jdaBuilder.setAutoReconnect(true);
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setGame(Game.of(GameType.DEFAULT, PLAYS_MESSAGE));
		
		try {
			jda = jdaBuilder.buildBlocking();
			jda.addEventListener(this);
			guild = jda.getGuildById(DiscordConfigurator.getGuildId());
			generalChannel = jda.getTextChannelById(DiscordConfigurator.getGeneralChannelId());
			loggingChannel = jda.getTextChannelById(DiscordConfigurator.getLoggingChannelId());
			botsChannel = jda.getTextChannelById(DiscordConfigurator.getBotsChannelId());
			audioChannel = guild.getVoiceChannelById(DiscordConfigurator.getAudioChannelId());
			audioManager = guild.getAudioManager();
			configureLavaPlayer();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the bot user's status to offline and stops the bot.
	 */
	public void stop() {
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
	 * Sends an embed that will not be written into the Minecraft chat.
	 * 
	 * @param title The title of the embed. 
	 * @param color The color of the embed.
	 * @param inLogChannel If the message should be send to the log channel.
	 */
	public void sendEmbedFromMinecraft(String title, Color color, boolean inLogChannel) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle(title);
		embedBuilder.setColor(color);
		
		if(inLogChannel) {
			loggingChannel.sendMessage(embedBuilder.build()).queue();
		}
		else {
			generalChannel.sendMessage(embedBuilder.build()).queue();
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
			if(StringUtils.startsWith(message, "shiro hentai") && WauzCore.IP_AND_PORT.equals(MAIN_SERVER_IP)) {
				channel.sendMessage(getHentaiString(channel, message)).queue();
				return;
			}
			if(!channel.getId().equals(botsChannel.getId()) && WauzCore.IP_AND_PORT.equals(MAIN_SERVER_IP)) {
				channel.sendMessage("No! Try this again here: " + botsChannel.getAsMention()).queue();
				return;
			}
			if(StringUtils.startsWith(message, "shiro servers") && isMaster(id)) {
				channel.sendMessage("`" + WauzCore.getServerKey() + "` `" + WauzCore.IP_AND_PORT + "`").queue();
				return;
			}
			if(StringUtils.startsWith(message, "shiro command " + WauzCore.IP_AND_PORT + " ") && isMaster(id)) {
				channel.sendMessage(executeCommand(message)).queue();
				return;
			}
			if(!WauzCore.IP_AND_PORT.equals(MAIN_SERVER_IP)) {
				return;
			}
			
// Main Server Commands
				
			if(StringUtils.startsWith(message, "shiro help")) {
				channel.sendMessage(getHelpString()).queue();
			}
			else if(StringUtils.startsWith(message, "shiro profile ")) {
				channel.sendMessage(getProfileString(message)).queue();
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
		return id.equals(ADMIN) || id.equals(jda.getSelfUser().getId());
	}
	
	/**
	 * Executes a Minecraft command, sent from Discord to this server.
	 * 
	 * @param message The message that contains the command.
	 * 
	 * @return The response from the bot.
	 */
	private String executeCommand(String message) {
		try {
			String command = StringUtils.substringAfter(message, "shiro command " + WauzCore.IP_AND_PORT + " ");
			Bukkit.getScheduler().callSyncMethod(WauzDiscord.getInstance(),
					() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
			
			return "Your command was executed on " + WauzCore.IP_AND_PORT + ", my master!";
		}
		catch(Exception e) {
			return ERROR_MESSAGE;
		}
	}
	
	/**
	 * Gives out a Minecraft profile, requested from Discord.
	 * 
	 * @param message The message that contains the Minecraft name.
	 * 
	 * @return The response from the bot.
	 */
	private String getProfileString(String message) {
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
	 * @return A list of command syntax, for controlling the bot from Discord.
	 */
	private String getHelpString() {
		String helpString = "";
		String br = System.lineSeparator();
		helpString += "--- NORMAL COMMANDS ---";
		helpString += br + "`shiro profile <Minecraft Name (Case Sensitive)>` shows player stats";
		helpString += br + "`shiro play <YouTube-URL>` plays song in \"" + audioChannel.getName() + "\"";
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
	
	/**
	 * Finds a hentai image from Rule 34, with an optional tag.
	 * I am not proud of this one.
	 * 
	 * @param channel The channel, to check if it contains "nsfw" in its name.
	 * @param message The message that may contain a tag.
	 * 
	 * @return The response from the bot.
	 */
	public String getHentaiString(MessageChannel channel, String message) {
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
			
			if(result.size() == 0) {
				return "Shiro didn't find anything...";
			}
			else {
				return result.get(random.nextInt(result.size())).getURL();
			}
		}
		catch (Exception e) {
			WauzDebugger.catchException(getClass(), e);
			return ERROR_MESSAGE;
		}
	}
	
	/**
	 * Tells the bot to play a song from a Discord command with a YouTube URL.
	 * 
	 * @param message The message that contains the song URL.
	 * @param channel The channel to write the response.
	 * @param startPlaying If the bot should instantly skip to this song.
	 */
	private void playAudio(String message, MessageChannel channel, boolean startPlaying) {
		if(startPlaying)
			addTrackToLavaPlayer(StringUtils.substringAfter(message, "shiro playnow "), channel, true);
		else
			addTrackToLavaPlayer(StringUtils.substringAfter(message, "shiro play "), channel, false);
	}
	
	private void skipAudio() {
		if(!audioQueue.isEmpty()) {
			AudioTrack audioTrack = audioQueue.get(0);
			audioQueue.remove(audioTrack);
		}
		
		if(audioQueue.isEmpty())
			leaveAudioChannel();
		else
			audioPlayer.playTrack(audioQueue.get(0));
	}
	
	/**
	 * Tells the bot to join the audio channel from its config.
	 */
	private void joinAudioChannel() {
		audioManager.setSendingHandler(getLavaPlayerAudioSendHandler());
		audioManager.openAudioConnection(audioChannel);
	}
	
	/**
	 * Tells the bot to leave its current audio channel.
	 */
	private void leaveAudioChannel() {
		audioManager.closeAudioConnection();
		audioPlayer.stopTrack();
		audioQueue.clear();
	}
	
	/**
	 * Sets up an audio player.
	 */
	private void configureLavaPlayer() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayer = audioPlayerManager.createPlayer();
		trackScheduler = new TrackScheduler();
		audioPlayer.addListener(trackScheduler);
	}
	
	/**
	 * The audio handler, to send sound from its source to the current audio channel.
	 * 
	 * @return A new instance of the handler.
	 */
	private AudioSendHandler getLavaPlayerAudioSendHandler() {
		return new AudioSendHandler() {
			
			private AudioFrame lastFrame;
			
			@Override
			public boolean canProvide() {
				lastFrame = audioPlayer.provide();
				return lastFrame != null;
			}
			
			@Override
			public byte[] provide20MsAudio() {
				return lastFrame.getData();
			}
			
			@Override
			public boolean isOpus() {
				return true;
			}
			
		};
	}
	
	/**
	 * Tells the bot to play a song.
	 * 
	 * @param identifier The identifier that a specific source manager should be able to find the track with.
	 * @param channel The channel to write the response.
	 * @param startPlaying If the bot should instantly skip to this song.
	 */
	private void addTrackToLavaPlayer(String identifier, MessageChannel channel, boolean startPlaying) {
		audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {
			
			/**
			 * If a single track was loaded, it gets added to the audio queue.
			 */
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				if(startPlaying || audioQueue.isEmpty()) {
					joinAudioChannel();
					audioQueue.add(audioTrack);
					audioPlayer.playTrack(audioTrack);
					channel.sendMessage("Shiro will now play your track! Nya~!").queue();
				}
				else {
					audioQueue.add(audioTrack);
					channel.sendMessage("Shiro added your track to the queue! Nya~!").queue();
				}
			}
			
			/**
			 * If a playlist was loaded, its tracks get added to the audio queue.
			 */
			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				boolean audioQueueEmpty = audioQueue.isEmpty();
				audioQueue.addAll(audioPlaylist.getTracks());
				channel.sendMessage("Shiro added your playlist to the queue! Nya~!").queue();
				if(startPlaying || audioQueueEmpty) {
					joinAudioChannel();
					audioPlayer.playTrack(audioQueue.get(0));
				}
			}
			
			/**
			 * Print an error if the source is unreadable.
			 */
			@Override
			public void noMatches() {
				channel.sendMessage("Sorry! Shiro can't read this audio source!").queue();
			}
			
			/**
			 * Print an error if the loading of the track caused an exception.
			 */
			@Override
			public void loadFailed(FriendlyException exception) {
				if(exception.severity.equals(Severity.COMMON))
					channel.sendMessage("Shiro got this error: " + exception.getMessage()).queue();
				else
					channel.sendMessage(ERROR_MESSAGE).queue();
			}
			
		});
	}
	
	/**
	 * A subclass of the bot to handle audio events.
	 * 
	 * @author Wauzmons
	 */
	private class TrackScheduler extends AudioEventAdapter {

		/*
		@Override
		public void onPlayerPause(AudioPlayer player) {
			// Player was paused
		}

		@Override
		public void onPlayerResume(AudioPlayer player) {
			// Player was resumed
		}

		@Override
		public void onTrackStart(AudioPlayer player, AudioTrack track) {
			// A track started playing
		}
		*/

		/**
		 * If a track has ended, load the next one
		 * or leave the channel if nobody is listening.
		 */
		@Override
		public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
			// endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
			// endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
			// endReason == STOPPED: The player was stopped.
			// endReason == REPLACED: Another track started playing while this had not finished
			// endReason == CLEANUP: Player hasn't been queried for a while
			if (endReason.mayStartNext)
				skipAudio();
			else if(endReason.equals(AudioTrackEndReason.CLEANUP))
				leaveAudioChannel();
		}

		/*
		@Override
		public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
			// An already playing track threw an exception (track end event will still be received separately)
		}
		*/

		/**
		 * The audio track could not provide any audio, so it will be skipped.
		 */
		@Override
		public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
			skipAudio();
		}
		
	}
	
}
