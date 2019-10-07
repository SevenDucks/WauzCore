package eu.wauz.wauzdiscord;

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
 * The Discord Bot running on this Server.
 * Named after my Waifu.
 * 
 * @author Wauzmons
 */
public class ShiroDiscordBot extends ListenerAdapter {
	
	/**
	 * The API to Access Discord from the Bot Account.
	 */
	private JDA jda;
	
	/**
	 * The Discord Server.
	 */
	private Guild guild;
	
	/**
	 * The General Channel for the Chat.
	 */
	private TextChannel generalChannel;
	
	/**
	 * The Bots Channel for Commands.
	 */
	private TextChannel botsChannel;
	
	/**
	 * The Audio Channel for playing Songs.
	 */
	private VoiceChannel audioChannel;
	
	/**
	 * The Audio Manager to establish an Audio Connection.
	 */
	private AudioManager audioManager;
	
	/**
	 * The Audio Player Manager to load Songs into the Audio Connection.
	 */
	private AudioPlayerManager audioPlayerManager;
	
	/**
	 * The Player created from the Player Manager that provides Sound.
	 */
	private AudioPlayer audioPlayer;
	
	/**
	 * The Track Scheduler to handle Audio Events such as Track Changes.
	 */
	private TrackScheduler trackScheduler;
	
	/**
	 * The List of all queued Audio Tracks.
	 */
	private List<AudioTrack> audioQueue = new ArrayList<>();
	
	/**
	 * A Random Instance to select Hentai Images. (Oof)
	 */
	private Random random = new Random(); 
	
	/**
	 * The Token for the Discord Bot User.
	 */
	private final String TOKEN = DiscordConfigurator.getToken();
	
	/**
	 * The Discord ID of the Admin User.
	 */
	private final String ADMIN = DiscordConfigurator.getAdminUserId();
	
	/**
	 * The IP of the Server that should handle Bot Commands.
	 */
	private final String MAIN_SERVER_IP = DiscordConfigurator.getMainServerIp();
	
	/**
	 * The Text shown under the Bot in the Discord User List.
	 */
	private final String PLAYS_MESSAGE = DiscordConfigurator.getPlaysMessage();
	
	/**
	 * The Discord Message when an Error occurs.
	 */
	private final String ERROR_MESSAGE = DiscordConfigurator.getErrorMessage();
	
	/**
	 * Creates logs in a new Bot and loads all Channels.
	 * Also initializes the Audio Player.
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
	 * Sets the Bot Users Status to Offline and stops the Bot.
	 */
	public void stop() {
		jda.getPresence().setStatus(OnlineStatus.OFFLINE);
		jda.shutdownNow();
	}
	
	/**
	 * Sends a Message that will not be written into the Minecraft Chat.
	 * The Minecraft Prefix prevents infinite Loops between Minecraft and Discord.
	 * 
	 * @param message
	 */
	public void sendMessageFromMinecraft(String message) {
		generalChannel.sendMessage("**Minecraft**: `" + ChatColor.stripColor(message) + "`").queue();
	}

	/**
	 * Reads a Message from Discord and checks if it is a Command.
	 * Also sends it to the Minecraft Chat.
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
			if(StringUtils.startsWith(message, "shiro hentai") && WauzCore.IP.equals(MAIN_SERVER_IP)) {
				channel.sendMessage(getHentaiString(channel, message)).queue();
				return;
			}
			if(!channel.getId().equals(botsChannel.getId()) && WauzCore.IP.equals(MAIN_SERVER_IP)) {
				channel.sendMessage("No! Try this again here: " + botsChannel.getAsMention()).queue();
				return;
			}
			if(StringUtils.startsWith(message, "shiro servers") && isMaster(id)) {
				channel.sendMessage("`" + WauzCore.getServerKey() + "` `" + WauzCore.IP + "`").queue();
				return;
			}
			if(StringUtils.startsWith(message, "shiro command " + WauzCore.IP + " ") && isMaster(id)) {
				channel.sendMessage(executeCommand(message)).queue();
				return;
			}
			if(!WauzCore.IP.equals(MAIN_SERVER_IP)) {
				return;
			}
			
// Main Server Commands
				
			if(StringUtils.startsWith(message, "shiro help"))
				channel.sendMessage(getHelpString()).queue();
				
			else if(StringUtils.startsWith(message, "shiro profile "))
				channel.sendMessage(getProfileString(message)).queue();
			
// Music Player
			
			else if(StringUtils.startsWith(message, "shiro play "))
				playAudio(message, channel, false);
			
			else if(StringUtils.startsWith(message, "shiro playnow "))
				playAudio(message, channel, true);
			
			else if(StringUtils.startsWith(message, "shiro skip"))
				skipAudio();
			
			else if(StringUtils.startsWith(message, "shiro stop"))
				leaveAudioChannel();
			
// Fun Stuff
			
			else if(StringUtils.containsAny(message.toLowerCase(), "marc", "clara", "clarc", "gay", "gae"))
				channel.sendMessage("Marc is really, really gay!").queue();
			
			else if(StringUtils.containsAny(message.toLowerCase(), "good girl", "pat"))
				channel.sendMessage(":3").queue();
			
			else if(StringUtils.containsAny(message.toLowerCase(), "attack", "atacc", "stand"))
				channel.sendMessage("ORAORAORAORAORA").queue();
			
			else if(StringUtils.containsAny(message.toLowerCase(), "die", "baka", "fuck"))
				channel.sendMessage("Baka!").queue();
		}
		catch(Exception e) {
			WauzDebugger.catchException(getClass(), e);
		}
	}
	
	/**
	 * @param id The ID of an Discord User
	 * @return If the User has Administrator Access to the Bot.
	 */
	private boolean isMaster(String id) {
		return id.equals(ADMIN) || id.equals(jda.getSelfUser().getId());
	}
	
	/**
	 * Executes a Minecraft Command, sent from Discord to this Server.
	 * 
	 * @param message The Message that contains the Commands.
	 * @return The Response from the Bot.
	 */
	private String executeCommand(String message) {
		try {
			String command = StringUtils.substringAfter(message, "shiro command " + WauzCore.IP + " ");
			Bukkit.getScheduler().callSyncMethod(WauzDiscord.getInstance(),
					() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
			
			return "Your command was executed on " + WauzCore.IP + ", my master!";
		}
		catch(Exception e) {
			return ERROR_MESSAGE;
		}
	}
	
	/**
	 * Gives out a Minecraft Profile, requested from Discord.
	 * 
	 * @param message The Message that contains the Minecraft Name.
	 * @return The Response from the Bot.
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
//					+ "http://31.214.208.243:7069/player.html?plr=" + player.getName();

			return profileString;
		}
		catch(Exception e) {
			return "Sorry! Shiro doesn't know this player. :/";
		}
	}
	
	/**
	 * @return A List of Command Syntax for Controlling the Bot from Discord.
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
	 * Finds a Hentai Image from Rule 34, with an optional Tag.
	 * I am not proud of this one.
	 * 
	 * @param channel The Channel to check if it contains "nsfw" in its Name.
	 * @param message The Message that may contain a Tag.
	 * @return The Response from the Bot.
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
	 * Tells the Bot to play a Song from a Discord Command with a YouTube URL.
	 * 
	 * @param message The Message that contains the Song URL.
	 * @param channel The Channel to write the Response.
	 * @param startPlaying If the Bot should instantly skip to this Song.
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
	 * Tells the Bot to join the Audio Channel from its Config.
	 */
	private void joinAudioChannel() {
		audioManager.setSendingHandler(getLavaPlayerAudioSendHandler());
		audioManager.openAudioConnection(audioChannel);
	}
	
	/**
	 * Tells the Bot to leave its current Audio Channel.
	 */
	private void leaveAudioChannel() {
		audioManager.closeAudioConnection();
		audioPlayer.stopTrack();
		audioQueue.clear();
	}
	
	/**
	 * Sets up an Audio Player.
	 */
	private void configureLavaPlayer() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayer = audioPlayerManager.createPlayer();
		trackScheduler = new TrackScheduler();
		audioPlayer.addListener(trackScheduler);
	}
	
	/**
	 * The Audio Handler to send Sound from its Source to the current Audio Channel.
	 * 
	 * @return a new Instance of the Handler.
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
	 * Tells the Bot to play a Song.
	 * 
	 * @param identifier The Identifier that a specific Source Manager should be able to find the Track with.
	 * @param channel The Channel to write the Response.
	 * @param startPlaying If the Bot should instantly skip to this Song.
	 */
	private void addTrackToLavaPlayer(String identifier, MessageChannel channel, boolean startPlaying) {
		audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {
			
			/**
			 * If a Single track was loaded, it gets added to the Audio Queue.
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
			 * If a Playlist was loaded, its Tracks get added to the Audio Queue.
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
			 * Print an Error if the Source is unreadable.
			 */
			@Override
			public void noMatches() {
				channel.sendMessage("Sorry! Shiro can't read this audio source!").queue();
			}
			
			/**
			 * Print an Error if the Loading of the Track caused an Exception.
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
	 * A Subclass of the Bot to handle Audio Events.
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
		 * If a Track has Ended, load the next one
		 * or leave the Channel if nobody is Listening.
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
		 * The Audio Track could not provide any Audio, so it will be skipped.
		 */
		@Override
		public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
			skipAudio();
		}
		
	}
	
}
