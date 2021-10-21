package com.ydg.httpsocket.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        context = applicationContext
        super.onCreate()
    }
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context
    }
}