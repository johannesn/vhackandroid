package com.donat3llo.rockpaperscissors.comm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.util.Log;

import com.donat3llo.rockpaperscissors.model.Item;

public class TcpCommHandler implements CommunicationHandler {

	protected static final int TIMEOUT = 3000;
	private Socket mClientSocket;
	private Thread mTcpThread;

	public TcpCommHandler() {
		mTcpThread = new Thread(mTcpRunnable);
		mTcpThread.start();
	}

	public void sendItem(Item rock) {
		// TODO Auto-generated method stub

	}

	Runnable mTcpRunnable = new Runnable() {

		private DataOutputStream mOut;
		private DataInputStream mIn;

		public void run() {
			try {
				Log.d("Network", "Thread started");
				InetSocketAddress address = new InetSocketAddress(
						"192.168.2.199", 4040);
				mClientSocket = new Socket();
				mClientSocket.connect(address, TIMEOUT);

				mOut = new DataOutputStream(mClientSocket.getOutputStream());
				mOut.writeInt(1234);

				mIn = new DataInputStream(new BufferedInputStream(
						mClientSocket.getInputStream()));
				Log.d("Network", "reading");
				Double outCome = mIn.readDouble();
				Log.d("Network", "outcome = " + outCome);

				Log.d("Network", "Shutting down client");
				mIn.close();
				mOut.close();
				mClientSocket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
