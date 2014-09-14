package org.beltcan.sponsor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TwelveStepsActivity extends Activity {
	private ListView lv;
	private ArrayAdapter<String> la;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twelve_steps);
		
		setupUI();
	}

	private void setupUI() {
		lv = (ListView) findViewById(R.id.lvId);
		String[] twelve_steps = getResources().getStringArray(R.array.twelve_steps);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.row, twelve_steps));
	}
	
	
}
