package com.devjam.tamagotchi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.devjam.tamagotchi.game.Monster;
import com.devjam.tamagotchi.view.MonsterPreview;

public class PairingResultActivity extends Activity {

	public static Monster monster;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pairing_result);

		final MonsterPreview mp = (MonsterPreview) findViewById(R.id.pairresult_monster);
		mp.setMonster(monster);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < 60; i++) {
						mp.postInvalidate();
						Thread.sleep(50);
					}
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_pairing_result, menu);
		return true;
	}
}
