package com.tec77.bsatahalk.api.response;

import java.io.Serializable;

/**
 * Created by Nagwa on 24/02/2018.
 */

public class PartModelStudent implements Serializable {
    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String email;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    private String phone;

    public String getPhone() { return this.phone; }

    public void setPhone(String phone) { this.phone = phone; }

    private int number_quizes;

    public int getNumberQuizes() { return this.number_quizes; }

    public void setNumberQuizes(int number_quizes) { this.number_quizes = number_quizes; }

    private int total_degree;

    public int getTotalDegree() { return this.total_degree; }

    public void setTotalDegree(int total_degree) { this.total_degree = total_degree; }

    private String token;

    public String getToken() { return this.token; }

    public void setToken(String token) { this.token = token; }

    private String user_image;

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
