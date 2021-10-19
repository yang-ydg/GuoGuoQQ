package com.ydg.httpsocket.domain

class SendUser(var username: String?,var password: String?,val headIcon : ByteArray,val lastMsg:String?,val clientId:String) {
    override fun toString(): String {
        return "User(username=$username, password=$password, headIcon=$headIcon, lastMsg=$lastMsg, clientId='$clientId')"
    }
}