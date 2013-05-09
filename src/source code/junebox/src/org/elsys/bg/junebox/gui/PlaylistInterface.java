package org.elsys.bg.junebox.gui;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.playlist.Playlist;
import org.elsys.bg.junebox.playlist.service.PlaylistSaver;
import org.elsys.bg.junebox.service.ShellMover;

public class PlaylistInterface {

	private Shell shell;
	private Shell mShell;
	private SongList swtList;
	
	private Playlist playlist = Playlist.getInstance();
	private static volatile PlaylistInterface instance = null;	
	
	private PlaylistInterface() {}
	
	public static synchronized PlaylistInterface getInstance() {
		if(instance == null) instance = new PlaylistInterface();
		return instance;
	}
	
	public void createNewPL(Shell mainShell) {
		mShell = mainShell;
		
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,false);
		shell = new Shell(mainShell.getShell(),SWT.RESIZE);
		shell.setBounds(mainShell.getBounds().x, mainShell.getBounds().y + mainShell.getBounds().height + 5, 400, 400);
		shell.setLayout(new GridLayout(1,false));
		shell.setMinimumSize(400, 300);
		
		Composite mainComp = createComp(shell ,new GridLayout(5,false), data);
		createPLControls(mainComp);
		
		swtList = new SongList();
		swtList.createSWTList(shell);
		
		ShellMover.getInstance().setMovers(shell);
		
		shell.open();
	}
	
	public void createPLControls(final Composite parent) {
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,false);

		Image addImage = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/list-add.png"));
		Image openImage = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/document-open.png"));
		Image clearImage = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/user-trash.png"));
		Image saveImage = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/filesave.png"));
		Image sortImage = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/sort.png"));
		
		Button clear = createButton(parent, data, clearImage);
		Button open = createButton(parent, data, openImage);	
		Button append = createButton(parent, data, addImage);		
		Button save = createButton(parent, data, saveImage);
		Button sort = createButton(parent, data, sortImage);
		
		addSaveButtonListener(save);
		addAppendButtonListener(append);
		addClearButtonListener(clear);
		addOpenButtonListener(open);
		addSortButtonListener(sort);
	}

	private void addSortButtonListener(Button sort) {
		sort.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				swtList.sort();
			}
		});
	}

	private void addClearButtonListener(Button clear) {
		clear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				swtList.clearSongList();
			}
		});
	}
	
	private void addOpenButtonListener(Button open) {
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ArrayList<Song> songs = new ArrayList<Song>();
				songs = OpenFileInterface.getInstance().openFileInit(mShell);
				if(!songs.isEmpty()) {
					swtList.clearSongList();
					swtList.addSongList(songs);
				}
			}
		});
	}
	
	private void addAppendButtonListener(Button openPL) {
		openPL.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				swtList.addSongList(OpenFileInterface.getInstance().openFileInit(mShell));
			}
		});
	}

	private void addSaveButtonListener(Button savePL) {
		savePL.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PlaylistSaver.getInstance().savePlaylist(playlist.getPlaylist(), mShell);
			}
		});
	}
	
	private Button createButton(Composite parent, GridData gridData, Image image) {
		Button button = new  Button(parent, SWT.PUSH);
		button.setLayoutData(gridData);
		button.setImage(image);
		return button;
	}
	
	private Composite createComp(Composite parent , Layout layout, GridData gridData) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(layout);
		comp.setLayoutData(gridData);
		ShellMover.getInstance().setMovers(comp);
		
		return comp;
	}
	
	public Shell getShell() {
		return shell;
	}
	
	public SongList getSongList() {
		return swtList;
	}
}
