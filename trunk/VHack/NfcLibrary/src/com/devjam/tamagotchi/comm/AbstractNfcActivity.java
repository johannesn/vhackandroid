package com.devjam.tamagotchi.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

@SuppressWarnings("deprecation")
public abstract class AbstractNfcActivity extends Activity {
	NfcAdapter nfcAdapter;
	private IntentFilter[] intentFiltersArray;
	private PendingIntent pendingIntent;
	private String[][] techListsArray;
	private boolean writeMode = false;

	private static final int port = 12345;

	private int msg = 1337;
	private boolean paused = true;

	abstract protected void dataArrived(int msg);

	private void startListening() {
		new Thread(new Runnable() {
			public void run() {
				try {
					ServerSocket sock = new ServerSocket(port);
					Socket conn = sock.accept();
					DataInputStream in = new DataInputStream(
							conn.getInputStream());
					final int msg = in.readInt();
					runOnUiThread(new Runnable() {
						public void run() {
							dataArrived(msg);
							setWriteMode(false);
						}
					});
					in.close();
					conn.close();
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void startWriting(final String ip) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Log.d("IpAddress", ip);
					Socket sock = new Socket(ip, port);
					DataOutputStream out = new DataOutputStream(
							sock.getOutputStream());
					out.writeInt(msg);
					out.close();
					sock.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setup();
	}

	private void setup() {
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
			e.printStackTrace();
		}
		intentFiltersArray = new IntentFilter[] { ndef };
		techListsArray = new String[][] { new String[] { Ndef.class.getName() } };
	}

	public void onPause() {
		super.onPause();
		nfcAdapter.disableForegroundNdefPush(this);
		nfcAdapter.disableForegroundDispatch(this);

		paused = true;
	}

	public void onResume() {
		super.onResume();
		setup();
		if (writeMode) {
			nfcAdapter.enableForegroundNdefPush(this,
					new NdefMessage(
							new NdefRecord[] { new NdefRecord(
									NdefRecord.TNF_WELL_KNOWN,
									NdefRecord.RTD_TEXT, "Action".getBytes(),
									getLocalIpAddress().getBytes()) }));
			startListening();
		} else {
			nfcAdapter.enableForegroundDispatch(this, pendingIntent,
					intentFiltersArray, techListsArray);
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

	public int getMsg() {
		return msg;
	}

	public void setMsg(int msg) {
		this.msg = msg;
	}

	public boolean isWriteMode() {
		return writeMode;
	}

	public void setWriteMode(boolean writeMode) {
		this.writeMode = writeMode;
		if (writeMode) {
			if (!paused) {
				nfcAdapter.disableForegroundDispatch(this);
				nfcAdapter.enableForegroundNdefPush(this, new NdefMessage(
						new NdefRecord[] { new NdefRecord(
								NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
								"Action".getBytes(), getLocalIpAddress()
										.getBytes()) }));
				startListening();
			}
		} else {
			if (!paused) {
				nfcAdapter.enableForegroundDispatch(this, pendingIntent,
						intentFiltersArray, techListsArray);
				nfcAdapter.disableForegroundNdefPush(this);
			}
		}
	}

	private String getLocalIpAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = "";
		for (int i = 0; i < 4; i++) {
			int b = ipAddress & 0x000000FF;
			ipAddress = ipAddress >> 8;
			ip = ip + b + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}
}
