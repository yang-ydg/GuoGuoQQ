package com.ydg.httpsocket.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ydg.httpsocket.activity.IndexActivity
import com.ydg.httpsocket.databinding.ContentIndex4Binding

class fragment4() : Fragment() {
    private  var _binding: ContentIndex4Binding? = null
    private val binding get() = _binding!!
    private lateinit var act : IndexActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        act = activity as IndexActivity
        _binding = ContentIndex4Binding.inflate(inflater,container,false)
        binding.headIcon.setImageBitmap(BitmapFactory.decodeByteArray(act.user.headIcon,0,act.user.headIcon!!.size))
        binding.headIcon.setOnClickListener {
            act.bind.drawLayout.openDrawer(Gravity.LEFT)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}