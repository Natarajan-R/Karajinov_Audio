package org.elsys.bg.junebox.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.gui.MainWindowInterface;
import org.elsys.bg.junebox.player.AudioPlayer;
import org.elsys.bg.junebox.playlist.Playlist;


public class ConfigurationSaver {
	private static ConfigurationSaver instance;
	private MainWindowInterface mwc;
	private Playlist playlist;
	
	private ConfigurationSaver() {}
	
	public static synchronized ConfigurationSaver getInstance(){
		if(instance == null) instance = new ConfigurationSaver();
		return instance;
	}
	
	public void savePlayerConfigurations() {
		mwc = MainWindowInterface.getInstance();
		playlist = Playlist.getInstance();
		try {
			FileWriter fw = new FileWriter(new File(".//pconfig.jnb"));
			BufferedWriter bw = new BufferedWriter(fw);
			
			writeSongDetails(bw);
			writeOptionDetails(bw);
			writePlaylist(bw);
			
			bw.flush();
			bw.close();
				
		} catch (IOException e) {
			System.out.println("IOException handled in ConfigurationSaver");
		}
	}

	private void writePlaylist(BufferedWriter bw) throws IOException {
		bw.write("playlist");
		bw.newLine();
		if(playlist.size()>0) {
			ArrayList<Song> songs = playlist.getPlaylist();
			for(int i =0;i<playlist.size();i++) {
				bw.write(songs.get(i).getDirectory() + songs.get(i).getName() + ".mp3");
				bw.newLine();
			}
		}
	}

	private void writeOptionDetails(BufferedWriter bw) throws IOException {
		bw.write("volume - " + mwc.getVolumeScale().getSelection());
		bw.newLine();
		
		bw.write("shuffle - " + mwc.isShuffleEnabled());
		bw.newLine();
		
		bw.write("repeat - " + mwc.isRepeatEnabled());
		bw.newLine();
		
		bw.write("hide - " + mwc.isHideEnabled());
		bw.newLine();
	}

	private void writeSongDetails(BufferedWriter bw) throws IOException {
		Song song = AudioPlayer.getInstance().getSong();
		
		if(playlist.getPlaylist().contains(song)) {
			writeSongDetails(bw, song);
		} else if(playlist.size()>0) {
			song = playlist.getPlaylist().get(0);
			writeSongDetails(bw, song);
		} else {
			bw.write("selection - -1");
			bw.newLine();
			bw.write("songlabel - null");
			bw.newLine();
		}
	}

	private void writeSongDetails(BufferedWriter bw, Song song) throws IOException {
		bw.write("selection - " + playlist.getPlaylist().indexOf(song));
		bw.newLine();
		bw.write("songlabel - " + song.getName());
		bw.newLine();
	}
}
