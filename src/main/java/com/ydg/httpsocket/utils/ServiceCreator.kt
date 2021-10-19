package com.ydg.httpsocket.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val BASE_URL = "http://119.29.163.199:8080/GuoGuoQQ/"
    private  final var client = OkHttpClient.Builder().
                        connectTimeout(60, TimeUnit.SECONDS).
                        readTimeout(60, TimeUnit.SECONDS).
                        writeTimeout(60, TimeUnit.SECONDS).build();
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> create(serviceClass:Class<T>):T= retrofit.create(serviceClass)
    inline fun <reified T> create():T= create(T::class.java)
}