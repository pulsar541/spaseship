package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.StarshipGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "StarshipGame";
		config.width = StarshipGame.WIDTH;
		config.height = StarshipGame.HEIGHT;
		new LwjglApplication(new StarshipGame(), config);
	}
}
