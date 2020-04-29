package com.yz.baiduai.common.http;

import com.google.gson.JsonParseException;
import com.yz.data.http.ApiExecption;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

public class HttpErrorInfo {

    //对应 HTTP 的状态码
    private static final int PARAMETER_ERROR = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int NOT_SUPPORT_HTTP = 505;

    private static final String LOGIN = "545";

    public static void getErrorInfo(Throwable e, HttpErrorCallBack callBack) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case PARAMETER_ERROR:
                    callBack.showError("服务器不理解请求的语法。");
                    break;
                case UNAUTHORIZED:
                    callBack.showError("当前请求需要用户验证");
                    break;
                case FORBIDDEN:
                    callBack.showError("服务器已经理解请求，但是拒绝执行它");
                    break;
                case NOT_FOUND:
                    callBack.showError("404找不到请求");
                    break;
                case REQUEST_TIMEOUT:
                    callBack.showError("请求超时");
                    break;
                case GATEWAY_TIMEOUT:
                    callBack.showError("作为网关或者代理工作的服务器尝试执行请求时，未能及时从上游服务器（URI 标识出的服务器，例如 HTTP、FTP、LDAP）或者辅助服务器（例如 DNS）收到响应");
                    break;
                case INTERNAL_SERVER_ERROR:
                    callBack.showError("服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理");
                    break;
                case BAD_GATEWAY:
                    callBack.showError("作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应");
                    break;
                case SERVICE_UNAVAILABLE:
                    callBack.showError("由于临时的服务器维护或者过载，服务器当前无法处理请求");
                    break;
                case NOT_SUPPORT_HTTP:
                    callBack.showError("服务器不支持请求中所用的 HTTP 协议版本");
                    break;
                default:
                    callBack.showError("网络错误");  //其它均视为网络错误
                    break;
            }
        } else if (e instanceof ApiExecption) {
            ApiExecption execption = (ApiExecption) e;
            if (LOGIN.equals(execption.getErrorCode())) {
                callBack.onLogin(execption.getMessage());
            } else {
                callBack.showError(execption.getMessage());
            }
        } else if (e instanceof ConnectException) callBack.showError("连接失败");
        else if (e instanceof JsonParseException || e instanceof JSONException) {
            callBack.showError("解析失败");
        } else if (e instanceof SSLHandshakeException) callBack.showError("证书验证失败");
        else if (e instanceof UnknownHostException) callBack.showError("DNS解析错误");
        else if (e instanceof SocketTimeoutException || e instanceof TimeoutException)
            callBack.showError("连接超时");
        else {
            e.printStackTrace();
            callBack.showError("未知错误");
        }

    }
}
