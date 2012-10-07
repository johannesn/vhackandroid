package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.game.Game;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GameService extends Service {
	public static Game mGame;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mGame.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		mGame.stop();
		super.onDestroy();
	}

}
