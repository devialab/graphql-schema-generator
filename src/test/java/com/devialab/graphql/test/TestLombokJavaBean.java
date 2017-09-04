package com.devialab.graphql.test;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@Data
@Builder
public class TestLombokJavaBean {
    private String string;

    @NonNull
    private String notNullableString;
}
