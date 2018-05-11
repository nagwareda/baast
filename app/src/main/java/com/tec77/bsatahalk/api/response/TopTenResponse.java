package com.tec77.bsatahalk.api.response;

import java.util.ArrayList;

/**
 * Created by Nagwa on 25/02/2018.
 */

public class TopTenResponse {
    private boolean success;


    public boolean getSuccess() {
        return this.isSuccess();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private ArrayList<TopTenModel> topTen;

    public ArrayList<TopTenModel> getTopTen() {
        return this.topTen;
    }

    public void setTopTen(ArrayList<TopTenModel> topTen) {
        this.topTen = topTen;
    }

    private int code;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public class TopTenModel {


        public void TopTenModel() {
        }

        private String degree;

        public String getDegree() {
            return this.degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        private String quizNumber;

        public String getQuizNumber() {
            return this.quizNumber;
        }

        public void setQuizNumber(String quizNumber) {
            this.quizNumber = quizNumber;
        }

        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String user_image;

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }
    }
}


