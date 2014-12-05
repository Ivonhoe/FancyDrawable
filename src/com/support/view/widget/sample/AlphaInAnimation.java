package com.support.view.widget.sample;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;
import com.support.view.widget.core.AnimationImpl;

/**
 * Created by ivonhoe on 14-11-19.
 */
public class AlphaInAnimation implements AnimationImpl {

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        return new Animator[0];
    }

}
