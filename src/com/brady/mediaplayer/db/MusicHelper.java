package com.brady.mediaplayer.db;

import java.util.ArrayList;

import com.brady.mediaplayer.entities.MusicInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

public class MusicHelper {
	private Context mContext;
	private SQLiteDatabase db;
	private DBHelper helper;

	public MusicHelper(Context context) {
		mContext = context;
		helper = new DBHelper(mContext);
		db = helper.getWritableDatabase();
	}

	public ArrayList<MusicInfo> getMusicList() {
		ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
		Cursor cursor = (mContext.getContentResolver()).query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
				null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		for (int i = 0; i < cursor.getCount(); i++) {
			MusicInfo musicInfo = new MusicInfo();
			cursor.moveToNext();
			long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
			String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
			if (isMusic != 0) {
				musicInfo.setId(id);
				musicInfo.setTitle(title);
				musicInfo.setArtist(artist);
				musicInfo.setDuration(duration);
				musicInfo.setSize(size);
				musicInfo.setUrl(url);
				musicList.add(musicInfo);
			}
		}
		return musicList;
	}

	public ArrayList<MusicInfo> getPlayList() {
		ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
		Cursor c = db.rawQuery("select * from playlist order by _id desc", null);
		while (c.moveToNext()) {
			MusicInfo info = new MusicInfo();
			info.setArtist(c.getString(c.getColumnIndex("music_artist")));
			info.setDuration(c.getLong(c.getColumnIndex("music_durcation")));
			info.setId(c.getLong(c.getColumnIndex("music_id")));
			info.setSize(c.getLong(c.getColumnIndex("music_size")));
			info.setTitle(c.getString(c.getColumnIndex("music_title")));
			info.setUrl(c.getString(c.getColumnIndex("music_url")));
			musicList.add(info);
		}
		return musicList;

	}

	public void addToPlayList(MusicInfo info) {
		db.beginTransaction();
		try {
			db.execSQL("insert into playlist VALUES(null, ?, ?, ?, ?, ?, ?)", new Object[] { info.getId(),
					info.getTitle(), info.getArtist(), info.getDuration(), info.getSize(), info.getUrl() });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void removeFromPlayList(int id) {
		db.delete("playlist", "music_id = ?", new String[] { String.valueOf(id) });
	}

	public void closeDB() {
		db.close();
	}
}
