package com.brady.mediaplayer.interfaces;

import com.brady.mediaplayer.adapters.MusicListAdapter;

import android.support.v7.widget.RecyclerView.Adapter;

public interface IAllMusicFragment {
	void showData();
	void setAdapter(MusicListAdapter adapter);
}
