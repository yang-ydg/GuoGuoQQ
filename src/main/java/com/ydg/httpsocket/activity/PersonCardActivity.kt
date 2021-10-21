package com.ydg.httpsocket.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ydg.httpsocket.R
import com.ydg.httpsocket.components.MyNestedScrollView
import com.ydg.httpsocket.databinding.ActivityPersonCardBinding

class PersonCardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bind : ActivityPersonCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        bind = ActivityPersonCardBinding.inflate(layoutInflater)

        val username = intent.getStringExtra("username").toString()
        val perSign = intent.getStringExtra("perSign").toString()
        val headIcon = intent.getByteArrayExtra("headIcon")

        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.cardToolBar.background.mutate().alpha = 0
        bind.cardContent.setScanScrollChangedListener(object : MyNestedScrollView.ISmartScrollChangedListener{
            override fun onScrolledToBottom() {}
            override fun onScrolling() {
                bind.cardToolBar.background.mutate().alpha = 255
                bind.cardLabel.visibility = View.VISIBLE
            }
            override fun onScrolledToTop(y:Int) {
                bind.cardToolBar.background.mutate().alpha = y*255/85 as Int
                bind.cardLabel.visibility = View.INVISIBLE
            }

        })
        bind.cardBack.setOnClickListener(this)
        bind.btn1.setOnClickListener(this)
        bind.btn2.setOnClickListener(this)
        bind.btn3.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            bind.cardBack.id -> finish()
        }
    }
}