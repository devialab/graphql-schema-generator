package com.devialab.graphql.test;

import com.devialab.graphql.annotation.GraphqlField;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@GraphqlField(value = "static", type = "String!")
public class TestJavaStaticFieldDefinition implements TestJavaInterface {

    private final String dynamic;

    public TestJavaStaticFieldDefinition(String dynamic) {
        this.dynamic = dynamic;
    }

    public String getDynamic() {
        return dynamic;
    }
}
