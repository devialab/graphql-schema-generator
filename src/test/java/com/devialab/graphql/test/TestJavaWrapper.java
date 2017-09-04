package com.devialab.graphql.test;

import com.devialab.graphql.annotation.WrapperGenericType;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@WrapperGenericType
public class TestJavaWrapper<T> {
    private final T delegate;

    public TestJavaWrapper(T delegate) {
        this.delegate = delegate;
    }
}
