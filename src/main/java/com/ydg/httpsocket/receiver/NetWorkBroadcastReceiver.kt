package com.ydg.httpsocket.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

//网络断线提醒
class NetWorkBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0,"网络连接不可用", Toast.LENGTH_LONG).show()
    }
}