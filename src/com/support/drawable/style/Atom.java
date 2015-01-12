package com.support.drawable.style;

/**
 * Created by ivonhoe on 2015/1/7.
 */
public class Atom {

    private float delta = 0;
    private float locationX = -1;
    private float locationY = -1;
    private String text;

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }
}
