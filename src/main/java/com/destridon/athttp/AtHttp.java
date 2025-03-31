package com.destridon.athttp;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

import java.lang.reflect.Method;

import java.util.Map;

import net.bytebuddy.ByteBuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import net.bytebuddy.implementation.*;

import net.bytebuddy.matcher.ElementMatchers;




public class AtHttp {
    
    

    @SuppressWarnings("unchecked")
    public static <T> T generate(Class<T> interfaceClass, Map<String, String> variables) {
        try {
            System.out.println("Generating proxy for: " + interfaceClass.getName());
            
            Constructor[] constructors = interfaceClass.getDeclaredConstructors();
            for (Constructor c : constructors) {
                System.out.println("Found constructor: " + c.getName());
            }

            for (Method m : interfaceClass.getDeclaredMethods()) {
                System.out.println("Found method: " + m.getName());
            }



            return new ByteBuddy()
                .subclass(interfaceClass)
                .method(ElementMatchers.isAbstract())
                .intercept(MethodDelegation.to(new AtHttpInterceptor(variables)))
                .make()
                .load(interfaceClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate proxy", e);
        }
    }



    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Path {
        String value() default "";
        String accept() default "";
    }
   


    /* Enables user to specify the request parameters */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestParam {
        String defaultValue();
        String alias() default "";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestBody {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Headers.class)
    public @interface Header {
        String key() default "";
        String value() default "";
    }

    @Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Headers {
		Header[] value();
	}

    
}
