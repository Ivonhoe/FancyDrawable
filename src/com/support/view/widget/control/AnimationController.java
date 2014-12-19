package com.support.view.widget.control;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import com.support.view.widget.utils.L;

/**
 * Created by ivonhoe on 14-11-28.
 */
public class AnimationController {

    public static final int SAMPLE_SIZE = 1;
    private static AnimationController animationController;

    private static DisplayMetrics displayMetrics;
    private final SparseArray<PageScrollingListener> registeredView = new SparseArray<PageScrollingListener>();
    private float downY = 0;
    private float lastMovingProgress = 0;
    private int scrollToRight = 1;

    private PageScrollingListener mListener;
    private float[] offsetPixels = new float[SAMPLE_SIZE];
    private float[] offset = new float[SAMPLE_SIZE];
    private int checkTimes = 0;

    public static AnimationController getInstance(Context context) {
        if (animationController == null) {
            animationController = new AnimationController();
            displayMetrics = context.getResources().getDisplayMetrics();
        }
        return animationController;
    }

    public void register(PageScrollingListener listView) {
        int hashCode = listView.hashCode();
        if (registeredView.get(hashCode) == null) {
            registeredView.put(listView.hashCode(), listView);
        }
    }

    public void unregister(PageScrollingListener listView) {
        registeredView.remove(listView.hashCode());
    }

    public void setTouchDownPosition(float positionY) {
        downY = positionY;
    }

    public void onPageScrolling(float scrollProgress, float positionOffsetPixels) {
        float progress = scrollProgress;
        // make sure the first offset in the cache is the beginning location of the listener
        if (lastMovingProgress > 0.99 && progress == 0) {
            progress = 1f;
        } else if (scrollProgress == 0) {
            checkTimes = 0;
        }

        progress = progress * scrollToRight;

        if (mListener != null) {
            mListener.onPageScrolling(progress);
        } else if (checkTimes > SAMPLE_SIZE) {
            return;
        } else if (checkTimes == SAMPLE_SIZE) {
            PageScrollingListener listener = getCurrentListener(progress);
            if (listener != null) {
                mListener = listener;
                mListener.onPageScrolling(progress);
            }
            checkTimes++;
        } else {
            offsetPixels[checkTimes] = positionOffsetPixels;
            offset[checkTimes] = scrollProgress;
            getCurrentListener(progress);
            checkTimes++;
        }
        lastMovingProgress = progress;
    }

    private PageScrollingListener getCurrentListener(float scrollProgress) {
        int index = -1;
        float minDistance = Float.MAX_VALUE;
        for (int i = 0; i < registeredView.size(); i++) {
            PageScrollingListener listener = registeredView.valueAt(i);
            Location[] locations = listener.onFindPageScroll(scrollProgress, downY);
            if (locations == null) {
                continue;
            }
            boolean result = checkLocation(locations);

            float distance = Math.abs(offsetPixels[0] - locations[0].x);
            if (result && distance < minDistance) {
                index = i;
                minDistance = distance;
            }
        }
        return index > -1 ? registeredView.valueAt(index) : null;
    }

    private boolean checkLocation(Location[] locations) {
        if (locations == null) {
            return false;
        }

        int matchCount = 0;
        float delta = 0;
        for (int i = 1; i < offsetPixels.length; i++) {
            delta = offsetPixels[i] - offsetPixels[0];
            if (Math.abs(locations[i].x + delta - locations[0].x) < 2) {
                matchCount++;
            }
        }

        // 1. check direction use offsetPixels sample
        // scrollToRight = delta > 0 ? 1 : -1;
        // 2. check direction use offsetProgress
        scrollToRight = offset[0] > 0.9 ? -1 : 1;
        int displayWidth = displayMetrics.widthPixels;
        if (scrollToRight > 0 && locations[0].x >= displayWidth &&
                locations[0].x < 2 * displayWidth) {
            return true;
        } else if (scrollToRight < 0 && locations[0].x <= 0 && locations[0].x >= -displayWidth) {
            return true;
        }

        return false;
    }

    public void onPageScrollEnd() {
        mListener = null;
        checkTimes = 0;
        for (int i = 0; i < registeredView.size(); i++) {
            PageScrollingListener listener = registeredView.valueAt(i);
            listener.onEndPageScroll();
        }
    }
}
