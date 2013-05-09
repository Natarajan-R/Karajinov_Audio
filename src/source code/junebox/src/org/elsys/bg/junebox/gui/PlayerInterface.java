package org.elsys.bg.junebox.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.service.ShellMover;

public class PlayerInterface {
	
	public void draw(Shell shell) {		
		new DropMenu().createNewMenu(shell);
		
		MainWindowInterface mwc = MainWindowInterface.getInstance();
		mwc.createSongNameLable(shell);
		
		Composite mainComp = createComp(shell, new GridLayout(3,false), new GridData(SWT.FILL,SWT.FILL,true,true));
		Composite controls = createComp(mainComp, new GridLayout(2,false), new GridData(SWT.FILL,SWT.FILL,true,true));
		mwc.createSeeker(controls);
		mwc.createControls(controls);
		
		Composite volume = createComp(mainComp, new GridLayout(), new GridData(SWT.FILL,SWT.FILL,false,true));
		mwc.createVolumeControl(volume);
		
		Composite PlaylistButtons = createComp(mainComp, new FillLayout(SWT.VERTICAL), new GridData(SWT.FILL,SWT.CENTER,false,true));
		mwc.createPlaylistButtons(PlaylistButtons);
		ShellMover.getInstance().setMovers(PlaylistButtons);
		
		PlaylistInterface.getInstance().createNewPL(shell);		
	}
	
	private Composite createComp(Composite parent ,Layout layout, GridData data) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(layout);
		comp.setLayoutData(data);
		ShellMover.getInstance().setMovers(comp);
		
		return comp;
	}
}
