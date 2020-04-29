package com.yz.data.http;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestBodyHelper {
    private MediaType mediaType;
    private Gson gson;

    private RequestBodyHelper(String type) {
        mediaType = MediaType.parse(type);
        gson = new Gson();
    }

    public static RequestBodyHelper getInstance(String type) {
        return new RequestBodyHelper(type);
    }

    public RequestBody createObjectBody(Object o) {
        String json = gson.toJson(o);
        Logger.json(json);
        return RequestBody.create(mediaType, json);
    }

    public RequestBody getRequestBody(Map<String, Object> hashMap) {
        StringBuffer data = new StringBuffer();
        if (hashMap != null && hashMap.size() > 0) {
            Iterator iter = hashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                data.append(key).append("=").append(val).append("&");
            }
        }
        String jso = data.substring(0, data.length() - 1);
        Logger.i(jso);
        return  RequestBody.create(mediaType,jso);
    }

}
