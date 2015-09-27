package com.wordpress.jftreading;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	

	ViewPager viewPager = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main);
		viewPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		viewPager.setAdapter(new MyAdapter(fm, this));
	}
}

class MyAdapter extends FragmentPagerAdapter
{
	private String[] pageTitles;
	public MyAdapter(FragmentManager fm, Context context) {
		super(fm);
		pageTitles = context.getResources().getStringArray(R.array.page_titles);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = null;
        if (arg0 == 0)
        {
            fragment = new MainFragment();
        }
        if (arg0 == 1)
        {
            fragment = new TwelveStepsFragment();
        }
        if (arg0 == 2)
        {
            fragment = new MeetingFragment();
        }
        if (arg0 == 3)
        {
        	fragment = new Home();
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
