package com.devialab.graphql.annotation;


import java.lang.annotation.*;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Repeatable(GraphqlFields.class)
@Inherited
public @interface GraphqlField {
    String value();
    String type() default NULL;

    String NULL = "_null";
}
