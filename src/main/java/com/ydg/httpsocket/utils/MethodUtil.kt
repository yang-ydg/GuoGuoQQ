package com.ydg.httpsocket.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object  MethodUtil {
    //判断网络是否可用
     fun isConnectIsNomarl(context: Context) : Boolean {
        val connectivityManager: ConnectivityManager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info : NetworkInfo? = connectivityManager.activeNetworkInfo
        if (info != null && info.isAvailable){
            return true
        }else{
            return false
        }
    }
}