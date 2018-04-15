package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * 就是一个开机广播的注册；
 * 不行：
 *      开机广播不行。在努比亚手机上不行。
 *   行：
 *      网络变化可以。
 *      息屏和开屏也可以。如果努比亚能做到，那么其余的手机也是可以的。
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        for (int i=0;i<5;i++){
            Toast.makeText(context, "Boot Complete"+i, Toast.LENGTH_LONG).show();
            Log.e("sww", "onReceive: "+i);
        }
    }

}
