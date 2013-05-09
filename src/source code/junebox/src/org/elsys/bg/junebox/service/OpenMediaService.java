package org.elsys.bg.junebox.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.data.Song;

public class OpenMediaService {
	
	private File fDir;
	private ArrayList<Song> songs;
	private boolean playlistOpened;
	private static OpenMediaService instance = null;
	
	private OpenMediaService() {}
	
	public static synchronized OpenMediaService getInstance(){
		if(instance == null) instance = new OpenMediaService();
		return instance;
	}
	
	public boolean isPlaylistOpened() {
		return playlistOpened;
	}
	
	public ArrayList<Song> openFile(Shell shell) {
		songs = new ArrayList<Song>();
		FileDialog fileDialog = new FileDialog(shell,SWT.MULTI);
		fileDialog.setText("Open File(s)");
		String[] filterExt = { "*.mp3", "*.*" };
		fileDialog.setFilterExtensions(filterExt);
		if (fileDialog.open() != null){
			String[] children = fileDialog.getFileNames();
			for (int i=0; i<children.length; i++) {
				if(children[i].endsWith(".mp3")){
					Song song = new Song(fileDialog.getFilterPath() + "\\" + children[i]);
					song.setName(children[i].replace(".mp3", ""));
					song.setDirectory(fileDialog.getFilterPath() + "\\");
					songs.add(song);
				}
			}
		}
		return songs;
	}
	
	public ArrayList<Song> openFolder(Shell shell) {
		songs = new ArrayList<Song>();
		DirectoryDialog dlg = new DirectoryDialog(shell);
		dlg.setText("Open Folder");
		dlg.setMessage("Select a directory");
		
		String dir = dlg.open();
		songs.addAll(openDirectiry(dir));		
		return songs;
	}

	private ArrayList<Song> openDirectiry(String dir) {
		ArrayList<Song> songList = new ArrayList<Song>();
		String[] children;
		if (dir != null){
			fDir = new File(dir);
			children = fDir.list();
			if(children != null) {
				for (int i=0; i<children.length; i++){
					if(children[i].endsWith(".mp3")){
						Song song = new Song(fDir.getAbsolutePath() + "\\" + children[i]);
						song.setName(children[i].replace(".mp3", ""));
						song.setDirectory(fDir.getAbsolutePath() + "\\");
						songList.add(song);
					}
				}
			}
		}
		return songList;
	}
	
	public ArrayList<Song> openPlaylist(Shell shell) {
		songs = new ArrayList<Song>();
		playlistOpened = false;
		File playlist = null;
		FileDialog fileDialog = new FileDialog(shell,SWT.OPEN);
		fileDialog.setText("Open Playlist");
		String[] filterExt = { "*.m3u", "*.*" };
		fileDialog.setFilterExtensions(filterExt);
		if(fileDialog.open() != null)
			if(fileDialog.getFileName().endsWith(".m3u")) {
				playlist = new File(fileDialog.getFilterPath() + "\\" + fileDialog.getFileName());
				playlistOpened = true;
			}
			try {
				FileReader fr = new FileReader(playlist);
				BufferedReader br = new BufferedReader(fr);
				
				while(br.ready()) {
					String line = br.readLine();
					if(!line.startsWith("#")) {
						String[] str = line.split("\\\\");
						String dir = "";
						if(str[str.length-1].endsWith(".mp3")) {
							for(int i =0; i<str.length-1;i++)
								dir += str[i] + "\\";
							Song song = new Song(line);
							song.setName(str[str.length-1].replace(".mp3", ""));
							song.setDirectory(dir);
							songs.add(song);
						} else songs.addAll(openDirectiry(line));
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException handled in OpenMediaService");
			} catch (IOException e) {
				System.out.println("IOException handled in OpenMediaService");
			} catch (NullPointerException e) {
				System.out.println("NullPointerException handled in OpenMediaService");
			}

		return songs;
	}
	
}
