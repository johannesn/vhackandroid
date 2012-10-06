package com.example.nfctest;

import org.vhack.vhacksvntest.AbstractNfcActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ReceiverActivity extends AbstractNfcActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void dataArrived(int msg) {
		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText("Message arrived: " + msg);
	}
}
