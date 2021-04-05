package eu.wauz.wauzcore.discord.music;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import eu.wauz.wauzcore.data.DiscordConfigurator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Adds music functionalities and commands to the bot.
 * 
 * @author Wauzmons
 */
public class MusicManager {
	
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
	
	public MusicManager(Guild guild) {
		audioChannel = guild.getVoiceChannelById(DiscordConfigurator.getAudioChannelId());
		audioManager = guild.getAudioManager();

		audioPlayerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayer = audioPlayerManager.createPlayer();
		audioPlayer.addListener(new WauzAudioEventAdapter(this));
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
