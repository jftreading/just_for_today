package com.pythonanywhere.healingpathfoundationhp;

import com.pythonanywhere.healingpathfoundationhp.R;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class MeetingActivity extends Activity
{
	private final String LANDLINE = "029395917";
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting);
        setTitle(R.string.title_two);
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
