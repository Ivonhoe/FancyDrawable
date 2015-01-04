package com.support.widget.listview.sample;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import com.support.widget.listview.core.AnimationImpl;

/**
 * Created by ivonhoe on 14-11-19.
 */
public class AlphaInAnimation implements AnimationImpl {

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        return new Animator[0];
    }

}
