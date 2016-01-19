package com.brady.mediaplayer.fragments;

import com.brady.mediaplayer.R;
import com.brady.mediaplayer.activities.MainActivity;
import com.brady.mediaplayer.utils.LogUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {
	WebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online_music, container, false);
		LogUtils.i("call create");
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initWebView(view);
		super.onViewCreated(view, savedInstanceState);
	}

	private void initWebView(View view) {
		mWebView = (WebView) view.findViewById(R.id.webview);
		WebSettings mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);// 设置使用够执行JS脚本
		mWebSettings.setBuiltInZoomControls(false);// 设置使支持缩放
		mWebSettings.setLoadWithOverviewMode(true);
		mWebSettings.setUseWideViewPort(true);
		mWebSettings.setDefaultTextEncodingName("GBK");
		mWebSettings.setLoadsImagesAutomatically(true);
		mWebSettings.setDomStorageEnabled(true);
		mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
		String appCacheDir = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		mWebSettings.setAppCachePath(appCacheDir);
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setAppCacheEnabled(true);
		mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			}
		});
			mWebView.loadUrl("http://m.xiami.com");
	}

	@Override
	public void onPause() {
		LogUtils.i("call on Pause");
		// ((MainActivity) getActivity()).showPlayControler();
		super.onPause();
	}

	@Override
	public void onResume() {
		// ((MainActivity) getActivity()).hidePlayControler();
		super.onResume();
	}

}
