package com.brady.mediaplayer.presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.brady.mediaplayer.IMusicControler;
import com.brady.mediaplayer.MusicControlerImpl;
import com.brady.mediaplayer.R;
import com.brady.mediaplayer.activities.MainActivity;
import com.brady.mediaplayer.adapters.MusicListAdapter;
import com.brady.mediaplayer.db.MusicHelper;
import com.brady.mediaplayer.entities.MusicInfo;
import com.brady.mediaplayer.fragments.AllMusicFragment;
import com.brady.mediaplayer.fragments.AllMusicFragment.onItemClickListener;
import com.brady.mediaplayer.fragments.PlayListFragment;
import com.brady.mediaplayer.fragments.WebViewFragment;
import com.brady.mediaplayer.interfaces.IMainPresenter;
import com.brady.mediaplayer.interfaces.IMainView;
import com.brady.mediaplayer.utils.ConstValues;
import com.brady.mediaplayer.utils.LogUtils;
import com.brady.mediaplayer.utils.ConvertUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.Prediction;
import android.media.MediaCodecInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainPresenterImpl implements IMainPresenter {
	private static MainPresenterImpl instance;
	private static IMainView sMainView;
	private ArrayList<MusicInfo> mMusicList;
	private ArrayList<MusicInfo> mPlayList;
	private int mPlayStatus;
	private AllMusicFragment allMusicFragment;
	private PlayListFragment playListFragment;
	private WebViewFragment webViewFragment;
	private Context mContext;
	private MusicHelper helper;
	MusicListAdapter mPlayListAdapter;
	MusicListAdapter mMusicListAdapter;

	private MainPresenterImpl(IMainView iMainView) {
		mPlayStatus = ConstValues.STOP_MSG;
		allMusicFragment = new AllMusicFragment();
		playListFragment = new PlayListFragment();
		webViewFragment = new WebViewFragment();
		mContext = iMainView.getContext();
		helper = new MusicHelper(mContext);
		addRecevier();
	}

	public static MainPresenterImpl getInstance(IMainView mainView) {
		if (instance == null) {
			instance = new MainPresenterImpl(mainView);
		}
		sMainView = mainView;
		return instance;
	}

	@Override
	public void processFunctionButtonClick() {
		switch (mPlayStatus) {
		case ConstValues.PLAY_MSG: {
			pause();
			sMainView.showPlayButton();
			break;
		}
		case ConstValues.STOP_MSG: {
			if (mMusicList != null && mMusicList.size() > 0) {
				play(0);
				sMainView.showPauseButton();
			}
			break;
		}
		case ConstValues.PAUSE_MSG: {
			play();
			sMainView.showPauseButton();
			break;
		}
		}
	}

	@Override
	public void play() {
		Intent intent = new Intent();
		intent.setAction(ConstValues.ACTION_MEDIA_SERVICE);
		intent.putExtra("MSG", ConstValues.RESUME_MSG);
		sMainView.startNewService(intent);
		mPlayStatus = ConstValues.PLAY_MSG;
	}

	@Override
	public void play(int position) {
		sMainView.showPauseButton();
		Intent intent = new Intent();
		intent.setAction(ConstValues.ACTION_MEDIA_SERVICE);
		intent.putExtra("position", position);
		intent.putExtra("function", ConstValues.PLAY_LIST);
		intent.putExtra("MSG", ConstValues.PLAY_MSG);
		sMainView.startNewService(intent);
		sMainView.showPauseButton();
		mPlayStatus = ConstValues.PLAY_MSG;
	}

	@Override
	public void pause() {
		Intent intent = new Intent();
		intent.setAction(ConstValues.ACTION_MEDIA_SERVICE);
		intent.putExtra("MSG", ConstValues.PAUSE_MSG);
		sMainView.startNewService(intent);
		sMainView.showPlayButton();
		mPlayStatus = ConstValues.PAUSE_MSG;
	}

	@Override
	public void stop() {
		Intent intent = new Intent();
		intent.setAction(ConstValues.ACTION_MEDIA_SERVICE);
		intent.putExtra("MSG", ConstValues.STOP_MSG);
		sMainView.startNewService(intent);
		mPlayStatus = ConstValues.STOP_MSG;
	}

	@Override
	public int getPlayStatus() {
		return mPlayStatus;
	}

	@Override
	public void showAllMusicList() {
		mMusicList = helper.getMusicList();
		mMusicListAdapter = new MusicListAdapter(mMusicList);
		mMusicListAdapter.setFunction(ConstValues.ALL_MUSIC);
		mMusicListAdapter.setOnItemClickListener(new onItemClickListener() {
			@Override
			public void onItemClick(int position, View view) {
				play(mMusicList.get(position).getUrl());
				addToPlayList(mMusicList.get(position));
				sMainView.showMusicArtist(mMusicList.get(position).getArtist());
				sMainView.showMusicTitle(mMusicList.get(position).getTitle());
				mPlayListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onItemLongClick(int position, View view) {

			}
		});
		allMusicFragment.setAdapter(mMusicListAdapter);
	}

	@Override
	public Fragment getFragment(int position) {
		switch (position) {
		case 0: {
			return playListFragment;
		}
		case 1: {
			return webViewFragment;
		}
		case 2: {
			return allMusicFragment;
		}
		}
		return null;
	}

	@Override
	public void showPlayList() {
		mPlayList = helper.getPlayList();
		mPlayListAdapter = new MusicListAdapter(mPlayList);
		mPlayListAdapter.setFunction(ConstValues.PLAY_LIST);
		mPlayListAdapter.setOnItemClickListener(new onItemClickListener() {
			@Override
			public void onItemClick(int position, View view) {
				play(position);
			}

			@Override
			public void onItemLongClick(int position, View view) {
				// TODO Auto-generated method stub

			}
		});
		playListFragment.setAdapter(mPlayListAdapter);
		showFirstMusicInfo();
	}

	@Override
	public void addToPlayList(MusicInfo info) {
		for (MusicInfo item : mPlayList) {
			if (item.getId() == info.getId())
				return;
		}
		mPlayList.add(0, info);
		helper.addToPlayList(info);
	}

	@Override
	public void removeFromPlayList(int id) {
		helper.removeFromPlayList(id);

	}

	@Override
	public void play(String url) {
		sMainView.showPauseButton();
		Intent intent = new Intent();
		intent.setAction(ConstValues.ACTION_MEDIA_SERVICE);
		intent.putExtra("url", url);
		intent.putExtra("function", ConstValues.ALL_MUSIC);
		intent.putExtra("MSG", ConstValues.PLAY_MSG);
		sMainView.startNewService(intent);
		mPlayStatus = ConstValues.PLAY_MSG;
	}

	private BroadcastReceiver MediaInfoChangeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.i("broadcast received");
			if (intent.getAction() == ConstValues.ACTION_UPDATE_BROADCAST) {
				String musicName = intent.getStringExtra(ConstValues.MUSIC_NAME);
				String musicSinger = intent.getStringExtra(ConstValues.MUSIC_SINGER);
				sMainView.showMusicTitle(musicName);
				sMainView.showMusicArtist(musicSinger);
			}
		}
	};

	private void addRecevier() {
		IntentFilter filter = new IntentFilter(ConstValues.ACTION_UPDATE_BROADCAST);
		mContext.registerReceiver(MediaInfoChangeReceiver, filter);
	}

	@Override
	public void showFirstMusicInfo() {
		if (mPlayList == null || mPlayList.size() == 0) {
			sMainView.showMusicTitle("暂无正在播放歌曲");
			sMainView.showMusicArtist("暂无艺术家");
		} else {
			sMainView.showMusicTitle(mPlayList.get(0).getTitle());
			sMainView.showMusicArtist(mPlayList.get(0).getArtist());
		}
	}

	@Override
	public boolean processMenuSelected(MenuItem item) {
		int playListPosition = 0;
		int AllMusicListPosition = 0;
		if (mPlayListAdapter != null) {
			playListPosition = mPlayListAdapter.getMyPosition();
		}
		if (mMusicListAdapter != null) {
			AllMusicListPosition = mMusicListAdapter.getMyPosition();
		}
		int id = item.getItemId();

		switch (id) {
		case 1: {
			playOrPause(ConstValues.PLAY_LIST, playListPosition);
			break;
		}
		case 2: {
			addToPlayList(playListPosition);
			break;
		}
		case 3: {
			removeFromPlayListByPosition(playListPosition);
			break;
		}
		case 4: {
			showMusicInfoDialog(ConstValues.PLAY_LIST, playListPosition);
			break;
		}
		case 5: {
			playOrPause(ConstValues.ALL_MUSIC, AllMusicListPosition);
			break;
		}
		case 6: {
			addToPlayList(AllMusicListPosition);
			break;
		}
		case 7: {
			removeFromPlayListByPosition(AllMusicListPosition);
			break;
		}
		case 8: {
			showMusicInfoDialog(ConstValues.ALL_MUSIC, AllMusicListPosition);
			break;
		}
		}

		return true;
	}

	@Override
	public void playOrPause(int function, int position) {
		if (mPlayStatus == ConstValues.PLAY_MSG) {
			pause();
		} else if (mPlayStatus == ConstValues.PAUSE_MSG) {
			play();
		} else {
			if (function == ConstValues.ALL_MUSIC) {
				play(mMusicList.get(position).getUrl());
				sMainView.showMusicArtist(mMusicList.get(position).getArtist());
				sMainView.showMusicTitle(mMusicList.get(position).getTitle());
			} else {
				play(mPlayList.get(position).getUrl());
				sMainView.showMusicArtist(mPlayList.get(position).getArtist());
				sMainView.showMusicTitle(mPlayList.get(position).getTitle());
			}
		}
	}

	@Override
	public void addToPlayList(int position) {
		addToPlayList(mMusicList.get(position));
		mPlayListAdapter.notifyDataSetChanged();
		LogUtils.i("add to play list" + position);
	}

	@Override
	public void removeFromPlayListByPosition(int position) {
		removeFromPlayList((int) mPlayList.get(position).getId());
		mPlayList.remove(position);
		mPlayListAdapter.notifyDataSetChanged();
	}

	@Override
	public void showMusicInfoDialog(int function, int position) {
		LayoutInflater inflater = sMainView.getLayoutInflater();
		View view = inflater.inflate(R.layout.activity_music_property, null);
		TextView tvMusicTitle = (TextView) view.findViewById(R.id.tv_music_name);
		TextView tvMusicArtist = (TextView) view.findViewById(R.id.tv_music_singer);
		TextView tvMusicDuration = (TextView) view.findViewById(R.id.tv_music_duration);
		TextView tvMusicLength = (TextView) view.findViewById(R.id.tv_music_size);
		TextView tvMusicUrl = (TextView) view.findViewById(R.id.tv_music_url);
		MusicInfo musicInfo = null;
		switch (function) {
		case ConstValues.ALL_MUSIC: {
			musicInfo = mMusicList.get(position);
			break;
		}
		case ConstValues.PLAY_LIST: {
			musicInfo = mPlayList.get(position);
			break;
		}
		}
		if (musicInfo != null) {
			tvMusicArtist.setText(musicInfo.getArtist());
			tvMusicDuration.setText(ConvertUtils.formatTime(musicInfo.getDuration()));
			tvMusicLength.setText(ConvertUtils.convertToStringRepresentation(musicInfo.getSize()));
			tvMusicTitle.setText(musicInfo.getTitle());
			tvMusicUrl.setText(musicInfo.getUrl());
		}
		new AlertDialog.Builder(mContext).setTitle("属性").setView(view).setPositiveButton("确定", null).show();
	}
}
