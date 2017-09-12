package com.devialab.graphql.test;

import com.devialab.graphql.annotation.GraphqlField;
import com.devialab.graphql.annotation.GraphqlId;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
public class TestJavaBaseClass {

    @GraphqlId
    @GraphqlField("id")
    public String getUri() {
        return "uri";
    }
}
