package org.elsys.bg.junebox.gui;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.service.OpenMediaService;

public class OpenFileInterface {

	private ArrayList<Song> songs;
	private static OpenFileInterface instance = null;
	
	private OpenFileInterface() {}
	
	public static synchronized OpenFileInterface getInstance(){
		if(instance == null) instance = new OpenFileInterface();
		return instance;
	}
	
	public ArrayList<Song> openFileInit(Shell mShell){
		songs = new ArrayList<Song>();
		final Shell shell = new Shell(mShell,SWT.APPLICATION_MODAL|SWT.CLOSE);
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		shell.setText("Open...");
		shell.setBounds(400, 400, 300, 100);
		shell.setLayout(new GridLayout(3,true));
		
		Button openFile = createButton(shell, data, "File(s)");
		Button openFolder = createButton(shell, data, "Folder");
		Button openPlaylist = createButton(shell, data, "Playlist");
		
		addOpenFileSelectionListener(shell, openFile);
		addOpenFolderSelectionListener(shell, openFolder);
		addOpenPlaylistSelectionListener(shell, openPlaylist);
		
		shell.open();
		
		while(!shell.isDisposed())
			if(!shell.getDisplay().readAndDispatch())
		shell.getDisplay().sleep();
		
		return songs;
	}

	private void addOpenPlaylistSelectionListener(final Shell shell,
			Button openPlaylist) {
		openPlaylist.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				songs = OpenMediaService.getInstance().openPlaylist(shell);
				shell.dispose();
			}
		});
	}

	private void addOpenFolderSelectionListener(final Shell shell,
			Button openFolder) {
		openFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				songs = OpenMediaService.getInstance().openFolder(shell);
				shell.dispose();
			}
		});
	}

	private void addOpenFileSelectionListener(final Shell shell, Button openFile) {
		openFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				songs = OpenMediaService.getInstance().openFile(shell);
				shell.dispose();
			}
		});
	}

	private Button createButton(Composite parent, GridData gridData, String name) {
		Button button = new  Button(parent, SWT.PUSH);
		button.setText(name);
		button.setLayoutData(gridData);
		
		return button;
	}
	
}
