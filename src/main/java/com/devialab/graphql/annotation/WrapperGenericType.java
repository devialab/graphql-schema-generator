package com.devialab.graphql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is user to indicate the the Graphql Schema Generation that the java type is not to be used in
 * the Graphql Schema but one of its generic type instead.
 *
 * This is only useful for very specific cases where you have wrappers (e.g. proxies) in your java graph
 *
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WrapperGenericType {
    int typeArgument() default 0;
}
