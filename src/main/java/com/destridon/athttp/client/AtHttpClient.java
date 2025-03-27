package com.destridon.athttp.client;

import java.util.Map;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public abstract class AtHttpClient {

	
	public abstract Response send(Request request) throws Exception;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request{
		public String method;
		public String url;
		public Map<String, String> headers;
		public String body;
	}
	

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response{
		public int code;
		public String body;
	}

}
