package com.destridon.athttp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class AtHttp {
    
    

    @SuppressWarnings("unchecked")
    public static <T> T generate(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[] { interfaceClass },
            new HttpInvocationHandler()
        );
    }

    public static class HttpInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Handle Object methods like toString(), equals(), etc.
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            // Get the HTTP method annotation and URL
            String url = "";
            VerbType httpMethod = null;
            String path = "";
            
            httpMethod = Stream.of(VerbType.values())
            		.filter(x -> method.getName().toUpperCase().startsWith(x.toString()))
            		.findFirst()
            		.orElseThrow(() -> new IllegalStateException("No HTTP method annotation found on " + method.getName()));

            // Build the full URL
            String fullUrl = url + path;

            // Create and execute the HTTP request
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .method(httpMethod.toString(), HttpRequest.BodyPublishers.noBody());

            HttpClient httpClient = HttpClient.newHttpClient();
            
            HttpResponse<String> response = httpClient.send(
                requestBuilder.build(),
                HttpResponse.BodyHandlers.ofString()
            );

            // Handle the response based on the method's return type
            Class<?> returnType = method.getReturnType();
            if (returnType == String.class) {
                return response.body();
            } else if (returnType == void.class) {
                return null;
            } else if (returnType == int.class || returnType == Integer.class) {
                return response.statusCode();
            }
            
            // Add more return type handling as needed
            throw new UnsupportedOperationException("Unsupported return type: " + returnType);
        }
    }
    
    public static enum VerbType{
    	GET, POST, PUT, PATCH, DELETE, HEAD
    }
}
