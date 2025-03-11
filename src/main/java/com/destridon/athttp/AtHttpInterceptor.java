package com.destridon.athttp;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpRequest;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.Empty;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.This;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;




import com.google.common.collect.Lists;




public class AtHttpInterceptor {

    private Map<String, String> globalVariables;
    private HttpClient httpClient = HttpClient.newHttpClient();


    public AtHttpInterceptor(Map<String, String> globalVariables) {
        this.globalVariables = globalVariables;
    }

    @RuntimeType
    public Object intercept(@This Object self, 
                                 @Origin Method method, 
                                 @AllArguments Object[] args, 
                                 @SuperMethod(nullIfImpossible = true) Method superMethod,
                                 @Empty Object defaultValue) throws Throwable {

        System.out.println("Intercepting method: " + method.getName());
       
        // Handle other interfaces that may represent other exchange contained in the same class
        if(method.getReturnType().getName().startsWith(method.getDeclaringClass().getName())) {
        	return AtHttp.generate(method.getReturnType(), globalVariables);
        }
        
        
        // Handle variables
        Map<String, String> variables = new HashMap<>(globalVariables);
        if(args != null) {
	        for(int i = 0; i < args.length; i++) {
	            Object arg = args[i];
	            if (arg != null) {
	                AtHttp.Variable variableAnnotation = method.getParameters()[i].getAnnotation(AtHttp.Variable.class);
	                if (variableAnnotation != null) {
	                    variables.put(variableAnnotation.value(), arg.toString());
	                }
	            }
	        }
        }
        
        
        // Get method from name
        VerbType httpMethod = Stream.of(VerbType.values())
            .filter(x -> method.getName().toUpperCase().startsWith(x.toString()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No HTTP method annotation found on " + method.getName()));

        
        // Get the HTTP method annotation and URL
        ArrayList<AtHttp.Path> exchanges = digPaths(method);

        // Generate URL by getting all exchanges annotations, and building path by reversing the list and joining with "/"
        String url = Lists.reverse(exchanges).stream()
            .map(x -> x.value())
            .collect(Collectors.joining("/"))
            .replaceAll("//", "/");
        
        
        // TODO : replace all variables
        url = injectVariables(url, variables);

        // Create and execute the HTTP request
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .method(httpMethod.toString(), HttpRequest.BodyPublishers.noBody());

        // Add headers
        List<AtHttp.Header> headers = digHeaders(method);
        for(AtHttp.Header header : headers) {
            requestBuilder.header(header.key(), injectVariables(header.value(), variables));
        }
        
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

        // Parse JSON response into return type using ObjectMapper with generic type information
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                response.body(), 
                mapper.getTypeFactory().constructType(method.getGenericReturnType())
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse response body", e);
        }


    }

    private static ArrayList<AtHttp.Path> digPaths(Method method) {
        ArrayList<AtHttp.Path> exchanges = new ArrayList<>();

        AtHttp.Path methodExchange = method.getAnnotation(AtHttp.Path.class);
        if (methodExchange != null) {
            exchanges.add(methodExchange);
        }

        Class<?> currentClass = method.getDeclaringClass();
        
        while (currentClass != null) {
            AtHttp.Path exchange = currentClass.getAnnotation(AtHttp.Path.class);
            if (exchange != null) {
                exchanges.add(exchange);
            }
            currentClass = currentClass.getEnclosingClass();
        }
        return exchanges;
    }

    private static ArrayList<AtHttp.Header> digHeaders(Method method) {
        ArrayList<AtHttp.Header> headers = new ArrayList<>();

        AtHttp.Header methodHeader = method.getAnnotation(AtHttp.Header.class);
        if(method.getAnnotationsByType(AtHttp.Header.class) != null) {
            headers.addAll(List.of(method.getAnnotationsByType(AtHttp.Header.class)));
        }

        Class<?> currentClass = method.getDeclaringClass();
        while (currentClass != null) {
            AtHttp.Header[] classHeaders = currentClass.getAnnotationsByType(AtHttp.Header.class);
            if (classHeaders != null) {
                headers.addAll(List.of(classHeaders));
            }
            currentClass = currentClass.getEnclosingClass();
        }
        return headers;

    }
        
    
    public static String injectVariables(String content, Map<String, String> variables) {
        for(Map.Entry<String, String> entry : variables.entrySet()) {
            content = content.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return content;
    }
    
    
    public static enum VerbType{
    	GET, POST, PUT, PATCH, DELETE, HEAD
    }

}
