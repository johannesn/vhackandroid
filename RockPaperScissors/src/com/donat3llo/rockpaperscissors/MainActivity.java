package com.donat3llo.rockpaperscissors;

import com.donat3llo.rockpaperscissors.comm.CommunicationHandler;
import com.donat3llo.rockpaperscissors.comm.TcpCommHandler;
import com.donat3llo.rockpaperscissors.model.Game;
import com.donat3llo.rockpaperscissors.model.Item;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private Button mRockButton;
	private Button mPaperButton;
	private Button mScissorsButton;

	private CommunicationHandler mCommHandler;

	private Game mGame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGame = new Game();
		mCommHandler = new TcpCommHandler();

		mRockButton = (Button) findViewById(R.id.main_rockBtn);
		mPaperButton = (Button) findViewById(R.id.main_paperBtn);
		mScissorsButton = (Button) findViewById(R.id.main_scissorsBtn);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onClick(View view) {
		if (mGame.isMyTurn()) {
			if (view == mRockButton) {
				mCommHandler.sendItem(Item.Rock);
			} else if (view == mPaperButton) {
				mCommHandler.sendItem(Item.Paper);
			} else if (view == mScissorsButton) {
				mCommHandler.sendItem(Item.Scissors);
			}
		}
	}
}
