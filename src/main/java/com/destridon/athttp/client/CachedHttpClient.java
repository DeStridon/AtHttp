package com.destridon.athttp.client;

import java.util.HashMap;
import java.util.Map;

public class CachedHttpClient <U extends AtHttpClient> extends AtHttpClient {

    private U client;

    public CachedHttpClient(U client) {
        this.client = client;
    }

	Map<String, AtHttpClient.Response> cache = new HashMap<>();

	public U.Response send(U.Request request) throws Exception {

        if(request.method.equals("GET")){
            String cacheKey = request.url;
            AtHttpClient.Response response = cache.get(cacheKey);
            if (response != null) {
                return response;
            }
        }

        AtHttpClient.Response response = client.send(request);

        if(request.method.equals("GET")){
            cache.put(request.url, response);
        }
    
        return response;
    }


}
