package kepler;

import com.badlogic.gdx.math.Vector3;

public class Vec3 {
    public float x;
    public float y;
    public float z;
    Vec3() {

    }
    public Vec3(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = .0f;
    }
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vec3(Vec3 v) {
        set(v);
    }
    public Vec3 normalized() {
        float length = Math.dist(new Vec3(0,0,0), this);
        if(length <=0 )
            return new Vec3(1,0,0);
        return new Vec3(x / length, y / length, z / length );
    }
    public void set(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 toVector3() {
        return new Vector3(x,y,z);
    }


}
