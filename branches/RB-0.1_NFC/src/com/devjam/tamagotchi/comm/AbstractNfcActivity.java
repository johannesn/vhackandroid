package com.devjam.tamagotchi.comm;

import com.devjam.tamagotchi.game.Monster;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;

@SuppressWarnings("deprecation")
public abstract class AbstractNfcActivity extends Activity {
	NfcAdapter nfcAdapter;
	private IntentFilter[] intentFiltersArray;
	private PendingIntent pendingIntent;
	private String[][] techListsArray;
	private boolean writeMode = false;

	public static final int ACTION_PAIR = 0;

	private static final int port = 12345;

	private boolean paused = true;
	private Monster monster;
	private int action;
	private Thread thread;

	abstract protected Monster pairWithMonster(Monster monster);

	abstract protected void pairSuccessful(Monster monster);

	abstract public void actionRefused();

	public void requestPairing(Monster monster) {
		this.monster = monster;
		this.action = ACTION_PAIR;
		setWriteMode(true);
		startListening();
	}

	private void startListening() {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
		thread = new ListenThread(this, action, monster, port);
		thread.start();
	}

	private void startWriting(final String ip) {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
		thread = new WriterThread(this, ip, port);
		thread.start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setup();
	}

	private void setup() {
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (nfcAdapter != null) {
			pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
					getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

			IntentFilter ndef = new IntentFilter(
					NfcAdapter.ACTION_NDEF_DISCOVERED);
			try {
				ndef.addDataType("text/plain");
			} catch (MalformedMimeTypeException e) {
				e.printStackTrace();
			}
			intentFiltersArray = new IntentFilter[] { ndef };
			techListsArray = new String[][] { new String[] { Ndef.class
					.getName() } };
		}
	}

	public void onPause() {
		super.onPause();
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundNdefPush(this);
			nfcAdapter.disableForegroundDispatch(this);
		}
		paused = true;
	}

	public void onResume() {
		super.onResume();
		setup();
		if (nfcAdapter != null) {
			if (writeMode) {
				nfcAdapter.enableForegroundNdefPush(
						this,
						new NdefMessage(new NdefRecord[] { new NdefRecord(
								NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
								"Action".getBytes(), Network.getLocalIpAddress(
										this).getBytes()) }));
				startListening();
			} else {
				nfcAdapter.enableForegroundDispatch(this, pendingIntent,
						intentFiltersArray, techListsArray);
			}
		}
		paused = false;
	}

	public void onNewIntent(Intent intent) {
		if (!writeMode
				&& NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Parcelable[] parcelables = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msg = (NdefMessage) parcelables[0];
			startWriting(new String(msg.getRecords()[0].getPayload()));
		}
	}

	public void setWriteMode(boolean writeMode) {
		this.writeMode = writeMode;
		if (!paused && nfcAdapter != null) {
			if (writeMode) {
				nfcAdapter.disableForegroundDispatch(this);
				nfcAdapter.enableForegroundNdefPush(
						this,
						new NdefMessage(new NdefRecord[] { new NdefRecord(
								NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
								"Action".getBytes(), Network.getLocalIpAddress(
										this).getBytes()) }));
			} else {
				nfcAdapter.enableForegroundDispatch(this, pendingIntent,
						intentFiltersArray, techListsArray);
				nfcAdapter.disableForegroundNdefPush(this);
			}
		}
	}

	public void actionSuccessful() {
		thread = null;
	}

	public boolean isNfcSupported() {
		return nfcAdapter != null;
	}

	public boolean getWriteMode() {
		return writeMode;
	}
}
