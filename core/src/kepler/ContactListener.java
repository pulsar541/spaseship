package kepler;

public abstract class ContactListener {
    protected World world;

    public ContactListener(World world) {
        this.world = world;
    }

    public abstract void beginContactWall(int bodyIndex, int bodyUid, World.Walls wall);
    public abstract void beginContactBodies(int firstBodyIndex, int secondBodyIndex, Vec3 point);
}
