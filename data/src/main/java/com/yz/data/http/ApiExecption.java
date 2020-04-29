package com.yz.data.http;

/**
 * 捕获异常
 * 将服务器返回的msg加入ApiExecption的msg中
 * 也可以根据和服务器约定好的errorCode来判断
 * Created by 残梦 on 2018/3/30.
 */

public class ApiExecption extends RuntimeException {

    private String errorCode;

     public ApiExecption(String code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
