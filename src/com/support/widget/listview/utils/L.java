package com.support.widget.listview.utils;

import android.util.Log;

/**
 * Created by ivonhoe on 14-11-19.
 */
public class L {

    private static final String TAG = "simply";

    public static void d(String print) {
        Log.d(TAG, print);
    }

    public static void d(String TAG, String print) {
        Log.d(TAG, print);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
