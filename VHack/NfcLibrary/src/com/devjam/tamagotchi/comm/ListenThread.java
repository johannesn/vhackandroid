package com.devjam.tamagotchi.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.devjam.tamagotchi.game.Monster;

public class ListenThread extends Thread {

	private AbstractNfcActivity activity;
	private int action;
	private Monster monster;
	private int port;

	public ListenThread(AbstractNfcActivity activity, int action,
			Monster monster, int port) {
		super();
		this.activity = activity;
		this.action = action;
		this.monster = monster;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			ServerSocket sock = new ServerSocket(port);
			Socket conn = sock.accept();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			// Write desired Action to other device
			out.writeInt(action);
			if (AbstractNfcActivity.ACTION_PAIR == action) {
				// Send Monster to other device
				MonsterFactory.writeMonster(out, monster);
				DataInputStream in = new DataInputStream(conn.getInputStream());
				boolean pair_accepted = in.readBoolean();
				// final Monster in_monster =
				if (pair_accepted) {
					MonsterFactory.readMonster(in);
					activity.runOnUiThread(new Runnable() {
						public void run() {
							// activity.pairSuccessful(in_monster);
							activity.setWriteMode(false);
							activity.actionSuccessful();
						}
					});
				} else {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							// activity.pairSuccessful(in_monster);
							activity.setWriteMode(false);
							activity.actionRefused();
						}
					});
				}
				out.close();
				in.close();
			}
			conn.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
