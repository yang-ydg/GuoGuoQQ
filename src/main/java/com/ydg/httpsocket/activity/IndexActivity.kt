package com.ydg.httpsocket.activity

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.Window
import com.ydg.httpsocket.databinding.ActivityIndexBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ydg.httpsocket.R
import com.ydg.httpsocket.fragment.fragment1
import com.ydg.httpsocket.fragment.fragment2
import com.ydg.httpsocket.fragment.fragment3
import com.ydg.httpsocket.fragment.fragment4
import com.ydg.httpsocket.service.MqttService
import com.ydg.httpsocket.utils.StatusBarUtil
import kotlin.system.exitProcess
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import com.ydg.httpsocket.domain.User
import com.ydg.httpsocket.receiver.NetWorkBroadcastReceiver
import com.ydg.httpsocket.utils.LogUtil


class IndexActivity : BaseActivity(), View.OnClickListener {
    companion object{
        var service : MqttService? = null
    }
    lateinit var bind: ActivityIndexBinding
    lateinit var fragments : ArrayList<Fragment>
    lateinit var user:User
    var frag1 : fragment1 = fragment1()
    var frag2 : fragment2 = fragment2()
    var frag3 : fragment3 = fragment3()
    var frag4 : fragment4 = fragment4()
    private var actives : Array<Boolean> = arrayOf(true,false,false,false)
    private lateinit var adapter: FragmentPagerAdapter
    private var conn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            var myBinder = p1 as MqttService.Mybinder
            service = myBinder.service
            service!!.act = this@IndexActivity
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val username = intent.getStringExtra("username").toString()
        val perSign = intent.getStringExtra("perSign").toString()
        val headIcon = intent.getByteArrayExtra("headIcon")
        user = User(username,"",headIcon,perSign,username)
        MqttService.clientId = username

        //启动mqtt服务
        var intent1 = Intent(this, MqttService::class.java)
        bindService(intent1,conn, Context.BIND_AUTO_CREATE)

        //初始化view
        bind = ActivityIndexBinding.inflate(layoutInflater)
        val headView = bind.navView.getHeaderView(0)
        headView.findViewById<TextView>(R.id.username).text = username
        headView.findViewById<TextView>(R.id.perSign).text = perSign
        val headiconView : ImageView = headView.findViewById(R.id.drawHeadIcon)
        headiconView.setImageBitmap(BitmapFactory.decodeByteArray(headIcon,0,headIcon!!.size))
        headiconView.setOnClickListener {
            val intent = Intent(this,PersonCardActivity::class.java)
            intent.putExtra("username",username)
            intent.putExtra("headIcon",headIcon)
            intent.putExtra("perSign",perSign)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        //滑动切换界面
        initFragmentList()
        adapter = FragmentAdapter(this.supportFragmentManager,this)
        bind.viewPager.adapter = adapter
        bind.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {
                if (state == 2){
                    if (bind.viewPager.currentItem ==2){
                        StatusBarUtil.setStatusBarColor(this@IndexActivity,false)
                    }else{
                        StatusBarUtil.setStatusBarColor(this@IndexActivity,true)
                    }
                    switchActive(bind.viewPager.currentItem)
                }
            }
        })

        //绑定bottomToolBarItem的点击事件
        bind.contactsFrag.setOnClickListener(this)
        bind.dynamicFrag.setOnClickListener(this)
        bind.msgFrag.setOnClickListener(this)
        bind.watchFrag.setOnClickListener(this)
    }

    //初始化界面列表
    private fun initFragmentList(){
        fragments = ArrayList()
        fragments.add(frag1)
        fragments.add(frag2)
        fragments.add(frag3)
        fragments.add(frag4)
    }

    //全局点击监听方法
    override fun onClick(p0: View) {
        when(p0.id){
            bind.contactsFrag.id -> switchActive(1)
            bind.msgFrag.id -> switchActive(0)
            bind.dynamicFrag.id -> switchActive(3)
            bind.watchFrag.id -> switchActive(2)
        }
    }

    //bottomToolBar切换
    private fun switchActive(k:Int){
        if (!actives[k]){
            if (k == 2){
                StatusBarUtil.setStatusBarColor(this,false)
            }else{
                StatusBarUtil.setStatusBarColor(this,true)
            }
            bind.viewPager.currentItem = k
            var tag = 0
            for (i in 0 until actives.size){
                if (actives[i]){
                    tag = i
                }
                actives[i] = false
            }
            actives[k] = true
            when(tag){
                0 -> {
                    bind.msgImage.setImageResource(R.drawable.msg)
                    bind.msgText.setTextColor(this.resources.getColor(R.color.FragItemTextColor))
                }
                1 -> {
                    bind.contactsImage.setImageResource(R.drawable.contacts)
                    bind.contactsText.setTextColor(this.resources.getColor(R.color.FragItemTextColor))
                }
                2 -> {
                    bind.watchImage.setImageResource(R.drawable.watch)
                    bind.watchText.setTextColor(this.resources.getColor(R.color.FragItemTextColor))
                }
                3 -> {
                    bind.dynamicImage.setImageResource(R.drawable.dynamic)
                    bind.dynamicText.setTextColor(this.resources.getColor(R.color.FragItemTextColor))
                }
            }
            when(k){
                0 -> {
                    bind.msgImage.setImageResource(R.drawable.msg_active)
                    bind.msgText.setTextColor(this.resources.getColor(R.color.FragItemActiveTextColor))
                }
                1 -> {
                    bind.contactsImage.setImageResource(R.drawable.contacts_active)
                    bind.contactsText.setTextColor(this.resources.getColor(R.color.FragItemActiveTextColor))
                }
                2 -> {
                    bind.watchImage.setImageResource(R.drawable.watch_active)
                    bind.watchText.setTextColor(this.resources.getColor(R.color.FragItemActiveTextColor))
                }
                3 -> {
                    bind.dynamicImage.setImageResource(R.drawable.dynamic_active)
                    bind.dynamicText.setTextColor(this.resources.getColor(R.color.FragItemActiveTextColor))
                }
            }
        }
    }

    inner class FragmentAdapter(val fm : FragmentManager,val activity: IndexActivity) : FragmentPagerAdapter(fm){
        override fun getCount(): Int {
            return fragments.size
        }
        override fun getItem(position: Int): Fragment {
            return fragments[position]
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
        unbindService(conn)
        LogUtil.i("fragment1","activityOnDestroy")
        super.onDestroy()
    }

}