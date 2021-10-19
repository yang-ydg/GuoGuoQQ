package com.ydg.httpsocket.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydg.httpsocket.R
import com.ydg.httpsocket.activity.ChatActivity
import com.ydg.httpsocket.activity.IndexActivity
import com.ydg.httpsocket.databinding.ContentIndex2Binding
import com.ydg.httpsocket.domain.*
import com.ydg.httpsocket.service.HttpRequestService
import com.ydg.httpsocket.service.MqttService
import com.ydg.httpsocket.utils.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class fragment2() : Fragment() {
    var friendsList = ArrayList<User>()
    private  var _binding: ContentIndex2Binding? = null
    private val binding get() = _binding!!
    private lateinit var pageAdapter : GroupRecyclerAdapter
    private lateinit var friendsAdapter : FriendsRecyclerAdapter
    private var pageList = ArrayList<GroupItem>()
    private lateinit var act : IndexActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        act = activity as IndexActivity
        _binding = ContentIndex2Binding.inflate(inflater,container,false)
        binding.headIcon.setImageBitmap(BitmapFactory.decodeByteArray(act.user.headIcon,0,act.user.headIcon!!.size))
        initPageList()
        initFriendsList()
        var layoutManager = LinearLayoutManager(activity)
        binding.friendsListRecycler.layoutManager = layoutManager
        friendsAdapter = FriendsRecyclerAdapter(friendsList,activity!!)
        friendsAdapter.addOnItemClickListener(object : FriendsRecyclerAdapter.OnItemClickListener{
            override fun onClick(holder: FriendsRecyclerAdapter.FriendsViewHolder) {
                val chat = friendsList[holder.adapterPosition]
                if (!isContainChatList(chat)){
                    act.frag1.chatList.add(chat)
                    act.frag1.adapter.notifyDataSetChanged()
                    act.frag1.writeInFile()
                }
                val intent = Intent(activity!!, ChatActivity::class.java)
                intent.putExtra("toClientId",chat.clientId)
                intent.putExtra("chatName",chat.username)
                intent.putExtra("friendHeadIcon",chat.headIcon)
                intent.putExtra("myHeadIcon",act.user.headIcon)
                startActivity(intent)
            }

        })
        binding.friendsListRecycler.adapter = friendsAdapter

        val layoutManager1 = LinearLayoutManager(activity)
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        binding.groupRecycler.layoutManager = layoutManager1
        pageAdapter = GroupRecyclerAdapter(pageList,activity!!)
        pageAdapter.addOnItemClickListener(object : GroupRecyclerAdapter.OnItemClickListener{
            override fun onClick(holder: GroupRecyclerAdapter.GroupRecyclerViewHolder) {
                for ( i in 0 until pageList.size){
                    if (i == holder.adapterPosition) pageList[i].selected = true
                    else pageList[i].selected = false
                }
                pageAdapter.notifyDataSetChanged()
            }
        })
        binding.groupRecycler.adapter = pageAdapter
        binding.headIcon.setOnClickListener {
            act.bind.drawLayout.openDrawer(Gravity.LEFT)
        }
        binding.addFriends.setOnClickListener{

        }
        binding.newFriends.setOnClickListener {

        }
        binding.chatNotify.setOnClickListener {

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initPageList(){
        pageList.addAll(arrayOf(GroupItem("好友",true),GroupItem("分组",false),GroupItem("群聊",false),GroupItem("设备",false),GroupItem("通讯录",false),GroupItem("订阅号",false)))
    }

    private fun initFriendsList(){
        val request = ServiceCreator.create<HttpRequestService>()
        request.getFriendsList(MqttService.clientId).enqueue(object : Callback<ResultVO<List<User>>>{
            override fun onResponse(
                call: Call<ResultVO<List<User>>>,
                response: Response<ResultVO<List<User>>>
            ) {
                val list = response.body()?.data as ArrayList<User>
                for (i in list){
                    friendsList.add(User(i.username,i.password,i.headIcon,i.perSign,i.clientId))
                }
                binding.friendsListRecycler.adapter!!.notifyDataSetChanged()
            }
            
            override fun onFailure(call: Call<ResultVO<List<User>>>, t: Throwable) {
                Log.i("ydgper", t.message ?: "")
                Toast.makeText(activity,"获取好友列表失败!",Toast.LENGTH_LONG).show()
            }

        })
    }

    //判断聊天列表是否包含当前聊天窗口
    private fun isContainChatList(chat:User):Boolean{
        var flag = false
        for (i in act.frag1.chatList){
            if (i.clientId == chat.clientId) flag = true
        }
        return flag
    }
}