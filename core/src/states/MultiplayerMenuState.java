package states;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector3;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import applib.ButtonImage;
import applib.SRq;

public class MultiplayerMenuState extends State {

    ButtonImage biServer;
    ButtonImage biClient;

    Server server;
    Client client;

    static boolean messageReceived = false;

    public MultiplayerMenuState(GameStateManager gsm) {
        super(gsm);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        camera  = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        biServer = new ButtonImage("btnServer.png",    Gdx.graphics.getWidth()/4 ,  Gdx.graphics.getHeight()/2 );
        biClient = new ButtonImage("btnClient.png",    3*Gdx.graphics.getWidth()/4 ,Gdx.graphics.getHeight()/2 );
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (biServer.isTouched(touchPos)) {

                server = new Server();

                Kryo kryo = server.getKryo();
                kryo.register(SRq.class);
                server.start();
                try {
                    server.bind(54555, 54777);
                } catch (Exception e) {
                    System.err.println("Failed to bind to port!");
                }
                server.addListener(new Listener() {
                    @Override
                    public void received(Connection connection, Object object) {
                        if(object instanceof SRq) {
                            System.out.println("Server " +  ((SRq) object).data);
                            SRq sRq = new SRq();
                            sRq.data = "Data";
                            connection.sendTCP(sRq);
                        }
                    }
                });
                return;
            }

            if (biClient.isTouched(touchPos)) {
                client = new Client();
                Kryo kryo = client.getKryo();
                kryo.register(SRq.class);
                client.start();
                try {
                    client.connect(6000, "localhost", 54555, 54777);
                } catch (Exception e) {
                    System.err.println("Failed to connect to server!");
                }

                client.addListener(new Listener() {
                    @Override
                    public void received(Connection connection, Object object) {
                    if(object instanceof SRq) {
                        Gdx.app.log("Client", ((SRq) object).data);
                    }
                    }
                });

                System.out.println("Connected to server!  Waiting data from server...");

                while(!messageReceived) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

//                SRq sRq = new SRq();
//                sRq.data = "Log in";
//                client.sendTCP(sRq);

                return;
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
        biServer.draw(sb);
        biClient.draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
