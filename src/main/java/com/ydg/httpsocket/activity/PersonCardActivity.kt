package com.ydg.httpsocket.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ydg.httpsocket.R
import com.ydg.httpsocket.components.MyNestedScrollView
import com.ydg.httpsocket.databinding.ActivityPersonCardBinding
import com.ydg.httpsocket.domain.CardSelectedAdapter
import java.io.ByteArrayOutputStream

class PersonCardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bind : ActivityPersonCardBinding
    private var selectedList = ArrayList<ByteArray>()
    private lateinit var adapter:CardSelectedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        bind = ActivityPersonCardBinding.inflate(layoutInflater)
        initSelectedList()

        val username = intent.getStringExtra("username").toString()
        val perSign = intent.getStringExtra("perSign").toString()
        val headIcon = intent.getByteArrayExtra("headIcon")

        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        adapter = CardSelectedAdapter(selectedList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        bind.cardSelectedRecycler.layoutManager = layoutManager
        bind.cardSelectedRecycler.adapter = adapter

        bind.cardHeadIcon.setImageBitmap(BitmapFactory.decodeByteArray(headIcon,0,headIcon!!.size))
        bind.cardUsername.text = username
        bind.cardClientId.text = username
        bind.cardPerSign.text = perSign
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
        bind.cardDetailRow.setOnClickListener(this)
        bind.cardLevelRow.setOnClickListener(this)
        bind.cardPerSignRow.setOnClickListener(this)
        bind.cardPrivilegeRow.setOnClickListener(this)
        bind.cardSpaceRow.setOnClickListener(this)
        bind.cardPerTagRow.setOnClickListener(this)
        bind.cardExtendsRow.setOnClickListener(this)
        bind.cardAnonymousRow.setOnClickListener(this)
    }

    private fun initSelectedList(){
        for(i in 1 until 7){
            val out = ByteArrayOutputStream()
            val bitmap = BitmapFactory.decodeResource(resources,resources.getIdentifier("default_icon_"+i,"drawable",this.packageName))!!
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.flush()
            out.close()
            selectedList.add(out.toByteArray())
        }
    }
    override fun onClick(p0: View) {
        when(p0.id){
            bind.cardBack.id -> finish()
        }
    }
}