package com.tec77.bsatahalk.api.response;

import java.util.ArrayList;

/**
 * Created by Nagwa on 05/03/2018.
 */

public class SchoolClassesResponse {

    public void SchoolClassesResponse(){

    }

    private boolean success;


    public boolean getSuccess() { return this.isSuccess(); }

    public void setSuccess(boolean success) { this.success = success; }

    private ArrayList<Category> category;

    public ArrayList<Category> getCategory() { return this.category; }

    public void setCategory(ArrayList<Category> category) { this.category = category; }

    private int code;

    public int getCode() { return this.code; }

    public void setCode(int code) { this.code = code; }

    public boolean isSuccess() {
        return success;
    }




    public class Category
    {

        public void Category(){

        }
        private int id;

        public int getId() { return this.id; }

        public void setId(int id) { this.id = id; }

        private String name;

        public String getName() { return this.name; }

        public void setName(String name) { this.name = name; }

        private ArrayList<PartModelYear> year;

        public ArrayList<PartModelYear> getYear() { return this.year; }

        public void setYear(ArrayList<PartModelYear> year) { this.year = year; }
    }

}
