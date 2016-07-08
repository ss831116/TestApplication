package com.yuyu.android.wct.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yuyu.android.wct.main.HomeActivity_;

/**
 * Created by bernie.shi on 2016/4/26.
 */
public class RollitBackReceiver extends BroadcastReceiver {
    public static String TAG = "Rollit.Back.Receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(TAG)){
            Intent intent1 = new Intent(context, HomeActivity_.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
