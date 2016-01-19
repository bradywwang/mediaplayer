package com.brady.mediaplayer.fragments;

import java.util.ArrayList;

import com.brady.mediaplayer.IMusicControler;
import com.brady.mediaplayer.MusicControlerImpl;
import com.brady.mediaplayer.R;
import com.brady.mediaplayer.activities.MainActivity;
import com.brady.mediaplayer.adapters.MusicListAdapter;
import com.brady.mediaplayer.db.MusicHelper;
import com.brady.mediaplayer.entities.MusicInfo;
import com.brady.mediaplayer.interfaces.IAllMusicFragment;
import com.brady.mediaplayer.interfaces.IMainPresenter;
import com.brady.mediaplayer.presenters.MainPresenterImpl;
import com.brady.mediaplayer.utils.ConstValues;
import com.brady.mediaplayer.utils.LogUtils;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AllMusicFragment extends Fragment implements IAllMusicFragment{
	IMainPresenter presenter;
	RecyclerView recyclerView;
	private MusicListAdapter allMusicAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_music_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initRecyclerView(view);
		presenter = MainPresenterImpl.getInstance((MainActivity)getActivity());
		showData();
		registerForContextMenu(recyclerView);
		super.onViewCreated(view, savedInstanceState);
	}

	private void initRecyclerView(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.music_list_recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
	}

	public interface onItemClickListener {
		void onItemClick(int position, View view);
		void onItemLongClick(int position, View view);
	}

	@Override
	public void setAdapter(MusicListAdapter adapter) {
		if(recyclerView == null){
			LogUtils.e("recyclerView is null");
		}else{
			allMusicAdapter = adapter;
			recyclerView.setAdapter(adapter);
		}
	}

	@Override
	public void showData() {
		presenter.showAllMusicList();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return presenter.processMenuSelected(item);
	}
}
