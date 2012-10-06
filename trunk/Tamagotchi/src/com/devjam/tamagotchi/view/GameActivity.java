package com.devjam.tamagotchi.view;

import java.util.Random;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devjam.tamagotchi.R;
import com.devjam.tamagotchi.comm.AbstractNfcActivity;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.Monster;
import com.devjam.tamagotchi.game.MonsterEvent;
import com.devjam.tamagotchi.game.MonsterEventListener;
import com.devjam.tamagotchi.game.MonsterEventReaction;

public class GameActivity extends AbstractNfcActivity implements MonsterView,
		MonsterEventListener {

	private TamagotchiAndroidView mLilMonView;
	private Game mGame;
	private Monster mMonster;
	private Thread mGameUiThread;
	private ImageButton mFeedButton;
	private ImageButton mPlayButton;
	private ImageButton mSleepButton;
	private ImageButton mPairButton;
	private TextView mHungry;
	private TextView mSad;
	private TextView mTired;
	private ImageView mImageShit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Typeface myTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/BistroSketch.ttf");

		// get ui stuff
		mLilMonView = (TamagotchiAndroidView) findViewById(R.id.customview);
		mFeedButton = (ImageButton) findViewById(R.id.btn_feed);
		mPlayButton = (ImageButton) findViewById(R.id.btn_play);
		mSleepButton = (ImageButton) findViewById(R.id.btn_sleep);
		mPairButton = (ImageButton) findViewById(R.id.btn_pair);
		mSad = (TextView) findViewById(R.id.sadness);
		mSad.setTypeface(myTypeface);
		mTired = (TextView) findViewById(R.id.tired);
		mTired.setTypeface(myTypeface);
		mHungry = (TextView) findViewById(R.id.hungry);
		mHungry.setTypeface(myTypeface);

		mImageShit = (ImageView) findViewById(R.id.imageShit);

		// init game
		mGame = new Game("Penismon", 4000);
		mGame.addView(this);
		mMonster = mGame.getMonster();
		mMonster.setMonsterEventListener(this);
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
		else if (view == mPlayButton)
			mMonster.play();
		else if (view == mSleepButton)
			mMonster.sleep();
		else if (view == mPairButton)
			requestPairing(mMonster);
		else if (view == mImageShit) {
			mMonster.reactToEvent(MonsterEventReaction.CLEAN);
			mImageShit.setVisibility(View.INVISIBLE);
		}

		refreshView();
	}

	public void refreshView() {
		// display monster needs
		runOnUiThread(mUpdateHud);

		switch (mGame.getGameState()) {
		case FEEDING:
			mLilMonView.startFeedingAnimation();
			break;
		case PLAYING:
			mLilMonView.startPlayingAnimation();
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
			mHungry.setText(Math.max(100 - mMonster.getHunger(), 0) + "");
			mSad.setText(Math.max(100 - mMonster.getSadness(), 0) + "");
			mTired.setText(Math.max(100 - mMonster.getTiredness(), 0) + "");
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

	@Override
	public void onMonsterEvent(MonsterEvent monsterEvent) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mImageShit.setVisibility(View.VISIBLE);
			}
		});
	}
}
