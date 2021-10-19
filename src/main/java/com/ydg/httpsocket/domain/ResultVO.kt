package com.ydg.httpsocket.domain

class ResultVO<T> {
    var code: Int
    var msg: String
    var data: T?

    constructor(data: T) : this(ResultCode.SUCCESS, data) {}
    constructor(code: Int, msg: String, data: T) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    override fun toString(): String {
        return "ResultVO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}'
    }

    constructor(resultCode: ResultCode, data: T) {
        this.code = resultCode.code
        msg = resultCode.msg
        this.data = data
    }
}