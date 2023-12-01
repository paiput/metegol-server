package com.eblp.metegol.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.eblp.metegol.entities.Ball;
import com.eblp.metegol.entities.Team;
import com.eblp.metegol.utils.Config;
import com.eblp.metegol.utils.Global;
import com.eblp.metegol.utils.MyImage;
import com.eblp.metegol.utils.MyRenderer;
import com.eblp.metegol.utils.MyText;
import com.eblp.metegolserver.MetegolServer;
import com.eblp.metegolserver.network.ServerThread;

import enums.TeamType;

public class MatchScreen implements Screen {
	private MetegolServer game;
	private FitViewport viewport;
	private MyImage pitch;
	private MyImage hGoal, vGoal; // Arcos
	private MyText goalAlert, matchEndAlert, waiting;
	private ServerThread st;
	
	private boolean matchEnded = false;
	
	private Team hTeam, vTeam;
	private Ball ball;
	
	private float goalAlertOpacity = 0;
	private boolean showGoalAlert = false;

	public MatchScreen(MetegolServer game) {
		this.game = game;
		
		viewport = new FitViewport(Config.SCREEN_W, Config.SCREEN_H);
		
		st = new ServerThread();
		System.out.println("Width: " + Config.SCREEN_W + " Height: " + Config.SCREEN_H);
	}
	
	@Override
	public void show() {		
		float vw = Gdx.graphics.getWidth();
		float vh = Gdx.graphics.getHeight();
		
		// Aplica el viewport
		viewport.apply();
		
		pitch = new MyImage("pitch-2.jpg");
		pitch.setSize(Config.SCREEN_W * 0.75f, Config.SCREEN_H * 0.75f);
		pitch.setPosition(vw/2 - pitch.getWidth()/2, Config.SCREEN_H/2 - pitch.getHeight()/2);
		
		// Arco local
		hGoal = new MyImage("arco-inverso.png");
		hGoal.setSize(32, 160);
		hGoal.setPosition(vw/2 - pitch.getWidth()/2 + 4, vh/2 - hGoal.getHeight()/2);
		
		// Arco visitante
		vGoal = new MyImage("arco.png");
		vGoal.setSize(32, 160);
		vGoal.setPosition(vw/2 + pitch.getWidth()/2 - vGoal.getWidth() - 4, vh/2 - hGoal.getHeight()/2);
		
		ball = new Ball(Config.SCREEN_W/2-8, Config.SCREEN_H/2-8, 16, 16);
		
		// Equipo local
		hTeam = new Team("Velez", "escudo-velez-pixel.png", TeamType.HOME);
		hTeam.setLineUp(pitch.getWidth(), pitch.getHeight());
		
		// Equipo visitante
		vTeam = new Team("Independiente", "escudo-independiente-pixel.png", TeamType.VISITOR);
		vTeam.setLineUp(pitch.getWidth(), pitch.getHeight());	
		
		goalAlert = new MyText("Gooool", Config.FONT, 128, Color.YELLOW);
		goalAlert.setPosition(vw/2-goalAlert.getWidth()/2, vh/2+goalAlert.getHeight()/2);
		
		matchEndAlert = new MyText("Fin del partido", Config.FONT, 128, Color.YELLOW);
		matchEndAlert.setPosition(vw/2-matchEndAlert.getWidth()/2, vh/2+matchEndAlert.getHeight()/2);
			
		waiting = new MyText("Esperando jugadores", Config.FONT, 64, Color.WHITE);
		waiting.setPosition(vw/2-waiting.getWidth()/2, vh/2-waiting.getHeight()/2);
		
		// Se inicia el hilo del cliente
		st.start();
	}

	@Override
	public void render(float delta) {
		// Limpia la pantalla
		MyRenderer.cleanScreen(0, 0, 0);
		
		if (!Global.start) {
			MyRenderer.batch.begin();
        	waiting.draw();
        	MyRenderer.batch.end();
        	return;
		}
		
		if (matchEnded) {
			MyRenderer.batch.begin();
        	matchEndAlert.draw();
        	MyRenderer.batch.end();
        	return;
        }
		
		// Termina el partido
        if (hTeam.getScore() == 2 || vTeam.getScore() == 2) {
        	String winner = hTeam.getScore() == 2 ? hTeam.getName() : vTeam.getName();
        	matchEnded = true;
        	return;
        }
        
        MyRenderer.batch.begin();
		
		// Renderiza entidades
		pitch.draw();
		ball.draw();
		hTeam.drawPlayers();
		hTeam.drawLogo();
        vTeam.drawPlayers();
        vTeam.drawLogo();
        // Arcos
        hGoal.draw();
        vGoal.draw();
        hTeam.drawScore();
        vTeam.drawScore();
        
        // Gestiona rebote de pelota en los bordes y deteccion de gol
        if (showGoalAlert && goalAlertOpacity < 1) {
        	goalAlertOpacity += 0.01f;
        	goalAlert.setOpacity(goalAlertOpacity);
        	goalAlert.draw();
        	MyRenderer.batch.end();
        	return;
        }
        
        ball.handleCollisions(); 
               	
        if (ball.isGoal()) {
        	int side = ball.getGoalSide();
        	if (side == -1) vTeam.scoreGoal();
        	else hTeam.scoreGoal();
        	ball.placeOnCenter();
        	showGoalAlert = true;
        	goalAlertOpacity = 0;
        }
        
        // Habilita movimiento de palos
        hTeam.init();
        vTeam.init();
        
        // Gestiona interseccion entre jugador y pelota
        hTeam.detectCollision(ball);
        vTeam.detectCollision(ball);
        
        MyRenderer.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		MyRenderer.batch.dispose();
		pitch.dispose();
		hTeam.dispose();
		vTeam.dispose();
		ball.dispose();
	}

}
