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
import android.view.View;
import android.view.View.OnLongClickListener;

public class MainFragmentActivity extends FragmentActivity {
	MyAdapter mAdapter;
	
	ViewPager viewPager = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		mAdapter = new MyAdapter(getSupportFragmentManager(), this);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(mAdapter);
	}
	
	public static class MyAdapter extends FragmentPagerAdapter {
		private String[] pageTitles;
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
				fragment = WebViewFragment.newInstance(arg0);
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
		private String[] links;
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
		}

		@Override
		public boolean isNetworkUp() {
			ConnectivityManager check = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiNetwork = check.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileNetwork = check.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			if (wifiNetwork.isConnected() || mobileNetwork.isConnected())
				return true;
			return false;
		}

		@Override
		public String selectRightLink() {
			String link = links[mNum];
			if (!isNetworkUp()) {
				browser.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						return true;
					}
				});
				browser.setLongClickable(false);
				link = links[0];
			} else {
			    browser.setOnLongClickListener(null);
			    browser.setLongClickable(true);
			}
			return link;
		}
	}
}