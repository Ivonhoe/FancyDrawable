package com.support.view.widget.control;

/**
 * Created by ivonhoe on 14-11-28.
 */
public interface PageScrollingListener {

    public Location[] onFindPageScroll(float progress);

    public void onPageScrolling(float pageScrolling, float downY);

    public void onEndPageScroll();

}
