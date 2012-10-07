package com.devjam.tamagotchi.game;

/**
 * Every monster event has a counter, after which something happens (can be good
 * or bad). <code>getCounter()</code> has to be called each round.
 * 
 * @author Qui Don Ho
 * 
 */
public interface MonsterEvent {

	/**
	 * Decreases counter by 1. Has to be called each round.
	 */
	public void count();

	/**
	 * Check if event should do its action.
	 * 
	 * @return true when counter is 0, false otherwise
	 */
	public boolean isEventDue();

	/**
	 * Do effect
	 */
	public void onDue();

	/**
	 * Get the remaining time for reacting to that event.
	 * 
	 * @return remaining time-to-live
	 */
	public int getTTL();

	/**
	 * React to event.
	 */
	public boolean onReact(MonsterEventReaction reaction);
}
