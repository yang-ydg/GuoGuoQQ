package com.ydg.httpsocket.domain

class Msg(val clientId:String, val toClientId:String, val content: String, var type: Int) {
    companion object {
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
    }
}