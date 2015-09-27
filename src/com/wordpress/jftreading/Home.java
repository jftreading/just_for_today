package com.wordpress.jftreading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class Home extends Fragment {
	private View fragmentView;
	private WebView browser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.home, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit);
		browser.loadUrl("https://jftreading.wordpress.com/");
		return fragmentView;
	}
}
