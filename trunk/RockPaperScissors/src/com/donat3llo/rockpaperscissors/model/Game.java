package com.donat3llo.rockpaperscissors.model;

public class Game {

	private GameState mGameState;

	public Game() {
		mGameState = GameState.MyTurn;
	}

	public boolean isMyTurn() {
		return mGameState == GameState.MyTurn ? true : false;
	}
}
