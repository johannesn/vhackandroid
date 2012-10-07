package com.devjam.tamagotchi.test;

import com.devjam.tamagotchi.game.Game;
import com.devjam.tamagotchi.view.ConsoleView;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game("Penismon", 4000);
		new ConsoleView(game);
		game.start();
	}

}
