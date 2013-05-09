package org.elsys.bg.junebox.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.gui.MainWindowInterface;
import org.elsys.bg.junebox.gui.PlaylistInterface;
import org.elsys.bg.junebox.playlist.Playlist;

public class ConfigurationLoader {
	
	private static ConfigurationLoader instance;
	private MainWindowInterface mwc;
	private Playlist playlist;
	private ArrayList<Song> songs;
	private int selectionIndex = -1;
	private enum property {selection,songlabel,volume,shuffle,repeat,hide,playlist};
	
	private ConfigurationLoader() {}
	
	public static synchronized ConfigurationLoader getInstance(){
		if(instance == null) instance = new ConfigurationLoader();
		return instance;
	}
	
	public void loadPlayerConfigurations() {
		mwc = MainWindowInterface.getInstance();
		playlist = Playlist.getInstance();
		songs = new ArrayList<Song>();
		try {
			File confingFile = new File(".//pconfig.jnb");
			if(confingFile.exists()) {
				FileReader fr = new FileReader(confingFile);
				BufferedReader br = new BufferedReader(fr);
				
				while(br.ready()) {
					String line = br.readLine();
					String result[] = line.split(" - ");
					
					loadProperty(br, result);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException handled in ConfigurationLoader");
		} catch (IOException e) {
			System.out.println("IOException handled in ConfigurationLoader");
		} catch (NullPointerException e) {
			System.out.println("NullPointerException handled in ConfigurationLoader");
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException handled in ConfigurationLoader");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException handled in ConfigurationLoader");
		}
	}

	private void loadProperty(BufferedReader br, String[] result) throws IOException {
		switch (property.valueOf(result[0])){
			case selection:
				selectionIndex = Integer.parseInt(result[1]);
			break;
				
			case songlabel:
				setSongName(result);
			break;
				
			case volume:
				mwc.getVolumeScale().setSelection(Integer.parseInt(result[1]));
			break;
				
			case shuffle:
				if(result[1].equals("true"))
					mwc.enableShuffle();
			break;
				
			case repeat:
				if(result[1].equals("true"))
					mwc.enableRepeat();
			break;
				
			case hide:
				if(result[1].equals("true"))
					mwc.enableHidePlaylist();
			break;
				
			case playlist:
				loadPlaylist(br);	
				setListSelection();
			break;
		}
	}

	private void setListSelection() {
		PlaylistInterface.getInstance().getSongList().addSongList(songs);
		if(selectionIndex > -1)
			PlaylistInterface.getInstance().getSongList().setSelection(selectionIndex);
		else if(playlist.size() > 0) {
			PlaylistInterface.getInstance().getSongList().setSelection(0);
			mwc.getSongLabel().setText(Playlist.getInstance().getPlaylist().get(0).getName());
		} else if(playlist.size() == 0) mwc.getSongLabel().setText("");
	}

	private void loadPlaylist(BufferedReader br) throws IOException {
		while(br.ready()) {
			String songUrl = br.readLine();
			String[] str = songUrl.split("\\\\");
			String dir = "";
			
			for(int i =0; i<str.length-1;i++)
				dir += str[i] + "\\";
			Song song = new Song(songUrl);
			song.setName(str[str.length-1].replace(".mp3", ""));
			song.setDirectory(dir);
			songs.add(song);
		}
	}

	private void setSongName(String[] result) {
		if(!result[1].equals("null")) {
			String name = result[1];
			for(int i =2;i<result.length; i++)
				name += " - " + result[i];
			mwc.getSongLabel().setText(name);
		}
	}
}
