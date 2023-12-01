package com.eblp.metegol.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MyText {
	
	private BitmapFont font;
	private float x, y;
	private String text;
	private GlyphLayout layout;

	public MyText(String text, String fontPath, int size, Color color) {
		this.text = text;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = color;
		font = generator.generateFont(parameter);
		layout = new GlyphLayout();
		layout.setText(font, text);
	}
	
	public MyText(String text, String fontPath, int size, Color color, Color shadow) {
		this.text = text;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = color;
		parameter.shadowColor = shadow;
		font = generator.generateFont(parameter);
		layout = new GlyphLayout();
		layout.setText(font, text);
	}
	
	public float getY() {
		return y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getWidth() {
		return layout.width;
	}
	
	public float getHeight() {
		return layout.height;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setOpacity(float alpha) {
		Color color = font.getColor();
		font.setColor(color.r, color.g, color.b, alpha);
	}
	
	public void draw() {
		font.draw(MyRenderer.batch, text, x, y);
	}
	
	public void dispose() {
		font.dispose();
	}
}
