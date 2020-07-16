package kepler;

import java.util.ArrayList;
import java.util.List;

public class World {
    public List<Body> bodies = new ArrayList<>();
    public List<Joint> joints = new ArrayList<>();

    private static float kk = 0.0000002F;
    private static int lastUid = 1;
    private ContactListener contactListener;
    private float contactEpsilon = 0.0f;
    public  float gravity = 0.0f; // -0.25f;

    public  float getGravity() {
        return gravity;
    }

    public  void setGravity(float value) {
        gravity = value;
    }


    public float getGlobalFriction() {
        return globalFriction;
    }

    public void setGlobalFriction(float globalFriction) {
        this.globalFriction = globalFriction;
    }

    public  float globalFriction = 0.0f;


    float WORLD_LEFT_WALL;
    float WORLD_RIGHT_WALL;
    float WORLD_BOTTOM_WALL;
    float WORLD_TOP_WALL;

    public boolean wasContactBodies = false;

    public static enum Walls {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
    public World(float leftWall, float bottomWall, float rightWall, float topWall) {
        WORLD_LEFT_WALL = leftWall;
        WORLD_BOTTOM_WALL = bottomWall;
        WORLD_RIGHT_WALL = rightWall;
        WORLD_TOP_WALL = topWall;
    }


    public int bodiesCount() {
        return bodies.size();
    }

    public float getContactEpsilon() {
        return contactEpsilon;
    }

    public void setContactEpsilon(float contactEpsilon) {
        this.contactEpsilon = contactEpsilon;
    }

    public void setContactListener(ContactListener contactListener) {
        this.contactListener = contactListener;
    }

    public int createBody(Body body) {
        body.uid = lastUid++;
        bodies.add(body);
        return  body.uid;
    }

    public void createBody(Body body, int uid) {
       if(getBodyByUid(uid) == null ) {
           body.uid = uid;
           bodies.add(body);
       }
    }

    public Body getBody(int index) {
        return bodies.get(index);
    }
    public Body getBodyByUid(int uid) {
        for(int i = 0; i < bodies.size(); i++) {
            if(bodies.get(i).uid == uid) {
                 return  bodies.get(i);
            }
        }
        return null;
    }
    public void removeBodyByIndex(int index) {

        Body body = bodies.get(index);
        for(Integer item : body.neighboursIndexes){
            Body nb =  getBody(item.intValue());
            nb.setKinematic(false);
        }

        bodies.remove(index);
    }

    public void removeBodyByUid(int uid) {
        for(int i = 0; i < bodies.size(); i++) {
            if(bodies.get(i).uid == uid) {
                Body body = bodies.get(i);
                for(Integer item : body.neighboursIndexes){
                    Body nb =  getBody(item.intValue());
                    nb.setKinematic(false);
                }
                bodies.remove(i);
                break;
            }
        }
    }

    public void addJoint(Joint joint) {
        joints.add(joint);
    }

    public void removeJoint(int index) {
        joints.remove(index);
    }


    public void updateBody(int index, float dt) {
        Body body1 = (Body)this.bodies.get(index);
        if(!body1.isKinematic()) {

            body1.FSh.calc_inertion(body1.isAllowInertion(), body1.oldPos, body1.pos);
            if (body1.isAllowGravity())
                body1.FSh.calcGravityToBottom(gravity, dt);

            body1.FMove = body1.FSh;

            body1.deltaPos.x = (body1.FMove.vEnd.x - body1.FMove.vBegin.x);
            body1.deltaPos.y = (body1.FMove.vEnd.y - body1.FMove.vBegin.y);
        }
        ///////////
    }

    public void uptate(float dt) {

        int bodiesSize  = bodies.size();

        for ( int i = 0; i < bodiesSize; i++)
        {

            Body body1 = (Body)this.bodies.get(i);

            if(body1.isAllowCollision()) {

                if (body1.pos.y < WORLD_BOTTOM_WALL + body1.getRadius()) {
                    body1.pos.y = WORLD_BOTTOM_WALL + body1.getRadius();
                }

                if (body1.pos.y > WORLD_TOP_WALL - body1.getRadius()) {
                    body1.pos.y = WORLD_TOP_WALL - body1.getRadius();
                }

                if (body1.pos.x > WORLD_RIGHT_WALL - body1.getRadius()) {
                    body1.pos.x = (WORLD_RIGHT_WALL - body1.getRadius());
                }
                if (body1.pos.x < WORLD_LEFT_WALL + body1.getRadius()) {
                    body1.pos.x = WORLD_LEFT_WALL + body1.getRadius();
                }
            }


            ////////////////////////////////////////
            updateBody(i, dt);
            ////////////////////////////////////////


            if(!body1.isTouch)
                body1.Move(1.0f - globalFriction);


            body1.neighboursIndexes.clear();

        }


        for(int i=0; i<bodiesSize; i++) {
            Body body1 = (Body) this.bodies.get(i);
            float minDist = 10000;
            for(int j=0; j<bodiesSize; j++) {
                if(i==j)
                    continue;
                Body body2 = (Body) this.bodies.get(j);

                float d = kepler.Math.dist(body1.pos, body2.pos) - body1.getRadius() - body2.getRadius();
                if(d < minDist)
                    minDist = d;
            }
            if( minDist > body1.getRadius() * 0.35f) {
                body1.isSoundReady = true;
            }

        }



        for(int i=0; i<bodiesSize; i++) {
            Body body1 = (Body)this.bodies.get(i);

            for(int j=0; j<bodiesSize; j++) {
                if(i==j) continue;

                Body body2 = (Body)this.bodies.get(j);
                boolean awayResult = AwayM(body1, body2, body1.getRadius() + body2.getRadius(), 500);


                if(kepler.Math.nearestThen(body1.pos, body2.pos, body1.getRadius() + body2.getRadius() + contactEpsilon)) {

                    if(!body1.neighboursIndexes.contains(j))
                        body1.neighboursIndexes.add(j);

                    if(!body2.neighboursIndexes.contains(i))
                        body2.neighboursIndexes.add(i);


                }

            }
        }

        int jointsSize  = joints.size();
        for(int j=0; j<jointsSize; j++) {
            HardLink(
                    joints.get(j).body[0],
                    joints.get(j).body[1],
                    joints.get(j).distance,
                    1000
            );
        }

        for(int i=0; i<bodiesSize; i++) {
            Body body1 = (Body) this.bodies.get(i);
            if(body1.isSoundReady && body1.neighboursIndexes.size()  > 0) {
                body1.justContact = true;
            }
        }
    }


    public static boolean AwayM(Body firstBall, Body secondBall, float distance, int power)
    {
        if (java.lang.Math.abs(firstBall.pos.x - secondBall.pos.x) >= distance)
            return false;
        if (java.lang.Math.abs(firstBall.pos.y - secondBall.pos.y) >= distance)
            return false;

        boolean res = false;

        for (int i = 0;  Math.nearestThen(firstBall.pos, secondBall.pos, distance) && (i < power); i++)
        {
            if(!firstBall.isKinematic()) {
                firstBall.pos.x += kk * (firstBall.pos.x - secondBall.pos.x) * secondBall.getMassa();
                firstBall.pos.y += kk * (firstBall.pos.y - secondBall.pos.y) * secondBall.getMassa();
            }
            if(!secondBall.isKinematic()) {
                secondBall.pos.x += kk * (secondBall.pos.x - firstBall.pos.x) * firstBall.getMassa();
                secondBall.pos.y += kk * (secondBall.pos.y - firstBall.pos.y) * firstBall.getMassa();
            }

            res = true;

            if (java.lang.Math.abs(firstBall.pos.x - secondBall.pos.x) >= distance)
                return res;
            if (java.lang.Math.abs(firstBall.pos.y - secondBall.pos.y) >= distance)
                return res;

        }

        return res;

    }

//    public static boolean ComeM(Body firstBall, Body secondBall, float distance, int power)
//    {
//
//
//        boolean res = false;
//
//        for (int i = 0;  !Math.nearestThen(firstBall.pos, secondBall.pos, distance) && (i < power); i++)
//        {
//            if(!firstBall.isKinematic()) {
//                firstBall.pos.x -= kk * (firstBall.pos.x - secondBall.pos.x) * secondBall.getMassa();
//                firstBall.pos.y -= kk * (firstBall.pos.y - secondBall.pos.y) * secondBall.getMassa();
//            }
//            if(!secondBall.isKinematic()) {
//                secondBall.pos.x -= kk * (secondBall.pos.x - firstBall.pos.x) * firstBall.getMassa();
//                secondBall.pos.y -= kk * (secondBall.pos.y - firstBall.pos.y) * firstBall.getMassa();
//            }
//
//            res = true;
//
//
//
//        }
//
//        return res;
//
//    }

    void HardLink(Body u1, Body u2, float dist,int power)
    {
        for(int c=0;Math.farThen(u1.pos,u2.pos, dist + 1) && c<power;c++)
        {

            u1.pos.y-= 0.001*(u1.pos.y - u2.pos.y);
            u1.pos.x-= 0.001*(u1.pos.x - u2.pos.x);
            u2.pos.y-= 0.001*(u2.pos.y - u1.pos.y);
            u2.pos.x-= 0.001*(u2.pos.x - u1.pos.x);
        }

        for(int c=0;Math.nearestThen(u1.pos,u2.pos, dist-1) && c<power;c++)
        {

            u1.pos.y+= 0.001*(u1.pos.y - u2.pos.y);
            u1.pos.x+= 0.001*(u1.pos.x - u2.pos.x);
            u2.pos.y+= 0.001*(u2.pos.y - u1.pos.y);
            u2.pos.x+= 0.001*(u2.pos.x - u1.pos.x);
        }

    }

}
