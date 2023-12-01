package com.eblp.metegol.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyImage {
	
	private Texture t;
	private Sprite s;

	public MyImage(String route) {
		t = new Texture(route);
		s = new Sprite(t);
	}
	
	public void draw() {
		s.draw(MyRenderer.batch);
	}
	
	public float getWidth() {
		return s.getWidth();
	}
	
	public float getHeight() {
		return s.getHeight();
	}
	
	public void setSize(float w, float h) {
		s.setSize(w, h);
	}
	
	public void setPosition(float x, float y) {
		s.setPosition(x, y);
	}
	
	public void setOpacity(float a) {
		s.setAlpha(a);
	}
	
	public void rotate(float deg) {
		s.rotate(deg);
	}
	
	public void dispose() {
		t.dispose();
	}

}
