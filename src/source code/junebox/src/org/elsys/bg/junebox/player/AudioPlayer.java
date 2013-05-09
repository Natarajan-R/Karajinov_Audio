package org.elsys.bg.junebox.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.media.CannotRealizeException;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;

import org.elsys.bg.junebox.data.Song;
import org.elsys.bg.junebox.gui.MainWindowInterface;
import org.elsys.bg.junebox.gui.PlaylistInterface;
import org.elsys.bg.junebox.playlist.Playlist;

public class AudioPlayer implements BasicPlayer {

	private ArrayList<Boolean> playedSongs;
	private Song currentSong;
	private Song previousSong;
	private Playlist playlist;	
	
	private boolean play = false;
	private boolean paused = false;
	private boolean stopped = false;
	private boolean threadStop = false;
	private boolean enableGUI = true;
	
	private Player player;
	private Time resume;
	private MediaLocator mediaLocator;

	private int songIndex;
	private Random random;
	
	private int counter;
	
	private static volatile AudioPlayer instance = null;
	
	public static synchronized AudioPlayer getInstance() {
		if(instance == null) instance = new AudioPlayer();
		return instance;
	}
	
	private AudioPlayer() {
		playedSongs = new ArrayList<Boolean>();
		playlist = Playlist.getInstance();
		random = new Random();
	}	
	
	public boolean isEnableGUI() {
		return enableGUI;
	}
	
	public void setThreadStop(boolean flag) {
		this.threadStop = flag;
	}
	
	public boolean isThreadStop() {
		return threadStop;
	}
	
	public boolean isPlaying() {
		return play;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public boolean isStopped() {
		return stopped;
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getSongTime() {
		return (int) player.getDuration().getSeconds();
	}
	
	public Song getSong() {
		return currentSong;
	}
	
	public void clearSongList() {
		playedSongs.clear();
	}
	
	public void addSong() {
		playedSongs.add(false);
	}
	
	public void removeSong(int index) {
		playedSongs.remove(index);
	}
	
	public void setSongs(ArrayList<Song> songList) {
		playedSongs.clear();
		for(int i=0;i<songList.size();i++) {
			playedSongs.add(false);
		}
	}
	
	public void setResumeTime(int seconds) {
		resume = new Time((double)seconds);
	}
	
	public void moveToPosiotion(int selection) {
		player.setMediaTime(new Time((double) selection));
	}
	
	private boolean playlistHasFinished() {
		for(int i=0;i<playedSongs.size();i++)
			if(!playedSongs.get(i))
				return false;
			return true;
	}
	
	public void play(int index){ 
		previousSong = currentSong;
		songIndex = index;
		currentSong = playlist.getPlaylist().get(songIndex);
		if(previousSong != null)
			if(!previousSong.equals(currentSong))
				stop();
		playedSongs.set(songIndex, true);
		if(!stopped && !paused) stop();
		run();
	}
	
	public void run() {
		if(paused) {
			pause();
		} else {	
			try {
				mediaLocator = new MediaLocator(currentSong.getURL());
				player = Manager.createRealizedPlayer(mediaLocator);
				startPlayer();			
			} catch (NoPlayerException e) {
				handleException(" NoPlayerException handled in AudioPlayer");
			} catch (IOException e) {
				handleException("IOException handled in AudioPlayer");
			} catch (CannotRealizeException e) {
				handleException("CannotRealizeException handled in AudioPlayer");
			} catch(NullPointerException e) {
				handleException("NullPointerException handled in AudioPlayer");
			}
			if(player == null){
				threadStop = false;
				playNextRealizableTrack();
			}
		} 
	}

	private void handleException(String message) {
		System.out.println(message);
		player = null;
		play = false;
		enableGUI = false;
	}

	private void startPlayer() {
		configureGUI();
		addPlayerListener();
		
		player.getGainControl().setLevel(MainWindowInterface.getInstance().getVolume());
		player.prefetch();
		player.start();
		MainWindowInterface.getInstance().getSeeker().execute();
		
		stopped = false;
		play = true;
	}

	private void configureGUI() {
		MainWindowInterface.getInstance().getSongLabel().setText(currentSong.getName());
		PlaylistInterface.getInstance().getSongList().setSelection(songIndex);
	}
	
	private void playNextRealizableTrack() {
		counter = 1;
		new Thread(new Runnable() {
			public void run() {			
				while(!MainWindowInterface.getInstance().getShell().isDisposed()) {				
					if(threadStop) {
						threadStop = false;
						enableGUI = true;
						break;
					}
					MainWindowInterface.getInstance().getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							try {
								if(songIndex == playlist.size()-1) songIndex = 0;
								else songIndex++;
								
								currentSong = playlist.getPlaylist().get(songIndex);
								configureGUI();

								if(playlist.size() == counter) threadStop = true;							
								counter++;
								
								mediaLocator = new MediaLocator(currentSong.getURL());
								player = Manager.createRealizedPlayer(mediaLocator);
							} catch (NoPlayerException e) {
								handleException("NoPlayerException handled in AudioPlayer");
							} catch (IOException e) {
								handleException("IOException handled in AudioPlayer");
							} catch (CannotRealizeException e) {
								handleException("CannotRealizeException handled in AudioPlayer");
							}
								
							if(player != null) {
								startPlayer();
								threadStop = true;
								enableGUI = true;
							}
						}
					});
					try {
						Thread.sleep(300);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		}).start();		
	}

	private void addPlayerListener() {
		player.addControllerListener(new ControllerListener() {
			public void controllerUpdate(ControllerEvent e) {
				if (e instanceof EndOfMediaEvent) {
					stop();
					if(playlist.size() > 0) {
						play = true;
						MainWindowInterface.getInstance().getShell().getDisplay().asyncExec(new Runnable() {
							public void run() {
								if(MainWindowInterface.getInstance().isShuffleEnabled()) {
									if(playlistHasFinished()) {
										setSongs(playlist.getPlaylist());
										if(MainWindowInterface.getInstance().isRepeatEnabled())
											play(random.nextInt(playlist.size()));
									} else {
										int randomSong = random.nextInt(playlist.size());
										while(playedSongs.get(randomSong))
											randomSong = random.nextInt(playlist.size());
										play(randomSong);
									}
								} else if(songIndex == (playedSongs.size()-1)) {
									if(!MainWindowInterface.getInstance().isRepeatEnabled()) play = false;
									next(songIndex);
								} else next(songIndex);
							}
						});
					}
				}
			}
		});
	}
	
	public void pause() {
		if(currentSong != null && !stopped && player != null){
			if(paused) {
				player.setMediaTime(resume);
				player.getGainControl().setLevel(MainWindowInterface.getInstance().getVolume());
				player.start();
				play = true;
				paused = false;
			} else {
				resume = player.getMediaTime();
				player.stop();
				play = false;
				paused = true;
			}
			stopped = false;
		}
	}
	
	public void stop() {
		if(player != null && !stopped) {
			stopped = true;
			player.stop();
			player.close();	
			paused = false;
			play = false;
			MainWindowInterface.getInstance().getSeeker().stopActiveThread();
		}
	}

	public void previous(int currentSongIndex) {
		int previous;
		
		if(MainWindowInterface.getInstance().isShuffleEnabled())
			if(currentSongIndex == 0) previous = 0;
			else previous = random.nextInt(currentSongIndex);
		else {
			if(currentSongIndex == 0) previous = playlist.size() - 1;
			else previous = currentSongIndex - 1;	
		}
		
		changeSong(previous);
	}

	public void next(int currentSongIndex) {
		int next;
		int lastSong = playlist.size() - 1;
		
		if(MainWindowInterface.getInstance().isShuffleEnabled()) {
			next = getNextShuffledSong();
		} else {
			if(currentSongIndex == lastSong) next = 0;
			else next = currentSongIndex + 1;
		}
		
		changeSong(next);
	}

	private int getNextShuffledSong() {
		int next = random.nextInt(playlist.size());
		while(playedSongs.get(next))
			next = random.nextInt(playlist.size());
		return next;
	}
	
	private void changeSong(int selection) {
		if(paused) stop();
		if(!play) {
			PlaylistInterface.getInstance().getSongList().setSelection(selection);
	        MainWindowInterface.getInstance().getSongLabel().setText(playlist.getPlaylist().get(selection).getName());
		} else play(selection);
	}

}
