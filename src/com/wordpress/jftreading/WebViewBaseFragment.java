package com.wordpress.jftreading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public abstract class WebViewBaseFragment extends Fragment implements WebViewInterface {
	protected View fragmentView;
	protected WebView browser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.webview_fragment, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit);		
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!networkIsUp())
					url = getErrorPage();
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
		browser.addJavascriptInterface(new MyJavascriptInterface(), "Android");
		browser.loadUrl(linkSelector());
		return fragmentView;
	}	
	
	private void refreshFragment() {
		Fragment fragment = this;			
		getActivity().getSupportFragmentManager()
		    .beginTransaction()
		    .detach(this)
		    .attach(fragment)
		    .commit();
	}
	
	public class MyJavascriptInterface {
		@JavascriptInterface
		public void reloadPage() {
			refreshFragment();
		}
	}
}
