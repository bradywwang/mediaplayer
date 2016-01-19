package com.brady.mediaplayer.interfaces;

import com.brady.mediaplayer.entities.MusicInfo;

import android.support.v4.app.Fragment;
import android.view.MenuItem;

public interface IMainPresenter {
	void processFunctionButtonClick();
	public void play();
	public void pause();
	public void stop();
	public int getPlayStatus();
	public void showAllMusicList();
	public void showPlayList();
	public Fragment getFragment(int position);
	public void play(int position);
	public void play(String url);
	public void addToPlayList(MusicInfo info);
	public void removeFromPlayList(int id);
	public void showFirstMusicInfo();
	public boolean processMenuSelected(MenuItem item);
	public void playOrPause(int function, int position);
	public void addToPlayList(int position);
	public void removeFromPlayListByPosition(int position);
	public void showMusicInfoDialog(int function,int position);
}
