package com.yz.data.http;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 残梦 网络请求配置
 * Created by dell on 2018/3/27.
 */

public class RetofitHttp {

    private static String token;

    private static final int DEFAULT_TIMEOUT = 20;
    private static final int DEFAULT_LONG_TIMEOUT = 5;
    private static int time = DEFAULT_TIMEOUT;

    private Retrofit retrofit;
    private static RetofitHttp retofitHttp;
    private static RetofitHttp longTimeRetofitHttp;

    private LoggingInterceptor loggingInterceptor;

    private static RetrofitService retrofitService;

    private OkHttpClient client = new OkHttpClient();

    private RetofitHttp(boolean isLongTime) {
        retrofit = new Retrofit.Builder()
                .baseUrl(HttpMethods.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(ResponseConvertFactory.create()) // 使用Gson作为数据转换器
                .client(getClient(isLongTime))
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public static RetrofitService getInstance() {
        if (retofitHttp == null) {
            synchronized (RetofitHttp.class) {
                if (retofitHttp == null) {
                    time = DEFAULT_TIMEOUT;
                    retofitHttp = new RetofitHttp(false);
                }
            }
        }
        return retrofitService;
    }

    public static RetrofitService getLongTimeInstance() {
        if (longTimeRetofitHttp == null) {
            synchronized (RetofitHttp.class) {
                if (longTimeRetofitHttp == null) {
                    time = DEFAULT_LONG_TIMEOUT;
                    longTimeRetofitHttp = new RetofitHttp(true);
                }
            }
        }
        return retrofitService;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 网络连接设置
     *
     * @return OkHttpClient
     */
    private OkHttpClient getClient(boolean isLongTime) {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{getTrustManager()}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            client = new OkHttpClient.Builder().proxy(Proxy.NO_PROXY).addInterceptor(new Interceptor() {
                @SuppressLint("DefaultLocale")
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
                  /*  if (TextUtils.isEmpty(token)) {
                        builder.removeHeader("token");
                    } else {
                        builder.addHeader("token", token);
                    }*/
                    Request request = builder.build();
                    return chain.proceed(request);
                }
            }).addInterceptor(getLoggingInterceptor())
                    .connectTimeout(time, TimeUnit.SECONDS)
                    .writeTimeout(time, TimeUnit.SECONDS)
                    .readTimeout(time, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory, Objects.requireNonNull(getTrustManager()))//证书认证 使用HTTPS协议
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * 证书认证 使用HTTPS协议
     *
     * @return X509TrustManager
     */
    private X509TrustManager getTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LoggingInterceptor getLoggingInterceptor() {
        if (loggingInterceptor == null) loggingInterceptor = new LoggingInterceptor();
        return loggingInterceptor;
    }

    public class LoggingInterceptor implements Interceptor {
        @SuppressLint("DefaultLocale")
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            //Chain 里包含了request和response
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
            Logger.i(String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            //不能直接使用response.body（）.string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，
            // 我们需要创建出一个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Logger.i(String.format("接收响应：[%s] %n %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()

            ));
            Logger.json(responseBody.string());
            return response;
        }
    }

    public static void initToken(String token) {
        RetofitHttp.token = token;
    }

    public static void cleanToken() {
        RetofitHttp.token = null;
    }


}
