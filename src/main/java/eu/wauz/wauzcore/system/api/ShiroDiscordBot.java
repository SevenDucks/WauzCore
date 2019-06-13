package eu.wauz.wauzcore.system.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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

public class ShiroDiscordBot extends ListenerAdapter {
	
	private JDA jda;
	
	private Guild guild;
	
	private TextChannel generalChannel;
	
	private TextChannel botsChannel;
	
	private AudioManager audioManager;
	
	private AudioPlayerManager audioPlayerManager;
	
	private AudioPlayer audioPlayer;
	
	private TrackScheduler trackScheduler;
	
	private List<AudioTrack> audioQueue = new ArrayList<>();
	
	private final String TOKEN = "NDkwNDEzOTY0NjkwNDU2NTk3.Dn4--A._DlC-V4f1QxHW-9Lo9rN5bh_9Kw";
	
	private final String ADMIN = "271386371950772224";
	
	private final String ERROR_MESSAGE_DEFAULT = "Oopsy! Shiro thinks something went wrong. :/";
	
	public ShiroDiscordBot() {
		JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
		jdaBuilder.setToken(TOKEN);
		jdaBuilder.setAutoReconnect(true);
		jdaBuilder.setStatus(OnlineStatus.ONLINE);
		jdaBuilder.setGame(Game.of(GameType.DEFAULT, "with Lord Wauzmons"));
		
		try {
			jda = jdaBuilder.buildBlocking();
			jda.addEventListener(this);
			guild = jda.getGuildById(272417655938351106L);
			generalChannel = jda.getTextChannelById(272417655938351106L);
			botsChannel = jda.getTextChannelById(574219369530261514L);
			audioManager = guild.getAudioManager();
			configureLavaPlayer();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		jda.getPresence().setStatus(OnlineStatus.OFFLINE);
		jda.shutdownNow();
	}
	
	public void sendMessage(String message) {
		generalChannel.sendMessage(message).queue();
	}

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
			
			if(!StringUtils.startsWithIgnoreCase(message, "shiro")) {
				return;
			}
			if(!channel.getId().equals(botsChannel.getId()) && WauzCore.IP.equals("31.214.208.243")) {
				channel.sendMessage("No! Try this again here: " + botsChannel.getAsMention()).queue();;
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
			if(!WauzCore.IP.equals("31.214.208.243")) {
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

		}
	}
	
	private boolean isMaster(String id) {
		return id.equals(ADMIN) || id.equals(jda.getSelfUser().getId());
	}
	
	private String executeCommand(String message) {
		try {
			String command = StringUtils.substringAfter(message, "shiro command " + WauzCore.IP + " ");
			Bukkit.getScheduler().callSyncMethod(WauzCore.getInstance(),
					() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
			
			return "Your command was executed on " + WauzCore.IP + ", my master!";
		}
		catch(Exception e) {
			return ERROR_MESSAGE_DEFAULT;
		}
	}
	
	private String getProfileString(String message) {
		try {
			OfflinePlayer player = WauzCore.getOfflinePlayer(message.split(" ")[2]);
			if(player.getFirstPlayed() == 0 && player.getLastPlayed() == 0)
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
	
	private String getHelpString() {
		String helpString = "";
		String br = System.lineSeparator();
		helpString += "--- NORMAL COMMANDS ---";
		helpString += br + "`shiro profile <Minecraft Name (Case Sensitive)>` shows player stats";
		helpString += br + "`shiro play <YouTube-URL>` plays song in \"Musichouse\"";
		helpString += br + "`shiro playnow <YouTube-URL>` same but instantly skip to song";
		helpString += br + "`shiro skip` skips to next song in queue";
		helpString += br + "`shiro stop` stops song and clears queue";
		helpString += br + br + "--- ADMIN COMMANDS ---";
		helpString += br + "`shiro servers` lists all running servers";
		helpString += br + "`shiro command <IP> <Command>` executes commands on remote machine";
		return helpString;
	}
	
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
	
	private void joinAudioChannel() {
		VoiceChannel audioChannel = guild.getVoiceChannelById(574183802260422677L);
		audioManager.setSendingHandler(getLavaPlayerAudioSendHandler());
		audioManager.openAudioConnection(audioChannel);
	}
	
	private void leaveAudioChannel() {
		audioManager.closeAudioConnection();
		audioPlayer.stopTrack();
		audioQueue.clear();
	}
	
	private void configureLavaPlayer() {
		audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayer = audioPlayerManager.createPlayer();
		trackScheduler = new TrackScheduler();
		audioPlayer.addListener(trackScheduler);
	}
	
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
	
	private void addTrackToLavaPlayer(String identifier, MessageChannel channel, boolean startPlaying) {
		audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {
			
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
			
			@Override
			public void noMatches() {
				channel.sendMessage("Sorry! Shiro can't read this audio source!").queue();
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				if(exception.severity.equals(Severity.COMMON))
					channel.sendMessage("Shiro got this error: " + exception.getMessage()).queue();
				else
					channel.sendMessage(ERROR_MESSAGE_DEFAULT).queue();
			}
			
		});
	}
	
	private class TrackScheduler extends AudioEventAdapter {

//		@Override
//		public void onPlayerPause(AudioPlayer player) {
//			// Player was paused
//		}
//
//		@Override
//		public void onPlayerResume(AudioPlayer player) {
//			// Player was resumed
//		}
//
//		@Override
//		public void onTrackStart(AudioPlayer player, AudioTrack track) {
//			// A track started playing
//		}

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

//		@Override
//		public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
//			// An already playing track threw an exception (track end event will still be received separately)
//		}

		@Override
		public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
			// Audio track has been unable to provide us any audio, might want to just start a new track
			skipAudio();
		}
		
	}
	
}
