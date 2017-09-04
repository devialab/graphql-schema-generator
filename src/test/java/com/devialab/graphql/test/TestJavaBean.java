package com.devialab.graphql.test;

import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
public class TestJavaBean {

    private String string;
    private int intValue;
    private Integer integerValue;
    private long longValue;
    private short shortValue;
    private byte byteValue;
    private float floatValue;
    private double doubleValue;
    private boolean booleanValue;
    private String notNullableString;
    private final String inmutableString;
    private List<String> javaList;
    private String[] javaArray;
    private TestJavaWrapper<String> javaWrapper;
    private TestJavaEnum javaEnum;

    private String privateField;

    public TestJavaBean(String inmutableString) {
        this.inmutableString = inmutableString;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }


    public String getNotNullableString() {
        return notNullableString;
    }

    @NotNull
    public void setNotNullableString(String notNullableString) {
        this.notNullableString = notNullableString;
    }

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public void setByteValue(byte byteValue) {
        this.byteValue = byteValue;
    }

    public String getInmutableString() {
        return inmutableString;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public List<String> getJavaList() {
        return javaList;
    }

    public void setJavaList(List<String> javaList) {
        this.javaList = javaList;
    }

    public String[] getJavaArray() {
        return javaArray;
    }

    public void setJavaArray(String[] javaArray) {
        this.javaArray = javaArray;
    }

    public TestJavaWrapper<String> getJavaWrapper() {
        return javaWrapper;
    }

    public void setJavaWrapper(TestJavaWrapper<String> javaWrapper) {
        this.javaWrapper = javaWrapper;
    }

    public TestJavaEnum getJavaEnum() {
        return javaEnum;
    }

    public void setJavaEnum(TestJavaEnum javaEnum) {
        this.javaEnum = javaEnum;
    }
}
