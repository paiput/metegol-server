package com.eblp.metegol.entities;

import com.badlogic.gdx.graphics.Texture;
import com.eblp.metegol.utils.MyImage;
import com.eblp.metegol.utils.MyRenderer;

public class Pitch {
	
	private float w, h;
	
	public MyImage image;

	public Pitch(float w, float h) {
		this.w = w;
		this.h = h;
		image = new MyImage("pitch-2.jpg");
		image.setSize(w, h);
		image.setPosition(0, 0);
	}
	
	public void draw() {
		image.draw();
	}
	
	public float getWidth() {
		return w;
	}
	
	public float getHeight() {
		return h;
	}
	
	public void dispose() {
		image.dispose();
	}

}
