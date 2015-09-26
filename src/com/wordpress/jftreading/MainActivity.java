package com.wordpress.jftreading;

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
		viewPager.setAdapter(new MyAdapter(fm));
	}
}

class MyAdapter extends FragmentPagerAdapter
{

	public MyAdapter(FragmentManager fm) {
		super(fm);		
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
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {		
		if (position==0) {
			return "Call before you drink";
		}
		if (position==1) {
			return "The Twelve Steps - Narcotics Anonymous";
		}
		if (position==2) {
			return "Location";
		}
		return null;
	}
	
	
} 
