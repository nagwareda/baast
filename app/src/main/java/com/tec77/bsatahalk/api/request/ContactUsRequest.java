package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 14/04/2018.
 */

public class ContactUsRequest {
    private String question;

    public ContactUsRequest() {
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
