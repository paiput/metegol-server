package com.eblp.metegolserver.network;

import java.net.InetAddress;

public class NetworkAddress {
	
	private InetAddress ip;
	private int port;

	public NetworkAddress(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public InetAddress getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}

}
