package com.wordpress.jftreading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class JFTReadingFragment extends Fragment {
	private View fragmentView;
	private WebView browser;
    private String[] links;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.webview_fragment, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit);
		links = getResources().getStringArray(R.array.links);
		browser.loadUrl(links[0]);
		return fragmentView;		
	}

}
