package com.eblp.metegol.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eblp.metegol.utils.MyRenderer;

public class MyTextButton {
	
	private Texture texture;
	private BitmapFont font;
	private TextButtonStyle style;
	private TextButton button;

	public MyTextButton(String txt, float x, float y) {
		generateFont();
		generateStyle();
		button = new TextButton(txt, style);
		button.setPosition(x, y);
	}
	
	public MyTextButton(String txt) {
		generateFont();
		generateStyle();
		button = new TextButton(txt, style);
	}
	
	private void generateFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/VT323-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.DARK_GRAY;
		font = generator.generateFont(parameter);
	}
	
	private void generateStyle() {
		texture = new Texture("ui-components/button-spritesheet.png");
		Drawable btnUp = new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 128, 64));
		Drawable btnDown = new TextureRegionDrawable(new TextureRegion(texture, 128, 0, 128, 64));
		
		style = new TextButtonStyle(btnUp, btnDown, btnDown, font);
		style.over = btnDown;
		style.overFontColor = Color.BLACK;
	}
	
	public void addListener(EventListener listener) {
		button.addListener(listener);
	}
	
	public float getWidth() {
		return button.getWidth();
	}
	
	public float getHeight() {
		return button.getHeight();
	}
	
	public void setWidth(float width) {
		button.setWidth(width);
	}
	
	public void setHeight(float width) {
		button.setHeight(width);
	}
	
	public TextButton getButton() {
		return button;
	}
	
	public void draw() {
		button.draw(MyRenderer.batch, 1);
	}
	
	public void dispose() {
		texture.dispose();
	}

}
