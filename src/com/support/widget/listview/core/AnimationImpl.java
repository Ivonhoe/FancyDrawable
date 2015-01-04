package com.support.widget.listview.core;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ivonhoe on 14-11-19.
 */
public interface AnimationImpl {

    public Animator[] getAnimators(final ViewGroup parent, final View view);

}
