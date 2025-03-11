package com.destridon.athttp;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.*;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveTypeAwareAssigner;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

import net.bytebuddy.utility.JavaModule;



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
   

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Variable {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestParam {
        String value() default "";
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestBody {
        String value() default "";
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
