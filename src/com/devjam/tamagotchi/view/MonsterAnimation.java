package com.devjam.tamagotchi.view;

import android.graphics.Bitmap;

public interface MonsterAnimation {

	public void nextRound();

	public boolean isAlive();

	public Bitmap getHead(Bitmap headToDraw);

	public Bitmap getTorso(Bitmap headToDraw);

	public Bitmap getLegs(Bitmap headToDraw);
}
