package com.eblp.metegol.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MyFont {
	
	private BitmapFont font;

	public MyFont(String path, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		parameter.color = Color.DARK_GRAY;
		font = generator.generateFont(parameter);
	}
	
	public BitmapFont getFont() {
		return font;
	}

	public void dispose() {
		font.dispose();
	}
	
}
