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
import com.devjam.tamagotchi.game.LifeStage;
import com.devjam.tamagotchi.game.Monster;

public class MonsterPreview extends View {

	// public static final int MONSTERDENSITY = 360;
	// public static final int MONSTERDENSITY = 360;
	private static final String TAG = "MyCustomView";

	private int[] torsos;
	private int[] leg;
	private int[] heads;
	private Bitmap graveStone;
	private Bitmap baby;
	private Paint mPaint;
	private Game mAnimationEndListener;
	private Monster mMonster;

	private Context mContext;

	// private MediaPlayer mBiteSound;
	//
	// private MediaPlayer mSnoreSound;
	//
	// private MediaPlayer mPlaySound;

	public MonsterPreview(Context context) {
		super(context);
		mContext = context;
		initialize();
	}

	public MonsterPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initialize();
	}

	public MonsterPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initialize();
	}

	public void initialize() {

		graveStone = BitmapFactory.decodeResource(getResources(),
				R.drawable.rip);
		heads = new int[12];
		baby = BitmapFactory
				.decodeResource(getResources(), R.drawable.baby_1_a);
		heads[0] = R.drawable.head_1_a;
		heads[1] = R.drawable.head_2_a;
		heads[2] = R.drawable.head_3_a;
		heads[3] = R.drawable.head_4_a;
		heads[4] = R.drawable.head_5_a;
		heads[5] = R.drawable.head_6_a;
		heads[6] = R.drawable.head_7_a;
		heads[7] = R.drawable.head_8_a;
		heads[8] = R.drawable.head_9_a;
		heads[9] = R.drawable.head_10_a;
		heads[10] = R.drawable.head_11_a;
		heads[11] = R.drawable.head_12_a;

		BitmapFactory.decodeResource(getResources(), R.drawable.body_6);
		torsos = new int[12];
		torsos[0] = R.drawable.body_1;
		torsos[1] = R.drawable.body_2;
		torsos[2] = R.drawable.body_3;
		torsos[3] = R.drawable.body_4;
		torsos[4] = R.drawable.body_5;
		torsos[5] = R.drawable.body_6;
		torsos[6] = R.drawable.body_7;
		torsos[7] = R.drawable.body_8;
		torsos[8] = R.drawable.body_9;
		torsos[9] = R.drawable.body_10;
		torsos[10] = R.drawable.body_11;
		torsos[11] = R.drawable.body_12;

		leg = new int[10];
		leg[0] = R.drawable.foot_1;
		leg[1] = R.drawable.foot_2;
		leg[2] = R.drawable.foot_3;
		leg[3] = R.drawable.foot_4;
		leg[4] = R.drawable.foot_5;
		leg[5] = R.drawable.foot_6;
		leg[6] = R.drawable.foot_7;
		leg[7] = R.drawable.foot_8;
		leg[8] = R.drawable.foot_9;
		leg[9] = R.drawable.foot_10;

		mPaint = new Paint();

		// sounds
		// mBiteSound = MediaPlayer.create(mContext, R.raw.snd_bite_noloop);
		// mBiteSound.setLooping(false);
		// mSnoreSound = MediaPlayer.create(mContext, R.raw.snd_snore_noloop);
		// mSnoreSound.setLooping(false);
		// mPlaySound = MediaPlayer.create(mContext,
		// R.raw.snd_cutebroop_noloop);
		// mPlaySound.setLooping(false);
	}

	// public void setAnimationEndListener(Game listener) {
	// mAnimationEndListener = listener;
	// }

	public void setMonster(Monster monster) {
		mMonster = monster;
	}

	MonsterAnimation mMonsterAnimation = null;
	private int counter = 0;

	private int heartCounter;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Bitmap headToDraw = BitmapFactory.decodeResource(getResources(),
				heads[mMonster.getHead()]);
		Bitmap torsoToDraw = BitmapFactory.decodeResource(getResources(),
				torsos[mMonster.getTorso()]);
		Bitmap legsToDraw = BitmapFactory.decodeResource(getResources(),
				leg[mMonster.getLegs()]);

		counter = (counter + 1) % 100;
		if (counter == 0)
			mMonsterAnimation = new BlinkAnimation(this, mMonster.getHead());

		if (mMonsterAnimation != null) {
			headToDraw = mMonsterAnimation.getHead(headToDraw);
			torsoToDraw = mMonsterAnimation.getTorso(torsoToDraw);
			legsToDraw = mMonsterAnimation.getLegs(legsToDraw);
			mMonsterAnimation.nextRound();
			if (!mMonsterAnimation.isAlive()) {
				mMonsterAnimation = null;
			}
		}

		canvas.drawBitmap(headToDraw, 0, -100, mPaint);
		canvas.drawBitmap(torsoToDraw, 0, 335, mPaint);
		canvas.drawBitmap(legsToDraw, 0, 465, mPaint);

		Bitmap heart1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.heart_1);
		Bitmap heart2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.heart_2);
		Bitmap heart3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.heart_3);

		heartCounter = (heartCounter + 1) % 6;

		if (heartCounter > 3)
			canvas.drawBitmap(heart3, 0, 65, mPaint);
		else if (heartCounter > 1)
			canvas.drawBitmap(heart2, 0, 265, mPaint);
		else
			canvas.drawBitmap(heart1, 0, 465, mPaint);
	}
}
