package com.tec77.bsatahalk.api.response;

/**
 * Created by Nagwa on 05/03/2018.
 */

public class PartModelYear {


    public void Year() {
    }

    public void Year(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
