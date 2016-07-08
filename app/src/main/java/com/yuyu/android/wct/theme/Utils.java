package com.yuyu.android.wct.theme;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by bernie.shi on 2016/4/5.
 */
public class Utils {
    public static int titleBackgroundColor = Color.RED;
    public static float Text_Size_Array[] = {12, 14, 16, 18, 20, 22, 24, 26};
    public TextView textView;
    public void setTitleBackgroundColor(int titleBackgroundColor) {
        this.titleBackgroundColor = titleBackgroundColor;
    }

    public  void setTextSize(int textSize) {
        for ( int i = 0;i < Text_Size_Array.length; i++){
            Text_Size_Array[i]= textSize+2*(i+1);
        }
    }
}
