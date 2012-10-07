package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class PlayingAnimation implements MonsterAnimation {

	int max = 41;
	int counter = max;
	int[] head = new int[max];
	private View mView;

	public PlayingAnimation(View view, int headNo) {
		int[] feedinghead = { R.drawable.head_1_h, R.drawable.head_2_h,
				R.drawable.head_3_h, R.drawable.head_4_h, R.drawable.head_5_h,
				R.drawable.head_6_h, R.drawable.head_7_h, R.drawable.head_8_h,
				R.drawable.head_9_h, R.drawable.head_10_h,
				R.drawable.head_11_h, R.drawable.head_12_h };

		int[] feedinghead2 = { R.drawable.head_1_i, R.drawable.head_2_i,
				R.drawable.head_3_i, R.drawable.head_4_i, R.drawable.head_5_i,
				R.drawable.head_6_i, R.drawable.head_7_i, R.drawable.head_8_i,
				R.drawable.head_9_i, R.drawable.head_10_i,
				R.drawable.head_11_i, R.drawable.head_12_i };

		int[] feedinghead3 = { R.drawable.head_1_j, R.drawable.head_2_j,
				R.drawable.head_3_j, R.drawable.head_4_j, R.drawable.head_5_j,
				R.drawable.head_6_j, R.drawable.head_7_j, R.drawable.head_8_j,
				R.drawable.head_9_j, R.drawable.head_10_j,
				R.drawable.head_11_j, R.drawable.head_12_j };

		int which = 0;
		for (int i = 0; i < max; i++) {
			if (i % 7 == 6) {
				which = (which + 1) % 3;
			}

			if (which == 0)
				head[i] = feedinghead[headNo];
			else if (which == 1)
				head[i] = feedinghead2[headNo];
			else
				head[i] = feedinghead3[headNo];
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
