package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class BlinkAnimation implements MonsterAnimation {

	int max = 6;
	int counter = max;
	int[] head = new int[max];
	private View mView;

	public BlinkAnimation(View view, int headNo) {

		int[] feedinghead2 = { R.drawable.head_1_b, R.drawable.head_2_b,
				R.drawable.head_3_b, R.drawable.head_4_b, R.drawable.head_5_b,
				R.drawable.head_6_b, R.drawable.head_7_b, R.drawable.head_8_b,
				R.drawable.head_9_b, R.drawable.head_10_a,
				R.drawable.head_11_b, R.drawable.head_12_b };

		int[] feedinghead3 = { R.drawable.head_1_c, R.drawable.head_2_c,
				R.drawable.head_3_c, R.drawable.head_4_c, R.drawable.head_5_c,
				R.drawable.head_6_c, R.drawable.head_7_c, R.drawable.head_8_c,
				R.drawable.head_9_c, R.drawable.head_10_c,
				R.drawable.head_11_c, R.drawable.head_12_c };

		for (int i = 0; i < max; i++) {

			if (i > 3)
				head[i] = feedinghead2[headNo];
			else if (i > 0)
				head[i] = feedinghead3[headNo];
			else
				head[i] = feedinghead2[headNo];
		}

		mView = view;
	}

	@Override
	public void nextRound() {
		counter--;
	}

	@Override
	public boolean isAlive() {
		return counter > 0;
	}

	@Override
	public Bitmap getHead(Bitmap headToDraw) {
		if (isAlive())
			return BitmapFactory.decodeResource(mView.getResources(), head[max
					- counter]);
		return headToDraw;
	}

	@Override
	public Bitmap getTorso(Bitmap torsoToDraw) {
		return torsoToDraw;
	}

	@Override
	public Bitmap getLegs(Bitmap legsToDraw) {
		return legsToDraw;
	}

}
