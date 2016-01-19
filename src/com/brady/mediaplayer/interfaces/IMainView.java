package com.brady.mediaplayer.interfaces;

import java.util.ArrayList;

import com.brady.mediaplayer.entities.MusicInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

public interface IMainView {
	void showPlayButton();
	void showStopButton();
	void showPauseButton();
	void showMusicTitle(String title);
	void showMusicArtist(String name);
	void showToast(String toast);
	void startNewService(Intent intent);
	Context getContext();
	LayoutInflater getLayoutInflater();
}
