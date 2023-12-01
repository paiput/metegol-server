package com.eblp.metegolserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eblp.metegol.screens.MatchScreen;
import com.eblp.metegol.utils.MyRenderer;

public class MetegolServer extends Game {
	
	@Override
	public void create () {
		MyRenderer.batch = new SpriteBatch();
		this.setScreen(new MatchScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void dispose () {
		MyRenderer.batch.dispose();
	}
}
