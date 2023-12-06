package com.eblp.metegol.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.eblp.metegol.utils.Config;
import com.eblp.metegol.utils.MyRenderer;
import com.eblp.metegolserver.network.ServerThread;

import enums.TeamType;
import gameplay.Data;

public class Ball {
	private final int REGION_HEIGHT = 32;
	private final int REGION_WIDTH = 32;
	private int goalSide = 0;
	private Texture texture;
	private Sprite sprite;
	private int w, h;

	// og x = 2  /  y = 0
	private float dirX = 0.5f, dirY = 0.5f;

	private final float goalTop = Config.SCREEN_H / 2 + 60;
	private final float goalBottom = Config.SCREEN_H / 2 - 60;
	private final float padding = 35;

	public Ball(float x, float y, int w, int h) {
		texture = new Texture("ball.png");
		sprite = new Sprite(texture, 0, 0, REGION_WIDTH, REGION_HEIGHT);
		this.w = w;
		this.h = h;
		goalSide = 0;
		sprite.setPosition(x, y);
		sprite.setSize(w, h);
		Data.xBall = sprite.getX();
		Data.yBall = sprite.getY();
	}

	public void handleCollisions() {
		boolean leftBorder = sprite.getX() <= Config.SCREEN_W / 2 - Config.SCREEN_W * 0.75f / 2 + padding;
		boolean rightBorder = sprite.getX() >= Config.SCREEN_W / 2 + Config.SCREEN_W * 0.75f / 2 - w - padding;
		boolean topBorder = sprite.getY() >= Config.SCREEN_H / 2 + Config.SCREEN_H * 0.75f / 2 - h - padding;
		boolean bottomBorder = sprite.getY() <= Config.SCREEN_H / 2 - Config.SCREEN_H * 0.75f / 2 + padding;

		// Rebota al colisionar con los bordes en x
		if (rightBorder || leftBorder)
			dirX *= -1;

		// Rebota el colisionar con los bordes en y
		if (topBorder || bottomBorder)
			dirY *= -1;

		sprite.setX(sprite.getX() + dirX);
		sprite.setY(sprite.getY() + dirY);
		
		Data.xBall = sprite.getX();
		Data.yBall = sprite.getY();
		ServerThread.sendMessageAll("ball_position_" + String.valueOf(Data.xBall) + "," + String.valueOf(Data.yBall));
	}

	public float getDistanceFromGoalX(TeamType team) {
		float pitch = Config.SCREEN_W * 0.75f / 2;
		float border = team == TeamType.VISITOR ? pitch : -pitch;
		return Config.SCREEN_W / 2 + border - sprite.getX();
	}

	public float getDistanceFromGoalY() {
		return sprite.getY() - Config.SCREEN_H / 2;
	}

	public void goToGoal(TeamType team) {
//		applyImpulse(getDistanceFromGoalX(team) / 60, -getDistanceFromGoalY() / 60);
		applyImpulse(getDistanceFromGoalX(team) / 90, -getDistanceFromGoalY() / 90);
	}

	public boolean isGoal() {
		boolean leftBorder = sprite.getX() <= Config.SCREEN_W / 2 - Config.SCREEN_W * 0.75f / 2 + padding;
		boolean rightBorder = sprite.getX() >= Config.SCREEN_W / 2 + Config.SCREEN_W * 0.75f / 2 - w - padding;

		goalSide = 0;

		// Pasa por el arco
		if ((leftBorder || rightBorder) && (sprite.getY() < goalTop && sprite.getY() > goalBottom)) {
			if (leftBorder) {
				goalSide = -1;
				ServerThread.sendMessageAll("goal_left");
			} else if (rightBorder) {
				ServerThread.sendMessageAll("goal_right");
				goalSide = 1;
			}
		}

		return goalSide != 0;
	}

	public int getGoalSide() {
		return goalSide;
	}

	public void placeOnCenter() {
		sprite.setPosition(Config.SCREEN_W / 2 - 8, Config.SCREEN_H / 2 - 8);
	}

	public void applyImpulse(float x, float y) {
		dirX = x;
		dirY = y;
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public void draw() {
		sprite.draw(MyRenderer.batch);
	}
	
	public void dispose() {
		texture.dispose();
	}

}
