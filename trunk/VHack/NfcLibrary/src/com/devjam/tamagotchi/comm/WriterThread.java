package com.devjam.tamagotchi.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.devjam.tamagotchi.game.Monster;

public class WriterThread extends Thread {

	private AbstractNfcActivity activity;
	private int port;
	private String ip;

	public WriterThread(AbstractNfcActivity activity, String ip, int port) {
		super();
		this.activity = activity;
		this.port = port;
		this.ip = ip;
	}

	@Override
	public void run() {
		try {
			Socket sock = new Socket(ip, port);
			DataInputStream in = new DataInputStream(sock.getInputStream());
			// Read Action Code
			int action_code = in.readInt();
			if (action_code == AbstractNfcActivity.ACTION_PAIR) {
				Monster monster = MonsterFactory.readMonster(in);
				DataOutputStream out = new DataOutputStream(
						sock.getOutputStream());
				final Monster out_monster = activity.pairWithMonster(monster);
				MonsterFactory.writeMonster(out, out_monster);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						activity.pairSuccessful(out_monster);
						activity.actionSuccessful();
					}
				});
				out.close();
			}
			in.close();
			sock.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
