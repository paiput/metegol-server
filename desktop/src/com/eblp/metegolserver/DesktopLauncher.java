package com.eblp.metegolserver;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("metegol-server");
		config.setWindowedMode(1200, 890);
		config.setWindowSizeLimits(960, 720, 2880, 1620);
		new Lwjgl3Application(new MetegolServer(), config);
	}
}
