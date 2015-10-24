/*******************************************************************************
 * Copyright (c) 2015 jftreading.wordpress.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     jftreading.wordpress.com - email: machinehead449@gmail.com
 ******************************************************************************/
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
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public abstract class BaseFragmentWebView extends Fragment implements WebViewInterface {
	protected View fragmentView;
	protected WebView browser;
	protected boolean offlinePage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.webview_fragment, container, false);
		browser = (WebView) fragmentView.findViewById(R.id.webkit);		
		browser.getSettings().setJavaScriptEnabled(true);
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!networkIsUp() && !offlinePage) {
				    view.loadUrl(getErrorPage());
				    return true;
				}
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:")) {                	
                    return false;                    
                }                                
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
				return true;
			}			
		});
		browser.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
				return super.onJsConfirm(view, url, message, result);
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
