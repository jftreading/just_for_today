package com.pythonanywhere.healingpathfoundationhp;

import com.pythonanywhere.healingpathfoundationhp.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TwelveStepsFragment extends Fragment
{
	private ListView lv;	
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		/* lv = (ListView) v.findViewById(R.id.lvId);
		String[] twelve_steps = getResources().getStringArray(R.array.twelve_steps);
		lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.row, twelve_steps)); */
		
		return inflater.inflate(R.layout.twelve_steps_fragment, container, false);
	}
}
