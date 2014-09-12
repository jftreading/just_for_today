package org.beltcan.sponsor;

import org.beltcan.sponsor.R;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class AboutActivity extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
	}
	
	public void onClick(View view) 
    {
        Intent intent=new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo: 14.708956, 121.078503"));
        startActivity(intent);    
    }
}
