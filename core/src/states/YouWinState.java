package states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.StarshipGame;

public class YouWinState extends State {

    Texture texWin;
    public YouWinState(GameStateManager gsm) {
        super(gsm);
        texWin = new Texture("win.png");
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(texWin,0,  0, StarshipGame.WIDTH, StarshipGame.HEIGHT  );
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
