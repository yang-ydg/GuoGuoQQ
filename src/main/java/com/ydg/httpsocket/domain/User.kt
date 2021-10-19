package com.ydg.httpsocket.domain

import java.sql.Blob

class User(var username: String?,var password: String?,val headIcon : ByteArray?,val perSign:String?,val clientId:String) {
    override fun toString(): String {
        return "User(username=$username, password=$password, headIcon=$headIcon, perSign=$perSign, clientId='$clientId')"
    }
}