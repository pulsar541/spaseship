package com.pulsaruniverse.hsg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pulsaruniverse.hsg.StarshipGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Hardest space game";
		config.width = StarshipGame.WIDTH;
		config.height = StarshipGame.HEIGHT;
		new LwjglApplication(new StarshipGame(), config);
	}
}
