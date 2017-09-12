package com.devialab.graphql.test;

import com.devialab.graphql.annotation.GraphqlField;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
public interface TestJavaInterface {

    @GraphqlField(value = "annotated")
    default String getInherited() {
        return "getInherited";
    }

}
