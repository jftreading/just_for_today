package com.wordpress.jftreading;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class HomepageFragment extends Fragment {
	private View fragmentView;
	private WebView browser;
	private String[] links;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.homepage, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit1);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}			
		});
		links = getResources().getStringArray(R.array.links);
		browser.loadUrl(links[1]);
		return fragmentView;
	}
}
