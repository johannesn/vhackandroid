package com.devjam.tamagotchi.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devjam.tamagotchi.R;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.Monster;

public class GameActivity extends Activity implements MonsterView {

	private TamagotchiAndroidView mLilMonView;
	private Game mGame;
	private Monster mMonster;
	private TextView mHud;
	private Thread mGameUiThread;
	private Button mFeedButton;
	private Button mPlayButon;
	private Button mSleepButon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// get ui stuff
		mLilMonView = (TamagotchiAndroidView) findViewById(R.id.customview);
		mHud = (TextView) findViewById(R.id.main_text);
		mFeedButton = (Button) findViewById(R.id.btn_feed);
		mPlayButon = (Button) findViewById(R.id.btn_play);
		mSleepButon = (Button) findViewById(R.id.btn_sleep);

		// init game
		mGame = new Game("Penismon", 4000);
		mGame.addView(this);
		mMonster = mGame.getMonster();
		mGame.start();
		mLilMonView.setAnimationEndListener(mGame);
		mLilMonView.setMonster(mMonster);

		// start gui updating thread
		mGameUiThread = new Thread(new Runnable() {

			public void run() {
				try {
					while (!Thread.interrupted()) {
						mLilMonView.postInvalidate();
						Thread.sleep(50);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mGameUiThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Callback for the action buttons
	 * 
	 * @param view
	 *            the button clicked
	 */
	public void onActionClick(View view) {
		if (view == mFeedButton)
			mMonster.feed();
		else if (view == mPlayButon)
			mMonster.play();
		else if (view == mSleepButon)
			mMonster.sleep();

		refreshView();
	}

	public void refreshView() {
		// display monster needs
		runOnUiThread(mUpdateHud);

		switch (mGame.getGameState()) {
		case FEEDING:
			break;
		case PLAYING:
			break;
		case SLEEPING:
			mLilMonView.startSleepingAnimation();
			break;
		case RUNNING:
			break;
		}
	}

	private Runnable mUpdateHud = new Runnable() {

		public void run() {
			mHud.setText("Hungrig: " + mMonster.getHunger() + ", Traurig: "
					+ mMonster.getSadness() + ", M�de: "
					+ mMonster.getTiredness());
		}
	};
}