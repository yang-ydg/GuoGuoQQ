package com.ydg.httpsocket.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import com.ydg.httpsocket.utils.LogUtil
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ydg.httpsocket.R
import com.ydg.httpsocket.activity.ChatActivity
import com.ydg.httpsocket.activity.IndexActivity
import com.ydg.httpsocket.components.Mypopup
import com.ydg.httpsocket.databinding.ContentIndex1Binding
import com.ydg.httpsocket.databinding.PopupLayoutBinding
import com.ydg.httpsocket.domain.ChatAdapter
import com.ydg.httpsocket.domain.User


class fragment1() : Fragment(), View.OnClickListener {
    var chatList= ArrayList<User>()
    lateinit var adapter: ChatAdapter
    lateinit var  prefs : SharedPreferences
    private var _binding: ContentIndex1Binding? = null
    private val binding get() = _binding!!
    private lateinit var act : IndexActivity

    override fun onAttach(context: Context) {
        LogUtil.i("fragment1","onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.i("fragment1","onCreate")
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LogUtil.i("fragment1","onCreateView")
        act = activity as IndexActivity
        prefs = activity!!.getSharedPreferences(act.user.username, Context.MODE_PRIVATE)
        initChatList()
        _binding = ContentIndex1Binding.inflate(inflater,container,false)
        binding.headIcon.setImageBitmap(BitmapFactory.decodeByteArray(act.user.headIcon,0,act.user.headIcon!!.size))
        binding.username.text= act.user.username

        //构建chatList的recyclerView
        adapter = ChatAdapter(chatList)
        adapter.addOnItemClickListener(object : ChatAdapter.OnItemClickListener{
            override fun click(holder: ChatAdapter.ChatViewHolder) {
                val chat = chatList[holder.adapterPosition]
                val intent = Intent(activity!!,ChatActivity::class.java)
                intent.putExtra("toClientId",chat.clientId)
                intent.putExtra("chatName",chat.username)
                intent.putExtra("friendHeadIcon",chat.headIcon)
                intent.putExtra("myHeadIcon",act.user.headIcon)
                startActivity(intent)
            }

        })
        adapter.addOnItenLongClickListener(object : ChatAdapter.OnItemLongClickListener{
            override fun longClick(holder: ChatAdapter.ChatViewHolder) {
                var chatPopup = PopupMenu(context,holder.chatNameText)
                chatPopup.menuInflater.inflate(R.menu.chatlist_menu,chatPopup.menu)
                chatPopup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.chatPopupDelete ->{
                            chatList.removeAt(holder.adapterPosition)
                            adapter.notifyItemRemoved(holder.adapterPosition)
                            writeInFile()
                        }
                    }
                    false
                }
                chatPopup.show()
            }

        })
        val layoutManager = LinearLayoutManager(activity)
        binding.chatRecycler.layoutManager = layoutManager
        binding.chatRecycler.adapter = adapter


        binding.headIcon.setOnClickListener(this)
        binding.popupAdd.setOnClickListener(this)
        return binding.root
    }

    //将聊天列表写入记录文件
    fun writeInFile(){
        val gson : Gson = Gson()
        val listString : String = gson.toJson(chatList)
        prefs.edit {
            putString(act.user.username,listString)
            apply()
        }
    }

    override fun onDestroyView() {
        LogUtil.i("fragment1","onDestroyView")
        writeInFile()
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        LogUtil.i("fragment1","onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        LogUtil.i("fragment1","onDetach")
        super.onDetach()
    }

    override fun onClick(p0: View) {
        when(p0.id){
            binding.headIcon.id ->act.bind.drawLayout.openDrawer(Gravity.LEFT)
            binding.popupAdd.id ->{
                var mypopup = Mypopup(this.activity!!, PopupLayoutBinding.inflate(LayoutInflater.from(this.activity)))
                mypopup.showAsDropDown(p0,-150,45)
            }
        }
    }

    private fun initChatList(){
        val listString: String? = prefs.getString(act.user.username,"")
        val gson : Gson = Gson()
        if (listString != "" && listString !=null){
            chatList = gson.fromJson(listString,object : TypeToken<ArrayList<User>>(){}.type)
        }
    }

}