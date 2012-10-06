package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class SleepingAnimation implements MonsterAnimation {

	int max = 100;
	int counter = max;
	Bitmap[] head = new Bitmap[max];

	// private Bitmap sleepinghead;

	public SleepingAnimation(View view) {
		Bitmap sleepinghead = BitmapFactory.decodeResource(view.getResources(),
				R.drawable.head_1_c);
		sleepinghead.setDensity(TamagotchiAndroidView.MONSTERDENSITY);
		Bitmap sleepinghead2 = BitmapFactory.decodeResource(
				view.getResources(), R.drawable.head_1_d);
		sleepinghead2.setDensity(TamagotchiAndroidView.MONSTERDENSITY);

		boolean first = true;
		for (int i = 0; i < max; i++) {
			if (i % 10 == 0) {
				first = first ? false : true;
			}

			if (first)
				head[i] = sleepinghead;
			else
				head[i] = sleepinghead2;
		}
	}

	public void nextRound() {
		counter--;
	}

	public boolean isAlive() {
		return counter > 0;
	}

	public Bitmap getHead(Bitmap headToDraw) {
		if (isAlive())
			return head[max - counter];
		return headToDraw;
	}

	public Bitmap getTorso(Bitmap torsoToDraw) {
		return torsoToDraw;
	}

	public Bitmap getLegs(Bitmap legsToDraw) {
		return legsToDraw;
	}

}
