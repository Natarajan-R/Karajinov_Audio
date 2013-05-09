package org.elsys.bg.junebox.player;

public interface BasicPlayer {
	public void play(int index);
	public void pause();
	public void stop();
	public void next(int index);
	public void previous(int index);
}
