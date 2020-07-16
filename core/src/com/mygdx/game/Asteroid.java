package com.mygdx.game;

import kepler.Body;

public class Asteroid extends Body {
    private static int _lastSsteroidUid = 1000;


    public int getAsteroidUid() {
        return asteroidUid;
    }

    private int asteroidUid = _lastSsteroidUid;

    public Asteroid() {
        super();
        asteroidUid = _lastSsteroidUid++;
    }

    public float getTakesLifeCount() {
        return takesLifeCount;
    }

    public void setTakesLifeCount(float takesLifeCount) {
        this.takesLifeCount = takesLifeCount;
    }

    private float takesLifeCount = 0;
}
