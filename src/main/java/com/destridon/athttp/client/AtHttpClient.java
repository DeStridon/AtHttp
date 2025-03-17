package com.destridon.athttp.client;

import java.util.Map;


public abstract class AtHttpClient {

	
	public abstract Response send(Request request) throws Exception;

	public static class Request{
		public String method;
		public String url;
		public Map<String, String> headers;
		public String body;
	}
	
	public static class Response{
		public int code;
		public String body;
	}

}
