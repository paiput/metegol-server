package com.eblp.metegolserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.eblp.metegol.utils.Global;

public class ServerThread extends Thread {
	
	private DatagramSocket connection;
	private boolean end = false;
	private NetworkAddress[] clients = new NetworkAddress[2];
	private int amountClients = 0;
	
	public ServerThread() {
		try {
			connection = new DatagramSocket(3030);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String msg, InetAddress ip, int port) {
		byte[] data = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length, ip, port);
		try {
			connection.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				connection.receive(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			processMessage(dp);
		} while(!end);
	}

	// Procesa el mensaje enviado por los clientes para iniciar la partida
	private void processMessage(DatagramPacket dp) {
		String msg = new String(dp.getData()).trim();
		System.out.println("Mensaje servidor: " + msg);
		if (msg.equals("Conexion")) {
			if (amountClients < 2) {
				clients[amountClients] = new NetworkAddress(dp.getAddress(), dp.getPort());
				sendMessage("OK", clients[amountClients].getIp(), clients[amountClients].getPort());
				amountClients++;
				if (amountClients == 2) {
					System.out.println("Server detecta 2 jugadores: EMPEZAR");
					// Habilita el juego para empezar
					Global.start = true;
					for (int i=0; i<clients.length; i++) {
						sendMessage("Empieza", clients[i].getIp(), clients[i].getPort());
					}
				}
			}
		}
	}

}
