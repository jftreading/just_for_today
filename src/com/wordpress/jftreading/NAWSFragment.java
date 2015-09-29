package com.wordpress.jftreading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class NAWSFragment extends Fragment {
	private View fragmentView;
	private WebView browser;
	private String[] links;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.webview_fragment, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {				
				if (url.contains(getResources().getString(R.string.share_link))) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(browserIntent);
					return true;
				} else {
				    view.loadUrl(url);
				    return true;
				}
			}			
		});
		links = getResources().getStringArray(R.array.links);
		browser.loadUrl(links[1]);
		return fragmentView;
	}
}
