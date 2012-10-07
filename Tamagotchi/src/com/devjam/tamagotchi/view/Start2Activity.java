package com.devjam.tamagotchi.view;

import com.devjam.tamagotchi.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Start2Activity extends Activity {

	private EditText name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start2);

		Typeface myTypeface = Typeface.createFromAsset(getAssets(),
				"fonts/BistroSketch.ttf");
		name = (EditText) findViewById(R.id.name_field);
		name.setTypeface(myTypeface);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_start2, menu);
		return true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnBegin:
			Intent i = new Intent(this,GameActivity.class);
			Editable string = name.getText();
			i.putExtra("name", string!=null?string:"");
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			break;
		}
	}
}
