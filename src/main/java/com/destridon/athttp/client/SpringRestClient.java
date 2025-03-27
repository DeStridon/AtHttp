package com.destridon.athttp.client;

import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class SpringRestClient extends AtHttpClient{
	
 	public AtHttpClient.Response send(AtHttpClient.Request request) throws Exception {
		
 		HttpHeaders headers = new HttpHeaders();
         for(Entry<String, String> header : request.headers.entrySet()) {
 			headers.set(header.getKey(), header.getValue());
 		}

 		RestTemplate restTemplate = new RestTemplate();

         HttpEntity<String> entity = new HttpEntity<>("parameters", headers);


         ResponseEntity<String> httpResponse = restTemplate.exchange(request.url, HttpMethod.GET, entity, String.class);


        Response response = new Response();
 		response.code = httpResponse.getStatusCode().value();
 		response.body = httpResponse.getBody();
 		return response;

 	}

}
