package com.tec77.bsatahalk.api.response.Quiz;

import java.util.ArrayList;

/**
 * Created by Nagwa on 15/03/2018.
 */

public class QuizResponse {

    public QuizResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    private boolean success;

    public boolean getSuccess() { return this.success; }

    public void setSuccess(boolean success) { this.success = success; }

    private ArrayList<QuizModel> quiz;

    public ArrayList<QuizModel> getQuiz() { return this.quiz; }

    public void setQuiz(ArrayList<QuizModel> quiz) { this.quiz = quiz; }

    private int code;

    public int getCode() { return this.code; }

    public void setCode(int code) { this.code = code; }
}
