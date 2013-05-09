package org.elsys.bg.junebox.gui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.playlist.Playlist;
import org.elsys.bg.junebox.playlist.service.PlaylistSaver;

public class DropMenu {
	
	private Shell parentShell = null;
	private AboutInterface aboutInterface = null;
	private HelpInterface helpInterface = null;
	
	public void createNewMenu(final Shell shell) {
		aboutInterface = new AboutInterface();
		helpInterface = new HelpInterface();
		
		parentShell = shell.getShell();
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		createMenuFile(shell, menuBar);
		createMenuOptions(shell, menuBar);
		createMenuAbout(shell, menuBar);
	}

	private void createMenuAbout(final Shell shell, Menu menuBar) {
		MenuItem aboutItem = newMenuField(menuBar, "&About");
		Menu aboutSubMenu = new Menu(shell, SWT.DROP_DOWN);
		aboutItem.setMenu(aboutSubMenu);
		
		MenuItem about = newSubMenu(aboutSubMenu,"&About\tCtrl+A");
		about.setAccelerator(SWT.CTRL + 'A');
		addAboutListener(about);
		
		MenuItem help = newSubMenu(aboutSubMenu,"&Help\tCtrl+H");
		help.setAccelerator(SWT.CTRL + 'H');
		addHelpListener(help);
	}

	private void createMenuOptions(final Shell shell, Menu menuBar) {
		MenuItem OptionsItem = newMenuField(menuBar, "&Options");
		Menu optionsSubmenu = new Menu(shell, SWT.DROP_DOWN);
		OptionsItem.setMenu(optionsSubmenu);
		
		MenuItem enableShuffle = newSubMenu(optionsSubmenu,"Enable &Shuffle\tShift+E");
		enableShuffle.setAccelerator(SWT.SHIFT + 'E');
		addShuffleListener(enableShuffle);
		
		MenuItem enableRepeat = newSubMenu(optionsSubmenu,"Enable &repeat\tShift+R");
		enableRepeat.setAccelerator(SWT.SHIFT + 'R');
		addRepeatListener(enableRepeat);
		
		MenuItem enableHidePlaylist = newSubMenu(optionsSubmenu,"Hide &playlist\tShift+H");
		enableHidePlaylist.setAccelerator(SWT.SHIFT + 'H');
		addHideListener(enableHidePlaylist);
	}

	private void createMenuFile(final Shell shell, Menu menuBar) {
		MenuItem fileItem = newMenuField(menuBar, "&File");
		Menu fileSubmenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileSubmenu);
		
		MenuItem openFile = newSubMenu(fileSubmenu,"&Open...\tCtrl+O");
		openFile.setAccelerator(SWT.CTRL + 'O');
		addOpenFileListener(shell, openFile);
		
		MenuItem savePlaylist = newSubMenu(fileSubmenu,"Save &Playlist\tCtrl+S");
		savePlaylist.setAccelerator(SWT.CTRL + 'S');
		addSavePlaylistListener(shell, savePlaylist);
		
		MenuItem exit = newSubMenu(fileSubmenu,"&Exit\tCtrl+E");
		exit.setAccelerator(SWT.CTRL + 'E');
		addExitListener(exit);
	}

	private void addAboutListener(MenuItem about) {
		about.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				aboutInterface.draw(parentShell);
			}
		});
	}

	private void addHelpListener(MenuItem help) {
		help.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				helpInterface.draw(parentShell);
			}
		});
	}
	
	private void addHideListener(MenuItem enableHidePlaylist) {
		enableHidePlaylist.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				MainWindowInterface.getInstance().enableHidePlaylist();
			}
		});
	}

	private void addRepeatListener(MenuItem enableRepeat) {
		enableRepeat.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				MainWindowInterface.getInstance().enableRepeat();
			}
		});
	}

	private void addShuffleListener(MenuItem enableShuffle) {
		enableShuffle.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				MainWindowInterface.getInstance().enableShuffle();
			}
		});
	}

	private void addExitListener(MenuItem exit) {
		exit.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				System.exit(0);
			}
		});
	}

	private void addSavePlaylistListener(final Shell shell,
			MenuItem savePlaylist) {
		savePlaylist.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				PlaylistSaver.getInstance().savePlaylist(Playlist.getInstance().getPlaylist(), shell);
			}
		});
	}

	private void addOpenFileListener(final Shell shell, MenuItem openFile) {
		openFile.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				ArrayList<Song> songs = new ArrayList<Song>();
				songs = OpenFileInterface.getInstance().openFileInit(shell.getShell());
				if(!songs.isEmpty()) {
					PlaylistInterface.getInstance().getSongList().clearSongList();
					PlaylistInterface.getInstance().getSongList().addSongList(songs);
				}
			}
		});
	}
	
	public static MenuItem newMenuField(Menu parent, String name) {
		MenuItem temp = new MenuItem (parent, SWT.CASCADE);
		temp.setText(name);
			
		return temp;
	}
	
	public static MenuItem newSubMenu(Menu submenu, String name) {
		MenuItem item = new MenuItem(submenu, SWT.PUSH);
		item.setText (name);
		
		return item;
	}
}
