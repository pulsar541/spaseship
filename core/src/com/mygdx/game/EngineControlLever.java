package com.mygdx.game;

public class EngineControlLever {
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void reset() {
        this.value = 0.0f;
    }

    private float value;

    public EngineControlLever(float value) {
        this.value = value;
    }
}
