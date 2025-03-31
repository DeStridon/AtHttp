//package com.destridon.athttp.client;
//
//
//import java.util.Map.Entry;
//
//import okhttp3.OkHttp;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.Request.Builder;
//import okhttp3.OkHttpClient;
//
//
//public class OkHttpAtHttpClient extends AtHttpClient{
//
//    @Override
//    public Response send(Request request) throws Exception {
//        
//        OkHttpClient client = new OkHttpClient();
//
//
//        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder().url(request.url);
//        for(Entry<String, String> header : request.headers.entrySet()) {
//            requestBuilder.addHeader(header.getKey(), header.getValue());
//        }
//        okhttp3.Request okHttpRequest = requestBuilder.build();
//        okhttp3.Response response = client.newCall(okHttpRequest).execute();
//        return new Response(response.code(), response.body().string());
//
//    }
//
//    
//
//}
