package com.pythonanywhere.healingpathfoundationhp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
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
			fragment = (Fragment) new JFTFragment();
		}
		if (arg0 == 2)
		{
			fragment = new TwelveStepsFragment();
		}
		if (arg0 == 3)
		{
			fragment = new MeetingFragment();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 4;
	}	
} 
