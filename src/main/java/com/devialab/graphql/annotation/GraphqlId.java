package com.devialab.graphql.annotation;

import java.lang.annotation.*;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface GraphqlId {
}
