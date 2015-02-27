package com.pythonanywhere.healingpathfoundationhp;

import com.pythonanywhere.healingpathfoundationhp.R;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MeetingFragment extends Fragment
{
	private final String LANDLINE = "029395917";
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.meeting_fragment, container, false);		
	}
	
	public void onCallLandlineClick(View view)
	{
		Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + LANDLINE));
        startActivity(callIntent);		
	}
	
	public void onShowMapClick(View view) 
    {
		Intent intent=new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo: 14.708956, 121.078503"));
        startActivity(intent);    
    }
}
