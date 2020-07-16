package com.mygdx.game;

import kepler.Vec3;

public class Star extends Vec3 {

    public Star(float x, float y) {
        super(x, y);
    }

    public Star(float x, float y, float z) {
        super(x, y, z);
    }

    public Star(Vec3 v) {
        super(v);
    }
}
