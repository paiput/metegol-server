package com.eblp.metegol.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.eblp.metegol.utils.MyRenderer;

import enums.StickType;
import enums.TeamType;

public class Player {
	private final int REGION_WIDTH = 32;
	private final int REGION_HEIGHT = 32;
	private boolean isKicking;
	private Texture texture;
	private StickType type;
	private float kickingX;
	private Sprite sprite;
	private int w, h;

	public Player(TeamType teamType, StickType type, float x, float y, int w, int h) {
		texture = new Texture(teamType == TeamType.HOME ? "player-blue-spritesheet-2.png" : "player-red-spritesheet.png");
		sprite = new Sprite(texture, 0, 0, REGION_WIDTH, REGION_HEIGHT);
		this.type = type;
		this.w = w;
		this.h = h;
		sprite.setPosition(x, y);
		sprite.setSize(w, h);
		
		kickingX = x + w/2 + 6;
		isKicking = false;
	}
	
	public void kick() {
		// Cambia la textura al patear
		sprite.setRegion(REGION_WIDTH, 0, REGION_WIDTH, REGION_HEIGHT);
		isKicking = true;
	}
	
	public void stand() {
		// Cambia la textura al dejar de patear
		sprite.setRegion(0, 0, REGION_WIDTH, REGION_HEIGHT);
		isKicking = false;
	}
	
	public boolean isKicking() {
		return isKicking;
	}

	public void draw() {
		sprite.setPosition(sprite.getX(), sprite.getY());
		sprite.draw(MyRenderer.batch);
	}
	
	public float getX() {
		return sprite.getX();
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public float getH() {
		return h;
	}
	
	public StickType getType() {
		return type;
	}
	
	public void moveY(float y) {
		sprite.setY(sprite.getY() + y);
	}
	
	public void setY(float y) {
		sprite.setY(y);
	}
	
	public void dispose() {
		texture.dispose();
	}
}
