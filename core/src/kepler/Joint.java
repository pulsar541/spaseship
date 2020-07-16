package kepler;

public class Joint {
    Body[] body = new Body[2];
    float distance;
    boolean isLink = true;
    public void setBodies(Body firstBody, Body secondBody) {
        body[0] = firstBody;
        body[1] = secondBody;
    }
    public void setDistance(float value) {
        distance = value;
    }
    public void setBodiesLink(Body firstBody, Body secondBody) {
        setBodies( firstBody,  secondBody);
        setDistance(Math.dist(firstBody.pos, secondBody.pos));
    }
}
