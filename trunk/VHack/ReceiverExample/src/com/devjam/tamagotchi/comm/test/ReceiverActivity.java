package com.devjam.tamagotchi.comm.test;

import com.devjam.tamagotchi.comm.AbstractNfcActivity;
import com.devjam.tamagotchi.game.Monster;

import android.os.Bundle;
import android.widget.TextView;

public class ReceiverActivity extends AbstractNfcActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected Monster pairWithMonster(Monster monster) {
		return new Monster("Kind");
	}

	@Override
	protected void pairSuccessful(Monster monster) {
		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText(monster.getName());
	}
}
