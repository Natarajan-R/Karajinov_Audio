package org.elsys.bg.junebox.playlist.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.data.Song;


public class PlaylistSaver {
	
	private final String header = "#EXTM3U";
	private final String extraInfo = "#EXTINF:";
	private Song tmpSong;
	private static PlaylistSaver instance;
	
	private PlaylistSaver() {}
	
	public static synchronized PlaylistSaver getInstance(){
		if(instance == null) instance = new PlaylistSaver();
		return instance;
	}
	
	public void savePlaylist(ArrayList<Song> songs, Shell shell) {
		FileDialog fileDialog = new FileDialog(shell,SWT.SAVE);
		fileDialog.setText("Save playlist");
		fileDialog.setFilterPath(".//");
		String[] filterExt = { "*.m3u", "*.*" };
		fileDialog.setFilterNames(new String[] { "M3U Playlist", "All Files (*.*)" });
		fileDialog.setFilterExtensions(filterExt);
		
		try {
			FileWriter fw = new FileWriter(fileDialog.open());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(header);
			for(int i=0; i<songs.size();i++) {
				tmpSong = songs.get(i);
				int endIndex = tmpSong.getName().length()-4;
				
				bw.newLine();
				bw.write(extraInfo + "-1," + tmpSong.getName().substring(0, endIndex));
				bw.newLine();
				bw.write(tmpSong.getDirectory() + tmpSong.getName() + ".mp3");
			}
			
			bw.flush();
			bw.close();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("NullPointerException handled in PlaylistSaver");
		}
	}
}
