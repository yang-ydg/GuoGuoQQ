package com.ydg.httpsocket.utils

import android.util.Log
import java.lang.Exception

object LogUtil {
    private const val VERBOSE = 0
    private const val DEBUG = 1
    private const val INFO = 2
    private const val WARN = 3
    private const val ERROR = 4
    private const val level = VERBOSE

    fun v(tag:String,msg:String){
        if (level <= VERBOSE){
            Log.v(tag,msg)
        }
    }
    fun d(tag:String,msg:String){
        if (level <= DEBUG){
            Log.d(tag,msg)
        }
    }
    fun i(tag:String,msg:String){
        if (level <= INFO){
            Log.v(tag,msg)
        }
    }
    fun w(tag:String,msg:String){
        if (level <= WARN){
            Log.v(tag,msg)
        }
    }
    fun e(tag:String,msg:String,e : Exception? = null){
        if (level <= ERROR){
            Log.v(tag,msg,e)
        }
    }
}