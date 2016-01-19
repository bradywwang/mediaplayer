package com.brady.mediaplayer.services;

import java.io.IOException;
import java.util.ArrayList;

import com.brady.mediaplayer.db.MusicHelper;
import com.brady.mediaplayer.entities.MusicInfo;
import com.brady.mediaplayer.utils.ConstValues;
import com.brady.mediaplayer.utils.LogUtils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.text.TextUtils;

public class MediaService extends Service {
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private String path;
	private boolean isPause;
	private int currentPosition;
	private int currentIndex;
	private ArrayList<MusicInfo> musicList;
	private ArrayList<MusicInfo> playList;
	private MusicHelper musicHelper;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		musicHelper = new MusicHelper(this);
		musicList = musicHelper.getMusicList();
		playList = musicHelper.getPlayList();
		int msg = 0;
		if (intent != null) {
			currentIndex = intent.getIntExtra("position", -1);
			msg = intent.getIntExtra("MSG", 0);
		}
		if (msg == ConstValues.PLAY_MSG) {
			int function = intent.getIntExtra("function", 0);
			if (function == ConstValues.PLAY_LIST) {
				play(0);
			} else if (function == ConstValues.ALL_MUSIC) {
				play(intent.getStringExtra("url"));
			}
		} else if (msg == ConstValues.PAUSE_MSG) {
			pause();
		} else if (msg == ConstValues.RESUME_MSG) {
			resume();
		} else if (msg == ConstValues.STOP_MSG)

		{
			stop();
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (currentIndex++ > playList.size() - 1) {
					currentIndex = 0;
				}
				Intent sendIntent = new Intent(ConstValues.ACTION_UPDATE_BROADCAST);
				sendIntent.putExtra("current", currentIndex);
				sendBroadcast(sendIntent);
				path = playList.get(currentIndex).getUrl();
				play(0);
			}
		});
		return super.onStartCommand(intent, flags, startId);

	}

	private void play(String url) {
		try {
			path = url;
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new PreparedListener(0));
		} catch (Exception e) {
			LogUtils.e(e.toString());
		}
	}

	private void play(int position) {
		try {
			if (currentIndex != -1) {
				path = playList.get(currentIndex).getUrl();
				LogUtils.i("position" + position);
				mediaPlayer.reset();
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				mediaPlayer.setOnPreparedListener(new PreparedListener(position));
				Intent intent = new Intent();
				intent.setAction(ConstValues.ACTION_UPDATE_BROADCAST);
				intent.putExtra(ConstValues.MUSIC_NAME, playList.get(currentIndex).getTitle());
				intent.putExtra(ConstValues.MUSIC_SINGER, playList.get(currentIndex).getArtist());
				LogUtils.i("broadCast Sent");
				sendBroadcast(intent);
			}
		} catch (Exception e) {
			LogUtils.e(e.toString());
		}
	}

	private void pause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			isPause = true;
			currentPosition = mediaPlayer.getCurrentPosition();
			LogUtils.i("music is pause");
		} else if (mediaPlayer == null)
			LogUtils.i("media player null" + this);
		else {
			LogUtils.i("media not playing" + this);
		}
	}

	private void resume() {
		mediaPlayer.seekTo(currentPosition);
		mediaPlayer.start();
	}

	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				LogUtils.e(e.getMessage());
			} catch (IOException e) {
				LogUtils.e(e.getMessage());
			}
		}
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		super.onDestroy();
	}

	private class PreparedListener implements OnPreparedListener {
		private int position;

		public PreparedListener(int position) {
			this.position = position;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();
			if (position > 0) {
				mediaPlayer.seekTo(position);
			}
		}
	}
}
