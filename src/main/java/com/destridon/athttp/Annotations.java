
package com.destridon.athttp;
public class Annotations {


    public @interface HttpExchange {
        String value() default "";
        String accept() default "";
    }
   


    public @interface PathVariable {
        String value() default "";
    }

    public @interface RequestParam {
        String value() default "";
    }

    public @interface Body {
        String value() default "";
    }
}