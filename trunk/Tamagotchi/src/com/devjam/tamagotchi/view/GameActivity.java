package com.devjam.tamagotchi.view;

import java.util.Random;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devjam.tamagotchi.PairingResultActivity;
import com.devjam.tamagotchi.R;
import com.devjam.tamagotchi.comm.AbstractNfcActivity;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.GameState;
import com.devjam.tamagotchi.game.LifeStage;
import com.devjam.tamagotchi.game.Monster;
import com.devjam.tamagotchi.game.MonsterDeathListener;
import com.devjam.tamagotchi.game.MonsterEvent;
import com.devjam.tamagotchi.game.MonsterEventListener;
import com.devjam.tamagotchi.game.MonsterEventReaction;

public class GameActivity extends AbstractNfcActivity implements MonsterView,
		MonsterEventListener, MonsterDeathListener {

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
	private MediaPlayer mMoopSound;
	private MediaPlayer mPairSound;
	private Intent gameService;

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
		if (!super.isNfcSupported()) {
			mPairButton.setVisibility(View.GONE);
		}
		mSad = (TextView) findViewById(R.id.sadness);
		mSad.setTypeface(myTypeface);
		mTired = (TextView) findViewById(R.id.tired);
		mTired.setTypeface(myTypeface);
		mHungry = (TextView) findViewById(R.id.hungry);
		mHungry.setTypeface(myTypeface);
		mImageShit = (ImageView) findViewById(R.id.imageShit);

		mMoopSound = MediaPlayer.create(this, R.raw.moooop);
		mMoopSound.setLooping(false);
		mPairSound = MediaPlayer.create(this, R.raw.pair);
		mPairSound.setLooping(false);

		// init game
		mGame = new Game("Penismon", 1000);
		mGame.addView(this);
		mMonster = mGame.getMonster();
		mMonster.setName(getIntent().getStringExtra("name"));
		mMonster.setMonsterEventListener(this);
		mMonster.setMonsterDeathListener(this);

		GameService.mGame = mGame;
		gameService = new Intent(this, GameService.class);
		startService(gameService);

		mLilMonView.setAnimationEndListener(mGame);
		mLilMonView.setMonster(mMonster);
	}

	private void startUiUpdates() {
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

	private void stopUiUpdates() {
		mGameUiThread.interrupt();
	}

	@Override
	public void onResume() {
		super.onResume();
		startUiUpdates();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopUiUpdates();
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
		if (view == mImageShit) {
			mMonster.reactToEvent(MonsterEventReaction.CLEAN);
			mImageShit.setVisibility(View.INVISIBLE);
		} else if (!mMonster.isDead()
				&& mGame.getGameState() == GameState.RUNNING) {
			if (view == mFeedButton) {
				mMonster.feed();
			} else if (view == mPlayButton) {
				mMonster.play();
			} else if (view == mSleepButton) {
				mMonster.sleep();
			} else if (view == mPairButton) {
				if (mMonster.getLifestage() == LifeStage.ADULT) {
					requestPairing(mMonster);
				} else {
					mMoopSound.start();
				}
			}
		} else {
			mMoopSound.start();
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
		if (mMonster.getLifestage() == LifeStage.ADULT) {
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
		} else {
			return null;
		}
	}

	@Override
	protected void pairSuccessful(Monster monster) {
		Toast.makeText(this, "Pairing Successful", Toast.LENGTH_LONG).show();
		this.mMonster = monster;
		monster.setGame(mGame);
		mGame.setMonster(monster);
		mLilMonView.setMonster(monster);
		mPairSound.start();

		PairingResultActivity.monster = monster;
		Intent intent = new Intent(this, PairingResultActivity.class);
		startActivity(intent);
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

	@Override
	public void actionRefused() {
		Toast.makeText(this, "Pairing Refused", Toast.LENGTH_LONG).show();
		mMoopSound.start();
	}

	@Override
	public void onMonsterDeath() {
		stopService(gameService);
	}
}
