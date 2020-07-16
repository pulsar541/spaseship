package kepler;


import com.badlogic.gdx.math.Vector3;

public class Math
{
    public static float PI = 3.1415926535897932384626f;
    public static float PiOver180 = PI / 180.0f;

    public static float abs(float a) {
        return a < 0 ? -a : a;
    }

    public static float dist(Vec3 v0, Vec3 v1)
    {
        return 1.0F / invSqrt((v0.x - v1.x) * (v0.x - v1.x) + (v0.y - v1.y) * (v0.y - v1.y));
    }

    public static float getHipo(float paramFloat1, float paramFloat2)
    {
        return 1.0F / invSqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2);
    }

    public static float invSqrt(float paramFloat)
    {
        float f1 = 0.5F * paramFloat;
        float f2 = Float.intBitsToFloat(1597462997 - (Float.floatToIntBits(paramFloat) >> 1));
        float f3 = f2 * (1.5F - f2 * (f1 * f2));
        return f3 * (1.5F - f3 * (f1 * f3));
    }

    public static boolean nearestThen(Vec3 v0, Vec3 v1, float distance)
    {
        return distance * distance >= (v0.x - v1.x) * (v0.x - v1.x) + (v0.y - v1.y) * (v0.y - v1.y);
    }
    public static boolean farThen(Vec3 v0, Vec3 v1, float distance)
    {
        return distance * distance < (v0.x - v1.x) * (v0.x - v1.x) + (v0.y - v1.y) * (v0.y - v1.y);
    }

    public static Vec3 sub(Vec3 v0, Vec3 v1) {
        return new Vec3(v0.x - v1.x, v0.y - v1.y, v0.z  - v1.z);
    }

    public static Vec3 mul(Vec3 a, Vec3 b) {
        return new Vec3(a.y * b.z - a.z * b.y,
                        a.z * b.x - a.x * b.z,
                        a.x * b.y - a.y * b.x);
    }

    public static Vec3 middle(Vec3 a, Vec3 b) {
        return new Vec3((a.x + b.x) * 0.5f,
                (a.y + b.y) * 0.5f,
                (a.z + b.z) * 0.5f);
    }

    public static float angleFromPositions(Vec3 srcPos, Vec3 tPos)
    {
        float X1 = srcPos.x;
        float Y1 = srcPos.y;
        float X2 = tPos.x;
        float Y2 = tPos.y;

        float b;  b= Math.abs(Y1-Y2);
        float c;  c= Math.abs(X1-X2);
        float result_ugol=0;

        if(Y2<=Y1 && X2>=X1) result_ugol = 360 - (float)java.lang.Math.atan(b/c)/PiOver180;
        if(Y2<=Y1 && X2<X1)  result_ugol = 180 + (float)java.lang.Math.atan(b/c)/PiOver180;
        if(Y2>Y1 && X2<=X1)  result_ugol = 180 - (float)java.lang.Math.atan(b/c)/PiOver180;
        if(Y2>Y1 && X2>X1)   result_ugol = (float)java.lang.Math.atan(b/c)/PiOver180;

        return result_ugol;
    }

    public static float angleRadiansFromPositions(Vec3 srcPos, Vec3 tPos)
    {
        return angleFromPositions(srcPos, tPos) * PiOver180;
    }
}