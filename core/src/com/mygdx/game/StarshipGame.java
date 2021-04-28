package com.mygdx.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import states.GameStateManager;
import states.MenuState;

public class StarshipGame extends ApplicationAdapter {


	public static  int WIDTH = 1200;
	public static int HEIGHT =  800;

	public static int currentLevel = 1;
	public static int MAX_LEVEL_NUMBER = 64;
	public static int type = 0;

	public static final String TITLE = "Spaceship";
	private GameStateManager gsm;
	private SpriteBatch batch;

	private BitmapFont font;
	//Texture img;

	public static void tryDispose(Disposable disposableObj) {
		if(disposableObj != null)
			disposableObj.dispose();
	}

	@Override
	public void create() {

		currentLevel = 1;

		batch = new SpriteBatch();
		gsm = new GameStateManager();
		// Use LibGDX's default Arial font.1
		//font = new BitmapFont();
		Gdx.gl.glClearColor(0,0,0,1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	public void dispose() {
		tryDispose(batch);
 		tryDispose(font);
	}

}