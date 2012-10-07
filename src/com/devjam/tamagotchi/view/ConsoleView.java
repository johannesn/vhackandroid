package com.devjam.tamagotchi.view;

import java.util.EnumSet;
import java.util.Scanner;

import com.devjam.tamagotchi.game.CauseOfDeath;
import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.game.LifeStage;
import com.devjam.tamagotchi.game.Monster;
import com.devjam.tamagotchi.game.MonsterEvent;
import com.devjam.tamagotchi.game.MonsterEventReaction;
import com.devjam.tamagotchi.game.ShitEvent;

public class ConsoleView implements MonsterView {

	private Game mGame;
	private Monster mMonster;

	public ConsoleView(Game game) {
		mGame = game;
		mGame.addView(this);
		mMonster = game.getMonster();

		new Thread(new Runnable() {

			@Override
			public void run() {
				Scanner input = new Scanner(System.in);

				boolean quit = false;
				while (!quit) {
					String command = input.nextLine();

					if (command.equals("feed")) {
						mMonster.feed();
					} else if (command.equals("play")) {
						mMonster.play();
					} else if (command.equals("sleep")) {
						mMonster.sleep();
					} else if (command.equals("clean")) {
						if (mMonster.reactToEvent(MonsterEventReaction.CLEAN))
							System.out.println("Kacke weggeräumt!");
					}
				}

				System.out.println("Stoppppp!!!!!p");
			}
		}).start();
	}

	@Override
	public void refreshView() {
		System.out.println("=============================================");
		// display monster status
		String name = mMonster.getName();
		LifeStage lifeStage = mMonster.getLifestage();
		int age = mMonster.getAge();
		String lifeStageName = null;
		switch (lifeStage) {
		case BABY:
			lifeStageName = "ein Baby";
			break;
		case ADOLESCENT:
			lifeStageName = "ein Kind";
			break;
		case ADULT:
			lifeStageName = "ein Erwachsener";
			break;
		case DEAD:
			lifeStageName = "tot! Starb wegen ";
			EnumSet<CauseOfDeath> causes = mMonster.getCauseOfDeath();
			if (causes.contains(CauseOfDeath.AGE))
				lifeStageName += "Alter ";
			if (causes.contains(CauseOfDeath.HUNGER))
				lifeStageName += "Hunger ";
			if (causes.contains(CauseOfDeath.TIREDNESS))
				lifeStageName += "Müdigkeit ";
			if (causes.contains(CauseOfDeath.SADNESS))
				lifeStageName += "Traurigkeit ";
			if (causes.contains(CauseOfDeath.DISEASE))
				lifeStageName += "einer Krankheit ";
			break;
		}
		System.out.println(name + " ist " + age + " Tage alt und "
				+ lifeStageName);

		// display monster needs
		int hunger = mMonster.getHunger();
		int sadness = mMonster.getSadness();
		int tiredness = mMonster.getTiredness();
		System.out.println("Hungrig: " + hunger + ", Traurig: " + sadness
				+ ", Müde: " + tiredness);

		// display current event
		MonsterEvent currentEvent = mMonster.getCurrentEvent();
		if (currentEvent instanceof ShitEvent) {
			System.out.println(name + " hat geschissen! Zeit wegzuräumen: "
					+ currentEvent.getTTL() + " Stunden!");
		}
	}
}
