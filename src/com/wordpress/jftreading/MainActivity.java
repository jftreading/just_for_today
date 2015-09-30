package com.wordpress.jftreading;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
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
			} else if (arg0 == 5) {
				fragment = new TwelveStepsFragment();
			} else {				
				fragment = WebViewFragment.newInstance(arg0-1);
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
		}		
	}
}