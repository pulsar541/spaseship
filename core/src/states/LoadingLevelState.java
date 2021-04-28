package states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Level;
import com.mygdx.game.Spaceship;
import com.mygdx.game.StarshipGame;

public class LoadingLevelState  extends State  {
    public LoadingLevelState(GameStateManager gsm) {
        super(gsm);
    }

    float sleepTime = 0;
    float MAX_SLEEP_TIME = 3;
    BitmapFont font = new BitmapFont();

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        sleepTime += dt;
        if(sleepTime > MAX_SLEEP_TIME) {
            PlayState playState = new PlayState(gsm);
            playState.allowGravity(StarshipGame.type == 1);
            gsm.set(playState);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        font.draw(sb,  "Level   " + String.valueOf(StarshipGame.currentLevel) + " / " +  String.valueOf(StarshipGame.MAX_LEVEL_NUMBER),  StarshipGame.WIDTH/2 , StarshipGame.HEIGHT/2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2);
        sb.end();

    }

    @Override
    public void dispose() {

    }
}
