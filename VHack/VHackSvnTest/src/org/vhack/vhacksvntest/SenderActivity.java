package org.vhack.vhacksvntest;

import android.os.Bundle;
import android.widget.TextView;

public class SenderActivity extends AbstractNfcActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onResume() {
		super.onResume();
		setMsg(14556);
		setWriteMode(true);
	}

	@Override
	protected void dataArrived(int msg) {
		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText("Message arrived: " + msg);
	}
}
