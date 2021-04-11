package edu.skku.jmk_project;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.RadioGroup;
import android.widget.Toast;
import edu.skku.jmk_project_jnidriver.*;

public class MainActivity extends ActionBarActivity {
	
	boolean open;
	int stage;
	int goal_val;
	int goal_cnt;
	private RadioGroup radioGroup;
	jmk_JNIDriver jniDriver = new jmk_JNIDriver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
}

	RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int i) {
			if(i == R.id.radio0){
				stage = 1;
				goal_val = 2;
				goal_cnt = 2;
			}

			else if(i == R.id.radio1){
				stage = 2;
				goal_val = 50;
				goal_cnt = 3;
			}

			else if(i == R.id.radio2){
				stage = 3;
				goal_val = 21;
				goal_cnt = 5;
			}
		}
	};

	public void on1(View v){
		switch(stage){
		case 1:
		Intent intent = new Intent(getBaseContext(),Stage1.class);
		intent.putExtra("val",goal_val);
		intent.putExtra("cnt",goal_cnt);
		startActivity(intent);
		break;

		case 2:
		Intent intent2 = new Intent(getBaseContext(),Stage2.class);
		intent2.putExtra("val",goal_val);
		intent2.putExtra("cnt",goal_cnt);
		startActivity(intent2);
		break;

		case 3:
		Intent intent3 = new Intent(getBaseContext(),Stage3.class);
		intent3.putExtra("val",goal_val);
		intent3.putExtra("cnt",goal_cnt);
		startActivity(intent3);
		break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
	return super.onOptionsItemSelected(item);
	}
	
	protected void onResume() {
		if(jniDriver.segopen() < 0)
			Toast.makeText(getBaseContext(), "segment_Driver Open Failed", Toast.LENGTH_SHORT).show();
		
		if(jniDriver.ledopen() < 0)
			Toast.makeText(getBaseContext(), "LED_Driver Open Failed", Toast.LENGTH_SHORT).show();
		
		if(jniDriver.lcdopen() < 0)
			Toast.makeText(getBaseContext(), "TextLcd_Driver Open Failed", Toast.LENGTH_SHORT).show();
		
		jniDriver.lcdinitialize();
		jniDriver.lcdclear();
		jniDriver.lcdprint1Line("Calculator");
		jniDriver.lcdprint2Line("Game");
		jniDriver.segprint(101010);
		jniDriver.ledon(10);
		super.onResume();
	}
	
	protected void onPause() {
		jniDriver.segclose();
		jniDriver.ledclose();
		jniDriver.lcdclear();
		jniDriver.lcdoff();
		super.onPause();
	}

}
