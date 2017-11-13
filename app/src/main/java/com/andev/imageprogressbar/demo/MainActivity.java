package com.andev.imageprogressbar.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.andev.imageprogressbar.ImageProgressBar;
import com.andev.imageprogressbar.OnProgressBarListener;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";

	ImageProgressBar ipb_no2;
	ImageProgressBar ipb_no4;

	private Timer timer2;
	private Timer timer4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ipb_no2 = (ImageProgressBar)findViewById(R.id.ipb_no2);
		ipb_no4 = (ImageProgressBar)findViewById(R.id.ipb_no4);

		timer2 = new Timer();
		timer2.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ipb_no2.incrementProgressBy(1);
					}
				});
			}
		}, 1000, 100);

		ipb_no2.setOnProgressBarListener(new OnProgressBarListener() {
			@Override
			public void onProgressChange(int current, int max) {
				Log.d(TAG, "current: " + current + " max: " + max);
				if(current == max){
					ipb_no2.setProgress(0);
				}
			}
		});


		timer4 = new Timer();
		timer4.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ipb_no4.incrementProgressBy(2);
					}
				});
			}
		}, 1000, 100);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer2.cancel();
		timer4.cancel();
	}
}