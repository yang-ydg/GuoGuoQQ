package com.ydg.httpsocket.service

import com.ydg.httpsocket.domain.ResultVO
import com.ydg.httpsocket.domain.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface HttpRequestService {
    @POST("user/checkUser")
    fun checkUser(@Body user:User) : Call<ResultVO<User>>
    @POST("user/getFriendsList")
    fun getFriendsList(@Body clientId : String) : Call<ResultVO<List<User>>>
    @POST("user/registerUser")
    fun registerUser(@Body user : User) : Call<ResultVO<Any?>>
}