package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class FeedingAnimation implements MonsterAnimation {

	int max = 41;
	int counter = max;
	int[] head = new int[max];
	private View mView;

	public FeedingAnimation(View view, int headNo) {
		int[] feedinghead = { R.drawable.head_1_e, R.drawable.head_2_e,
				R.drawable.head_3_e, R.drawable.head_4_e, R.drawable.head_5_e,
				R.drawable.head_6_e, R.drawable.head_7_e, R.drawable.head_8_e,
				R.drawable.head_9_e, R.drawable.head_10_e,
				R.drawable.head_11_e, R.drawable.head_12_e };

		int[] feedinghead2 = { R.drawable.head_1_f, R.drawable.head_2_f,
				R.drawable.head_3_f, R.drawable.head_4_f, R.drawable.head_5_f,
				R.drawable.head_6_f, R.drawable.head_7_f, R.drawable.head_8_f,
				R.drawable.head_9_f, R.drawable.head_10_f,
				R.drawable.head_11_f, R.drawable.head_12_f };

		int[] feedinghead3 = { R.drawable.head_1_g, R.drawable.head_2_g,
				R.drawable.head_3_g, R.drawable.head_4_g, R.drawable.head_5_g,
				R.drawable.head_6_g, R.drawable.head_7_g, R.drawable.head_8_g,
				R.drawable.head_9_g, R.drawable.head_10_g,
				R.drawable.head_11_g, R.drawable.head_12_g };

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
