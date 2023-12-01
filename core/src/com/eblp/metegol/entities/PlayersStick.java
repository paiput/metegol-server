package com.eblp.metegol.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.eblp.metegol.utils.MyRenderer;
import com.eblp.metegol.utils.Config;

import enums.StickType;
import enums.TeamType;

public class PlayersStick {
	private final int REGION_WIDTH = 16;
	private final int REGION_HEIGHT = 64;
	private int keyUp, keyDown;
	private Player[] players;
	private Texture texture;
	private StickType type;
	private Sprite sprite;
	private float x, y;
	private float w, h;

	public PlayersStick(TeamType teamType, StickType st, int playersCount, float x, float y, float w, float h, int keyUp, int keyDown) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.keyUp = keyUp;
		this.keyDown = keyDown;
		this.type = st;
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
		for (Player p : players) {
			float vel = 5;
			
			// Movimiento vertical del palo
			if (Gdx.input.isKeyPressed(keyDown)) {
				// Bloquea los jugadores al llegar al borde de abajo
				if (p.getY() > bottom + p.getH()*pIndex + (pIndex*h*0.15f)) {
					p.moveY(-vel);
				}
			} else if (Gdx.input.isKeyPressed(keyUp)) {
				// Bloquea los jugadores al llegar al borde de arriba
				if (p.getY() < top - (p.getH()*(players.length-pIndex) + (players.length-pIndex-1)*h*0.15f)) {
					p.moveY(vel);
				}
			} 
			
			// Cambia la region cuando patea 
			/*if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				sprite.setRegion(REGION_WIDTH, 0, REGION_WIDTH, REGION_HEIGHT);
				p.kick();
			} else {
				sprite.setRegion(0, 0, REGION_WIDTH, REGION_HEIGHT);
				p.stand();
			}*/
			p.stand();
			
			
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
