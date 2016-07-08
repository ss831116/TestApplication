package com.yuyu.android.wct.log;

import android.util.Log;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class DreamAchieveLog {
    public static boolean isDebug=true;

    public static void e(Class<?> clazz,String msg){
        if(isDebug){
            Log.e(clazz.getSimpleName(), msg + "");
        }
    }

    public static void i(Class<?> clazz,String msg){
        if(isDebug){
            Log.i(clazz.getSimpleName(), msg+"");
        }
    }

    public static void w(Class<?> clazz,String msg) {
        if (isDebug) {
            Log.w(clazz.getSimpleName(), msg + "");
        }
    }
}
