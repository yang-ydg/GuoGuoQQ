package com.ydg.httpsocket.activity

import android.R.attr
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydg.httpsocket.databinding.ActivityChatBinding
import com.ydg.httpsocket.domain.Msg
import com.ydg.httpsocket.domain.MsgAdapter
import com.ydg.httpsocket.receiver.NetWorkBroadcastReceiver

import com.ydg.httpsocket.service.MqttService
import com.ydg.httpsocket.utils.SoftHideKeyBoardUtil
import android.widget.Toast

import android.R.attr.keyHeight
import android.graphics.Color
import android.widget.ScrollView
import android.view.MotionEvent
import android.view.View.OnTouchListener


class ChatActivity : AppCompatActivity(){
    private var msgList = ArrayList<Msg>()
    private lateinit var adapter: MsgAdapter
    private lateinit var bind : ActivityChatBinding
    private lateinit var myBroadcastReceiver: MyBroadcastReceiver
    private lateinit var netWorkBroadcastReceiver: NetWorkBroadcastReceiver
    private  val TAG = ChatActivity::class.java.simpleName
    private lateinit var  prefs : SharedPreferences
    private  var toClientId : String? = null
    private  var clientId : String? = null
    private var chatName : String? = null
    private var friendheadicon : ByteArray? = null
    private var myheadicon : ByteArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG,"onCreate()===========")
        toClientId = intent.getStringExtra("toClientId").toString()
        chatName = intent.getStringExtra("chatName").toString()
        friendheadicon = intent.getByteArrayExtra("friendHeadIcon")
        myheadicon = intent.getByteArrayExtra("myHeadIcon")
        clientId = MqttService.clientId
        prefs = getSharedPreferences(clientId+"&"+toClientId,Context.MODE_PRIVATE)
        bind = ActivityChatBinding.inflate(layoutInflater)
        bind.chatName.text = chatName
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        SoftHideKeyBoardUtil.assistActivity(this)
        initList()

        //构建recyclerView
        val layoutManager = LinearLayoutManager(this)
        bind.recyclerView.layoutManager = layoutManager
        if (!::adapter.isInitialized){
            adapter = MsgAdapter(msgList,friendheadicon!!,myheadicon!!)
        }
        bind.recyclerView.adapter = adapter
        bind.recyclerView.scrollToPosition(msgList.size - 1)
        bind.contentScroll.post{
            bind.contentScroll.fullScroll(ScrollView.FOCUS_DOWN)
        }
        bind.contentScroll.setOnTouchListener { arg0, arg1 ->
            true
        }

        //注册更新ui广播
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.ydg.myBroadcastReceiver")
        myBroadcastReceiver = MyBroadcastReceiver()
        registerReceiver(myBroadcastReceiver, intentFilter)

        //注册网络状态广播
        val intentFilter1 = IntentFilter()
        intentFilter1.addAction("com.ydg.NetWorkBroadcastReceiver")
        netWorkBroadcastReceiver = NetWorkBroadcastReceiver()
        registerReceiver(netWorkBroadcastReceiver, intentFilter1)

        bind.chatBack.setOnClickListener {
            finish()
        }

        bind.inputText.setOnClickListener {
            bind.recyclerView.scrollToPosition(msgList.size - 1)
        }

        bind.send.setOnClickListener {
            Log.v(TAG,"publishMsgButtonCLick===========")
            val msgContent = bind.inputText.text.toString()
            bind.inputText.setText("")
            val msg = Msg(clientId!!,toClientId!!,msgContent,Msg.TYPE_SENT)
            //发送消息
            IndexActivity.service?.publish(msg)
        }
    }

    //初始化聊天记录list
    fun initList(){
        val listString: String? = prefs.getString(clientId+"|||"+toClientId,"")
        val gson : Gson = Gson()
        if (listString != "" && listString !=null){
            msgList = gson.fromJson(listString,object : TypeToken<ArrayList<Msg>>(){}.type)
        }
    }

    //将聊天记录列表写入记录文件
//    fun writeInFile(){
//        val gson : Gson = Gson()
//        val listString : String = gson.toJson(msgList)
//        prefs.edit {
//            putString(clientId+"|||"+toClientId,listString)
//            apply()
//        }
//    }

    override fun onDestroy() {
        Log.v(TAG,"onDestroy()===========")
        unregisterReceiver(myBroadcastReceiver)
        unregisterReceiver(netWorkBroadcastReceiver)
        super.onDestroy()
    }

    //消息更新广播
    inner class MyBroadcastReceiver : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent) {
            val listJson : String = p1.getStringExtra("listJson").toString()
            val gson = Gson()
            val list = gson.fromJson<ArrayList<Msg>>(listJson,object : TypeToken<ArrayList<Msg>>(){}.type)
            for (i in msgList.size until list.size){
                msgList.add(list[i])
            }
            adapter.notifyDataSetChanged()
            bind.recyclerView.scrollToPosition(msgList.size - 1)  // 将 RecyclerView定位到最后一行
        }
    }

    override fun onBackPressed() {
        finish()
    }


}