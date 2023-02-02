package com.qy.groovy.demo;

public class TestRun {

    public String invoke(String name,Integer count){
        for (int i = 0; i < count; i++) {
            name += ("_"+count);
        }
        return name;
    }

}
