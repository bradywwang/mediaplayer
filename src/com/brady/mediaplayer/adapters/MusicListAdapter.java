package com.brady.mediaplayer.adapters;

import java.util.ArrayList;

import com.brady.mediaplayer.R;
import com.brady.mediaplayer.entities.MusicInfo;
import com.brady.mediaplayer.fragments.AllMusicFragment.onItemClickListener;
import com.brady.mediaplayer.utils.ConstValues;
import com.brady.mediaplayer.utils.LogUtils;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
	private ArrayList<MusicInfo> musicList;
	onItemClickListener listener;
	private int position;
	private int function;

	public int getFunction() {
		return function;
	}

	public void setFunction(int function) {
		this.function = function;
	}

	public int getMyPosition() {
		return position;
	}

	public void setMyPosition(int position) {
		this.position = position;
	}

	public MusicListAdapter(ArrayList<MusicInfo> musicList) {
		super();
		this.musicList = musicList;
	}

	@Override
	public int getItemCount() {
		LogUtils.d("size=" + musicList.size());
		return musicList.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder arg0, int arg1) {
		MusicInfo musicInfo = musicList.get(arg1);
		arg0.tvMusicName.setText(musicInfo.getTitle());
		arg0.tvMusicSinger.setText(musicInfo.getArtist());
		arg0.itemView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				setMyPosition(arg0.getPosition());
				return false;
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(final ViewGroup arg0, final int arg1) {
		View view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.music_item, arg0, false);
		ViewHolder vh = new ViewHolder(view);
		return vh;
	}

	class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnCreateContextMenuListener {
		TextView tvMusicName;
		TextView tvMusicSinger;

		public ViewHolder(View itemView) {
			super(itemView);
			tvMusicName = (TextView) itemView.findViewById(R.id.tv_music_name);
			tvMusicSinger = (TextView) itemView.findViewById(R.id.tv_singer_name);
			itemView.setOnCreateContextMenuListener(this);
			LogUtils.i("create" + getPosition());
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onItemClick(getPosition(), v);
				}

			});
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("功能选择");
			switch (function) {
			case ConstValues.ALL_MUSIC: {
				menu.add(0, 5, 0, "播放/暂停");// groupId, itemId, order, title
				menu.add(0, 6, 0, "添加到播放列表");
				menu.add(0, 8, 0, "查看属性");
				break;
			}
			case ConstValues.PLAY_LIST: {
				menu.add(0, 1, 0, "播放/暂停");// groupId, itemId, order, title
				menu.add(0, 3, 0, "从播放列表中移除");
				menu.add(0, 4, 0, "查看属性");
				break;
			}
			}
		}
	}

	public void setOnItemClickListener(onItemClickListener listener) {
		this.listener = listener;
	}

}