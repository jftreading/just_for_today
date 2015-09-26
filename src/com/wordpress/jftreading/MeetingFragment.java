package com.wordpress.jftreading;

import android.os.Bundle;

import com.wordpress.jftreading.R;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MeetingFragment extends Fragment
{
	private final String LANDLINE = "+6329395917";
	private View fragmentView;
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.meeting_fragment, container, false);
		Button but1 = (Button) fragmentView.findViewById(R.id.Button1);
		but1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse("tel:" + LANDLINE));
		        startActivity(callIntent);
			}
		});
		
		Button but2 = (Button) fragmentView.findViewById(R.id.Button2);
		but2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(android.content.Intent.ACTION_VIEW);
		        intent.setData(Uri.parse("geo: 14.708956, 121.078503"));
		        startActivity(intent);
			}
		});
		return fragmentView;
	}		
}
