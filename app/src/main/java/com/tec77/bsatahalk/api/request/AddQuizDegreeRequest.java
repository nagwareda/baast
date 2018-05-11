package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 28/03/2018.
 */

public class AddQuizDegreeRequest {

    private int user_id;
    private int quiz_id;
    private float degree;

    public AddQuizDegreeRequest() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }
}
