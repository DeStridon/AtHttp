// package com.destridon.athttp.client;


// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;

// import java.util.Map.Entry;


// public class JavaNetHttpClient extends AtHttpClient {

// 	HttpClient httpClient = HttpClient.newHttpClient();
	
// 	public Response send(Request request) throws Exception {
		
// 		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
// 	            .uri(URI.create(request.url))
// 	            .method(request.method, HttpRequest.BodyPublishers.noBody());
		
// 		for(Entry<String, String> header : request.headers.entrySet()) {
// 			requestBuilder.header(header.getKey(), header.getValue());
// 		}

// 		HttpResponse<String> httpResponse = httpClient.send(
//             requestBuilder.build(),
//             HttpResponse.BodyHandlers.ofString()
//         );
		
// 		Response response = new Response();
// 		response.code = httpResponse.statusCode();
// 		response.body = httpResponse.body();
// 		return response;
// 	}

// }
