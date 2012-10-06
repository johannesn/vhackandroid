package com.devjam.tamagotchi.game;

import java.util.LinkedList;
import java.util.List;

import com.devjam.tamagotchi.view.MonsterView;

public class Game {

	private int mGameSpeed;
	private Thread mGameThread;
	private Monster mMonster;
	private List<MonsterView> mMonsterViews;

	public Game() {
		this("Standardmon", 1000);
	}

	public Game(String name, int gameSpeed) {
		mMonster = new Monster(name);
		mGameSpeed = gameSpeed;
		mGameThread = new Thread(mGameRunning);
		mMonsterViews = new LinkedList<MonsterView>();
	}

	public void addView(MonsterView monsterView) {
		mMonsterViews.add(monsterView);
	}

	/**
	 * Starts game loop.
	 */
	public void start() {
		mGameThread.start();
	}

	/**
	 * Game loop.
	 */
	private Runnable mGameRunning = new Runnable() {

		@Override
		public void run() {
			try {
				while (!mMonster.isDead()) {
					// do game
					mMonster.growOlder();

					// draw
					for (MonsterView monsterView : mMonsterViews)
						monsterView.refreshView();

					// wait
					Thread.sleep(mGameSpeed);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	public Monster getMonster() {
		return mMonster;
	}
}
