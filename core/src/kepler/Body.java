package kepler;


import java.util.ArrayList;
import java.util.List;

public abstract class Body {

    public int uid;
    public int linkedWithUid = -1;
    public FVector FMove = new FVector();
    public FVector FSh = new FVector();
    private float radius = 1.0f;
    private float massa = Math.PI;
    public List<Integer> neighboursIndexes = new ArrayList<>();
    public boolean isSoundReady = true;
    public boolean justContact = false;

    public boolean isTouch = false;

    public Vec3 getPos() {
        return pos;
    }


    public Vec3 pos = new Vec3();

    public Vec3 getOldPos() {
        return oldPos;
    }

    public void setOldPos(Vec3 oldPos) {
        this.oldPos = oldPos;
    }

    public Vec3 getDeltaPos() {
        return deltaPos;
    }

    public void setDeltaPos(Vec3 deltaPos) {
        this.deltaPos = deltaPos;
    }

    public Vec3 oldPos = new Vec3();
    public Vec3 deltaPos = new Vec3();
    private boolean isKinematic;
    private boolean allowInertion = true;
    private boolean allowGravity = false;

    public boolean isAllowCollision() {
        return allowCollision;
    }

    public void setAllowCollision(boolean allowCollision) {
        this.allowCollision = allowCollision;
    }

    private  boolean allowCollision = true;

    public float getRadius() {
        return radius;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public boolean isAllowGravity() {
        return allowGravity;
    }

    public void setAllowGravity(boolean allowGravity) {
        this.allowGravity = allowGravity;
    }

    public float getMassa() {
        return massa;
    }

    public void setMassa(float massa) {
        this.massa = massa;
    }

    public boolean isKinematic() {
        return isKinematic;
    }

    public void setKinematic(boolean kinematic) {
        isKinematic = kinematic;
    }

    public boolean isAllowInertion() {
        return allowInertion;
    }

    public void setAllowInertion(boolean allowInertion) {
        this.allowInertion = allowInertion;
    }

    public float getSpeed()
    {
        return  (float) java.lang.Math.sqrt(this.deltaPos.x * this.deltaPos.x + this.deltaPos.y * this.deltaPos.y);
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }
    public void setPos(Vec3 pos) {
        this.pos.set(pos);
        this.oldPos.set(this.pos);
    }

    public void updatePos(Vec3 _pos) {
        oldPos.set(this.pos);
        this.pos.set(_pos);
    }

    public void invertDx(float paramFloat)
    {
        deltaPos.x = paramFloat * -deltaPos.x;
        oldPos.x =  pos.x - deltaPos.x;
    }

    public void invertDy(float paramFloat)
    {
        deltaPos.y = paramFloat * -deltaPos.y;
        oldPos.y =  pos.y - deltaPos.y;
    }

    public void Move(float paramFloat)
    {
        if(isKinematic)
            return;

        oldPos.set(pos);

        pos.x +=  paramFloat * deltaPos.x;
        pos.y +=  paramFloat * deltaPos.y;

    }
    public void setVectorAndSpeed(Vec3 vec, float speed) {
        Vec3 v = new Vec3();
        v.set(vec.normalized());
        oldPos.x -= v.x * speed;
        oldPos.y -= v.y * speed;
    }


}
