package com.wordpress.jftreading;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class Home extends Fragment {
	private View fragmentView;
	private WebView browser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.home, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.loadUrl("https://jftreading.wordpress.com/");
		return fragmentView;
	}
}
