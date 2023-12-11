package com.eblp.metegol.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.eblp.metegol.utils.Config;
import com.eblp.metegol.utils.Constants;
import com.eblp.metegol.utils.MyImage;
import com.eblp.metegol.utils.MyText;
import com.eblp.metegolserver.network.ServerThread;

import enums.StickType;
import enums.TeamType;
import gameplay.Data;

public class Team {
	private String name;
	private TeamType teamType;
	private MyText scoreText;
	private int score = 0;

	private MyImage image;

	// Grupos de jugadores por posición
	private PlayersStick gkStick;
	private PlayersStick defStick;
	private PlayersStick midStick;
	private PlayersStick fwdStick;

	public Team(String name, String texturePath, TeamType teamType) {
		this.name = name;
		this.teamType = teamType;

		image = new MyImage(texturePath);
		image.setSize(64, 64);
		image.setPosition(
				teamType == TeamType.HOME ? Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() * 0.75f / 2
						: Gdx.graphics.getWidth() / 2 + Gdx.graphics.getWidth() * 0.75f / 2 - image.getWidth(),
				Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() * 0.75f / 2);

		scoreText = new MyText(Integer.toString(score), Config.FONT, 64, Color.WHITE);
		scoreText.setPosition(
				teamType == TeamType.HOME
						? Config.SCREEN_W / 2 - Config.SCREEN_W * 0.75f / 2 + image.getWidth()
						: Config.SCREEN_W / 2 + Config.SCREEN_W * 0.75f / 2 - scoreText.getWidth()
								- image.getWidth(),
				Config.SCREEN_H / 2 + Config.SCREEN_H * 0.75f / 2 + scoreText.getHeight() + 15);

	}

	public void setLineUp(float pitchW, float pitchH) {
		float y = Config.SCREEN_H / 2 - pitchH / 2;
		float xi = Config.SCREEN_W / 2 - pitchW / 2; // toma como referencia inicial el borde izquierdo de la cancha

		if (teamType == TeamType.HOME) {
			gkStick = new PlayersStick(TeamType.HOME, StickType.GK, 1, xi + pitchW * 0.05f, y, 4, pitchH);
			defStick = new PlayersStick(TeamType.HOME, StickType.DEF, 3, xi + pitchW * 0.15f, y, 4, pitchH);
			midStick = new PlayersStick(TeamType.HOME, StickType.MID, 4, xi + pitchW * 0.4f, y, 4, pitchH);
			fwdStick = new PlayersStick(TeamType.HOME, StickType.FWD, 3, xi + pitchW * 0.7f, y, 4, pitchH);
		} else {
			// Invierte la posición de los jugadores
			gkStick = new PlayersStick(TeamType.VISITOR, StickType.GK, 1, xi + pitchW * 0.95f, y, 4, pitchH);
			defStick = new PlayersStick(TeamType.VISITOR, StickType.DEF, 3, xi + pitchW * 0.85f, y, 4, pitchH);
			midStick = new PlayersStick(TeamType.VISITOR, StickType.MID, 4, xi + pitchW * 0.6f, y, 4, pitchH);
			fwdStick = new PlayersStick(TeamType.VISITOR, StickType.FWD, 3, xi + pitchW * 0.3f, y, 4, pitchH);
		}
	}

	public void update() {
		gkStick.update();
		defStick.update();
		midStick.update();
		fwdStick.update();
	}

	// Detecta contacto entre pelota y jugador de un palo
	public void detectCollision(Ball ball) {
		float ballX = ball.getX();

		if ((ballX >= gkStick.getX() - Constants.HITBOX) && (ballX <= gkStick.getX() + Constants.HITBOX)) 
			handleStickKick(gkStick, ball);

		else if ((ballX >= defStick.getX() - Constants.HITBOX) && (ballX <= defStick.getX() + Constants.HITBOX)) 
			handleStickKick(defStick, ball);

		else if ((ballX >= midStick.getX() - Constants.HITBOX) && (ballX <= midStick.getX() + Constants.HITBOX)) 
			handleStickKick(midStick, ball);

		else if ((ballX >= fwdStick.getX() - Constants.HITBOX) && (ballX <= fwdStick.getX() + Constants.HITBOX)) 
			handleStickKick(fwdStick, ball);

		else {
			stickStand(gkStick);
			stickStand(defStick);
			stickStand(midStick);
			stickStand(fwdStick);
		}
	}

	// Avisa al servidor cuando patea algún palo
	public void handleStickKick(PlayersStick stick, Ball ball) {
		
		float ballY = ball.getY();

		for (Player p : stick.getPlayers()) {
			if (ballY >= p.getY() - Constants.HITBOX && ballY <= p.getY() + Constants.HITBOX) {
				stickKick(stick);
				// Envia mensaje de patear
				ServerThread.sendMessageAll("kick_" + (teamType == TeamType.HOME ? "P1" : "P2") + "_" + stick.getStickType().getName());
				// Si no es un delantero
				if (stick.getStickType() != StickType.FWD) {
					float dir = (float) Math.round(Math.random()); // Dirije la pelota en diagonal hacia arriba o
																	// hacia abajo aleatoriamente
					// ball.applyImpulse(teamType == TeamType.HOME ? 3 : -3, dir == 0 ? 3 : -3);
					ball.applyImpulse(teamType == TeamType.HOME ? 1 : -1, dir == 0 ? 1 : -1);// copia para ir mas
																								// despacio
				} else {
					// Si es un delantero la pelota se apunta al arco
					ball.goToGoal(teamType == TeamType.HOME ? TeamType.VISITOR : TeamType.HOME);
				}
			} else {
				stickStand(stick);
			}
		}

	}

	public void stickKick(PlayersStick stick) {
		for (Player p : stick.getPlayers()) {
			p.kick();
		}
	}

	public void stickStand(PlayersStick stick) {
		for (Player p : stick.getPlayers()) {
			p.stand();
		}
	}

	public void scoreGoal() {
		System.out.println("Goool de " + name);
		if (teamType == TeamType.HOME) Data.score1 += 1;
		else if (teamType == TeamType.VISITOR) Data.score2 += 1;
		scoreText.setText(Integer.toString(teamType == TeamType.HOME ? Data.score1 : Data.score2));
	}

	public String getName() {
		return name;
	}

	public void drawLogo() {
		image.draw();
	}

	public void drawScore() {
		scoreText.draw();
	}

	public void drawPlayers() {
		gkStick.draw();
		defStick.draw();
		midStick.draw();
		fwdStick.draw();
	}

	public void dispose() {
		gkStick.dispose();
		defStick.dispose();
		midStick.dispose();
		fwdStick.dispose();
		image.dispose();
	}
}
