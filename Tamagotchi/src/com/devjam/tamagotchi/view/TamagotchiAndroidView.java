package com.devjam.tamagotchi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.devjam.tamagotchi.R;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.Monster;

public class TamagotchiAndroidView extends View {

	// public static final int MONSTERDENSITY = 360;
	// public static final int MONSTERDENSITY = 360;
	private static final String TAG = "MyCustomView";

	private Bitmap torsos[];
	private Bitmap leg[];
	private Bitmap[] heads;
	private Paint mPaint;
	private Game mAnimationEndListener;
	private Monster mMonster;

	private Context mContext;

	private MediaPlayer mBiteSound;

	private MediaPlayer mSnoreSound;

	private MediaPlayer mPlaySound;

	public TamagotchiAndroidView(Context context) {
		super(context);
		mContext = context;
		initialize();
	}

	public TamagotchiAndroidView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initialize();
	}

	public TamagotchiAndroidView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initialize();
	}

	public void initialize() {
		BitmapFactory.decodeResource(getResources(), R.drawable.head_1_a);
		heads = new Bitmap[12];
		heads[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_1_a);
		heads[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_2_a);
		heads[2] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_3_a);
		heads[3] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_4_a);
		heads[4] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_5_a);
		heads[5] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_6_a);
		heads[6] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_7_a);
		heads[7] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_8_a);
		heads[8] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_9_a);
		heads[9] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_10_a);
		heads[10] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_11_a);
		heads[11] = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_12_a);

		BitmapFactory.decodeResource(getResources(), R.drawable.body_6);
		torsos = new Bitmap[12];
		torsos[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_1);
		torsos[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_2);
		torsos[2] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_3);
		torsos[3] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_4);
		torsos[4] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_5);
		torsos[5] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_6);
		torsos[6] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_7);
		torsos[7] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_8);
		torsos[8] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_9);
		torsos[9] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_10);
		torsos[10] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_11);
		torsos[11] = BitmapFactory.decodeResource(getResources(),
				R.drawable.body_12);

		BitmapFactory.decodeResource(getResources(), R.drawable.foot_1);
		leg = new Bitmap[10];
		leg[0] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_1);
		leg[1] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_2);
		leg[2] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_3);
		leg[3] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_4);
		leg[4] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_5);
		leg[5] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_6);
		leg[6] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_7);
		leg[7] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_8);
		leg[8] = BitmapFactory
				.decodeResource(getResources(), R.drawable.foot_9);
		leg[9] = BitmapFactory.decodeResource(getResources(),
				R.drawable.foot_10);

		// for (Bitmap b : leg)
		// b.setDensity(MONSTERDENSITY);
		//
		// head.setDensity(MONSTERDENSITY);
		// torso.setDensity(MONSTERDENSITY);
		// legs.setDensity(MONSTERDENSITY);

		mPaint = new Paint();

		// sounds
		mBiteSound = MediaPlayer.create(mContext, R.raw.snd_bite_noloop);
		mBiteSound.setLooping(false);
		mSnoreSound = MediaPlayer.create(mContext, R.raw.snd_snore_noloop);
		mSnoreSound.setLooping(false);
		mPlaySound = MediaPlayer.create(mContext, R.raw.snd_cutebroop_noloop);
		mPlaySound.setLooping(false);
	}

	public void setAnimationEndListener(Game listener) {
		mAnimationEndListener = listener;
	}

	public void setMonster(Monster monster) {
		mMonster = monster;
	}

	MonsterAnimation mMonsterAnimation = null;
	private int counter = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.d(TAG, "Dens" + canvas.getDensity());

		Bitmap headToDraw = heads[mMonster.getHead()];
		Bitmap torsoToDraw = torsos[mMonster.getTorso()];
		Bitmap legsToDraw = leg[mMonster.getLegs()];

		counter = (counter + 1) % 100;
		if (counter == 0)
			mMonsterAnimation = new BlinkAnimation(this, mMonster.getHead());

		// if (animating) {
		// whichHead = (whichHead + 1) % 4;
		// switch (whichHead) {
		// case 0:
		// theHead = head2;
		// break;
		// case 1:
		// theHead = head3;
		// break;
		// case 2:
		// theHead = head2;
		// break;
		// case 3:
		// theHead = head;
		// animating = false;
		// break;
		// }
		// }
		if (mMonsterAnimation != null) {
			headToDraw = mMonsterAnimation.getHead(headToDraw);
			torsoToDraw = mMonsterAnimation.getTorso(torsoToDraw);
			legsToDraw = mMonsterAnimation.getLegs(legsToDraw);
			mMonsterAnimation.nextRound();
			if (!mMonsterAnimation.isAlive()) {
				mMonsterAnimation = null;
				mAnimationEndListener.start();
			}
		}

		canvas.drawBitmap(headToDraw, 0, -100, mPaint);
		canvas.drawBitmap(torsoToDraw, 0, 335, mPaint);
		canvas.drawBitmap(legsToDraw, 0, 465, mPaint);
	}

	public void startSleepingAnimation() {
		mMonsterAnimation = new SleepingAnimation(this, mMonster.getHead());
		mSnoreSound.start();
	}

	public void startPlayingAnimation() {
		mMonsterAnimation = new PlayingAnimation(this, mMonster.getHead());
		mPlaySound.start();
	}

	public void startFeedingAnimation() {
		mMonsterAnimation = new FeedingAnimation(this, mMonster.getHead());
		mBiteSound.start();
	}

}
