package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnStart:
			Intent i = new Intent(this,Start2Activity.class);
			startActivity(i);
			break;
		case R.id.btnHighscore:
			// TODO
			break;
		}
	}
}
