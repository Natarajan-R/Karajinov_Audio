package org.elsys.bg.junebox.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AboutInterface {
	
	private Shell shell = null;
	private String textContent = "Version 1.0\n\n A simple user-friendly audio player" +
    		"\n\nAleksandar Karadzhinov\n<aleksandar.karadjinov@gmail.com>";
	
	public void draw(Shell parent) {
		if(shell == null || shell.isDisposed()) createAboutWindow(parent);
	}
	
	private void createAboutWindow(Shell parent) {
		shell = new Shell(parent,SWT.CLOSE);
		shell.setLayout(new GridLayout());
		shell.setLocation(parent.getLocation().x + 50, parent.getLocation().y);
		shell.setSize(300, 260);
		shell.setText("About junebox");
		
		Image icon = new Image(parent.getDisplay(), this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/speaker.png"));
		
		Composite imageComp = createComposite(shell, new GridData(SWT.FILL,SWT.FILL,false,false));
		Composite textComposite = createComposite(shell, new GridData(SWT.FILL,SWT.FILL,true,true));
		
		Label imageLabel = createLabel(imageComp, new GridData(SWT.CENTER,SWT.TOP,true,true), "");
		imageLabel.setImage(icon);
		
	    Label header = createLabel(textComposite, new GridData(SWT.FILL,SWT.BOTTOM,true,false), "junebox");
	    header.setFont(new Font(parent.getDisplay(), new FontData("default", 16, SWT.BOLD)));

	    @SuppressWarnings("unused")
		Label content = createLabel(textComposite, new GridData(SWT.FILL,SWT.BOTTOM,true,false), textContent);
	    
	    shell.open();
	}
	
	private Composite createComposite(Composite parent, GridData data) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1,false));
		comp.setLayoutData(data);
		
		return comp;
	}
	
	private Label createLabel(Composite parent, GridData data, String text) {
	    Label label = new Label(parent, SWT.CENTER);
	    label.setLayoutData(data);
	    label.setText(text);
	    
	    return label;
	}
}
