package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector3;

import java.awt.TextArea;

import applib.ButtonImage;

public class MenuState  extends State {

    private TextureRegion textureRegion;
    float drawingWidth, drawingHeight;
    BitmapFont font = new BitmapFont();


    ButtonImage biPlay;
    ButtonImage biPlayGravity;
    ButtonImage biMultiplayer;

    private TextArea taIP;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera  = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        biPlay = new ButtonImage("btnPlay.png",    Gdx.graphics.getWidth()/4 ,  Gdx.graphics.getHeight()/2 );
        biPlayGravity = new ButtonImage("btnPlayGravity.png",    3*Gdx.graphics.getWidth()/4 ,  Gdx.graphics.getHeight()/2 );
        biMultiplayer = new ButtonImage("btnMultiplayer.png",    Gdx.graphics.getWidth()/2 ,  Gdx.graphics.getHeight() / 5 );

    }

    @Override
    protected void handleInput() {


        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (biPlay.isTouched(touchPos)) {
                PlayState playState = new PlayState(gsm);
                playState.allowGravity(false);
                gsm.set(playState);

            }

            if (biPlayGravity.isTouched(touchPos)) {
                PlayState playState = new PlayState(gsm);
                playState.allowGravity(true);
                gsm.set(playState);

            }

            if (biMultiplayer.isTouched(touchPos)) {
                MultiplayerMenuState multiplayerMenuState = new MultiplayerMenuState(gsm);
                gsm.set(multiplayerMenuState);
            }
        }
    }


    @Override
    public void update(float dt) {
        handleInput();


    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
      //   sb.draw(textureRegion,0,0, drawingWidth, drawingHeight);
      //  font.draw(sb, "PLAY", Gdx.graphics.getWidth()/2-10, Gdx.graphics.getHeight() /2);

        font.draw(sb, "programmer: E.U.", 20, 40);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2);


        biPlay.draw(sb);
        biPlayGravity.draw(sb);
        biMultiplayer.draw(sb);


        sb.end();

    }

    @Override
    public void dispose() {
      //  background.dispose();

    }
}