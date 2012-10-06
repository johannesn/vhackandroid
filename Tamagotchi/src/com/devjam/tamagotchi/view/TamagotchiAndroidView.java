package com.devjam.tamagotchi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.devjam.tamagotchi.R;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.Monster;

public class TamagotchiAndroidView extends View {

//	public static final int MONSTERDENSITY = 360;
//	public static final int MONSTERDENSITY = 360;
	private static final String TAG = "MyCustomView";

	private Bitmap head;
	private Bitmap torso;
	private Bitmap legs;
	private Bitmap leg[];
	private Paint mPaint;
	private Game mAnimationEndListener;
	private Monster mMonster;

	public TamagotchiAndroidView(Context context) {
		super(context);
		initialize();
	}

	public TamagotchiAndroidView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public TamagotchiAndroidView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public void initialize() {
		head = BitmapFactory
				.decodeResource(getResources(), R.drawable.head_1_a);
		torso = BitmapFactory.decodeResource(getResources(), R.drawable.body_6);

		legs = BitmapFactory.decodeResource(getResources(), R.drawable.foot_1);
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

//		for (Bitmap b : leg)
//			b.setDensity(MONSTERDENSITY);
//
//		head.setDensity(MONSTERDENSITY);
//		torso.setDensity(MONSTERDENSITY);
//		legs.setDensity(MONSTERDENSITY);

		mPaint = new Paint();
	}

	public void setAnimationEndListener(Game listener) {
		mAnimationEndListener = listener;
	}

	public void setMonster(Monster monster) {
		mMonster = monster;
	}

	int whichHead = 0;
	int counter = 0;
	boolean animating = false;
	MonsterAnimation mMonsterAnimation = null;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.d(TAG, "Dens" + canvas.getDensity());

		Bitmap headToDraw = head;
		Bitmap torsoToDraw = torso;
		Bitmap legsToDraw = legs;
		counter++;
		if (counter % 40 == 0) {
			animating = true;
			whichHead = -1;
		}

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
		canvas.drawBitmap(leg[mMonster.getLegs()], 0, 465, mPaint);
	}

	public void startSleepingAnimation() {
		mMonsterAnimation = new SleepingAnimation(this);
	}

}
