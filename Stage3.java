package edu.skku.jmk_project;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.skku.jmk_project_jnidriver.jmk_JNIDriver;;

public class Stage3 extends ActionBarActivity {
	jmk_JNIDriver jniDriver = new jmk_JNIDriver();
	int cur_val = 0;
	int cur_cnt ;
	int goalval;
	int goalcnt;
	Segment th = new Segment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stage3);
		
		TextView valtxt = (TextView)findViewById(R.id.textVal);
		TextView cnttxt = (TextView)findViewById(R.id.textCnt);
		
		Intent intent = getIntent();
		
		goalval = intent.getExtras().getInt("val");
		valtxt.setText(String.valueOf(goalval));
		goalcnt = intent.getExtras().getInt("cnt");
		cnttxt.setText(String.valueOf(goalcnt));
		cur_cnt = goalcnt;
		th.setDaemon(true);
		th.start();
	}
	

	public void on1(View v){
		cur_val += 5;
		cur_cnt--;
		jniDriver.segprint(cur_val);
		jniDriver.ledon(cur_cnt);
		if(cur_cnt == 0){
			if(cur_val == goalval){
				Toast.makeText(getBaseContext(), "Clear", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
	
	public void on2 (View v){
		cur_val = 0;
		cur_cnt = goalcnt;
		jniDriver.segprint(cur_val);
		jniDriver.ledon(cur_cnt);
	}
	
	public void on3(View v){
		cur_val *= 5;
		cur_cnt--;
		jniDriver.segprint(cur_val);
		jniDriver.ledon(cur_cnt);
		if(cur_cnt == 0){
			if(cur_val == goalval){
				Toast.makeText(getBaseContext(), "Clear", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
	
	public void on4(View v){
		cur_val *= 3;
		cur_cnt--;
		jniDriver.segprint(cur_val);
		jniDriver.ledon(cur_cnt);
		if(cur_cnt == 0){
			if(cur_val == goalval){
				Toast.makeText(getBaseContext(), "Clear", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
	
	public void on5(View v){
		cur_val /= 10;
		cur_cnt--;
		jniDriver.segprint(cur_val);
		jniDriver.ledon(cur_cnt);
		if(cur_cnt == 0){
			if(cur_val == goalval){
				Toast.makeText(getBaseContext(), "Clear", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	}
	
	protected void onPause() {
		th.interrupt();
		jniDriver.segclose();
		jniDriver.ledclose();
		jniDriver.lcdclear();
		jniDriver.lcdoff();
		super.onPause();
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
		jniDriver.lcdprint1Line("Hard Stage");
		jniDriver.lcdprint2Line("Goal: " + goalval);
		jniDriver.ledon(cur_cnt);
		super.onResume();
	}
	
	class Segment extends Thread{
		public void run(){
			try{
				while(!Thread.currentThread().isInterrupted()){
					jniDriver.segprint(cur_val);
				}
			} catch (Exception e){
				
			}
		}
	}

}
