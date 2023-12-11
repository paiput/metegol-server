package com.eblp.metegolserver.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import gameplay.Data;

public class ServerThread extends Thread {

	private static DatagramSocket connection;
	private boolean end = false;
	private static NetworkAddress[] clients = new NetworkAddress[2];
	private static int amountClients = 0;
	
	private static final int PORT = 3030;

	public ServerThread() {
		try {
			connection = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	// Reinicia el array clientes
	public static void restartClients() {
		clients = new NetworkAddress[2];
		amountClients = 0;
	}

	// Envia mensaje a un cliente especifico
	private static void sendMessage(String msg, InetAddress ip, int port) {
		byte[] data = msg.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length, ip, port);
		try {
			connection.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Envia mensaje a todos los clientes
	public static void sendMessageAll(String msg) {
		for (int i = 0; i < clients.length; i++) {
			sendMessage(msg, clients[i].getIp(), clients[i].getPort());
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
		} while (!end);
	}

	// Procesa el mensaje enviado por los clientes para iniciar la partida
	private void processMessage(DatagramPacket dp) {
		String msg = new String(dp.getData()).trim();
		System.out.println("Mensaje servidor: " + msg);
		System.out.println("  - Clients: " + clients.length);

		int clientId = -1;
		// Asigna el índice del cliente que envió el mensaje que está siendo procesado a clientId
		if (amountClients > 1) {
			for (int i = 0; i < amountClients; i++) {
				if (dp.getPort() == clients[i].getPort() && dp.getAddress().equals(clients[i].getIp())) {
					clientId = i;
				}
			}
		}

		if (amountClients < 2 || msg.equals("Conexion")) {
			if (amountClients < 2) {
				clients[amountClients] = new NetworkAddress(dp.getAddress(), dp.getPort());
				sendMessage("OK", clients[amountClients].getIp(), clients[amountClients].getPort());
				amountClients++;
			}
			// Habilita el juego para empezar si hay dos jugadores conectados
			if (amountClients == 2) {
				System.out.println("Server detecta 2 jugadores: EMPEZAR");
				Data.runGame = true;
				// Avisa a los clientes que empieza el partido
				for (int i = 0; i < clients.length; i++) {
					sendMessage("Empieza", clients[i].getIp(), clients[i].getPort());
				}
			}

		} else {
			// Lee I/O solo para 2 clientes (0 y 1) para evitar mensajes de un tercer cliente que se conecte
			if (clientId <= 1) {
				// Detección de I/O
				if (msg.equals("key_up")) {
					if (clientId == 0) {
						Data.isUp1 = true;
					} else {
						Data.isUp2 = true;
					}
				} else if (msg.equals("key_down")) {
					if (clientId == 0) {
						Data.isDown1 = true;
					} else {
						Data.isDown2 = true;
					}
				} else if (msg.equals("stop_key_up")) {
					if (clientId == 0) {
						Data.isUp1 = false;
					} else {
						Data.isUp2 = false;
					}
				} else if (msg.equals("stop_key_down")) {
					if (clientId == 0) {
						Data.isDown1 = false;
					} else {
						Data.isDown2 = false;
					}
				}
			}

		}

	}

}
