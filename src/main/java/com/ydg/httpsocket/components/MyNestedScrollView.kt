package com.ydg.httpsocket.components

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.TranslateAnimation
import androidx.core.widget.NestedScrollView

class MyNestedScrollView(context: Context,attributeSet: AttributeSet) : NestedScrollView(context,attributeSet) {
    private var isScrolledToTop = true
    private var isScrolledToBottom = false
    private var mSmartScrollChangedListener:ISmartScrollChangedListener? = null

    //弹性参数
    private var mInnerView: View? = null //孩子View
    private var mDownY = 0f // 点击时y坐标
    private val mRect = Rect()
    private var offset = 0
    private var isCount = false // 是否开始计算
    private var mWidth = 0
    private var mHeight = 0

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

    //弹性函数
    override fun onFinishInflate() {
        super.onFinishInflate()
        //获取的就是 scrollview 的第一个子 View
        if (getChildCount() > 0) {
            mInnerView = getChildAt(0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        val lp: MarginLayoutParams = mInnerView!!.layoutParams as MarginLayoutParams
        //减去 margin 的值
        offset = mInnerView!!.measuredHeight - lp.topMargin - lp.bottomMargin - mHeight
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (mInnerView != null) {
            commOnTouchEvent(e)
        }
        return super.onTouchEvent(e)
    }

    fun commOnTouchEvent(e: MotionEvent) {
        when (e.getAction()) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
                val preY = mDownY // 按下时的y坐标
                val nowY: Float = e.getY() // 时时y坐标
                var deltaY = (preY - nowY).toInt() // 滑动距离
                //排除出第一次移动计算无法得知y坐标
                if (!isCount) {
                    deltaY = 0
                }
                mDownY = nowY
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove) {
                    if (mRect.isEmpty) {
                        // 保存正常的布局位置
                        mRect[mInnerView!!.left, mInnerView!!.top, mInnerView!!.right] =
                            mInnerView!!.bottom
                    }
                    // 移动布局
                    mInnerView!!.layout(
                        mInnerView!!.left, mInnerView!!.top - deltaY / 2,
                        mInnerView!!.right, mInnerView!!.bottom - deltaY / 2
                    )
                }
                isCount = true
            }
            MotionEvent.ACTION_UP -> if (isNeedAnimation) {
                translateAnimator()
                isCount = false
            }
        }
    }

    private fun translateAnimator() {
        val animation = TranslateAnimation(0F, 0F, mInnerView!!.top.toFloat(), mRect.top.toFloat())
        animation.setDuration(200)
        animation.setInterpolator(AnticipateInterpolator(3f))
        mInnerView!!.startAnimation(animation)
        // 设置回到正常的布局位置
        mInnerView!!.layout(mRect.left, mRect.top, mRect.right, mRect.bottom)
        mRect.setEmpty()
    }

    // 是否需要开启动画
    val isNeedAnimation: Boolean
        get() = !mRect.isEmpty// 0是顶部，offset是底部

    // 判断是否处于顶部或者底部
    val isNeedMove: Boolean
        get() =// 0是顶部，offset是底部
            if (getScrollY() == 0 || getScrollY() >= offset) {
                true
            } else false

}