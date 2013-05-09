package org.elsys.bg.junebox;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.gui.PlayerInterface;
import org.elsys.bg.junebox.service.ConfigurationLoader;
import org.elsys.bg.junebox.service.ShellMover;

public class Main {

	public static void main(String[] args) {
			
		final Display display = new Display();
		Shell programShell = new Shell(display);
		
		programShell.setAlpha(0);
		programShell.setVisible(true);
		
		Shell shell = new Shell(programShell,SWT.CLOSE|SWT.MIN);
		
		shell.setBounds(400,100,400,150);
		shell.setLayout(new GridLayout());
		shell.setText("junebox");
		
		new PlayerInterface().draw(shell);

		ShellMover.getInstance().setMovers(shell);
		shell.open();

		ConfigurationLoader.getInstance().loadPlayerConfigurations();
		
		while(!shell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
	}
	
}

