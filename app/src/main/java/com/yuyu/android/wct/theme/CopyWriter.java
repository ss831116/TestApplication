package com.yuyu.android.wct.theme;

import android.content.Context;

import com.yuyu.android.wct.R;

/**
 * Created by bernie.shi on 2016/4/6.
 */
public class CopyWriter {
    public static String fistPageTitle;
    public static String firstPageTabBarTitle;
    public static String secondPageTitle;
    public static String secondPageTabBarTitle;
    public static String thirdPageTitle;
    public static String thirdPageTabBarTitle;
    public static String fourthPageTitle;
    public static String fourthPageTabBarTitle;
    public static String dialogSuccessUploadDes;
    public static String dialogSuccessShareDes;
    public static String dialogSuccessShareOut;
    public static String dialogFailTitle;
    public static String dialogFailDelete;
    public static String dialogFailReUpload;

    public CopyWriter(Context context) {
        fistPageTitle = context.getString(R.string.price_notice);
        firstPageTabBarTitle = context.getString(R.string.price_notice);
        secondPageTitle = context.getString(R.string.goto_vote);
        secondPageTabBarTitle = context.getString(R.string.goto_vote);
        thirdPageTitle = context.getString(R.string.coming_soon);
        thirdPageTabBarTitle = context.getString(R.string.coming_soon);
        fourthPageTitle = context.getString(R.string.price_notice);
        fourthPageTabBarTitle = context.getString(R.string.person);
        dialogSuccessUploadDes = context.getString(R.string.upload_success_text1);
        dialogSuccessShareDes = context.getString(R.string.upload_success_text2);
        dialogSuccessShareOut = context.getString(R.string.upload_success_text3);
        dialogFailTitle = context.getString(R.string.upload_fail_text);
        dialogFailDelete = context.getString(R.string.delete_video);
        dialogFailReUpload = context.getString(R.string.reupload);
    }
}
