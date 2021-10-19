package com.ydg.httpsocket.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ydg.httpsocket.R
import com.ydg.httpsocket.databinding.ActivityRegisterBinding
import com.ydg.httpsocket.domain.ResultCode
import com.ydg.httpsocket.domain.ResultVO
import com.ydg.httpsocket.domain.User
import com.ydg.httpsocket.service.HttpRequestService
import com.ydg.httpsocket.utils.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class RegisterActivity : BaseActivity() {
    private lateinit var bind : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.back.setOnClickListener { finish() }

        bind.apply {
            username.setOnFocusChangeListener { view, b ->
                if (b){
                    bind.username.hint = ""
                }else{
                    bind.username.hint = "新账号"
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

        bind.register.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(bind.username.windowToken,0)
            val username = bind.username.text.toString()
            val password = bind.password.text.toString()
            val act = this
            if (username.length == 0){
                Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show()
            }else if (username.length > 6){
                Toast.makeText(this,"用户名不能超过6位字符",Toast.LENGTH_SHORT).show()
            }else if (password.length > 16 && password.length < 6){
                Toast.makeText(this,"密码长度为6-16位字符",Toast.LENGTH_SHORT).show()
            }else{
                val request = ServiceCreator.create<HttpRequestService>()
                val headIcon : ByteArray = getDefaultIcon()
                val user = User(username,password,headIcon,null,username)
                bind.registerPro.visibility = View.VISIBLE
                request.registerUser(user).enqueue(object : Callback<ResultVO<Any?>>{
                    override fun onResponse(
                        call: Call<ResultVO<Any?>>,
                        response: Response<ResultVO<Any?>>
                    ) {
                        val result = response.body() as ResultVO<Any?>
                        if (result.code == ResultCode.SUCCESS.code){
                            val intent = Intent(act,IndexActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.putExtra("username",user.username)
                            intent.putExtra("headIcon",user.headIcon)
                            intent.putExtra("perSign","这个人很懒，什么都没留下...")
                            startActivity(intent)
                        }else{
                            Toast.makeText(act,result.msg,Toast.LENGTH_SHORT).show()
                            bind.registerPro.visibility = View.INVISIBLE
                        }
                    }
                    override fun onFailure(call: Call<ResultVO<Any?>>, t: Throwable) {
                        bind.registerPro.visibility = View.INVISIBLE
                        Log.i("retrofitErr:",t.message!!)
                        var intent = Intent("com.ydg.ServerErrBroadcastReceiver")
                        sendBroadcast(intent)
                    }

                })
            }


        }
    }
    private fun getDefaultIcon():ByteArray{
        val random = Random.nextInt(6)+1
        val out = ByteArrayOutputStream()
        val bitmap = BitmapFactory.decodeResource(resources,resources.getIdentifier("default_icon_"+random,"drawable",this.packageName))!!
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
        out.flush()
        out.close()
        return out.toByteArray()
    }
}