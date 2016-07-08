package com.yuyu.android.wct.tools;

import android.content.Context;

import java.util.Locale;

/**
 * Created by bernie.shi on 2016/3/17.
 */
public class SystemInfoUtils {
    public static  String getCurrentLauguageUseResources(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;
    }
}
