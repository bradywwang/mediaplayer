package com.brady.mediaplayer.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import com.brady.mediaplayer.IMusicControler;
import com.brady.mediaplayer.MusicControlerImpl;
import com.brady.mediaplayer.R;
import com.brady.mediaplayer.R.id;
import com.brady.mediaplayer.R.layout;
import com.brady.mediaplayer.R.menu;
import com.brady.mediaplayer.adapters.MusicListAdapter;
import com.brady.mediaplayer.db.MusicHelper;
import com.brady.mediaplayer.entities.MusicInfo;
import com.brady.mediaplayer.fragments.AllMusicFragment;
import com.brady.mediaplayer.fragments.PlayListFragment;
import com.brady.mediaplayer.fragments.WebViewFragment;
import com.brady.mediaplayer.interfaces.IMainPresenter;
import com.brady.mediaplayer.interfaces.IMainView;
import com.brady.mediaplayer.presenters.MainPresenterImpl;
import com.brady.mediaplayer.utils.ConstValues;
import com.brady.mediaplayer.utils.LogUtils;
import com.brady.mediaplayer.utils.PagerSlidingTabStrip;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IMainView {
	private Button btnPlay;
	IMusicControler iMusicControler;
	IMainPresenter presenter;
	TextView tvMusicTitle;
	TextView tvMusicArtist;
	MusicHelper mMusicHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		presenter = MainPresenterImpl.getInstance(this);
		mMusicHelper = new MusicHelper(this);
		initComponents();
		initToolbar();
		initViewPager();
	}


	private void initToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
	}

	private void initViewPager() {
		ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
		PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_strip);
		viewPager.setAdapter(new FunctionChoiceFragmentPagerAdapter(getSupportFragmentManager()));
		pagerSlidingTabStrip.setViewPager(viewPager);
		pagerSlidingTabStrip.setTextColor(getResources().getColor(R.color.abc_primary_text_material_dark));
		pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.abc_primary_text_material_dark));
	}
	
	
	
	public class FunctionChoiceFragmentPagerAdapter extends FragmentPagerAdapter {

		final int PAGE_COUNT = 3;
		private String tabTitles[] = new String[] { "播放列表", "在线音乐", "全部音乐" };

		public FunctionChoiceFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return PAGE_COUNT;
		}

		@Override
		public Fragment getItem(int position) {
			return presenter.getFragment(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Generate title based on item position
			return tabTitles[position];
		}
	}

	private void initComponents() {
		tvMusicTitle = (TextView) findViewById(R.id.tv_music_name);
		tvMusicArtist = (TextView) findViewById(R.id.tv_singer_name);
		btnPlay = (Button) findViewById(R.id.btn_play);
		btnPlay.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				presenter.processFunctionButtonClick();
			}
		});
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void showPlayButton() {
		btnPlay.setBackground(getResources().getDrawable(R.drawable.ic_play));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void showStopButton() {
		btnPlay.setBackground(getResources().getDrawable(R.drawable.ic_stop));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void showPauseButton() {
		btnPlay.setBackground(getResources().getDrawable(R.drawable.ic_pause));
	}

	@Override
	public void showMusicTitle(String title) {
		tvMusicTitle.setText(title);
	}

	@Override
	public void showMusicArtist(String name) {
		tvMusicArtist.setText(name);
	}

	@Override
	public void showToast(String toast) {
	}

	@Override
	public void startNewService(Intent intent) {
		startService(intent);
	}

	@Override
	public Context getContext() {
		return this;
	}
}
