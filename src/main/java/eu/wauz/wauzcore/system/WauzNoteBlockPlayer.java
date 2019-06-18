package eu.wauz.wauzcore.system;

import java.io.File;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.players.WauzPlayerData;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;

public class WauzNoteBlockPlayer {
	
	private static WauzCore core = WauzCore.getInstance();
	
	public static void play(Player player) {
		play(player, new File(player.getWorld().getWorldFolder().getAbsolutePath() + "/soundtrack.nbs"));
	}
	
	public static void play(Player player, String songName) {
		play(player, new File(core.getDataFolder(), "Music/" + songName + ".nbs"));
	}
	
	private static void play(Player player, File soundtrackFile) {
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null)
			return;
		
		SongPlayer currentSongPlayer = pd.getSongPlayer();
		if(currentSongPlayer != null)
			currentSongPlayer.destroy();
		
		if(soundtrackFile.exists()) {
			Song song = NBSDecoder.parse(soundtrackFile);
			SongPlayer songPlayer = new RadioSongPlayer(song);
			songPlayer.setAutoDestroy(true);
			songPlayer.setRepeatMode(RepeatMode.ALL);
			songPlayer.setVolume((byte) 30);
			songPlayer.addPlayer(player);
			songPlayer.setPlaying(true);
			
			WauzDebugger.log(player, "Loaded Soundtrack: " + soundtrackFile.getAbsolutePath());
			pd.setSongPlayer(songPlayer);
		}
		else {
			WauzDebugger.log(player, "No Soundtrack found: " + soundtrackFile.getAbsolutePath());
			pd.setSongPlayer(null);
		}
	}

}
