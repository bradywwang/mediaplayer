package com.brady.mediaplayer.db;

import com.brady.mediaplayer.utils.ConstValues;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, ConstValues.DB_NAME, null, ConstValues.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS playlist"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "music_id INTEGER, music_title VARCHAR ,"
				+ " music_artist VARCHAR, music_durcation INTEGER ,"
				+ " music_size INTEGER , music_url INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
