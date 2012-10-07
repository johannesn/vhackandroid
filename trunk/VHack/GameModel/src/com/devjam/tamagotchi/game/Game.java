package com.devjam.tamagotchi.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.devjam.tamagotchi.view.MonsterView;

public class Game {

	private int mGameSpeed;
	private Thread mGameThread;
	private Monster mMonster;
	private List<MonsterView> mMonsterViews;
	private GameState mGameState;

	public Game() {
		this("Standardmon", 100);
	}

	public Game(String name, int gameSpeed) {
		mMonster = new Monster(name);
		Random rand = new Random(System.currentTimeMillis());
		mMonster.setLegs(rand.nextInt(10));
		mMonster.setTorso(rand.nextInt(12));
		mMonster.setHead(rand.nextInt(12));
		mGameSpeed = gameSpeed;
		mGameState = GameState.RUNNING;
		mMonsterViews = new LinkedList<MonsterView>();

		mMonster.setGame(this);
	}

	public void addView(MonsterView monsterView) {
		mMonsterViews.add(monsterView);
	}

	/**
	 * Starts game loop.
	 */
	public void start() {
		if (mGameThread == null || !mGameThread.isAlive()) {
			mGameThread = new Thread(mGameRunning);
			mGameThread.start();
			mGameState = GameState.RUNNING;
		}
	}

	/**
	 * Game loop.
	 */
	private Runnable mGameRunning = new Runnable() {

		@Override
		public void run() {
			try {
				while (!mMonster.isDead() || Thread.interrupted()) {
					// do game
					mMonster.growOlder();

					// draw
					for (MonsterView monsterView : mMonsterViews)
						monsterView.refreshView();

					// wait
					Thread.sleep(mGameSpeed);
				}
			} catch (InterruptedException e) {
			}
		}
	};

	public Monster getMonster() {
		return mMonster;
	}

	/**
	 * Pauses game and saves state, so that correct animation can be shown.
	 * 
	 * @param gameState
	 *            the state to switch to
	 */
	public void switchState(GameState gameState) {
		mGameThread.interrupt();
		mGameState = gameState;
		// draw
		for (MonsterView monsterView : mMonsterViews)
			monsterView.refreshView();
	}

	public GameState getGameState() {
		return mGameState;
	}
}
