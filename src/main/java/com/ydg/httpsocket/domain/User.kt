package com.ydg.httpsocket.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(var username: String?,var password: String?,val headIcon : ByteArray?,val perSign:String?,val clientId:String) : Parcelable{
    override fun toString(): String {
        return "User(username=$username, password=$password, headIcon=$headIcon, perSign=$perSign, clientId='$clientId')"
    }
}