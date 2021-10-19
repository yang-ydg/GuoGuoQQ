package com.ydg.httpsocket.activity

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ydg.httpsocket.receiver.NetWorkBroadcastReceiver
import com.ydg.httpsocket.receiver.ServerErrBroadCastReceiver

open class BaseActivity : AppCompatActivity() {
    private lateinit var netWorkBroadcastReceiver: NetWorkBroadcastReceiver
    private lateinit var serverErrBroadCastReceiver: ServerErrBroadCastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        //注册网络状态广播
        val intentFilter1 = IntentFilter()
        intentFilter1.addAction("com.ydg.NetWorkBroadcastReceiver")
        netWorkBroadcastReceiver = NetWorkBroadcastReceiver()
        registerReceiver(netWorkBroadcastReceiver, intentFilter1)

        //注册服务器错误广播
        val intentFilter2 = IntentFilter()
        intentFilter2.addAction("com.ydg.ServerErrBroadcastReceiver")
        serverErrBroadCastReceiver = ServerErrBroadCastReceiver()
        registerReceiver(serverErrBroadCastReceiver, intentFilter2)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        unregisterReceiver(netWorkBroadcastReceiver)
        unregisterReceiver(serverErrBroadCastReceiver)
        super.onDestroy()
    }
}