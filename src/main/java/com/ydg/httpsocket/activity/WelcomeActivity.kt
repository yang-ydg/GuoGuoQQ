package com.ydg.httpsocket.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydg.httpsocket.databinding.ActivityWelcomeBinding
import com.ydg.httpsocket.receiver.NetWorkBroadcastReceiver
import com.ydg.httpsocket.service.MqttService
import java.util.*

class WelcomeActivity : AppCompatActivity() {
    private lateinit var bind : ActivityWelcomeBinding
    private lateinit var prefs : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityWelcomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        prefs = getSharedPreferences("loginedUser",Context.MODE_PRIVATE)
        val username = prefs.getString("username","")
        val perSign = prefs.getString("perSign","")
        val headIcon = Gson().fromJson<ByteArray>(prefs.getString("headIcon",""),object : TypeToken<ByteArray>(){}.type)
        val intent : Intent
        if (username == "" || username == null){
            intent = Intent(this,LoginActivity::class.java)
        }else{
            intent = Intent(this,IndexActivity::class.java)
            intent.putExtra("username",username)
            intent.putExtra("headIcon",headIcon)
            intent.putExtra("perSign",perSign)
        }
        val timer = Timer()
        val task = object : TimerTask(){
            override fun run() {
                startActivity(intent)
                finish()
            }

        }
        timer.schedule(task,1000)
    }

    override fun onStart() {

        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}