package com.support.view.widget.core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.support.view.widget.control.AnimationController;

/**
 * Created by ivonhoe on 14-11-28.
 */
public class ViewPagerDecorator extends ViewPager {

    View currentPage;
    float downY;

    public ViewPagerDecorator(Context context) {
        super(context);
    }

    public ViewPagerDecorator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            downY = ev.getRawY();
            AnimationController.getInstance(getContext()).setTouchDownPosition(ev.getRawY());
        }
        return super.onInterceptTouchEvent(ev);
    }
}
