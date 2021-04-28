package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import kepler.Body;
import kepler.Joint;
import kepler.Vec3;
import kepler.World;

public class Spaceship {

    public static final int LEFT_ENGINE = 0;
    public static final int RIGHT_ENGINE = 1;

    World _world;
    private Timer sparseTimer;

    public Spaceship(World world) {
        _world = world;
    }
    public  int[] engineWorkPower = new int[2];
    public  float life = 100;

    public Stack<Vec3> wayPoints = new Stack<Vec3>();

    public void setImpulsePower(float impulsePower) {
        this.impulsePower = impulsePower;
    }

    private float impulsePower = 0.01f;
    public Vec3 oldWaypointPos = new Vec3(0,0,0);

    public Vec3 getRespawnPos() {
        return respawnPos;
    }

    public void setRespawnPos(Vec3 respawnPos) {
        this.respawnPos = respawnPos;
    }

    public Vec3 respawnPos = new Vec3(0,0,0);

    public void init(Vec3 pos, float width, float height) {
        float halfWidth = width / 2;
        float ballRad =  halfWidth * 0.9f ;

        SPoint leftEngine = new SPoint();
        leftEngine.setPos(new Vec3(pos.x - halfWidth, pos.y, 0));
        leftEngine.setRadius(ballRad);
        leftEngine.setAllowGravity(true);
        _world.createBody(leftEngine);

        SPoint rightEngine = new SPoint();
        rightEngine.setPos(new Vec3(pos.x + halfWidth, pos.y, 0));
        rightEngine.setRadius(ballRad);
        rightEngine.setAllowGravity(true);
        _world.createBody(rightEngine);

        Joint joint = new Joint();
        joint.setBodiesLink(leftEngine, rightEngine);
        _world.addJoint(joint);

        wayPoints.clear();

        oldWaypointPos.set(-9999,-9999,0);


//        sparseTimer = new Timer();
//        sparseTimer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Gdx. app . postRunnable (new  Runnable () {
//                    @Override
//                    public  void  run () {
//                        if(kepler.Math.farThen(getPos(), oldPos, width() * 3.0f)) {
//                            pushWayPoint();
//                            oldPos.set(getPos());
//                        }
//
//                        if(life <= 0) {
//                            respawnOnHalfWay();
//
//
//                        }
//
//                    }
//                });
//            }
//        }, 200, 200);


    }
    public void update(float dt) {
        for(int i = 0; i<2; i++)
             engineWorkPower[i] = 0;

        _world.uptate(dt);
    }
    public void setImpulse(int numSpaceshipPart, float dt, float impulsePower) {
        Vec3 vector = new Vec3(0,0,0); //= new Vec3((float) java.lang.Math.cos(getRotation() + 90),(float) java.lang.Math.sin(getRotation()+ 90),0);

        Vec3 v = new Vec3(kepler.Math.sub(_world.getBody(LEFT_ENGINE).pos, _world.getBody(RIGHT_ENGINE).pos)).normalized();

        vector = kepler.Math.mul(v, new Vec3(0,0, 1));

        engineWorkPower[numSpaceshipPart] =  impulsePower > 0 ? 1: -1;
        _world.getBody(numSpaceshipPart).setVectorAndSpeed(vector,  impulsePower);
        _world.updateBody(numSpaceshipPart, dt);
    }

    public Vec3 getPos() {
        return  kepler.Math.middle(_world.getBody(RIGHT_ENGINE).pos, _world.getBody(LEFT_ENGINE).pos);
    }

    public float width() {
        Body ble = _world.getBody(LEFT_ENGINE);
        Body bre = _world.getBody(RIGHT_ENGINE);
        return kepler.Math.dist(ble.pos,bre.pos) + ble.getRadius() + bre.getRadius();
    }
    public float width0() {
        Body ble = _world.getBody(LEFT_ENGINE);
        Body bre = _world.getBody(RIGHT_ENGINE);
        return kepler.Math.dist(ble.pos,bre.pos);
    }

    public float height() {
        return width();
    }
    public float height0() {
        return width0() ;
    }

    public float visualSize() {
        return width0() * 1.5f;
    }


    public float getRotation() {
        return kepler.Math.angleFromPositions(getPos(), _world.getBody(RIGHT_ENGINE).pos);
    }

    public float getRotationRadians() {
        return kepler.Math.angleRadiansFromPositions(getPos(), _world.getBody(RIGHT_ENGINE).pos);
    }



    public void setPos(Vec3 pos) {
        float halfWidth = width0() / 2;
        _world.getBody(LEFT_ENGINE).setPos(new Vec3(pos.x - halfWidth, pos.y , 0));
        _world.getBody(RIGHT_ENGINE).setPos(new Vec3(pos.x + halfWidth, pos.y , 0));

        oldWaypointPos.set(pos);

    }

    public void respawnOnHalfWay() {
        int wayPointsHalfSize =  wayPoints.size() / 2;
        if(wayPointsHalfSize < 1)
            wayPointsHalfSize = 1;
        for(int k = 0; k < wayPointsHalfSize; k++) {
            wayPoints.pop();
        }

        if(wayPoints.size()>0)
            setPos(wayPoints.peek());

        life = 100;
    }

    public void pushWayPoint() {
        Vec3 vec = new Vec3(0,0,0);
        vec.set(getPos());
        wayPoints.push(vec);
        oldWaypointPos.set(getPos());
    }
}
