package com.eblp.metegol.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.eblp.metegol.utils.Constants;
import com.eblp.metegol.utils.MyImage;
import com.eblp.metegol.utils.MyText;
import com.eblp.metegol.utils.Config;

import enums.StickType;
import enums.TeamType;

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
		image.setPosition(teamType == TeamType.HOME ? Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()*0.75f/2 : Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()*0.75f/2 - image.getWidth(), Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()*0.75f/2);
		
		scoreText = new MyText(Integer.toString(score), Config.FONT, 64, Color.WHITE);
		scoreText.setPosition(teamType == TeamType.HOME ? Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()*0.75f/2 + image.getWidth(): Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()*0.75f/2 - scoreText.getWidth() - image.getWidth(), Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()*0.75f/2 + scoreText.getHeight() + 15);
	
	}
	
	public void setLineUp(float pitchW, float pitchH) {
		float y = Config.SCREEN_H/2 - pitchH/2;
		float xi = Config.SCREEN_W/2 - pitchW/2; // toma como referencia inicial el borde izquierdo de la cancha
				
		if (teamType == TeamType.HOME) {
			gkStick = new PlayersStick(TeamType.HOME, StickType.GK, 1, xi + pitchW*0.05f, y, 4, pitchH);
			defStick = new PlayersStick(TeamType.HOME, StickType.DEF, 3, xi + pitchW*0.15f, y, 4, pitchH);
			midStick = new PlayersStick(TeamType.HOME, StickType.MID, 4, xi + pitchW*0.4f, y, 4, pitchH);
			fwdStick = new PlayersStick(TeamType.HOME, StickType.FWD, 3, xi + pitchW*0.7f, y, 4, pitchH);
		} else {			
			// Invierte la posición de los jugadores
			gkStick = new PlayersStick(TeamType.VISITOR, StickType.GK, 1, xi + pitchW*0.95f, y, 4, pitchH);
			defStick = new PlayersStick(TeamType.VISITOR, StickType.DEF, 3, xi + pitchW*0.85f, y, 4, pitchH);
			midStick = new PlayersStick(TeamType.VISITOR, StickType.MID, 4, xi + pitchW*0.6f, y, 4, pitchH);
			fwdStick = new PlayersStick(TeamType.VISITOR, StickType.FWD, 3, xi + pitchW*0.3f, y, 4, pitchH);
		}
	}
	
	public void init() {
		gkStick.init();
		defStick.init();
		midStick.init();
		fwdStick.init();
	}
	
	public void detectCollision(Ball ball) {		
		int i = 0;
		for (Player p : getAllPlayers()) {
        	boolean contactX = (ball.getX() >= p.getX() - Constants.HITBOX) && (ball.getX() <= p.getX() + Constants.HITBOX);
        	boolean contactY = (ball.getY() >= p.getY() - Constants.HITBOX) && (ball.getY() <= p.getY() + Constants.HITBOX);
        	if (contactX && contactY) {
        		p.kick(); // Animacion de patear
        		System.out.println("Patea");
//        		kickSound.play();
        		if (p.getType() != StickType.FWD) {
        			// Dirije la pelota en diagonal hacia arriba o hacia abajo aleatoriamente
        			float dir = (float)Math.round(Math.random());
        			// copia para ir mas despacio
        			//ball.applyImpulse(teamType == TeamType.HOME ? 3 : -3, dir == 0 ? 3 : -3);
        			ball.applyImpulse(teamType == TeamType.HOME ? 1 : -1, dir == 0 ? 1 : -1);
        		} else {
        			ball.goToGoal(teamType == TeamType.HOME ? TeamType.VISITOR : TeamType.HOME);
        		}
        	}
        }
	}
	
	public void scoreGoal() {
		System.out.println("Goool de " + name);
		score += 1;
		scoreText.setText(Integer.toString(score));
	}
	
	public int getScore() {
		return score;
	}
	
	public String getName() {
		return name;
	}
	
	public Player[] getAllPlayers() {
		Player[] players = new Player[11];
		System.arraycopy(gkStick.getPlayers(), 0, players, 0, 1);
		System.arraycopy(defStick.getPlayers(), 0, players, 1, 3);
		System.arraycopy(midStick.getPlayers(), 0, players, 4, 4);
		System.arraycopy(fwdStick.getPlayers(), 0, players, 8, 3);
		return players;
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
