package eu.wauz.wauzdiscord.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import eu.wauz.wauzdiscord.ShiroDiscordBot;

/**
 * A subclass of the bot to handle audio events.
 * 
 * @author Wauzmons
 */
public class WauzAudioEventAdapter extends AudioEventAdapter {
	
	/**
	 * The Discord bot running on the server.
	 */
	private ShiroDiscordBot shiroDiscordBot;
	
	/**
	 * Creates a new audio event adapter for the given bot.
	 * 
	 * @param shiroDiscordBot The Discord bot running on the server.
	 */
	public WauzAudioEventAdapter(ShiroDiscordBot shiroDiscordBot) {
		this.shiroDiscordBot = shiroDiscordBot;
	}
	
	/**
	 * If a track has ended, load the next one
	 * or leave the channel if nobody is listening.
	 */
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			shiroDiscordBot.skipAudio();
		}
		else if(endReason.equals(AudioTrackEndReason.CLEANUP)) {
			shiroDiscordBot.leaveAudioChannel();
		}
	}

	/**
	 * The audio track could not provide any audio, so it will be skipped.
	 */
	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		shiroDiscordBot.skipAudio();
	}

}
