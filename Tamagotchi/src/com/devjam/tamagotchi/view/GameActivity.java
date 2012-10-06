package com.devjam.tamagotchi.view;

import java.util.Random;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devjam.tamagotchi.R;
import com.devjam.tamagotchi.comm.AbstractNfcActivity;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.Monster;

public class GameActivity extends AbstractNfcActivity implements MonsterView {

	private TamagotchiAndroidView mLilMonView;
	private Game mGame;
	private Monster mMonster;
	private TextView mHud;
	private Thread mGameUiThread;
	private Button mFeedButton;
	private Button mPlayButon;
	private Button mSleepButon;
	private View mPairButton;

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
		mPairButton = (Button) findViewById(R.id.btn_pair);

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
		else if (view == mPairButton)
			requestPairing(mMonster);

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
			mHud.setText("Hungy: " + mMonster.getHunger() + ", Sad: "
					+ mMonster.getSadness() + ", Tired: "
					+ mMonster.getTiredness());
		}
	};

	@Override
	protected Monster pairWithMonster(Monster monster) {
		Random rand = new Random(System.currentTimeMillis());
		Monster child = new Monster("Helmut");
		if (rand.nextBoolean()) {
			child.setLegs(mMonster.getLegs());
		} else {
			child.setLegs(monster.getLegs());
		}
		if (rand.nextBoolean()) {
			child.setTorso(mMonster.getTorso());
		} else {
			child.setTorso(monster.getTorso());
		}
		if (rand.nextBoolean()) {
			child.setHead(mMonster.getHead());
		} else {
			child.setHead(monster.getHead());
		}
		if (rand.nextBoolean()) {
			child.setSkinColor(mMonster.getSkinColor());
		} else {
			child.setSkinColor(monster.getSkinColor());
		}
		return child;
	}

	@Override
	protected void pairSuccessful(Monster monster) {
		Toast.makeText(this, "Pairing Successful", Toast.LENGTH_LONG).show();
		this.mMonster = monster;
		monster.setGame(mGame);
		mLilMonView.setMonster(monster);
	}
}
