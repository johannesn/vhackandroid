package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class SleepingAnimation implements MonsterAnimation {

	int max = 100;
	int counter = max;
	int[] head = new int[max];
	private View mView;

	// private Bitmap sleepinghead;

	public SleepingAnimation(View view, int headNo) {
		// Bitmap[] sleepinghead = new Bitmap[12];
		int[] sleepinghead = new int[12];
		sleepinghead[0] = R.drawable.head_1_c;
		sleepinghead[1] = R.drawable.head_2_c;
		sleepinghead[2] = R.drawable.head_3_c;
		sleepinghead[3] = R.drawable.head_4_c;
		sleepinghead[4] = R.drawable.head_5_c;
		sleepinghead[5] = R.drawable.head_6_c;
		sleepinghead[6] = R.drawable.head_7_c;
		sleepinghead[7] = R.drawable.head_8_c;
		sleepinghead[8] = R.drawable.head_9_c;
		sleepinghead[9] = R.drawable.head_10_c;
		sleepinghead[10] = R.drawable.head_11_c;
		sleepinghead[10] = R.drawable.head_10_c;
		sleepinghead[11] = R.drawable.head_12_c;
		int[] sleepinghead2 = new int[12];
		sleepinghead2[0] = R.drawable.head_1_d;
		sleepinghead2[1] = R.drawable.head_2_d;
		sleepinghead2[2] = R.drawable.head_3_d;
		sleepinghead2[3] = R.drawable.head_4_d;
		sleepinghead2[4] = R.drawable.head_5_d;
		sleepinghead2[5] = R.drawable.head_6_d;
		sleepinghead2[6] = R.drawable.head_7_d;
		sleepinghead2[7] = R.drawable.head_8_d;
		sleepinghead2[8] = R.drawable.head_9_d;
		sleepinghead2[9] = R.drawable.head_10_d;
		sleepinghead2[10] = R.drawable.head_11_d;
		sleepinghead2[11] = R.drawable.head_12_d;

		boolean first = true;
		for (int i = 0; i < max; i++) {
			if (i % 10 == 0) {
				first = first ? false : true;
			}

			if (first)
				head[i] = sleepinghead[headNo];
			else
				head[i] = sleepinghead2[headNo];
		}

		mView = view;
	}

	public void nextRound() {
		counter--;
	}

	public boolean isAlive() {
		return counter > 0;
	}

	public Bitmap getHead(Bitmap headToDraw) {
		if (isAlive())
			return BitmapFactory.decodeResource(mView.getResources(), head[max
					- counter]);
		return headToDraw;
	}

	public Bitmap getTorso(Bitmap torsoToDraw) {
		return torsoToDraw;
	}

	public Bitmap getLegs(Bitmap legsToDraw) {
		return legsToDraw;
	}

}
