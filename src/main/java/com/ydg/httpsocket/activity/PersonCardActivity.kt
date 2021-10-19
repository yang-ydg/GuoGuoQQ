package com.ydg.httpsocket.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ydg.httpsocket.R
import com.ydg.httpsocket.components.MyNestedScrollView
import com.ydg.httpsocket.databinding.ActivityPersonCardBinding

class PersonCardActivity : AppCompatActivity() {
    private lateinit var bind : ActivityPersonCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        bind = ActivityPersonCardBinding.inflate(layoutInflater)
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
        bind.cardBack.setOnClickListener {
            finish()
        }
    }
}