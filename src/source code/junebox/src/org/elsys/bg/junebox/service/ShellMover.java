package org.elsys.bg.junebox.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ShellMover {
	private static ShellMover instance = null;
	private static boolean mousedown = false;
	private static Point position = new Point(0, 0);
	
	private ShellMover() {}
	
	public static synchronized ShellMover getInstance() {
		if(instance == null) instance = new ShellMover();
		return instance;
	}
	
	public void setMovers(Composite comp) {
		final Shell shell = comp.getShell();
		comp.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				mousedown = true;
				position.x = shell.getDisplay().getCursorLocation().x - shell.getLocation().x;
				position.y = shell.getDisplay().getCursorLocation().y - shell.getLocation().y;
			}
		});
		
		comp.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				mousedown = false;
			}
		});
		
		comp.addListener(SWT.MouseMove, new Listener() {
			public void handleEvent(Event event) {
				if(mousedown) shell.setLocation(shell.getDisplay().getCursorLocation().x - position.x, shell.getDisplay().getCursorLocation().y - position.y);
			}
		});
	}
}

