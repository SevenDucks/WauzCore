package eu.wauz.wauzcore.discord.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * A subclass of the music manager to handle audio events.
 * 
 * @author Wauzmons
 */
public class WauzAudioEventAdapter extends AudioEventAdapter {
	
	/**
	 * The manager for music functionalities.
	 */
	private MusicManager musicManager;
	
	/**
	 * Creates a new audio event adapter for the given bot.
	 * 
	 * @param musicManager The manager for music functionalities.
	 */
	public WauzAudioEventAdapter(MusicManager musicManager) {
		this.musicManager = musicManager;
	}
	
	/**
	 * If a track has ended, load the next one
	 * or leave the channel if nobody is listening.
	 */
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			musicManager.skipAudio();
		}
		else if(endReason.equals(AudioTrackEndReason.CLEANUP)) {
			musicManager.leaveAudioChannel();
		}
	}

	/**
	 * The audio track could not provide any audio, so it will be skipped.
	 */
	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		musicManager.skipAudio();
	}

}
