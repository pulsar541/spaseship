package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector3;
import com.pulsaruniverse.hsg.StarshipGame;

import java.awt.TextArea;

import applib.ButtonImage;

public class MenuState  extends State {

    private TextureRegion textureRegion;
    float drawingWidth, drawingHeight;
    BitmapFont font = new BitmapFont();


    Texture bg;
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

        biPlay = new ButtonImage("btnPlay.png",    Gdx.graphics.getWidth() - Gdx.graphics.getWidth() /4 ,  Gdx.graphics.getHeight()/2, 2 );
       // biPlayGravity = new ButtonImage("btnPlayGravity.png",    3*Gdx.graphics.getWidth()/4 ,  Gdx.graphics.getHeight()/2 );
       // biMultiplayer = new ButtonImage("btnMultiplayer.png",    Gdx.graphics.getWidth()/2 ,  Gdx.graphics.getHeight() / 5 );

        bg = new Texture("maintitle.png");

    }

    @Override
    protected void handleInput() {


        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (biPlay.isTouched(touchPos)) {
                StarshipGame.type = 0;
                gsm.set(new LoadingLevelState(gsm));
            }

           // if (biPlayGravity.isTouched(touchPos)) {
          //      StarshipGame.type = 1;
          //      gsm.set(new LoadingLevelState(gsm));
         //  }

           // if (biMultiplayer.isTouched(touchPos)) {
           //     MultiplayerMenuState multiplayerMenuState = new MultiplayerMenuState(gsm);
           //     gsm.set(multiplayerMenuState);
           // }
        }
    }


    @Override
    public void update(float dt) {
        handleInput();


    }

    @Override
    public void render(SpriteBatch sb) {

        sb.begin();
        sb.draw(bg,0,  0, StarshipGame.WIDTH, StarshipGame.HEIGHT  );
        sb.end();
        sb.begin();
      //   sb.draw(textureRegion,0,0, drawingWidth, drawingHeight);
      //  font.draw(sb, "PLAY", Gdx.graphics.getWidth()/2-10, Gdx.graphics.getHeight() /2);

            font.draw(sb, "version 0.54", 40, 80);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            font.getData().setScale(3);


            font.draw(sb,  "(c) 2021 pulsaruniverse", Gdx.graphics.getWidth()/2, 80);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            font.getData().setScale(3);


            biPlay.draw(sb);
           // biPlayGravity.draw(sb);
           // biMultiplayer.draw(sb);
        sb.end();



    }

    @Override
    public void dispose() {
      //  background.dispose();

    }
}