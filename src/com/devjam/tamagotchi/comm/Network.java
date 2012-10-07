package com.devjam.tamagotchi.comm;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Network {

	public static String getLocalIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
