package com.dmsegypt.dms.rest.model;

/**
 * Created by mahmoud on 1/01/18.
 */

public class ReasonsCheck {
    String name;
    int value;

    ReasonsCheck(String name, int value){
        this.name = name;
        this.value = value;
    }
    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }
}
