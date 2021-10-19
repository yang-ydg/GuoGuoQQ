package com.ydg.httpsocket.components

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.ydg.httpsocket.activity.LoginActivity
import com.ydg.httpsocket.databinding.PopupLayoutBinding


class Mypopup(val context: FragmentActivity,val binding: PopupLayoutBinding,width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int= ViewGroup.LayoutParams.WRAP_CONTENT) :
    PopupWindow(context),View.OnClickListener{
    init {
        setWidth(width)
        setHeight(height)
        isFocusable = true
        isOutsideTouchable = true
        isTouchable = true
        contentView = binding.root
        setBackgroundDrawable(BitmapDrawable())
        setOnDismissListener {
            switchBackgroud(1f)
        }
        binding.groupChat.setOnClickListener(this)
        binding.collectOrPay.setOnClickListener(this)
        binding.addFriends.setOnClickListener(this)
        binding.scan.setOnClickListener(this)
        binding.faceToFaceLoad.setOnClickListener(this)
        binding.cancellation.setOnClickListener(this)
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        super.showAsDropDown(anchor, xoff, yoff)
        switchBackgroud(0.75f)
    }

    //加背景蒙版
    private fun switchBackgroud(bgcolor:Float){
        var lp : WindowManager.LayoutParams = context.window.attributes
        lp.alpha = bgcolor
        context.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.window.setAttributes(lp);
    }

    override fun onClick(p0: View) {
        when(p0.id){
            binding.cancellation.id ->{
                val prefs = context.getSharedPreferences("loginedUser",Context.MODE_PRIVATE)
                prefs.edit {
                    putString("username","")
                    putString("headIcon","")
                }
                context.startActivity(Intent(context,LoginActivity::class.java))
                context.finish()
            }
        }
    }

    //检测手指释放点是否在控件内
//    private fun isContain(view:View,x:Float,y:Float) : Boolean
//    {
//        var point = IntArray(2)
//        view.getLocationOnScreen(point);
//        if(x >= point[0] && x <= (point[0]+view.width) && y >= point[1] && y <= (point[1]+view.height))
//        {
//            return true;
//        }
//        return false;
//    }
}