package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameConfig;

public class PreferencesState extends State {

    BitmapFont font = new BitmapFont();

    public PreferencesState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(true, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
    }

    @Override
    protected void handleInput() {

        if(Gdx.input.justTouched()) {
            GameConfig.useArcadeCamera = !GameConfig.useArcadeCamera;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
