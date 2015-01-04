package com.support.widget.listview.control;

/**
 * Created by ivonhoe on 14-11-30.
 */
public class Location {

    public float x;
    public float y;

    public boolean isClear() {
        return x == -1f && y == -1f;
    }

    public void clear() {
        x = -1f;
        y = -1f;
    }
}
