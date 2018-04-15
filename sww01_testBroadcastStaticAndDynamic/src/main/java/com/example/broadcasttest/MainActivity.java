package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 动态注册
 *  1.一般广播：
 *      1.继承BroadcastReceiver --> MyBroadcastReceiver实例化；
 *      2.IntentFilter实例化，再add(action);
 *      3.registerReceiver(myBroadcastReceiver,intentFilter);
 *  2.本地广播：
 *      3.拿到LocalBroadcastManager实例，并使用实例调用registerReceiver。
 *
 * 发送广播：
 *  1.构建Intent, 然后sendBroadcast(intent);
 *  2.构建Intent, 然后LocalBroadcastManager.sendBroadcast(intent);
 *
 *  试一下：
 *      1.开机广播
 *      2.息屏广播和亮屏广播；
 *      3.网络变化广播；
 *
 * Android 无法接收开机广播的问题。总结如下：

 如下几个原因：
 (1)、BOOT_COMPLETED对应的action和uses-permission没有一起添加
 (2)、应用安装到了sd卡内，安装在sd卡内的应用是收不到BOOT_COMPLETED广播的
 (3)、系统开启了Fast Boot模式，这种模式下系统启动并不会发送BOOT_COMPLETED广播
 (4)、应用程序安装后重来没有启动过，这种情况下应用程序接收不到任何广播，
    包括BOOT_COMPLETED、ACTION_PACKAGE_ADDED、CONNECTIVITY_ACTION等等。
    系统为了加强了安全性控制，应用程序安装后或是(设置)应用管理中被强制关闭后处于stopped状态，
    在这种状态下接收不到任何广播。
    直到被启动过(用户打开或是其他应用调用)才会脱离这种状态，所以Android3.1之后
 (1)、应用程序无法在安装后自己启动
 (2)、没有ui的程序必须通过其他应用激活才能启动，如它的Activity、Service、Content Provider被其他应用调用。

 但还是有时候收不到的原因呢？

 1.安装应用后，首先要启动一次。
 2.如果签名后，不可以用eclipse安装apk文件，手动安装好后，也要启动一次。
 3.添加以下：
 <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 <uses-permission android:name="android.permission.RESTART_PACKAGES" />
 */
public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;

    private LocalReceiver localReceiver;

    private LocalBroadcastManager localBroadcastManager;

//    private IntentFilter intentFilter;
//
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        localBroadcastManager = LocalBroadcastManager.getInstance(this); // 获取实例
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcasttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent); // 发送本地广播
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter); // 注册本地广播监听器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkChangeReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    /**
     * 动态注册的广播不需要注册；
     */
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }

    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Toast.makeText(MainActivity.this,"screen off",Toast.LENGTH_SHORT).show();
                Log.e("sww", "onReceive: off" );
            }else {
                Toast.makeText(MainActivity.this,"screen on",Toast.LENGTH_LONG).show();
                Log.e("sww", "onReceive: on" );
            }
            /*ConnectivityManager connectionManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(context, "network is available",
                Toast.makeText(context, "screen is on",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "screen is off",
                        Toast.LENGTH_SHORT).show();
            }*/

        }

    }

}
