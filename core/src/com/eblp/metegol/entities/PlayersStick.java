package com.eblp.metegol.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.eblp.metegol.utils.Config;
import com.eblp.metegol.utils.MyRenderer;
import com.eblp.metegolserver.network.ServerThread;

import enums.StickType;
import enums.TeamType;
import gameplay.Data;

public class PlayersStick {
	private final int REGION_HEIGHT = 64;
	private final int REGION_WIDTH = 16;
	private Player[] players;
	private Texture texture;
	private StickType type;
	private TeamType team;
	private Sprite sprite;
	private float h;
	
	public boolean isUp = false, isDown = false;
	
	// velocidad de movimiento de los jugadores
	private final float vel = 5;

	public PlayersStick(TeamType teamType, StickType st, int playersCount, float x, float y, float w, float h) {
		this.h = h;
		this.type = st;
		this.team = teamType;
		texture = new Texture("stick-spritesheet.png");
		sprite = new Sprite(texture, 0, 0, REGION_WIDTH, REGION_HEIGHT);
		
		players = new Player[playersCount];
		int playerSize = 32;
		for (int i=0; i<playersCount; i++) {
			players[i] = new Player(teamType, st, x-playerSize/2+w/2, y+i*playerSize+i*h*0.15f, playerSize, playerSize);
		}
		sprite.setPosition(x, y);
		sprite.setSize(w, h);
	}
	
	public void init() {
		
		float bottom = Config.SCREEN_H/2 - h/2;
		float top = Config.SCREEN_H/2 + h/2;
		
		int pIndex = 0;
		for (int i = 0; i < players.length; i++) {
			Player p = players[i];
			
			// Movimiento vertical del palo
			if ((team == TeamType.HOME && Data.isDown1) || (team == TeamType.VISITOR && Data.isDown2)) {
				// Bloquea los jugadores al llegar al borde de abajo
				if (p.getY() > bottom + p.getH()*pIndex + (pIndex*h*0.15f)) {
					p.moveY(-vel);
					String positions = "";
					for (int j=0; j<players.length; j++) {
						if (j==0) positions += (String.valueOf(players[j].getY()));
						else positions += "," + (String.valueOf(players[j].getY()));
					}
					ServerThread.sendMessageAll("update_" + (team == TeamType.HOME ? "P1" : "P2") + "_" + type.getName() + "_" + positions); 
				}
			} else if ((team == TeamType.HOME && Data.isUp1) || (team == TeamType.VISITOR && Data.isUp2)) {
				// Bloquea los jugadores al llegar al borde de arriba
				if (p.getY() < top - (p.getH()*(players.length-pIndex) + (players.length-pIndex-1)*h*0.15f)) {
					p.moveY(vel);
				}
				String positions = "";
				for (int j=0; j<players.length; j++) {
					if (j==0) positions += (String.valueOf(players[j].getY()));
					else positions += "," + (String.valueOf(players[j].getY()));
				}
				ServerThread.sendMessageAll("update_" + (team == TeamType.HOME ? "P1" : "P2") + "_" + type.getName() + "_" + positions); 
			} 
			
			p.stand();
			
			// Datos para pasar al cliente
			updateGampeplayData(p, i);
			
			pIndex++;
		}
		
		// Caso especial para el arquero
		if (players.length == 1) {
			Player gk = players[0];
			if (gk.getY() > bottom + h*0.6f - gk.getH()/2) {
				gk.setY(bottom + h*0.6f - gk.getH()/2);				
			} else if (gk.getY() < bottom + h*0.4f - gk.getH()/2) {
				gk.setY(bottom + h*0.4f - gk.getH()/2);
			}
		}		
	}
	
	private void updateGampeplayData(Player p, int index) {
		if (team == TeamType.HOME) {
			if (type == StickType.GK) Data.yGk1[index] = p.getY();
			else if (type == StickType.GK) Data.yDef1[index] = p.getY();
			else if (type == StickType.GK) Data.yMid1[index] = p.getY();
			else if (type == StickType.GK) Data.yFwd1[index] = p.getY();
		} else {
			if (type == StickType.GK) Data.yGk2[index] = p.getY();
			else if (type == StickType.GK) Data.yDef2[index] = p.getY();
			else if (type == StickType.GK) Data.yMid2[index] = p.getY();
			else if (type == StickType.GK) Data.yFwd2[index] = p.getY();			
		}
	}
	
	public Player[] getPlayers() {
		return players;
	}
	
	public StickType getStickType() {
		return type;
	}
	
	public void draw() {
		sprite.draw(MyRenderer.batch);
		for (Player player : players) {
			player.draw();
		}
	}
	
	public void dispose() {
		texture.dispose();
		for (Player player : players) {
			player.dispose();
		}
	}

}
