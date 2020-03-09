package com.finder.web.configuration;

public class Example {
    private int i;
    public Example(int i) throws Exception {
        this.i=i;
        throw new Exception();
        //this.i=i; unreachable
    }

    public static void main(String[] args){
        Example example = null;
        try {
            example = new Example(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(example.i); //example java.lang.NullPointerException
    }
}
