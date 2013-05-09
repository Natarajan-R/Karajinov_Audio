package org.elsys.bg.junebox.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.player.AudioPlayer;


public class Seeker extends Thread {
	
	private Slider slider;
	private Label time;
	private int songTime;
	private AudioPlayer audioPlayer;
	private Song song;
	private boolean stop = false;
	private Shell shell;
	
	public void setSelection(int value) {
		slider.setSelection(value);
		time.setText(calculateTime(value));
	}
	
	public Slider getSeeker() {
		return slider;
	}
	
	private String calculateTime(int seconds) {	
		int min = seconds/60;
		int sec = seconds%60;
	
		return normalizeTime(min) + ":" + normalizeTime(sec);
	}
	
	private String normalizeTime(int seconds) {
		if(seconds > 9) return "" + seconds;
		else return "0" + seconds;
	}

	public void stopActiveThread() {
		stop = true;	
	}
	
	public void createSeeker(Composite parent) {
		shell = parent.getShell();
		audioPlayer = AudioPlayer.getInstance();
		
		slider = new Slider(parent, SWT.HORIZONTAL);
		slider.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		slider.setMaximum(200);
		
		time = new Label(parent, SWT.CENTER);
		time.setText("00:00");
		time.setLayoutData(new GridData(SWT.DEFAULT, SWT.CENTER, false, false));
		
		addSliderMouseListeners();
	}

	private void addSliderMouseListeners() {
		slider.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				song = audioPlayer.getSong();
				if(song != null) {
					audioPlayer.moveToPosiotion(slider.getSelection());
					
					if(audioPlayer.isPaused()) audioPlayer.setResumeTime(slider.getSelection());
					
					songTime = (int) Math.round(audioPlayer.getPlayer().getMediaTime().getSeconds());
					time.setText(calculateTime(songTime));
				}
				
			}

			public void mouseDown(MouseEvent e) {
				song = audioPlayer.getSong();
				if(song != null)
					slider.setMaximum(audioPlayer.getSongTime()+10);
			}
		});
	}

	public void execute() {
		slider.setMaximum((int) audioPlayer.getPlayer().getDuration().getSeconds() + 10);
		new Thread(new Runnable() {
			public void run() {
				while(!shell.isDisposed()) {
					if(stop) {
						stop = false;
						break;
					}
					try { 
						Thread.sleep(1000);
					} catch (Exception e) {
						
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							if(audioPlayer.isPlaying()) {
								songTime = (int) Math.round(audioPlayer.getPlayer().getMediaTime().getSeconds());
								slider.setSelection(songTime);
								time.setText(calculateTime(songTime));
								if(slider.getSelection()+10 == slider.getMaximum()) {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									setSelection(0);
								}
							}
							if(audioPlayer.isStopped())
								if(slider.getSelection() != 0)
									setSelection(0);
						}
					});
				}
			}
		}).start();
	}
}
