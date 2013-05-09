package org.elsys.bg.junebox.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.elsys.bg.junebox.player.AudioPlayer;
import org.elsys.bg.junebox.service.ConfigurationSaver;
import org.elsys.bg.junebox.service.ShellMover;

public class MainWindowInterface {
	
	private Label songName;
	private Seeker seeker;
	private Shell shell;
	private Scale volumeScale;
	
	private boolean shuffleEnabled = false;
	private boolean repeatEnabled = false;
	private boolean hideEnabled = false;
	
	private int index;
	
	private Image hideUp;
	private Image hideDown;
	
	private Button shuffle;
	private Button repeat;
	private Button hidePlaylist;
	
	private static volatile MainWindowInterface instance = null;
	
	private MainWindowInterface() {}
	
	public static synchronized MainWindowInterface getInstance() {
		if(instance == null) instance = new MainWindowInterface();
		return instance;
	}
	
	public Scale getVolumeScale() {
		return volumeScale;
	}
	
	public void enableShuffle() {
		shuffleEnabled = !shuffleEnabled;
		shuffle.setSelection(shuffleEnabled);
	}
	
	public void enableRepeat() {
		repeatEnabled = !repeatEnabled;
		repeat.setSelection(repeatEnabled);
	}
	
	public void enableHidePlaylist() {
		hideEnabled = !hideEnabled;
		if(hideEnabled) {
			PlaylistInterface.getInstance().getSongList().hide();
			hidePlaylist.setImage(hideDown);
		}
		else {
			PlaylistInterface.getInstance().getSongList().show();
			hidePlaylist.setImage(hideUp);
		}
		hidePlaylist.setSelection(hideEnabled);
	}
	
	public boolean isShuffleEnabled() {
		return shuffleEnabled;
	}
	
	public boolean isRepeatEnabled() {
		return repeatEnabled;
	}
	
	public boolean isHideEnabled() {
		return hideEnabled;
	}
	
	public Shell getShell() {
		return shell;
	}
	
	public Seeker getSeeker() {
		return seeker;
	}
	
	public Label getSongLabel() {
		return songName;
	}
	
	public float getVolume() {
		return (float) (100 - volumeScale.getSelection())/100;
	}
	
	public void createVolumeControl(Composite parent) {
		volumeScale = new Scale(parent, SWT.VERTICAL);
		volumeScale.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		volumeScale.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if(AudioPlayer.getInstance().getSong() != null) 
					AudioPlayer.getInstance().getPlayer().getGainControl()
					.setLevel((float) (100 - volumeScale.getSelection())/100);
			}
		});
	}
	
	public void createSongNameLable(Composite parent) {
		songName = new Label(parent, SWT.CENTER);
		songName.setLayoutData(new GridData(SWT.FILL,SWT.BOTTOM,true,false));
		GridLayout gl = (GridLayout) parent.getLayout();
		gl.verticalSpacing = 0;
	}
	
	public void createSeeker(Composite parent){		
		seeker = new Seeker();
		seeker.createSeeker(parent);
	}
	
	public void createPlaylistButtons(final Composite parent) {		
		shuffle = new Button(parent, SWT.TOGGLE);
		repeat = new Button(parent, SWT.TOGGLE);
		hidePlaylist = new Button(parent, SWT.TOGGLE);
		
		hideDown = new Image(shell.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/go-down.png"));
		hideUp = new Image(shell.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/go-up.png"));
		
		Image shuffleIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/media-playlist-shuffle.png"));
		Image repeatIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/media-playlist-repeat.png"));
		
		shuffle.setImage(shuffleIcon);
		repeat.setImage(repeatIcon);
		hidePlaylist.setImage(hideUp);
		
		addHideButtonListener();
		addShuffleButtonListener();
		addRepeatButtonListener();
	}

	private void addRepeatButtonListener() {
		repeat.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				enableRepeat();
			}
		});
	}

	private void addShuffleButtonListener() {
		shuffle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				enableShuffle();
			}
		});
	}

	private void addHideButtonListener() {
		hidePlaylist.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				enableHidePlaylist();
			}
		});
	}
	
	public void createControls(Composite parent) {
		shell = parent.getShell();
		
		GridData data = new GridData(SWT.FILL,SWT.FILL,false,true);
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(5,false));
		comp.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		ShellMover.getInstance().setMovers(comp);
		
		Image prevIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/gtk-media-forward-rtl.png"));
		Image playIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/gtk-media-play-ltr.png"));
		Image pauseIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/gtk-media-pause.png"));
		Image stopIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/gtk-media-stop.png"));
		Image nextIcon = new Image(parent.getDisplay(),this.getClass().getClassLoader()
				.getResourceAsStream("org/elsys/bg/junebox/images/gtk-media-forward-ltr.png"));
		
		Button prev = createButton(comp, prevIcon, new GridData(SWT.RIGHT,SWT.TOP,true,false));
		Button play = createButton(comp, playIcon, data);	
		Button pause = createButton(comp, pauseIcon, data);
		Button stop = createButton(comp, stopIcon, data);
		Button next = createButton(comp, nextIcon, new GridData(SWT.LEFT,SWT.TOP,true,false));
		
		addPreviousButtonListener(prev);
		addNextButtonListener(next);
		addPlayButtonListener(play);
		addPauseButtonListener(pause);
		addStopButtonListener(stop);
		addShellDisposeListener();
	}

	private void addShellDisposeListener() {
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if(AudioPlayer.getInstance().getSong() != null)
					AudioPlayer.getInstance().stop();
				AudioPlayer.getInstance().setThreadStop(true);
				ConfigurationSaver.getInstance().savePlayerConfigurations();	
			}
		});
	}

	private void addStopButtonListener(Button stop) {
		stop.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				AudioPlayer.getInstance().stop();
				AudioPlayer.getInstance().setThreadStop(true);
			}
		});
	}

	private void addPauseButtonListener(Button pause) {
		pause.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(AudioPlayer.getInstance().isEnableGUI())
					AudioPlayer.getInstance().pause();
			}
		});
	}

	private void addPlayButtonListener(Button play) {
		play.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(AudioPlayer.getInstance().isEnableGUI()) {
					index = PlaylistInterface.getInstance().getSongList().getSelectionIndex();
					if(index > -1)
						AudioPlayer.getInstance().play(index);
				}
			}
		});
	}

	private void addNextButtonListener(Button next) {
		next.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(AudioPlayer.getInstance().isEnableGUI()) {
					index = PlaylistInterface.getInstance().getSongList().getSelectionIndex();
					if(index > -1) {
						AudioPlayer.getInstance().next(index);
					}
				}
			}
		});
	}

	private void addPreviousButtonListener(Button prev) {
		prev.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(AudioPlayer.getInstance().isEnableGUI()) {
					index = PlaylistInterface.getInstance().getSongList().getSelectionIndex();
					if(index > -1) {
						AudioPlayer.getInstance().previous(index);
					}
				}
			}
		});
	}
	
	private Button createButton(Composite parent, Image image, GridData gridData) {
		Button button = new  Button(parent, SWT.PUSH);
		button.setLayoutData(gridData);
		button.setImage(image);
		return button;
	}
}
