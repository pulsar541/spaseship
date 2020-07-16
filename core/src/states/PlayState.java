package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import com.mygdx.game.Asteroid;
import com.mygdx.game.StarshipGame;
import com.mygdx.game.EngineControlLever;
import com.mygdx.game.GameConfig;
import com.mygdx.game.Level;
import com.mygdx.game.Spaceship;
import com.mygdx.game.Star;


import java.util.Timer;
import java.util.TimerTask;

import kepler.Body;
import kepler.Vec3;
import kepler.World;

public class PlayState extends State {


    World world ;
    Spaceship spaceship;
    Level level;
    EngineControlLever eclLeft, eclRight;

    OrthographicCamera cameraScreen;
    OrthographicCamera cameraStars;
    OrthographicCamera cameraLevel;
    OrthographicCamera cameraShip;

    public void allowGravity(boolean allowGr) {

        _allowGravity = allowGr;
        if(allowGr) {
            world.setGravity(-0.5f);
            bgTexture = new Texture("sky.jpg");
        }

    }

    private boolean _allowGravity = false;

    int score = 0;

    BitmapFont font = new BitmapFont();

    ShapeRenderer shapeRenderer;
    Timer sparseTimer;
  //  Sound soundDestroyBalls ;
   // Sound contactBalls ;
   // Music music;
    Texture bgTexture;
    Texture texShip ;
    Texture texAsteroid ;
    Texture texAsteroidRed;
    Texture texExplosive;
    Texture texEcl;
    Texture texCamera;
    Texture texStar;

    Rectangle rectButCamera;

    Texture texLevel;
    Pixmap pixmapLevel;

    Texture texRocketfire;

    Vector3 cameraLevelOffsetPos = new Vector3(0,0,0);



    int optim_i ;
    int optim_j ;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        Gdx.gl.glClearColor(0,0,0,1);

        cameraScreen = new OrthographicCamera();


        StarshipGame.HEIGHT = StarshipGame.WIDTH * Gdx.graphics.getBackBufferHeight() / Gdx.graphics.getBackBufferWidth();

        cameraScreen.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);

        cameraLevel = new OrthographicCamera();
        cameraShip = new OrthographicCamera();
        cameraStars = new  OrthographicCamera();

        cameraStars.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);
        shapeRenderer = new ShapeRenderer();


        sparseTimer = new Timer();
        sparseTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Gdx. app . postRunnable (new  Runnable () {
                    @Override
                    public  void  run () {
//                        if(kepler.Math.farThen(spaceship.getPos(), spaceship.oldWaypointPos, spaceship.width() * 3.0f)) {
//                            spaceship.pushWayPoint();
//                        }

                        Asteroid tmpAster;
                        for(int i = optim_i-5; i < optim_i+5; i++) {
                            if(i<0 || i>= Level.levelSizeHorCount)
                                continue;
                            for(int j = optim_j-5; j < optim_j+5; j++) {
                                if (j < 0 || j >= Level.levelSizeVertCount)
                                    continue;
                                tmpAster =  level.asteroids.get(level.table[i][j]);
                                if(tmpAster.getTakesLifeCount() != 100)
                                if(kepler.Math.nearestThen(spaceship.getPos(),tmpAster.pos, spaceship.width()*3)) {
                                    world.createBody(tmpAster, tmpAster.getAsteroidUid());
                                }

                                if(world.getBodyByUid(tmpAster.getAsteroidUid()) != null &&  kepler.Math.farThen(spaceship.getPos(),tmpAster.pos, spaceship.width()*5)) {
                                    world.removeBodyByUid(tmpAster.getAsteroidUid());
                                }


                            }
                        }

                        if(spaceship.life <= 0) {
                            //spaceship.respawnOnHalfWay();
                            spaceship.life = 100;
                            eclLeft.reset();
                            eclRight.reset();
                            spaceship.setPos(spaceship.getRespawnPos());
                        }

                    }
                });
            }
        }, 200, 200);




        texShip = new Texture("ship.png");
        texAsteroid = new Texture("asteroid_low.png");
        texAsteroidRed = new Texture("asteroid_red_low.png");
        bgTexture = new Texture("space.jpg");
        texRocketfire = new Texture("rocketfire.png");
        texExplosive = new Texture("explosive.png");
        texEcl = new Texture("ecl.png");
        texCamera = new Texture("camera.png");
        texStar = new Texture("star.png");

       // soundDestroyBalls = Gdx.audio.newSound(Gdx.files.internal("removeballs.mp3"));
       /// contactBalls = Gdx.audio.newSound(Gdx.files.internal("twoballs.mp3"));
//        music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
//        music.setLooping(true);
//        music.setVolume(0.25f);
//        music.play();

        texLevel = new Texture("level0.png");
        if (!texLevel.getTextureData().isPrepared()) {
            texLevel.getTextureData().prepare();
        }
        pixmapLevel = texLevel.getTextureData().consumePixmap();

        world = new World(0, 0, Level.levelSizeHorCount * Level.elementSize, Level.levelSizeVertCount * Level.elementSize);
        world.setContactEpsilon(3.0f);
        // world.setGlobalFriction(0.001f);



        spaceship = new Spaceship(world);
        spaceship.init( new Vec3(Level.elementSize * 10, Level.elementSize * 10, 0), 30, 30);
        spaceship.setImpulsePower(0.04f);

        eclLeft = new EngineControlLever(0.0f);
        eclRight = new EngineControlLever( 0.0f);

        level = new Level(world);
        //level.load(pixmapLevel);
        Vec3 startPos = level.generate(spaceship.width() * 3.0f);
        spaceship.setPos(startPos);
        spaceship.setRespawnPos(spaceship.getPos());


        rectButCamera = new Rectangle();
        float texCamW  = texCamera.getWidth() / 3;
        float texCamH  = texCamW  * texCamera.getHeight()  /  texCamera.getWidth();
        rectButCamera.set( StarshipGame.WIDTH / 2 - texCamW / 2 , StarshipGame.HEIGHT - texCamH - 10,   texCamW,  texCamH );

        score = 0;
    }


    @Override
    public void dispose() {
        tryDispose(font);
        tryDispose(shapeRenderer);
        tryDispose(texShip);
        tryDispose(texAsteroid);
        tryDispose(texLevel);
        //  tryDispose(soundDestroyBalls);
        // tryDispose(music);
        tryDispose(texRocketfire);
        tryDispose(texExplosive);
        tryDispose(texEcl);
        tryDispose(texCamera);
        tryDispose(texStar);
    }

    @Override
    protected void handleInput() {

        boolean isEclLeftBusy = false;
        boolean isEclRightBusy = false;
        for(int i = 0; i < 4; i++) {
            if (Gdx.input.isTouched(i)) {
                if (Gdx.input.getX(i) < Gdx.graphics.getBackBufferWidth() / 3) {
                    eclLeft.setValue(2.0f * Gdx.input.getY(i) / Gdx.graphics.getBackBufferHeight() - 1.0f);

                    if(_allowGravity)

                    if(eclLeft.getValue() > 0)
                        eclLeft.setValue( eclLeft.getValue() - 0.02f);
                    else if(eclLeft.getValue() < 0)
                        eclLeft.setValue( eclLeft.getValue() + 0.02f);

                    isEclLeftBusy = true;

                }
                if (Gdx.input.getX(i) > 2 * Gdx.graphics.getBackBufferWidth() / 3)  {
                    eclRight.setValue(2.0f * Gdx.input.getY(i) / Gdx.graphics.getBackBufferHeight() - 1.0f);

                    if(eclRight.getValue() > 0)
                        eclRight.setValue( eclRight.getValue() - 0.02f);
                    else if(eclRight.getValue() < 0)
                        eclRight.setValue( eclRight.getValue() + 0.02f);

                    isEclRightBusy = true;

                }
            }
        }

        if(!isEclLeftBusy) {
            eclLeft.setValue( eclLeft.getValue() * 0.9f);
        }

        if(!isEclRightBusy) {
            eclRight.setValue( eclRight.getValue() * 0.9f);
        }



        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraScreen.unproject(touchPos);
            if (rectButCamera.overlaps(new Rectangle(touchPos.x - rectButCamera.width / 2, touchPos.y - rectButCamera.height / 2, rectButCamera.width, rectButCamera.height))) {
                GameConfig.useArcadeCamera = !GameConfig.useArcadeCamera;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                cameraLevelOffsetPos.x -= 10;

            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                cameraLevelOffsetPos.x += 10;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                cameraLevelOffsetPos.y += 10;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                cameraLevelOffsetPos.y -= 10;
            }
        }

    }
    int rotation = 0;
    @Override
    public void update(float dt) {

        spaceship.update(dt);
        handleInput();

        float impulseMulCoeff = _allowGravity ? 0.03f : 0.01f;

        if(Math.abs(eclLeft.getValue())  >  0) {
            spaceship.setImpulse(Spaceship.LEFT_ENGINE, Gdx.graphics.getDeltaTime(),   -eclLeft.getValue()  * impulseMulCoeff);
        }

        if(Math.abs(eclRight.getValue())  > 0.05) {
            spaceship.setImpulse(Spaceship.RIGHT_ENGINE, Gdx.graphics.getDeltaTime(), -eclRight.getValue() * impulseMulCoeff);
        }

        if(GameConfig.useArcadeCamera ) {  // non-rotation camera

            cameraLevel.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);
            cameraLevel.position.set(new Vector3(spaceship.getPos().x + cameraLevelOffsetPos.x, spaceship.getPos().y + cameraLevelOffsetPos.y, 0));

            cameraShip.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);
            cameraShip.position.set(new Vector3(spaceship.getPos().x, spaceship.getPos().y, 0));
            cameraShip.rotate(-spaceship.getRotation(), 0, 0, 1);

            cameraStars.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);

        }

        else {  // rotated camera

            cameraLevel.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);
            cameraLevel.position.set(new Vector3(  spaceship.getPos().x, spaceship.getPos().y, 0 ));
            cameraLevel.rotate(spaceship.getRotation(),0,0,1);

            cameraShip.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);
            cameraShip.position.set(new Vector3(  spaceship.getPos().x, spaceship.getPos().y, 0 ));

            cameraStars.setToOrtho(false, StarshipGame.WIDTH, StarshipGame.HEIGHT);
            cameraStars.rotate(spaceship.getRotation(),0,0,1);


        }

        cameraLevel.update();
        cameraShip.update();
        cameraStars.update();


          optim_i = (int) ((spaceship.getPos().x + cameraLevelOffsetPos.x) / level.elementSize);
          optim_j = (int) ((spaceship.getPos().y + cameraLevelOffsetPos.y) / level.elementSize);


        Asteroid tmpAster;

        for(int i = optim_i-5; i < optim_i+5; i++) {
            if(i<0 || i>= Level.levelSizeHorCount)
                continue;

            for(int j = optim_j-5; j < optim_j+5; j++) {
                if (j < 0 || j >= Level.levelSizeVertCount)
                    continue;

                tmpAster =  level.asteroids.get(level.table[i][j]);


                if(kepler.Math.nearestThen(spaceship.getPos(),tmpAster.pos, spaceship.width0()*0.65f + tmpAster.getRadius() * 0.5f)) {

                    world.createBody((Body) tmpAster, tmpAster.getAsteroidUid());

                    spaceship.life -= tmpAster.getTakesLifeCount();
                    if( spaceship.life <= 0) {
                        spaceship.life = 0;
                    }
                }
            }
        }


        Star tmpStar;
        int starsSize = level.stars.size();
        for(int i = 0; i < starsSize; i++) {

            tmpStar = level.stars.get(i);

            if(kepler.Math.nearestThen(spaceship.getPos(),tmpStar, spaceship.width()*0.85f)) {
                score ++;
                spaceship.setRespawnPos(tmpStar);
                if(score >= 10) {
                    score = 10;
                    gsm.set(new YouWinState(gsm));
                }
                level.stars.remove(i);
                break;
            }

        }


    }

    @Override
    public void render(SpriteBatch sb) {
        int ballsSize  = world.bodiesCount();

    ///    sb.setProjectionMatrix(cameraStars.combined);
        sb.enableBlending();

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(new Color(Color.rgba8888(0.5f,0.5f,.5f,1.0f)));
//        shapeRenderer.box(LEFT_WALL, 0, 0, RIGHT_WALL-LEFT_WALL, maxHeight , 1);
//        shapeRenderer.end();
//


        sb.setProjectionMatrix(cameraStars.combined);
        float maxScreenSize = Math.max(StarshipGame.WIDTH, StarshipGame.HEIGHT);
        sb.begin();
          sb.draw(bgTexture,
                  -(int)(maxScreenSize * 0.25f),
                  -(int)(maxScreenSize * 0.25f),
                  (int)(maxScreenSize * 1.5f) ,
                  (int)(maxScreenSize * 1.5f)  );
        sb.end();



        sb.setProjectionMatrix(cameraLevel.combined);

        sb.begin();

        Asteroid tmpAster;
        for(int i = optim_i-15; i < optim_i+15; i++) {
            if(i<0 || i>= Level.levelSizeHorCount)
                continue;

            for(int j = optim_j-15; j < optim_j+15; j++) {
                if (j < 0 || j >= Level.levelSizeVertCount)
                    continue;

                tmpAster = level.asteroids.get(level.table[i][j]);
                float ax = tmpAster.pos.x;
                float ay = tmpAster.pos.y;

                sb.draw( tmpAster.getTakesLifeCount() == 100 ? texAsteroidRed : texAsteroid,
                        ax-Level.elementSize * 0.5f,
                        ay-Level.elementSize * 0.5f,
                        level.elementSize,
                        level.elementSize );

            }
        }


        sb.end();


        sb.begin();
        int starsSize = level.stars.size();
        for(int i = 0; i < starsSize; i++) {

            float ax = level.stars.get(i).x;
            float ay = level.stars.get(i).y;

            sb.draw(  texStar,
                    ax-Level.elementSize * 0.5f,
                    ay-Level.elementSize * 0.5f,
                    level.elementSize,
                    level.elementSize );
        }
        sb.end();

        sb.setProjectionMatrix(cameraShip.combined);
        sb.begin();
        if(spaceship.life > 0 ) {


            float leftFireScale  = Math.abs(eclLeft.getValue()) * (1.0f + (float) Math.random() * 0.3f);
            float rightFireScale = Math.abs(eclRight.getValue())* (1.0f + (float) Math.random() * 0.3f);


            if (spaceship.engineWorkPower[Spaceship. LEFT_ENGINE] < 0) {
                //lefttop
                sb.draw(texRocketfire,
                        spaceship.getPos().x - spaceship.visualSize() / 2,
                        spaceship.getPos().y,
                        spaceship.visualSize() / 2,
                        spaceship.visualSize() * leftFireScale);
            }

            if (spaceship.engineWorkPower[Spaceship. RIGHT_ENGINE] < 0) {
                //righttop
                sb.draw(texRocketfire,
                        spaceship.getPos().x,
                        spaceship.getPos().y,
                        spaceship.visualSize() / 2,
                        spaceship.visualSize() * rightFireScale);
            }

            if (spaceship.engineWorkPower[Spaceship. LEFT_ENGINE] > 0) {
                //leftbottom
                sb.draw(texRocketfire,
                        spaceship.getPos().x - spaceship.visualSize() / 2,
                        spaceship.getPos().y - spaceship.visualSize() *leftFireScale * 1.25f,
                        spaceship.visualSize() / 2,
                        spaceship.visualSize() * leftFireScale, 0, 0, 1, 1);
            }

            if (spaceship.engineWorkPower[Spaceship. RIGHT_ENGINE] > 0) {
                //rightbottom
                sb.draw(texRocketfire,
                        spaceship.getPos().x,
                        spaceship.getPos().y - spaceship.visualSize() * rightFireScale * 1.25f,
                        spaceship.visualSize() / 2,
                        spaceship.visualSize() *rightFireScale, 0, 0, 1, 1);
            }

        }

        if( spaceship.life > 0 ) {
            sb.draw( texShip  ,
                    spaceship.getPos().x - spaceship.visualSize() / 2.0f,
                    spaceship.getPos().y - spaceship.visualSize() / 2.0f,
                    spaceship.visualSize(),
                    spaceship.visualSize());
        } else {
            sb.draw( texExplosive,
                    spaceship.getPos().x - spaceship.visualSize()*0.65f,
                    spaceship.getPos().y - spaceship.visualSize()*0.65f,
                    spaceship.visualSize()*1.5f,
                    spaceship.visualSize()*1.5f);
        }

//        sb.draw(  texShip,
//                spaceship.getPos().x-spaceship.width()/2.0f,
//                spaceship.getPos().y-spaceship.height()/2.0f,
//                spaceship.width() / 2.0f,
//                spaceship.height() / 2.0f,
//                spaceship.width(),
//                spaceship.height(),
//                1.0f,
//                1.0f,
//                 spaceship.getRotation(),
//                0,
//                0,
//                texShip.getWidth(),
//                texShip.getHeight(),
//                false,
//                false);






        //      sb.draw(  grayBallTexture, nextBallGun.getPos().x-10, nextBallGun.getPos().y-10, 20, 20 );

        sb.end();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);





//       shapeRenderer.setProjectionMatrix(cameraLevel.combined);
//
//       shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//       shapeRenderer.setColor(new Color(Color.rgba8888(0.0f, 1.0f, 0.0f, 0.5f)));
//        int waySize =  spaceship.wayPoints.size();
//        for(int i = 0; i < waySize; i++) {
//            //   shapeRenderer.identity();
//            //    shapeRenderer.translate(x, y, 0.f);
//                 shapeRenderer.rect( spaceship.wayPoints.get(i).x, spaceship.wayPoints.get(i).y, 2, 2);
//        }
//        shapeRenderer.end();








//        shapeRenderer.setProjectionMatrix(cameraLevel.combined);
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(new Color(Color.rgba8888(1.0f, 1.0f, 1.0f, 0.5f)));
//
//
//        int optim_i = (int) (spaceship.getPos().x / level.elementSize);
//        int optim_j = (int) (spaceship.getPos().y / level.elementSize);
//
//        for(int i = optim_i-5; i < optim_i+5; i++) {
//            if(i<0 || i>=100)
//                continue;
//
//            for(int j = optim_j-5; j < optim_j+5; j++) {
//                if (j < 0 || j >= 100)
//                    continue;
//
//
//                float x = level.asteroids.get(level.table[i][j]).pos.x;
//                float y = level.asteroids.get(level.table[i][j]).pos.y;
//
//                shapeRenderer.identity();
//                shapeRenderer.translate(x, y, 0.f);
//                shapeRenderer.rect(-8, -8, 16, 16);
//
//            }
//
//        }
//
//        shapeRenderer.end();






        //            for ( int i = 0; i < ballsSize; i++) {
//                sb.draw(  texShip,
//                        world.getBody(i).getPos().x - world.getBody(i).getRadius(),
//                        world.getBody(i).getPos().y- world.getBody(i).getRadius(),
//                        world.getBody(i).getRadius() * 2,
//                        world.getBody(i).getRadius() * 2 );
//
//            }




        Gdx.gl.glDisable(GL20.GL_BLEND);


        sb.setProjectionMatrix(cameraScreen.combined);
        sb.begin();
         font.draw(sb, String.valueOf( score ) + " / " + String.valueOf( level.stars.size() ),  StarshipGame.WIDTH - 80 , StarshipGame.HEIGHT - 20);
         font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
         font.getData().setScale(2);
        sb.end();



        sb.begin();
        sb.draw(texEcl,10,( 0.5f - eclLeft.getValue() * 0.5f) * StarshipGame.HEIGHT - 50,  100 , 100  );
        sb.draw(texEcl, StarshipGame.WIDTH - 110,( 0.5f - eclRight.getValue() * 0.5f) * StarshipGame.HEIGHT - 50, 100, 100  );
        sb.end();

        sb.begin();
        float texCamW  = texCamera.getWidth() / 3;
        float texCamH  = texCamW  * texCamera.getHeight()  /  texCamera.getWidth();
        sb.draw(texCamera,rectButCamera.x, rectButCamera.y, rectButCamera.width, rectButCamera.height);
        sb.end();

        sb.begin();
        for(int i = 0; i< score; i++) {
            sb.draw(texStar,10 + i*32, StarshipGame.HEIGHT - 35, 25, 25  );
        }
        sb.end();

    }



}
