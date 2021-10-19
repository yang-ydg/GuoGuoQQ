package com.ydg.httpsocket.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydg.httpsocket.activity.IndexActivity
import com.ydg.httpsocket.domain.Msg
import com.ydg.httpsocket.domain.User
import com.ydg.httpsocket.fragment.fragment2
import com.ydg.httpsocket.utils.MethodUtil
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MqttService() : Service() {
    companion object{
        private val  topic = "client:"
        var client: MqttAndroidClient? = null
        var clientId = ""
        val TAG: String = MqttService::class.java.simpleName
        private var scheduler: ScheduledExecutorService? = null
    }
    lateinit var act : IndexActivity
    private var conOpt : MqttConnectOptions ?= null
    private val username = "ydg"
    private val password = "yang5537."
    private val host = "tcp://119.29.163.199:1883"
    private  val gson : Gson = Gson()

    override fun onCreate() {
        Log.e(TAG,"OnCreate")
        init()
        super.onCreate()
    }

    //初始化
    private fun init(){
        //服务器地址（协议+地址+端口号）
        Log.e(TAG,"clientId:" + clientId)
        client = MqttAndroidClient(this,host, clientId)
        //设置MQTT监听并且接受消息
        client!!.setCallback(mqttCallback)
        //构建连接参数
        conOpt = MqttConnectOptions()
        conOpt!!.let {
            it.isCleanSession = false
            it.keepAliveInterval = 90
            it.isAutomaticReconnect = true
            it.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
            it.connectionTimeout = 20
            it.userName = username
            it.password = password.toCharArray()
        }
        var doConnet = true
        //设置连接结束后的遗嘱
        val message = "{client$clientId terminal!}"
        val topic = "last"
        val qos = 0
        val retained = false
        if ((!message.equals("")) || (!topic.equals(""))){
            try {
                conOpt!!.setWill(topic,message.toByteArray(),qos,retained)
            }catch (e : Exception){
                Log.e(TAG,"Will Exception Occured!",e)
                doConnet = false
                iMqttActionListener.onFailure(null,e)
            }
        }
        if(doConnet){
            doClientConnection()
        }

    }

    //连接时监听
    private val iMqttActionListener : IMqttActionListener = object : IMqttActionListener{
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            /*
                订阅消息
             */
            if (act.frag2.friendsList != null){
                for(i in act.frag2.friendsList){
                    client?.subscribe("Msg-ps"+i.clientId+"xxxxx"+ clientId,1)
                    Log.i("subscribe&publish",topic+i.clientId+"|to|"+ clientId)
                }
            }
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            exception?.printStackTrace()
            reConnect()
            Log.i(TAG,"onFailure:=====连接失败,重连=====")
        }
    }

    //连接成功后监听MQTT
    private val mqttCallback : MqttCallback = object : MqttCallback{
        override fun connectionLost(cause: Throwable?) {
            //失去连接
            Log.i(TAG,"connectionLost:====重新注册客户端====")
            var intent = Intent("com.ydg.NetWorkBroadcastReceiver")
            sendBroadcast(intent)
            doClientConnection()
        }

        override fun deliveryComplete(token: IMqttDeliveryToken) {
            try {
                Log.i(TAG, "deliveryComplete:" + token.message)
            } catch (e: Exception) {
                Log.e(TAG, "deliveryComplete e======" + e.message)
            }
        }

        override fun messageArrived(topic: String?, message: MqttMessage) {
            if (topic!!.substring(0,6).equals("Msg-ps")) {
                val msgJson = String(message.payload)
                val msg = gson.fromJson<Msg>(msgJson,object :TypeToken<Msg>(){}.type)
                msg.type = Msg.TYPE_RECEIVED
                Log.i("subscribe&publish2", topic?:"")
                val prefs = getSharedPreferences(msg.toClientId+"&"+msg.clientId,Context.MODE_PRIVATE)
                val listString: String? = prefs.getString(msg.toClientId+"|||"+msg.clientId,"")
                val msgList : ArrayList<Msg>
                if (listString != "" && listString !=null){
                     msgList = gson.fromJson<ArrayList<Msg>>(listString,object : TypeToken<ArrayList<Msg>>(){}.type)
                }else msgList = ArrayList()
                msgList.add(msg)
                prefs.edit {
                    putString(msg.toClientId+"|||"+msg.clientId,gson.toJson(msgList))
                    apply()
                }
                var intent = Intent("com.ydg.myBroadcastReceiver")
                intent.putExtra("listJson",gson.toJson(msgList))
                sendBroadcast(intent)
            }
        }
    }

    //注册客户端
    private fun  doClientConnection(){
        if(MethodUtil.isConnectIsNomarl(this)){
            if (!client!!.isConnected){
                try {
                    Log.i(TAG,"开始连接")
                    client!!.connect(conOpt,null,iMqttActionListener)
                }catch (e : Exception){
                    Log.e(TAG,"doClientConnection e!")
                }
            }
        }else{
            sendBroadcast(Intent("com.ydg.NetWorkBroadcastReceiver"))
            client!!.connect(conOpt,null,iMqttActionListener)
        }

    }


    //连接失败进行重新连接
    private fun reConnect(){
        scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler!!.schedule(Runnable {
            if (null != client && !client!!.isConnected){
                Log.e(TAG,"reconnect=============")
                try {
                    client!!.connect(conOpt,null,iMqttActionListener)
                }catch (e:Exception){
                    Log.e(TAG,"client.connect() e=============")
                }
            }
        },1000,TimeUnit.MILLISECONDS)
    }

    override fun onBind(intent: Intent): IBinder? {
        return Mybinder()
    }

    override fun onDestroy() {
        Log.e(TAG,"MQTTService====onDestroy()")
        stopSelf()
        try {
            if (client == null)
                return
            client!!.disconnect()
            client!!.unregisterResources()
            client!!.close()
        }catch (e : Exception){
            Log.e(TAG,"onDestroy()=e======" + e.message)
        }
        super.onDestroy()
    }

    //发布消息
    fun publish(msg: Msg){
        try {
            if (client != null){
                //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
                client!!.publish("Msg-ps"+ msg.clientId+"xxxxx"+msg.toClientId,gson.toJson(msg).toByteArray(),1,false)
                Log.i("subscribe&publish1",topic+ msg.clientId+"|to|"+msg.toClientId)
                val prefs = getSharedPreferences(msg.clientId+"&"+msg.toClientId,Context.MODE_PRIVATE)
                val listString: String? = prefs.getString(msg.clientId+"|||"+msg.toClientId,"")
                val msgList : ArrayList<Msg>
                if (listString != "" && listString !=null){
                    msgList = gson.fromJson<ArrayList<Msg>>(listString,object : TypeToken<ArrayList<Msg>>(){}.type)
                }else msgList = ArrayList()
                msgList.add(msg)
                prefs.edit {
                    putString(msg.clientId+"|||"+msg.toClientId,gson.toJson(msgList))
                    apply()
                }
                var intent = Intent("com.ydg.myBroadcastReceiver")
                intent.putExtra("listJson",gson.toJson(msgList))
                sendBroadcast(intent)
            }
        }catch (e : Exception){
            Log.e(TAG,"MqttException====="+e.message)
        }
    }

    inner class Mybinder : Binder(){
        val service : MqttService
        get() = this@MqttService
    }
}