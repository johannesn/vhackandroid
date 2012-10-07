package com.devjam.tamagotchi.game;

import java.util.EnumSet;
import java.util.Random;

public class Monster {

	private static final int ROUNDUNITS_PER_DAY = 12;
	private static final int ROUNDUNITS_TILL_DEAD = 100;
	private static final int ROUNDUNITS_TILL_ADULT = 50;
	private static final int ROUNDUNITS_TILL_ADOLESCENT = 20;
	private static final int MAX_NEED = 100;

	// looks
	int mHead;
	int mTorso;
	int mLegs;
	int mSkinColor;

	// Needs. If any of this reaches MAX_NEED, Monster dies.
	int mHunger;
	int mSadness;
	int mTiredness;

	// age in round units
	int mAgeRoundUnits;
	LifeStage mLifeStage;

	// other attributes
	String mName;
	private EnumSet<CauseOfDeath> mCauseOfDeath;

	// Current event. Occurs randomly, and you have some time to react to that,
	// otherwise something happens (bad probably).
	private MonsterEvent mCurrentEvent;
	private MonsterEventListener monsterEventListener;
	private MonsterDeathListener monsterDeathListener;
	private Game mGame;

	public Monster(String name) {
		this(2, 0, 1, 0, 0, 0, 0, 0, name);
	}

	public Monster(int mHead, int mTorso, int mLegs, int mSkinColor,
			int mHunger, int mSadness, int mTiredness, int mAgeRoundUnits,
			String mName) {
		this.mHead = mHead;
		this.mTorso = mTorso;
		this.mLegs = mLegs;
		this.mSkinColor = mSkinColor;
		this.mHunger = mHunger;
		this.mSadness = mSadness;
		this.mTiredness = mTiredness;
		this.mAgeRoundUnits = mAgeRoundUnits;
		this.mName = mName;

		mCauseOfDeath = EnumSet.noneOf(CauseOfDeath.class);
	}

	public void setGame(Game game) {
		mGame = game;
	}

	public void feed() {
		mHunger -= 10;
		if (mHunger < 0)
			mHunger = 0;

		mGame.switchState(GameState.FEEDING);
	}

	public void play() {
		mSadness -= 20;
		if (mSadness < 0)
			mSadness = 0;

		mGame.switchState(GameState.PLAYING);
	}

	public void sleep() {
		mTiredness -= 30;
		if (mTiredness < 0)
			mTiredness = 0;

		mGame.switchState(GameState.SLEEPING);
	}

	/**
	 * Age one round unit. Calculate what happened in that time unit.
	 */
	public void growOlder() {
		// compute age
		if (mAgeRoundUnits > ROUNDUNITS_TILL_DEAD) {
			mLifeStage = LifeStage.DEAD;
		} else if (mAgeRoundUnits > ROUNDUNITS_TILL_ADULT) {
			mLifeStage = LifeStage.ADULT;
		} else if (mAgeRoundUnits > ROUNDUNITS_TILL_ADOLESCENT) {
			mLifeStage = LifeStage.ADOLESCENT;
		} else {
			mLifeStage = LifeStage.BABY;
		}

		// increase needs and age (things that happen after time)
		mHunger += 1;
		mSadness += 1;
		mTiredness += 1;
		mAgeRoundUnits += 1;

		// compute death (based on the above stats)
		if (mHunger > MAX_NEED)
			die(CauseOfDeath.HUNGER);
		if (mSadness > MAX_NEED)
			die(CauseOfDeath.SADNESS);
		if (mTiredness > MAX_NEED)
			die(CauseOfDeath.TIREDNESS);

		// throw dice for events
		if (mCurrentEvent == null) {
			Random random = new Random(System.currentTimeMillis());
			int die = random.nextInt(20);
			if (die == 5) {
				mCurrentEvent = new ShitEvent(this);
				if (monsterEventListener != null) {
					monsterEventListener.onMonsterEvent(mCurrentEvent);
				}
			}
		} else {
			if (mCurrentEvent.isEventDue()) {
				mCurrentEvent.onDue();
			} else {
				mCurrentEvent.count();
			}
		}
	}

	public int getHunger() {
		return mHunger;
	}

	public int getSadness() {
		return mSadness;
	}

	public int getTiredness() {
		return mTiredness;
	}

	/**
	 * Get the age in days
	 * 
	 * @return age in days
	 */
	public int getAge() {
		return mAgeRoundUnits / ROUNDUNITS_PER_DAY;
	}

	public LifeStage getLifestage() {
		return mLifeStage;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public boolean isDead() {
		return mLifeStage == LifeStage.DEAD;
	}

	public EnumSet<CauseOfDeath> getCauseOfDeath() {
		return mCauseOfDeath;
	}

	public void die(CauseOfDeath cause) {
		mLifeStage = LifeStage.DEAD;
		mCauseOfDeath.add(cause);
		if(monsterDeathListener!=null){
			monsterDeathListener.onMonsterDeath();
		}
	}

	public MonsterEvent getCurrentEvent() {
		return mCurrentEvent;
	}

	public boolean reactToEvent(MonsterEventReaction reaction) {
		if (mCurrentEvent.onReact(reaction)) {
			mCurrentEvent = null;
			return true;
		}

		return false;
	}

	public int getHead() {
		return mHead;
	}

	public void setHead(int mHead) {
		this.mHead = mHead;
	}

	public int getTorso() {
		return mTorso;
	}

	public void setTorso(int mTorso) {
		this.mTorso = mTorso;
	}

	public int getLegs() {
		return mLegs;
	}

	public void setLegs(int mLegs) {
		this.mLegs = mLegs;
	}

	public int getSkinColor() {
		return mSkinColor;
	}

	public void setSkinColor(int mSkinColor) {
		this.mSkinColor = mSkinColor;
	}

	public int getAgeRoundUnits() {
		return mAgeRoundUnits;
	}

	public void setAgeRoundUnits(int mAgeRoundUnits) {
		this.mAgeRoundUnits = mAgeRoundUnits;
	}

	public void setMonsterEventListener(
			MonsterEventListener monsterEventListener) {
		this.monsterEventListener = monsterEventListener;
	}

	public void setMonsterDeathListener(MonsterDeathListener monsterDeathListener) {
		this.monsterDeathListener = monsterDeathListener;
	}
}
