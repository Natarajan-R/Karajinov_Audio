package org.elsys.bg.junebox.playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.media.Player;

import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.player.AudioPlayer;

public class Playlist {
	
    Player player = null;
	private ArrayList<Song> playlist;
	
	private static volatile Playlist instance = null;
	
	private Playlist() {
		playlist = new ArrayList<Song>();
	}
	
	public static synchronized Playlist getInstance() {
		if(instance == null) instance = new Playlist();
		return instance;
	}
	
	public ArrayList<Song> getPlaylist() {
		return playlist;
	}
	
	public int size() {
		return playlist.size();
	}
	
	public boolean addToPlaylist(Song song) {
		AudioPlayer.getInstance().addSong();
		return playlist.add(song);
	}
	
	public void clearPlaylist() {
		playlist.clear();
		AudioPlayer.getInstance().clearSongList();
	}
    
	public void removeSongs(int[] index) {
		ArrayList<Song> items = new ArrayList<Song>();
		for(int i = 0; i<index.length;i++)
			items.add(playlist.get(index[i])); 
		playlist.removeAll(items);
	}
	
	public void removeSong(int index) {
		playlist.remove(index);
		AudioPlayer.getInstance().removeSong(index);
	}
	
	public void printPL() {
		for(int i = 0; i < playlist.size(); ++i) {
			System.out.println(playlist.get(i).getName());
		}
	}
	
	public void sortPL() {
		Collections.sort(playlist, new Comparator<Song>(){	 
            public int compare(Song o1, Song o2) {
               return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
		AudioPlayer.getInstance().setSongs(playlist);
	}
}