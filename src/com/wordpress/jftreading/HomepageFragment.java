package com.wordpress.jftreading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class HomepageFragment extends Fragment {
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
				view.loadUrl(url);
				return true;
			}			
		});
		browser.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN){
					WebView webView = (WebView) v;
					
					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						if (webView.canGoBack()) {
							webView.goBack();
							return true;
						}						
						break;
					}
				}
				return false;
			}
		});
		browser.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);				
			}
		});
		links = getResources().getStringArray(R.array.links);
		browser.loadUrl(links[3]);
		return fragmentView;
	}
}
