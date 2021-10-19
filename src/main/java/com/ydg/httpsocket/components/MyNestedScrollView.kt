package com.ydg.httpsocket.components

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

class MyNestedScrollView(context: Context,attributeSet: AttributeSet) : NestedScrollView(context,attributeSet) {
    private var isScrolledToTop = true
    private var isScrolledToBottom = false
    private var mSmartScrollChangedListener:ISmartScrollChangedListener? = null

    /** 定义监听接口 */
    interface ISmartScrollChangedListener {
        fun onScrolledToBottom()
        fun onScrolling()
        fun onScrolledToTop(y:Int)
    }

    fun setScanScrollChangedListener(smartScrollChangedListener:ISmartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
            if (getScrollY() <= 85) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(
                    0
                ).getHeight()
            ) {
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
            notifyScrollChangedListeners(t);
        }
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
            super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
            if (scrollY <= 85) {
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = clampedY;
            }
            notifyScrollChangedListeners(scrollY);
    }

    fun notifyScrollChangedListeners(y:Int) {
        if (isScrolledToTop) {
                mSmartScrollChangedListener?.onScrolledToTop(y);
        } else if (isScrolledToBottom) {
            mSmartScrollChangedListener?.onScrolledToBottom();
        }else{
            mSmartScrollChangedListener?.onScrolling()
        }
    }

    fun isScrolledToTop() : Boolean{
        return isScrolledToTop;
    }

    fun isScrolledToBottom() : Boolean{
        return isScrolledToBottom;
    }
}