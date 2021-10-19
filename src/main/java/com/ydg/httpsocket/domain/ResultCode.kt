package com.ydg.httpsocket.domain

enum class ResultCode(var code: Int, var msg: String) {
    SUCCESS(1000, "操作成功"),
    FAILURE(1001, "响应失败"),
    VALIDATE_FAILURE(1002, "参数校验错误"),
    USER_NOTEXIST(1003, "用户名不存在"),
    AUTH_ERROR(1004, "用户名或密码错误"),
    USER_EXISTED(1005,"用户名已存在"),
    ERROR(5000, "未知错误");

}