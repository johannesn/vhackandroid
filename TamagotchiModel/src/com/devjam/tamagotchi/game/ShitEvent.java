package com.devjam.tamagotchi.game;

public class ShitEvent implements MonsterEvent {

	// how many rounds does this event count?
	private static final int TIME_TO_LIVE = 5;

	private int mCounter;
	private Monster mMonster;

	public ShitEvent(Monster monster) {
		mMonster = monster;
		mCounter = TIME_TO_LIVE;
	}

	@Override
	public void count() {
		mCounter--;
	}

	@Override
	public boolean isEventDue() {
		return mCounter <= 0;
	}

	@Override
	public void onDue() {
		mMonster.die(CauseOfDeath.DISEASE);
	}

	@Override
	public boolean onReact(MonsterEventReaction reaction) {
		// nothing happens (bad thing was prevented)
		return reaction == MonsterEventReaction.CLEAN;
	}

	@Override
	public int getTTL() {
		return mCounter;
	}
}
