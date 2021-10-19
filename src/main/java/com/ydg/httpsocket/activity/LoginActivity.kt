package com.ydg.httpsocket.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.edit
import com.google.gson.Gson
import com.ydg.httpsocket.R
import com.ydg.httpsocket.databinding.ActivityLoginBinding
import com.ydg.httpsocket.domain.ResultCode
import com.ydg.httpsocket.domain.ResultVO
import com.ydg.httpsocket.domain.User
import com.ydg.httpsocket.receiver.NetWorkBroadcastReceiver
import com.ydg.httpsocket.receiver.ServerErrBroadCastReceiver
import com.ydg.httpsocket.service.HttpRequestService
import com.ydg.httpsocket.service.MqttService
import com.ydg.httpsocket.utils.MethodUtil
import com.ydg.httpsocket.utils.ServiceCreator
import com.ydg.httpsocket.utils.SoftHideKeyBoardUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private lateinit var bind : ActivityLoginBinding
    private lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityLoginBinding.inflate(layoutInflater)
        bind.loginPage.setBackgroundResource(R.drawable.animation_bg)
        val drawable : AnimationDrawable = bind.loginPage.background as AnimationDrawable
        drawable.start()
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        //登录按钮点击事件
        bind.login.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(bind.username.windowToken,0)
            bind.loginPro.visibility = View.VISIBLE
            if (MethodUtil.isConnectIsNomarl(this)){ //判断网络状态
                val username : String = bind.username.text.toString()
                val password : String = bind.password.text.toString()
                var user = User(username,password,"dasdas".toByteArray(),null,"")
                var request = ServiceCreator.create<HttpRequestService>()
                val act : LoginActivity = this
                //向服务器进行用户认证
                request.checkUser(user).enqueue(object : Callback<ResultVO<User>>{
                    override fun onResponse(
                        call: Call<ResultVO<User>>,
                        response: Response<ResultVO<User>>
                    ) {
                        var result = (response.body() ?: ResultVO(ResultCode.FAILURE,null)) as ResultVO<User>
                        if (result.code == ResultCode.SUCCESS.code){
                            Log.i("loginActivity",result.data.toString())
                            prefs = getSharedPreferences("loginedUser",Context.MODE_PRIVATE)
                            prefs.edit {
                                putString("username",result.data!!.username)
                                putString("perSign",result.data!!.perSign)
                                putString("headIcon",Gson().toJson(result.data!!.headIcon))
                            }
                            var intent = Intent(act, IndexActivity::class.java)
                            intent.putExtra("username",result.data!!.username)
                            intent.putExtra("headIcon",result.data!!.headIcon)
                            intent.putExtra("headIcon",result.data!!.perSign)
                            startActivity(intent)
                            finish()
                        }else if(result.code == ResultCode.VALIDATE_FAILURE.code){
                            bind.loginPro.visibility = View.INVISIBLE
                            Toast.makeText(act,result.data as String,Toast.LENGTH_SHORT).show()
                        }else{
                            bind.loginPro.visibility = View.INVISIBLE
                            Toast.makeText(act,result.msg,Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultVO<User>>, t: Throwable) {
                        bind.loginPro.visibility = View.INVISIBLE
                        var intent = Intent("com.ydg.ServerErrBroadcastReceiver")
                        sendBroadcast(intent)
                    }
                })
            }else{
                bind.loginPro.visibility = View.INVISIBLE
                var intent = Intent("com.ydg.NetWorkBroadcastReceiver")
                sendBroadcast(intent)
            }
        }
        bind.apply {
            username.setOnFocusChangeListener { view, b ->
                if (b){
                    bind.username.hint = ""
                }else{
                    bind.username.hint = "输入账号"
                }
            }
            password.setOnFocusChangeListener { view, b ->
                if (b){
                    bind.password.hint = ""
                }else{
                    bind.password.hint = "输入密码"
                }
            }
            clearUsernameText.setOnClickListener {
                bind.username.setText("")
            }
            clearPasswordText.setOnClickListener {
                bind.password.setText("")
            }
        }
        bind.userRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }



    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}