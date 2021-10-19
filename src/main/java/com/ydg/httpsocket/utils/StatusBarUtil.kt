package com.ydg.httpsocket.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

object StatusBarUtil {
            fun setStatusBarColor(activity: Activity, dark: Boolean) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val window = activity.window
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    if (dark) {
                        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                    } else {
                        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    }
                }
            }

}