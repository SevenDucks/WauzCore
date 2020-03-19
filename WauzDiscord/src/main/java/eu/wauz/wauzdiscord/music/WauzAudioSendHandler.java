package eu.wauz.wauzdiscord.music;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import eu.wauz.wauzdiscord.ShiroDiscordBot;
import net.dv8tion.jda.api.audio.AudioSendHandler;

/**
 * A subclass of the bot to handle the sending of audio frames.
 * 
 * @author Wauzmons
 */
public class WauzAudioSendHandler implements AudioSendHandler {
	
	/**
	 * The Discord bot running on the server.
	 */
	private ShiroDiscordBot shiroDiscordBot;
	
	/**
	 * Creates a new audio send handler for the given bot.
	 * 
	 * @param shiroDiscordBot The Discord bot running on the server.
	 */
	public WauzAudioSendHandler(ShiroDiscordBot shiroDiscordBot) {
		this.shiroDiscordBot = shiroDiscordBot;
	}
	
	/**
	 * The last played audio frame.
	 */
	private AudioFrame lastFrame;
	
	/**
	 * @return If any audio frames are left, to be played.
	 */
	@Override
	public boolean canProvide() {
		lastFrame = shiroDiscordBot.getAudioPlayer().provide();
		return lastFrame != null;
	}
	
	/**
	 * @return The latest audio frame as byte buffer.
	 */
	@Override
	public ByteBuffer provide20MsAudio() {
		return ByteBuffer.wrap(lastFrame.getData());
	}
	
	/**
	 * @return Always true, to tell Disocrd, that the audio is already encoded aus opus.
	 */
	@Override
	public boolean isOpus() {
		return true;
	}

}
