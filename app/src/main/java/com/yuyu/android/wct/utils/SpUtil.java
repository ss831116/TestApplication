package com.yuyu.android.wct.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yuyu.android.wct.R;

/**
 * Created by jackie.sun on 2016/4/6.
 */
public class SpUtil {
    protected static final String SP_VALUE_USER = "theme_color";
    private static final String SP_KEY_POSITION = "color_position";

    public static void setSelectPosi(Context context, int position) {
        SharedPreferences sp = context.getSharedPreferences(SP_VALUE_USER,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(SP_KEY_POSITION, position);
        edit.commit();
    }

    public static int getSelectPosi(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_VALUE_USER,
                Context.MODE_PRIVATE);
        return sp.getInt(SP_KEY_POSITION, 1);
    }

    public static int getSelectColor(Context context) {
        int i = getSelectPosi(context);
        int color = R.color.theme_red;
        switch (i) {
            case 1:
                color = R.color.theme_red;
                break;
            case 2:
                color = R.color.theme_green;
                break;
            case 3:
                color = R.color.theme_yellow;
                break;
            case 4:
                color = R.color.theme_purple;
                break;
            default:
                color = R.color.theme_red;
                break;
        }
        return color;
    }

    public static int getSelectText(Context context) {
        int i = getSelectPosi(context);
        int text = R.string.setting_red;
        switch (i) {
            case 1:
                text = R.string.setting_red;
                break;
            case 2:
                text = R.string.setting_green;
                break;
            case 3:
                text = R.string.setting_yellow;
                break;
            case 4:
                text = R.string.setting_purple;
                break;
            default:
                text = R.string.setting_red;
                break;
        }
        return text;
    }

    public static int getSettingDrawable(Context context) {
        int i = getSelectPosi(context);
        int draw = R.drawable.theme_circle_red;
        switch (i) {
            case 1:
                draw = R.drawable.theme_circle_red;
                break;
            case 2:
                draw = R.drawable.theme_circle_green;
                break;
            case 3:
                draw = R.drawable.theme_circle_yellow;
                break;
            case 4:
                draw = R.drawable.theme_circle_pur;
                break;
            default:
                draw = R.drawable.theme_circle_red;
                break;
        }
        return draw;
    }

    public static String getVersion(Context context) {
        String version = "1.0.1";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }
}
