package com.learn.crud;

public class Learn {
    private final char value[];

    public Learn(Learn original) {
        this.value = original.value;
    }

    public Learn(){
        this.value = new char[0];
    }

    public void test(){
        Learn learn = new Learn();
        char value2[] = learn.value;
    }
}
