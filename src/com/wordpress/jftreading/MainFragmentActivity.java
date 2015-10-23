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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainFragmentActivity extends FragmentActivity {
	private MyAdapter mAdapter;
	private static String[] pageTitles;
	
	ViewPager viewPager = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		mAdapter = new MyAdapter(getSupportFragmentManager(), this);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(mAdapter);
		UserHandler handler = new UserHandler(this);
		handler.processFeed();
	}
	
	public static class MyAdapter extends FragmentPagerAdapter {
		
		public MyAdapter(FragmentManager fm, Context context) {			
			super(fm);
			pageTitles = context.getResources().getStringArray(R.array.page_titles);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = null;
			if (arg0 == 0) {
				fragment = new MainFragment();
			} else {				
				fragment = WebViewFragment.newInstance(arg0 - 1);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return pageTitles.length;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			if (pageTitles.length != 0) {
				return pageTitles[position];
			} else {
			    return null;
			}
		}
	}
	
	public static class WebViewFragment extends WebViewBaseFragment {
		private String savedLink;
		private String[] links, offlineLinks;
		private int mNum;		
		
		static WebViewFragment newInstance(int num) {
			WebViewFragment f = new WebViewFragment();
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);
			
			return f;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
			links = getResources().getStringArray(R.array.links);
			offlineLinks = getResources().getStringArray(R.array.offline_links);
			if (savedInstanceState != null)
				savedLink = savedInstanceState.getString("link");
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			String currentLink = browser.getUrl();
			outState.putString("link", currentLink);
		}

		@Override
		public boolean networkIsUp() {
			ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiNetwork = check.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileNetwork = check.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			if (wifiNetwork.isConnected() || mobileNetwork.isConnected())
				return true;
			return false;
		}

		@Override
		public String linkSelector() {
			if (savedLink != null) {
			    return savedLink;	
			}						
			if (!networkIsUp()) {
				return offlineLinks[1];	
			}
			if (mNum == 3) {
				return offlineLinks[0];
			}
			return links[mNum];
		}

		@Override
		public String getErrorPage() {
			return offlineLinks[1];
		}
	}
}
