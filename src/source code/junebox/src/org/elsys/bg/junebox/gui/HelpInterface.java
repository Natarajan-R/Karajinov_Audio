package org.elsys.bg.junebox.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class HelpInterface {
	
	private Shell shell = null;
	
	public void draw(Shell parent) {
		if(shell == null || shell.isDisposed()) createHelpWindow(parent);
	}
	
	private void createHelpWindow(Shell parent) {
		shell = new Shell(parent, SWT.CLOSE);
		shell.setLayout(new GridLayout(2,true));
		shell.setLocation(parent.getLocation().x + 50, parent.getLocation().y);
		shell.setSize(300, 260);
		shell.setText("Help");
		
		createImageInfo("gtk-media-play-ltr.png", "play song");
		createImageInfo("go-up.png", "hide playlist");
		createImageInfo("gtk-media-pause.png", "pause song");
		createImageInfo("go-down.png", "show playlist");
	    createImageInfo("gtk-media-stop.png", "stop song");
		createImageInfo("list-add.png", "add songs");
		createImageInfo("gtk-media-forward-rtl.png", "previous song");
	    createImageInfo("sort.png", "sort playlist");
	    createImageInfo("gtk-media-forward-ltr.png", "next song");
	    createImageInfo("filesave.png", "save playlist");
		createImageInfo("media-playlist-shuffle.png", "shuffle playlist");	
		createImageInfo("document-open.png", "open media");
		createImageInfo("media-playlist-repeat.png", "repeat playlist");	
	    createImageInfo("user-trash.png", "clear playlist");
	
		shell.open();
	}
	
	private void createImageInfo(String name, String description) {
		Composite comp = createComposite(shell);

		Label imageLabel = createImageLabel(comp);
		imageLabel.setImage(new Image(shell.getDisplay(), this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/" + name)));
		@SuppressWarnings("unused")
		Label arrow = createTextLabel(comp, " - > ");
		@SuppressWarnings("unused")
		Label textLabel = createTextLabel(comp, description);
	}
	
	private Composite createComposite(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(3,false));
		comp.setLayoutData(new GridData(SWT.LEFT,SWT.FILL,false,false));
		
		return comp;
	}
	
	private Label createTextLabel(Composite parent, String text) {
	    Label label = new Label(parent, SWT.LEFT);
	    label.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,true,true));
	    label.setText(text);
	    
	    return label;
	}
	
	private Label createImageLabel(Composite parent) {
	    Label label = new Label(parent, SWT.LEFT);
	    label.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,true,true));
	    
	    return label;
	}
}
