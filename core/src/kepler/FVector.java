package kepler;

public class FVector
{

    public float R;
    public float r;
    Vec3 vBegin = new Vec3();
    Vec3 vEnd = new Vec3();


//    public static void ResultofAddVect(FVector paramFVector1, FVector paramFVector2, FVector paramFVector3)
//    {
//        paramFVector1.vBegin.x = paramFVector2.vBegin.x;
//        paramFVector1.vBegin.y = paramFVector2.vBegin.y;
//        paramFVector1.vEnd.x = (paramFVector2.vEnd.x + paramFVector3.vEnd.x - paramFVector1.vBegin.x);
//        paramFVector1.vEnd.y = (paramFVector2.vEnd.y + paramFVector3.vEnd.y - paramFVector1.vBegin.y);
//    }

    public void calcGravityToPoint( Vec3 v, float gravity, float deltaTime)
    {

        float f1 = Math.dist(vEnd,  v);
        float f2 = (v.x - vEnd.x) / f1;
        float f3 = (v.y - vEnd.y) / f1;
        if (f1 < 150.0F)
            f1 = 150.0F;
        float f4 = 300000.0F / (f1 * f1);
        vEnd.x += 20.0F * (f4 * (f2 * gravity * deltaTime));
        vEnd.y += 20.0F * (f4 * (f3 * gravity * deltaTime));

    }

    public void calcGravityToBottom( float gravity, float deltaTime)
    {
        vEnd.y += gravity * deltaTime ;
    }

    public void calc_inertion(boolean allowInertion, Vec3 oldPos,  Vec3 pos)
    {
        vBegin.set(oldPos);
        vEnd.set(pos);
        if (!allowInertion)
        {
            vBegin = vEnd;
        }
    }
}