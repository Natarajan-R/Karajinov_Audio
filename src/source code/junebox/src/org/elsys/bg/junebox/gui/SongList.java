package org.elsys.bg.junebox.gui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.player.AudioPlayer;
import org.elsys.bg.junebox.playlist.Playlist;

public class SongList {

	private List swtList;
	private Shell shell;
	
	public void createSWTList(Shell parent) {
		shell = parent;
		swtList = new List(parent, SWT.BORDER|SWT.V_SCROLL|SWT.MULTI);
		swtList.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		addListListeners();
	}
	
	private void addListListeners() {
		swtList.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.DEL) {
					if(swtList.getSelectionIndex() > -1) {
						int[] index = swtList.getSelectionIndices();
						removeSongs(index);
					}
				}
			}
		});
		
		swtList.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				if(AudioPlayer.getInstance().isEnableGUI()) {
					int index = swtList.getSelectionIndex();
					if(index > -1)
						AudioPlayer.getInstance().play(index);
				}
			}
		});
	}
	
	public void hide() {
		shell.setVisible(false);
	}
	
	public void show() {
		shell.setLocation(shell.getParent().getBounds().x, shell.getParent().getBounds().y+155);
		shell.setVisible(true);
	}
	
	public void removeSongs(int[] index) {
		swtList.remove(index);
		Playlist.getInstance().removeSongs(index);
	}
	
	public void removeSong(int index) {
		swtList.remove(index);
		Playlist.getInstance().removeSong(index);
	}
	
	public void clearSongList() {
		if(Playlist.getInstance().size() > 0){
			swtList.removeAll();
			Playlist.getInstance().clearPlaylist();
			Playlist.getInstance().printPL();
		}
	}
	
	public void addSong(Song song) {
		swtList.add(song.getName());
		Playlist.getInstance().addToPlaylist(song);
	}
	
	public void addSongList(ArrayList<Song> list) {
		for(int i = 0;i < list.size(); i++){
			swtList.add(list.get(i).getName());
			Playlist.getInstance().addToPlaylist(list.get(i));
		}
	}
	
	public void sort() {
		Playlist.getInstance().sortPL();
		swtList.removeAll();
		for(int i=0;i<Playlist.getInstance().size();++i){
			swtList.add(Playlist.getInstance().getPlaylist().get(i).getName());
		}
	}
	
	public List getList() {
		return swtList;
	}

	public int getSelectionIndex() {
		return swtList.getSelectionIndex();
	}

	public void setSelection(int songIndex) {
		swtList.setSelection(songIndex);
	}
}
